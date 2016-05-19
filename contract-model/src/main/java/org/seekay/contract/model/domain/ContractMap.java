package org.seekay.contract.model.domain;

import lombok.Data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/*
 * Mirror of Contract, allows pre-processing of contracts before being marshalled into an object
 */
@Data
public class ContractMap  {

  private HashMap<String, Object> info;

  private LinkedList<Map<String, Object>> parameters;

  private LinkedList<Map<String, Object>> setup;

  private HashMap<String, Object> request;

  private HashMap<String, Object> response;


}
