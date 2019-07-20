-- *********************************************************************
-- Rollback 1 Change(s) Script
-- *********************************************************************
-- Change Log: migrations.xml
-- Ran at: 7/20/19, 3:24 PM
-- Against: user@jdbc:postgresql://localhost:5432/DropBookmarks
-- Liquibase version: 3.4.1
-- *********************************************************************

-- Lock Database
UPDATE databasechangeloglock SET LOCKED = TRUE, LOCKEDBY = 'fe80:0:0:0:7a1:dd47:40df:4725%utun0 (fe80:0:0:0:7a1:dd47:40df:4725%utun0)', LOCKGRANTED = '2019-07-20 15:24:03.564' WHERE ID = 1 AND LOCKED = FALSE;

-- Rolling Back ChangeSet: migrations.xml::3::dima
DELETE FROM users WHERE id=1;

DELETE FROM databasechangelog WHERE ID = '3' AND AUTHOR = 'dima' AND FILENAME = 'migrations.xml';

-- Release Database Lock
UPDATE databasechangeloglock SET LOCKED = FALSE, LOCKEDBY = NULL, LOCKGRANTED = NULL WHERE ID = 1;

