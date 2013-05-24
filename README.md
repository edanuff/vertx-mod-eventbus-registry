# EventBus Registry

This module keeps track of registered event bus addresses and provides a facilitiy for checking to see if a specific address has been registered as well as query all the registered addresses and getting the results.  It also can also expire addresses after a certain amount of time.

The latest version of this module can be found in the [edanuff/vertx-mod-eventbus-registry](https://github.com/edanuff/vertx-mod-eventbus-registry). 

## Dependencies

This module is built against Vert.x 2.0.0.

## Name

The module name is `org.usergrid.vx~eventbus-registry~0.1-SNAPSHOT`.

## Configuration

The session manager module takes the following configuration:

    {
        "expiration": <expiration time>,
        "ping": <ping time>,
        "sweep": <sweep time>
    }

For example:

    {
        "expiration": 5000,
        "ping": 1000,
        "sweep": 5000
    }        

A short description about each field:
* `expiration` The duration after which a registered address should be expired.  Set to 0 to prevent expiration.
* `ping` How often to publish a message to `eventbus.registry.ping` to remind handlers to re-register.  Set to 0 to prevent this message from being sent.
* `sweep` How often to clean out the list of registered addresses.  Set to 0 to prevent this from happening.

## Operations

### Register

Send a message to `eventbus.registry.register` with the address to register.  This will add the address to the registery.

### Get

Send a message to `eventbus.registry.get` with the address to check to see if it's been registered.  This message will return `true` or `false` to indicate whether the address exists and has not expired.

### Search

Send a message to `eventbus.registry.search` with a regex to get a set of matching registered addresses.  This message will return a JSON object with the addresses and the registration times.

    {
    	"my.first.address": 1369369623,
        "my.second.address": 1369369653
    }

