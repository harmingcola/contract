package org.seekay.contract.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.seekay.contract.server.ContractServer;

import static java.lang.Integer.valueOf;
import static org.seekay.contract.model.tools.SetTools.toSet;
import static org.seekay.contract.server.ContractServer.newServer;

@Mojo(name = "start-server")
public class StartServerMojo extends AbstractMojo {


  @Parameter(property = "port", defaultValue = "8080")
  private String port;

  @Parameter(property = "gitSource")
  private String gitSource;

  @Parameter(property = "localSource")
  private String localSource;

  @Parameter(property = "username")
  private String username;

  @Parameter(property = "password")
  private String password;

  @Parameter(property = "tagsToExclude")
  private String tagsToExclude;

  @Parameter(property = "tagsToRetain")
  private String tagsToRetain;

  public void execute() throws MojoExecutionException {
    if (gitSource == null && localSource == null) {
      throw new IllegalStateException("Either gitSource or localSource arguments must be provided");
    }
    if (username == null ^ password == null) {
      throw new IllegalStateException("Both username and password must be provided for a secure repo, or neither for a public repo");
    }

    ContractServer server = newServer().onPort(valueOf(port));
    if (gitSource != null && (password == null && username == null)) {
      server.withGitConfig(gitSource);
    }
    if (gitSource != null && (password != null && username != null)) {
      server.withGitConfig(gitSource, username, password);
    }
    if (localSource != null) {
      server.withLocalConfig(localSource);
    }
    server.tags(toSet(tagsToRetain), toSet(tagsToExclude));

    new ServerThread(server).start();
  }
}
