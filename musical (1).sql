-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Хост: 127.0.0.1
-- Время создания: Июн 19 2023 г., 13:25
-- Версия сервера: 10.4.28-MariaDB
-- Версия PHP: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- База данных: `musical`
--

-- --------------------------------------------------------

--
-- Структура таблицы `artists`
--

CREATE TABLE `artists` (
  `id` bigint(20) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `filename` varchar(255) DEFAULT NULL,
  `genre` varchar(255) DEFAULT NULL,
  `listeners` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Дамп данных таблицы `artists`
--

INSERT INTO `artists` (`id`, `description`, `filename`, `genre`, `listeners`, `name`) VALUES
(4, 'Тут описание Джизуса', '87afa426-d37e-414c-af6e-def3a5a3d52e.071136440cd7.png', 'Рок', '5000000', 'Джизус'),
(5, 'Тут будет описание салуки', '9bd4a6e0-e77f-449b-ab20-9cdd34c3e17b.channels4_profile.jpg', 'Рэп', '5000000', 'SALUKI'),
(6, 'Тут будет описание про короля и шута', 'af312af3-a24a-4b37-a0fe-bdec2a05bde6.4aa4da9d6814e029f14ef44ad9c47bbc480caf9a.jpg', 'Панк-Рок', '999999', 'Король и шут'),
(7, 'Тут будет описание про фарыча', 'b056f685-c25a-4324-8633-d7f04738a702.Pharaoh.jpg', 'Хип-Хоп', '1200000', 'PRARAOH'),
(8, 'Тут будет описание  про  многознала', '371e27f2-a203-4adb-9c9c-7b389e9ce433.m1000x1000.jpg', 'Репчик', '8700000', 'Mnogoznal'),
(9, 'Тут будет описание про цоя', '4ed0f5bd-cca6-4632-ae7a-132357dca23d.upload-GettyImages-1166522079-pic_32ratio_900x600-900x600-58937.jpg', 'Легкий рок', '6500000', 'Виктор Цой'),
(10, 'Скоро новый трек а возможно и альбом', '7559e70a-06bb-4659-8e92-eddead7aa2eb.27980388-305a-4c47-ba3c-fc3a44cf1c1a.jpg', 'Инди', '12', 'Никитка'),
(11, 'Тут будет описание про три дня', '1505cd55-df3c-499c-a012-8d90104feb65.4e2570b2eb9f4c4eb8f721d7e4d3.jpg', 'Рок', '7800000', 'Три дня дождя'),
(12, 'Тут будет описание про ломоносова', '1e2267a5-7d43-494b-8d51-d3af0ab5f246.plan4.jpeg', 'Хард-рок', '450000', 'План Ломоносова'),
(13, 'Тут будет описание про нервы', 'bf1bee38-e8f1-40ba-b1e6-95ed96835496.2.jpg', 'Рок', '650000', 'Нервы'),
(14, 'Тут будет описание про Wildways', '69dacb91-2400-4812-8411-01af6ba43aaa.53d826ab3228756700f6c8902ee8665c.jpg', 'Металл', '7450000', 'Wildways');

-- --------------------------------------------------------

--
-- Структура таблицы `spring_session`
--

CREATE TABLE `spring_session` (
  `PRIMARY_ID` char(36) NOT NULL,
  `SESSION_ID` char(36) NOT NULL,
  `CREATION_TIME` bigint(20) NOT NULL,
  `LAST_ACCESS_TIME` bigint(20) NOT NULL,
  `MAX_INACTIVE_INTERVAL` int(11) NOT NULL,
  `EXPIRY_TIME` bigint(20) NOT NULL,
  `PRINCIPAL_NAME` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

--
-- Дамп данных таблицы `spring_session`
--

INSERT INTO `spring_session` (`PRIMARY_ID`, `SESSION_ID`, `CREATION_TIME`, `LAST_ACCESS_TIME`, `MAX_INACTIVE_INTERVAL`, `EXPIRY_TIME`, `PRINCIPAL_NAME`) VALUES
('0994698e-a858-4880-8052-00503ba94518', '7b640196-8e14-473a-8fcd-6da28a5fef68', 1687172420623, 1687173882805, 1800, 1687175682805, 'adminko');

-- --------------------------------------------------------

--
-- Структура таблицы `spring_session_attributes`
--

CREATE TABLE `spring_session_attributes` (
  `SESSION_PRIMARY_ID` char(36) NOT NULL,
  `ATTRIBUTE_NAME` varchar(200) NOT NULL,
  `ATTRIBUTE_BYTES` blob NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

--
-- Дамп данных таблицы `spring_session_attributes`
--

INSERT INTO `spring_session_attributes` (`SESSION_PRIMARY_ID`, `ATTRIBUTE_NAME`, `ATTRIBUTE_BYTES`) VALUES
('0994698e-a858-4880-8052-00503ba94518', 'SPRING_SECURITY_CONTEXT', 0xaced00057372003d6f72672e737072696e676672616d65776f726b2e73656375726974792e636f72652e636f6e746578742e5365637572697479436f6e74657874496d706c00000000000002620200014c000e61757468656e7469636174696f6e7400324c6f72672f737072696e676672616d65776f726b2f73656375726974792f636f72652f41757468656e7469636174696f6e3b78707372004f6f72672e737072696e676672616d65776f726b2e73656375726974792e61757468656e7469636174696f6e2e557365726e616d6550617373776f726441757468656e7469636174696f6e546f6b656e00000000000002620200024c000b63726564656e7469616c737400124c6a6176612f6c616e672f4f626a6563743b4c00097072696e636970616c71007e0004787200476f72672e737072696e676672616d65776f726b2e73656375726974792e61757468656e7469636174696f6e2e416273747261637441757468656e7469636174696f6e546f6b656ed3aa287e6e47640e0200035a000d61757468656e746963617465644c000b617574686f7269746965737400164c6a6176612f7574696c2f436f6c6c656374696f6e3b4c000764657461696c7371007e0004787001737200266a6176612e7574696c2e436f6c6c656374696f6e7324556e6d6f6469666961626c654c697374fc0f2531b5ec8e100200014c00046c6973747400104c6a6176612f7574696c2f4c6973743b7872002c6a6176612e7574696c2e436f6c6c656374696f6e7324556e6d6f6469666961626c65436f6c6c656374696f6e19420080cb5ef71e0200014c00016371007e00067870737200136a6176612e7574696c2e41727261794c6973747881d21d99c7619d03000149000473697a65787000000001770400000001737200426f72672e737072696e676672616d65776f726b2e73656375726974792e636f72652e617574686f726974792e53696d706c654772616e746564417574686f7269747900000000000002620200014c0004726f6c657400124c6a6176612f6c616e672f537472696e673b7870740018d09fd0bed0bbd18cd0b7d0bed0b2d0b0d182d0b5d0bbd18c7871007e000d737200486f72672e737072696e676672616d65776f726b2e73656375726974792e7765622e61757468656e7469636174696f6e2e57656241757468656e7469636174696f6e44657461696c7300000000000002620200024c000d72656d6f74654164647265737371007e000f4c000973657373696f6e496471007e000f787074000f303a303a303a303a303a303a303a3174002464356261336131652d376665642d343931362d393963642d32366661363962336237646570737200326f72672e737072696e676672616d65776f726b2e73656375726974792e636f72652e7573657264657461696c732e5573657200000000000002620200075a00116163636f756e744e6f6e457870697265645a00106163636f756e744e6f6e4c6f636b65645a001563726564656e7469616c734e6f6e457870697265645a0007656e61626c65644c000b617574686f72697469657374000f4c6a6176612f7574696c2f5365743b4c000870617373776f726471007e000f4c0008757365726e616d6571007e000f787001010101737200256a6176612e7574696c2e436f6c6c656374696f6e7324556e6d6f6469666961626c65536574801d92d18f9b80550200007871007e000a737200116a6176612e7574696c2e54726565536574dd98509395ed875b0300007870737200466f72672e737072696e676672616d65776f726b2e73656375726974792e636f72652e7573657264657461696c732e5573657224417574686f72697479436f6d70617261746f720000000000000262020000787077040000000171007e0010787074000761646d696e6b6f);

-- --------------------------------------------------------

--
-- Структура таблицы `tracks`
--

CREATE TABLE `tracks` (
  `id` bigint(20) NOT NULL,
  `audiofilename` varchar(255) DEFAULT NULL,
  `clipfilename` varchar(255) DEFAULT NULL,
  `coverfilename` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `genre` varchar(255) DEFAULT NULL,
  `lyrics` varchar(255) DEFAULT NULL,
  `releaseyear` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Дамп данных таблицы `tracks`
--

INSERT INTO `tracks` (`id`, `audiofilename`, `clipfilename`, `coverfilename`, `description`, `genre`, `lyrics`, `releaseyear`, `title`) VALUES
(9, '61ac00e2-8815-4757-ba33-8d4338040531.Без названия.jpg', '9041ef6b-c2e7-4216-86a6-61543bb3e779.Без названия.jpg', '26a923d8-dc62-42d6-a620-c1856c5d25d2.1ed8617297a59bb387aa035bd43f5a36.1000x1000x1.jpg', '', '', '', '', 'Я умираю для тебя'),
(10, 'be540ded-5d1e-4967-b18b-cfb961a7e73e.62d10cebf38cc136699c00fe127c87c5.1000x1000x1.png', 'a3008f79-75a0-428d-8565-61969e3c3a25.62d10cebf38cc136699c00fe127c87c5.1000x1000x1.png', 'ab3ae130-df31-4225-a341-df086623a494.62d10cebf38cc136699c00fe127c87c5.1000x1000x1.png', '', '', '', '', 'Дух мира'),
(11, 'c1254f8a-8f37-4931-9698-d0c0a21071e7.d13f276dcc599ace48820e0e1657492a.1000x1000x1.jpg', '73e5f0c3-bcd8-46b3-b3c1-2e00f3590b7f.d13f276dcc599ace48820e0e1657492a.1000x1000x1.jpg', '48b8f853-5c61-4c7e-a842-1c8cf623f626.d13f276dcc599ace48820e0e1657492a.1000x1000x1.jpg', '', '', '', '', '74: War and Love'),
(12, 'fc866ad1-eef9-45fa-ab9a-be97d4a287d6.ebdd633c301203b9f43e2208cd7a8b0a.1000x1000x1.png', '73f9a033-df32-41b9-9736-d28d0ed1c610.ebdd633c301203b9f43e2208cd7a8b0a.1000x1000x1.png', 'd0cb2838-5d2e-40d3-bd2b-94dc505f94fc.ebdd633c301203b9f43e2208cd7a8b0a.1000x1000x1.png', '', '', '', '', 'Огней'),
(13, 'b093e24a-ac61-478d-b73c-4c0494d697fc.d7a7fd750888d92e00a2f3766a963e66.1000x1000x1.png', '8672af29-b8d1-41ca-8f3f-7693993d1fb4.d7a7fd750888d92e00a2f3766a963e66.1000x1000x1.png', '9b6014af-b03d-4881-be87-96d7133a40c9.d7a7fd750888d92e00a2f3766a963e66.1000x1000x1.png', '', '', '', '', ''),
(14, '7216bb82-483f-4f9c-8935-6b97eb45518a.scale_1200.webp', '9904f4b9-0951-4860-9c62-84702de4c872.scale_1200.webp', 'ef201530-6b28-4876-85c9-a98c93eb8357.scale_1200.webp', '', '', '', '', 'Акустический альбом'),
(15, '3815fc30-1d11-4497-bf27-0ce83b9e0183.5e9722b962348.jpg', 'c5c56ed1-d425-473d-b3e4-a8dc90e710bd.5e9722b962348.jpg', '0ad1e3a9-0b20-4d38-a10f-1b25b933d3de.5e9722b962348.jpg', '', '', '', '', 'Флора'),
(16, '7eece604-d43e-4011-acc2-f53726c7469a.5e9722b962348.jpg', 'b11e4387-dae7-46b6-8279-670bb7514c34.5e9722b962348.jpg', '075a156c-609c-4f12-9024-4420db70ebcc.5e971f3c39e96.jpg', '', '', '', '', 'Забыл'),
(17, '7b04ea66-8afc-43eb-80aa-a84f2d64f93e.large_kino-sorok-shest-1.jpg', '4badc561-b055-440f-8ac8-a058b943fc44.Без названия.png', '310f69f7-a2a6-4f67-8a07-689c7ed3cb77.Без названия.png', '', '', '', '', '45'),
(18, '8049cff9-0327-4a75-bdb7-0c6c21fb6eaa.large_kino-sorok-shest-1.jpg', '0f964ae1-f9c7-49cf-9efa-663a46c4aa58.large_kino-sorok-shest-1.jpg', 'fe3f234b-3500-41ab-b9b2-addab2d96768.large_kino-sorok-shest-1.jpg', '', '', '', '', '46');

-- --------------------------------------------------------

--
-- Структура таблицы `track_artist`
--

CREATE TABLE `track_artist` (
  `artist_id` bigint(20) DEFAULT NULL,
  `track_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Дамп данных таблицы `track_artist`
--

INSERT INTO `track_artist` (`artist_id`, `track_id`) VALUES
(4, 9),
(4, 10),
(4, 11),
(4, 13),
(4, 16),
(5, 12),
(6, 14),
(7, 15),
(9, 17),
(9, 18);

-- --------------------------------------------------------

--
-- Структура таблицы `users`
--

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL,
  `mail` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Дамп данных таблицы `users`
--

INSERT INTO `users` (`id`, `mail`, `name`, `password`, `username`, `surname`) VALUES
(1, 'gmailmal@gmail.com', 'Алексей', '$2a$10$kNS/n.ipZsmkYeoJvX/x1epaDwtR9XkgIj0Wtf97Kbs4KC1BIn0mu', 'admin', 'Горнов'),
(2, 'request@mail.ru', 'Никита', '$2a$10$cfpiG..7Bn4rJ.IVG3qkrObEsyFFZT2/zmyLSx.rncH0IHiTtAOOW', 'adminko', 'Мешков');

-- --------------------------------------------------------

--
-- Структура таблицы `users_roles`
--

CREATE TABLE `users_roles` (
  `user_id` bigint(20) NOT NULL,
  `roles` enum('ADMIN','USER') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Дамп данных таблицы `users_roles`
--

INSERT INTO `users_roles` (`user_id`, `roles`) VALUES
(1, 'USER'),
(2, 'USER');

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `artists`
--
ALTER TABLE `artists`
  ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `spring_session`
--
ALTER TABLE `spring_session`
  ADD PRIMARY KEY (`PRIMARY_ID`),
  ADD UNIQUE KEY `SPRING_SESSION_IX1` (`SESSION_ID`),
  ADD KEY `SPRING_SESSION_IX2` (`EXPIRY_TIME`),
  ADD KEY `SPRING_SESSION_IX3` (`PRINCIPAL_NAME`);

--
-- Индексы таблицы `spring_session_attributes`
--
ALTER TABLE `spring_session_attributes`
  ADD PRIMARY KEY (`SESSION_PRIMARY_ID`,`ATTRIBUTE_NAME`);

--
-- Индексы таблицы `tracks`
--
ALTER TABLE `tracks`
  ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `track_artist`
--
ALTER TABLE `track_artist`
  ADD PRIMARY KEY (`track_id`),
  ADD KEY `FK2g7swotc7qmp9480tmadc7qd3` (`artist_id`);

--
-- Индексы таблицы `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `users_roles`
--
ALTER TABLE `users_roles`
  ADD KEY `FK2o0jvgh89lemvvo17cbqvdxaa` (`user_id`);

--
-- AUTO_INCREMENT для сохранённых таблиц
--

--
-- AUTO_INCREMENT для таблицы `artists`
--
ALTER TABLE `artists`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT для таблицы `tracks`
--
ALTER TABLE `tracks`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT для таблицы `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `spring_session_attributes`
--
ALTER TABLE `spring_session_attributes`
  ADD CONSTRAINT `SPRING_SESSION_ATTRIBUTES_FK` FOREIGN KEY (`SESSION_PRIMARY_ID`) REFERENCES `spring_session` (`PRIMARY_ID`) ON DELETE CASCADE;

--
-- Ограничения внешнего ключа таблицы `track_artist`
--
ALTER TABLE `track_artist`
  ADD CONSTRAINT `FK2g7swotc7qmp9480tmadc7qd3` FOREIGN KEY (`artist_id`) REFERENCES `artists` (`id`),
  ADD CONSTRAINT `FKlkg79qb7k45nb6exrnt2120c4` FOREIGN KEY (`track_id`) REFERENCES `tracks` (`id`);

--
-- Ограничения внешнего ключа таблицы `users_roles`
--
ALTER TABLE `users_roles`
  ADD CONSTRAINT `FK2o0jvgh89lemvvo17cbqvdxaa` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
