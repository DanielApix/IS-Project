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