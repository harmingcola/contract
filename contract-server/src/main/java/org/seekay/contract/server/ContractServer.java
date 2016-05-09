package org.seekay.contract.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.seekay.contract.common.Http;
import org.seekay.contract.configuration.GitConfigurationSource;
import org.seekay.contract.configuration.LocalConfigurationSource;
import org.seekay.contract.model.builder.ContractOperator;
import org.seekay.contract.model.domain.Contract;
import org.seekay.contract.model.tools.ContractTools;
import org.seekay.contract.server.servet.ConfigurationServlet;
import org.seekay.contract.server.servet.HealthServlet;
import org.seekay.contract.server.servet.RequestHandlerServlet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.apache.catalina.startup.Tomcat.addServlet;
import static org.seekay.contract.model.tools.SleepTools.sleep;

@Slf4j
public class ContractServer implements ContractOperator<ContractServer> {

	private Tomcat tomcat;

	private Integer port;

  private List<Contract> contracts;

	private ObjectMapper objectMapper;

	private ContractServer() {
    this.contracts = new ArrayList<Contract>();
		this.objectMapper = new ObjectMapper();
		this.tomcat = new Tomcat();
	}

  private ContractServer(List<Contract> contracts) {
    this.contracts = contracts;
    this.objectMapper = new ObjectMapper();
    this.tomcat = new Tomcat();
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
   * Starts the server and populates it with loaded contracts.
   * @return
   */
  public ContractServer startServer() {
    tomcat.setPort(this.port);
    tomcat.setBaseDir("target/tomcat/");
    configureServer();
    try {
      tomcat.start();
			waitForServerToStart();
    } catch (LifecycleException e) {
      throw new IllegalStateException("Problem occurred starting tomcat", e);
    }
    log.info("Tomcat server started on port " + this.port);
    pushContractsToServer();
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
   * Removes all contracts from the server.
   */
  public void reset() {
    Http.delete().toPath(configurePath()).execute();
    contracts = new ArrayList<Contract>();
  }

  /**
   * Loads contracts from every config source. Useful when used in conjunction with reset() to blank slate the server.
   */
  public void pushContractsToServer() {
		log.info("Uploading contracts to server");
    for (Contract contract : contracts) {
      addContract(contract);
    }
  }

  /**
   * Creates a new instance populated with supplied contracts
   * @param contracts
   * @return
   */
  public static ContractServer fromContracts(List<Contract> contracts) {
    return new ContractServer(contracts);
  }

	public ContractServer withLocalConfig(String... configLocations) {
		for (String localConfigLocation : configLocations) {
			contracts.addAll(new LocalConfigurationSource(localConfigLocation).load());
		}
		return this;
	}

	public ContractServer withGitConfig(String repositoryUrl, String username, String password) {
		contracts.addAll(new GitConfigurationSource(repositoryUrl, username, password).load());
		return this;
	}

	public ContractServer withGitConfig(String repositoryUrl) {
    contracts.addAll(new GitConfigurationSource(repositoryUrl).load());
		return this;
	}


  public void addContract(Contract contract) {
    Http.post().toPath(configurePath()).withBody(toJson(contract)).execute();
  }

  public void addContracts(Contract... contracts) {
    for (Contract contract : contracts) {
      addContract(contract);
    }
  }

	public ContractServer retainTags(String... tagsToRetain) {
		this.contracts = ContractTools.retainTags(this.contracts, tagsToRetain);
		return this;
	}

	public ContractServer excludeTags(String... tagsToExclude) {
		this.contracts = ContractTools.excludeTags(this.contracts, tagsToExclude);
		return this;
	}

	public ContractServer tags(Set<String> tagsToRetain, Set<String> tagsToExclude) {
		this.contracts = ContractTools.tags(this.contracts, tagsToRetain, tagsToExclude);
		return this;
	}

	private void waitForServerToStart() {
		String healthUrl = path() + "/__health";
		Http http = Http.get().fromPath(healthUrl);
		while (http.execute().status() != 200) {
			sleep(100);
		}
	}

	private String toJson(Contract contract) {
		try {
			return objectMapper.writeValueAsString(contract);
		} catch (JsonProcessingException e) {
			log.error("Error configuring server with contract ["+ contract +"]", e);
			throw new IllegalStateException(e);
		}
	}

	private void configureServer() {
		Context context = tomcat.addContext("/", new File(".").getAbsolutePath());

		addServlet(context, "healthEndpoint", new HealthServlet());
		context.addServletMapping("/__health", "healthEndpoint");

		addServlet(context, "configurationHandler", new ConfigurationServlet());
		context.addServletMapping("/__configure", "configurationHandler");

		addServlet(context, "requestHandler", new RequestHandlerServlet());
		context.addServletMapping("/*", "requestHandler");
	}
}
