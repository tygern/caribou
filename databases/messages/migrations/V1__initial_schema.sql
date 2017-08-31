CREATE TABLE lists (
  id    VARCHAR(255),
  title VARCHAR(255),

  PRIMARY KEY (id)
)
  DEFAULT CHARSET = utf8;

CREATE TABLE message_lists (
  list_id    VARCHAR(255),
  message_id VARCHAR(255),

  PRIMARY KEY (list_id, message_id)
)
  DEFAULT CHARSET = utf8;
