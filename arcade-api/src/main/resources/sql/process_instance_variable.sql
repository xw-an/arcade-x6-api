-- auto-generated definition
create table process_instance_variable
(
    id                  bigint auto_increment
        primary key,
    version_id          bigint                              null,
    process_instance_id bigint                              null,
    process_node_id     bigint                              null,
    type                varchar(255)                        null,
    variable_name       varchar(255)                        null,
    is_delete           int       default 0                 not null,
    created_by          varchar(255)                        not null,
    created_time        timestamp default CURRENT_TIMESTAMP null,
    updated_by          varchar(255)                        not null,
    updated_time        timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP
);

create index process_instance_id
    on process_instance_variable (process_instance_id);

create index process_node_id
    on process_instance_variable (process_node_id);

