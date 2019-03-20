show user;

select *
from member;

-- ROOMTYPE(숙소 유형)
comment on table ROOMTYPE
is '숙소 유형 테이블';

comment on column ROOMTYPE.ROOMTYPE_IDX
is '숙소 유형 인덱스 번호';

comment on column ROOMTYPE.ROOMTYPE_NAME
is '숙소 유형 명';

-- BUILDTYPE(건물유형)
comment on table BUILDTYPE
is '건물 유형 테이블';

comment on column BUILDTYPE.BUILDTYPE_IDX
is '건물 유형 인덱스 번호';

comment on column BUILDTYPE.BUILDTYPE
is '건물 유형 명';

-- BUILDTYPE_DETAIL(건물세부유형)
comment on table BUILDTYPE_DETAIL
is '건물 세부 유형 테이블';

comment on column BUILDTYPE_DETAIL.BUILDTYPE_DETAIL_IDX
is '건물 세부 유형 인덱스 번호';

comment on column BUILDTYPE_DETAIL.FK_BUILDTYPE_IDX
is '건물 유형 인덱스 번호(외래키)';

comment on column BUILDTYPE_DETAIL.BUILDTYPE_DETAIL
is '건물 세부 유형 명';

-- 신고 테이블(Report)
comment on table REPORT
is '신고 테이블';

comment on column REPORT.REPORT_IDX
is '신고 컬럼 인덱스 번호';

comment on column REPORT.FK_USERID
is '신고 글 작성자 아이디(외래키)';

comment on column REPORT.REPORTTYPE
is '신고 유형';

comment on column REPORT.REPORT_CONTENT
is '신고 내용';

comment on column REPORT.REPORT_DATE
is '신고 글 작성 날짜';

comment on column REPORT.REPORT_STATUS
is '신고 글 처리 상태';


ALTER TABLE REPORT RENAME COLUMN REPORT_WARNCOUNT TO REPORT_STATUS;
commit;

select *
from user_sequences;
select *
from user_tab_comments;



select *
from options;

desc room;

select *
from member;



desc mycoupon;

SELECT data_default
FROM user_tab_cols 
WHERE table_name = UPPER('mycoupon')  
AND column_name = UPPER('usedate');

ALTER TABLE mycoupon add (useDate date);
commit;
select *
from mycoupon;

desc mycoupon;



select ROOMCODE, FK_USERID, FK_ROOMTYPE_IDX, ROOMNAME, ROOMMAINIMG, ROOMTEL, ROOMPOST, ROOMSIGUNGU, ROOMSIDO, ROOMBNAME, ROOMPRICE, PEAKPER,
       CLEANPAY, BASIC_PERSON, MAX_PERSON, PERSON_ADDPAY, ROOMCOUNT, BATHCOUNT, CHECKINTIME, CHECKOUTTIME, LATITUDE, LONGITUDE, VIEWCOUNT,
       ROOMSTATUS, ROOM_WARNCOUNT, FK_BUILDTYPE_DETAIL_IDX, ROOMINFO            
from room
order by roomcode desc;


select *
from room;

select *
from reservation;

-- 메인페이지에서 추천 숙소 보여줄 7개 설정
select ROOMCODE, SCORE, ROOMMAINIMG, ROOMNAME, PROFILEIMG, ROOMPRICE, RNO 
from(
    select distinct(roomcode) AS ROOMCODE, avg((correct+communicate+clean+position+checkin+value)) AS SCORE, roomMainImg, roomname, C.profileImg as profileImg, roomprice, rownum as RNO
    from room A JOIN review B
    on A.roomcode = B.fk_roomcode
    join member C
    on B.fk_userid = C.userid 
    group by roomcode, roomname, C.profileImg, roomprice, roomMainImg, 
    rownum
)V
where RNO between 1 and 7;

select *
from review;

select *
from room A join member B
on A.fk_userid = B.userid;

select ROWNUM, ROOMCODE, ROOMNAME, ROOMMAINIMG, ROOMPRICE, PROFILEIMG
	from(
		select ROOMCODE, FK_USERID, FK_ROOMTYPE_IDX, ROOMNAME, ROOMMAINIMG, ROOMTEL, ROOMPOST, ROOMSIGUNGU, ROOMSIDO, ROOMBNAME, ROOMPRICE, PEAKPER,
	       	   CLEANPAY, BASIC_PERSON, MAX_PERSON, PERSON_ADDPAY, ROOMCOUNT, BATHCOUNT, CHECKINTIME, CHECKOUTTIME, LATITUDE, LONGITUDE, VIEWCOUNT,
	           ROOMSTATUS, ROOM_WARNCOUNT, FK_BUILDTYPE_DETAIL_IDX, ROOMINFO, ROWNUM, PROFILEIMG         
		from room A JOIN member B
		on A.fk_userid = B.userid
	)V
	where ROWNUM between 1 and 7


insert into room(ROOMCODE, FK_USERID, fk_buildType_detail_idx, FK_ROOMTYPE_IDX, ROOMNAME, ROOMMAINIMG, ROOMTEL, ROOMINFO, ROOMPOST, 
                 ROOMSIDO,ROOMSIGUNGU,  ROOMBNAME , ROOMDETAILADDR, ROOMPRICE, PEAKPER, CLEANPAY, BASIC_PERSON, MAX_PERSON, PERSON_ADDPAY, 
                 ROOMCOUNT, BATHCOUNT, CHECKINTIME,
                 CHECKOUTTIME, LATITUDE, LONGITUDE, VIEWCOUNT, ROOMSTATUS, ROOM_WARNCOUNT, ROOMIMGFILENAME) 
values(ROOMCODE_seq.nextval,
       'daiunii', 3, 3, 'Joy HouseA',
       'https://a0.muscache.com/im/pictures/6d077dd4-fb9a-4f79-b5e6-2da65189a8db.jpg?aki_policy=xx_large',
       1231561,
       '직접 전 세계 여행을 했었던 소품들로 가득 개인의 소중한 추억들이 담겨있는 방 세계여행 소품으로 꾸며진 MOMOROOM 입니다. '
       , 15134,
       '인천광역시', '중구','인현동', '20-6', 30500, 20, 50000, 4, 6, 20000, 4, 2, 
       to_date('2019/01/20 18:00:00','yyyy/mm/dd hh24:mi:ss'),
       to_date('2019/01/30 13:00:00','yyyy/mm/dd hh24:mi:ss'),
       37.475218, 126.630540,default,default,default,
       'aceguesthouse.jpg');
       
commit;

select *
from member;

select ROOMCODE, ROOMSIDO, ROOMSIGUNGU, ROOMBNAME, ROOMDETAILADDR
from room;

update room set ROOMBNAME = '화정동' , ROOMDETAILADDR = '185-10'
where roomcode = 23;

desc room;


select *
from reservation;


desc review;

select *
from review;
select *
from room;

select * from user_sequences;

insert into review(REVIEW_IDX, FK_ROOMCODE, FK_USERID, CORRECT, COMMUNICATE, CLEAN, POSITION, CHECKIN, VALUE, REVIEW_CONTENT, REVIEW_WRITEDATE)
values(REVIEW_IDX_SEQ.nextval, '23', 'leess', 1, 1, 2, 1,2, 1, '최악.....', to_date('2019-01-14', 'yyyy-mm-dd'));

commit;
select REVIEW_IDX_SEQ.nextval
from dual;



 select FK_USERID, REVIEW_CONTENT, REVIEW_WRITEDATE
 from review A JOIN room B
 on A.fk_roomcode = B.roomcode;
 
select FK_USERID, REVIEW_CONTENT, ROOMNAME, REVIEW_WRITEDATE, PROFILEIMG
from 
(select A.FK_USERID, REVIEW_CONTENT, ROOMNAME, REVIEW_WRITEDATE, PROFILEIMG
from review A JOIN room B
on A.fk_roomcode = B.roomcode
JOIN member C
on B.fk_userid = C.userid 
order by to_date(REVIEW_WRITEDATE,'yyyy-mm-dd hh24:mi:ss') desc
)V
where rownum between 1 and 2;

select *
from review A join room B
on A.fk_roomcode = B.roomcode;

update review set review_content = '1. 화장실이 조금 좁아요 (+수건걸이가 샤워기에 조금 더 떨어져 있으면 좋겠어요.) 2. 주방이 분리되어 있어서 음식하는냄새가 안들어와서 좋아요. 3. 침대가 넓어서 두명도 다 사용할수 있어요! 4. 보일러가 올라오는게 조금 늦어요♥ 5. 큰거울이 있어서 너무 편했어요. 6. 곳곳에 게스트분들을 위해서 준비해놓게 너무 기분 좋았어요! +7. 버스터미널이랑 가까우니깐 광주여행 오신분들은 꼭 이용하셨으면 좋겠어요^^'
where fk_roomcode = 23;
commit;

select roomsido, roomsigungu, roombname, latitude,longitude
from room;

select *
from member; 

update room set roomsido = '광주광역시' , roomsigungu = '서구 ' ,roombname = '화정동 185-10'
where latitude = 35.153572 and longitude = 126.883984;
commit;


select roommainimg, roomname, roomsido, roomsigungu, roombname, username, roomprice
from room A JOIN member B
on A.fk_userid = B.userid;

desc room;

select *
from room;


select *
from member;

select ROOMMAINIMG, ROOMNAME, ROOMSIDO, ROOMSIGUNGU, ROOMBNAME, USERNAME, ROOMPRICE, RNO
from(
    select ROOMMAINIMG, ROOMNAME, ROOMSIDO, ROOMSIGUNGU, ROOMBNAME, USERNAME, ROOMPRICE, ROWNUM AS RNO
    from(
        select ROOMCODE, ROOMMAINIMG, ROOMNAME, ROOMSIDO, ROOMSIGUNGU, ROOMBNAME, USERNAME, ROOMPRICE
        from room A JOIN member B
        on A.fk_userid = B.userid
        where A.ROOMSTATUS = 1
        order by ROOMCODE desc
        
    ) 
)
where RNO between 1 and 10;


select *
from review;


select *
from reservation;

select *
from(
select nvl(REVIEW_IDX, -9999) as REVIEW_IDX
from review A RIGHT JOIN reservation B
on A.F = B.FK_USERID
)
where review_idx = -9999 ;

update room set roomstatus = 0
where roomcode = 23;

select roomstatus
from room
where roomcode = 23;

rollback;

select *
from member;

update member set userid = 'admin'
where username ='leess';

select *
from room
order by roomcode desc;

select *
from review;

update room set ROOMSTATUS = 99
where ROOMCODE in (24,25,26);
commit;


select ROOMCODE, ROOMMAINIMG, ROOMNAME, ROOMPRICE
    from(
       select distinct(roomcode), avg((correct+communicate+clean+position+checkin+value)) AS SCORE, roomname, roomprice, roomMainImg
        from room A JOIN review B
        on A.roomcode = B.fk_roomcode
        join member C
        on B.fk_userid = C.userid 
        group by roomcode, roomname, roomprice, roomMainImg 
    )V
where rownum between 1 and 7;

select profileImg
from member A JOIN room B
on A.userid = B.fk_userid
where B.roomcode = 'R2179';


select *
from review;










