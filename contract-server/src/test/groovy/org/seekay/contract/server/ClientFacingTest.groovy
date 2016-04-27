package org.seekay.contract.server

import spock.lang.Specification

class ClientFacingTest extends Specification {

    protected ContractServer server

    protected Session session

    public static final String CONTRACT_SERVER = "contractServer"

    def setup() {
        session = new Session()
        server = session.getObjectByKey(CONTRACT_SERVER)
        if(server == null) {
            server = ContractServer.newServer()
                    .onRandomPort()
                    .startServer()
            session.setKeyValue(CONTRACT_SERVER, server)
        } else {
            server.reset()
        }
    }

}
