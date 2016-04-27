package org.seekay.contract.model.builder;

import org.seekay.contract.model.domain.Contract;

import java.util.Set;

public interface ContractOperator<T> {

  T tags(Set<String> tagsToInclude, Set<String> tagsToExclude);

  /**
   * Filters already loaded contracts. Only contracts containing none of the tagsToExclude will be retained.
   * @param tagsToExclude
   * @return
   */
  T excludeTags(String... tagsToExclude);

  /**
   * Filters already loaded contracts. Only contracts containing at least one tag will be retained.
   * @param tagsToInclude
   * @return
   */
  T onlyIncludeTags(String... tagsToInclude);

  /**
   * Adds contracts to memory for execution
   * @param contracts
   */
  void addContracts(Contract... contracts);

  /**
   * Adds a single contract to memory
   * @param contract
   */
  void addContract(Contract contract);

  /**
   * Loads contracts from a local directory
   * @param configLocations
   * @return
   */
  T withLocalConfig(String... configLocations);

  /**
   * Loads contracts from a secured git repository
   * @param repositoryUrl
   * @param username
   * @param password
   * @return
   */
  T withGitConfig(String repositoryUrl, String username, String password);

  /**
   * Loads contracts from a git repository
   * @param repositoryUrl
   * @return
   */
  T withGitConfig(String repositoryUrl);

  // static T fromContracts(List<Contract> contracts)
}
