package org.seekay.contract.model.tools;

import org.seekay.contract.model.domain.Contract;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.seekay.contract.model.tools.SetTools.*;

public class ContractTools {

  /**
   * Private constructor for utility class
   */
  private ContractTools() {
    throw new IllegalStateException("Utility classes should never be constructed");
  }

  public static List<Contract> retainTags(List<Contract> contracts, String... tagsToRetain) {
    if(tagsToRetain.length == 0) {
      return contracts;
    }
    Set<Contract> contractsToRetain = new HashSet<Contract>();
    for(Contract contract : contracts) {
      Set<String> contractTags = contract.readTags();
      for(String tagToRetain : tagsToRetain) {
        if(contractTags.contains(tagToRetain)) {
          contractsToRetain.add(contract);
          continue;
        }
      }
    }
    return new ArrayList<Contract>(contractsToRetain);
  }

  public static List<Contract> excludeTags(List<Contract> contracts, String... tagsToExclude) {
    if(tagsToExclude.length != 0) {
      Set<Contract> contractsToExclude = new HashSet<Contract>();
      for (Contract contract : contracts) {
        Set<String> contractTags = contract.readTags();
        for (String tagToExclude : tagsToExclude) {
          if (contractTags.contains(tagToExclude)) {
            contractsToExclude.add(contract);
            continue;
          }
        }
      }
      contracts.removeAll(contractsToExclude);
    }
    return contracts;
  }

  public static List<Contract> tags(List<Contract> contracts, Set<String> tagsToRetain, Set<String> tagsToExclude) {
    if(tagsToRetain != null) {
      contracts = retainTags(contracts, toArray(tagsToRetain));
    }
    if(tagsToExclude != null) {
      contracts = excludeTags(contracts, toArray(tagsToExclude));
    }
    return contracts;
  }
}
