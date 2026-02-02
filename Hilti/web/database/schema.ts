import { sqliteTable, integer, text, uniqueIndex } from 'drizzle-orm/sqlite-core';

export const substitutions = sqliteTable('substitutions', {
  id: integer('id').primaryKey({ autoIncrement: true }),
  date_yyyymmdd: integer('date_yyyymmdd').notNull().unique(),
  fetched_at: text('fetched_at').notNull(),
  raw_json: text('raw_json').notNull(),
});

export const teacher = sqliteTable('teacher', {
  id: integer('id').primaryKey({ autoIncrement: true }),
  code: text('code').notNull().unique(),
});

export const room = sqliteTable('room', {
  id: integer('id').primaryKey({ autoIncrement: true }),
  code: text('code').notNull().unique(),
});

export const class_ = sqliteTable('class', { // class is a reserved keyword
  id: integer('id').primaryKey({ autoIncrement: true }),
  code: text('code').notNull().unique(),
});

export const subject = sqliteTable('subject', {
  id: integer('id').primaryKey({ autoIncrement: true }),
  code: text('code').notNull().unique(),
});

export const substitutionEntry = sqliteTable('substitution_entry', {
  id: integer('id').primaryKey({ autoIncrement: true }),
  substitution_id: integer('substitution_id').notNull().references(() => substitutions.id, { onDelete: 'cascade' }),
  row_index: integer('row_index').notNull(),

  group_label: text('group_label'),
  period_raw: text('period_raw'),
  period_start: integer('period_start'),
  period_end: integer('period_end'),
  time_start: text('time_start'),
  time_end: text('time_end'),
  subject_raw: text('subject_raw'),
  info_flag: text('info_flag'),
  comment: text('comment'),

  is_cancelled: integer('is_cancelled').notNull().default(0),
  is_room_change: integer('is_room_change').notNull().default(0),
  is_teacher_change: integer('is_teacher_change').notNull().default(0),

  room_new_id: integer('room_new_id').references(() => room.id),
  room_old_id: integer('room_old_id').references(() => room.id),
  teacher_new_id: integer('teacher_new_id').references(() => teacher.id),
  teacher_absent_id: integer('teacher_absent_id').references(() => teacher.id),
}, (t) => ({
  idx_day_row: uniqueIndex('idx_substitution_entry_day_row').on(t.substitution_id, t.row_index),
}));

export const entryClass = sqliteTable('entry_class', {
  entry_id: integer('entry_id').notNull().references(() => substitutionEntry.id, { onDelete: 'cascade' }),
  class_id: integer('class_id').notNull().references(() => class_.id),
});
