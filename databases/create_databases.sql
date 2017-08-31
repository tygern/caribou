DROP DATABASE IF EXISTS lists;
DROP DATABASE IF EXISTS lists_test;
DROP DATABASE IF EXISTS messages;
DROP DATABASE IF EXISTS messages_test;

CREATE USER IF NOT EXISTS 'caribou'@'localhost'
  IDENTIFIED BY '';
GRANT ALL PRIVnILEGES ON *.* TO 'caribou' @'localhost';

create DATABASE lists;
create DATABASE lists_test;
create DATABASE messages;
create DATABASE messages_test;
