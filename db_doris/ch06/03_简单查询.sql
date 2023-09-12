# 按照部门编号查看部门员工的平均年龄和合计薪资
select dept_id, avg(age) avg_age, sum(salary) total_salary from emp_info group by dept_id;

# 按照部门信息升序、薪资降序查询员工信息
select * from emp_info order by dept_id asc, salary desc;

# 查询薪资排名前五的员工
select * from emp_info order by salary desc limit 5;

# 查询全部的部门信息，按照部门层级展开
select dept_id dept_lvl1_id, dept_name dept_lvl1_name, null dept_lvl2_id, null dept_lvl2_name, null dept_lvl3_id, null dept_lvl3_name
from dept_info
where dept_level = 1
union all
select b.dept_id detp_lvl1_id, b.dept_name dept_lvl1_name, t.dept_id dept_lvl2_id, t.dept_name dept_lvl2_name, null dept_lvl3_id, null dept_lvl3_name
from dept_info t
left join dept_info b
on t.parent_dept = b.dept_id
where t.dept_level=2
union all
select c.dept_id detp_lvl1_id, c.dept_name dept_lvl1_name, b.dept_id dept_lvl2_id, b.dept_name dept_lvl2_name, t.dept_id dept_lvl3_id, t.dept_name dept_lvl3_name
from dept_info t
left join dept_info b
on t.parent_dept = b.dept_id
left join dept_info c
on b.parent_dept = c.dept_id
where t.dept_level=3;

# 查询平均年龄大于25的员工部门编号、员工人数和薪资总和
select dept_id, count(emp_id) emp_cnt, sum(salary) total_salary from emp_info
group by dept_id having avg(age) > 25

# 查询销售部的全部人数及对应的信息（按照员工升序展示）
# 向查询销售部的二级部门和三级部门，再关联获取对应部门的员工信息
with sales_dept as (
select dept_id, dept_name from dept_info where dept_name = '销售部'
union all
select t.dept_id, t.dept_name from dept_info t
inner join dept_info b on t.parent_dept = b.dept_id and b.dept_name = '销售部'
)
select b.dept_name, t.* from emp_info t, sales_dept b where t.dept_id = b.dept_id order by emp_id;

# 查询部门号为1200的员工涨薪15%以后的合计薪资
with salary_rst as (
    select sum(salary) total_salary from emp_info where dept_id = '1200'
)
select total_salary * (1 + 0.15) as new_total_salary from salary_rst;

# 查询销售部汲取下属部门，合并查询结果
with sales_dept as (
    select dept_id, dept_name from dept_info where dept_name='销售部'
),
lvl3_sales_dept as (
    select t.dept_id, t.dept_name from dept_info t, sales_dept b where t.parent_dept = b.dept_id
)
select dept_id, dept_name from sales_dept
union all
select dept_id, dept_name from lvl3_sales_dept;

# 查询部门总薪资岛屿所有部门平均总薪资的部门的员工信息、部门平均薪资、公司平均薪资
with dept_avg_salary as (
    select dept_id, avg(salary) as avg_salary from emp_info group by dept_id
), avg_salary as (
    select avg(salary) as avg_salary from emp_info
), dept_rst as (
    select t.dept_id, t.avg_salary as dept_avg_salary, b.avg_salary as comp_avg_salary
    from dept_avg_salary t, avg_salary b where t.avg_salary > b.avg_salary
)
select t.emp_id, t.emp_name, t.dept_id, t.salary, b.dept_avg_salary, b.comp_avg_salary
from emp_info t, dept_rst b where t.dept_id = b.dept_id;

