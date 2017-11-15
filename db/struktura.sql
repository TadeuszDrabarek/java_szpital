-- Skrypt tworzy strukturę bazy danych
create database szpital;

-- Utworzenie bazu danych
use szpital;

-- Ustawienie bazy danych jako aktywnej

-- 1. Tabela Lekarze
create table Lekarze (
	idlekarza integer primary key auto_increment,
    imie varchar(50),
    nazwisko varchar(50),
    isvalid integer
);

-- alter table Lekarze change idvalid isvalid integer; 

-- 2. Tabela Specjalności
create table Specjalnosci (
	idspecjalnosci integer primary key auto_increment,
    nazwaspecjalnosci varchar(50),
    isvalid integer
);

-- 3. Tabela definiująca jake specjalności ma lekarz 
# jeden lekarz może mieć wiele specjalności, wielu lekarzy może mieć tę samą specjalność
create table LekarzeSpecjalnosci (
	idls integer primary key auto_increment,
    idlekarza integer,  
    idspecjalnosci integer,
    FOREIGN KEY (idlekarza) references Lekarze(idlekarza),
    FOREIGN KEY (idspecjalnosci) references Specjalnosci(idspecjalnosci)
);

-- 4. Tabela definiująca czas obowiązywania grafiku
create table Grafiki(
	idgrafiku integer primary key auto_increment,
    data_od date,
    data_do date,
    nazwagrafiku varchar(50)
);

-- 5. Tabela opisująca grafik dla lekarzy
create table GrafikDET(
	idgd integer primary key auto_increment,
    dzientyg integer,
    idgrafiku integer,
    godzina_od time,
    godzina_do time,
    idls integer,
    FOREIGN KEY (idgrafiku) references Grafiki(idgrafiku),
    FOREIGN KEY (idls) references LekarzeSpecjalnosci(idls)    
);


-- 6. Tabela z pacjentami
create table Pacjenci(
	idpacjenta integer primary key auto_increment,
    datautworzenia date,
    pesel varchar(11) unique,
    imie varchar(50),
    nazwisko varchar(50),
    adres varchar(200)
);

-- 7. Tabela z umówionymi wizytami
create table Wizyty(
	idwizyty integer primary key auto_increment,
    idpacjenta integer,
    idgd integer,
    opis text,
    data date,
    dataumowienia date,
    czyodbyta integer default 0,
    FOREIGN KEY (idpacjenta) references Pacjenci(idpacjenta),
    FOREIGN KEY (idgd) references GrafikDET(idgd)
);

-- Tabela z datami
create table daty as 
select date, datediff(date,curdate()) as days_from_tomorrow, dayofweek(date) dayofweek from 
(select adddate('1970-01-01',t4*10000 + t3*1000 + t2*100 + t1*10 + t0) date from
 (select 0 t0 union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t0,
 (select 0 t1 union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t1,
 (select 0 t2 union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t2,
 (select 0 t3 union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t3,
 (select 0 t4 union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t4) v
where date between '2017-01-01' and '2511-12-31';

-- Tabela z godzinami przyjęć
create table czasy as 
select sec_to_time(hour*60*60+minutes*60) godzina_od
, sec_to_time(hour*60*60+(minutes+19)*60) godzina_do
, hour*60+minutes idtime 
from (
select 0 hour union select 1 union select 2 union select 3 union select 4 union 
select 5 union select 6 union select 7 union select 8 union select 9 union 
select 10 union select 11 union select 12 union select 13 union select 14 union select 15 union
select 16 union select 17 union select 18 union select 19 union select 20 union select 21 union 
select 22 union select 23 ) h,
(select 0 minutes union select 20 union select 40) m;


