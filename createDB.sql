CREATE TABLE `ldapconfig` (
	`idldapconfig` INT NOT NULL AUTO_INCREMENT ,
	`host` VARCHAR(100) NOT NULL ,
	`port` INT NOT NULL ,
	`useSSL` TINYINT NOT NULL ,
	`ignoreCertificates` TINYINT NOT NULL ,
	`basedn` VARCHAR(100) NOT NULL ,
	`uidField` VARCHAR(100) NOT NULL ,
	`cpfField` VARCHAR(100) NOT NULL ,
	`registerField` VARCHAR(100) NOT NULL ,
	`nameField` VARCHAR(100) NOT NULL ,
	`emailField` VARCHAR(100) NOT NULL ,
	PRIMARY KEY (`idldapconfig`)
);

CREATE TABLE `campus` (
	`idcampus` int(11) NOT NULL AUTO_INCREMENT,
	`nome` varchar(100) NOT NULL,
	`endereco` varchar(100) NOT NULL,
	`site` varchar(255) NOT NULL,
	`logo` mediumblob,
	`ativo` tinyint(4) NOT NULL,
	PRIMARY KEY (`idcampus`)
);

CREATE TABLE `departamentos` (
	`iddepartamento` int(11) NOT NULL AUTO_INCREMENT,
	`idcampus` int(11) NOT NULL,
	`nome` varchar(100) NOT NULL,
	`nomeCompleto` varchar(255) NOT NULL,
	`site` varchar(255) NOT NULL,
	`logo` mediumblob,
	`ativo` tinyint(4) NOT NULL,
	PRIMARY KEY (`iddepartamento`),
	KEY `fk_departamento_campus_idx` (`idcampus`),
	CONSTRAINT `fk_departamento_campus` FOREIGN KEY (`idcampus`) REFERENCES `campus` (`idcampus`) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE `usuarios` (
	`idusuario` int(11) NOT NULL AUTO_INCREMENT,
	`nome` varchar(100) NOT NULL,
	`login` varchar(50) NOT NULL,
	`senha` varchar(100) NOT NULL,
	`email` varchar(100) NOT NULL,
	`externo` tinyint(4) NOT NULL,
	`ativo` tinyint(4) NOT NULL,
	`administrador` tinyint(4) NOT NULL,
	PRIMARY KEY (`idusuario`)
);

CREATE TABLE `orgaos` (
	`idorgao` int(11) NOT NULL AUTO_INCREMENT,
	`iddepartamento` int(11) NOT NULL,
	`idpresidente` int(11) NOT NULL,
	`idsecretario` int(11) NOT NULL,
	`nome` varchar(100) NOT NULL,
	`nomeCompleto` VARCHAR(255) NOT NULL,
	`designacaoPresidente` VARCHAR(255) NOT NULL,
	`ativo` tinyint(4) NOT NULL,
	PRIMARY KEY (`idorgao`),
	KEY `fk_orgao_departamento_idx` (`iddepartamento`),
	CONSTRAINT `fk_orgao_departamento` FOREIGN KEY (`iddepartamento`) REFERENCES `departamentos` (`iddepartamento`) ON DELETE NO ACTION ON UPDATE NO ACTION,
	KEY `fk_orgao_presidente_idx` (`idpresidente`),
	CONSTRAINT `fk_orgao_presidente` FOREIGN KEY (`idpresidente`) REFERENCES `usuarios` (`idusuario`) ON DELETE NO ACTION ON UPDATE NO ACTION,
	KEY `fk_orgao_secretario_idx` (`idsecretario`),
	CONSTRAINT `fk_orgao_secretario` FOREIGN KEY (`idsecretario`) REFERENCES `usuarios` (`idusuario`) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE `membros` (
	`idorgao` int(11) NOT NULL,
	`idusuario` int(11) NOT NULL,
	`designacao` VARCHAR(50) NOT NULL,
	PRIMARY KEY (`idorgao`, `idusuario`),
	KEY `fk_membro_orgao_idx` (`idorgao`),
	CONSTRAINT `fk_membro_orgao` FOREIGN KEY (`idorgao`) REFERENCES `orgaos` (`idorgao`) ON DELETE NO ACTION ON UPDATE NO ACTION,
	KEY `fk_membro_usuario_idx` (`idusuario`),
	CONSTRAINT `fk_membro_usuario` FOREIGN KEY (`idusuario`) REFERENCES `usuarios` (`idusuario`) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE `atas` (
	`idata` int(11) NOT NULL AUTO_INCREMENT,
	`idorgao` int(11) NOT NULL,
	`idsecretario` int(11) NOT NULL,
	`idpresidente` int(11) NOT NULL,
	`tipo` tinyint(4) NOT NULL,
	`numero` tinyint(4) NOT NULL,
	`data` DATETIME NOT NULL,
	`local` VARCHAR(255) NOT NULL,
	`localCompleto` VARCHAR(255) NOT NULL,
	`dataLimiteComentarios` DATETIME NOT NULL,
	`consideracoesIniciais` TEXT NOT NULL,
	`audio` mediumblob NULL,
	`publicada` tinyint(4) NOT NULL,
	`dataPublicacao` DATETIME NULL,
	`documento` mediumblob NULL,
	`aceitarComentarios` tinyint(4) NOT NULL,
	PRIMARY KEY (`idata`),
	KEY `fk_ata_orgao_idx` (`idorgao`),
	CONSTRAINT `fk_ata_orgao` FOREIGN KEY (`idorgao`) REFERENCES `orgaos` (`idorgao`) ON DELETE NO ACTION ON UPDATE NO ACTION,
	KEY `fk_ata_secretario_idx` (`idsecretario`),
	CONSTRAINT `fk_ata_secretario` FOREIGN KEY (`idsecretario`) REFERENCES `usuarios` (`idusuario`) ON DELETE NO ACTION ON UPDATE NO ACTION,
	KEY `fk_ata_presidente_idx` (`idpresidente`),
	CONSTRAINT `fk_ata_presidente` FOREIGN KEY (`idpresidente`) REFERENCES `usuarios` (`idusuario`) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE `pautas` (
	`idpauta` int(11) NOT NULL AUTO_INCREMENT,
	`idata` int(11) NOT NULL,
	`ordem` tinyint(4) NOT NULL,
	`titulo` VARCHAR(255) NOT NULL,
	`descricao` TEXT NOT NULL,
	PRIMARY KEY (`idpauta`),
	KEY `fk_pauta_ata_idx` (`idata`),
	CONSTRAINT `fk_pauta_ata` FOREIGN KEY (`idata`) REFERENCES `atas` (`idata`) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE `ataparticipantes` (
	`idataparticipante` int(11) NOT NULL AUTO_INCREMENT,
	`idata` int(11) NOT NULL,
	`idusuario` int(11) NOT NULL,
	`designacao` VARCHAR(50) NOT NULL,
	`presente` tinyint(4) NOT NULL,
	`membro` tinyint(4) NOT NULL,
	`motivo` VARCHAR(255) NOT NULL,
	PRIMARY KEY (`idataparticipante`),
	KEY `fk_ataparticipantes_ata_idx` (`idata`),
	CONSTRAINT `fk_ataparticipantes_ata` FOREIGN KEY (`idata`) REFERENCES `atas` (`idata`) ON DELETE NO ACTION ON UPDATE NO ACTION,
	KEY `fk_ataparticipantes_usuario_idx` (`idusuario`),
	CONSTRAINT `fk_ataparticipantes_usuario` FOREIGN KEY (`idusuario`) REFERENCES `usuarios` (`idusuario`) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE `anexos` (
    `idanexo` INT NOT NULL AUTO_INCREMENT,
    `idata` INT NOT NULL,
    `descricao` VARCHAR(50) NOT NULL,
    `ordem` SMALLINT NOT NULL,
    `arquivo` mediumblob NOT NULL,
    PRIMARY KEY (idanexo),
	CONSTRAINT `fk_anexos_ata` FOREIGN KEY (`idata`) REFERENCES `atas` (`idata`) ON DELETE NO ACTION ON UPDATE NO ACTION,
	KEY `fk_anexos_ata_idx` (`idata`)
);

CREATE TABLE `comentarios` (
	`idcomentario` int(11) NOT NULL AUTO_INCREMENT,
	`idpauta` int(11) NOT NULL,
	`idusuario` int(11) NOT NULL,
	`situacao` tinyint(4) NOT NULL,
	`comentarios` TEXT NOT NULL,
	`situacaoComentarios` tinyint(4) NOT NULL,
	`motivo` TEXT NOT NULL,
	PRIMARY KEY (`idcomentario`),
	KEY `fk_comentario_pauta_idx` (`idpauta`),
	CONSTRAINT `fk_comentario_pauta` FOREIGN KEY (`idpauta`) REFERENCES `pautas` (`idpauta`) ON DELETE NO ACTION ON UPDATE NO ACTION,
	KEY `fk_comentario_usuario_idx` (`idusuario`),
	CONSTRAINT `fk_comentario_usuario` FOREIGN KEY (`idusuario`) REFERENCES `usuarios` (`idusuario`) ON DELETE NO ACTION ON UPDATE NO ACTION
);

INSERT INTO `ldapconfig`(`host`, `port`, `useSSL`, `ignoreCertificates`, `basedn`, `uidField`, `cpfField`, `registerField`, `nameField`, `emailField`) VALUES('200.134.18.102', 636, 1, 1, 'dc=utfpr,dc=edu,dc=br', 'uid', 'employeeNumber', 'carLicense', 'cn', 'mail');
INSERT INTO `usuarios`(`nome`,`login`,`senha`,`email`,`externo`,`ativo`,`administrador`) VALUES('Administrador','admin','7df6c2b08a8b5c53504e829189d9f35c','',1,1,1);
