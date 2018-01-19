CREATE TABLE `roles` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='Описывает роли пользователей (администратор, пользователь и т.д.).Связь 1 к 1. Вынесено в отдельную таблицу, т.к. \n1) расширение функционала (может понадобится добаление характеристик)\n2) четкое ограничение набора  ролей (отпадает необходимость вносить константы)';

CREATE TABLE `accounts` (
  `account_number` int(10) NOT NULL AUTO_INCREMENT,
  `balance` decimal(5,2) unsigned NOT NULL DEFAULT '0.00',
  `loan` decimal(5,2) unsigned NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`account_number`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COMMENT='Описывает счета пользователей. Содержит уникальный номер счета, баланс и кредит. Столбец кредит хранит долг, если пользователь делал обещанный платеж.';

CREATE TABLE `genres` (
  `genre_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  `description` text,
  PRIMARY KEY (`genre_id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='Описывает жанры. Содержит имя и описание жанра(если есть). Вынесено в отдельную таблицу:\n1) является справочником (характеристикой любого произведения)\n2)  расширяемость';

CREATE TABLE `publication_types` (
  `publication_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  PRIMARY KEY (`publication_type_id`),
  UNIQUE KEY `rype_name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='Описывает типы изданий (журнал, газета, серия книг и т.д.). Связь 1 к 1, т.к.:\n1) является справочником (есть у каждого издания)\n2) расширяемость\n3) не надо использовать константы\n3) используется в связи многие ко многим';

CREATE TABLE `publications` (
  `publication_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `publication_type_id` int(11) NOT NULL,
  `genre_id` int(11) NOT NULL,
  `description` text,
  `price` decimal(5,2) NOT NULL,
  `picture_name` varchar(50) NOT NULL,
  `picture` blob,
  PRIMARY KEY (`publication_id`),
  KEY `publication_type_id_idx` (`publication_type_id`),
  KEY `genre_id_idx` (`genre_id`),
  CONSTRAINT `genre_id` FOREIGN KEY (`genre_id`) REFERENCES `genres` (`genre_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `publication_type_id` FOREIGN KEY (`publication_type_id`) REFERENCES `publication_types` (`publication_type_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='Описывает издания. Содержит имя, тип (газета, книга и т.д.), жанр и описание (аннотация - если есть) и цену подписки.';

CREATE TABLE `authors` (
  `author_id` int(11) NOT NULL AUTO_INCREMENT,
  `publisher_name` varchar(30) NOT NULL,
  `author_lastname` varchar(30) DEFAULT NULL,
  `author_firstname` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`author_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='Описывает издателя и автора(если есть). Содержит имя издателя, имя и фамилию автора(если есть).Автора может не быть, например, у газеты. Зато и у газеты, и у книги есть издатель.';

CREATE TABLE `authors_publications` (
  `author_id` int(11) NOT NULL,
  `publication_id` int(11) NOT NULL,
  PRIMARY KEY (`author_id`,`publication_id`),
  KEY `authors_publications_ibfk_2` (`publication_id`),
  CONSTRAINT `authors_publications_ibfk_1` FOREIGN KEY (`author_id`) REFERENCES `authors` (`author_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `authors_publications_ibfk_2` FOREIGN KEY (`publication_id`) REFERENCES `publications` (`publication_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Связь многие ко многим. У автора может быть несколько изданий и у издания может быть несколько авторов.';

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL,
  `lastname` varchar(30) NOT NULL,
  `firstname` varchar(30) NOT NULL,
  `birthdate` date NOT NULL,
  `address` varchar(30) NOT NULL,
  `city` varchar(30) NOT NULL,
  `postal_index` int(10) unsigned NOT NULL,
  `account_number` int(10) NOT NULL,
  `login` varchar(30) NOT NULL,
  `password` varchar(100) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `login_UNIQUE` (`login`),
  KEY `role_id_idx` (`role_id`),
  KEY `account_number_idx` (`account_number`),
  CONSTRAINT `account_number` FOREIGN KEY (`account_number`) REFERENCES `accounts` (`account_number`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `role_id` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='Описывает информацию о пользователе. Содержит роль, имя и фамилию, дату рождения, адрес, город, почтовый индес, номер счета, логин и пароль. Столбец с датой рождения является типом date, т.к. время в данном случае является избыточной характеристикой. Логин является уникальной характеристикой.';

CREATE TABLE `block` (
  `user_id` int(11) NOT NULL,
  `admin_id` int(11) NOT NULL,
  `date` datetime NOT NULL,
  PRIMARY KEY (`user_id`,`admin_id`),
  KEY `block_ibfk_2` (`admin_id`),
  CONSTRAINT `block_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `block_ibfk_2` FOREIGN KEY (`admin_id`) REFERENCES `users` (`user_id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Таблица содержит информацию о заблокированных пользователях: id пользователя, id администратора (который заблокировал), дата блокировки. При разблокировании пользователя его строка удаляется из таблицы. Пользователь блокируется при наличии большой суммы кредита и ее непогашения в течении месяца. Администратор сам устанавливает порог кредита.';

CREATE TABLE `subscriptions` (
  `subscription_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `publication_id` int(11) NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `subscription_is_active` varchar(2) NOT NULL COMMENT 'y - если подписка активна, n - если срок подписки истек.',
  PRIMARY KEY (`subscription_id`),
  KEY `user_id_idx` (`user_id`),
  KEY `publication_id_idx` (`publication_id`),
  CONSTRAINT `publication_id` FOREIGN KEY (`publication_id`) REFERENCES `publications` (`publication_id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='Описывает подписки (факт подписки и отписки от издания). Подписка содержит информацию о пользователе, издание (предмет подписки), даты начала и окончания подписки(тип date, т.к. время избыточно)  и флаг активности подписки. (y - подписка активна, n - нет). Если не вносить сюда id пользователя, а делать наоборот - id подписки внести в информацию о пользователе, то получится, что у пользователя иожет быть только одна подписка - что неверно.';

CREATE TABLE `payments` (
  `payment_number` int(11) NOT NULL AUTO_INCREMENT,
  `subscription_id` int(11) NOT NULL,
  `sum` decimal(5,2) NOT NULL,
  `date` datetime NOT NULL,
  `statement` varchar(45) NOT NULL,
  PRIMARY KEY (`payment_number`),
  KEY `subscription_id_idx` (`subscription_id`),
  CONSTRAINT `subscription_id` FOREIGN KEY (`subscription_id`) REFERENCES `subscriptions` (`subscription_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='Содеждит информацию о платеже пользователя: информация о пользователе, подписка, сумма платежа, дата и время и состояние (y - платеж прошел, n - платеж не выполнился).';
