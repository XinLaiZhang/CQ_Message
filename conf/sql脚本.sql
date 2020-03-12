/*建立用户表*/
create table M_User (
	U_ID int primary key auto_increment,
	Username varchar(50) not null unique,
	Password varchar(64) not null,
	Permission tinyint unsigned not null default 0
);
/*添加默认管理员*/
insert into m_user (Username, Password, Permission) values ('admin', '210CF7AA5E2682C9C9D4511F88FE2789', 9);


/*建立任务清单*/
create table MissionList(
	M_ID int primary key auto_increment,
	M_Title varchar(100) not null,
	M_Class tinyint unsigned check(M_Class in (0,1,2,3,4)),
	M_List varchar(100) not null,
	M_Status tinyint unsigned check(M_Status in (0,1,2,3)),
	M_Type tinyint unsigned check(M_Type in (0,1,2)),
	M_Groupnum varchar(50) not null,
	M_Own int not null,
	M_Text varchar(255),
	foreign key (M_Own) references m_user(U_ID)
);
/*建立统计任务表*/
create table CountMission (
	M_ID int primary key,
	M_Regex varchar(255) not null,
	M_Reply varchar(255) default '[]',
	M_StartTime bigint not null,
	M_EndTime bigint not null,
	M_RemindTime bigint default 0,
	M_SaveText varchar(255) default('[]'),
	foreign key (M_ID) references MissionList(M_ID)
);
/*建立提醒任务表*/
create table RemindMission (
	M_ID int primary key,
	M_StartTime bigint not null,
	M_EveryNum int unsigned default 0,
	M_I int unsigned default 0,
	M_Interval bigint default 0,
	foreign key (M_ID) references MissionList(M_ID)
);

/*建立统计任务视图*/
create view MissionCountList as select ml.M_ID as 'M_ID',M_Title,M_Class,M_list,M_Groupnum,M_Status,M_Type,M_Own,M_Regex,M_Reply,M_StartTime,M_EndTime,M_RemindTime,M_SaveText,M_Text from MissionList ml,CountMission cm where ml.M_ID = cm.M_ID;

/*建立提醒任务视图*/
create view MissionRemindList as select ml.M_ID as 'M_ID',M_Title,M_Class,M_list,M_Groupnum,M_Status,M_Type,M_Own,M_StartTime,M_Text,M_EveryNum,M_I,M_Interval from MissionList ml,RemindMission rm where ml.M_ID = rm.M_ID;

