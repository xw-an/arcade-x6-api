-- auto-generated definition
create table process_instance_version
(
    id                  bigint auto_increment
        primary key,
    process_instance_id bigint        not null,
    version             int           not null,
    status              int default 0 not null,
    created_by          varchar(255)  not null,
    created_time        timestamp     not null,
    updated_by          varchar(255)  not null,
    updated_time        timestamp     not null
);

