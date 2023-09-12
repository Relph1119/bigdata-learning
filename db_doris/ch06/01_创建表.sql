create table if not exists example_db.emp_info(
    emp_id BIGINT NOT NULL COMMENT '员工编号',
    emp_name varchar(40) NOT NULL COMMENT '员工姓名',
    age int COMMENT '年龄',
    dept_id int COMMENT '员工部门编号',
    salary decimal(22, 4) COMMENT '员工薪资'
) DUPLICATE KEY(emp_id)
COMMENT '员工信息表'
DISTRIBUTED BY HASH(emp_id) BUCKETS 3
PROPERTIES(
    "replication_num"="1"
);

create table if not exists example_db.dept_info(
    dept_id BIGINT NOT NULL COMMENT '部门编号',
    dept_name varchar(40) NOT NULL COMMENT '部门名称',
    dept_leader int COMMENT '部门主管编号',
    parent_dept int COMMENT '上级部门',
    dept_level int COMMENT '部门层级'
) DUPLICATE KEY(dept_id)
COMMENT '部门信息表'
DISTRIBUTED BY HASH(dept_id) BUCKETS 3
PROPERTIES(
    "replication_num"="1"
);