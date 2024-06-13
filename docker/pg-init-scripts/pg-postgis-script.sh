psql -U postgres << EOF
\c incidents_location;

CREATE EXTENSION postgis;

EOF