package org.seekay.contract.model.tools;

import org.seekay.contract.model.domain.Contract;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ContractTools {

  /**
   * Private constructor for utility class
   */
  private ContractTools() {
    throw new IllegalStateException("Utility classes should never be constructed");
  }

  public static List<Contract> onlyIncludeTags(List<Contract> contracts, String... tagsToInclude) {
    Set<Contract> contractsToInclude = new HashSet<Contract>();
    for(Contract contract : contracts) {
      Set<String> contractTags = contract.readTags();
      for(String tagToInclude : tagsToInclude) {
        if(contractTags.contains(tagToInclude)) {
          contractsToInclude.add(contract);
          continue;
        }
      }
    }
    return new ArrayList<Contract>(contractsToInclude);
  }

  public static List<Contract> excludeTags(List<Contract> contracts, String... tagsToExclude) {
    Set<Contract> contractsToExclude = new HashSet<Contract>();
    for(Contract contract : contracts) {
      Set<String> contractTags = contract.readTags();
      for(String tagToInclude : tagsToExclude) {
        if(contractTags.contains(tagToInclude)) {
          contractsToExclude.add(contract);
          continue;
        }
      }
    }
    contracts.removeAll(contractsToExclude);
    return contracts;
  }

  public static List<Contract> tags(List<Contract> contracts, Set<String> tagsToInclude, Set<String> tagsToExclude) {
    if(tagsToInclude != null) {
      contracts = onlyIncludeTags(contracts, SetTools.toArray(tagsToInclude));
    }
    if(tagsToExclude != null) {
      contracts = excludeTags(contracts, SetTools.toArray(tagsToExclude));
    }
    return contracts;
  }
}
