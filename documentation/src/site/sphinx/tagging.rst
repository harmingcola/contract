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

The ContractClient can either onlyInclude or exclude certain contracts based on their tags.
For example to execute the Contracts tagged with 'core'

.. code-block::

    ContractClient.fromContracts(contracts)
        .onlyIncludeTags("core")
        .runTests()

Multiple tags can also be passed as arguments

.. code-block::

    ContractClient.fromContracts(contracts)
        .onlyIncludeTags("core", "webclient", "login")
        .runTests()

And contracts with any of these tags will be executed. The Opposite is also possible.

.. code-block::

    ContractClient.fromContracts(contracts)
        .excludeTags("core")
        .runTests()

This will exclude every contract with the tag 'core'. Include and exclude can be used in conjunction with one another.
However, their ordering will need to be taken into account by the user.