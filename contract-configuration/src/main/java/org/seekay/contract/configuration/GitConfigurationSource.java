package org.seekay.contract.configuration;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
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
    cloneFromGit();
    return localSource.loadFromDirectory(DOWNLOAD_LOCATION);
  }

  private void cloneFromGit() {
    File localPath = setupDownloadLocation();
    setupRepositoryConnection(localPath);
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

  private File setupDownloadLocation() {
    File downloadLocation = new File(DOWNLOAD_LOCATION);
    deleteExistingCheckout(downloadLocation);
    if (!downloadLocation.mkdir()) {
      throw new IllegalStateException("Unable to create download directory");
    }
    return downloadLocation;
  }

  private void deleteExistingCheckout(File localPath) {
    if (localPath.exists()) {
      try {
        deleteDirectory(localPath);
      } catch (IOException e) {
        throw new IllegalStateException("Unable to remove old config source", e);
      }
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

  /*
   * Total testing hack, made public so an exception can be thrown via a Spy
   */
  public void deleteDirectory(File localPath) throws IOException {
    FileUtils.deleteDirectory(localPath);
  }
}
