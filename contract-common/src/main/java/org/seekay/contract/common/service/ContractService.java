package org.seekay.contract.common.service;

import org.seekay.contract.model.domain.Contract;

import java.util.HashSet;
import java.util.Set;

public class ContractService {

    private Set<Contract> contracts = new HashSet<Contract>();

    public void create(Contract contract) {
        contracts.add(contract);
    }

    public Set<Contract> read() {
        return contracts;
    }
}
