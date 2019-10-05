CREATE TABLE autotestcommodity (
  commodity_id VARCHAR(40) NOT NULL DEFAULT '',
  commodity_name VARCHAR(4000) NOT NULL DEFAULT '',
  commodity_code VARCHAR(40) NOT NULL DEFAULT '',
  apply_start_date VARCHAR(200) NOT NULL DEFAULT '',
  upc VARCHAR(13) NOT NULL DEFAULT '',
  expiration_date VARCHAR(200) NOT NULL DEFAULT '',
  qty_per_unit INT NOT NULL DEFAULT 0,
  other_attributes JSONB NOT NULL DEFAULT '{}',
  program_id VARCHAR(4000) NOT NULL DEFAULT '',
  create_date_time TIMESTAMP,
  create_user_id VARCHAR(200) DEFAULT '',
  PRIMARY KEY (commodity_id)
) WITH (OIDS=TRUE);