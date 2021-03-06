package com.inidus.platform.fhir.careplan;

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
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.IdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component("CarePlanProvider")
public class CarePlanProvider {

    private final CarePlanConverter converter = new CarePlanConverter();

    @Autowired
    private CarePlanConnector connector;

    @Read(type=CarePlan.class)
    public Bundle getResourceById(@IdParam IdType id) throws IOException, FHIRException {
        JsonNode ehrJsonList = connector.getResourceById(id.getIdPart());

        if (null != ehrJsonList) {
            return converter.convertToCarePlan(ehrJsonList);
        } else {
            return null;
        }
    }

    @Search(type=CarePlan.class)
    public List<Bundle> getAllResources() throws IOException, FHIRException {
        JsonNode ehrJsonList = connector.getAllResources();

        if (null != ehrJsonList) {
            return converter.convertToCarePlanList(ehrJsonList);
        } else {
            return null;
        }
    }

    @Search()
    public List<Bundle> getFilteredResources(
            @OptionalParam(name = "patient.id") StringParam id,
            @OptionalParam(name = "patient.identifier") TokenParam identifier
    ) throws IOException, FHIRException {

        JsonNode ehrJsonList = connector.getFilteredCarePlans(id, identifier);

        if (null != ehrJsonList) {
            return converter.convertToCarePlanList(ehrJsonList);
        } else {
            return null;
        }
    }
}
