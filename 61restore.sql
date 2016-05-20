drop database jackrabbitprd;
create database jackrabbitprd character set utf8;
use jackrabbitprd;
source ~/backups/uportland-backups/61jackrabbitprd.sql;

drop database pilotsprd;
create database pilotsprd character set utf8;
use pilotsprd;
source ~/backups/uportland-backups/61pilotsprd.sql;