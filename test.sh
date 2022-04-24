#! /usr/bin/env bash
RED='\033[0;31m'
GREEN='\033[0;32m'
NOCOLOR='\033[0m'
clean() {
	rm -f $TESTDIR/*.yaml
}
trap "rm -f $TESTDIR/*.yaml" EXIT

assert() {
	RESULT=$?
	if [ "$1" = "fail" ] && [ $RESULT -eq 1 ]
	then
		echo -e "${GREEN}Test passed - expected $1, got $RESULT${NOCOLOR}"
		return 0
	fi

	if [ "$1" = "succeed" ] && [ $RESULT -eq 0 ]
	then
		echo -e "${GREEN}Test passed - expected $1, got $RESULT${NOCOLOR}"
		return 0
	else

		echo -e "${RED}Test failed - expected $1, got $RESULT${NOCOLOR}"
		exit 1
	fi
}

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
assert "succeed"

echo "Task - Test 2"
# Create a task with missing description and date - succeed
pg add -n "hello" -e 12
assert "succeed"

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
assert "succeed"
echo "Modify - Test 2"
# Modify a task not in list of tasks - fail
pg modify -i 1 -n "hello"
assert "fail"
## Delete

echo "Delete - Test 1"
# Delete - succeed
pg delete -i 0
assert "succeed"
# Recreate task
pg add -n "hello" -e 3

echo "Delete - Test 2"
# Delete task - fail
pg delete -i 1
assert "fail"

# Player Add
echo "Player - Test 1"
pg player -n "collin"
assert "succeed"

# Task complete
echo "Task - Test 3"
pg add -n "hello" -e 12 -D "2022-05-01"
pg complete -i 0
assert "succeed"
