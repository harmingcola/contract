Key Value Server
================

The Key Value server (kvServer) is a testing project we use to validate the server facing aspects of the Contract project.
It is treated as a project that is a user of our code and wants to use every possible feature.

Its contracts are held seperately in the [kvContracts repository](https://github.com/harmingcola/kvContracts) in order
to share them with kvClient project.

Urls
----

The following urls are accessible on the server.


* GET
    * URL : http://{host}:{port}/kv/pair/{key}
    * Response : {"key": "name","value": "ann"}
    * Description : Returns the key value pair for that key

* POST
    * URL : http://{host}:{port}/kv/pair
    * Response : {"key": "age","value": 27}
    * Description : Accepts and stores a key/value pair











