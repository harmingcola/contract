Matching
========

Matching is how we identify a contract from a request and how we validate a response against a contract.
All matching is performed in the order outlined below.
Our intent with matching is to always go from most specific to least specific.

Path
----
    * Exact String from contracts will match first.
    * Paths with out of order query parameters.
    * Paths containing expressions

Method
------
    * Method matching is performed as a simple .equals() on our Method enum.

Headers
-------
    * Contract headers will match if every header specified is contained in the request/response headers.

Body
----
    * A request/response body and contract body stripped of whitespace characters will match first.
    * A JSON body will match regardless of order.

Expressions
-----------
We support matching based on expressions. We currently support
    * ${contract.anyString}
        * Will match with any string in its place, regex equivalent : .*
        * Currently only supported in *request.path*

    * ${contract.timestamp}
        * Will match the current time in nano seconds. It will give 1 millisecond buffer.
        * Currently only supported in *response.body*