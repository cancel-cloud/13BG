import argparse
import datetime as dt
import json
import sqlite3
import time
from pathlib import Path
from typing import Dict, Any, Optional

import requests

# ----------------------------
# Configuration
# ----------------------------

UNTIS_URL = (
    "https://friedrich-dessauer-schule-limburg.webuntis.com/"
    "WebUntis/monitor/substitution/data?school=friedrich-dessauer-schule-limburg"
)

DB_PATH = Path("substitutions.db")

# This is your base payload from curl with a placeholder for "date"
BASE_PAYLOAD: Dict[str, Any] = {
    "formatName": "Web-Schüler-heute",
    "schoolName": "friedrich-dessauer-schule-limburg",
    # "date": 20251208,  # will be overwritten per request
    "dateOffset": 0,
    "strikethrough": True,
    "mergeBlocks": True,
    "showOnlyFutureSub": True,
    "showBreakSupervisions": False,
    "showTeacher": True,
    "showClass": True,
    "showHour": True,
    "showInfo": True,
    "showRoom": True,
    "showSubject": True,
    "groupBy": 1,
    "hideAbsent": False,
    "departmentIds": [],
    "departmentElementType": -1,
    "hideCancelWithSubstitution": True,
    "hideCancelCausedByEvent": False,
    "showTime": True,
    "showSubstText": True,
    "showAbsentElements": [],
    "showAffectedElements": [1],
    "showUnitTime": False,
    "showMessages": True,
    "showStudentgroup": False,
    "enableSubstitutionFrom": False,
    "showSubstitutionFrom": 0,
    "showTeacherOnEvent": False,
    "showAbsentTeacher": True,
    "strikethroughAbsentTeacher": True,
    "activityTypeIds": [],
    "showEvent": True,
    "showCancel": True,
    "showOnlyCancel": False,
    "showSubstTypeColor": False,
    "showExamSupervision": False,
    "showUnheraldedExams": False,
}

HEADERS = {
    "Content-Type": "application/json",
}


# ----------------------------
# Database helpers
# ----------------------------

def init_db(db_path: Path = DB_PATH) -> sqlite3.Connection:
    """Create (or open) the SQLite DB and ensure table exists."""
    conn = sqlite3.connect(db_path)
    conn.execute(
        """
        CREATE TABLE IF NOT EXISTS substitutions (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            date_yyyymmdd INTEGER NOT NULL UNIQUE,
            fetched_at TEXT NOT NULL,
            raw_json TEXT NOT NULL
        );
        """
    )
    conn.commit()
    return conn


def save_substitution_day(
    conn: sqlite3.Connection,
    date_int: int,
    fetched_at: str,
    raw_json: Dict[str, Any],
) -> None:
    """Insert or replace data for a given date."""
    conn.execute(
        """
        INSERT INTO substitutions (date_yyyymmdd, fetched_at, raw_json)
        VALUES (?, ?, ?)
        ON CONFLICT(date_yyyymmdd) DO UPDATE SET
            fetched_at = excluded.fetched_at,
            raw_json   = excluded.raw_json;
        """,
        (date_int, fetched_at, json.dumps(raw_json)),
    )
    conn.commit()


# ----------------------------
# HTTP / scraping
# ----------------------------

def build_payload_for_date(date_int: int) -> Dict[str, Any]:
    """Return the POST body for a given yyyymmdd integer."""
    payload = BASE_PAYLOAD.copy()
    payload["date"] = date_int
    return payload


def fetch_substitution_for_date(date_int: int) -> Optional[Dict[str, Any]]:
    """
    Call the WebUntis substitution endpoint for a given date.
    Returns parsed JSON or None if the request fails.
    """
    payload = build_payload_for_date(date_int)

    try:
        resp = requests.post(UNTIS_URL, headers=HEADERS, json=payload, timeout=20)
    except requests.RequestException as e:
        print(f"[ERROR] Request failed for {date_int}: {e}")
        return None

    if resp.status_code != 200:
        print(f"[WARN] Non-200 status for {date_int}: {resp.status_code}")
        return None

    try:
        return resp.json()
    except ValueError:
        print(f"[ERROR] Could not decode JSON for {date_int}")
        return None


# ----------------------------
# Date utilities
# ----------------------------

def date_str_to_date(s: str) -> dt.date:
    """
    Convert 'YYYY-MM-DD' -> datetime.date.
    """
    return dt.datetime.strptime(s, "%Y-%m-%d").date()


def date_to_yyyymmdd_int(d: dt.date) -> int:
    """Convert date -> yyyymmdd int."""
    return int(d.strftime("%Y%m%d"))


def iterate_dates(start: dt.date, end: dt.date):
    """
    Iterate from start to end inclusive (start <= day <= end).
    """
    if end < start:
        raise ValueError("end date must be >= start date")

    current = start
    while current <= end:
        yield current
        current += dt.timedelta(days=1)


# ----------------------------
# Main loop
# ----------------------------

def main():
    parser = argparse.ArgumentParser(
        description=(
            "Scrape WebUntis substitution data day by day into a local SQLite DB."
        )
    )
    parser.add_argument(
        "--start",
        required=True,
        help="Start date in format YYYY-MM-DD (inclusive).",
    )
    parser.add_argument(
        "--end",
        required=True,
        help="End date in format YYYY-MM-DD (inclusive).",
    )
    parser.add_argument(
        "--sleep",
        type=float,
        default=0.5,
        help="Seconds to sleep between requests (default: 0.5).",
    )

    args = parser.parse_args()

    start_date = date_str_to_date(args.start)
    end_date = date_str_to_date(args.end)

    print(f"Scraping from {start_date} to {end_date} (inclusive)…")

    conn = init_db()

    for day in iterate_dates(start_date, end_date):
        date_int = date_to_yyyymmdd_int(day)
        print(f"\n[INFO] Fetching {day} (date={date_int})…")

        data = fetch_substitution_for_date(date_int)
        if data is None:
            print(f"[WARN] Skipping {day} due to error.")
        else:
            fetched_at = dt.datetime.now().isoformat(timespec="seconds")
            save_substitution_day(conn, date_int, fetched_at, data)
            print(f"[OK] Saved data for {day} into SQLite.")

        time.sleep(args.sleep)

    conn.close()
    print("\nDone. Data stored in:", DB_PATH.resolve())


if __name__ == "__main__":
    main()
