ALTER TABLE ACCOUNT_PAYMENT ADD COLUMN COMMENT VARCHAR(80);

UPDATE DATABASE_VERSION SET DATABASE_VERSION = 219 WHERE DATABASE_VERSION = 218;
