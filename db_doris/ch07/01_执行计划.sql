# 图形化展示执行计划的概要
desc graph
select t.dept_id, b.dept_name, sum(t.salary) as dept_salary
from example_db.emp_info t, example_db.dept_info b
where t.dept_id = b.dept_id
group by t.dept_id, b.dept_name
order by t.dept_id;

# 展示数据过滤条件、执行步骤等
explain
select t.dept_id, b.dept_name, sum(t.salary) as dept_salary
from example_db.emp_info t, example_db.dept_info b
where t.dept_id = b.dept_id
group by t.dept_id, b.dept_name
order by t.dept_id;

# 展示执行哪些列的信息
desc verbose
select t.dept_id, b.dept_name, sum(t.salary) as dept_salary
from example_db.emp_info t, example_db.dept_info b
where t.dept_id = b.dept_id
group by t.dept_id, b.dept_name
order by t.dept_id;