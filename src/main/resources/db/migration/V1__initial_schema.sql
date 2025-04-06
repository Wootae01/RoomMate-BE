
CREATE TABLE member (
    member_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255),
    nickname VARCHAR(10),
    introduce VARCHAR(255),
    age INT,
    dorm VARCHAR(30),
    gender VARCHAR(30)
);

CREATE TABLE chat_room(
    chat_room_id BIGINT PRIMARY KEY AUTO_INCREMENT
);

CREATE TABLE member_chat_room(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    member_id BIGINT,
    chat_room_id BIGINT,
    FOREIGN KEY (member_id) REFERENCES member(member_id),
    FOREIGN KEY (chat_room_id) REFERENCES chat_room(chat_room_id)

);

CREATE TABLE message(
    message_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    sender_id BIGINT,
    chat_room_id BIGINT,
    content TEXT,
    send_time DATETIME,
    FOREIGN KEY (sender_id) REFERENCES member(member_id),
    FOREIGN KEY (chat_room_id) REFERENCES chat_room(chat_room_id)

);

CREATE TABLE notification(
    notification_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    member_id BIGINT,
    token VARCHAR(255),
    permission TINYINT(1),
    FOREIGN KEY (member_id) REFERENCES member(member_id)
);

CREATE TABLE refresh_entity(
    refresh_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255),
    refresh VARCHAR(511),
    role VARCHAR(255),
    expiration TIMESTAMP
);

CREATE TABLE options(
    option_id BIGINT PRIMARY KEY ,
    category VARCHAR(255),
    option_value VARCHAR(255)
);

CREATE TABLE lifestyle(
    lifestyle_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    option_id BIGINT,
    member_id BIGINT,

    FOREIGN KEY (option_id) REFERENCES options(option_id),
    FOREIGN KEY (member_id) REFERENCES member(member_id)
);

CREATE TABLE preference(
    preference_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    option_id BIGINT,
    member_id BIGINT,

    FOREIGN KEY (option_id) REFERENCES options(option_id),
    FOREIGN KEY (member_id) REFERENCES member(member_id)
)

