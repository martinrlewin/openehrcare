package com.inidus.platform.fhir.questionnaireresponse;

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
import org.hl7.fhir.r4.model.Flag;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.QuestionnaireResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component("QuestionnaireResponseProvider")
public class QuestionnaireResponseProvider implements IResourceProvider {

    private final QuestionnaireResponseConverter converter = new QuestionnaireResponseConverter();

    @Autowired
    private QuestionnaireResponseConnector connector;

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return QuestionnaireResponse.class;
    }

    @Read()
    public QuestionnaireResponse getResourceById(@IdParam IdType id) throws IOException, FHIRException {
        JsonNode ehrJsonList = connector.getResourceById(id.getIdPart());

        if (null != ehrJsonList) {
            return converter.convertToQuestionnaireResponse(ehrJsonList);
        } else {
            return null;
        }
    }

    @Search()
    public List<QuestionnaireResponse> getAllResources() throws IOException, FHIRException {
        JsonNode ehrJsonList = connector.getAllResources();

        if (null != ehrJsonList) {
            return converter.convertToQuestionnaireResponseList(ehrJsonList);
        } else {
            return null;
        }
    }

    @Search()
    public List<QuestionnaireResponse> getFilteredResources(
            @OptionalParam(name = "patient.id") StringParam id,
            @OptionalParam(name = "patient.identifier") TokenParam identifier
    ) throws IOException, FHIRException {

        JsonNode ehrJsonList = connector.getFilteredQuestionnaireResponses(id, identifier);

        if (null != ehrJsonList) {
            return converter.convertToQuestionnaireResponseList(ehrJsonList);
        } else {
            return null;
        }
    }
}
