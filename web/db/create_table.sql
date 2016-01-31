CREATE TABLE account(
id int IDENTITY,
forename varchar(255),
password varchar(255));

CREATE TABLE event(
event_id int IDENTITY,
name varchar(255),
description varchar(255),
acc_id int REFERENCES account(id) not null
);

CREATE TABLE participation(
participation_id int IDENTITY,
acc_id int REFERENCES account(id) not null,
event_id int REFERENCES event(event_id) not null,
day int,
hour int
)