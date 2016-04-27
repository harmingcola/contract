===========
Quick Start
===========

I want to test a client using Java and Maven
--------------------------------------------

Include our contract-server dependency in your project.

.. code-block:: xml

    <dependency>
        <groupId>org.seekay</groupId>
        <artifactId>contract-server</artifactId>
        <version>${release.version}</version>
        <scope>test</scope>
    </dependency>

See the `client facing example <http://harmingcola.github.io/contract/kv_client.html>`_ for contracts and code samples.

I want to test a server using Java and Maven
--------------------------------------------

Include our contract-client dependency in your project.

.. code-block:: xml

    <dependency>
        <groupId>org.seekay</groupId>
        <artifactId>contract-client</artifactId>
        <version>${release.version}</version>
        <scope>test</scope>
    </dependency>

See the `server facing example <http://harmingcola.github.io/contract/kv_server.html>`_ for contracts and code samples.

