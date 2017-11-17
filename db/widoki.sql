
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
    , vd.date, vd.iddate
    , vd.days_from_tomorrow
    #, vd.iddate*1420+vt.idtime id
    , concat(vd.iddate*1420+vt.idtime,'+',ls.idls) idunique
    , ls.idls
    , w.idwizyty
from grafikdet gd
inner join lekarzespecjalnosci ls on ls.idls=gd.idls
inner join lekarze l on l.idlekarza=ls.idlekarza
inner join specjalnosci s on s.idspecjalnosci=ls.idspecjalnosci
inner join daty vd on vd.dayofweek=gd.dzientyg
inner join czasy vt on gd.godzina_od<=vt.godzina_od      
					  and gd.godzina_do>=vt.godzina_do
inner join grafiki g on g.data_od<=vd.date and g.data_do>=vd.date 
                       and g.idgrafiku=gd.idgrafiku
left join wizyty w on w.idunique=concat(vd.iddate*1420+vt.idtime,'+',ls.idls)                       
; 

-- Grafik dzienny LIGHT
create or replace view v_grafik_dzienny_l as
select concat(l.nazwisko,' ',l.imie) lekarz, s.nazwaspecjalnosci, gd.dzientyg,
concat(date_format(gd.godzina_od,'%H:%i'),'-',date_format(addtime(gd.godzina_do,sec_to_time(60)),'%H:%i')) godziny 
,ls.idls, s.idspecjalnosci, l.idlekarza
from grafikdet gd
inner join lekarzespecjalnosci ls on gd.idls=ls.idls
inner join lekarze l on l.idlekarza=ls.idlekarza
inner join specjalnosci s on s.idspecjalnosci=ls.idspecjalnosci
inner join grafiki g on g.data_od<=curdate() and g.data_do>=curdate() 
                       and g.idgrafiku=gd.idgrafiku;
                       										
                       
-- Widok z datami
create or replace view daty as 
select date, datediff(date,curdate()) as days_from_tomorrow, dayofweek(date) dayofweek
, iddate 
from 
(select adddate('1970-01-01',t4*10000 + t3*1000 + t2*100 + t1*10 + t0) date, t4*10000 + t3*1000 + t2*100 + t1*10 + t0 iddate from
 (select 0 t0 union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t0,
 (select 0 t1 union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t1,
 (select 0 t2 union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t2,
 (select 0 t3 union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t3,
 (select 0 t4 union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t4) v
where date between '2017-01-01' and '2511-12-31';

-- Wizyty umówione
create or replace view v_wizyty_umowione as
select w.idunique, w.idls,w.idpacjenta, w.idwizyty, w.data, w.czas
	,p.imie, p.nazwisko, p.pesel
	,v.lekarz, v.nazwaspecjalnosci, v.idlekarza
    ,d.days_from_tomorrow
from wizyty w
inner join daty d on d.date=w.data
inner join pacjenci p on p.idpacjenta=w.idpacjenta
inner join v_lekarzespecjalnosci v on v.idls=w.idls
;                       