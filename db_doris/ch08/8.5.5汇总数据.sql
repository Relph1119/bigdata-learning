
CREATE TABLE IF NOT EXISTS example_db.ods_dept_sum( 
    `dept_id` int COMMENT "员工部门编号", 
    `age` float COMMENT "年龄", 
    `salary` decimal(22,4) COMMENT "员工薪水"   
)Unique KEY(`dept_id`)
COMMENT '员工详细信息表'
Distributed by HASH(`dept_id`) BUCKETS 1
Properties(
 "replication_num"="1"
); 



CREATE TABLE flink_doris_dept_sum_sink (
  dept_id INT,
  age FLOAT,
  salary DECIMAL(22,4) 
 ) WITH (
      'connector' = 'doris',
      'fenodes' = '203.57.238.114:8030',
      'table.identifier' = 'example_db.ods_emp_detail',
      'username' = 'root',
      'password' = 'doris123', 
      'sink.properties.two_phase_commit'='true' ,
      'sink.label-prefix'='doris_demo_dept_sum_003' 
);

select * from flink_doris_dept_sum_sink;

SET execution.checkpointing.interval = 10s;

INSERT INTO flink_doris_emp_detail_sink 
select a.dept_id,avg(age) as age,sum(salary ) as salary 
  from flink_mysql_emp_source a 
 group by a.dept_id;