========
Contract
========

A jvm contract based testing tool for HTTP clients and servers.
The aim of this library is to simplify the development and testing of systems that communicate via HTTP.

Our approach favors neither side of the client server relationship, removing the *consumer driven* from *consumer
driven contracts* and you're left with just contracts. By favouring neither side, we're focusing on the interaction.

The project is available from maven central, see the `quick start <http://harmingcola.github.io/contract/quick_start.html>`_ page for more details
Check out the code on `github <https://github.com/harmingcola/contract>`_

The latest released version is ${release.version}


The problem of testing a client server relationship
---------------------------------------------------

Late stage integration testing of a HTTP client/server system can be a painful process.
Minor mis-communication of specification between client & server can cause systems to be incompatible.

.. figure :: images/intro-001.png
    :alt: A simple client server relationship

    A client server system in late stage integration testing, communicating via http

However, most testing approaches will indicate that both systems are operating correctly.
Its only when the two systems are actually communicating can problems arise. We need to fail earlier ......

Testing during development relies on replacing the other side of the HTTP relationship with what we "think" will be in place when both systems are in place.
This is a disconnected endeavor and relies on what we "think" to be updated manually with what our companion system requires.

.. figure :: images/intro-002.png
    :alt: Standard testing scenario, mocks in place

    A client server system in development, systems are disconnected. A mock server and test client take the place of actual services.

Contract is an open source testing tool aimed at replacing a companion system in a HTTP client or server relationship so that shared specifications can be used to unit, integration or acceptance level test to ensure that both systems are on the same page.
Server facing tests can be driven using a contract that specifies how the server is to react given a HTTP request.
Client facing tests can be driven by an identical contract specifying that when a client makes a specific request, they should receive a specific response.
With both sides of a client server relationship available for testing at a significantly earlier stage, we can improve the feedback of our testing cycles, and hopefully deliver value that little bit quicker.

.. figure :: images/intro-003.png
    :alt: Client & server testing against contract

    Client / server testing using contracts, the Contract Client and Contract Server are configured using the same
    contract files.

The rise of Micro-services
--------------------------
Micro-services make our existing problem significantly worse. We now have more than 2 systems communicating.
It becomes exceedingly more difficult to track down the perpetrator.
By using contracts to specify the behaviour of client/server pairs in a micro-services environment, it becomes immediately apparent which system is misbehaving.

Providing easy access to test servers for client development
------------------------------------------------------------
In order for clients to be developed, having an available server is of immense benefit.
Often a service provider will provide a test server and documentation to allow clients to be developed.
However, all a service provider need do is publish their contracts, and any client can run a ContractServer based on the service providers specifications.


.. toctree::
    :maxdepth: 1

    quick_start
    features
    contract_files
    kv_server
    kv_client
    contract_repository
    tagging
    building
    contributing
    support
    release_notes


Indices and tables
==================

* :ref:`genindex`
* :ref:`modindex`
* :ref:`search`

