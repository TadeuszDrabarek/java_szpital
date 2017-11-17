DELIMITER //
create trigger beforeInsertPacjent
before insert on Pacjenci for each row
begin
	set NEW.datautworzenia=coalesce(NEW.datautworzenia,sysdate());
end;//
delimiter ;



DELIMITER //
create trigger beforeInsertWizyty
before insert on Wizyty for each row
begin
	declare date_check integer;
    declare time_check integer;
    declare _idtime integer;
    declare _iddate integer;
    declare _idlek integer;
    
    select count(*) into date_check from daty where date =NEW.data;
    if (date_check<1) then
		signal sqlstate '45000' set message_text="Data spoza zakresu !";
    end if;
    
    select iddate into _iddate from daty where date =NEW.data;
    
    select count(*) into time_check from czasy where godzina_od=NEW.czas;
    if (time_check<1) then
		signal sqlstate '45000' set message_text="Czas spoza zakresu !";
    end if;
    
    select idtime into _idtime from czasy where godzina_od=NEW.czas;
    
	set NEW.dataumowienia=coalesce(NEW.dataumowienia,sysdate());
    #set NEW.id=coalesce(NEW.id,_iddate*1024+_idtime);
    
end;//
delimiter ;

DELIMITER //
create trigger beforeInsertGrafikDet
before insert on Grafikdet for each row
begin
	declare idl integer;
    declare ile integer;
    
	SELECT 
		idlekarza
	into idl
	FROM
		lekarzespecjalnosci
	WHERE
		idls = NEW.idls;
		
		SELECT 
		COUNT(*)
	INTO ile FROM
		grafikdet gd
			INNER JOIN
		lekarzespecjalnosci ls ON gd.idls = ls.idls
	WHERE
		ls.idlekarza = idl
			AND gd.dzientyg = NEW.dzientyg
			AND (NEW.godzina_od BETWEEN gd.godzina_od AND gd.godzina_do
			OR NEW.godzina_do BETWEEN gd.godzina_od AND gd.godzina_do
			OR NEW.godzina_od < gd.godzina_od
			AND NEW.godzina_do > gd.godzina_do);
			
	if (ile>0) then
		signal sqlstate '45000' set message_text="Wystąpił konflikt godzin !";
	end if;
end;//
delimiter ;


DELIMITER //
create trigger beforeInsertGrafikDet
before insert on Grafikdet for each row
begin
	
    declare ile integer;
    
	select sprawdz_dyzur(NEW.idls, NEW.dzientyg , NEW.godzina_od, NEW.godzina_do)
    into ile;
		
	if (ile>0) then
		signal sqlstate '45000' set message_text="Wystąpił konflikt godzin !";
	end if;
end;//
delimiter ;
