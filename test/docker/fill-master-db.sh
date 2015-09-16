#!/bin/sh

cd $(dirname $0)/../sql/master
psql -U alvch_db_username -h localhost alvch_db_name < schema.sql
cd -
