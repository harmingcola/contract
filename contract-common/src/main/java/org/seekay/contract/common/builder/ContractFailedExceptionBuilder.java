package org.seekay.contract.common.builder;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.seekay.contract.model.domain.Contract;
import org.seekay.contract.model.domain.ContractResponse;
import org.seekay.contract.model.exception.ContractFailedException;

import static org.seekay.contract.model.tools.PrintTools.prettyPrint;

public class ContractFailedExceptionBuilder extends RuntimeException {

  private static ObjectMapper objectMapper = new ObjectMapper();

  private String contractText = "";
  private String actualResponseText = "";
  private String statusCodeText = "";
  private String errorMessage = "";


  private ContractFailedExceptionBuilder(String contractText) {
    this.contractText = contractText;
  }

  public ContractFailedException build() {
    StringBuilder builder = new StringBuilder();
    if (!errorMessage.isEmpty()) {
      builder.append("\n" + errorMessage);
    }
    if (!statusCodeText.isEmpty()) {
      builder.append("\n" + statusCodeText);
    }
    builder.append("\nContract under test :\n");
    builder.append(contractText);
    builder.append("\nActual response :\n");
    builder.append(actualResponseText);
    return new ContractFailedException(builder.toString());
  }

  public static ContractFailedExceptionBuilder expectedContract(Contract contract) {
    String contractText = prettyPrint(contract, objectMapper);
    return new ContractFailedExceptionBuilder(contractText);
  }

  public ContractFailedExceptionBuilder actualResponse(ContractResponse actualResponse) {
    this.actualResponseText = prettyPrint(actualResponse, objectMapper);
    return this;
  }

  public ContractFailedExceptionBuilder statusCodes(String contractStatus, String actualStatus) {
    this.statusCodeText = "Status codes are expected to match, contract : " + contractStatus + ", actual : " + actualStatus;
    return this;
  }

  public ContractFailedExceptionBuilder errorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
    return this;
  }
}
