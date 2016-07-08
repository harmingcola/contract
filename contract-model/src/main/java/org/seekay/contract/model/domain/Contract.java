package org.seekay.contract.model.domain;

import lombok.Data;

import java.util.*;

import static java.util.Locale.*;

@Data
public class Contract implements Comparable<Contract> {

  private Map<String, Object> info;

  private List<Map<String, Object>> parameters;

  private LinkedList<Contract> setup;

  private ContractRequest request;

  private ContractResponse response;

  private Boolean enabled = true;

  public Set<String> readTags() {
    List<String> arrayListTags = (ArrayList<String>) readInfo().get("tags");
    if (arrayListTags == null) {
      arrayListTags = new ArrayList<String>();
      info.put("tags", arrayListTags);
    }
    return new HashSet<String>(arrayListTags);
  }

  public void addTags(String[] newTags) {
    Set<String> tags = readTags();
    for (String newTag : newTags) {
      if (!newTag.trim().isEmpty()) {
        tags.add(newTag.toLowerCase(ENGLISH));
      }
    }
    info.put("tags", new ArrayList<String>(tags));
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
