DROP TABLE IF EXISTS usersDataTable;

CREATE TABLE usersDataTable
(
    chatId       INTEGER PRIMARY KEY,
    firstName    text,
    lastName     text,
    userName     text,
    registeredAt timestamp
);