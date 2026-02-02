import json
import re
import sqlite3
from pathlib import Path
from html import unescape
from typing import Optional, Tuple, List, Dict, Any


DB_PATH = Path("substitutions.db")


# ---------------------------
# DB & schema
# ---------------------------

def get_conn() -> sqlite3.Connection:
    conn = sqlite3.connect(DB_PATH)
    conn.execute("PRAGMA foreign_keys = ON;")
    return conn


def reset_schema(conn: sqlite3.Connection) -> None:
    """
    Drop and recreate ONLY the derived tables.
    The original 'substitutions' table is kept as raw source.
    """
    cur = conn.cursor()
    cur.executescript(
        """
        DROP TABLE IF EXISTS entry_class;
        DROP TABLE IF EXISTS substitution_entry;
        DROP TABLE IF EXISTS message;
        DROP TABLE IF EXISTS teacher;
        DROP TABLE IF EXISTS room;
        DROP TABLE IF EXISTS class;
        DROP TABLE IF EXISTS subject;

        CREATE TABLE teacher (
            id   INTEGER PRIMARY KEY AUTOINCREMENT,
            code TEXT NOT NULL UNIQUE
        );

        CREATE TABLE room (
            id   INTEGER PRIMARY KEY AUTOINCREMENT,
            code TEXT NOT NULL UNIQUE
        );

        CREATE TABLE class (
            id   INTEGER PRIMARY KEY AUTOINCREMENT,
            code TEXT NOT NULL UNIQUE
        );

        CREATE TABLE subject (
            id   INTEGER PRIMARY KEY AUTOINCREMENT,
            code TEXT NOT NULL UNIQUE
        );

        CREATE TABLE substitution_entry (
            id                INTEGER PRIMARY KEY AUTOINCREMENT,
            substitution_id   INTEGER NOT NULL,
            row_index         INTEGER NOT NULL,

            group_label       TEXT,
            period_raw        TEXT,
            period_start      INTEGER,
            period_end        INTEGER,
            time_start        TEXT,
            time_end          TEXT,
            subject_raw       TEXT,
            info_flag         TEXT,
            comment           TEXT,

            is_cancelled      INTEGER NOT NULL DEFAULT 0,
            is_room_change    INTEGER NOT NULL DEFAULT 0,
            is_teacher_change INTEGER NOT NULL DEFAULT 0,

            room_new_id       INTEGER,
            room_old_id       INTEGER,
            teacher_new_id    INTEGER,
            teacher_absent_id INTEGER,

            FOREIGN KEY (substitution_id)
                REFERENCES substitutions(id) ON DELETE CASCADE,
            FOREIGN KEY (room_new_id)
                REFERENCES room(id),
            FOREIGN KEY (room_old_id)
                REFERENCES room(id),
            FOREIGN KEY (teacher_new_id)
                REFERENCES teacher(id),
            FOREIGN KEY (teacher_absent_id)
                REFERENCES teacher(id)
        );

        CREATE UNIQUE INDEX idx_substitution_entry_day_row
          ON substitution_entry (substitution_id, row_index);

        CREATE TABLE entry_class (
            entry_id INTEGER NOT NULL,
            class_id INTEGER NOT NULL,
            PRIMARY KEY (entry_id, class_id),
            FOREIGN KEY (entry_id)
                REFERENCES substitution_entry(id) ON DELETE CASCADE,
            FOREIGN KEY (class_id)
                REFERENCES class(id)
        );

        CREATE TABLE message (
            id              INTEGER PRIMARY KEY AUTOINCREMENT,
            substitution_id INTEGER NOT NULL,
            message_index   INTEGER NOT NULL,
            text            TEXT,
            FOREIGN KEY (substitution_id)
                REFERENCES substitutions(id) ON DELETE CASCADE
        );
        """
    )
    conn.commit()


# ---------------------------
# Lookup helpers
# ---------------------------

def get_or_create_code(conn: sqlite3.Connection, table: str, code: Optional[str]) -> Optional[int]:
    """
    Insert or fetch id for a given code in a lookup table (teacher/room/class/subject).
    Returns None if code is empty/None.
    """
    if not code:
        return None

    code = code.strip()
    if not code:
        return None

    cur = conn.cursor()
    cur.execute(f"SELECT id FROM {table} WHERE code = ?;", (code,))
    row = cur.fetchone()
    if row:
        return row[0]

    cur.execute(f"INSERT INTO {table} (code) VALUES (?);", (code,))
    conn.commit()
    return cur.lastrowid


# ---------------------------
# Parsing helpers
# ---------------------------

TAG_RE = re.compile(r"<[^>]+>")


def strip_html(text: str) -> str:
    """
    Remove HTML tags and unescape entities.
    """
    if not text:
        return ""
    text = unescape(text)
    return TAG_RE.sub("", text)


def parse_period(period_raw: str) -> Tuple[Optional[int], Optional[int]]:
    """
    "1 - 3" -> (1, 3)
    "5"     -> (5, 5)
    """
    if not period_raw:
        return None, None

    m = re.match(r"\s*(\d+)\s*-\s*(\d+)\s*$", period_raw)
    if m:
        return int(m.group(1)), int(m.group(2))

    m = re.match(r"\s*(\d+)\s*$", period_raw)
    if m:
        val = int(m.group(1))
        return val, val

    return None, None


def parse_time_range(time_raw: str) -> Tuple[Optional[str], Optional[str]]:
    """
    "07:45-10:15" -> ("07:45", "10:15")
    """
    if not time_raw or "-" not in time_raw:
        return None, None

    start, end = time_raw.split("-", 1)
    return start.strip(), end.strip()


SPAN_SUBST_RE = re.compile(r'class="substMonitorSubstElem">([^<]+)</span>')
SPAN_CANCEL_RE = re.compile(r'class="cancelStyle">([^<]+)</span>')
PAREN_CONTENT_RE = re.compile(r"\(([^()]+)\)")


def extract_room_info(cell: str) -> Tuple[Optional[str], Optional[str]]:
    """
    From e.g. '<span ...>A302</span> (B208)' → ('A302', 'B208')
    """
    if not cell:
        return None, None

    text = unescape(cell)

    new_room = None
    old_room = None

    m = SPAN_SUBST_RE.search(text)
    if m:
        new_room = m.group(1).strip()

    # old room usually plain in parentheses, no spans inside
    m2 = PAREN_CONTENT_RE.search(text)
    if m2:
        old_room = m2.group(1).strip()

    return new_room, old_room


def extract_teacher_info(cell: str) -> Tuple[Optional[str], Optional[str]]:
    """
    Handle:
      '<span ...>HEMA</span> (<span class="cancelStyle">KING</span>)'
      'BADE (KEIL)'
      'BADE'
      '--- (KEIL), BURG, GEIB, ...'  -> we still try to get 'KEIL'
    """
    if not cell:
        return None, None

    text = unescape(cell)

    # new teacher from substMonitorSubstElem span
    new_teacher = None
    m = SPAN_SUBST_RE.search(text)
    if m:
        new_teacher = m.group(1).strip()

    # absent teacher from cancelStyle span
    absent_teacher = None
    m2 = SPAN_CANCEL_RE.search(text)
    if m2:
        absent_teacher = m2.group(1).strip()

    # fallback: use plain text and parentheses
    plain = strip_html(text).strip()
    if not new_teacher:
        # everything before first '(' or ',' looks like main teacher code
        base = plain.split("(", 1)[0]
        base = base.split(",", 1)[0]
        base = base.strip()
        if base and base != "---":
            new_teacher = base

    if not absent_teacher:
        m3 = PAREN_CONTENT_RE.search(plain)
        if m3:
            inside = m3.group(1)
            # first token in parentheses
            absent_teacher = inside.split(",")[0].strip()

    # if both are empty or clearly not teacher codes, return None
    if new_teacher == "---":
        new_teacher = None

    return new_teacher, absent_teacher


def split_classes(classes_raw: str) -> List[str]:
    """
    "11A BZ, 12A BZ" -> ["11A BZ", "12A BZ"]
    "10A BV, ?"      -> ["10A BV"]
    """
    if not classes_raw:
        return []

    parts = [p.strip() for p in classes_raw.split(",")]
    return [p for p in parts if p and p != "?"]


def detect_is_cancelled(data_cells: List[str], cell_classes: Dict[str, List[str]]) -> bool:
    """
    Lesson is cancelled if:
      - 'Entfall' appears in any cell, or
      - 'cancelStyle' is present in cellClasses for any column.
    """
    for val in data_cells:
        if isinstance(val, str) and "Entfall" in strip_html(val):
            return True

    for classes in cell_classes.values():
        if any(c == "cancelStyle" for c in classes):
            return True

    return False


def detect_room_change(info_flag: str, room_new_code: Optional[str], room_old_code: Optional[str]) -> bool:
    """
    Room change if:
     - info mentions Raumänderung, or
     - new != old and both present.
    """
    if info_flag:
        plain = strip_html(info_flag)
        if "Raumänderung" in plain or "Raum\u00e4nderung" in info_flag:
            return True

    if room_new_code and room_old_code and room_new_code != room_old_code:
        return True

    return False


def detect_teacher_change(new_code: Optional[str], absent_code: Optional[str]) -> bool:
    return bool(new_code and absent_code and new_code != absent_code)


# ---------------------------
# Main normalization routine
# ---------------------------

def normalize() -> None:
    conn = get_conn()
    reset_schema(conn)

    cur = conn.cursor()
    cur.execute("SELECT id, date_yyyymmdd, raw_json FROM substitutions ORDER BY date_yyyymmdd;")
    rows = cur.fetchall()

    print(f"Normalizing {len(rows)} substitution days...")

    for sub_id, date_int, raw_json in rows:
        try:
            doc = json.loads(raw_json)
        except Exception as e:
            print(f"[WARN] Could not parse JSON for substitution_id={sub_id}: {e}")
            continue

        payload = doc.get("payload", {})
        table_rows = payload.get("rows", []) or []
        message_data = payload.get("messageData", {}) or {}
        messages = message_data.get("messages", []) or []

        # messages for the whole day
        for msg_idx, msg in enumerate(messages):
            # messages are sometimes dicts, sometimes strings
            if isinstance(msg, dict):
                text = msg.get("text") or json.dumps(msg, ensure_ascii=False)
            else:
                text = str(msg)

            conn.execute(
                """
                INSERT INTO message (substitution_id, message_index, text)
                VALUES (?, ?, ?);
                """,
                (sub_id, msg_idx, text),
            )

        # rows in the table
        for row_index, row in enumerate(table_rows):
            data_cells: List[str] = row.get("data", []) or []
            group_label: str = row.get("group") or ""
            cell_classes: Dict[str, List[str]] = row.get("cellClasses") or {}

            # Safe access with defaults
            c0 = data_cells[0] if len(data_cells) > 0 else ""
            c1 = data_cells[1] if len(data_cells) > 1 else ""
            c2 = data_cells[2] if len(data_cells) > 2 else ""
            c3 = data_cells[3] if len(data_cells) > 3 else ""
            c4 = data_cells[4] if len(data_cells) > 4 else ""
            c5 = data_cells[5] if len(data_cells) > 5 else ""
            c6 = data_cells[6] if len(data_cells) > 6 else ""
            c7 = data_cells[7] if len(data_cells) > 7 else ""

            period_raw = strip_html(c0)
            time_raw = strip_html(c1)
            classes_raw = strip_html(c2)
            subject_raw = strip_html(c3)
            info_flag = strip_html(c6)
            comment = strip_html(c7)

            period_start, period_end = parse_period(period_raw)
            time_start, time_end = parse_time_range(time_raw)

            room_new_code, room_old_code = extract_room_info(c4)
            teacher_new_code, teacher_absent_code = extract_teacher_info(c5)

            is_cancelled = int(detect_is_cancelled(data_cells, cell_classes))
            is_room_change = int(detect_room_change(info_flag, room_new_code, room_old_code))
            is_teacher_change = int(detect_teacher_change(teacher_new_code, teacher_absent_code))

            # Lookup IDs (these functions return None if code is empty)
            room_new_id = get_or_create_code(conn, "room", room_new_code)
            room_old_id = get_or_create_code(conn, "room", room_old_code)
            teacher_new_id = get_or_create_code(conn, "teacher", teacher_new_code)
            teacher_absent_id = get_or_create_code(conn, "teacher", teacher_absent_code)
            subject_id = get_or_create_code(conn, "subject", subject_raw)  # currently unused, but fills table

            # Insert substitution_entry
            cur2 = conn.cursor()
            cur2.execute(
                """
                INSERT INTO substitution_entry (
                    substitution_id,
                    row_index,
                    group_label,
                    period_raw,
                    period_start,
                    period_end,
                    time_start,
                    time_end,
                    subject_raw,
                    info_flag,
                    comment,
                    is_cancelled,
                    is_room_change,
                    is_teacher_change,
                    room_new_id,
                    room_old_id,
                    teacher_new_id,
                    teacher_absent_id
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
                """,
                (
                    sub_id,
                    row_index,
                    group_label,
                    period_raw,
                    period_start,
                    period_end,
                    time_start,
                    time_end,
                    subject_raw,
                    info_flag,
                    comment,
                    is_cancelled,
                    is_room_change,
                    is_teacher_change,
                    room_new_id,
                    room_old_id,
                    teacher_new_id,
                    teacher_absent_id,
                ),
            )
            entry_id = cur2.lastrowid

            # Classes
            for cls_code in split_classes(classes_raw):
                class_id = get_or_create_code(conn, "class", cls_code)
                if class_id is not None:
                    conn.execute(
                        "INSERT OR IGNORE INTO entry_class (entry_id, class_id) VALUES (?, ?);",
                        (entry_id, class_id),
                    )

        conn.commit()
        print(f"[OK] Normalized substitution_id={sub_id} (date={date_int})")

    print("Done.")


if __name__ == "__main__":
    normalize()
