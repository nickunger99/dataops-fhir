FROM postgres:16.2

ENV POSTGRES_MULTIPLE_DATABASES={POSTGRES_MULTIPLE_DATABASES}
ENV POSTGRES_USER={POSTGRES_USER}
ENV POSTGRES_PASSWORD={POSTGRES_PASSWORD}
ENV POSTGRES_DB={POSTGRES_DB}

COPY ./init-db.sh /docker-entrypoint-initdb.d/

RUN chmod +x /docker-entrypoint-initdb.d/init-db.sh
