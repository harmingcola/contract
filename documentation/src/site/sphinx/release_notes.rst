=============
Release Notes
=============

0.0.2
-----
* Feature     : Support for ${contract.timestamp} in response bodies
* Feature     : Added info block to contracts for documentation & reporting purposes.
* Improvement : Request & response bodies can now be written in json and escaped text.
* Bug fix     : When a contract doesn't load correctly, no error is thrown.

0.0.1
-----
* Feature     : JSON DSL for contract specification
* Feature     : Support for loading contracts from local source
* Feature     : Support for loading contracts from git
* Feature     : Embedded tomcat for client facing tests
* Feature     : Client test runner for server facing tests
* Feature     : Hard coded body support for GET, PUT, POST & DELETE Requests