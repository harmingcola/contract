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

I'm using Maven with another programming language
-------------------------------------------------
Both our client and server are available as maven plugins

To run the server:

.. code-block:: bash

     mvn org.seekay:contract-maven-plugin:${release.version}:run-server -Dport=8091 -DgitSource=https://github.com/harmingcola/kvServerContracts.git -Dusername=seekay_test -Dpassword=seekay_test_password

To run the client

.. code-block:: bash

    mvn org.seekay:contract-maven-plugin:${release.version}:run-client -Dtarget=http://localhost:8091 -DgitSource=https://github.com/harmingcola/kvServerContracts.git -Dusername=seekay_test -Dpassword=seekay_test_password


I want to start a client / server from the command line
-------------------------------------------------------
We provide a fat and runnable jar that contains both the client and server.
This option servers practically all client / servers across a variety of languages and platforms.
The jar is available from `maven central <http://mvnrepository.com/artifact/org.seekay/contract-all>`_

To run a server:

.. code-block:: bash

    java -jar contract-all-${release.version}.jar run-server 8091 https://bitbucket.org/harmingcola/contract-test-private.git seekay_test seekay_test_password
    java -jar contract-all-${release.version}.jar run-server <port> <local or git repo URI> <git username, optional> <git password, optional>

To run a client:

.. code-block:: bash

    java -jar contract-all-${release.version}.jar run-client http://localhost:8091 https://bitbucket.org/harmingcola/contract-test-private.git seekay_test seekay_test_password
    java -jar contract-all-${release.version}.jar run-client <target url> <local or git repo URI> <git username, optional> <git password, optional>


Usernames / passwords can be omitted for publicly accessible repositories

