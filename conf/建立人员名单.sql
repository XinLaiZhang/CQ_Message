/*建立人员名单*/
create table test (
	N_ID int primary key auto_increment,
	N_Name varchar(255) default '',
	N_QQ int unsigned not null,
	N_Status tinyint unsigned default 0,
	N_SaveText varchar(255) default '[]'
);