#!/bin/sh

cd $TRAVIS_BUILD_DIR/test/sql/master
psql -U $TEST_DB_USERNAME -c "create database $TEST_DB_NAME;"
psql -U $TEST_DB_USERNAME $TEST_DB_NAME < schema.sql
cd $TRAVIS_BUILD_DIR

