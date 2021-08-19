-- SET PASSWORDS BEFORE RUNNING USER SCRIPT
CREATE USER 'u_puff_dev'@'%' IDENTIFIED BY '${DEV_PASSWORD}';
GRANT DELETE ON puff_dev.* TO 'u_puff_dev'@'%';
GRANT INSERT ON puff_dev.* TO 'u_puff_dev'@'%';
GRANT SELECT ON puff_dev.* TO 'u_puff_dev'@'%';
GRANT UPDATE ON puff_dev.* TO 'u_puff_dev'@'%';

CREATE USER 'u_puff_qa_ro'@'%' IDENTIFIED BY '${QA_PASSWORD}';
GRANT SELECT ON puff_dev.* TO 'u_puff_qa_ro'@'%';

CREATE USER 'u_puff_service'@'%' IDENTIFIED BY '${SERVICE_PASSWORD}';
GRANT DELETE ON puff_dev.* TO 'u_puff_service'@'%';
GRANT INSERT ON puff_dev.* TO 'u_puff_service'@'%';
GRANT SELECT ON puff_dev.* TO 'u_puff_service'@'%';
GRANT UPDATE ON puff_dev.* TO 'u_puff_service'@'%';

FLUSH PRIVILEGES;