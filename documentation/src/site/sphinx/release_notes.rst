=============
Release Notes
=============

0.0.4
-----
* Feature     : Filter Contracts from being executed by ContractClient
* Improvement : Switched far jar interface to use Apache commons CLI

0.0.3
-----
* Feature     : Maven plugin for running ContractServer and ContractClient
* Feature     : Fat & Runnable jar for starting ContractServer and running ContractClient

0.0.2
-----
*

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