DROP TABLE users CASCADE;
DROP TABLE rooms CASCADE;
DROP TABLE reservation CASCADE;

CREATE TABLE users(
	user_id VARCHAR(100) PRIMARY KEY
	, name VARCHAR(100)
	, password VARCHAR(100)
);

CREATE TABLE room(
	room_id VARCHAR(100) PRIMARY KEY
	, start_time TIMESTAMP
	, end_time TIMESTAMP
	, computer VARCHAR(100)
);

CREATE TABLE reservation(
	room_id VARCHAR(100)
	, user_id VARCHAR(100)
	, reserve_start TIMESTAMP
	, reserve_end TIMESTAMP
	, FOREIGN KEY(room_id) REFERENCES rooms(room_id)
	, FOREIGN KEY(user_id) REFERENCES users(user_id)
	, PRIMARY KEY(room_id, user_id, reserveStart, reserveEnd)
);
