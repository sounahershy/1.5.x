ALTER TABLE customer_position CHANGE COLUMN CUSTOMER_POSITION_ID CUSTOMER_POSITION_ID INTEGER NOT NULL AUTO_INCREMENT;

UPDATE DATABASE_VERSION SET DATABASE_VERSION = 220 WHERE DATABASE_VERSION = 219;