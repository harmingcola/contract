package org.seekay.contract.all;

import org.eclipse.jgit.util.StringUtils;
import org.seekay.contract.client.client.ContractClient;
import org.seekay.contract.server.ContractServer;

import static java.lang.Integer.valueOf;
import static java.lang.System.out;
import static java.lang.Thread.sleep;
import static java.util.Arrays.asList;

public class CommandLineInterface {

  public static void main(String[] args) {
    checkArgs(args);
    if(argumentIs("run-server", args, 0) && args.length == 3) {
      startServerUnsecuredSource(args[1], args[2]);
      waitUntilKilled();
    } else if(argumentIs("run-server", args, 0) && args.length == 5) {
      startServerSecuredSource(args[1], args[2], args[3], args[4]);
      waitUntilKilled();
    } else if(argumentIs("run-client", args, 0) && args.length == 3) {
      startClientUnsecuredSource(args[1], args[2]);
    } else if(argumentIs("run-client", args, 0) && args.length == 5) {
      startClientSecuredSource(args[1], args[2], args[3], args[4]);
    } else {
      exitWithHelp(args);
    }
  }

  private static void startClientSecuredSource(String target, String source, String username, String password) {
    ContractClient.newClient().withGitConfig(source, username, password).againstPath(target).runTests();
  }

  private static void startClientUnsecuredSource(String target, String source) {
    ContractClient.newClient().withGitConfig(source).againstPath(target).runTests();
  }

  private static void startServerSecuredSource(String port, String source, String username, String password) {
    ContractServer server = ContractServer.newServer().onPort(valueOf(port));
    server.withGitConfig(source, username, password);
    server.startServer();
  }

  private static void waitUntilKilled() {
    while(true) try {
      sleep(10000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private static void startServerUnsecuredSource(String port, String source) {
    ContractServer server = ContractServer.newServer().onPort(valueOf(port));
    if(source.startsWith("http") || source.startsWith("git")) {
      server.withGitConfig(source);
    } else {
      server.withLocalConfig(source);
    }
    server.startServer();
  }

  private static boolean argumentIs(String argValue, String[] args, int i) {
    return args[i].equalsIgnoreCase(argValue);
  }

  private static void checkArgs(String[] args) {
    if(args.length == 0) {
      exitWithHelp(args);
    }
    if(!args[0].equalsIgnoreCase("run-server") && !args[0].equalsIgnoreCase("run-client")) {
      exitWithHelp(args);
    }
  }

  private static void exitWithHelp(String[] args) {
    out.println("------------------------------");
    out.println("--- Contract Command Line");
    out.println("------------------------------");
    out.println("--- Invalid parameters passed");
    out.println("--- ");
    out.println("--- " + StringUtils.join(asList(args), ", "));
    out.println("--- ");
    out.println("--- Valid Command : <run-server> <port> <source> <username> <passwword>");
    out.println("--- Valid Command : <run-client> <target> <source> <username> <passwword>");
    out.println("--- Username and password can be omitted for local and unsecured sources");
    out.println("--- ");
    out.println("--- Example : run-server 8080 git@github.com:harmingcola/kvServerContracts.git");
    out.println("--- Example : run-client 8080 git@github.com:harmingcola/kvServerContracts.git");
    out.println("------------------------------");
    System.exit(1);
  }
}
