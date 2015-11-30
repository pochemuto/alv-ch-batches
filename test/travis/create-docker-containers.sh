#!/bin/bash

TOTAL=2
WAIT_STEP=10
WAIT_MAX=60

wait_total=0
nb_containers=0

cd $(dirname $0)/../docker/

#echo "Docker Compose Up (without Legacy DB)!"
docker-compose up -d master_db jobdesk_es
docker-compose up -d legacy_db

until [ $nb_containers -eq $TOTAL ]
do
  sleep $WAIT_STEP;
  wait_total=$(($wait_total + $WAIT_STEP))
  nb_containers=$(docker-compose ps -q | wc -l)
  # trim white spaces
  nb_containers="$(echo -e "${nb_containers}" | tr -d '[[:space:]]')"
  echo "Waiting for the $TOTAL containers to be up and ready... (started: $nb_containers in $wait_total seconds)"
done

echo "Docker Compose looks Ready:"
docker-compose ps

cd -

