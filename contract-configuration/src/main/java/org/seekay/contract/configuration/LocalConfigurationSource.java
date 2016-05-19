package org.seekay.contract.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.seekay.contract.configuration.loaders.JsonBodyFileLoader;
import org.seekay.contract.configuration.loaders.StringBodyJsonFileLoader;
import org.seekay.contract.model.domain.Contract;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.seekay.contract.configuration.ParameterExpander.*;


@Slf4j
public class LocalConfigurationSource {

  private static final List<String> IGNORED_DIRECTORIES = Arrays.asList(".git", "__ignored");
  public static final String CONTRACT_FILE_SUFFIX = ".contract.json";

  private ObjectMapper objectMapper = new ObjectMapper();

  public List<Contract> loadFromDirectory(String directoryPath) {
    List<Contract> contracts = new ArrayList<Contract>();
    File directory = new File(directoryPath);
    loadFromDirectory(directory, directory, contracts);
    return contracts;
  }

  public Contract loadFromFile(File file) {
    if (file.getName().endsWith(CONTRACT_FILE_SUFFIX)) {
      log.info("Loading config file : " + file.getPath());
      try {
        byte[] encoded = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        String fileContents = new String(encoded, Charset.defaultCharset());
        return loadFromString(fileContents);
      } catch (IOException e) {
        throw new IllegalStateException(e);
      }
    } else {
      log.info("Skipping {} filename doesnt end in expected suffix", file.getName());
    }
    return null;
  }

  public Contract loadFromString(String contractDefinition) {
    return contractFileLoaderFactory(contractDefinition);
  }

  protected void loadFromDirectory(File directory, File baseDirectory, List<Contract> contracts) {
    if (IGNORED_DIRECTORIES.contains(directory.getName())) {
      return;
    }
    for (File file : directory.listFiles()) { //NOSONAR
      if (file.isDirectory()) {
        loadFromDirectory(file, baseDirectory, contracts);
      } else {
        Contract contract = loadFromFile(file);
        if (contract != null) {
          buildTagsFromDirectoryStructure(contract, baseDirectory, file);
          if(containsParameters(contract)) {
            ParameterExpander expander = new ParameterExpander();
            contracts.addAll(expander.expandParameters(contract));
          } else {
            contracts.add(contract);
          }
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

  private Contract contractFileLoaderFactory(String contractDefinition) {
    try {
      HashMap contents = objectMapper.readValue(contractDefinition, HashMap.class);
      return checkPayloadType(contractDefinition, contents);
    } catch (IOException e) {
      throw new IllegalStateException("Problem occurred converting json to contract", e);
    }
  }

  private Contract checkPayloadType(String contractDefinition, HashMap contents) {
    Map<String, Object> request = (Map<String, Object>) contents.get("request");
    Map<String, Object> response = (Map<String, Object>) contents.get("response");

    if (request.get("body") instanceof String || response.get("body") instanceof String) {
      return new StringBodyJsonFileLoader().load(contractDefinition);
    } else if (request.get("body") instanceof Map || response.get("body") instanceof Map) {
      return new JsonBodyFileLoader().load(contents);
    } else if (request.get("body") instanceof Map || response.get("body") instanceof List) {
      return new JsonBodyFileLoader().load(contents);
    } else{
      return new StringBodyJsonFileLoader().load(contractDefinition);
    }
  }

}
