# 假设员工Trump已经离职，无部门信息，薪资为空
insert into emp_info values ('10', 'Trump', 35, null, null);

# 获取员工信息表和部门信息表总可以完全匹配的数据
select t.*, b.* from emp_info t
inner join dept_info b
on t.dept_id = b.dept_id
order by t.emp_id;

#