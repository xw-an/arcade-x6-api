-- auto-generated definition
create table process_node
(
    id                  bigint auto_increment
        primary key,
    version_id          bigint        null,
    process_instance_id bigint        not null,
    uuid                varchar(500)  not null,
    name                varchar(255)  not null,
    type                varchar(255)  not null,
    position            varchar(255)  not null,
    size                varchar(255)  not null,
    ports               varchar(500)  null,
    is_delete           int default 0 not null,
    created_by          varchar(255)  not null,
    created_time        timestamp     not null,
    updated_by          varchar(255)  not null,
    updated_time        timestamp     not null
);

create index process_instance_id
    on process_node (process_instance_id);

create index process_node_uuid_index
    on process_node (uuid);

