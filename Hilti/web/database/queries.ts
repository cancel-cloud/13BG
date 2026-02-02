
import { db } from './db';
import { teacher, substitutionEntry, substitutions, room, subject, class_, entryClass } from './schema';
import { eq, desc, count, sql, like, and, isNotNull } from 'drizzle-orm';

export async function getTopAbsentTeachers(limit = 10) {
  return await db
    .select({
      id: teacher.id,
      code: teacher.code,
      absences: count(substitutionEntry.id),
      lastAbsence: sql<string>`MAX(${substitutions.date_yyyymmdd})`,
    })
    .from(teacher)
    .leftJoin(substitutionEntry, eq(teacher.id, substitutionEntry.teacher_absent_id))
    .leftJoin(substitutions, eq(substitutionEntry.substitution_id, substitutions.id))
    .groupBy(teacher.id)
    .orderBy(desc(count(substitutionEntry.id)))
    .limit(limit);
}

export async function getAllTeachers() {
  return await db
    .select({
      id: teacher.id,
      code: teacher.code,
      absences: count(substitutionEntry.id),
    })
    .from(teacher)
    .leftJoin(substitutionEntry, eq(teacher.id, substitutionEntry.teacher_absent_id))
    .groupBy(teacher.id)
    .orderBy(teacher.code);
}

export async function searchTeachers(query: string) {
  return await db
    .select({
      id: teacher.id,
      code: teacher.code,
      absences: count(substitutionEntry.id),
    })
    .from(teacher)
    .leftJoin(substitutionEntry, eq(teacher.id, substitutionEntry.teacher_absent_id))
    .where(like(teacher.code, `%${query}%`))
    .groupBy(teacher.id)
    .orderBy(desc(count(substitutionEntry.id)));
}

export async function getTeacherStats(teacherId: number) {
  const teacherData = await db.query.teacher.findFirst({
    where: eq(teacher.id, teacherId),
  });

  if (!teacherData) return null;

  const absences = await db
    .select({ count: count() })
    .from(substitutionEntry)
    .where(eq(substitutionEntry.teacher_absent_id, teacherId));

  const recentEntries = await db
    .select({
      date: substitutions.date_yyyymmdd,
      period: substitutionEntry.period_raw,
      subject: substitutionEntry.subject_raw,
      info: substitutionEntry.info_flag,
      substitution_id: substitutions.id,
    })
    .from(substitutionEntry)
    .innerJoin(substitutions, eq(substitutionEntry.substitution_id, substitutions.id))
    .where(eq(substitutionEntry.teacher_absent_id, teacherId))
    .orderBy(desc(substitutions.date_yyyymmdd))
    .limit(10);

  return {
    ...teacherData,
    totalAbsences: absences[0].count,
    recentEntries,
  };
}

export async function getCancelledLessons(limit = 50) {
  return await db
    .select({
      id: substitutionEntry.id,
      date: substitutions.date_yyyymmdd,
      period: substitutionEntry.period_raw,
      subject: substitutionEntry.subject_raw,
      teacher: teacher.code,
      room: room.code,
      info: substitutionEntry.info_flag,
      group: substitutionEntry.group_label,
    })
    .from(substitutionEntry)
    .innerJoin(substitutions, eq(substitutionEntry.substitution_id, substitutions.id))
    .leftJoin(teacher, eq(substitutionEntry.teacher_absent_id, teacher.id))
    .leftJoin(room, eq(substitutionEntry.room_old_id, room.id))
    .where(eq(substitutionEntry.is_cancelled, 1))
    .orderBy(desc(substitutions.date_yyyymmdd))
    .limit(limit);
}
