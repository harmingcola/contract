package org.seekay.contract.all;

import org.apache.commons.cli.*;
import org.seekay.contract.client.client.ContractClient;
import org.seekay.contract.server.ContractServer;

import static java.lang.Integer.valueOf;
import static java.lang.Thread.sleep;
import static org.seekay.contract.model.tools.SetTools.toSet;

public class CommandLineInterface {

	public static void main(String[] args) throws Exception {

		Options options = buildOptions();
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse(options, args);

		StringBuffer footer = new StringBuffer();

		boolean displayHelp = false;
		if(cmd.hasOption("s")) {
			if(cmd.hasOption("p") && cmd.hasOption("g")) {
				if(!cmd.hasOption("u") && !cmd.hasOption("q")) {
					startServerUnsecuredSource(cmd.getOptionValue("p"), cmd.getOptionValue("g"));
				}
				else if(cmd.hasOption("u") && cmd.hasOption("q")) {
					startServerSecuredSource(cmd.getOptionValue("p"), cmd.getOptionValue("g"), cmd.getOptionValue("u"), cmd.getOptionValue("q"));
				}
				else {
					footer.append("Looks like either a username or password was supplied, but not both\n");
					displayHelp = true;
				}
			}
			else {
				footer.append("To start a server, both a port and a configuration source must be specified\n");
				displayHelp = true;
			}
		}
		else if(cmd.hasOption("c")) {
			if(cmd.hasOption("t") && cmd.hasOption("g")) {
				if (!cmd.hasOption("u") && !cmd.hasOption("q")) {
					startClientUnsecuredSource(cmd);
				}
				else if (cmd.hasOption("t") && cmd.hasOption("g") && cmd.hasOption("u") && cmd.hasOption("q")) {
					startClientSecuredSource(cmd);
				}
				else {
					footer.append("Looks like either a username or password was supplied, but not both\n");
					displayHelp = true;
				}
			}
			else {
				footer.append("To run client tests, both a target and a configuration source must be specified\n");
				displayHelp = true;
			}
		}
		else {
			footer.append("Please select a mode to run the jar in, -s for server, -c for client\n");
			displayHelp = true;
		}

		if(displayHelp) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("contract", "", options, footer.toString(), true);
		}
	}

	private static void startClientSecuredSource(CommandLine cmd) {
		ContractClient.newClient().withGitConfig(
          cmd.getOptionValue('g'), cmd.getOptionValue('u'), cmd.getOptionValue('q'))
        .againstPath(cmd.getOptionValue('t'))
        .tags(toSet(cmd.getOptionValue('r')), toSet(cmd.getOptionValue('e')))
        .runTests();
	}

  private static void startClientUnsecuredSource(CommandLine cmd) {
    ContractClient.newClient().withGitConfig(
          cmd.getOptionValue('g'))
        .againstPath(cmd.getOptionValue('t'))
        .tags(toSet(cmd.getOptionValue('r')), toSet(cmd.getOptionValue('e')))
        .runTests();
	}

	private static void startServerSecuredSource(String port, String source, String username, String password) {
		ContractServer server = ContractServer.newServer().onPort(valueOf(port));
		server.withGitConfig(source, username, password);
		server.startServer();
		waitUntilKilled();
	}

	private static void startServerUnsecuredSource(String port, String source) {
		ContractServer server = ContractServer.newServer().onPort(valueOf(port));
		if (source.startsWith("http") || source.startsWith("git")) {
			server.withGitConfig(source);
		} else {
			server.withLocalConfig(source);
		}
		server.startServer();
		waitUntilKilled();
	}



	private static void waitUntilKilled() {
		while (true) try {
			sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static Options buildOptions() {
		Options options = new Options();

		OptionGroup libraryMode = new OptionGroup();
		libraryMode.addOption(new Option("s", "server", false, "starts the jar in server mode"));
		libraryMode.addOption(new Option("c", "client", false, "starts the jar in client mode"));
		options.addOptionGroup(libraryMode);

		OptionGroup modeOptions = new OptionGroup();
		modeOptions.addOption(new Option("p", "port", true, "Port to start server on, only needed when running server"));
		modeOptions.addOption(new Option("t", "target", true, "Target server for tests, only needed when running client"));
		options.addOptionGroup(modeOptions);

		options.addOption("g", "source", true, "URL of the git repository or local location contracts should be loaded from");
		options.addOption("u", "username", true, "Username of secured git repository, optional");
		options.addOption("q", "password",true,"Password of secured git repository, optional");
		options.addOption("e", "exclude-tags", true, "Comma separated list of tags to be excluded from test run");
		options.addOption("r", "retain-tags", true, "Comma separated list of tags to be retained, all other contracts will be ignored");
		options.addOption("h", "help",false,"Prints usage info");

		return options;
	}

}