Client facing example : Key Value Client
========================================

The Key value client (kvClient) is a project we're using to validate the client facing aspects of the Contract project.
Its treated like a project that is a user of our code that wants to use every possible feature.

Its contracts are held seperately in the [kvContracts repository](https://github.com/harmingcola/kvContracts) in order
to share them with kvClient project.

Features
--------

Create
------
Exposes a method allowing creation of a key on the KvServer. The contract and unit test for this functionality is below.

.. code-block:: javascript

    {
      "request" : {
        "method" : "POST",
        "path" : "/pair",
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

.. code-block:: java

    import org.seekay.contract.server.ContractServer
    import spock.lang.Shared
    import spock.lang.Specification

    class CreatePairSpec extends Specification {

        @Shared
        ContractServer server;

        @Shared
        KvClient client;

        def setupSpec() {
            server = ContractServer.newServer()
                    .onRandomPort()
                    .withGitConfig('https://github.com/harmingcola/kvContracts')
                    .startServer()

            client = new KvClient(server.path() + '/kv')
        }

        def 'a pair should be created and returned on the server'() {
            given:
                Pair pair = new Pair( key:'name', value:'create key value pair, test 0001')
            when:
                Pair createdPair = client.create(pair)
            then:
                createdPair.key == 'age'
                createdPair.value == '27'

        }

    }


