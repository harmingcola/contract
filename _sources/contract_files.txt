==============
Contract Files
==============

Contract files model a HTTP request/response. The define the responsibilities of a client or server in any interaction.

* Contract files written in JSON must end in **.contract.json**

.. code-block:: javascript

    {
      "info" : {
        "details" : "The server should respond with a key value pair when a valid create request is received",
        "tags" : ["create", "example"]
      }
      "request" : {
        "method" : "POST",
        "path" : "/kv/pair",
        "headers": {
          "Content-Type" : "application/json"
        },
        "body" : {
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


Fields
------
    * info
        * details
            * Used to give a description of the behaviour of the contract file
        * tags
            * List of string tags referring to the contract.
        * *anything else*
            * *Any other information can be put in the info block as long as it is valid JSON*
    * setup
        * The setup block is a list of request response pairs allowing for multiple setup calls to be made before the contract is validated
    * request
        * method
            * HTTP method to be used for and responded to for requests
        * path
            * The path, from the root of the server, to send requests to and respond to requests on.
        * headers
            * Headers to be included in requests. These headers must be in included in requests for the test server to respond correctly. Any additional headers in the request will be ignored.
        * body
            * The text body expected for a request and sent by the ContractClient.
            * The body can either be specified as a json object representing the body or as a string containing json.
            * Any JSON in a string body must have double quotes escaped.
    * response
        * status
            * The HTTP status to be returned with the response
        * headers
            * Headers to be included in responses. These headers must be in included in responses for the test client to accept the server response as valid.
        * body
            * The text body returned for a request and expected by the ContractClient


Wildcards
---------
Wildcards are expressions that the server will fill with the appropriate data, and the client will recognise when given in a response.
For more detail on wildcards, see `Matching <http://harmingcola.github.io/contract/matching.html>`_

.. code-block:: javascript

    {
      "request" : {
        "method" : "GET",
        "path" : "/kv/service/${contract.anyString}"
      },
      "response" : {
        "status" : 200,
        "body" : {
          "key": "time",
          "value": "${contract.timestamp}"
        }
      }
    }


Info
----
The info will be auto-populated by certain data when your contracts are loaded.
    * filename : The name of the file the contract came from
    * tags     : A tag for every directory in the directory structure the contract was loaded from. Tags can also be added manually, they will be added to the auto-populated tags.



Parameters
----------
The parameters block allows for a single contract to be expanded into multiple. The example below shows an example from our kvServer project.
This contract specifies a list of 4 maps of key value pairs. We will search for each key in the contract and replace it with the specified value.
A key with the name *age* will match ${contract.parameter.age}.
Any values can be used in the parameters block, `expressions <http://harmingcola.github.io/contract/matching.html#expressions>`_ can also be used.

.. code-block:: javascript

    {
      "info" : {
        "details": "The server should accept and store multiple create key/value requests and respond correctly"
      },
      "parameters" : [
        {
          "first" : "iron",
          "last": "man"
        },
        {
          "first" : "captain",
          "last": "america"
        },
        {
          "first" : "incredible",
          "last": "hulk"
        },
        {
          "first" : "black",
          "last": "widow"
        }
      ],
      "request" : {
        "method" : "POST",
        "path" : "/kv/pair",
        "headers": {
          "content-type" : "application/json"
        },
        "body": {
          "key": "${contract.parameter.first}",
          "value": "${contract.parameter.last}"
        }
      },
      "response" : {
        "status" : 201,
        "body" : {
          "key": "${contract.parameter.first}",
          "value": "${contract.parameter.last}"
        }
      }
    }