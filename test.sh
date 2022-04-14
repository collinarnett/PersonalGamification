#! /usr/bin/env bash

TESTDIR=$(pwd)/test/
USER="collinarnett"
IMAGE=$(nix build ".#dockerImage" && docker load < $(pwd)/result | grep -oP "$USER.*[^\n]")
docker run --rm -v $TESTDIR:/var/lib/pg $IMAGE $@
