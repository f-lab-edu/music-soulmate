insert into MEMBERS (EMAIL, PASSWORD, NICK_NAME) values ('admin@gmail.com', '$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi', 'admin');
insert into MEMBERS (EMAIL, PASSWORD, NICK_NAME) values ('user@gmail.com', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'user');

insert into AUTHORITY (AUTHORITY_NAME) values ('ROLE_USER');
insert into AUTHORITY (AUTHORITY_NAME) values ('ROLE_ADMIN');

insert into MEMBER_AUTHORITY (MEMBER_ID, AUTHORITY_NAME) values (1, 'ROLE_USER');
insert into MEMBER_AUTHORITY (MEMBER_ID, AUTHORITY_NAME) values (1, 'ROLE_ADMIN');
insert into MEMBER_AUTHORITY (MEMBER_ID, AUTHORITY_NAME) values (2, 'ROLE_USER');