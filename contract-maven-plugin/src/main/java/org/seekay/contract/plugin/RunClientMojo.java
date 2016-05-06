package org.seekay.contract.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.seekay.contract.client.client.ContractClient;

import static org.seekay.contract.model.tools.SetTools.toSet;

@Mojo(name = "run-client")
public class RunClientMojo extends AbstractMojo {

  @Parameter(property = "target", required = true)
  private String target;

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
    if(gitSource == null && localSource == null) {
      throw new IllegalStateException("Either gitSource or localSource arguments must be provided");
    }
    if(username == null ^ password == null) {
      throw new IllegalStateException("Both username and password must be provided for a secure repo, or neither for a public repo");
    }

    ContractClient client = ContractClient.newClient().againstPath(target);
    if(gitSource != null && (password == null && username == null)) {
      client.withGitConfig(gitSource);
    }
    if(gitSource != null && (password != null && username != null)) {
      client.withGitConfig(gitSource, username, password);
    }
    if(localSource != null) {
      client.withLocalConfig(localSource);
    }
    client.tags(toSet(tagsToRetain), toSet(tagsToExclude));
    client.runTests();
  }
}
