=============
Release Notes
=============

0.0.5
-----
* Feature     : Support for `parameters <http://harmingcola.github.io/contract/parameters.html>`_ block, expands single contracts into multiple contracts
* Feature     : Support for expressions in the response.status field
* Bug fix     : Manually configuring native json on the server throws exceptions
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
* Improvement : Request & response bodies can now be written in json and escaped text.
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