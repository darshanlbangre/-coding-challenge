DROP TABLE IF EXISTS subscribers;

CREATE TABLE subscribers (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  name VARCHAR(250) NOT NULL,
  email VARCHAR(250) NOT NULL
  );

INSERT INTO subscribers (name, email) VALUES
  ('Darshan','darshan.bangre@gmail.com');