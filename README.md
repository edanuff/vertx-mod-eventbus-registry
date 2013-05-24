# EventBus Registry

This module keeps track of registered event bus addresses and provides a facility for checking to see if a specific address has been registered as well as query all the registered addresses and getting the results.  It also can also expire addresses after a certain amount of time.

The latest version of this module can be found in the [edanuff/vertx-mod-eventbus-registry](https://github.com/edanuff/vertx-mod-eventbus-registry). 

## Dependencies

This module is built against Vert.x 2.0.0.

## Name

The module name is `org.usergrid.vx~eventbus-registry~0.1-SNAPSHOT`.

## Usage

The registry keep a set of addresses and, for each address, the time the address was registered.  An expiration time can be provided which is the duration after which an address should no longer be considered active.

Addresses can be registered as often as you want.  Every time an address is registered, it's timestamp is set to the current time.  By doing this, you can prevent an address from expiring.  To make this easier, a ping message is published to `eventbus.registry.ping` and your Verticle can subscribe to this address and when it receives the message, re-register the address so that it doesn't expire.

Expired addresses can be periodically automatically purged.  This is useful if there are a large number of addresses being registered that are expected to be allowed to expire.  The default is to not sweep.

Addresses can be looked up by exact name, which is going to be the fastest way to determine if an address has been registered.  Addresses can also be searched by providing a regular expression to match against.  This will search through all registered addresses and return the matches.  Obviously, the execution time of a search will be related to how many addresses have been registered and that overhead should be taken into consideration.

## Configuration

The session manager module takes the following optional configuration:

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
* `expiration` The duration after which a registered address should be expired.  Set to 0 to prevent expiration.  The default value if this configuration isn't provided is 5000.
* `ping` How often to publish a message to `eventbus.registry.ping` to remind handlers to re-register.  Set to 0 to prevent this message from being sent.  The default value if this configuration isn't provided is 1000.
* `sweep` How often to clean out the list of registered addresses.  Set to 0 to prevent this from happening.  The default value if this configuration isn't provided is 0, so, by default, sweeping won't occur.

## Operations

### Register

Send a message to `eventbus.registry.register` with the address to register.  This will add the address to the registry.

### Get

Send a message to `eventbus.registry.get` with the address to check to see if it's been registered.  This message will return `true` or `false` to indicate whether the address exists and has not expired.

### Search

Send a message to `eventbus.registry.search` with a regex to get a set of matching registered addresses.  This message will return a JSON object with the addresses and the registration times.

    {
    	"my.first.address": 1369369623,
        "my.second.address": 1369369653
    }

