-- auto-generated definition
create table process_link
(
    id                  bigint auto_increment
        primary key,
    version_id          bigint        null,
    process_instance_id bigint        not null,
    uuid                varchar(500)  null,
    source_node_id      bigint        not null,
    target_node_id      bigint        not null,
    target_port_id      varchar(255)  null,
    source_port_id      varchar(255)  null,
    line_condition      text          null,
    is_delete           int default 0 not null,
    created_by          varchar(255)  not null,
    created_time        timestamp     not null,
    updated_by          varchar(255)  not null,
    updated_time        timestamp     not null
);

create index process_instance_id
    on process_link (process_instance_id);

create index source_node_id
    on process_link (source_node_id);

create index target_node_id
    on process_link (target_node_id);

