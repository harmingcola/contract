Client facing example : Key Value Server
========================================

The Key Value server (kvServer) is a testing project we use to validate the server facing aspects of the Contract project.
It is treated as a project that is a user of our code and wants to use every possible feature.

Its contracts are held separately in the `kvContracts repository <https://github.com/harmingcola/kvContracts>`_ in order
to share them with kvClient project.

Running the server
------------------

The server can be started with the following command from the root directory

.. code-block:: bash

    mvn spring-boot:run


Endpoints
---------

The following urls are accessible on the server.


* GET
    * URL : http://{host}:{port}/kv/pair/{key}
    * Response : {"key": "name","value": "ann"}
    * Description : Returns the key value pair for that key

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


* POST
    * URL : http://{host}:{port}/kv/pair
    * Response : {"key": "age","value": 27}
    * Description : Accepts and stores a key/value pair

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
        "body" : "{\"key\": \"age\",\"value\": 27}"
      }
    }



Running the ContractClient
--------------------------

The server facing tests are wrapped in a standard junit test. The contracts are stored in our `kvContracts repository <https://github.com/harmingcola/kvContracts>`_

.. code-block:: java

    import org.junit.Test;
    import org.seekay.contract.client.client.ContractClient;

    public class KvServerContractTests {

        @Test
        public void runContractTestsAgainstServer() {

            ContractClient.newClient().againstPath("http://localhost:8080")
                    .withGitConfig("https://github.com/harmingcola/kvContracts.git")
                    .runTests();

        }
    }



