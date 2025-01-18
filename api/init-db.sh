#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
	    SELECT 'CREATE DATABASE $PROD_DB_NAME' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = '$PROD_DB_NAME')\gexec
	    \c $PROD_DB_NAME

	    -- Clear all tables if they exist (will be recreated by JPA)
	    DO \$\$
	    DECLARE
	        r RECORD;
	    BEGIN
	        FOR r IN (SELECT tablename FROM pg_tables WHERE schemaname = current_schema()) LOOP
	            EXECUTE 'DROP TABLE IF EXISTS ' || quote_ident(r.tablename) || ' CASCADE';
	        END LOOP;
	    END \$\$;
EOSQL
