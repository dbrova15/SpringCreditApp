CREATE TABLE "public"."client"
(
   idx_ref varchar(30) PRIMARY KEY NOT NULL,
   address varchar(45) DEFAULT NULL::character varying,
   curr_salary char(3),
   date_birthday timestamp NOT NULL,
   decision varchar(45) DEFAULT NULL::character varying,
   id_client int NOT NULL,
   limit_itog real,
   mail varchar(45) DEFAULT NULL::character varying,
   month_salary numeric(15,2) DEFAULT NULL::numeric,
   phone varchar(45) DEFAULT NULL::character varying
)
;
CREATE UNIQUE INDEX client_pkey ON "public"."client"(idx_ref)
;
CREATE TABLE "public"."credit"
(
   id_credit bigint PRIMARY KEY NOT NULL,
   amt_credit real,
   date_start date NOT NULL,
   id_client bigint NOT NULL,
   state_credit varchar(1)
)
;
CREATE UNIQUE INDEX credit_pkey ON "public"."credit"(id_credit)
;
