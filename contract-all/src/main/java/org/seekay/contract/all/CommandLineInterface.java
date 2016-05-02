package org.seekay.contract.all;

import org.apache.commons.cli.*;
import org.seekay.contract.client.client.ContractClient;
import org.seekay.contract.server.ContractServer;

import static java.lang.Integer.valueOf;
import static java.lang.Thread.sleep;

public class CommandLineInterface {
	
	public static void main(String[] args) throws Exception {

		Options options = buildOptions();
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse(options, args);

		boolean displayHelp = true;
		if(cmd.hasOption("s")) {
			if(cmd.hasOption("p") && cmd.hasOption("g") && !cmd.hasOption("u") && !cmd.hasOption("q")) {
				startServerUnsecuredSource(cmd.getOptionValue("p"), cmd.getOptionValue("g"));
				displayHelp = false;
			}
			else if(cmd.hasOption("p") && cmd.hasOption("g") && cmd.hasOption("u") && cmd.hasOption("q")) {
				startServerSecuredSource(cmd.getOptionValue("p"), cmd.getOptionValue("g"), cmd.getOptionValue("u"), cmd.getOptionValue("q"));
				displayHelp = false;
			}
		}
		else if(cmd.hasOption("c")) {
			if(cmd.hasOption("t") && cmd.hasOption("g") && !cmd.hasOption("u") && !cmd.hasOption("q")) {
				startClientUnsecuredSource(cmd.getOptionValue("t"), cmd.getOptionValue("g"));
				displayHelp = false;
			}
			else if(cmd.hasOption("t") && cmd.hasOption("g") && cmd.hasOption("u") && cmd.hasOption("q")) {
				startClientSecuredSource(cmd.getOptionValue("t"), cmd.getOptionValue("g"), cmd.getOptionValue("u"), cmd.getOptionValue("q"));
				displayHelp = false;
			}
		}

		if(displayHelp) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("contract", "", options, "", true);
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
		options.addOption("h", "help",false,"Prints usage info");

		return options;
	}

}

