CREATE TABLE MEMBER (
    member_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255),
    nickname VARCHAR(10),
    introduce VARCHAR(255),
    age INT,
    dorm VARCHAR(30),
    gender VARCHAR(30)
);

CREATE TABLE CHAT_ROOM(
    chat_room_id BIGINT PRIMARY KEY AUTO_INCREMENT
);

CREATE TABLE MEMBER_CHAT_ROOM(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    member_id BIGINT,
    chat_room_id BIGINT,
    FOREIGN KEY (member_id) REFERENCES MEMBER(member_id),
    FOREIGN KEY (chat_room_id) REFERENCES CHAT_ROOM(chat_room_id)

);

CREATE TABLE MESSAGE(
    message_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    sender_id BIGINT,
    chat_room_id BIGINT,
    content TEXT,
    sendTime DATETIME,
    FOREIGN KEY (sender_id) REFERENCES MEMBER(member_id),
    FOREIGN KEY (chat_room_id) REFERENCES CHAT_ROOM(chat_room_id)

);

CREATE TABLE NOTIFICATION(
    notification_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    member_id BIGINT,
    token VARCHAR(255),
    permission TINYINT(1),
    FOREIGN KEY (member_id) REFERENCES MEMBER(member_id)
);

CREATE TABLE REFRESH_ENTITY(
    refresh_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255),
    refresh VARCHAR(511),
    role VARCHAR(255),
    expiration TIMESTAMP
);

CREATE TABLE OPTIONS(
    option_id BIGINT PRIMARY KEY ,
    category VARCHAR(255),
    option_value VARCHAR(255)
);

CREATE TABLE LIFESTYLE(
    lifestyle_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    option_id BIGINT,
    member_id BIGINT,

    FOREIGN KEY (option_id) REFERENCES OPTIONS(option_id),
    FOREIGN KEY (member_id) REFERENCES MEMBER(member_id)
);

CREATE TABLE PREFERENCE(
    preference_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    option_id BIGINT,
    member_id BIGINT,

    FOREIGN KEY (option_id) REFERENCES OPTIONS(option_id),
    FOREIGN KEY (member_id) REFERENCES MEMBER(member_id)
)

