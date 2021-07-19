#! /usr/bin/env bash

(cd qwiz-backend && ./gradlew bootBuilImage) && docker compose up
