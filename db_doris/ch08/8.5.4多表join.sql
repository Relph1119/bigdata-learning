
create table demo.dept_info(
    `dept_id` BIGINT NOT NULL COMMENT "部门编号",
    `dept_name` varchar(40) NOT NULL COMMENT "部门名称",
    `dept_leader` int COMMENT "部门主管编号",
    `parent_dept` int COMMENT "上级部门", 
    `dept_level` int COMMENT "部门层级" ,
  PRIMARY KEY (`dept_id`)
)
ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 
COMMENT '部门信息表';


INSERT INTO demo.dept_info VALUES
    (1000,'总经办',1,null,1),
    (1100,'销售部',2,1000,2),
    (1200,'研发部',5,1000,2),
    (1110,'销售一部',7,1100,3);


CREATE TABLE IF NOT EXISTS example_db.ods_emp_detail(
    `emp_id` BIGINT NOT NULL COMMENT "员工编号",
    `emp_name` varchar(40) NOT NULL COMMENT "员工姓名",
    `age` int COMMENT "年龄",
    `dept_id` int COMMENT "员工部门编号", 
    `dept_name` varchar(40) COMMENT "部门名称", 
    `dept_leader` int COMMENT "部门主管编号",
    `salary` decimal(22,4) COMMENT "员工薪水" ,
    `database_name` varchar(40) COMMENT "数据来源库" ,
    `table_name` varchar(80) COMMENT "数据来源表" 
)DUPLICATE KEY(`emp_id`)
COMMENT '员工详细信息表'
Distributed by HASH(`emp_id`) BUCKETS 3
Properties(
 "replication_num"="1"
); 

INSERT INTO example_db.ods_emp_detail VALUES
 (4,'Mark',25,1100,'销售部',2,65000,'example_db','ods_emp_info') ;
 

CREATE TABLE flink_mysql_dept_source (
  dept_id BIGINT NOT NULL,
  dept_name STRING,
  dept_leader INT,
  parent_dept INT,
  dept_level INT,
  database_name STRING METADATA VIRTUAL,
  table_name STRING METADATA VIRTUAL,
  PRIMARY KEY (`dept_id`) NOT ENFORCED
 ) WITH (
    'connector' = 'mysql-cdc',
    'hostname' = '120.48.17.186',
    'port' = '13306',
    'username' = 'root',
    'password' = 'Admin@123',
    'database-name' = 'demo',
    'table-name' = 'emp_info'
 );


CREATE TABLE flink_doris_emp_detail_sink (
  emp_id BIGINT NOT NULL,
  emp_name STRING,
  age INT,
  dept_id INT,
  dept_name STRING,
  dept_leader INT,
  salary DECIMAL(22,4),
  database_name STRING,
  table_name STRING 
 ) WITH (
      'connector' = 'doris',
      'fenodes' = '203.57.238.114:8030',
      'table.identifier' = 'example_db.ods_emp_detail',
      'username' = 'root',
      'password' = 'doris123', 
      'sink.properties.two_phase_commit'='true',
      'sink.label-prefix'='doris_demo_emp_detail_001' 
);

select * from flink_doris_emp_detail_sink;

SET execution.checkpointing.interval = 10s;

INSERT INTO flink_doris_emp_detail_sink 
select a.emp_id,a.emp_name,a.age,a.dept_id,b.dept_name,b.dept_leader,a.salary,a.database_name,a.table_name
from flink_mysql_emp_source a 
inner join flink_mysql_dept_source b 
on a.dept_id = b.dept_id;

