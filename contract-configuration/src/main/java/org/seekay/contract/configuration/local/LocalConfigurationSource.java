package org.seekay.contract.configuration.local;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.seekay.contract.configuration.ConfigurationSource;
import org.seekay.contract.model.domain.Contract;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class LocalConfigurationSource implements ConfigurationSource {

    private File baseDirectory;

    private ObjectMapper objectMapper;

    public LocalConfigurationSource(String baseDirectory) {
        this.baseDirectory = new File(baseDirectory);
        log.info("Loading config from : " + this.baseDirectory.getAbsolutePath());
		objectMapper = new ObjectMapper();
    }

    public List<Contract> load() {
        List<Contract> contracts = new ArrayList<Contract>();
        loadFromDirectory(baseDirectory, contracts);
        return contracts;
    }

    protected void loadFromDirectory(File directory, List<Contract> contracts) {
		if(directory.getName().equals(".git")) {
			return;
		}
        for(File file : directory.listFiles()) { // NOSONAR
            if(file.isDirectory()) {
                loadFromDirectory(file, contracts);
            } else {
                loadFromFile(file, contracts);
            }
        }
    }

    protected void loadFromFile(File file, List<Contract> contracts) {
        if(file.getName().endsWith(".contract.json")) {
			log.info("Loading config file : " + file.getAbsolutePath());
			try {
				contracts.add(objectMapper.readValue(file, Contract.class));
			} catch (IOException e) {
				log.error("Problem with unmarshalling file ", e);
			}
		}
    }
}
