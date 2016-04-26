package org.seekay.contract.model.domain;

import lombok.Data;

import java.util.*;

@Data
public class Contract implements Comparable<Contract> {

	private Map<String, Object> info;

    private ContractRequest request;

    private ContractResponse response;

	public Set<String> readTags() {
		if(info == null) {
			info = new HashMap<String, Object>();
		}

		ArrayList<String> arrayListTags = (ArrayList<String>) info.get("tags");
		if(arrayListTags == null) {
			arrayListTags = new ArrayList<String>();
			info.put("tags", new ArrayList<String>());
		}
		return new HashSet<String>(arrayListTags);
	}

	public void addTags(String[] newTags) {
		Set<String> tags = readTags();
		for(String newTag : newTags) {
			if(!newTag.trim().isEmpty()) {
				tags.add(newTag.toLowerCase());
			}
		}
		info.put("tags", new ArrayList<String>(tags));
	}

	public void addInfo(String key, String value) {
		if(info == null) {
			info = new HashMap<String, Object>();
		}
		info.put(key, value);
	}

	public int compareTo(Contract otherContract) {
		return this.request.getPath().compareTo(otherContract.request.getPath());
	}
}
