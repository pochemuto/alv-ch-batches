#!/bin/sh

cd $TRAVIS_BUILD_DIR/test/sql/legacy
psql -U $TEST_DB_USERNAME -c "create database web1;"
psql -U $TEST_DB_USERNAME web1 < oste.sql
gunzip -c oste.txt.gz | psql -U $TEST_DB_USERNAME web1 -c "copy oste_x28 from STDIN"
cd $TRAVIS_BUILD_DIR

