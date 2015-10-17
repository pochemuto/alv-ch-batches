#!/bin/sh

export PGPASSWORD=alvch_db_password
cd $(dirname $0)
psql -U alvch_db_username -h localhost alvch_db_name < schema.sql
cd -

