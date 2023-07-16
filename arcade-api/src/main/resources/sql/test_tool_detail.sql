-- auto-generated definition
create table test_tool_detail
(
    id             bigint auto_increment
        primary key,
    tool_id        bigint       not null comment '测试工具ID',
    class_name     varchar(255) not null comment '类名',
    interface_name varchar(255) null comment '接口名',
    request_param  text         null comment '请求参数',
    response_param text         null comment '返回参数',
    create_time    datetime     not null comment '创建时间',
    update_time    datetime     null comment '更新时间'
)
    comment '测试工具详情';

create index idx_tool_id
    on test_tool_detail (tool_id);

