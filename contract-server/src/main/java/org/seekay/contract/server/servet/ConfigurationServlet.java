package org.seekay.contract.server.servet;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.seekay.contract.model.domain.Contract;
import org.seekay.contract.common.builder.ContractBuilder;
import org.seekay.contract.common.service.ContractService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

import static org.seekay.contract.common.ApplicationContext.*;
import static org.seekay.contract.server.util.RequestReader.from;
import static org.seekay.contract.server.util.ResponseWriter.to;

@Slf4j
public class ConfigurationServlet extends HttpServlet {

  public static final String NO_PACTS_HAVE_BEEN_DEFINED = "No contracts have been defined";

  private ContractService contractService;
    private ContractBuilder contractBuilder;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        contractService = contractService();
        contractBuilder = contractBuilder();
        objectMapper = objectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ServletException, IOException {
        log.info("Configuration request received");
        Set<Contract> contracts = contractService.read();
        if(!contracts.isEmpty()) {
            String responseBody = objectMapper.writeValueAsString(contracts);
            to(httpResponse).ok().write(responseBody);
        } else {
            to(httpResponse).ok().write(NO_PACTS_HAVE_BEEN_DEFINED);
        }
    }

    @Override
    protected void doPost(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ServletException, IOException {
        String contractDefinition = from(httpRequest).readBody();
        Contract contract = contractBuilder.fromJson(contractDefinition);
        contractService.create(contract);
        to(httpResponse).created().write(objectMapper.writeValueAsString(contract));
        //log.info("Contract successfully added to server {}", prettyPrint(contract, objectMapper));
    }
}
