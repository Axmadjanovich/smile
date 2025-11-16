with detl as (select *
              from demographics
              where employment_length = '5.514892986574798'--employment_length is null
),
     detl1 as (select distinct a.age                  d_age,
                               employment_type,
                               education,
                               marital_status,
                               avg(employment_length) over (partition by a.age ,employment_type,education,marital_status) AVG_EMP_L
               from demographics a),
     last_detl as (select *
                   from detl a
                            left join detl1 b on a.age = b.d_age and a.employment_type = b.employment_type
                       and a.education = b.education
                       and a.marital_status = b.marital_status)
update demographics a
set employment_length=(select b.AVG_EMP_L
                       from last_detl b
                       where a.cust_id = b.cust_id)
where a.employment_length = '5.514892986574798' --and a.cust_id='80730' --80730 5.505136884141229 5.514892986574798


select *
from demographics
where employment_length = '5.514892986574798'