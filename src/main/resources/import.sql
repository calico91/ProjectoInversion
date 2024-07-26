-- crear roles
INSERT INTO roles (name) VALUES ('SUPER'),('ADMIN'),('COBRADOR'),('GENERICO');
-- crear super ususario
INSERT INTO user (country,email,firstname,lastname,password,username) VALUES ('Colombia','super@gmail.com','Super','Super','$2a$10$b4ffcTyaDA6jcq4ihwF23.vHW1UuXsvOlkVlfXouYjq19zXUKr/6W','super');
-- asignar rol a super usuario
INSERT INTO user_roles (user_id,role_id) VALUES (1,1);
-- se crea modalidades
INSERT INTO modalidad (description) VALUES ("mensual"),("quincenal");





