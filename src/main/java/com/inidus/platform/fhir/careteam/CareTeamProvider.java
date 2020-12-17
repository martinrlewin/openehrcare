package com.inidus.platform.fhir.careteam;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import com.fasterxml.jackson.databind.JsonNode;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CareTeam;
import org.hl7.fhir.r4.model.IdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component("CareTeamProvider")
public class CareTeamProvider implements IResourceProvider {

    private final CareTeamConverter converter = new CareTeamConverter();

    @Autowired
    private CareTeamConnector connector;

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return CareTeam.class;
    }

    @Read()
    public CareTeam getResourceById(@IdParam IdType id) throws IOException, FHIRException {
        JsonNode ehrJsonList = connector.getResourceById(id.getIdPart());

        if (null != ehrJsonList) {
            return converter.convertToCareTeam(ehrJsonList);
        } else {
            return null;
        }
    }

    @Search()
    public List<CareTeam> getAllResources() throws IOException, FHIRException {
        JsonNode ehrJsonList = connector.getAllResources();

        if (null != ehrJsonList) {
            return converter.convertToCareTeamList(ehrJsonList);
        } else {
            return null;
        }
    }

    @Search()
    public List<CareTeam> getFilteredResources(
            @OptionalParam(name = "patient.id") StringParam id,
            @OptionalParam(name = "patient.identifier") TokenParam identifier
    ) throws IOException, FHIRException {

        JsonNode ehrJsonList = connector.getFilteredCareTeams(id, identifier);

        if (null != ehrJsonList) {
            return converter.convertToCareTeamList(ehrJsonList);
        } else {
            return null;
        }
    }
}
