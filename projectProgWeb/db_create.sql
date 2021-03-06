CREATE TABLE users(
    userid INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    avatar TEXT,
    email TEXT NOT NULL
);

CREATE TABLE groups(
    groupid INTEGER PRIMARY KEY AUTOINCREMENT,
    ownerid INTEGER NOT NULL,
    groupname TEXT NOT NULL,
    creationdate TEXT
);

CREATE TABLE user_groups (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    userid INTEGER NOT NULL,
    groupid INTEGER NOT NULL,
    status INTEGER,
    FOREIGN KEY(userid) REFERENCES users(userid),
    FOREIGN KEY(groupid) REFERENCES groups(groupid)
);

CREATE TABLE post(
    postid INTEGER PRIMARY KEY AUTOINCREMENT,
    groupid INTEGER,
    ownerid INTEGER,
    date TEXT,
    content TEXT
);

CREATE TABLE post_file(
    fileid INTEGER PRIMARY KEY AUTOINCREMENT,
    postid INTEGER,
    filepath TEXT,
    FOREIGN KEY(postid) REFERENCES post(postid)
);

