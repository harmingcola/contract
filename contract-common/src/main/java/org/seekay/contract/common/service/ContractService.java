package org.seekay.contract.common.service;

import org.seekay.contract.model.domain.Contract;

import java.util.HashSet;
import java.util.Set;

public class ContractService {

    private Set<Contract> contracts = new HashSet<Contract>();

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
      return contracts;
    }

  public Set<Contract> readAll() {
    return contracts;
  }

    public void deleteContracts() {
        contracts = new HashSet<Contract>();
    }
}
