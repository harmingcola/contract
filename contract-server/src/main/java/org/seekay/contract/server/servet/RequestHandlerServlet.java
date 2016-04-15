package org.seekay.contract.server.servet;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.seekay.contract.model.domain.Contract;
import org.seekay.contract.model.domain.ContractRequest;
import org.seekay.contract.server.ApplicationContext;
import org.seekay.contract.server.service.MatchingService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.seekay.contract.server.util.RequestReader.from;
import static org.seekay.contract.server.util.ResponseWriter.to;

public class RequestHandlerServlet extends HttpServlet {

  public static final String NO_MATCHING_PACTS_FOUND = "No Matching Contracts Found";

  private MatchingService matchingService;


    @Override
    public void init() throws ServletException {
        matchingService = ApplicationContext.matchingService();
    }

    @Override
    protected void doGet(HttpServletRequest httpRequest, HttpServletResponse httpsResponse) throws ServletException, IOException {
        ContractRequest contractRequest = from(httpRequest).toContractRequest();
        Contract contract = matchingService.matchGetRequest(contractRequest);
        writeResponse(httpsResponse, contract);
    }

    @Override
    protected void doPost(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ServletException, IOException {
      ContractRequest contractRequest = from(httpRequest).toContractRequest();
      Contract contract = matchingService.matchPostRequest(contractRequest);
      writeResponse(httpResponse, contract);
    }

    @Override
    protected void doPut(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ServletException, IOException {
        ContractRequest contractRequest = from(httpRequest).toContractRequest();
        Contract contract = matchingService.matchPutRequest(contractRequest);
        writeResponse(httpResponse, contract);
    }

    @Override
    protected void doDelete(HttpServletRequest httpRequest, HttpServletResponse httpsResponse) throws ServletException, IOException {
        ContractRequest contractRequest = from(httpRequest).toContractRequest();
        Contract contract = matchingService.matchDeleteRequest(contractRequest);
        writeResponse(httpsResponse, contract);
    }

    private void writeResponse(HttpServletResponse httpResponse, Contract contract) throws JsonProcessingException {
        if(contract != null && contract.getResponse() != null) {
            to(httpResponse)
                    .status(contract.getResponse().getStatus())
                    .headers(contract.getResponse().getHeaders())
                    .write(contract.getResponse().getBody());
        } else {
            to(httpResponse).notFound().write(NO_MATCHING_PACTS_FOUND);
        }
    }
}
