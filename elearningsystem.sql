-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : lun. 23 juin 2025 à 00:35
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `elearningsystem`
--

-- --------------------------------------------------------

--
-- Structure de la table `chapter`
--

CREATE TABLE `chapter` (
  `id` int(11) NOT NULL,
  `chapter1id` varchar(255) DEFAULT NULL,
  `chapter1name` varchar(255) DEFAULT NULL,
  `chapter2id` varchar(255) DEFAULT NULL,
  `chapter2name` varchar(255) DEFAULT NULL,
  `chapter3id` varchar(255) DEFAULT NULL,
  `chapter3name` varchar(255) DEFAULT NULL,
  `chapter4id` varchar(255) DEFAULT NULL,
  `chapter4name` varchar(255) DEFAULT NULL,
  `chapter5id` varchar(255) DEFAULT NULL,
  `chapter5name` varchar(255) DEFAULT NULL,
  `chapter6id` varchar(255) DEFAULT NULL,
  `chapter6name` varchar(255) DEFAULT NULL,
  `chapter7id` varchar(255) DEFAULT NULL,
  `chapter7name` varchar(255) DEFAULT NULL,
  `chapter8id` varchar(255) DEFAULT NULL,
  `chapter8name` varchar(255) DEFAULT NULL,
  `coursename` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `chapter`
--

INSERT INTO `chapter` (`id`, `chapter1id`, `chapter1name`, `chapter2id`, `chapter2name`, `chapter3id`, `chapter3name`, `chapter4id`, `chapter4name`, `chapter5id`, `chapter5name`, `chapter6id`, `chapter6name`, `chapter7id`, `chapter7name`, `chapter8id`, `chapter8name`, `coursename`) VALUES
(19, 'zzzzz', 'chapter 20', '', '', '', '', '', '', '', '', '', '', '', '', '', '', 'cours test');

-- --------------------------------------------------------

--
-- Structure de la table `comment`
--

CREATE TABLE `comment` (
  `id` bigint(20) NOT NULL,
  `content` varchar(1000) NOT NULL,
  `created_at` datetime NOT NULL,
  `rating` int(11) DEFAULT NULL,
  `course_id` int(11) NOT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `professor_id` varchar(255) DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `course`
--

CREATE TABLE `course` (
  `id` int(11) NOT NULL,
  `courseid` varchar(255) DEFAULT NULL,
  `coursename` varchar(255) DEFAULT NULL,
  `coursetype` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `enrolledcount` varchar(255) DEFAULT NULL,
  `enrolleddate` varchar(255) DEFAULT NULL,
  `instructorinstitution` varchar(255) DEFAULT NULL,
  `instructorname` varchar(255) DEFAULT NULL,
  `is_premuim` bit(1) NOT NULL,
  `language` varchar(255) DEFAULT NULL,
  `skilllevel` varchar(255) DEFAULT NULL,
  `websiteurl` varchar(255) DEFAULT NULL,
  `youtubeurl` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `course`
--

INSERT INTO `course` (`id`, `courseid`, `coursename`, `coursetype`, `description`, `enrolledcount`, `enrolleddate`, `instructorinstitution`, `instructorname`, `is_premuim`, `language`, `skilllevel`, `websiteurl`, `youtubeurl`) VALUES
(16, 'mbge5kIVCvkh', 'cours Math', 'Youtube', 'zzzz', '0', '2025-06-12', 'aaaaaa', 'cours test ', b'0', 'English', '', 'cccaaaaaaaaaaaaaa', ''),
(18, 'sffOVdMQBXHL', 'cours test 30', 'Youtube', 'AA', '0', '2025-06-12', 'medmedoussema', 'cours ', b'1', 'English', 'Advanced', '', ''),
(20, 'l96SWxaSU2sH', 'Cours English ', 'Youtube', 'aa', '0', '2025-06-11', 'medmedoussema', 'medmedoussema', b'1', 'English', 'Intermediate', '', ''),
(22, 'AuM5XsIylYcD', 'cours python ', 'Youtube', 'aaa', '0', '2025-06-14', 'aaa', 'aaaa', b'0', 'English', 'Advanced', '', '');

-- --------------------------------------------------------

--
-- Structure de la table `enrollment`
--

CREATE TABLE `enrollment` (
  `id` int(11) NOT NULL,
  `courseid` varchar(255) DEFAULT NULL,
  `coursename` varchar(255) DEFAULT NULL,
  `coursetype` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `enrolledcount` varchar(255) DEFAULT NULL,
  `enrolleddate` varchar(255) DEFAULT NULL,
  `enrolleduserid` varchar(255) DEFAULT NULL,
  `enrolledusername` varchar(255) DEFAULT NULL,
  `enrolledusertype` varchar(255) DEFAULT NULL,
  `instructorinstitution` varchar(255) DEFAULT NULL,
  `instructorname` varchar(255) DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL,
  `skilllevel` varchar(255) DEFAULT NULL,
  `websiteurl` varchar(255) DEFAULT NULL,
  `youtubeurl` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `enrollment`
--

INSERT INTO `enrollment` (`id`, `courseid`, `coursename`, `coursetype`, `description`, `enrolledcount`, `enrolleddate`, `enrolleduserid`, `enrolledusername`, `enrolledusertype`, `instructorinstitution`, `instructorname`, `language`, `skilllevel`, `websiteurl`, `youtubeurl`) VALUES
(2, '8oWxrP8SOOjY', 'cours test ', 'Youtube', 'aaaa', '0', '2025-06-21', 'CFAaV4SIQh2e', 'prof', 'PROFESSOR', 'medmedoussema', 'cours test ', 'Hindi', 'Basic', '', ''),
(5, '8oWxrP8SOOjY', 'cours test ', 'Youtube', 'aaaa', '0', '2025-06-22', 'CFAaV4SIQh2e', 'prof', 'PROFESSOR', 'medmedoussema', 'cours test ', 'Hindisssss', 'Basic', '', ''),
(6, '8oWxrP8SOOjY', 'cours test ', 'Youtube', 'aaaa', '0', '2025-06-22', 'CFAaV4SIQh2e', 'prof', 'PROFESSOR', 'medmedoussema', 'cours test ', 'Hindisssss', 'Basic', '', ''),
(7, '8oWxrP8SOOjY', 'cours test ', 'Youtube', 'aaaa', '0', '2025-06-22', 'CFAaV4SIQh2e', 'prof', 'PROFESSOR', 'medmedoussema', 'cours test ', 'Hindisssss', 'Basic', '', ''),
(8, '8oWxrP8SOOjY', 'cours test ', 'Youtube', 'aaaa', '0', '2025-06-22', 'CFAaV4SIQh2e', 'prof', 'PROFESSOR', 'medmedoussema', 'cours test ', 'Hindisssss', 'Basic', '', ''),
(9, '8oWxrP8SOOjY', 'cours test ', 'Youtube', 'aaaa', '0', '2025-06-22', 'CFAaV4SIQh2e', 'prof', 'PROFESSOR', 'medmedoussema', 'cours test ', 'Hindisssss', 'Basic', '', ''),
(10, '8oWxrP8SOOjY', 'cours test ', 'Youtube', 'aaaa', '0', '2025-06-22', 'CFAaV4SIQh2e', 'prof', 'PROFESSOR', 'medmedoussema', 'cours test ', 'Hindisssss', 'Basic', '', ''),
(11, 'xJfjAE7PjSWs', 'cours test2 ', 'Youtube', 'aaaa', '0', '2025-06-22', 'CFAaV4SIQh2e', 'prof', 'PROFESSOR', 'aaaaaa', 'zzaa', 'Français', '', 'aaa', ''),
(12, '8oWxrP8SOOjY', 'cours test ', 'Youtube', 'aaaa', '0', '2025-06-22', 'CFAaV4SIQh2e', 'prof', 'PROFESSOR', 'medmedoussema', 'cours test ', 'Hindisssss', 'Basic', '', ''),
(21, 'mbge5kIVCvkh', 'cours Math', 'Youtube', 'zzzz', '0', '2025-06-22', 'yn01v1aobfss', 'med@gmail.com', 'USER', 'aaaaaa', 'cours test ', 'English', '', 'cccaaaaaaaaaaaaaa', ''),
(23, 'sffOVdMQBXHL', 'cours test 30', 'Youtube', 'AA', '0', '2025-06-23', 'skvccfQPOmGF', 'USER', 'USER', 'medmedoussema', 'cours ', 'English', 'Advanced', '', ''),
(24, 'mbge5kIVCvkh', 'cours Math', 'Youtube', 'zzzz', '0', '2025-06-23', 'skvccfQPOmGF', 'USER', 'USER', 'aaaaaa', 'cours test ', 'English', '', 'cccaaaaaaaaaaaaaa', ''),
(25, 'AuM5XsIylYcD', 'cours python ', 'Youtube', 'aaa', '0', '2025-06-23', 'skvccfQPOmGF', 'USER', 'USER', 'aaa', 'aaaa', 'English', 'Advanced', '', '');

-- --------------------------------------------------------

--
-- Structure de la table `hibernate_sequence`
--

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `hibernate_sequence`
--

INSERT INTO `hibernate_sequence` (`next_val`) VALUES
(26);

-- --------------------------------------------------------

--
-- Structure de la table `message`
--

CREATE TABLE `message` (
  `id` bigint(20) NOT NULL,
  `content` varchar(255) NOT NULL,
  `timestamp` datetime NOT NULL,
  `receiver_professor_id` varchar(255) DEFAULT NULL,
  `receiver_user_id` varchar(255) DEFAULT NULL,
  `sender_professor_id` varchar(255) DEFAULT NULL,
  `sender_user_id` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `message`
--

INSERT INTO `message` (`id`, `content`, `timestamp`, `receiver_professor_id`, `receiver_user_id`, `sender_professor_id`, `sender_user_id`) VALUES
(1, 'AAAA', '2025-06-22 23:46:26', 'prof@gmail.com', NULL, NULL, 'med@gmail.com'),
(2, 'AAAA', '2025-06-22 23:46:29', 'prof@gmail.com', NULL, NULL, 'med@gmail.com');

-- --------------------------------------------------------

--
-- Structure de la table `option`
--

CREATE TABLE `option` (
  `id` bigint(20) NOT NULL,
  `content` varchar(255) NOT NULL,
  `correct` bit(1) NOT NULL,
  `question_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `option`
--

INSERT INTO `option` (`id`, `content`, `correct`, `question_id`) VALUES
(1, 'a ', b'1', 1),
(2, 'c', b'0', 1),
(3, 'd ', b'0', 1),
(4, 'a', b'0', 1),
(5, 'c', b'1', 2),
(6, 'c', b'0', 2),
(7, 'd ', b'0', 2),
(8, 'f', b'0', 2),
(9, 'a', b'1', 3),
(10, 'c', b'0', 3),
(11, 'd ', b'0', 3),
(12, 'f', b'0', 3),
(13, '', b'0', 3);

-- --------------------------------------------------------

--
-- Structure de la table `payment`
--

CREATE TABLE `payment` (
  `id` bigint(20) NOT NULL,
  `amount` double DEFAULT NULL,
  `card_number` varchar(255) DEFAULT NULL,
  `card_type` varchar(255) DEFAULT NULL,
  `expiry_date` varchar(255) DEFAULT NULL,
  `payment_method` varchar(255) DEFAULT NULL,
  `receipt_date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `payment`
--

INSERT INTO `payment` (`id`, `amount`, `card_number`, `card_type`, `expiry_date`, `payment_method`, `receipt_date`) VALUES
(1, 1, '11111111111111111', 'mastercard', '2025-09', 'credit', '2025-06-24'),
(2, 3, '111111111111111', 'visa', '2025-05', 'paypal', '2025-06-19'),
(3, 111, '1234577', 'amex', '2025-09', 'credit', '2025-05-27');

-- --------------------------------------------------------

--
-- Structure de la table `professor`
--

CREATE TABLE `professor` (
  `email` varchar(255) NOT NULL,
  `degreecompleted` varchar(255) DEFAULT NULL,
  `department` varchar(255) DEFAULT NULL,
  `experience` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `institutionname` varchar(255) DEFAULT NULL,
  `mobile` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `professorid` varchar(255) DEFAULT NULL,
  `professorname` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `professor`
--

INSERT INTO `professor` (`email`, `degreecompleted`, `department`, `experience`, `gender`, `institutionname`, `mobile`, `password`, `professorid`, `professorname`, `status`) VALUES
('prof@gmail.com', 'aaaa', 'info', '3', 'Male', 'prof@gmail.com', '1234545662', '$2a$10$BiK.gp0pTrvTbB//6xktlOu0O5NjY7opYavBV.rJTbYnwB/1V1Gii', 'bNhHk7d0pkfu', 'prof', 'false');

-- --------------------------------------------------------

--
-- Structure de la table `question`
--

CREATE TABLE `question` (
  `id` bigint(20) NOT NULL,
  `content` varchar(1000) NOT NULL,
  `created_at` datetime NOT NULL,
  `course_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `question`
--

INSERT INTO `question` (`id`, `content`, `created_at`, `course_id`) VALUES
(1, 'donner une reponse ', '2025-06-22 15:12:40', 16),
(2, 'donner une reponse 2', '2025-06-22 16:01:28', 18),
(3, 'donner une reponse Q3 ', '2025-06-22 21:07:42', 20);

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

CREATE TABLE `user` (
  `email` varchar(255) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `is_premuim` bit(1) NOT NULL,
  `mobile` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `profession` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `userid` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `user`
--

INSERT INTO `user` (`email`, `address`, `gender`, `is_premuim`, `mobile`, `password`, `profession`, `role`, `userid`, `username`) VALUES
('med@gmail.com', 'aaaaa', '', b'0', '2873668489', '$2a$10$wbWzCcE/nrLKiAJWLyHM4Ok/8Yd5tvxKYzFJklGcTW8/WjKqDwBla', 'user', 'Admin', 'aRmVQ4roqZl3', 'med@gmail.com'),
('usrer@gmail.com', 'tunis', '', b'1', '2873668489', '$2a$10$JLuowOwsjMVIqCXvt2Q2tezyIJql66hpYlndiHYhI13nCFooms5/2', 'apprenant', 'User', 'skvccfQPOmGF', 'USER');

-- --------------------------------------------------------

--
-- Structure de la table `wishlist`
--

CREATE TABLE `wishlist` (
  `coursename` varchar(255) NOT NULL,
  `courseid` varchar(255) DEFAULT NULL,
  `coursetype` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `enrolledcount` varchar(255) DEFAULT NULL,
  `instructorinstitution` varchar(255) DEFAULT NULL,
  `instructorname` varchar(255) DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL,
  `likeduser` varchar(255) DEFAULT NULL,
  `likedusertype` varchar(255) DEFAULT NULL,
  `skilllevel` varchar(255) DEFAULT NULL,
  `websiteurl` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `wishlist`
--

INSERT INTO `wishlist` (`coursename`, `courseid`, `coursetype`, `description`, `enrolledcount`, `instructorinstitution`, `instructorname`, `language`, `likeduser`, `likedusertype`, `skilllevel`, `websiteurl`) VALUES
('cours test2 ', 'xJfjAE7PjSWs', 'Youtube', 'aaaa', '0', 'aaaaaa', 'zzaa', 'Français', 'prof@gmail.com', 'PROFESSOR', '', 'aaa');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `chapter`
--
ALTER TABLE `chapter`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `comment`
--
ALTER TABLE `comment`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKdsub2q6m6519rpas8b075fr7m` (`course_id`),
  ADD KEY `FKde3rfu96lep00br5ov0mdieyt` (`parent_id`),
  ADD KEY `FKfy1bwjan4lb9714eb2ov4xx63` (`professor_id`),
  ADD KEY `FK8kcum44fvpupyw6f5baccx25c` (`user_id`);

--
-- Index pour la table `course`
--
ALTER TABLE `course`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `enrollment`
--
ALTER TABLE `enrollment`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `message`
--
ALTER TABLE `message`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK2a0c0vix8ownyrfd8veq3pawq` (`receiver_professor_id`),
  ADD KEY `FKh4xy93vijopygnpjqbe0c9iew` (`receiver_user_id`),
  ADD KEY `FK3en5pjvr0o75mkiw9tw0sto2m` (`sender_professor_id`),
  ADD KEY `FK80flimpheqbm2ex5r6ng1iodk` (`sender_user_id`);

--
-- Index pour la table `option`
--
ALTER TABLE `option`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKgtlhwmagte7l2ssfsgw47x9ka` (`question_id`);

--
-- Index pour la table `payment`
--
ALTER TABLE `payment`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `professor`
--
ALTER TABLE `professor`
  ADD PRIMARY KEY (`email`);

--
-- Index pour la table `question`
--
ALTER TABLE `question`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKnbqlwvoi94mkynn6c3r5h8dlg` (`course_id`);

--
-- Index pour la table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`email`);

--
-- Index pour la table `wishlist`
--
ALTER TABLE `wishlist`
  ADD PRIMARY KEY (`coursename`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `comment`
--
ALTER TABLE `comment`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `message`
--
ALTER TABLE `message`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `option`
--
ALTER TABLE `option`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT pour la table `payment`
--
ALTER TABLE `payment`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `question`
--
ALTER TABLE `question`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `comment`
--
ALTER TABLE `comment`
  ADD CONSTRAINT `FK8kcum44fvpupyw6f5baccx25c` FOREIGN KEY (`user_id`) REFERENCES `user` (`email`),
  ADD CONSTRAINT `FKde3rfu96lep00br5ov0mdieyt` FOREIGN KEY (`parent_id`) REFERENCES `comment` (`id`),
  ADD CONSTRAINT `FKdsub2q6m6519rpas8b075fr7m` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`),
  ADD CONSTRAINT `FKfy1bwjan4lb9714eb2ov4xx63` FOREIGN KEY (`professor_id`) REFERENCES `professor` (`email`);

--
-- Contraintes pour la table `message`
--
ALTER TABLE `message`
  ADD CONSTRAINT `FK2a0c0vix8ownyrfd8veq3pawq` FOREIGN KEY (`receiver_professor_id`) REFERENCES `professor` (`email`),
  ADD CONSTRAINT `FK3en5pjvr0o75mkiw9tw0sto2m` FOREIGN KEY (`sender_professor_id`) REFERENCES `professor` (`email`),
  ADD CONSTRAINT `FK80flimpheqbm2ex5r6ng1iodk` FOREIGN KEY (`sender_user_id`) REFERENCES `user` (`email`),
  ADD CONSTRAINT `FKh4xy93vijopygnpjqbe0c9iew` FOREIGN KEY (`receiver_user_id`) REFERENCES `user` (`email`);

--
-- Contraintes pour la table `option`
--
ALTER TABLE `option`
  ADD CONSTRAINT `FKgtlhwmagte7l2ssfsgw47x9ka` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`);

--
-- Contraintes pour la table `question`
--
ALTER TABLE `question`
  ADD CONSTRAINT `FKnbqlwvoi94mkynn6c3r5h8dlg` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
