-- FUNCTION: public.finishorder()

-- DROP FUNCTION public.finishorder();

CREATE FUNCTION public.finishorder()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100.0
    VOLATILE NOT LEAKPROOF 
AS $BODY$

BEGIN
    IF NEW.state = 'FINISHED' THEN
        IF (select count(*) FROM public.lot WHERE state != 'FINISHED' AND lot."ORDER" = NEW."ORDER") = 0
        THEN
            UPDATE pom.Order set state = 'COMPLETED' WHERE orderno = new."ORDER";
        END IF; 
    END IF;
RETURN NEW;
END;

$BODY$;

ALTER FUNCTION public.finishorder()
    OWNER TO postgres;

