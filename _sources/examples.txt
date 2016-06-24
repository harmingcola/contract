Contract Examples
=================

GET an entity, verify status code and text body
-----------------------------------------------

.. code-block:: javascript

    {
      "request" : {
        "method" : "GET",
        "path" : "/example/url"
      },
      "response" : {
        "status" : 200,
        "body" : "Valid example response"
      }
    }

GET an entity, verify status code and JSON body
-----------------------------------------------

.. code-block:: javascript

    {
      "request" : {
        "method" : "GET",
        "path" : "/example/url"
      },
      "response" : {
        "status" : 200,
        "body" : {
            "text": "Valid example response"
        }
      }
    }