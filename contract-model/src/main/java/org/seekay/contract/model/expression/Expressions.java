package org.seekay.contract.model.expression;

public class Expressions {

  public static final String CONTRACT_EXPRESSION = ".*\\$\\{contract\\..*?\\}.*";

  public static final String ANY_STRING = "\\$\\{contract\\.anyString\\}";

  public static final String ANY_NUMBER = "\\$\\{contract\\.anyNumber\\}"; //-?(?:0|[1-9]\d*)(?:\.\d+)?(?:[eE][+-]?\d+)?

  public static final String TIMESTAMP = "\\$\\{contract.timestamp\\}";

  public static final String VARIABLE = "(\\$\\{contract\\.var\\..*?\\.)(.*?)(\\})";

  public static final String NUMBER_VARIABLE = "(\\$\\{contract\\.var\\.number\\.)(.*?)(\\})";

  public static final String POSITIVE_NUMBER_VARIABLE = "(\\$\\{contract\\.var\\.positiveNumber\\.)(.*?)(\\})";

}
