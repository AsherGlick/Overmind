--reserve a room
INSERT INTO reservation( rid, uid, startTime, endTime);

--unreserve a room
DELETE FROM reservation WHERE reservation.user_id = uid AND reservation.room_id = rid AND reservation.reserve_start = startTime AND reservation.reserve_end = endTime;

SELECT getRoomScheduleDate('room2', '2011-12-13 00:00:00');
