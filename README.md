# alv-ch-batches

[![Build Status](https://travis-ci.org/alv-ch/alv-ch-batches.svg?branch=master)](https://travis-ci.org/alv-ch/alv-ch-batches)
[![Coverage Status](https://codecov.io/github/alv-ch/alv-ch-batches/coverage.svg?branch=master)](https://codecov.io/github/alv-ch/alv-ch-batches?branch=master)
[![Buildtime trend](https://buildtimetrend.herokuapp.com/badge/alv-ch/alv-ch-batches/latest)](https://buildtimetrend.herokuapp.com/dashboard/alv-ch/alv-ch-batches/)

## Developer Guide

### Build Stack

- Java 8+
- PostgreSQL 9.4+
- Elasticsearch 1.7

See Maven dependencies for deeper details.

### Development Environment Setup

As `master` database, a PostgreSQL database is required in order to build this project, and you can easily get it up and ready with one of the following ways:

With **Vagrant**:

```shell
cd test/vagrant
vagrant up master_db
```

Note: the proposed Vagrant basebox supports `virtualbox` and `vmware` (desktop) machine providers.

With **Docker Compose**:

```shell
cd test/docker
docker-compose up -d master_db
```

Once you have a running PostgreSQL database, you must load the data model. This is currently handled by the following script:

```shell
test/sql/master/create-master-db-schema.sh
```

Note: This script will drop the whole 'public' schema, if it already exists.

