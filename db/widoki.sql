
-- Widok zwracający listę lekarzy i ich specjalnośsci
create or replace view v_lekarzespecjalnosci as 
select ls.idls, concat(l.imie,' ',l.nazwisko) as lekarz, s.nazwaspecjalnosci
	, l.idlekarza, s.idspecjalnosci
from lekarzespecjalnosci ls
inner join lekarze l on l.idlekarza=ls.idlekarza
inner join specjalnosci s on s.idspecjalnosci=ls.idspecjalnosci
;


-- Widok zwracającyh dzienny grafik lekarzy
-- należy podawać albo datę (date) nalbo względną liczbę dni od dzisiaj (days_from_tomorrow)
create or replace view v_grafik_dzienny as
select gd.dzientyg
	, l.imie, l.nazwisko, l.idlekarza
    , s.nazwaspecjalnosci, s.idspecjalnosci
    , vt.godzina_od, vt.godzina_do, vt.idtime
    , vd.date
    , vd.days_from_tomorrow
from grafikdet gd
inner join lekarzespecjalnosci ls on ls.idls=gd.idls
inner join lekarze l on l.idlekarza=ls.idlekarza
inner join specjalnosci s on s.idspecjalnosci=ls.idspecjalnosci
inner join daty vd on vd.dayofweek=gd.dzientyg
inner join czasy vt on gd.godzina_od<=vt.godzina_od      
					  and gd.godzina_do>=vt.godzina_do
inner join grafiki g on g.data_od<=vd.date and g.data_do>=vd.date 
                       and g.idgrafiku=gd.idgrafiku
; 