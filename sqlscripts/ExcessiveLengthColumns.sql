DROP TABLE IF EXISTS __textColumns__liferay;

CREATE TABLE __textColumns__liferay(
	Id SERIAL	PRIMARY KEY,
	TableName	CHAR(100),
	ColumnName	CHAR(100)
);

INSERT INTO __textColumns__liferay(TableName, ColumnName)
VALUES
('AnnouncementsEntry', 'content'),
('AnnouncementsEntry', 'url'),
('AssetCategory', 'title'),
('AssetCategory', 'description'),
('AssetEntry', 'title'),
('AssetEntry', 'description'),
('AssetEntry', 'summary'),
('AssetEntry', 'url'),
('AssetVocabulary', 'title'),
('AssetVocabulary', 'description'),
('AssetVocabulary', 'settings_'),
('BlogsEntry', 'smallImageURL'),
('BookmarksEntry', 'url'),
('BookmarksEntry', 'description'),
('BookmarksFolder', 'description'),
('CalEvent', 'description'),
('CalEvent', 'location'),
('Company', 'homeURL'),
('DDLRecordSet', 'name'),
('DDLRecordSet', 'description'),
('DDMContent', 'name'),
('DDMContent', 'description'),
('DDMStructure', 'name'),
('DDMStructure', 'description'),
('DDMTemplate', 'name'),
('DDMTemplate', 'description'),
('DLFileEntry', 'description'),
('DLFileEntryType', 'description'),
('DLFileVersion', 'description'),
('DLFolder', 'description'),
('DLSync', 'description'),
('ExpandoColumn', 'defaultData'),
('ExpandoValue', 'data_'),
('Group_', 'description'),
('Group_', 'typeSettings'),
('JournalArticle', 'title'),
('JournalArticle', 'description'),
('JournalArticle', 'smallImageURL'),
('JournalFeed', 'description'),
('JournalStructure', 'name'),
('JournalStructure', 'description'),
('JournalTemplate', 'name'),
('JournalTemplate', 'description'),
('JournalTemplate', 'smallImageURL'),
('Layout', 'name'),
('Layout', 'title'),
('Layout', 'description'),
('Layout', 'keywords'),
('Layout', 'robots'),
('Layout', 'css'),
('LayoutBranch', 'description'),
('LayoutPrototype', 'name'),
('LayoutPrototype', 'description'),
('LayoutPrototype', 'settings_'),
('LayoutRevision', 'name'),
('LayoutRevision', 'title'),
('LayoutRevision', 'description'),
('LayoutRevision', 'keywords'),
('LayoutRevision', 'robots'),
('LayoutRevision', 'css'),
('LayoutSet', 'css'),
('LayoutSet', 'settings_'),
('LayoutSetBranch', 'description'),
('LayoutSetBranch', 'css'),
('LayoutSetBranch', 'settings_'),
('LayoutSetPrototype', 'name'),
('LayoutSetPrototype', 'description'),
('LayoutSetPrototype', 'settings_'),
('MBCategory', 'description'),
('MDRAction', 'name'),
('MDRAction', 'description'),
('MDRRule', 'name'),
('MDRRule', 'description'),
('MDRRuleGroup', 'name'),
('MDRRuleGroup', 'description'),
('MembershipRequest', 'comments'),
('MembershipRequest', 'replyComments'),
('Organization_', 'treePath'),
('Organization_', 'comments'),
('PasswordPolicy', 'description'),
('PluginSetting', 'roles'),
('PollsChoice', 'description'),
('PollsQuestion', 'title'),
('PollsQuestion', 'description'),
('Portlet', 'roles'),
('Repository', 'description'),
('Role_', 'title'),
('Role_', 'description'),
('SCFrameworkVersion', 'url'),
('SCLicense', 'url'),
('SCProductEntry', 'shortDescription'),
('SCProductEntry', 'longDescription'),
('SCProductEntry', 'pageURL'),
('SCProductVersion', 'changeLog'),
('SCProductVersion', 'downloadPageURL'),
('SCProductVersion', 'directDownloadURL'),
('ShoppingCart', 'itemIds'),
('ShoppingCategory', 'description'),
('ShoppingCoupon', 'description'),
('ShoppingCoupon', 'limitCategories'),
('ShoppingCoupon', 'limitSkus'),
('ShoppingItem', 'description'),
('ShoppingItem', 'properties'),
('ShoppingItem', 'fieldsQuantities'),
('ShoppingItem', 'smallImageURL'),
('ShoppingItem', 'mediumImageURL'),
('ShoppingItem', 'largeImageURL'),
('ShoppingItemField', 'values_'),
('ShoppingItemField', 'description'),
('ShoppingOrder', 'comments'),
('ShoppingOrderItem', 'description'),
('ShoppingOrderItem', 'properties'),
('SocialActivity', 'extraData'),
('SocialRequest', 'extraData'),
('Team', 'description'),
('User_', 'comments'),
('UserGroup', 'description'),
('UserTrackerPath', 'path_'),
('Website', 'url'),
('WikiNode', 'description'),
('WikiPage', 'summary');



CREATE OR REPLACE FUNCTION list_length(tableName CHAR(100), columnName CHAR(100))
RETURNS varchar(4000) AS $func$
BEGIN
	RETURN 'SELECT * FROM ' || tableName || ' WHERE LENGTH(' || columnName || ') len > 2000';
	
END
$func$ language plpgsql;

DO $$
BEGIN
	FOR counter IN 1..115 LOOP
		DECLARE
			tableName CHAR(100) := (SELECT TableName FROM __textColumns__liferay where Id = counter);
			columnName CHAR(100) := (SELECT ColumnName FROM __textColumns__liferay where Id = counter);
		BEGIN
			raise notice '%', (SELECT list_length(tableName, columnName));
		END;
	END LOOP;
END; $$

DROP TABLE __textColumns__liferay;
DROP FUNCTION list_length(character, character);