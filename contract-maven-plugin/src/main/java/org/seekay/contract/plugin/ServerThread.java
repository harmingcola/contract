package org.seekay.contract.plugin;

import org.seekay.contract.server.ContractServer;

public class ServerThread extends Thread {

  private final ContractServer contractServer;

  public ServerThread(ContractServer contractServer) {
    this.contractServer = contractServer;
    this.setDaemon(true);
  }

  @Override
  public void run() {
    super.run();
    try {
      this.contractServer.startServer();
      while (true) {
        // do nothing
      }
    }catch(Exception e) {
      // do nothing
    }
  }

}
