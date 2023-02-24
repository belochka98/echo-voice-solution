-- Create user and database for user-service
CREATE
USER evoice_keycloack_db WITH ENCRYPTED PASSWORD 'evoice_keycloack_db';
CREATE
DATABASE keycloack_db OWNER evoice_keycloack_db;

-- Create user and database for user-service
CREATE
USER evoice_user_db WITH ENCRYPTED PASSWORD 'evoice_user_db';
CREATE
DATABASE user_db OWNER evoice_user_db;