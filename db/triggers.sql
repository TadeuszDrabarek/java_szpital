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