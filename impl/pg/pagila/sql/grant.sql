
-- REVOKE ALL PRIVILEGES ON DATABASE pagila FROM pagila
-- ;;

-- DROP ROLE pagila 
-- ;;

CREATE ROLE pagila LOGIN PASSWORD 'password' VALID UNTIL 'infinity'
;
 
GRANT ALL PRIVILEGES ON DATABASE pagila TO pagila
;

GRANT ALL ON SCHEMA public TO pagila
;

-- select table_name from information_schema.tables where table_schema = 'public' 

GRANT ALL ON TABLE public.film TO pagila
;
GRANT ALL ON TABLE public.actor_info TO pagila
;
GRANT ALL ON TABLE public.film_actor TO pagila
;
GRANT ALL ON TABLE public.film_category TO pagila
;
GRANT ALL ON TABLE public.country TO pagila
;
GRANT ALL ON TABLE public.customer TO pagila
;
GRANT ALL ON TABLE public.category TO pagila
;
GRANT ALL ON TABLE public.customer_list TO pagila
;
GRANT ALL ON TABLE public.film_list TO pagila
;
GRANT ALL ON TABLE public.nicer_but_slower_film_list TO pagila
;
GRANT ALL ON TABLE public.language TO pagila
;
GRANT ALL ON TABLE public.payment_p2007_02 TO pagila
;
GRANT ALL ON TABLE public.payment_p2007_03 TO pagila
;
GRANT ALL ON TABLE public.payment_p2007_04 TO pagila
;
GRANT ALL ON TABLE public.payment_p2007_05 TO pagila
;
GRANT ALL ON TABLE public.payment_p2007_06 TO pagila
;
GRANT ALL ON TABLE public.inventory TO pagila
;
GRANT ALL ON TABLE public.sales_by_film_category TO pagila
;
GRANT ALL ON TABLE public.sales_by_store TO pagila
;
GRANT ALL ON TABLE public.staff_list TO pagila
;
GRANT ALL ON TABLE public.payment_p2007_01 TO pagila
;
GRANT ALL ON TABLE public.city TO pagila
;
GRANT ALL ON TABLE public.payment TO pagila
;
GRANT ALL ON TABLE public.rental TO pagila
;
GRANT ALL ON TABLE public.address TO pagila
;
GRANT ALL ON TABLE public.actor TO pagila
;
GRANT ALL ON TABLE public.store TO pagila
;
GRANT ALL ON TABLE public.staff TO pagila
;
