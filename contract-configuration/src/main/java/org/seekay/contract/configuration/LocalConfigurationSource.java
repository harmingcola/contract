package org.seekay.contract.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.seekay.contract.model.domain.Contract;
import org.seekay.contract.model.domain.ContractMap;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.seekay.contract.configuration.ParameterExpander.containsParameters;


@Slf4j
public class LocalConfigurationSource {

  private static final List<String> IGNORED_DIRECTORIES = Arrays.asList(".git", "__ignored");
  public static final String JSON_CONTRACT_FILE_SUFFIX = ".contract.json";

  private ObjectMapper objectMapper = new ObjectMapper();

  public List<Contract> loadFromDirectory(String directoryPath) {
    List<Contract> contracts = new ArrayList<Contract>();
    File directory = new File(directoryPath);
    loadFromDirectory(directory, directory, contracts);
    return contracts;
  }

  public Contract loadFromFile(File file) {
    if (file.getName().endsWith(JSON_CONTRACT_FILE_SUFFIX)) {
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
    try {
      ContractMap contents = objectMapper.readValue(contractDefinition, ContractMap.class);
      return new JsonBodyFileLoader().load(contents);
    } catch (IOException e) {
      throw new IllegalStateException("Problem occurred converting json to contract", e);
    }
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

}
