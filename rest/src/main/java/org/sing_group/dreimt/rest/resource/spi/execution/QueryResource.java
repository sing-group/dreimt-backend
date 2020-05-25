package org.sing_group.dreimt.rest.resource.spi.execution;

import javax.ejb.Local;
import javax.ws.rs.core.Response;

import org.sing_group.dreimt.rest.entity.query.cmap.DrugPrioritizationQueryParameters;
import org.sing_group.dreimt.rest.entity.query.jaccard.SignaturesComparisonQueryParameters;

@Local
public interface QueryResource {
  Response jaccardQuery(SignaturesComparisonQueryParameters jaccardQueryParameters);

  Response cmapQuery(DrugPrioritizationQueryParameters cmapQueryParameters);
}
