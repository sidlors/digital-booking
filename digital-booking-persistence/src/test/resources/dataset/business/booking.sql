-- Cine 10
INSERT INTO C_THEATER (ID_THEATER ,ID_REGION,ID_CITY,ID_STATE,ID_VISTA, NU_THEATER, DS_TELEPHONE,FG_ACTIVE,DT_LAST_MODIFICATION,ID_LAST_USER_MODIFIER, ID_EMAIL) VALUES (10,1,1,1,1001,1,'1234',1,'2014-03-28 12:00:00',1,10)
INSERT INTO C_THEATER_LANGUAGE(ID_THEATER_LANGUAGE,ID_LANGUAGE,DS_NAME,ID_THEATER) VALUES (13,1,'Cine prueba',10)
INSERT INTO C_THEATER_LANGUAGE(ID_THEATER_LANGUAGE,ID_LANGUAGE,DS_NAME,ID_THEATER) VALUES (14,2,'Cine prueba',10)

insert into C_SCREEN (ID_SCREEN, ID_THEATER, ID_VISTA, NU_SCREEN, NU_CAPACITY, FG_ACTIVE, DT_LAST_MODIFICATION, ID_LAST_USER_MODIFIER) values (11, 10, '560-1', 1, 308, 1, '2014-08-25 13:58:57', 2)
insert into C_SCREEN (ID_SCREEN, ID_THEATER, ID_VISTA, NU_SCREEN, NU_CAPACITY, FG_ACTIVE, DT_LAST_MODIFICATION, ID_LAST_USER_MODIFIER) values (12, 10, '560-2', 2, 400, 1, '2014-08-25 13:58:57', 2)
insert into C_SCREEN (ID_SCREEN, ID_THEATER, ID_VISTA, NU_SCREEN, NU_CAPACITY, FG_ACTIVE, DT_LAST_MODIFICATION, ID_LAST_USER_MODIFIER) values (13, 10, '560-3', 3, 206, 1, '2014-08-25 13:58:57', 2)
insert into C_SCREEN (ID_SCREEN, ID_THEATER, ID_VISTA, NU_SCREEN, NU_CAPACITY, FG_ACTIVE, DT_LAST_MODIFICATION, ID_LAST_USER_MODIFIER) values (14, 10, '560-4', 4, 173, 1, '2014-08-25 13:58:57', 2)
insert into C_SCREEN (ID_SCREEN, ID_THEATER, ID_VISTA, NU_SCREEN, NU_CAPACITY, FG_ACTIVE, DT_LAST_MODIFICATION, ID_LAST_USER_MODIFIER) values (15, 10, '560-5', 5, 173, 1, '2014-08-25 13:58:57', 2)
insert into C_SCREEN (ID_SCREEN, ID_THEATER, ID_VISTA, NU_SCREEN, NU_CAPACITY, FG_ACTIVE, DT_LAST_MODIFICATION, ID_LAST_USER_MODIFIER) values (16, 10, '560-6', 6, 134, 1, '2014-08-25 13:58:57', 2)
insert into C_SCREEN (ID_SCREEN, ID_THEATER, ID_VISTA, NU_SCREEN, NU_CAPACITY, FG_ACTIVE, DT_LAST_MODIFICATION, ID_LAST_USER_MODIFIER) values (17, 10, '560-7', 7, 134, 1, '2014-08-25 13:58:57', 2)
insert into C_SCREEN (ID_SCREEN, ID_THEATER, ID_VISTA, NU_SCREEN, NU_CAPACITY, FG_ACTIVE, DT_LAST_MODIFICATION, ID_LAST_USER_MODIFIER) values (18, 10, '560-8', 8, 140, 1, '2014-08-25 13:58:57', 2)
insert into C_SCREEN (ID_SCREEN, ID_THEATER, ID_VISTA, NU_SCREEN, NU_CAPACITY, FG_ACTIVE, DT_LAST_MODIFICATION, ID_LAST_USER_MODIFIER) values (19, 10, '560-9', 9, 134, 1, '2014-08-25 13:58:57', 2)
insert into C_SCREEN (ID_SCREEN, ID_THEATER, ID_VISTA, NU_SCREEN, NU_CAPACITY, FG_ACTIVE, DT_LAST_MODIFICATION, ID_LAST_USER_MODIFIER) values (20, 10, '560-10', 10, 134, 1, '2014-08-25 13:58:57', 2)


INSERT INTO C_SCREEN_X_CATEGORY(ID_SCREEN,ID_CATEGORY)VALUES(11,6)
INSERT INTO C_SCREEN_X_CATEGORY(ID_SCREEN,ID_CATEGORY)VALUES(12,6)
INSERT INTO C_SCREEN_X_CATEGORY(ID_SCREEN,ID_CATEGORY)VALUES(13,6)
INSERT INTO C_SCREEN_X_CATEGORY(ID_SCREEN,ID_CATEGORY)VALUES(14,6)
INSERT INTO C_SCREEN_X_CATEGORY(ID_SCREEN,ID_CATEGORY)VALUES(15,6)
INSERT INTO C_SCREEN_X_CATEGORY(ID_SCREEN,ID_CATEGORY)VALUES(16,6)
INSERT INTO C_SCREEN_X_CATEGORY(ID_SCREEN,ID_CATEGORY)VALUES(17,6)
INSERT INTO C_SCREEN_X_CATEGORY(ID_SCREEN,ID_CATEGORY)VALUES(18,6)
INSERT INTO C_SCREEN_X_CATEGORY(ID_SCREEN,ID_CATEGORY)VALUES(19,6)
INSERT INTO C_SCREEN_X_CATEGORY(ID_SCREEN,ID_CATEGORY)VALUES(20,6)


INSERT INTO K_EVENT ( ID_EVENT, ID_EVENT_TYPE, ID_VISTA, DS_NAME, DS_CODE_DBS, QT_DURATION, QT_COPY, FG_PREMIERE, FG_ACTIVE, DT_LAST_MODIFICATION, ID_LAST_USER_MODIFIER, FG_CURRENT, FG_PRERELEASE, FG_FESTIVAL, FG_ACTIVE_IA, FG_ALTERNATE_CONTENT) VALUES ( 20, 1, 1010, 'Pelicula ', '2020', 138, 10, 1, 0, '2014-06-27', 1, 0, 0, 0, 0, 0)
INSERT INTO K_EVENT ( ID_EVENT, ID_EVENT_TYPE, ID_VISTA, DS_NAME, DS_CODE_DBS, QT_DURATION, QT_COPY, FG_PREMIERE, FG_ACTIVE, DT_LAST_MODIFICATION, ID_LAST_USER_MODIFIER, FG_CURRENT, FG_PRERELEASE, FG_FESTIVAL, FG_ACTIVE_IA, FG_ALTERNATE_CONTENT) VALUES ( 21, 1, 1010, 'Pelicula ', '2020', 138, 10, 1, 0, '2014-06-27', 1, 0, 0, 0, 0, 0)
INSERT INTO K_EVENT ( ID_EVENT, ID_EVENT_TYPE, ID_VISTA, DS_NAME, DS_CODE_DBS, QT_DURATION, QT_COPY, FG_PREMIERE, FG_ACTIVE, DT_LAST_MODIFICATION, ID_LAST_USER_MODIFIER, FG_CURRENT, FG_PRERELEASE, FG_FESTIVAL, FG_ACTIVE_IA, FG_ALTERNATE_CONTENT) VALUES ( 22, 1, 1010, 'Pelicula ', '2020', 138, 10, 1, 0, '2014-06-27', 1, 0, 0, 0, 0, 0)
INSERT INTO K_EVENT ( ID_EVENT, ID_EVENT_TYPE, ID_VISTA, DS_NAME, DS_CODE_DBS, QT_DURATION, QT_COPY, FG_PREMIERE, FG_ACTIVE, DT_LAST_MODIFICATION, ID_LAST_USER_MODIFIER, FG_CURRENT, FG_PRERELEASE, FG_FESTIVAL, FG_ACTIVE_IA, FG_ALTERNATE_CONTENT) VALUES ( 23, 1, 1010, 'Pelicula ', '2020', 138, 10, 1, 0, '2014-06-27', 1, 0, 0, 0, 0, 0)
INSERT INTO K_EVENT ( ID_EVENT, ID_EVENT_TYPE, ID_VISTA, DS_NAME, DS_CODE_DBS, QT_DURATION, QT_COPY, FG_PREMIERE, FG_ACTIVE, DT_LAST_MODIFICATION, ID_LAST_USER_MODIFIER, FG_CURRENT, FG_PRERELEASE, FG_FESTIVAL, FG_ACTIVE_IA, FG_ALTERNATE_CONTENT) VALUES ( 24, 1, 1010, 'Pelicula ', '2020', 138, 10, 1, 0, '2014-06-27', 1, 0, 0, 0, 0, 0)
INSERT INTO K_EVENT ( ID_EVENT, ID_EVENT_TYPE, ID_VISTA, DS_NAME, DS_CODE_DBS, QT_DURATION, QT_COPY, FG_PREMIERE, FG_ACTIVE, DT_LAST_MODIFICATION, ID_LAST_USER_MODIFIER, FG_CURRENT, FG_PRERELEASE, FG_FESTIVAL, FG_ACTIVE_IA, FG_ALTERNATE_CONTENT) VALUES ( 25, 1, 5678, 'LA DAMA DE NEGRO 2 DIG SUB', '5678', 120, 1, 1, 1, '2015-02-18', 1, 1, 1, 0, 0, 0)
INSERT INTO K_EVENT ( ID_EVENT, ID_EVENT_TYPE, ID_VISTA, DS_NAME, DS_CODE_DBS, QT_DURATION, QT_COPY, FG_PREMIERE, FG_ACTIVE, DT_LAST_MODIFICATION, ID_LAST_USER_MODIFIER, FG_CURRENT, FG_PRERELEASE, FG_FESTIVAL, FG_ACTIVE_IA, FG_ALTERNATE_CONTENT) VALUES ( 26, 1, 5679, 'LA DAMA DE NEGRO 2 4DX/2D ESP', '5679', 120, 1, 1, 1, '2015-02-18', 1, 1, 1, 0, 0, 0)
INSERT INTO K_EVENT ( ID_EVENT, ID_EVENT_TYPE, ID_VISTA, DS_NAME, DS_CODE_DBS, QT_DURATION, QT_COPY, FG_PREMIERE, FG_ACTIVE, DT_LAST_MODIFICATION, ID_LAST_USER_MODIFIER, FG_CURRENT, FG_PRERELEASE, FG_FESTIVAL, FG_ACTIVE_IA, FG_ALTERNATE_CONTENT) VALUES ( 27, 1, 5680, 'LA DAMA DE NEGRO 2 DIG ESP', '5680', 120, 1, 1, 1, '2015-02-18', 1, 1, 1, 0, 0, 0)
INSERT INTO K_EVENT ( ID_EVENT, ID_EVENT_TYPE, ID_VISTA, DS_NAME, DS_CODE_DBS, QT_DURATION, QT_COPY, FG_PREMIERE, FG_ACTIVE, DT_LAST_MODIFICATION, ID_LAST_USER_MODIFIER, FG_CURRENT, FG_PRERELEASE, FG_FESTIVAL, FG_ACTIVE_IA, FG_ALTERNATE_CONTENT) VALUES ( 28, 1, 5681, 'LA DAMA DE NEGRO 2 XE DIG ESP', '5681', 120, 1, 1, 1, '2015-02-18', 1, 1, 1, 0, 0, 0)

INSERT INTO K_EVENT_X_CATEGORY ( ID_EVENT, ID_CATEGORY ) VALUES ( 20, 6 )
INSERT INTO K_EVENT_X_CATEGORY ( ID_EVENT, ID_CATEGORY ) VALUES ( 21, 6 )
INSERT INTO K_EVENT_X_CATEGORY ( ID_EVENT, ID_CATEGORY ) VALUES ( 22, 6 )
INSERT INTO K_EVENT_X_CATEGORY ( ID_EVENT, ID_CATEGORY ) VALUES ( 23, 6 )
INSERT INTO K_EVENT_X_CATEGORY ( ID_EVENT, ID_CATEGORY ) VALUES ( 24, 6 )

insert into K_BOOKING (ID_BOOKING, ID_EVENT, ID_THEATER, FG_BOOKED, FG_ACTIVE, DT_LAST_MODIFICATION, ID_LAST_USER_MODIFIER, ID_BOOKING_TYPE) values (473, 1, 10, 1, 1, '2014-11-14 22:43:11', 2, 1 )
insert into K_BOOKING (ID_BOOKING, ID_EVENT, ID_THEATER, FG_BOOKED, FG_ACTIVE, DT_LAST_MODIFICATION, ID_LAST_USER_MODIFIER, ID_BOOKING_TYPE) values (485, 2, 10, 1, 1, '2014-11-14 22:33:50', 2, 1 )
insert into K_BOOKING (ID_BOOKING, ID_EVENT, ID_THEATER, FG_BOOKED, FG_ACTIVE, DT_LAST_MODIFICATION, ID_LAST_USER_MODIFIER, ID_BOOKING_TYPE) values (486, 3, 10, 1, 1, '2014-11-14 22:34:59', 2, 1 )

-- INSERT INTO C_BOOKING_TYPE ( ID_BOOKING_TYPE )  VALUES ( 1 )

insert into K_BOOKING_WEEK (ID_BOOKING_WEEK, ID_BOOKING, ID_BOOKING_STATUS, ID_WEEK, DT_EXHIBITION_END_DATE, NU_EXHIBITION_WEEK, QT_COPY, FG_SEND, FG_ACTIVE, DT_LAST_MODIFICATION, ID_LAST_USER_MODIFIER, QT_COPY_SCREEN_ZERO, QT_COPY_SCREEN_ZERO_TERMINATED) values (10654, 473, 1, 1, null, 1, 2, 0, 1, '2014-11-14 22:43:11', 2, 1, 0)
insert into K_BOOKING_WEEK (ID_BOOKING_WEEK, ID_BOOKING, ID_BOOKING_STATUS, ID_WEEK, DT_EXHIBITION_END_DATE, NU_EXHIBITION_WEEK, QT_COPY, FG_SEND, FG_ACTIVE, DT_LAST_MODIFICATION, ID_LAST_USER_MODIFIER, QT_COPY_SCREEN_ZERO, QT_COPY_SCREEN_ZERO_TERMINATED) values (10666, 485, 1, 1, null, 1, 1, 0, 1, '2014-11-14 22:33:50', 2, 0, 0)
insert into K_BOOKING_WEEK (ID_BOOKING_WEEK, ID_BOOKING, ID_BOOKING_STATUS, ID_WEEK, DT_EXHIBITION_END_DATE, NU_EXHIBITION_WEEK, QT_COPY, FG_SEND, FG_ACTIVE, DT_LAST_MODIFICATION, ID_LAST_USER_MODIFIER, QT_COPY_SCREEN_ZERO, QT_COPY_SCREEN_ZERO_TERMINATED) values (10667, 486, 1, 1, null, 1, 1, 0, 1, '2014-11-14 22:34:59', 2, 0, 0)

insert into K_BOOKING_WEEK_SCREEN (ID_BOOKING_WEEK_SCREEN, ID_BOOKING_WEEK, ID_BOOKING_STATUS, ID_SCREEN, ID_OBSERVATION) values (11170, 10654, 1, 11, null)
insert into K_BOOKING_WEEK_SCREEN (ID_BOOKING_WEEK_SCREEN, ID_BOOKING_WEEK, ID_BOOKING_STATUS, ID_SCREEN, ID_OBSERVATION) values (11171, 10654, 1, 12, null)
insert into K_BOOKING_WEEK_SCREEN (ID_BOOKING_WEEK_SCREEN, ID_BOOKING_WEEK, ID_BOOKING_STATUS, ID_SCREEN, ID_OBSERVATION) values (11174, 10654, 2, 13, null)
insert into K_BOOKING_WEEK_SCREEN (ID_BOOKING_WEEK_SCREEN, ID_BOOKING_WEEK, ID_BOOKING_STATUS, ID_SCREEN, ID_OBSERVATION) values (11182, 10654, 2, 17, null)
insert into K_BOOKING_WEEK_SCREEN (ID_BOOKING_WEEK_SCREEN, ID_BOOKING_WEEK, ID_BOOKING_STATUS, ID_SCREEN, ID_OBSERVATION) values (11183, 10654, 2, 16, null)
insert into K_BOOKING_WEEK_SCREEN (ID_BOOKING_WEEK_SCREEN, ID_BOOKING_WEEK, ID_BOOKING_STATUS, ID_SCREEN, ID_OBSERVATION) values (11185, 10666, 2, 15, null)
insert into K_BOOKING_WEEK_SCREEN (ID_BOOKING_WEEK_SCREEN, ID_BOOKING_WEEK, ID_BOOKING_STATUS, ID_SCREEN, ID_OBSERVATION) values (11186, 10666, 1, 16, null)
insert into K_BOOKING_WEEK_SCREEN (ID_BOOKING_WEEK_SCREEN, ID_BOOKING_WEEK, ID_BOOKING_STATUS, ID_SCREEN, ID_OBSERVATION) values (11187, 10667, 1, 18, null)


 -- <<< K_BOOKING >>> --
INSERT INTO K_BOOKING (ID_BOOKING, ID_EVENT, ID_THEATER, FG_BOOKED, FG_ACTIVE, DT_LAST_MODIFICATION, ID_LAST_USER_MODIFIER, ID_BOOKING_TYPE) VALUES (487, 1, 1, 1, 1, '2014-11-29 13:33:02', 2, 2)
INSERT INTO K_BOOKING (ID_BOOKING, ID_EVENT, ID_THEATER, FG_BOOKED, FG_ACTIVE, DT_LAST_MODIFICATION, ID_LAST_USER_MODIFIER, ID_BOOKING_TYPE) VALUES (488, 2, 2, 1, 1, '2014-11-29 13:33:02', 2, 2)
INSERT INTO K_BOOKING (ID_BOOKING, ID_EVENT, ID_THEATER, FG_BOOKED, FG_ACTIVE, DT_LAST_MODIFICATION, ID_LAST_USER_MODIFIER, ID_BOOKING_TYPE) VALUES (489, 3, 3, 1, 1, '2014-11-29 13:33:02', 2, 2)
INSERT INTO K_BOOKING (ID_BOOKING, ID_EVENT, ID_THEATER, FG_BOOKED, FG_ACTIVE, DT_LAST_MODIFICATION, ID_LAST_USER_MODIFIER, ID_BOOKING_TYPE) VALUES (490, 4, 4, 1, 1, '2014-11-29 13:33:02', 2, 2)
INSERT INTO K_BOOKING (ID_BOOKING, ID_EVENT, ID_THEATER, FG_BOOKED, FG_ACTIVE, DT_LAST_MODIFICATION, ID_LAST_USER_MODIFIER, ID_BOOKING_TYPE) VALUES (491, 5, 5, 1, 1, '2014-11-29 13:33:02', 2, 2)
 
 -- <<< K_BOOKING_SPECIAL_EVENT >>> --
INSERT INTO K_BOOKING_SPECIAL_EVENT (ID_BOOKING_SPECIAL_EVENT,ID_BOOKING, ID_BOOKING_STATUS, DT_START_SPECIAL_EVENT, DT_END_SPECIAL_EVENT, QT_COPY, FG_ACTIVE, DT_LAST_MODIFICATION, ID_LAST_USER_MODIFIER) VALUES (12,487, 1, '2014-11-29 13:33:02', '2014-11-29 14:33:02', 1, 1, '2014-11-29 13:33:02', 2)
INSERT INTO K_BOOKING_SPECIAL_EVENT (ID_BOOKING_SPECIAL_EVENT,ID_BOOKING, ID_BOOKING_STATUS, DT_START_SPECIAL_EVENT, DT_END_SPECIAL_EVENT, QT_COPY, FG_ACTIVE, DT_LAST_MODIFICATION, ID_LAST_USER_MODIFIER) VALUES (13,488, 1, '2014-11-29 13:33:02', '2014-11-29 14:33:02', 1, 1, '2014-11-29 13:33:02', 2)
INSERT INTO K_BOOKING_SPECIAL_EVENT (ID_BOOKING_SPECIAL_EVENT,ID_BOOKING, ID_BOOKING_STATUS, DT_START_SPECIAL_EVENT, DT_END_SPECIAL_EVENT, QT_COPY, FG_ACTIVE, DT_LAST_MODIFICATION, ID_LAST_USER_MODIFIER) VALUES (14,489, 1, '2014-11-29 13:33:02', '2014-11-29 14:33:02', 1, 1, '2014-11-29 13:33:02', 2) 
INSERT INTO K_BOOKING_SPECIAL_EVENT (ID_BOOKING_SPECIAL_EVENT,ID_BOOKING, ID_BOOKING_STATUS, DT_START_SPECIAL_EVENT, DT_END_SPECIAL_EVENT, QT_COPY, FG_ACTIVE, DT_LAST_MODIFICATION, ID_LAST_USER_MODIFIER) VALUES (15,490, 1, '2014-11-29 13:33:02', '2014-11-29 14:33:02', 1, 1, '2014-11-29 13:33:02', 2)
INSERT INTO K_BOOKING_SPECIAL_EVENT (ID_BOOKING_SPECIAL_EVENT,ID_BOOKING, ID_BOOKING_STATUS, DT_START_SPECIAL_EVENT, DT_END_SPECIAL_EVENT, QT_COPY, FG_ACTIVE, DT_LAST_MODIFICATION, ID_LAST_USER_MODIFIER) VALUES (16,491, 1, '2014-11-29 13:33:02', '2014-11-29 14:33:02', 1, 1, '2014-11-29 13:33:02', 2)
 
 -- <<< K_BOOKING_SPECIAL_EVENT_SCREEN >>> --
INSERT INTO K_BOOKING_SPECIAL_EVENT_SCREEN (ID_BOOKING_SPECIAL_EVENT_SCREEN,ID_BOOKING_SPECIAL_EVENT, ID_SCREEN, ID_BOOKING_STATUS) VALUES (25,12, 1, 1)
INSERT INTO K_BOOKING_SPECIAL_EVENT_SCREEN (ID_BOOKING_SPECIAL_EVENT_SCREEN,ID_BOOKING_SPECIAL_EVENT, ID_SCREEN, ID_BOOKING_STATUS) VALUES (26,13, 2, 1)
INSERT INTO K_BOOKING_SPECIAL_EVENT_SCREEN (ID_BOOKING_SPECIAL_EVENT_SCREEN,ID_BOOKING_SPECIAL_EVENT, ID_SCREEN, ID_BOOKING_STATUS) VALUES (27,14, 3, 1)
INSERT INTO K_BOOKING_SPECIAL_EVENT_SCREEN (ID_BOOKING_SPECIAL_EVENT_SCREEN,ID_BOOKING_SPECIAL_EVENT, ID_SCREEN, ID_BOOKING_STATUS) VALUES (28,15, 4, 1)
INSERT INTO K_BOOKING_SPECIAL_EVENT_SCREEN (ID_BOOKING_SPECIAL_EVENT_SCREEN,ID_BOOKING_SPECIAL_EVENT, ID_SCREEN, ID_BOOKING_STATUS) VALUES (29,16, 5, 1)
 
 -- <<< K_BOOKING_SPECIAL_EVENT_SCREEN_SHOW >>> --
INSERT INTO K_BOOKING_SPECIAL_EVENT_SCREEN_SHOW (ID_BOOKING_SPECIAL_EVENT_SCREEN_SHOW,ID_BOOKING_SPECIAL_EVENT_SCREEN, NU_SHOW) VALUES (30,1, 1)
INSERT INTO K_BOOKING_SPECIAL_EVENT_SCREEN_SHOW (ID_BOOKING_SPECIAL_EVENT_SCREEN_SHOW,ID_BOOKING_SPECIAL_EVENT_SCREEN, NU_SHOW) VALUES (31,1, 2)
INSERT INTO K_BOOKING_SPECIAL_EVENT_SCREEN_SHOW (ID_BOOKING_SPECIAL_EVENT_SCREEN_SHOW,ID_BOOKING_SPECIAL_EVENT_SCREEN, NU_SHOW) VALUES (32,2, 1)
INSERT INTO K_BOOKING_SPECIAL_EVENT_SCREEN_SHOW (ID_BOOKING_SPECIAL_EVENT_SCREEN_SHOW,ID_BOOKING_SPECIAL_EVENT_SCREEN, NU_SHOW) VALUES (33,2, 2)
INSERT INTO K_BOOKING_SPECIAL_EVENT_SCREEN_SHOW (ID_BOOKING_SPECIAL_EVENT_SCREEN_SHOW,ID_BOOKING_SPECIAL_EVENT_SCREEN, NU_SHOW) VALUES (34,3, 1)
INSERT INTO K_BOOKING_SPECIAL_EVENT_SCREEN_SHOW (ID_BOOKING_SPECIAL_EVENT_SCREEN_SHOW,ID_BOOKING_SPECIAL_EVENT_SCREEN, NU_SHOW) VALUES (35,3, 2)
INSERT INTO K_BOOKING_SPECIAL_EVENT_SCREEN_SHOW (ID_BOOKING_SPECIAL_EVENT_SCREEN_SHOW,ID_BOOKING_SPECIAL_EVENT_SCREEN, NU_SHOW) VALUES (36,4, 1)
INSERT INTO K_BOOKING_SPECIAL_EVENT_SCREEN_SHOW (ID_BOOKING_SPECIAL_EVENT_SCREEN_SHOW,ID_BOOKING_SPECIAL_EVENT_SCREEN, NU_SHOW) VALUES (37,4, 2)
INSERT INTO K_BOOKING_SPECIAL_EVENT_SCREEN_SHOW (ID_BOOKING_SPECIAL_EVENT_SCREEN_SHOW,ID_BOOKING_SPECIAL_EVENT_SCREEN, NU_SHOW) VALUES (38,5, 1)
INSERT INTO K_BOOKING_SPECIAL_EVENT_SCREEN_SHOW (ID_BOOKING_SPECIAL_EVENT_SCREEN_SHOW,ID_BOOKING_SPECIAL_EVENT_SCREEN, NU_SHOW) VALUES (39,5, 2)