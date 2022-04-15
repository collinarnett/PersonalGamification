#! /usr/bin/env bash
if [! -f .env]
then
	export $(cat $(pwd)/config.env | xargs)
fi

clean() {
	rm -f $TESTDIR/*
}

# verify() {
# 	# Maybe create a failure text output in program.
# 	# TODO Parse output
# 	if [ $result == "success" ]
# 	then
# 		return 0
# 	else
# 		return 1
# 	fi
# }

pg () {
	docker run --rm -v $TESTDIR:/var/lib/pg $IMAGE $@
}


TESTDIR=$(pwd)/test/
USER="collinarnett"
IMAGE=$(nix build ".#dockerImage" && docker load < $(pwd)/result | grep -oP "$USER.*[^\n]")

## Task list

# List tasks - succeed
pg list

# There should be no tasks at this point

## Task add

echo "Task - Test 1"
# Create Task
pg add -n "hello" -e 12

echo "Task - Test 2"
# Create a task with missing description and date - succeed
pg add -n "hello" -e 12

echo "Task - Test 3"
# Create a task with a negative effort - fail
pg add -n "hello" -e -1

# Clean
clean

# Create task to start program
pg add -n "hello" -e 12

# Task list
pg list

# There should be one task in list

## Task modify

echo "Modify - Test 1"
# Modify - succeed
pg modify -i 0 -n "hello"

echo "Modify - Test 2"
# Modify a task not in list of tasks - fail
pg modify -i 1 -n "hello"

## Delete

echo "Delete - Test 1"
# Delete - succeed
pg delete -i 0

# Recreate task
pg add -n "hello" -e 3

echo "Delete - Test 2"
# Delete task - fail
pg delete -i 1

# Clean
clean
