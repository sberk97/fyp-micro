CREATE DATABASE IF NOT EXISTS `advert_service`;

CREATE DATABASE IF NOT EXISTS `zuul_server`;

CREATE USER `advert`@`%` IDENTIFIED BY 'pass';
CREATE USER `zuul`@`%` IDENTIFIED BY 'pass';

GRANT ALL PRIVILEGES ON `advert_service`.* TO `advert`@`%`;

GRANT ALL PRIVILEGES ON `zuul_server`.* TO `zuul`@`%`;

