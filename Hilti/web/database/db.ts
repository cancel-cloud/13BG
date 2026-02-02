
import { drizzle } from 'drizzle-orm/better-sqlite3';
import Database from 'better-sqlite3';
import path from 'path';
import * as schema from './schema';

// Database is located in the sibling directory 'untis_scraper'
// In production/deployment, this path would need to be handled carefully.
// For this local setup, we access it via relative path from CWD (usually project root).
const dbPath = path.join(process.cwd(), '../untis_scraper/substitutions.db');

const sqlite = new Database(dbPath, { readonly: true });
export const db = drizzle(sqlite, { schema });
