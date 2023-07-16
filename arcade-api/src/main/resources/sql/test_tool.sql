-- auto-generated definition
create table test_tool
(
    id             bigint auto_increment comment '工具ID，自增长'
        primary key,
    tool_name      varchar(100)                       not null comment '工具名称',
    description    varchar(500)                       not null comment '工具描述',
    department     varchar(100)                       null comment '部门',
    business_line  varchar(50)                        not null comment '工具所属业务线',
    developer      varchar(100)                       not null comment '工具开发者',
    tags           varchar(200)                       null comment '工具标签，多个标签用逗号分隔',
    tool_type      varchar(50)                        not null comment '工具类型，例如Java、Python、Shell等',
    status         int      default 1                 not null comment '工具状态，0：禁用，1：启用',
    usage_restrict varchar(200)                       null comment '工具使用限制，例如授权、收费等，可为空值',
    create_time    datetime default CURRENT_TIMESTAMP not null comment '工具创建时间，自动生成当前时间',
    update_time    datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '工具修改时间，自动生成当前时间'
)
    comment '测试工具表';

create index idx_tool_name
    on test_tool (tool_name);

