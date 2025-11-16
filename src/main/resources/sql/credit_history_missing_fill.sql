update creadit_history b
set num_delinquencies_2yrs =(with random as (select random(1, 89167) rn, a.*
                                             from creadit_history a
                                             where num_delinquencies_2yrs is null),
                                  r_value as (select customer_number,
                                                     rn,
                                                     case
                                                         when rn > 89155 then '2'
                                                         when rn > 87458 then '1'
                                                         else 0 end clas
                                              from random)
                             select clas
                             from r_value a
                             where a.customer_number = b.customer_number)
where num_delinquencies_2yrs is null