-- auto-generated definition
create table test_tool_run_record
(
    id             bigint auto_increment
        primary key,
    tool_id        bigint        not null comment '测试工具ID',
    start_time     datetime      not null comment '开始时间',
    end_time       datetime      null comment '结束时间',
    request_param  text          null comment '请求参数',
    response_param text          null comment '返回参数',
    status         int default 0 not null comment '运行状态',
    result         varchar(200)  not null comment '运行结果',
    operator       varchar(50)   not null comment '执行人',
    create_time    datetime      not null comment '创建时间',
    update_time    datetime      null comment '更新时间'
)
    comment '测试工具运行记录';

create index idx_tool_id
    on test_tool_run_record (tool_id);

