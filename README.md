# PersonalGamification


A fun CLI task application that focuses on gamified task completion.

## Table of contents

- [Rational](#rational)
- [Features](#features)
- [Installation](#installation)
- [Credits](#credits)

## Rational

[Taskwarrior](https://taskwarrior.org/) is currently the most powerful command line based task application; however it lacks any form of extrinsic motivation. There are currently no task applications that incorporate gamified elements while maintaining the power and extensibility of taskwarrior. PersonalGamification aims to be a lightweight, extensible and powerful task application with a specialization in gamification.

## Features

- Fully featured task management system, including the ability to add, delete, complete and modify tasks.
- Events system that can be supported through user generated YAML assets such as monsters, items and status affect.
- Outcomes that are affected by whether or not users complete tasks by their given due date.
- DnD like character sheet that persists as long as you use the program.

## Installation

Currently the only available method of installing PG is through docker.

### Docker

You can find the docker image available on [Docker Hub](https://hub.docker.com/repository/docker/collinarnett/personalgamification).

It's recommended to create an alias for a convenient usage:

```bash
alias pg="docker run --rm \
    -v ${PWD}/pg_tasks:/var/lib/pg \
    collinarnett/${LATEST_TAG}"
```

You should put this e.g. into your `.bash_aliases`. Also keep in mind to replace the path definitions to something that fits to your needs (e.g. replace `… -v ${PWD}/pg_tasks:/var/lib/pg …` with `… -v ${HOME}/Tasks:/var/lib/pg \ …`).

Essentially, what this does is to map the ${PWD}/pg_tasks (or whatever other directory you specified) on your host system to locations inside the Docker container where the files can be written and read. These directories need to exist on your system before you can run the container:

`mkdir -p "${PWD}/pg_tasks"`

You will also need to fetch the latest tag. Currently we do not have a method to generate the latest tag automatically but you can find the most recent docker image on the top of the [tags page](https://hub.docker.com/repository/docker/collinarnett/personalgamification/tags?page=1&ordering=last_updated) on Docker Hub.

Eg.
```
collinarnett/z04ljdv53338bzxxvkjjfwxhjcbvmfx7
```

If you wish to supply your own assets or copy those available from this repository, copy the contents of the `data` directory and place them in the mounted folder.


Finally, you can test the correct installation as such:

```
pg --help
```

## Credits

- The docker installation section was adapted from the installation section on [whipper-team/whipper](https://github.com/whipper-team/whipper)
