use sync; //database name to use

select classNameId from ClassName_ where value = 'com.liferay.portal.model.User';
select mx from Company;

select
  count(*)
from AssetEntry inner join Group_
    on (AssetEntry.classNameId = Group_.classNameId and AssetEntry.classPK = Group_.classPK)
where AssetEntry.classNameId = 10005;

insert into User_
  (uuid_, userId, companyId, createDate, modifiedDate, defaultUser, contactId, password_, passwordEncrypted, screenName, emailAddress, firstName, agreedToTermsOfUse, status)
select
  AssetEntry.classUuid as uuid_,
  AssetEntry.classPK as userId,
  AssetEntry.companyId as companyId,
  AssetEntry.createDate as createDate,
  AssetEntry.modifiedDate as modifiedDate,
  false as defaultUser,
  AssetEntry.classPK + 1 as contactId,
  'test' as password_,
  0 as passwordEncrypted,
  substring(Group_.friendlyURL, 2) as screenName,
  concat(substring(Group_.friendlyURL, 2), '@swansdoor.org') as emailAddress,
  AssetEntry.title as firstName,
  1 as agreedToTermsOfUse,
  0 as status
from AssetEntry inner join Group_
    on (AssetEntry.classNameId = Group_.classNameId and AssetEntry.classPK = Group_.classPK)
where AssetEntry.classNameId = 10005;

select * from User_;
DELETE from Contact_;
select * from Contact_;


select accountId from Account_;

insert into Contact_
  (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, firstName, classNameId, classPK, emailAddress)
select
  contactId,
  companyId,
  userId,
  screenName as userName,
  createDate,
  modifiedDate,
  10156 as accountId,
  firstName,
  10005 as classNameId,
  userId as classPK,
  emailAddress
from User_;  

commit;
