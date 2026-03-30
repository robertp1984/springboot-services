FROM postgres:18.0

COPY config/ca.crt /etc/postgresql/ca.crt
COPY config/server.crt /etc/postgresql/server.crt
COPY config/server.key /etc/postgresql/server.key
COPY config/postgresql.conf /etc/postgresql/postgresql.conf

RUN chmod 600 /etc/postgresql/ca.crt /etc/postgresql/server.crt /etc/postgresql/server.key
RUN chown postgres:postgres /etc/postgresql/ca.crt /etc/postgresql/server.crt /etc/postgresql/server.key

ENTRYPOINT [ "docker-entrypoint.sh", "postgres", "-cconfig_file=/etc/postgresql/postgresql.conf"]
