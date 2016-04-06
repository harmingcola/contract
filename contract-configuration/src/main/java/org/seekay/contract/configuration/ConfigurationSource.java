package org.seekay.contract.configuration;

import org.seekay.contract.model.domain.Contract;

import java.util.List;

public interface ConfigurationSource {

    List<Contract> load();
}
