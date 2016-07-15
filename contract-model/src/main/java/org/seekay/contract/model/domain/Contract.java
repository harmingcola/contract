package org.seekay.contract.model.domain;

import lombok.Data;

import java.util.*;

import static java.util.Locale.*;

/*
 * Note: When adding fields to contract, ensure a matching field
 * is added to ContractMap.java
 */
@Data
public class Contract implements Comparable<Contract> {

  private Map<String, Object> info;

  private List<Map<String, Object>> parameters;

  private LinkedList<Contract> setup;

  private ContractRequest request;

  private ContractResponse response;

  private Boolean enabled = true;

  private String[] filters;

  public Set<String> readTags() {
    List<String> arrayListTags = (ArrayList<String>) readInfo().get("tags");
    if (arrayListTags == null) {
      arrayListTags = new ArrayList<>();
      info.put("tags", arrayListTags);
    }
    return new HashSet<>(arrayListTags);
  }

  public void addTags(String[] newTags) {
    Set<String> tags = readTags();
    for (String newTag : newTags) {
      if (!newTag.trim().isEmpty()) {
        tags.add(newTag.toLowerCase(ENGLISH));
      }
    }
    info.put("tags", new ArrayList<>(tags));
  }

  public Map<String, Object> readInfo() {
    if(info == null) {
      info = new HashMap();
    }
    return info;
  }

  public int compareTo(Contract otherContract) {
    return this.request.getPath().compareTo(otherContract.request.getPath());
  }
}
