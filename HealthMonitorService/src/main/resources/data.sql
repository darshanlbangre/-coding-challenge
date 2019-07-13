DROP TABLE IF EXISTS subscriber;

CREATE TABLE subscriber
(
    id    INT AUTO_INCREMENT PRIMARY KEY,
    name  VARCHAR(250) NOT NULL,
    email VARCHAR(250) NOT NULL
);

INSERT INTO subscriber (name, email)
VALUES ('Darshan', 'darshan.bangre@gmail.com');
INSERT INTO subscriber (name, email)
VALUES ('Amrutha', 'amrutha.br@gmail.com');