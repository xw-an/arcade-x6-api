-- auto-generated definition
create table process_instance
(
    id            bigint auto_increment
        primary key,
    name          varchar(255)  not null,
    description   varchar(500)  null,
    business_line varchar(255)  null,
    type          varchar(255)  not null,
    times         int default 0 null,
    status        int default 0 not null,
    created_by    varchar(255)  not null,
    created_time  timestamp     not null,
    updated_by    varchar(255)  not null,
    updated_time  timestamp     not null
);

