## Contract
### A JVM contract testing tool

#### Full Documentation
[Github pages](http://harmingcola.github.io/contract/)

#### Google group
[Group](https://groups.google.com/forum/#!forum/seekay-contracts)

#### Introduction
Late stage integration testing of a http client/server system can be a painful endeavour. 
Differences of specification between client & server can cause systems to be incompatible, we need to fail earlier.
Earlier stage testing relies on replacing the other side of the http relationship with what we "think" will be in place when both systems are in place.
This is a disconnected endeavour and relies on what we "think" to be updated periodically with what our companion system requires.
Contract is an open source testing tool aimed at replacing a companion system in a http client or server relationship so that shared specifications can be used to unit, integration or acceptance level test to ensure that both systems are on the same page.
Server facing tests can be driven using a contract that specifies how the server is to react given a http request. 
Client facing tests can be driven by a pact specifying that when a client makes a specific request, they should receive a specific response from a server. 
With both sides of a client server relationship available for testing at a significantly earlier stage, we can improve the feedback of our testing cycles, and hopefully deliver value that little bit quicker.