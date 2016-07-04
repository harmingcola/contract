Features
========
* `JSON DSL <http://harmingcola.github.io/contract/contract_files.html>`_ for specifying client / server interaction.
* DSL supports both json objects and text for specifying bodies.
* Share contracts via GIT or local storage.
* Auto and manual `tagging <http://harmingcola.github.io/contract/tagging.html>`_ of contracts.
* Filtering of contracts by tags
* Out of order JSON body `matching <http://harmingcola.github.io/contract/matching.html>`_.
* String, number and timestamp wildcard matching for paths, headers, and bodies
* Variable matching and substitution, see `expressions <http://harmingcola.github.io/contract/matching.html#expressions>`_ for more details
* Expand single contracts out to multiple using `parameters <http://harmingcola.github.io/contract/parameters.html>`_.

Server Features
---------------
* A test server to run client tests against.
* Hard coded server responses for POST, GET, PUT & DELETE requests.
* Specified headers will be expected & returned by the server.
* Support for current timestamp included in response bodies.
* Out of order query params will be recognised.
* Filters can be set on contracts on a running server to change behaviour for test cases.

Client Features
---------------
* A test client to verify server responses.
* Verification of responses to POST, GET, PUT & DELETE requests.
* Specified headers will be sent & expected by the client.
* Support for current timestamp in request bodies.
* Filtering of contracts to be executed based on tags.


