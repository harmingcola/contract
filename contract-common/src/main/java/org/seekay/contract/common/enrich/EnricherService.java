package org.seekay.contract.common.enrich;

import lombok.Setter;
import org.seekay.contract.common.enrich.enrichers.Enricher;
import org.seekay.contract.model.domain.Contract;

import java.util.Set;

@Setter
public class EnricherService implements Enricher {

    Set<Enricher> enrichers;

    public void enrichResponseBody(Contract contract) {
        if(contract != null) {
            String enrichedResponseBody = enrichResponseBody(contract.getResponse().getBody());
            contract.getResponse().setBody(enrichedResponseBody);
        }
    }

    public String enrichResponseBody(String body) {
		String enrichedBody = body;
        for(Enricher enricher : enrichers) {
			enrichedBody = enricher.enrichResponseBody(enrichedBody);
        }
        return enrichedBody;
    }
}
