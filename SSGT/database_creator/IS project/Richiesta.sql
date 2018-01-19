create table Richiesta
(
	id		int		auto_increment	not null,
    mittente	varchar(120)	not null,
    tipo	varchar(100)	not null,
    primary key(id)
)