package com.inidus.platform.fhir.questionnaireresponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.inidus.platform.fhir.openehr.OpenEHRConverter;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.model.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class QuestionnaireResponseConverter extends OpenEHRConverter {

    /**
     * Converts the given json coming from openEHR into 1 {@link Condition} resource.
     * Duplicates in the json will be merged.
     *
     * @param ehrJson is the array contained inside the "resultSet" section
     */
    public QuestionnaireResponse convertToQuestionnaireResponse(JsonNode ehrJson) throws FHIRException {
        List<QuestionnaireResponse> list = convertToQuestionnaireResponseList(ehrJson);
        return list.get(0);
    }

    /**
     * Converts the given json coming from openEHR into a list of {@link Procedure} resources.
     * Duplicates in the json will be merged.
     *
     * @param ehrJson is the array contained inside the AQL "resultSet" section
     */
    public List<QuestionnaireResponse> convertToQuestionnaireResponseList(JsonNode ehrJson) throws FHIRException {
        List<QuestionnaireResponse> responses = new ArrayList<>();
        Iterator<JsonNode> it = ehrJson.elements();
        while (it.hasNext()) {
            QuestionnaireResponse resource = createQuestionnaireResponseResource(it.next());
            responses.add(resource);
        }
        return responses;
    }

    private QuestionnaireResponse createQuestionnaireResponseResource(JsonNode ehrJson) throws FHIRException {

        QuestionnaireResponse retVal = new QuestionnaireResponse();

        retVal.setId(convertResourceId(ehrJson));
        retVal.setSubject(convertPatientReference(ehrJson));

        QuestionnaireResponse.QuestionnaireResponseItemComponent topLevelItem = new QuestionnaireResponse.QuestionnaireResponseItemComponent();
        topLevelItem.setText("About Me");

        JsonNode valueNode = ehrJson.get("s3_what_i_value");
        if (valueNode != null) {
            QuestionnaireResponse.QuestionnaireResponseItemComponent questionItem = new QuestionnaireResponse.QuestionnaireResponseItemComponent();
            questionItem.setText("What do I value?");
            questionItem.addAnswer();
            questionItem.getAnswer().get(0).setValue(new StringType(valueNode.textValue()));
            topLevelItem.addItem(questionItem);
        }

        JsonNode fearNode = ehrJson.get("s3_what_i_fear");
        if (fearNode != null) {
            QuestionnaireResponse.QuestionnaireResponseItemComponent questionItem = new QuestionnaireResponse.QuestionnaireResponseItemComponent();
            questionItem.setText("What do I fear?");
            questionItem.addAnswer();
            questionItem.getAnswer().get(0).setValue(new StringType(fearNode.textValue()));
            topLevelItem.addItem(questionItem);
        }
        // questionnaire
        // two items -text question,   answer valueString
        // - what I value
        // - what I fear
//s4 flag
//        s3 - about me


        retVal.addItem(topLevelItem);

        if (valueNode != null || fearNode != null) {
            retVal.setStatus(QuestionnaireResponse.QuestionnaireResponseStatus.COMPLETED);
        } else {
            retVal.setStatus(QuestionnaireResponse.QuestionnaireResponseStatus.STOPPED);
        }



        return retVal;
    }
}
