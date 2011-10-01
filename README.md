# Prerequisites

- Local Postgres database, with corresponding database and user account matching DATABASE_URL
- Local Redis server, used for message queueing.

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
    
# Environmental Dependencies

	1. Requires an nginx mapping for appdata -> /home/sroy/appdata.  This is where all the derived assets will be stored.
	2. Requires a Redis server installed and running with a password restriction matching the REDISTOGO url parameter above.
	3. Requires postgres to be installed
	4. Requires ruby and the foreman gem to be installed. (https://gist.github.com/1083861)

# Run

Now you can run your webapp with:

    $ sh target/bin/webapp

(the wrapper script is not executable by default).

...or if you are using foreman :

	$ foreman start -c imgworker=3


# Setting up a new postgres database

    sudo apt-get install postgresql

Then, follow the directions here :

    http://www.cyberciti.biz/faq/howto-add-postgresql-user-account/

# Generating schema

	mvn hibernate3:hbm2ddl