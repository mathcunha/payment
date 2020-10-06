DROP SEQUENCE IF EXISTS arquivos_cip_id_seq;
CREATE SEQUENCE arquivos_cip_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1;
DROP SEQUENCE IF EXISTS arquivos_cnab_id_seq;
CREATE SEQUENCE arquivos_cnab_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1;
DROP SEQUENCE IF EXISTS lotes_id_seq;
CREATE SEQUENCE lotes_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1;
DROP SEQUENCE IF EXISTS pagamentos_id_seq;
CREATE SEQUENCE pagamentos_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1;

DROP TABLE IF EXISTS "arquivos_cnab";
CREATE TABLE "public"."arquivos_cnab" (
    "id" bigint DEFAULT nextval('arquivos_cnab_id_seq') NOT NULL,
    "created" timestamp NOT NULL,
    "updated" timestamp NOT NULL,
    "version" bigint NOT NULL,
    "nome" character varying(300) NOT NULL,
    CONSTRAINT "arquivos_cnab_pkey" PRIMARY KEY ("id"),
    CONSTRAINT "uk_arquivocnab_nome" UNIQUE ("nome")
) WITH (oids = false);


DROP TABLE IF EXISTS "lotes";
CREATE TABLE "public"."lotes" (
    "id" bigint DEFAULT nextval('lotes_id_seq') NOT NULL,
    "created" timestamp NOT NULL,
    "updated" timestamp NOT NULL,
    "version" bigint NOT NULL,
    "status" integer NOT NULL,
    "arquivo_cnab_id" bigint NOT NULL,
    CONSTRAINT "lotes_pkey" PRIMARY KEY ("id"),
    CONSTRAINT "fkct34utiks23p928wmoxmrxivq" FOREIGN KEY (arquivo_cnab_id) REFERENCES arquivos_cnab(id) NOT DEFERRABLE
) WITH (oids = false);


DROP TABLE IF EXISTS "pagamentos";
CREATE TABLE "public"."pagamentos" (
    "id" bigint DEFAULT nextval('pagamentos_id_seq') NOT NULL,
    "created" timestamp NOT NULL,
    "updated" timestamp NOT NULL,
    "version" bigint NOT NULL,
    "agencia" integer NOT NULL,
    "conta" integer NOT NULL,
    "valor" float(4) NOT NULL,
    "lote_id" bigint NOT NULL,
    CONSTRAINT "pagamentos_pkey" PRIMARY KEY ("id"),
    CONSTRAINT "fkl2n5emqyen3qelr828g78jv23" FOREIGN KEY (lote_id) REFERENCES lotes(id) NOT DEFERRABLE
) WITH (oids = false);

DROP TABLE IF EXISTS "arquivos_cip";
CREATE TABLE "public"."arquivos_cip" (
    "id" bigint DEFAULT nextval('arquivos_cip_id_seq') NOT NULL,
    "created" timestamp NOT NULL,
    "updated" timestamp NOT NULL,
    "version" bigint NOT NULL,
    "name" character varying(150),
    "total" float(4) NOT NULL,
    "lote_id" bigint NOT NULL,
    CONSTRAINT "arquivos_cip_pkey" PRIMARY KEY ("id"),
    CONSTRAINT "fk521a1y6arptaamoyjeg5ufig" FOREIGN KEY (lote_id) REFERENCES lotes(id) NOT DEFERRABLE
) WITH (oids = false);