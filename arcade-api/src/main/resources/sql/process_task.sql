-- auto-generated definition
create table process_task
(
    id                  bigint auto_increment
        primary key,
    version_id          bigint                              null,
    process_instance_id bigint                              null,
    status              int       default 0                 not null,
    created_by          varchar(255)                        not null,
    created_time        timestamp default CURRENT_TIMESTAMP null,
    updated_by          varchar(255)                        not null,
    updated_time        timestamp                           null on update CURRENT_TIMESTAMP
);

