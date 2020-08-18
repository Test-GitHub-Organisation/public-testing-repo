select * from students
where birthdate between '01/01/1989' and '12/31/1989' AND
select name,surname,class,gender from students
where (class='9Math' and gender='M') or (class='9His' and gender='F')