#!/usr/bin/env bash

export COMPOSE_DOCKER_CLI_BUILD=1
export DOCKER_BUILDKIT=1

docker compose up -f ./docker-compose.yml -d