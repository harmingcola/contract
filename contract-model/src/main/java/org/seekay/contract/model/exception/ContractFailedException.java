package org.seekay.contract.model.exception;

public class ContractFailedException extends RuntimeException {

  public ContractFailedException(String msg) {
    super(msg);
  }
}
