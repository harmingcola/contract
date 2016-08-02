package org.seekay.contract.common.service;

import lombok.extern.slf4j.Slf4j;
import org.seekay.contract.model.domain.Contract;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class ContractService {

    private Set<Contract> contracts = new HashSet<>();

    public void create(Contract contract) {
        contracts.add(contract);
    }

    public Set<Contract> readEnabled() {
      Set<Contract> enabledContracts = new HashSet<>();
      for(Contract contract: contracts) {
        if(contract.getEnabled()) {
          enabledContracts.add(contract);
        }
      }
      return enabledContracts;
    }

  public Set<Contract> readAll() {
    return contracts;
  }

    public void deleteContracts() {
        contracts = new HashSet<>();
    }

  public void enableFilters(List<String> filters) {
    if(filters != null) {
      log.info("Disabling contracts not matching {}", filters);
      for (Contract contract : readAll()) {
        contract.setEnabled(false);
        for (String tag : filters) {
          if (contract.readTags().contains(tag)) {
            contract.setEnabled(true);
          }
        }
      }
    }
  }

  public void clearFilters() {
    log.info("Enabling all contracts");
    for (Contract contract : readAll()) {
      contract.setEnabled(true);
    }
  }
}
