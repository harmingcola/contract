=============
Release Notes
=============

0.0.8
-----
* Feature     : Contracts can update the filters on the server
* Feature     : ContractServer can now be stopped via function call
* Improvement : Git checkout code will now reuse existing checkouts and not clone every time

0.0.7
-----
* Bug fix     : Expression variables weren't being extracted correctly from complex JSON bodies
* Feature     : Added ability to enable/disable tags on contract server to change behaviour during test run
* Feature     : Added 'start-server' goal to maven plugin. Allow server to remain active during test runs.
* Improvement : Upped our internal tomcat to 8.5.3

0.0.6
-----
* Feature     : Added setup block to contract definition, allows for multiple setup calls to be made by a client before verifying the actual contract
* Feature     : Support for variable names in expressions, variables can be stored for later use in a contract

0.0.5
-----
* Feature     : Support for `parameters <http://harmingcola.github.io/contract/parameters.html>`_ block, expands single contracts into multiple contracts
* Feature     : Support for expressions in the response.status field
* Bug fix     : Manually configuring native JSON on the server throws exceptions
* Bug fix     : No logging when server was started from command line
* Bug fix     : Tomcat error handling

0.0.4
-----
* Feature     : Contract filtering by tag for ContractClient and ContractServer
* Improvement : Switched far jar interface to use Apache commons CLI
* Improvement : The start server call will now block until the server finishes starting
* Improvement : Removed the 404 "No contracts found" error message
* Feature     : ${contract.anyString} expression support for all string fields
* Feature     : ${contract.timestamp} expression supported in all string fields
* Feature     : ${contract.anyNumber} expression support for all string fields

0.0.3
-----
* Feature     : Maven plugin for running ContractServer and ContractClient
* Feature     : Fat & Runnable jar for starting ContractServer and running ContractClient

0.0.2
-----
* Feature     : Support for ${contract.timestamp} in response bodies
* Feature     : Contracts support an info block for documentation and reporting purposes
* Feature     : Auto tagging of contracts based on directory structure.
* Improvement : Request & response bodies can now be written in JSON and escaped text.
* Bug fix     : When a contract doesn't load correctly, no error is thrown.
* Feature     : Matching support for paths with query parameters.

0.0.1
-----
* Feature     : JSON DSL for contract specification
* Feature     : Support for loading contracts from local source
* Feature     : Support for loading contracts from git
* Feature     : Embedded tomcat for client facing tests
* Feature     : Client test runner for server facing tests
* Feature     : Hard coded body support for GET, PUT, POST & DELETE Requests