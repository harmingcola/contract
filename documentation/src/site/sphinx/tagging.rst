Tagging and Filtering
=====================

Contracts are auto tagged with the name of every directory they are in relative to the directory the contracts are loaded from.
For example, contracts loaded from *webclient/v0.0.1/login/* will be tagged with 'webclient', 'v0.0.1' and 'login'.

Tags can also be added manually to the contract file, a sample is shown below :

.. code-block:: javascript

    {
      "tags" : ["admin", "core"],
      "request" : {
        "method" : "POST",
        "path" : "/kv/pair",
        "headers": {
          "Content-Type" : "application/json"
        },
        "body": "{
          "key": "age",
          "value": 27
        }
      },
      "response" : {
        "status" : 201,
        "body" : {
          "key": "age",
          "value": 27
        }
      }
    }

This contract will also be tagged with 'admin' and 'core.

Filtering
---------

The ContractClient and ContractServer can either retain or exclude certain contracts based on their tags, the syntax is identical for both.
For example to execute the Contracts tagged with 'core'

.. code-block::

    ContractClient.fromContracts(contracts)
        .retainTags("core")
        .runTests()

Multiple tags can also be passed as arguments

.. code-block::

    ContractClient.fromContracts(contracts)
        .retainTags("core", "webclient", "login")
        .runTests()

And contracts with any of these tags will be executed. The Opposite is also possible.

.. code-block::

    ContractClient.fromContracts(contracts)
        .excludeTags("core")
        .runTests()

This will exclude every contract with the tag 'core'. Retain and exclude can be used in conjunction with one another.
However, their ordering will need to be taken into account by the user. This filtering prevents contracts from being
loaded by the client or server. They will not be loaded until the configuration is changed and the client or server
restarted. It is completely separate from the in running filtering explained below.


Filtering on a running server
-----------------------------

Once a server is up and running, we may want to filter what contracts are active in order to customize behaviour to the
test case currently running. This can be done 2 ways :

1. Sending requests to the server
* Send a post request to the '/__filter' endpoint of the server. The payload should be a JSON list of strings, contracts containing any of the filter parameters will remain active.
* To clear the filters, send a delete request to the '/__filter' endpoint.

2. Use the ContractServer object
* Use the 'setFilters' to set an array of strings to filter on.
* To clear the filters, call the 'clearFilters' method.