==============
Contract Files
==============

Contract files model a HTTP request/response. The define the responsibilities of a client or server in any interaction.

* Contract files written in JSON must end in **.contract.json**

.. code-block:: javascript

    {
      "request" : {
        "method" : "POST",
        "path" : "/kv/pair",
        "headers": {
          "Content-Type" : "application/json"
        },
        "body": "{\"key\": \"age\",\"value\": 27}"
      },
      "response" : {
        "status" : 201,
        "body" : "{\"key\": \"age\",\"value\": 27}"
      }
    }


Fields
    * request
        * method
            * HTTP method to be used for and responded to for requests
        * path
            * The path, from the root of the server, to send requests to and respond to requests on.
        * headers
            * Headers to be included in requests. These headers must be in included in requests for the test server to respond correctly. Any additional headers in the request will be ignored.
        * body
            * The text body expected for a request and sent by the ContractClient. Any JSON in the body must have double quotes escaped.
    * response
        * status
            * The HTTP status to be returned with the response
        * body
            * The text body returned for a request and expected by the ContractClient

