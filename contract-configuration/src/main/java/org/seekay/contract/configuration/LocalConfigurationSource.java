package org.seekay.contract.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.seekay.contract.configuration.loaders.ContractFileLoader;
import org.seekay.contract.configuration.loaders.JsonBodyFileLoader;
import org.seekay.contract.configuration.loaders.StringBodyJsonFileLoader;
import org.seekay.contract.model.domain.Contract;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.seekay.contract.common.ApplicationContext.objectMapper;

@Slf4j
public class LocalConfigurationSource implements ConfigurationSource {

    private File baseDirectory;

    private ObjectMapper objectMapper = objectMapper();

    public LocalConfigurationSource(String baseDirectory) {
        this.baseDirectory = new File(baseDirectory);
        log.info("Loading config from : " + this.baseDirectory.getAbsolutePath());
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
        for(File file : directory.listFiles()) { //NOSONAR
            if(file.isDirectory()) {
                loadFromDirectory(file, contracts);
            } else {
				Contract contract = loadFromFile(file);
				if(contract != null) {
					buildTagsFromDirectoryStructure(contract, this.baseDirectory, file);
					contracts.add(contract);
				}
            }
        }
    }

	private void buildTagsFromDirectoryStructure(Contract contract, File directory, File file) {
		String fileAbsolutePath = file.getAbsolutePath();
		String fileName = file.getName();
		String relativeLocation = fileAbsolutePath.replace(directory.getAbsolutePath(), "");
		String directoriesOnly = relativeLocation.replace(fileName, "");
		String[] tags = directoriesOnly.split("/");
		contract.addTags(tags);
	}

	protected Contract loadFromFile(File file) {
        if(file.getName().endsWith(".contract.json")) {
			log.info("Loading config file : " + file.getAbsolutePath());
			return contractFileLoaderFactory(file).load();
		}
		return null;
    }

	private ContractFileLoader contractFileLoaderFactory(File file) {
		try {
			HashMap contents = objectMapper.readValue(file, HashMap.class);
			return checkPayloadType(file, contents);
		} catch (IOException e) {
			throw new IllegalStateException("Problem occurred converting json to contract", e);
		}
	}

	private ContractFileLoader checkPayloadType(File file, HashMap contents) {
		Map<String, Object> request = (Map<String, Object>) contents.get("request");
		Map<String, Object> response = (Map<String, Object>) contents.get("response");

		if(request.get("body") == null && response.get("body") == null) {
			return new StringBodyJsonFileLoader(file);
		} else if(request.get("body") instanceof String || response.get("body") instanceof String) {
			return new StringBodyJsonFileLoader(file);
		} else if(request.get("body") instanceof Map || response.get("body") instanceof Map) {
			return new JsonBodyFileLoader(contents, file);
		}
		else {
			throw new IllegalStateException("Not sure how to create contract from " + contents);
		}
	}

}
