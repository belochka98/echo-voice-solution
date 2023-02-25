-- Create user and database for user-service
CREATE
USER evoice_keycloak_db WITH ENCRYPTED PASSWORD 'evoice_keycloak_db';
CREATE
DATABASE keycloak_db OWNER evoice_keycloak_db;

-- Create user and database for user-service
CREATE
USER evoice_user_db WITH ENCRYPTED PASSWORD 'evoice_user_db';
CREATE
DATABASE user_db OWNER evoice_user_db;