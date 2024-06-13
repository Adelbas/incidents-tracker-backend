FROM huntress/postgres-partman:14
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    postgresql-14-postgis-3 \
    postgresql-14-postgis-3-scripts && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*