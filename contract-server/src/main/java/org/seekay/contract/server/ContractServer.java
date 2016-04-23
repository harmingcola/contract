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
import org.seekay.contract.model.util.Http;
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

    public static ContractServer newServer() {
        return new ContractServer();
    }

    public ContractServer onPort(Integer port) {
        this.port = port;
        return this;
    }

    public ContractServer onRandomPort() {
        this.port = new Random().nextInt(999) + 9000;
        return this;
    }

    public ContractServer withLocalConfig(String... configLocations) {
        for(String localConfigLocation : configLocations) {
            sources.add(new LocalConfigurationSource(localConfigLocation));
        }
        return this;
    }

    public ContractServer withGitConfig(String repositoryUrl, String username, String password) {
        sources.add(new GitConfigurationSource(repositoryUrl, username, password));
        return this;
    }

    public ContractServer withGitConfig(String repositoryUrl) {
        sources.add(new GitConfigurationSource(repositoryUrl));
        return this;
    }

    public ContractServer startServer() {
        tomcat.setPort(this.port);
        tomcat.setBaseDir("target/tomcat/");
        configureServer();
        try {
            tomcat.start();
        } catch (LifecycleException e) {
            throw new IllegalStateException("Problem occurred starting tomcat", e);
        }
        addContractsFromConfigSources();
        return this;
    }


    public void stopServer() {
        try {
            tomcat.getServer().stop();
            tomcat.getServer().await();
            tomcat.destroy();
        } catch (LifecycleException e) {
            throw new IllegalStateException(e);
        }
    }

    public String path() {
        return "http://localhost:" + port;
    }

    public String configurePath() {
        return "http://localhost:" + port + "/__configure";
    }

    public void addContracts(Contract... contracts){
        for(Contract contract : contracts) {
            addContract(contract);
        }
    }

    public void addContract(Contract contract) {
        Http.post().toPath(configurePath()).withBody(toJson(contract)).execute();
    }

    private String toJson(Contract contract){
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


    private void addContractsFromConfigSources() {
        for(ConfigurationSource source : this.sources) {
            for(Contract contract : source.load()) {
               addContract(contract);
            }
        }
    }

}
