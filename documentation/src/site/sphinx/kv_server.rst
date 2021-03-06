Server facing example : Key Value Server
========================================

The Key Value server (kvServer) is a testing project we use to validate the server facing aspects of the Contract project.
It is treated as a project that is a user of our code and wants to use every possible feature.

Its contracts are held separately in the `kvServerContracts repository <https://github.com/harmingcola/kvServerContracts>`_ in order
to share them with kvClient project.

Our test examples are written in Spock. Our methods are test framework agnostic.

Maven dependency
----------------

To test the server, we require our contract client.

.. code-block:: xml

    <dependency>
        <groupId>org.seekay</groupId>
        <artifactId>contract-client</artifactId>
        <version>${release.version}</version>
        <scope>test</scope>
    </dependency>


Running the server
------------------

The server can be started with the following command from the root directory

.. code-block:: bash

    mvn spring-boot:run


Endpoints
---------

The following urls are accessible on the server.


* POST
    * URL : *http://{host}:{port}/kv/pair*
    * Description : Accepts and stores a key/value pair
    * Request : A json body representing a key-value pair
        * {"key": "age","value": 27}
    * Response : A json body matching the request body
        * {"key": "age","value": 27}


.. code-block:: javascript

    {
      "request" : {
        "method" : "POST",
        "path" : "/kv/pair",
        "headers": {
          "Content-Type" : "application/json"
        },
        "body": {
          "key": "age",
          "value": 27
        }
      },
      "response" : {
        "status" : 201,
        "body" : {
          "key": "age",
          "value": 27
        }
      }
    }


* GET
    * URL : *http://{host}:{port}/kv/pair/{key}*
    * Description : Returns the key value pair for that key
    * Response : A json body representing a key-value pair
        * {"key": "weight","value": "220"}


.. code-block:: javascript

    {
      "request" : {
        "method" : "GET",
        "path" : "/kv/pair/weight"
      },
      "response" : {
        "status" : 200,
        "body" : {
          "key": "weight",
          "value": "220"
        }
      }
    }


* PUT
    * URL : *http://{host}:{port}/kv/pair*
    * Description : Updates an existing a key/value pair
    * Request : A json body representing a key-value pair
        * {"key": "age","value": 27}
    * Response : A json body matching the request body
        * {"key": "age","value": 27}
    * Will respond with a 404 if they key to be updated doesnt exist.


.. code-block:: javascript

    {
      "request" : {
        "method" : "PUT",
        "path" : "/kv/pair",
        "headers": {
          "Content-Type" : "application/json"
        },
        "body": {
          "key": "age",
          "value": 27
        }
      },
      "response" : {
        "status" : 201,
        "body" : {
          "key": "age",
          "value": 27
        }
      }
    }


* DELETE
    * URL : *http://{host}:{port}/kv/pair/{key}*
    * Description : Deletes an existing a key/value pair
    * Will respond with a 404 if they key to be updated doesnt exist.


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


Running the ContractClient
--------------------------

The server facing tests are wrapped in a standard junit test. The contracts are stored in our `kvContracts repository <https://github.com/harmingcola/kvServerContracts>`_

.. code-block::

    import org.junit.Test;
    import org.seekay.contract.client.client.ContractClient;

    public class KvServerContractTests {

        @Test
        public void runContractTestsAgainstServer() {

            ContractClient.newClient().againstPath("http://localhost:8080")
                    .withGitConfig("https://github.com/harmingcola/kvServerContracts.git")
                    .runTests();

        }
    }



