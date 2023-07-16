-- auto-generated definition
create table process_instance_log
(
    id                  bigint auto_increment
        primary key,
    version_id          bigint       null,
    process_instance_id bigint       not null,
    task_id             bigint       null,
    process_node_id     bigint       null,
    process_type        varchar(255) null,
    message             text         null,
    process_parameter   text         null,
    process_result      text         null,
    created_time        timestamp    not null,
    created_by          varchar(255) not null,
    updated_by          varchar(255) not null,
    updated_time        timestamp    not null
);

