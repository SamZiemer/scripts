--use DB_NAME_HERE

-- Strip all key user information
update User_
set
screenName = '' + SUBSTRING(screenName,1,2) + CAST(userId as varchar),
emailAddress = '' + CAST(userId as varchar) + '@mailinator.com',
greeting = 'Welcome ' + CAST(userId as varchar),
firstName = '' + CAST(userId as varchar),
middleName = '',
lastName = 'User',
password_='password',
passwordEncrypted=0;

update Address
set
street1 = '5555 fake street',
street2 = '',
street3 = '',
city = 'anywhereville',
zip = '00000',
regionId = 0;

update Contact_
set
emailAddress = '' + CAST(userId as varchar) + '@mailinator.com',
firstName = '',
middleName = '',
lastName = '',
prefixId = 0,
suffixId = 0,
male = 3,
birthday = '1970-01-01 00:00:00',
smsSn = '',
aimSn = '',
facebookSn = '',
icqSn = '',
jabberSn = '',
msnSn = '',
mySpaceSn = '',
skypeSn = '',
twitterSn = '',
ymSn = '',
employeeStatusId = '',
employeeNumber = '',
jobTitle = '',
jobClass = '',
hoursOfOperation = '';

update CyrusUser
set
password_ = 'password'

update CyrusVirtual
set
emailAddress = ''

update DDLRecord
set versionUserName = (SELECT screenName FROM User_ where DDLRecord.versionUserId = User_.userId)

update EmailAddress
set address = '';

update MBMailingList
set
emailAddress = '' + CAST(userId as varchar) + '@mailinator.com';

update Phone
set
number_ = '',
extension = '';

update ShoppingOrder
set
billingFirstName = '',
billingLastName = '',
billingEmailAddress = '',
billingCompany = '',
billingStreet = '',
billingCity = '',
billingState = '',
billingZip = '',
shippingFirstName = '',
shippingLastName = '',
shippingEmailAddress = '',
shippingCompany = '',
shippingStreet = '',
shippingCity = '',
shippingState = '',
shippingZip = '',
shippingCountry = '',
shippingPhone = '',
ccName = '',
ccType = '',
ccNumber = '',
ccExpMonth = 0,
ccExpYear = 0,
ccVerNumber = 0,
comments = '',
ppPayerEmail = '',
ppReceiverEmail = '';

-- ################# REMOVE UserName ################ --
create table #screenName
(
ID INT IDENTITY(0,1) primary key,
tableName nvarchar(200)
);

insert into #screenName
SELECT TABLE_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE COLUMN_NAME = 'UserName' ORDER BY TABLE_NAME;

DECLARE @cnt INT = 0;

DECLARE @cnt_ttl INT = (SELECT COUNT(*) FROM #screenName);

WHILE @cnt < @cnt_ttl
BEGIN
	DECLARE @tblNm nvarchar(200) = (SELECT tableName from #screenName where ID = @cnt);
	PRINT 'updating ' + @tblNm;
	EXEC('update ' + @tblNm + ' set userName = (SELECT screenName FROM User_ where ' + @tblNm + '.userId = User_.userId)');
	SET @cnt = @cnt+1;
END

drop table #screenName;
-- ################# END REMOVE UserName ################ --

-- ################# REMOVE statusByUserName ################ --
create table #statusByUserName
(
ID INT IDENTITY(0,1) primary key,
tableName nvarchar(200)
);

insert into #statusByUserName
SELECT TABLE_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE COLUMN_NAME = 'statusByUserName' ORDER BY TABLE_NAME;

set @cnt = 0;

set @cnt_ttl = (SELECT COUNT(*) FROM #statusByUserName);

WHILE @cnt < @cnt_ttl
BEGIN
	set @tblNm = (SELECT tableName from #statusByUserName where ID = @cnt);
	PRINT 'updating ' + @tblNm;
	EXEC('update ' + @tblNm + ' set userName = (SELECT statusByUserName FROM User_ where ' + @tblNm + '.userId = User_.userId)');
	SET @cnt = @cnt+1;
END

drop table #statusByUserName;
-- ################# END REMOVE statusByUserName ################ --

-- Switch company to localhost
update Company
set mx = 'localhost';

-- Change remote publishing urls
update Group_
SET typeSettings = REPLACE(typeSettings, 'localhost', 'lfe01.web.sesameworkshop.org')
where type_ = 1;

-- Clear any publishing tasks from the queue
delete from QUARTZ_CRON_TRIGGERS;
delete from QUARTZ_JOB_DETAILS;
delete from QUARTZ_TRIGGERS;