
-- Widok zwracający listę lekarzy i ich specjalnośsci
create or replace view v_lekarzespecjalnosci as 
select ls.idls, concat(l.imie,' ',l.nazwisko) as lekarz, s.nazwaspecjalnosci
	, l.idlekarza, s.idspecjalnosci
from lekarzespecjalnosci ls
inner join lekarze l on l.idlekarza=ls.idlekarza
inner join specjalnosci s on s.idspecjalnosci=ls.idspecjalnosci
;