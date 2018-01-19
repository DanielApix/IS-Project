create table ricevuto_da
(
	id	int  references Richiesta(id),
    mittente	varchar(120)	not null,
    risposta	varchar(100) default 'nonRicevuto',
    destinatario varchar(120) not null,
    primary key(id,	mittente, destinatario)
)