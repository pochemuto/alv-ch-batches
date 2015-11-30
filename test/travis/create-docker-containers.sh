#!/bin/bash

TOTAL=3
WAIT_STEP=10
WAIT_MAX=60

wait_total=0
nb_containers=0

cd $(dirname $0)/../docker/

echo "Docker versions"
docker --version
docker-compose --version

echo "Docker Login to pull private images"
docker login -e="$DOCKER_EMAIL" -u="$DOCKER_USERNAME" -p="$DOCKER_PASSWORD" $DOCKER_PRIVATE_REPO

echo "Docker Compose Up!"
docker-compose up -d master_db jobdesk_es
docker-compose up -d legacy_db

until [ $nb_containers -eq $TOTAL ] || [ $wait_total -gt $WAIT_MAX ]
do
  sleep $WAIT_STEP;
  wait_total=$(($wait_total + $WAIT_STEP))
  nb_containers=$(docker-compose ps -q | wc -l)
  # trim white spaces
  nb_containers="$(echo -e "${nb_containers}" | tr -d '[[:space:]]')"
  echo "Waiting for the $TOTAL containers to be up and ready... (started: $nb_containers in $wait_total seconds)"
done

echo "Docker Compose status:"
docker-compose ps

cd -

# TODO return a Basic result status (OK if nb containers is as expected...)
