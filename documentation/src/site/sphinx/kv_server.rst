================
Key Value Server
================

The Key Value server (kvServer) is a testing project we use to validate the Contract project. It is treated as
a project that is a user of our code and wants to use every possible feature.

Its contracts are held seperately in the [kvContracts repository](https://github.com/harmingcola/kvContracts) in order
to share them with kvClient project.

----
Urls
----

The following urls are accessible on the server.

GET http://{host}:{port}/kv/pair/{key}
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Returns the key value pair for that key

    {"key": "name","value": "create key value pair, test 0001"}


GET http://{host}:{port}/kv/pair
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
