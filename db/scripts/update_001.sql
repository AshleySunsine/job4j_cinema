CREATE TABLE IF NOT EXISTS account (
  id SERIAL PRIMARY KEY,
  username VARCHAR NOT NULL,
  email VARCHAR NOT NULL UNIQUE,
  phone VARCHAR NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS ticket (
id SERIAL PRIMARY KEY,
session_id INT NOT NULL,
rov INT NOT NULL,
cell INT NOT NULL,
account_id INT NOT NULL REFERENCES account(id)
);

insert into account (username, email, phone) values ('a', 'a', '1');
insert into ticket (session_id, rov, cell, account_id) values (1, 1, 1, 1);