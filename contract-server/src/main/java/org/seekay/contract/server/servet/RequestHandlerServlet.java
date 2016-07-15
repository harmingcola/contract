//NOSONAR
package org.seekay.contract.server.servet;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.seekay.contract.common.enrich.EnricherService;
import org.seekay.contract.common.match.MatchingService;
import org.seekay.contract.common.service.ContractService;
import org.seekay.contract.model.domain.Contract;
import org.seekay.contract.model.domain.ContractRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.seekay.contract.common.ApplicationContext.*;
import static org.seekay.contract.server.util.RequestReader.from;
import static org.seekay.contract.server.util.ResponseWriter.to;

public class RequestHandlerServlet extends HttpServlet {

    private MatchingService matchingService;
    private EnricherService enricherService;
    private ContractService contractService;

    @Override
    public void init() throws ServletException {
        matchingService = matchingService();
        enricherService = enricherService();
        contractService = contractService();
    }

    @Override
    protected void doGet(HttpServletRequest httpRequest, HttpServletResponse httpsResponse) throws ServletException, IOException {
        ContractRequest contractRequest = from(httpRequest).toContractRequest();
        Contract contract = matchingService.matchGetRequest(contractRequest);
        enableFilters(contract);
        contract = enricherService.enrichResponse(contract);
        writeResponse(httpsResponse, contract);
    }

    @Override
    protected void doPost(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ServletException, IOException {
        ContractRequest contractRequest = from(httpRequest).toContractRequest();
        Contract contract = matchingService.matchPostRequest(contractRequest);
        contract = enricherService.enrichResponse(contract);
        writeResponse(httpResponse, contract);
    }

    @Override
    protected void doPut(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ServletException, IOException {
        ContractRequest contractRequest = from(httpRequest).toContractRequest();
        Contract contract = matchingService.matchPutRequest(contractRequest);
        contract = enricherService.enrichResponse(contract);
        writeResponse(httpResponse, contract);
    }

    @Override
    protected void doDelete(HttpServletRequest httpRequest, HttpServletResponse httpsResponse) throws ServletException, IOException {
        ContractRequest contractRequest = from(httpRequest).toContractRequest();
        Contract contract = matchingService.matchDeleteRequest(contractRequest);
        contract = enricherService.enrichResponse(contract);
        writeResponse(httpsResponse, contract);
    }

    private void writeResponse(HttpServletResponse httpResponse, Contract contract) throws JsonProcessingException {
        if (contract != null && contract.getResponse() != null) {
            to(httpResponse)
                    .status(contract.getResponse().getStatus())
                    .headers(contract.getResponse().getHeaders())
                    .write(contract.getResponse().getBody());
        } else {
            to(httpResponse).notFound();
        }
    }

    private void enableFilters(Contract contract) {
        if(contract != null) {
            contractService.enableFilters(contract.getFilters());
        }
    }
}
