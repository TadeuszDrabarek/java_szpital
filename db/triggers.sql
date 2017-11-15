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
	set NEW.dataumowienia=coalesce(NEW.dataumowienia,sysdate());
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

