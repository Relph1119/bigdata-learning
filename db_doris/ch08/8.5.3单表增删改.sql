create table demo.emp_info(
    `emp_id` BIGINT NOT NULL COMMENT "员工编号",
    `emp_name` varchar(40) NOT NULL COMMENT "员工姓名",
    `age` int COMMENT "年龄",
    `dept_id` int COMMENT "员工部门编号", 
    `salary` decimal(22,4) COMMENT "员工薪水", 
  PRIMARY KEY (`emp_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

INSERT INTO demo.emp_info VALUES
    (1,'Paul',32,1000,200000), 
    (2,'Allen',25,1100,150000),
    (3,'Teddy',23,1100,80000);

    
INSERT INTO demo.emp_info VALUES
    (4,'King',32,100,23000);
    
    
CREATE TABLE IF NOT EXISTS example_db.ods_emp_info(
    `emp_id` BIGINT NOT NULL COMMENT "员工编号",
    `emp_name` varchar(40) NOT NULL COMMENT "员工姓名",
    `age` int COMMENT "年龄",
    `dept_id` int COMMENT "员工部门编号", 
    `salary` decimal(22,4) COMMENT "员工薪水" ,
    `database_name` varchar(40) COMMENT "数据来源库" ,
    `table_name` varchar(80) COMMENT "数据来源表" 
)UNIQUE KEY(`emp_id`)
COMMENT '员工信息表'
Distributed by HASH(`emp_id`) BUCKETS 3
Properties(
 "replication_num"="1"
); 

INSERT INTO example_db.ods_emp_info VALUES
 (4,'Mark',25,1100,65000,'example_db','ods_emp_info') ;
 

CREATE TABLE flink_mysql_emp_source (
  emp_id BIGINT NOT NULL,
  emp_name STRING,
  age INT,
  dept_id INT,
  salary DECIMAL(22,4),
  database_name STRING METADATA VIRTUAL,
  table_name STRING METADATA VIRTUAL,
  PRIMARY KEY (`emp_id`) NOT ENFORCED
 ) WITH (
    'connector' = 'mysql-cdc',
    'hostname' = '120.48.17.186',
    'port' = '13306',
    'username' = 'root',
    'password' = 'Admin@123',
    'database-name' = 'demo',
    'table-name' = 'emp_info'
 );
 
select * from flink_mysql_emp_source;

 
CREATE TABLE flink_doris_emp_sink (
  emp_id BIGINT NOT NULL,
  emp_name STRING,
  age INT,
  dept_id INT,
  salary DECIMAL(22,4),
  database_name STRING,
  table_name STRING 
 ) WITH (
      'connector' = 'doris',
      'fenodes' = '203.57.238.114:8030',
      'table.identifier' = 'example_db.ods_emp_info',
      'username' = 'root',
      'password' = 'doris123', 
      'sink.properties.two_phase_commit'='true',
      'sink.label-prefix'='doris_demo_emp_003'
);

select * from flink_doris_emp_sink;

SET execution.checkpointing.interval = 10s;

INSERT INTO flink_doris_emp_sink 
select emp_id,emp_name,age,dept_id,salary,database_name,table_name
from flink_mysql_emp_source;



--依次在mysql执行
--插入
INSERT INTO demo.emp_info VALUES
    (6,'Kim',22,1200,75000);
--修改
update demo.emp_info set salary =8500 where emp_id =6;
--删除
delete from demo.emp_info where emp_id =6;





