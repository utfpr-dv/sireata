CREATE TABLE ldapconfig (
	idldapconfig SERIAL NOT NULL,
	host VARCHAR(100) NOT NULL,
	port INT NOT NULL,
	useSSL SMALLINT NOT NULL,
	ignoreCertificates SMALLINT NOT NULL,
	basedn VARCHAR(100) NOT NULL,
	uidField VARCHAR(100) NOT NULL,
	cpfField VARCHAR(100) NOT NULL,
	registerField VARCHAR(100) NOT NULL,
	nameField VARCHAR(100) NOT NULL,
	emailField VARCHAR(100) NOT NULL,
	PRIMARY KEY (idldapconfig)
);

CREATE TABLE campus (
	idcampus SERIAL NOT NULL,
	nome varchar(100) NOT NULL,
	endereco varchar(100) NOT NULL,
	site varchar(255) NOT NULL,
	logo BYTEA,
	ativo SMALLINT NOT NULL,
	PRIMARY KEY (idcampus)
);

CREATE TABLE departamentos (
	iddepartamento SERIAL NOT NULL,
	idcampus INT NOT NULL,
	nome varchar(100) NOT NULL,
	nomeCompleto varchar(255) NOT NULL,
	site varchar(255) NOT NULL,
	logo BYTEA,
	ativo SMALLINT NOT NULL,
	PRIMARY KEY (iddepartamento),
	CONSTRAINT fk_departamento_campus FOREIGN KEY (idcampus) REFERENCES campus (idcampus) ON DELETE NO ACTION ON UPDATE NO ACTION
);
CREATE INDEX fk_departamento_campus_idx ON departamentos (idcampus);

CREATE TABLE usuarios (
	idusuario SERIAL NOT NULL,
	nome varchar(100) NOT NULL,
	login varchar(50) NOT NULL,
	senha varchar(100) NOT NULL,
	email varchar(100) NOT NULL,
	externo SMALLINT NOT NULL,
	ativo SMALLINT NOT NULL,
	administrador SMALLINT NOT NULL,
	PRIMARY KEY (idusuario)
);

CREATE TABLE orgaos (
	idorgao SERIAL NOT NULL,
	iddepartamento INT NOT NULL,
	idpresidente INT NOT NULL,
	idsecretario INT NOT NULL,
	nome varchar(100) NOT NULL,
	nomeCompleto VARCHAR(255) NOT NULL,
	designacaoPresidente VARCHAR(255) NOT NULL,
	ativo SMALLINT NOT NULL,
	PRIMARY KEY (idorgao),
	CONSTRAINT fk_orgao_departamento FOREIGN KEY (iddepartamento) REFERENCES departamentos (iddepartamento) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT fk_orgao_presidente FOREIGN KEY (idpresidente) REFERENCES usuarios (idusuario) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT fk_orgao_secretario FOREIGN KEY (idsecretario) REFERENCES usuarios (idusuario) ON DELETE NO ACTION ON UPDATE NO ACTION
);
CREATE INDEX fk_orgao_departamento_idx ON orgaos (iddepartamento);
CREATE INDEX fk_orgao_presidente_idx ON orgaos (idpresidente);
CREATE INDEX fk_orgao_secretario_idx ON orgaos (idsecretario);

CREATE TABLE membros (
	idorgao INT NOT NULL,
	idusuario INT NOT NULL,
	designacao VARCHAR(50) NOT NULL,
	PRIMARY KEY (idorgao, idusuario),
	CONSTRAINT fk_membro_orgao FOREIGN KEY (idorgao) REFERENCES orgaos (idorgao) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT fk_membro_usuario FOREIGN KEY (idusuario) REFERENCES usuarios (idusuario) ON DELETE NO ACTION ON UPDATE NO ACTION
);
CREATE INDEX fk_membro_orgao_idx ON membros (idorgao);
CREATE INDEX fk_membro_usuario_idx ON membros (idusuario);

CREATE TABLE atas (
	idata SERIAL NOT NULL,
	idorgao INT NOT NULL,
	idsecretario INT NOT NULL,
	idpresidente INT NOT NULL,
	tipo SMALLINT NOT NULL,
	numero SMALLINT NOT NULL,
	data TIMESTAMP NOT NULL,
	local VARCHAR(255) NOT NULL,
	localCompleto VARCHAR(255) NOT NULL,
	dataLimiteComentarios TIMESTAMP NOT NULL,
	consideracoesIniciais TEXT NOT NULL,
	audio BYTEA NULL,
	publicada SMALLINT NOT NULL,
	dataPublicacao TIMESTAMP NULL,
	documento BYTEA NULL,
	aceitarComentarios SMALLINT NOT NULL,
	PRIMARY KEY (idata),
	CONSTRAINT fk_ata_orgao FOREIGN KEY (idorgao) REFERENCES orgaos (idorgao) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT fk_ata_secretario FOREIGN KEY (idsecretario) REFERENCES usuarios (idusuario) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT fk_ata_presidente FOREIGN KEY (idpresidente) REFERENCES usuarios (idusuario) ON DELETE NO ACTION ON UPDATE NO ACTION
);
CREATE INDEX fk_ata_orgao_idx ON atas (idorgao);
CREATE INDEX fk_ata_secretario_idx ON atas (idsecretario);
CREATE INDEX fk_ata_presidente_idx ON atas (idpresidente);

CREATE TABLE pautas (
	idpauta SERIAL NOT NULL,
	idata INT NOT NULL,
	ordem SMALLINT NOT NULL,
	titulo VARCHAR(255) NOT NULL,
	descricao TEXT NOT NULL,
	PRIMARY KEY (idpauta),
	CONSTRAINT fk_pauta_ata FOREIGN KEY (idata) REFERENCES atas (idata) ON DELETE NO ACTION ON UPDATE NO ACTION
);
CREATE INDEX fk_pauta_ata_idx ON pautas (idata);

CREATE TABLE ataparticipantes (
	idataparticipante SERIAL NOT NULL,
	idata INT NOT NULL,
	idusuario INT NOT NULL,
	designacao VARCHAR(50) NOT NULL,
	presente SMALLINT NOT NULL,
	membro SMALLINT NOT NULL,
	motivo VARCHAR(255) NOT NULL,
	PRIMARY KEY (idataparticipante),
	CONSTRAINT fk_ataparticipantes_ata FOREIGN KEY (idata) REFERENCES atas (idata) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT fk_ataparticipantes_usuario FOREIGN KEY (idusuario) REFERENCES usuarios (idusuario) ON DELETE NO ACTION ON UPDATE NO ACTION
);
CREATE INDEX fk_ataparticipantes_ata_idx ON ataparticipantes (idata);
CREATE INDEX fk_ataparticipantes_usuario_idx ON ataparticipantes (idusuario);

CREATE TABLE anexos (
    idanexo SERIAL NOT NULL,
    idata INT NOT NULL,
    descricao VARCHAR(50) NOT NULL,
    ordem SMALLINT NOT NULL,
    arquivo BYTEA NOT NULL,
    PRIMARY KEY (idanexo),
	CONSTRAINT fk_anexos_ata FOREIGN KEY (idata) REFERENCES atas (idata) ON UPDATE NO ACTION ON DELETE NO ACTION
);
CREATE INDEX fk_anexos_ata_idx ON anexos (idata);

CREATE TABLE comentarios (
	idcomentario SERIAL NOT NULL,
	idpauta INT NOT NULL,
	idusuario INT NOT NULL,
	situacao SMALLINT NOT NULL,
	comentarios TEXT NOT NULL,
	situacaoComentarios SMALLINT NOT NULL,
	motivo TEXT NOT NULL,
	PRIMARY KEY (idcomentario),
	CONSTRAINT fk_comentario_pauta FOREIGN KEY (idpauta) REFERENCES pautas (idpauta) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT fk_comentario_usuario FOREIGN KEY (idusuario) REFERENCES usuarios (idusuario) ON DELETE NO ACTION ON UPDATE NO ACTION
);
CREATE INDEX fk_comentario_pauta_idx ON comentarios (idpauta);
CREATE INDEX fk_comentario_usuario_idx ON comentarios (idusuario);

CREATE OR REPLACE FUNCTION year(timestamp) RETURNS integer AS $$
DECLARE
   d ALIAS FOR $1;
BEGIN
   return date_part('year', d);
END;

$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION month(timestamp) RETURNS integer AS $$
DECLARE
   d ALIAS FOR $1;
BEGIN
   return date_part('month', d);
END;

$$ LANGUAGE plpgsql;

INSERT INTO `ldapconfig`(`host`, `port`, `useSSL`, `ignoreCertificates`, `basedn`, `uidField`, `cpfField`, `registerField`, `nameField`, `emailField`) VALUES('200.134.18.102', 636, 1, 1, 'dc=utfpr,dc=edu,dc=br', 'uid', 'employeeNumber', 'carLicense', 'cn', 'mail');
INSERT INTO `usuarios`(`nome`,`login`,`senha`,`email`,`externo`,`ativo`,`administrador`) VALUES('Administrador','admin','7df6c2b08a8b5c53504e829189d9f35c','',1,1,1);
