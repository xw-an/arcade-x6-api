-- auto-generated definition
create table process_instance_parameter
(
    id                  bigint auto_increment
        primary key,
    version_id          bigint        null,
    process_instance_id bigint        not null,
    process_node_id     bigint        null,
    process_link_id     bigint        null,
    parameter_type      varchar(255)  null,
    parameter_name      varchar(255)  not null,
    parameter_value     text          null,
    is_delete           int default 0 not null,
    created_by          varchar(255)  not null,
    created_time        timestamp     not null,
    updated_by          varchar(255)  not null,
    updated_time        timestamp     not null
);

create index process_instance_id
    on process_instance_parameter (process_instance_id);

create index process_link_id
    on process_instance_parameter (process_link_id);

create index process_node_id
    on process_instance_parameter (process_node_id);

