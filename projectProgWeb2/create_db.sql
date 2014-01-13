CREATE TABLE users (
	userid INTEGER PRIMARY KEY ,
	username TEXT NOT NULL UNIQUE,
	password TEXT NOT NULL ,
	avatar TEXT,
	email TEXT NOT NULL UNIQUE ,
	type integer DEFAULT (0) 
);

CREATE TABLE groups(
    groupid INTEGER PRIMARY KEY AUTOINCREMENT,
    ownerid INTEGER NOT NULL,
    groupname TEXT NOT NULL,
    creationdate NUMERIC,
	"private" BOOL NOT NULL  DEFAULT true
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
	ownerid INTEGER,
    groupid INTEGER,
    filename TEXT,
    FOREIGN KEY(groupid) REFERENCES groups(groupid),
	FOREIGN KEY(ownerid) REFERENCES users(userid)
);
