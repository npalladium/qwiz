#! /usr/bin/env bash

mkdir -p ./.temp/outputs ./.temp/uploads ./.temp/logs

(cd qwiz-backend && ./gradlew bootBuilImage) && docker compose up --force-recreate
