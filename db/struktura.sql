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

alter table Lekarze change idvalid isvalid integer; 

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

