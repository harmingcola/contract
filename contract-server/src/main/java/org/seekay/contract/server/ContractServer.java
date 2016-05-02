package org.seekay.contract.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.seekay.contract.configuration.ConfigurationSource;
import org.seekay.contract.configuration.GitConfigurationSource;
import org.seekay.contract.configuration.LocalConfigurationSource;
import org.seekay.contract.model.domain.Contract;
import org.seekay.contract.model.tools.Http;
import org.seekay.contract.server.servet.ConfigurationServlet;
import org.seekay.contract.server.servet.RequestHandlerServlet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.apache.catalina.startup.Tomcat.addServlet;

@Slf4j
public class ContractServer {

	private Tomcat tomcat;

	private Integer port;

	private List<ConfigurationSource> sources;

	private ObjectMapper objectMapper;

	private ContractServer() {
		sources = new ArrayList<ConfigurationSource>();
		objectMapper = new ObjectMapper();
		tomcat = new Tomcat();
	}

	/**
	 * Creates a new ContractServer
	 * @return
	 */
	public static ContractServer newServer() {
		return new ContractServer();
	}

	/**
	 * Sets the port on which the server is to start.
	 * @param port
	 * @return
	 */
	public ContractServer onPort(Integer port) {
		this.port = port;
		return this;
	}

	/**
	 * Sets the port on which the server is to start somewhere between 9000 and 9999
	 * @return
	 */
	public ContractServer onRandomPort() {
		this.port = new Random().nextInt(999) + 9000;
		return this;
	}

	/**
	 * Loads contracts from a local directory
	 * @param configLocations
	 * @return
	 */
	public ContractServer withLocalConfig(String... configLocations) {
		for (String localConfigLocation : configLocations) {
			sources.add(new LocalConfigurationSource(localConfigLocation));
		}
		return this;
	}

	/**
	 * Loads contracts from a secured git repository
	 * @param repositoryUrl
	 * @param username
	 * @param password
	 * @return
	 */
	public ContractServer withGitConfig(String repositoryUrl, String username, String password) {
		sources.add(new GitConfigurationSource(repositoryUrl, username, password));
		return this;
	}

	/**
	 * Loads contracts from a git repository
	 * @param repositoryUrl
	 * @return
	 */
	public ContractServer withGitConfig(String repositoryUrl) {
		sources.add(new GitConfigurationSource(repositoryUrl));
		return this;
	}

	/**
	 * Starts the server and populates it with loaded contracts.
	 * @return
	 */
	public ContractServer startServer() {
		tomcat.setPort(this.port);
		tomcat.setBaseDir("target/tomcat/");
		configureServer();
		try {
			tomcat.start();
		} catch (LifecycleException e) {
			throw new IllegalStateException("Problem occurred starting tomcat", e);
		}
		log.info("Tomcat server started on port " + this.port);
		addContractsFromConfigSources();
		return this;
	}

	/**
	 * Returns the path of the current server. Useful when using the randomPort() method.
	 * @return
	 */
	public String path() {
		return "http://localhost:" + port;
	}

	/**
	 * Returns the config url of the server
	 * @return
	 */
	public String configurePath() {
		return "http://localhost:" + port + "/__configure";
	}

	/**
	 * Adds contracts to the server
	 * @param contracts
	 */
	public void addContracts(Contract... contracts) {
		for (Contract contract : contracts) {
			addContract(contract);
		}
	}

	/**
	 * Adds a single contract to the server
	 * @param contract
	 */
	public void addContract(Contract contract) {
		Http.post().toPath(configurePath()).withBody(toJson(contract)).execute();
	}

	/**
	 * Removes all contracts from the server.
	 */
	public void reset() {
		Http.delete().toPath(configurePath()).execute();
		sources = new ArrayList<ConfigurationSource>();
	}

	/**
	 * Loads contracts from every config source. Useful when used in conjunction with reset() to blank slate the server.
	 */
	public void addContractsFromConfigSources() {
		for (ConfigurationSource source : this.sources) {
			for (Contract contract : source.load()) {
				addContract(contract);
			}
		}
	}

	private String toJson(Contract contract) {
		try {
			return objectMapper.writeValueAsString(contract);
		} catch (JsonProcessingException e) {
			//log.error("Error configuring server with contract ["+ contract +"]", e);
			throw new IllegalStateException(e);
		}
	}

	private void configureServer() {
		Context context = tomcat.addContext("/", new File(".").getAbsolutePath());

		addServlet(context, "configurationHandler", new ConfigurationServlet());
		context.addServletMapping("/__configure", "configurationHandler");

		addServlet(context, "requestHandler", new RequestHandlerServlet());
		context.addServletMapping("/*", "requestHandler");
	}
}
