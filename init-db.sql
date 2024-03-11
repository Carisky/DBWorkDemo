
CREATE TABLE notice (
    id                  serial,
    message             varchar(255),
    type                varchar(255),
    processed           boolean,
    primary key (id)
)

