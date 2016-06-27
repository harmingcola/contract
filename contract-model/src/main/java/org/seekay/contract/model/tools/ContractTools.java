package org.seekay.contract.model.tools;

import org.seekay.contract.model.domain.Contract;

import java.util.List;
import java.util.Set;

import static org.seekay.contract.model.tools.SetTools.toArray;

public class ContractTools {

  /**
   * Private constructor for utility class
   */
  private ContractTools() {
    throw new IllegalStateException("Utility classes should never be constructed");
  }

  public static void retainTags(List<Contract> contracts, String... tagsToRetain) {
    if(tagsToRetain.length == 0) {
      return;
    }
    for(Contract contract : contracts) {
      contract.setEnabled(false);
      Set<String> contractTags = contract.readTags();
      for(String tagToRetain : tagsToRetain) {
        if(contractTags.contains(tagToRetain)) {
          contract.setEnabled(true);
        }
      }
    }
  }

  public static void excludeTags(List<Contract> contracts, String... tagsToExclude) {
    if(tagsToExclude.length != 0) {
      for (Contract contract : contracts) {
        Set<String> contractTags = contract.readTags();
        for (String tagToExclude : tagsToExclude) {
          if (contractTags.contains(tagToExclude)) {
            contract.setEnabled(false);
            continue;
          }
        }
      }
    }
  }

  public static void tags(List<Contract> contracts, Set<String> tagsToRetain, Set<String> tagsToExclude) {
    if(tagsToRetain != null) {
      retainTags(contracts, toArray(tagsToRetain));
    }
    if(tagsToExclude != null) {
      excludeTags(contracts, toArray(tagsToExclude));
    }
  }

  public static int enabledCount(List<Contract> contracts) {
    int count = 0;
    for(Contract contract : contracts) {
      if(contract.getEnabled()) {
        count++;
      }
    }
    return count;
  }
}
