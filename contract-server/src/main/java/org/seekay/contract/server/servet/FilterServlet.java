//NOSONAR
package org.seekay.contract.server.servet;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.seekay.contract.common.service.ContractService;
import org.seekay.contract.model.domain.Contract;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.util.Arrays.asList;
import static org.seekay.contract.common.ApplicationContext.contractService;
import static org.seekay.contract.common.ApplicationContext.objectMapper;
import static org.seekay.contract.server.util.RequestReader.from;

@Slf4j
public class FilterServlet extends HttpServlet {

	private ContractService contractService;
	private ObjectMapper objectMapper;

	@Override
	public void init() throws ServletException {
		contractService = contractService();
		objectMapper = objectMapper();
	}


	@Override
	protected void doPost(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ServletException, IOException {
		String filterDefinition = from(httpRequest).readBody();
		String[] filters = objectMapper.readValue(filterDefinition, String[].class);
    log.info("Disabling contracts not matching {}", filters);
		for(String tag : filters) {
			for(Contract contract : contractService.readAll()) {
				if(!contract.readTags().containsAll(asList(tag))) {
					contract.setEnabled(false);
				}
			}
		}
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    log.info("Enabling all contracts");
		for(Contract contract : contractService.readAll()) {
			contract.setEnabled(true);
		}
	}
}
