create table Tirocinio
(
id		int	auto_increment	not null,
studente varchar(120)	not null,
tutorAmministrativo	varchar(120)	not null,
tutorAziendale	varchar(120)	not null,
azienda		varchar(30)	not null,
primary key(id)
)