#!/bin/bash
set -e

# Verifica se a variável POSTGRES_MULTIPLE_DATABASES foi definida
if [ -n "$POSTGRES_MULTIPLE_DATABASES" ]; then
    echo "Creating multiple databases: $POSTGRES_MULTIPLE_DATABASES"
    IFS=',' read -ra DBS <<< "$POSTGRES_MULTIPLE_DATABASES"
    for db in "${DBS[@]}"; do
        echo "Creating database '$db'"
        psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<EOSQL
            CREATE DATABASE "$db";
EOSQL
    done
    echo "Databases created."
fi
