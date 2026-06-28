#!/usr/bin/env bash
set -e
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    SELECT 'CREATE DATABASE ledger_ai' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'ledger_ai')\gexec
EOSQL

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "ledger_ai" <<-EOSQL
    CREATE EXTENSION IF NOT EXISTS vector;
EOSQL