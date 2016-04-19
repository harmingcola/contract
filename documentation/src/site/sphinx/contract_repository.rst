===================
Contract Repository
===================

Contracts are at their most useful when readily available to all parties.
We support loading of contract files from a local directory, but our recommendation is to place them in a git repository.
It allows a service to easily publish their contracts to clients, and for clients to contribute their specific contracts back to service.

Structuring the repository
--------------------------
The contract client and server runners will happily take a directory and run every contract specified.
However to get the most out of Contract, we recommend following this folder structure.

* one service per repository
    * one directory per client
        * one directory per supported version
            * directories divided by feature (*can have more than one*)

*kvClient/v0.0.1/hard-coded/post/create-key-value-pair.contract.json*

See our kvServerContracts repository as an example.