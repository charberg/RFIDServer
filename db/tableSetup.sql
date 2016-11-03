--This script was run in SQLite, in a Raspbian environment

--Create items table
CREATE TABLE IF NOT EXISTS items (
	id INTEGER PRIMARY KEY,
	name TEXT,
	type TEXT,
	description TEXT,
	location INTEGER);

--Create locations table
CREATE TABLE IF NOT EXISTS locations(
	id INTEGER PRIMARY KEY,
	name TEXT,
	description TEXT,
	address TEXT,
	lat TEXT,
	lon TEXT);

