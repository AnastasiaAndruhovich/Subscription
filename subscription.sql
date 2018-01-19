-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: subscription
-- ------------------------------------------------------
-- Server version	5.7.20-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `accounts`
--

DROP TABLE IF EXISTS `accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `accounts` (
  `account_number` int(10) NOT NULL AUTO_INCREMENT,
  `balance` decimal(5,2) unsigned NOT NULL DEFAULT '0.00',
  `loan` decimal(5,2) unsigned NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`account_number`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COMMENT='Описывает счета пользователей. Содержит уникальный номер счета, баланс и кредит. Столбец кредит хранит долг, если пользователь делал обещанный платеж.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accounts`
--

LOCK TABLES `accounts` WRITE;
/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;
INSERT INTO `accounts` VALUES (1,32.30,0.00),(2,0.00,0.00),(3,3.20,0.00),(4,3.40,0.00),(5,37.40,0.00),(9,0.00,2.30),(10,2.30,0.00),(11,0.00,4.20);
/*!40000 ALTER TABLE `accounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `authors`
--

DROP TABLE IF EXISTS `authors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `authors` (
  `author_id` int(11) NOT NULL AUTO_INCREMENT,
  `publisher_name` varchar(30) NOT NULL,
  `author_lastname` varchar(30) DEFAULT NULL,
  `author_firstname` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`author_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='Описывает издателя и автора(если есть). Содержит имя издателя, имя и фамилию автора(если есть).Автора может не быть, например, у газеты. Зато и у газеты, и у книги есть издатель.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authors`
--

LOCK TABLES `authors` WRITE;
/*!40000 ALTER TABLE `authors` DISABLE KEYS */;
INSERT INTO `authors` VALUES (1,'АСТ','Сергей','Лукьяненко'),(2,'АСТ','Александр','Пушкин'),(3,'Росмэн','Джоан','Роулинг'),(4,'Аргументы и факты','-','-'),(5,'Вечерний Минск','-','-'),(6,'Газеты и журналы','-','-'),(7,'Росмэн','-','братья Гримм'),(8,'Pearson','Жюль','Верн'),(9,'Астрель','Ганс Христиан','Андерсен'),(10,'Астрель','Шарль','Перро');
/*!40000 ALTER TABLE `authors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `authors_publications`
--

DROP TABLE IF EXISTS `authors_publications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `authors_publications` (
  `author_id` int(11) NOT NULL,
  `publication_id` int(11) NOT NULL,
  PRIMARY KEY (`author_id`,`publication_id`),
  KEY `authors_publications_ibfk_2` (`publication_id`),
  CONSTRAINT `authors_publications_ibfk_1` FOREIGN KEY (`author_id`) REFERENCES `authors` (`author_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `authors_publications_ibfk_2` FOREIGN KEY (`publication_id`) REFERENCES `publications` (`publication_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Связь многие ко многим. У автора может быть несколько изданий и у издания может быть несколько авторов.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authors_publications`
--

LOCK TABLES `authors_publications` WRITE;
/*!40000 ALTER TABLE `authors_publications` DISABLE KEYS */;
INSERT INTO `authors_publications` VALUES (3,1),(2,2),(8,3),(7,4),(4,5),(6,6),(1,7),(5,8),(10,9),(8,10);
/*!40000 ALTER TABLE `authors_publications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `block`
--

DROP TABLE IF EXISTS `block`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `block` (
  `user_id` int(11) NOT NULL,
  `admin_id` int(11) NOT NULL,
  `date` datetime NOT NULL,
  PRIMARY KEY (`user_id`,`admin_id`),
  KEY `block_ibfk_2` (`admin_id`),
  CONSTRAINT `block_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `block_ibfk_2` FOREIGN KEY (`admin_id`) REFERENCES `users` (`user_id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Таблица содержит информацию о заблокированных пользователях: id пользователя, id администратора (который заблокировал), дата блокировки. При разблокировании пользователя его строка удаляется из таблицы. Пользователь блокируется при наличии большой суммы кредита и ее непогашения в течении месяца. Администратор сам устанавливает порог кредита.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `block`
--

LOCK TABLES `block` WRITE;
/*!40000 ALTER TABLE `block` DISABLE KEYS */;
INSERT INTO `block` VALUES (7,2,'2016-03-01 00:00:00'),(10,2,'2016-12-10 00:00:00');
/*!40000 ALTER TABLE `block` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `genres`
--

DROP TABLE IF EXISTS `genres`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `genres` (
  `genre_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  `description` text,
  PRIMARY KEY (`genre_id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='Описывает жанры. Содержит имя и описание жанра(если есть). Вынесено в отдельную таблицу:\n1) является справочником (характеристикой любого произведения)\n2)  расширяемость';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `genres`
--

LOCK TABLES `genres` WRITE;
/*!40000 ALTER TABLE `genres` DISABLE KEYS */;
INSERT INTO `genres` VALUES (1,'Сказка','Сказка зародилась в стародавние времена, а самые древние сказки появились вместе с мифами. Сказки не были предназначены для детей, их рассказывали тем, кто вступал во взрослую жизнь, проходил обряд посвящения во взрослые.  Сказка была очень серьезным делом. Сказки бывают разные. Сейчас это способ для детей узнать мир. И хотя в наше время сказки пишутся в основном для детей, взрослые также могут подчерпнуть для себя много полезного. Ведь именно сказки закладывают на подсознательном уровне линию поведения человека в критической ситуации. Иногда только из сказки ученые и могут почерпнуть знания о каком-либо обряде, бытовавшем в древности'),(2,'Детектив','Детектив - жанр не имеет прототипов в античной литературе, не представлен в мифах и архетипах, не числится в списке мировых Сюжетов. Он возник как целое, в законченном виде, вместе со всеми своими атрибутами. В детективе мышление героев, как преступников, так и сыщиков, является очень «продвинутым»: оно сильное, последовательное, рефлективное, чёткое. При этом герои ещё и решительны, предприимчивы, готовы рискнуть жизнью.'),(3,'Фентези','Жанр фантастической литературы, основанный на использовании мифологических и сказочных мотивов, действие которого происходит в вымышленном мире, близком к реальному Средневековью, герои которого сталкиваются со сверхъестественными явлениями и существами. Зачастую фентези построено на основе архетипических сюжетов. В отличие от научной фантастики, фентези не стремится объяснить мир, в котором происходит действие произведения, с точки зрения науки. Сам этот мир существует гипотетически, часто его местоположение относительно нашей реальности никак не оговаривается: то ли это параллельный мир, то ли другая планета, а его физические законы могут отличаться от земных. В таком мире может быть реальным существование богов, колдовства, мифических существ (драконы, эльфы, гномы, тролли, гоблины, кентавры и тд), привидений и любых других фантастических сущностей. В то же время принципиальное отличие чудес фэнтези от их сказочных аналогов в том, что они являются нормой описываемого мира и действуют системно, как законы природы.'),(4,'Исторический','Жанр построен на историческом сюжете, который воспроизводит в художественной форме какую-либо эпоху, определённый период истории. В историческом романе историческая правда сочетается с художественной, исторический факт — с художественным вымыслом, настоящие исторические лица — с лицами вымышленными, вымысел помещён в пределы изображаемой эпохи. Всё повествование в историческом романе ведётся на фоне исторических событий'),(5,'Приключения','Жанр романа с характерным для них стремлением бежать от мещанской повседневности в мир экзотики и героизма. В более широком смысле можно говорить о существовании особого авантюрного жанра, или приключенческой литературы, которую отличают резкое деление персонажей на героев и злодеев, «стремительность развития действия, переменчивость и острота сюжетных ситуаций, преувеличенность переживаний, мотивы похищения и преследования, тайны и загадки». Задача приключенческой литературы — не столько поучать, анализировать или описывать реальность, сколько развлекать читателя.'),(6,'Антиутопия','Это жанр произведений обычно эпического рода, в котором изображается социальная и техногенная катастрофа, крах общественных идей, разрушений иллюзий и идеалов.'),(7,'Новости','Описывают факты и события, происходящие в политической и культурной жизни людей.'),(8,'Общественно-политический',NULL),(9,'Развлекательный',NULL);
/*!40000 ALTER TABLE `genres` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
INSERT INTO `payments` VALUES (3,1,34.23,'2017-12-03 12:39:14','y'),(4,2,10.00,'2017-12-03 16:33:14','y');
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `publication_types`
--

DROP TABLE IF EXISTS `publication_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `publication_types` (
  `publication_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  PRIMARY KEY (`publication_type_id`),
  UNIQUE KEY `rype_name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='Описывает типы изданий (журнал, газета, серия книг и т.д.). Связь 1 к 1, т.к.:\n1) является справочником (есть у каждого издания)\n2) расширяемость\n3) не надо использовать константы\n3) используется в связи многие ко многим';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `publication_types`
--

LOCK TABLES `publication_types` WRITE;
/*!40000 ALTER TABLE `publication_types` DISABLE KEYS */;
INSERT INTO `publication_types` VALUES (1,'Газета'),(2,'Журнал'),(3,'Книга');
/*!40000 ALTER TABLE `publication_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `publications`
--

DROP TABLE IF EXISTS `publications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `publications`
--

LOCK TABLES `publications` WRITE;
/*!40000 ALTER TABLE `publications` DISABLE KEYS */;
INSERT INTO `publications` VALUES (1,'Гарри Поттер',3,3,'Cерия из семи романов, написанных английской писательницей Дж. К. 	Роулинг. Книги представляют собой хронику приключений юного 	волшебника Гарри Поттера, а также его друзей Рона Уизли и Гермионы	Грейнджер, обучающихся в школе чародейства и волшебства Хогвартс. 	Основной сюжет посвящён противостоянию Гарри и тёмного волшебника 	по имени лорд Волан-де-Морт, в чьи цели входит обретение бессмертия и 	порабощение магического мира.',21.30,'',NULL),(2,'Сказки для детей',3,1,'Лучшие 	произведения одного из самых известных писателей 19 века 		собраны в серии 	книг для детей.',23.34,'',NULL),(3,'Дети капитана Гранта',3,5,'Трилогия 	французского писателя Жюля Верна, впервые полностью 			опубликованная в 1868 году. Первый роман состоит из трёх частей, в 			каждой из которой главные герои 	повествования — лорд и леди 			Гленарван, майор Мак-Наббс, Жак Паганель, Мэри и Роберт Грант, Джон 		Манглс — в поисках капитана Гранта путешествуют вокруг Земли по 			Южной Америке через Патагонию, по Австралии и Новой 	Зеландии, 			строго придерживаясь 37-й параллели южной широты.',12.30,'',NULL),(4,'Сказки братьев Гримм',3,1,'Cборник сказок, собранных в немецких 	землях и литературно 				обработанных братьями Якобом и Вильгельмом Гримами. Первоначально 		издан в 1812 году.',34.30,'',NULL),(5,'Аргументы и Факты',1,7,'Крупнейшая российская еженедельная общественно-политическая газета. 		Издаётся с 1978, 	тираж более 1 млн газет. Газета является одним из самых 		популярных российских изданий за рубежом и вошла в книгу рекордов 		Гиннеса за самый большой тираж в истории человечества. Сейчас издание 		принадлежит мэрии Москвы.',23.20,'',NULL),(6,'Планета',2,8,'Общественно - политический журнал. Публикует события, происходящие в мире – если они основаны на фактах и на добросовестном анализе. Жесткие, но правдивые сведения, излагаются материалы ярко и ясно, делаются сложные научные и аналитические выкладки понятные неспециалистам.',24.30,'',NULL),(7,'Дозор',3,3,'На Земле 	живут «простые люди» и «Иные», к которым относятся маги, 		волшебники, оборотни, вампиры, ведьмы, ведьмаки и проч. Иные делятся 		на две армии — 	Светлых (объединенных в Ночной дозор) и Темных 			(Дневной дозор). И поскольку простодушия начала ХХ века к концу 			столетия уже не осталось (а 	заодно и идеи Бога), Добро со Злом не 			борется, а находится в динамическом равновесии. То есть соблюдается 		баланс Света и Тьмы, и любое доброе магическое воздействие должно 		уравновешиваться злым. Даже вампиры 	законным порядком получают 		лицензии на высасывание крови из людей, так как и вампиры — часть 		общего порядка. Темные стоят за свободу поведения и неприятную 			правду, Светлые же все время сомневаются, не приведет ли доброе дело к 		негативным результатам, и потому связаны по рукам и ногам. Два Дозора 		увлекательно интригуют и борются друг с другом в многоходовых 			комбинациях; плести сюжеты про эту мистическую «Зарницу» можно до 		бесконечности, чем 	автор и занят. За Дозорами приглядывает 			Инквизиция (Сумеречный Дозор), 	тоже из Иных, которые следят за 			точным соблюдением Договора и баланса Добра и Зла.',12.40,'',NULL),(8,'Вечерний Минск',1,7,'Газета «Вечерний Минск» появилась на свет 1 ноября 1967 года. В ЦК 		партии тогда решили, что cтолица Белоруссии, так же как и Москва, 			Ленинград или Киев, достойна иметь свою вечернюю газету. И это было 		абсолютно правильное решение. Потому что с первых дней рождения 			новоиспеченное издание заявило о себе в полный голос. «Вечерний 			Минск» стал зеркалом столичной жизни. Город рос, строился, обновлялся 		— и все это читатель мог проследить по публикациям «Вечерки». Так, 		вместе с минскими метростроителями «ВМ» забил первую сваю 			столичного метро в 1977 году. И начиная с этой первой сваи газета 			рассказывает читателям, как станция за станцией рождается новый вид 		столичного транспорта. А как ждали «вечеркинцы» миллионного 			минчанина! Город узнал имя 	новорожденного «миллионера» Олега Босько 		именно благодаря «Вечерке» 25 января 1972 года.',3.10,'',NULL),(9,'Сказки для детей',3,1,'В сказке все возможно - сын мельника превращается в маркиза, бедный 		солдат находит волшебное огниво, а принц переодевается в свинопаса. И 		всегда добрые люди побеждают, а злодеи бывают наказаны. Так говорят 		великие сказочники, и мы с удовольствием верим! Для детей младшего 		школьного возраста.',23.50,'',NULL),(10,'За 80 дней вокруг света',3,5,'Популярный приключенческий роман французского писателя Жюля 			Верна, повествующий о путешествии эксцентричного и флегматичного 		англичанина Филеаса Фогга и его слуги -	француза Жана Паспарту́ вокруг 		света, предпринятом в результате одного пари.',23.80,'',NULL);
/*!40000 ALTER TABLE `publications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `roles` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='Описывает роли пользователей (администратор, пользователь и т.д.).Связь 1 к 1. Вынесено в отдельную таблицу, т.к. \n1) расширение функционала (может понадобится добаление характеристик)\n2) четкое ограничение набора  ролей (отпадает необходимость вносить константы)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'Admin'),(2,'User');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subscriptions`
--

DROP TABLE IF EXISTS `subscriptions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subscriptions`
--

LOCK TABLES `subscriptions` WRITE;
/*!40000 ALTER TABLE `subscriptions` DISABLE KEYS */;
INSERT INTO `subscriptions` VALUES (1,2,5,'2017-09-09','2018-12-09','y'),(2,2,6,'2017-12-12','2018-02-12','n');
/*!40000 ALTER TABLE `subscriptions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (2,1,'Анастасия','Андрухович','1998-06-11','Кожара 2, 33','Гомель',246304,1,'anastasia.andruhovich','856e404c60372d642ca15ba318e2323e'),(3,2,'Анастасия','Клец','1999-11-18','Лебеловского 4, 40','Гомель',246764,2,'nastya60945','68a5b6bbb1d0ffec52bb10ac0862dd43'),(7,2,'Владимир','Туманов','1998-06-11','Юбилейная 5, 79','Гомель',246592,3,'vovka56','ea5d7ca2d86e6c1cf45e08573a731c25'),(8,2,'Марина','Сманцер','1997-08-06','Притыцкого 56','Пинск',218465,4,'drowmar132','78200d2466a4b6e866bcffaa07d2c8e6'),(9,2,'Елена','Николаева','1954-05-13','Кожара 2, 33','Гомель',246304,5,'elena.nikolaeva','a3ff2b7ff5f4c24d230f13191f35a20c'),(10,2,'Константин','Андрухович','1973-01-11','Маяковского 20, 89','Минск',364859,9,'andr_364','fe19e81da1104f86b9487ee187d47bfd'),(11,2,'Ирина','Клопоцкая','1964-02-28','Маяковского 10, 49','Минск',364918,10,'ira_klop','f11f082344480d7c7f97ca85bbefa0a2'),(12,2,'Ефим','Николаев','1939-11-09','Кожара 2, 33','Гомель',246304,11,'vov_45','3defaf3cb554b108470027b017af8116');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-01-19 21:51:53
