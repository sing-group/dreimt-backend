package org.sing_group.dreimt.rest.resource.execution;

import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.sing_group.dreimt.service.util.Sets.intersection;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.sing_group.dreimt.domain.dao.signature.SignatureListingOptions;
import org.sing_group.dreimt.domain.entities.execution.WorkEntity;
import org.sing_group.dreimt.rest.entity.execution.WorkData;
import org.sing_group.dreimt.rest.entity.query.cmap.DrugPrioritizationQueryParameters;
import org.sing_group.dreimt.rest.entity.query.jaccard.SignaturesComparisonQueryParameters;
import org.sing_group.dreimt.rest.filter.CrossDomain;
import org.sing_group.dreimt.rest.mapper.spi.execution.ExecutionMapper;
import org.sing_group.dreimt.rest.resource.route.BaseRestPathBuilder;
import org.sing_group.dreimt.rest.resource.spi.execution.QueryResource;
import org.sing_group.dreimt.service.query.cmap.DefaultCmapQueryOptions;
import org.sing_group.dreimt.service.query.jaccard.DefaultJaccardQueryOptions;
import org.sing_group.dreimt.service.spi.query.cmap.CmapQueryOptions;
import org.sing_group.dreimt.service.spi.query.cmap.CmapQueryService;
import org.sing_group.dreimt.service.spi.query.jaccard.JaccardQueryOptions;
import org.sing_group.dreimt.service.spi.query.jaccard.JaccardQueryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("query")
@Produces({
  APPLICATION_JSON
})
@Stateless
@CrossDomain()
@Api("query")
@ApiResponses({
  @ApiResponse(code = 200, message = "successful operation")
})
public class DefaultQueryResource implements QueryResource {

  @Inject
  private JaccardQueryService jaccardQueryService;

  @Inject
  private CmapQueryService cmapQueryService;

  @Inject
  private ExecutionMapper executionMapper;
  
  @Context
  private UriInfo uriInfo;
  
  @PostConstruct
  public void postConstruct() {
    final UriBuilder uriBuilder = this.uriInfo.getBaseUriBuilder();

    this.executionMapper.setUriBuilder(uriBuilder);
  }


  @POST
  @Produces(APPLICATION_JSON)
  @Path("signatures-comparison")
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(
    value = "Calculates the Jaccard indexes between the introduced gene lists and all signatures in the database. "
      + "The calculus are done asynchronously, thus this method returns a work-data instance with information about "
      + "the asynchronous task doing the calculations.",
    response = WorkData.class,
    code = 200
  )
  @Override
  public Response jaccardQuery(
    SignaturesComparisonQueryParameters jaccardQueryParameters
  ) {
    Set<String> upGenes =
      parseAndValidateJaccardQueryUpGenes(
        jaccardQueryParameters.getUpGenes(), jaccardQueryParameters.isOnlyUniverseGenes()
      );
    Set<String> downGenes =
      parseAndValidateJaccardQueryDownGenes(
        jaccardQueryParameters.getDownGenes(), jaccardQueryParameters.isOnlyUniverseGenes()
      );

    validateQueryGenes(upGenes, downGenes);

    final UriBuilder uriBuilder = this.uriInfo.getBaseUriBuilder();
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(uriBuilder);

    final Function<String, String> resultUriBuilder =
      id -> pathBuilder.jaccardResult(id).build().toString();

    SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        null, jaccardQueryParameters.getCellType1(), jaccardQueryParameters.getCellSubType1(),
        jaccardQueryParameters.getCellTypeOrSubType1(), jaccardQueryParameters.getCellType2(),
        jaccardQueryParameters.getCellSubType2(), jaccardQueryParameters.getCellTypeOrSubType2(),
        jaccardQueryParameters.getExperimentalDesign(), jaccardQueryParameters.getOrganism(),
        jaccardQueryParameters.getDisease(), jaccardQueryParameters.getSignatureSourceDb(),
        null, null, null, null
      );

    JaccardQueryOptions options =
      new DefaultJaccardQueryOptions(
        jaccardQueryParameters.getQueryTitle(), upGenes, downGenes, jaccardQueryParameters.isOnlyUniverseGenes(),
        resultUriBuilder, signatureListingOptions
      );

    final WorkEntity work = this.jaccardQueryService.jaccardQuery(options);

    return Response.ok(this.executionMapper.toWorkData(work)).build();
  }

  private Set<String> parseAndValidateJaccardQueryUpGenes(String[] upGenes, boolean onlyUniverseGenes) {
    return parseAndValidateUpGenes(
      upGenes,
      onlyUniverseGenes,
      this.jaccardQueryService::isValidGeneSet,
      this.jaccardQueryService::getMaximumGeneSetSize,
      this.jaccardQueryService::getMinimumGeneSetSize
    );
  }

  private Set<String> parseAndValidateJaccardQueryDownGenes(String[] downGenes, boolean onlyUniverseGenes) {
    return parseAndValidateDownGenes(
      downGenes,
      onlyUniverseGenes,
      this.jaccardQueryService::isValidGeneSet,
      this.jaccardQueryService::getMaximumGeneSetSize,
      this.jaccardQueryService::getMinimumGeneSetSize
    );
  }

  private static Set<String> parseAndValidateUpGenes(
    String[] upGenes,
    boolean onlyUniverseGenes,
    BiFunction<Set<String>, Boolean, Boolean> isValidGeneSet,
    Supplier<Integer> maximumGeneSetSizeSupplier,
    Supplier<Integer> minimumGeneSetSizeSupplier
  ) {
    Set<String> upGenesSet =
      upGenes == null ? emptySet() : new HashSet<String>(asList(upGenes));

    if (!upGenesSet.isEmpty() && !isValidGeneSet.apply(upGenesSet, onlyUniverseGenes)) {
      throw new IllegalArgumentException(
        "Invalid up (or geneset) genes list size. It must have at least " + minimumGeneSetSizeSupplier.get()
          + " and at most " + maximumGeneSetSizeSupplier.get() + " genes." + onlyUniverseGenesWarning(onlyUniverseGenes)
      );
    }

    return upGenesSet;
  }

  private static Set<String> parseAndValidateDownGenes(
    String[] downGenes,
    boolean onlyUniverseGenes,
    BiFunction<Set<String>, Boolean, Boolean> isValidGeneSet,
    Supplier<Integer> maximumGeneSetSizeSupplier,
    Supplier<Integer> minimumGeneSetSizeSupplier
  ) {
    Set<String> downGenesSet =
      downGenes == null ? emptySet() : new HashSet<String>(asList(downGenes));

    if (!downGenesSet.isEmpty() && !isValidGeneSet.apply(downGenesSet, onlyUniverseGenes)) {
      throw new IllegalArgumentException(
        "Invalid down genes list size. It must have at least " + minimumGeneSetSizeSupplier.get()
          + " and at most " + maximumGeneSetSizeSupplier.get() + " genes." + onlyUniverseGenesWarning(onlyUniverseGenes)
      );
    }

    return downGenesSet;
  }

  private static String onlyUniverseGenesWarning(boolean onlyUniverseGenes) {
    return onlyUniverseGenes ? " Note that genes must be in the DREIMT genes." : "";
  }

  @POST
  @Produces(APPLICATION_JSON)
  @Path("drug-prioritization")
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(
    value = "Makes the drug prioritization analysis for the gene lists introduced. "
      + "The calculus are done asynchronously, thus this method returns a work-data instance with information about "
      + "the asynchronous task doing the calculations.",
    response = WorkData.class,
    code = 200
  )
  @Override
  public Response cmapQuery(DrugPrioritizationQueryParameters cmapQueryParameters) {
    this.cmapQueryService.validateNumPerm(cmapQueryParameters.getNumPerm());
    Set<String> upGenes = parseAndValidateCmapQueryUpGenes(cmapQueryParameters.getUpGenes());
    Set<String> downGenes = parseAndValidateCmapQueryDownGenes(cmapQueryParameters.getDownGenes());

    validateQueryGenes(upGenes, downGenes);

    final UriBuilder uriBuilder = this.uriInfo.getBaseUriBuilder();
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(uriBuilder);

    final Function<String, String> resultUriBuilder =
      (downGenes.isEmpty() || upGenes.isEmpty()) ? id -> pathBuilder.cmapGeneSetSignatureResult(id).build().toString() : id -> pathBuilder.cmapUpDownSignatureResult(id).build().toString();

    CmapQueryOptions options =
      new DefaultCmapQueryOptions(
        cmapQueryParameters.getQueryTitle(), upGenes, downGenes, resultUriBuilder, cmapQueryParameters.getNumPerm(),
        cmapQueryParameters.getCaseType(), cmapQueryParameters.getReferenceType()
      );

    final WorkEntity work = this.cmapQueryService.cmapQuery(options);

    return Response.ok(this.executionMapper.toWorkData(work)).build();
  }

  private static void validateQueryGenes(Set<String> upGenes, Set<String> downGenes) {
    if (upGenes.isEmpty() && downGenes.isEmpty()) {
      throw new IllegalArgumentException("Both genes lists can't be empty. At least one gene list must be provided.");
    }

    if (intersection(upGenes, downGenes).size() > 0) {
      throw new IllegalArgumentException("Up and down gene lists cannot have genes in common");
    }
  }

  private Set<String> parseAndValidateCmapQueryUpGenes(String[] upGenes) {
    return parseAndValidateUpGenes(
      upGenes,
      true,
      (s, o) -> this.cmapQueryService.isValidGeneSet(s),
      this.cmapQueryService::getMaximumGeneSetSize,
      this.cmapQueryService::getMinimumGeneSetSize
    );
  }

  private Set<String> parseAndValidateCmapQueryDownGenes(String[] downGenes) {
    return parseAndValidateDownGenes(
      downGenes,
      true,
      (s, o) -> this.cmapQueryService.isValidGeneSet(s),
      this.cmapQueryService::getMaximumGeneSetSize,
      this.cmapQueryService::getMinimumGeneSetSize
    );
  }
}
