 DELIMITER //
 create function sprawdz_dyzur(_idls integer, _dzientyg integer, _godzina_od time, _godzina_do time) returns integer
 begin
 declare idl integer;
    declare ile integer;
    
	SELECT 
		idlekarza
	into idl
	FROM
		lekarzespecjalnosci
	WHERE
		idls = _idls;
		
		SELECT 
		COUNT(*)
	INTO ile FROM
		grafikdet gd
			INNER JOIN
		lekarzespecjalnosci ls ON gd.idls = ls.idls
	WHERE
		ls.idlekarza = idl
			AND gd.dzientyg = _dzientyg
			AND (_godzina_od BETWEEN gd.godzina_od AND gd.godzina_do
			OR _godzina_do BETWEEN gd.godzina_od AND gd.godzina_do
			OR _godzina_od < gd.godzina_od
			AND _godzina_do > gd.godzina_do);
            
	return ile;
end;
//
DELIMITER ;


drop function dodaj_wizyte;
DELIMITER //
create function dodaj_wizyte(_idpacjenta integer, uniqueid varchar(20)) returns integer
begin
	declare _czas time;
    declare _date date;
    declare id integer;
    declare ret integer;
    declare uid integer;
    declare _idls integer;
    
    select -1 into ret;
        
    select convert(substr(uniqueid,1,8), unsigned integer) into uid;
    select convert(substr(uniqueid,10), unsigned integer) into _idls;
    select date into _date from daty where iddate=floor(uid/1420);
    select godzina_od into _czas from czasy where idtime=uid%floor(uid/1420);
    
    select godzina_od into _czas from v_grafik_dzienny 
		where (1=1)
		and idls=_idls
        and date=_date
        and godzina_od=_czas
		and idunique=uniqueid
        ;
    if (_czas is not null) then
		#select id=coalesce(NEW.id,_iddate*1024+_idtime) into id;
		insert into wizyty(idpacjenta, idls, data, czas, idunique)
        values(_idpacjenta, _idls, _date, _czas, uniqueid );
        
        select 1 into ret;
    else
		select 0 into ret;
    end if;
    
    return ret;
    
end;
//
DELIMITER ;