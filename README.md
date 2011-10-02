# Prerequisites

1. Requires an nginx mapping for appdata -> /home/sroy/appdata.  This is where all the derived assets will be stored.
2. Requires a Redis server installed and running with a password restriction matching the REDISTOGO url parameter above.
3. Requires Postgres database to be installed
4. Requires ruby and the foreman gem to be installed. (https://gist.github.com/1083861)

# Build

Build the project with (after every code change)

    $ mvn install

# Configure

You will need to set the `REPO` environment variable, so the execution wrapper script knows where to find the maven dependencies. For example:

    export REPO=$HOME/.m2/repository
    export DATABASE_URL='postgres://devdbuser:dbpass@localhost/devdb'
    export REDISTOGO_URL='redis://username:redispass@localhost:6379'
    export STORAGE_DIRECTORY='/home/sroy/appdata/picshare'
    export WEB_SERVER_APPDATA_ROOT='http://localhost/appdata'
    
# Run

Now you can run your webapp with:

    $ sh launch.sh


# Setting up a new postgres database

    sudo apt-get install postgresql

Then, follow the directions here :

    http://www.cyberciti.biz/faq/howto-add-postgresql-user-account/

# Generating schema

	mvn hibernate3:hbm2ddl