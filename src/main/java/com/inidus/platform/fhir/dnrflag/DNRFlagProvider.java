package com.inidus.platform.fhir.dnrflag;

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
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.Flag;
import org.hl7.fhir.r4.model.IdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component("DNRFlagProvider")
public class DNRFlagProvider implements IResourceProvider {

    private final DNRFlagConverter converter = new DNRFlagConverter();

    @Autowired
    private DNRFlagConnector connector;

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return Flag.class;
    }

    @Read()
    public Flag getResourceById(@IdParam IdType id) throws IOException, FHIRException {
        JsonNode ehrJsonList = connector.getResourceById(id.getIdPart());

        if (null != ehrJsonList) {
            return converter.convertToFlag(ehrJsonList);
        } else {
            return null;
        }
    }

    @Search()
    public List<Flag> getAllResources() throws IOException, FHIRException {
        JsonNode ehrJsonList = connector.getAllResources();

        if (null != ehrJsonList) {
            return converter.convertToFlagList(ehrJsonList);
        } else {
            return null;
        }
    }

    @Search()
    public List<Flag> getFilteredResources(
            @OptionalParam(name = "patient.id") StringParam id,
            @OptionalParam(name = "patient.identifier") TokenParam identifier
    ) throws IOException, FHIRException {

        JsonNode ehrJsonList = connector.getFilteredCarePlans(id, identifier);

        if (null != ehrJsonList) {
            return converter.convertToFlagList(ehrJsonList);
        } else {
            return null;
        }
    }
}
