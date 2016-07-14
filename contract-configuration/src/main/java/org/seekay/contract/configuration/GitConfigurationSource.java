package org.seekay.contract.configuration;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.seekay.contract.model.domain.Contract;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
public class GitConfigurationSource {

  public static final String DOWNLOAD_LOCATION = "target/git-contract-source/";

  private final String repositoryUrl;
  private final String username;
  private final String password;

  private LocalConfigurationSource localSource = new LocalConfigurationSource();

  public GitConfigurationSource(String repositoryUrl, String username, String password) {
    this.repositoryUrl = repositoryUrl;
    this.username = username;
    this.password = password;
  }

  public GitConfigurationSource(String repositoryUrl) {
    this.repositoryUrl = repositoryUrl;
    this.username = null;
    this.password = null;
  }

  public List<Contract> load() {
    File localRepositoryLocation = cloneFromGit();
    return localSource.loadFromDirectory(localRepositoryLocation);
  }

  private File cloneFromGit() {
    String repositoryName = extractNameFromUrl(this.repositoryUrl);
    File localRepositoryLocation = new File(DOWNLOAD_LOCATION + repositoryName);
    if(localRepositoryLocation.exists()) {
      updateLocalRepository(localRepositoryLocation);
    }
    else {
      setupDownloadLocation(localRepositoryLocation);
      setupRepositoryConnection(localRepositoryLocation);
    }
    return localRepositoryLocation;
  }

  private String extractNameFromUrl(String repositoryUrl) {
    String[] tokens = repositoryUrl.split("/");
    return tokens[tokens.length-1].replace(".git","");
  }

  private Git setupRepositoryConnection(File localPath) {
    log.info("Cloning from {} to {}", this.repositoryUrl, localPath.getAbsolutePath());
    try {
      if (this.password == null) {
        return clonePublicRepository(localPath);
      }
      return clonePrivateRepository(localPath);
    } catch (GitAPIException e) {
      log.error("Problem occurred when contacting git", e);
      throw new IllegalStateException(e);
    }
  }

  private void setupDownloadLocation(File localRepositoryLocation) {
    localRepositoryLocation.mkdir();
  }

  private void updateLocalRepository(File localRepositoryLocation) {
    log.info("Updating existing repository at {}", localRepositoryLocation.getAbsolutePath());
    try {
      Git.open(localRepositoryLocation).pull();
    } catch (IOException e) {
      throw new IllegalStateException("Problem occurred pulling from existing repository",e);
    }
  }

  /*
   * Total testing hack, made public so an exception can be thrown via a Spy
   */
  public Git clonePrivateRepository(File localPath) throws GitAPIException {
    return Git.cloneRepository()
        .setURI(this.repositoryUrl)
        .setDirectory(localPath)
        .setCredentialsProvider(new UsernamePasswordCredentialsProvider(this.username, this.password))
        .call();
  }

  /*
   * Total testing hack, made public so an exception can be thrown via a Spy
   */
  public Git clonePublicRepository(File localPath) throws GitAPIException {
    return Git.cloneRepository()
        .setURI(this.repositoryUrl)
        .setDirectory(localPath)
        .call();
  }
}
