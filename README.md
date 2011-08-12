# Prerequisites

- Local Postgres database, with corresponding database and user account matching DATABASE_URL
- Local Redis server, used for message queueing.

# Build

Build the project with

    $ mvn install

# Configure

You will need to set the `REPO` environment variable, so the execution wrapper script knows where to find the maven dependencies. For example:

    export REPO=$HOME/.m2/repository
    export DATABASE_URL='postgres://devdbuser:dbpass@localhost/devdb'
    export REDISTOGO_URL='redis://username:redispass@localhost:6379'

# Run

Now you can run your webapp with:

    $ sh target/bin/webapp

(the wrapper script is not executable by default).


# Setting up a new postgres database

    sudo apt-get install postgresql

Then, follow the directions here :

    http://www.cyberciti.biz/faq/howto-add-postgresql-user-account/

