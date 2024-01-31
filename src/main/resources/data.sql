insert into MEMBERS ( EMAIL, PASSWORD, NICK_NAME) values ('admin@gmail.com', '$2a$10$F2JsXoyV3Ws9o6m1wmGaC.d5QT9pK6rNLpgwbCEQH1JuXm4HD6On6', 'admin');

insert into AUTHORITY (AUTHORITY_NAME) values ('ROLE_USER');
insert into AUTHORITY (AUTHORITY_NAME) values ('ROLE_ADMIN');

insert into MEMBER_AUTHORITY (MEMBER_ID, AUTHORITY_NAME) values (1, 'ROLE_USER');
insert into MEMBER_AUTHORITY (MEMBER_ID, AUTHORITY_NAME) values (1, 'ROLE_ADMIN');
