# 查询员工薪资最大值和最小值的不同写法
select b.dept_id, b.dept_name, t.emp_id, t.emp_name, t.age, t.salary,
    max(t.salary) over(partition by t.dept_id) as salary_max,
    min(t.salary) over(partition by t.dept_id) as salary_min,
    first_value(t.salary) over (partition by t.dept_id order by t.salary desc rows between unbounded preceding and unbounded following) as salary_first,
    last_value(t.salary) over (partition by t.dept_id order by t.salary desc rows between unbounded preceding and unbounded following) as salary_last,
    first_value(t.salary) over (partition by t.dept_id order by t.salary desc) as salary_first_1,
    last_value(t.salary) over (partition by t.dept_id order by t.salary desc rows between unbounded preceding and unbounded following) as salary_last_1

from emp_info t, dept_info b where t.dept_id = b.dept_id order by t.dept_id, t.emp_id;

# 对员工年龄进行排序
select
    row_number() over (order by t.age) as 'row_number排名',
    rank() over (order by t.age) as 'rank排名',
    dense_rank() over (order by t.age) as 'dense_rank排名'
from emp_info t order by t.age;

# 对员工薪资进行条件汇总
select t.emp_id, t.emp_name, t.age, t.salary,
    sum(t.salary) over() as '全局汇总',
    sum(t.salary) over(order by t.emp_id) as '逐行累加',
    sum(t.salary) over(partition by t.dept_id) as '分组汇总',
    sum(t.salary) over(partition by t.dept_id order by t.emp_id) as '分组逐行累加'
from emp_info t order by t.emp_id;

# 查询部门总薪资大于所有部门平均总薪资的部门员工信息及部门平均薪资、公司平均薪资
select * from (
    select emp_id, emp_name, dept_id, salary,
           avg(salary) over (partition by dept_id) dept_avg_salary,
           avg(salary) over () as comp_avg_salary
    from emp_info ) t
where t.dept_avg_salary > t.comp_avg_salary;
