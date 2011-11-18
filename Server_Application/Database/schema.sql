DROP TABLE users CASCADE;
DROP TABLE rooms CASCADE;
DROP TABLE reservations CASCADE;

CREATE TABLE users(
	user_id VARCHAR(100) PRIMARY KEY
	, name VARCHAR(100)
	, password VARCHAR(100)
);

CREATE TABLE rooms(
	room_id VARCHAR(100) PRIMARY KEY
	, startTime DATETIME
	, endTime DATETIME
	, computer VARCHAR(100)
);

CREATE TABLE reservations(
	room_id VARCHAR(100)
	, user_id VARCHAR(100)
	, reserveStart DATETIME
	, reserveEnd DATETIME
	, FOREIGN KEY(room_id) REFERENCES rooms(room_id)
	, FOREIGN KEY(user_id) REFERENCES users(user_id)
	, PRIMARY KEY(room_id, user_id, reserveStart, reserveEnd)
);
