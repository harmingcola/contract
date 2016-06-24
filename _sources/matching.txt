Matching
========

Matching is how we identify a contract from a request and how we validate a response against a contract.
All matching is performed in the order outlined below.
Our intent with matching is to always go from most specific to least specific and to follow postel's law where
what is specified in a contract is what is required, if additional detail is included it will be ignored. This allows
clients and servers to develop independently.

Path
----
    * Exact String from contracts will match first.
    * Paths with out of order query parameters.
    * Paths with out of order query parameters and using expressions.
    * Paths containing expressions

Path examples

.. code-block:: bash

    # Exact path
    "path" : "/example/1"

    # Out of order query parameters
    "path" : "/example?key=value&result=success"

    # Out of order query parameters
    "path" : "/example?key=%{contract.anyString}&count=${contract.anyNumber}"

    # Expression path match
    "path" : "/example/${contract.anyString}"

Headers
-------
    * Contract headers will match if every header specified is contained in the request/response headers.
    * Headers must either be an exact match, or a match for an evaluated expression

.. code-block:: bash

    # Exact match
    "headers" : {
        "client-name" : "bob",
        "count" : "5"
    }

    # Expression match
    "headers" : {
        "client-name" : "${contract.anyString}",
        "count" : "${contract.anyNumber}"
    }


Body
----
    * A request/response body treated as text, stripped of whitespace characters will match first.
    * A request/response body treated as text with expressions evaluated will match next.
    * A request/response body treated as JSON will match regardless of order.
    * A request/response body treated as JSON with expressions evaluated will match last.

.. code-block:: bash

    # Exact or Json body match
    "body" : {
        "name" : "bob",
        "age" : "25"
    }

    # Text and Json Expression body match
    "body" : {
        "name" : "${contract.anyString}",
        "age" : "${contract.anyNumber}"
    }

Method
------
    * Method matching is performed as a simple .equals() on our Method enum.

Expressions
-----------
We support matching based on expressions, they can be put in the path, headers, or body of a contract. We currently support
    * ${contract.anyString}
        * Will match with any string in its place, regex equivalent : .*
    * ${contract.anyNumber}
        * Will match with any number in its place, regex equivalent : -?[0-9]+(\\.[0-9]+)?
        * Currently only operates inside a string
    * ${contract.timestamp}
        * Will match the current time. It will give 10 milliseconds of leeway when matching the value.
    * ${contract.var.number.**variableName**}
        * Will match any JSON number
        * Will be replaced with a random integer for testing
    * ${contract.var.positiveNumber.**variableName**}
        * Will match any positve JSON number
        * Will be replaced with a random positive integer for testing
        * Should be used to represent generated IDs in a system backed by a database.
    * ${contract.var.string.**variableName**}
        * Will match any JSON string
        * Will be replaced with a random word for testing
Expressions can be contained within other strings, they are not exclusive to a field. for example:

.. code-block:: bash

    "my name is ${contract.anyString}

Will match with "my name is bob"