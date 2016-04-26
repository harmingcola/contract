Client facing example : Key Value Client
========================================

The Key value client (kvClient) is a project we're using to validate the client facing aspects of the Contract project.
Its treated like a project that is a user of our code that wants to use every possible feature.

Its contracts are held separately in the `kvServerContracts repository <https://github.com/harmingcola/kvServerContracts>`_ in order
to share them with kvClient project.

Maven dependency
----------------

To test the client, we require our contract server.

.. code-block:: xml

    <dependency>
        <groupId>org.seekay</groupId>
        <artifactId>contract-server</artifactId>
        <version>${release.version}</version>
        <scope>test</scope>
    </dependency>


Features
--------

The code to setup a server from a git source is identical for each test case.

.. code-block::

    import org.seekay.contract.server.ContractServer
    import org.seekay.kv.client.KvClient
    import spock.lang.Shared
    import spock.lang.Specification

    class ClientBaseSpec extends Specification {

        @Shared ContractServer server;
        @Shared KvClient client;
        @Shared Session session

        def setupSpec() {
            server = ContractServer.newServer()
                    .onRandomPort()
                    .withGitConfig('https://github.com/harmingcola/kvServerContracts')
                    .startServer()
            session.setContractServer(server)
        }
    }


Create
------
Exposes a method allowing creation of a key on the KvServer. The contract and unit test for this functionality is below.

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
        "body" : {
            "key": "age",
            "value": 27
        }
      }
    }

.. code-block::

    import org.seekay.kv.client.model.Pair
    import org.seekay.kv.client.util.ClientBaseSpec

    class CreatePairSpec extends ClientBaseSpec {

        def 'a pair should be created and returned on the server'() {
            given:
                Pair pair = new Pair( key:'name', value:'create key value pair, test 0001')
            when:
                Pair createdPair = client.create(pair)
            then:
                createdPair.key == 'name'
                createdPair.value == 'create key value pair, test 0001'
        }
    }


Read
----
Exposes a method allowing the reading of a key from the KvServer. The contract and unit test for this functionality is below.

.. code-block:: javascript

    {
      "request" : {
        "method" : "GET",
        "path" : "/kv/pair/weight"
      },
      "response" : {
        "status" : 200,
        "body" : "{\"key\": \"weight\",\"value\": \"220\"}"
      }
    }

.. code-block::

    import org.seekay.kv.client.model.Pair
    import org.seekay.kv.client.util.ClientBaseSpec

    class ReadPairSpec extends ClientBaseSpec {

        def 'a pair should be created and returned on the server'() {
            when:
                Pair createdPair = client.read('weight')
            then:
                createdPair.key == 'weight'
                createdPair.value == '220'
        }
    }



Update
------
Exposes a method allowing updating of a key on the KvServer. The contract and unit test for this functionality is below.

.. code-block:: javascript

    {
      "request" : {
        "method" : "PUT",
        "path" : "/kv/pair",
        "headers": {
          "Content-Type" : "application/json"
        },
        "body": "{\"key\": \"age\",\"value\": 27}"
      },
      "response" : {
        "status" : 201,
        "body" : {
            "key": "age",
            "value": 27
        }
      }
    }

.. code-block::

    import org.seekay.kv.client.model.Pair
    import org.seekay.kv.client.util.ClientBaseSpec

    class UpdatePairSpec extends ClientBaseSpec {

        def 'a pair should be updatable on the server'() {
            given:
                Pair pair = new Pair( key:'height', value:'180')
            when:
                Pair updatedPair = client.update(pair)
            then:
                updatedPair.key == 'height'
                updatedPair.value == '180'
        }
    }


Delete
------
Exposes a method allowing the deleting of a key from the KvServer. The contract and unit test for this functionality is below.

.. code-block:: javascript

    {
      "request" : {
        "method" : "DELETE",
        "path" : "/kv/pair/blood-pressure"
      },
      "response" : {
        "status" : 204
      }
    }

.. code-block::

    import org.seekay.kv.client.util.ClientBaseSpec

    class DeletePairSpec extends ClientBaseSpec {

        def 'a pair should be delete-able from the server'() {
            expect:
                kvClient.delete('blood-pressure') == true
        }
    }