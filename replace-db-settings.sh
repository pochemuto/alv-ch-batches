#!/bin/bash

TEST_DB_NAME=${TEST_DB_NAME:-alvch_master}
TEST_DB_USERNAME=${TEST_DB_USERNAME:-alvch_master}
TEST_DB_PASSWORD=${TEST_DB_PASSWORD:-} # empty password by default (handy for travis-ci)

find ./[A-z]* \( -name '*.properties' -o -name '*.yml' \) -type f -exec sed -i -e "s/alvch_db_name/$TEST_DB_NAME/g" {} \;
find ./[A-z]* \( -name '*.properties' -o -name '*.yml' \) -type f -exec sed -i -e "s/alvch_db_username/$TEST_DB_USERNAME/g" {} \;
find ./[A-z]* \( -name '*.properties' -o -name '*.yml' \) -type f -exec sed -i -e "s/alvch_db_password/$TEST_DB_PASSWORD/g" {} \;

