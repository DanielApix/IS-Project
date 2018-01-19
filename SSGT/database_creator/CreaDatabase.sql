create schema ssgt;

create table Account 
(
	username	varchar(120)	not null,
    password	varchar(120)	not null,
    tipo		varchar(100)	not null,
    primary key(username)
)

create table Azienda
(
nome			varchar(30)		not null,
sede					varchar(30)		not null,
tutor_amministrativo	varchar(120)		not null,
tutor_aziendale			varchar(120)		not null,
recapito_tutor_amministrativo	varchar(15),
recapito_tutor_aziendale		varchar(15),
primary key(nome)
)

create table Richiesta
(
	id		int		auto_increment	not null,
    mittente	varchar(120)	not null,
    tipo	varchar(100)	not null,
    primary key(id)
)

create table ricevuto_da
(
	id	int  references Richiesta(id),
    mittente	varchar(120)	not null,
    risposta	varchar(100) default 'nonRicevuto',
    destinatario varchar(120) not null,
    primary key(id,	mittente, destinatario)
)

create table Tirocinio
(
id		int	auto_increment	not null,
studente varchar(120)	not null,
tutorAmministrativo	varchar(120)	not null,
tutorAziendale	varchar(120)	not null,
azienda		varchar(30)	not null,
primary key(id)
)