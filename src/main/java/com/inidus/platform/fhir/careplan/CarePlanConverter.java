package com.inidus.platform.fhir.careplan;

import com.fasterxml.jackson.databind.JsonNode;
import com.inidus.platform.fhir.openehr.OpenEHRConverter;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CarePlanConverter extends OpenEHRConverter {

    /**
     * Converts the given json coming from openEHR into 1 {@link Condition} resource.
     * Duplicates in the json will be merged.
     *
     * @param ehrJson is the array contained inside the "resultSet" section
     */
    public Bundle convertToCarePlan(JsonNode ehrJson) throws FHIRException {
        List<Bundle> list = convertToCarePlanList(ehrJson);
        return list.get(0);
    }

    /**
     * Converts the given json coming from openEHR into a list of {@link Procedure} resources.
     * Duplicates in the json will be merged.
     *
     * @param ehrJson is the array contained inside the AQL "resultSet" section
     */
    public List<Bundle> convertToCarePlanList(JsonNode ehrJson) throws FHIRException {
        List<Bundle> carePlans = new ArrayList<>();
        Iterator<JsonNode> it = ehrJson.elements();
        while (it.hasNext()) {
            Bundle resource = createCarePlanResource(it.next());
            carePlans.add(resource);
        }
        return carePlans;
    }

    private Bundle createCarePlanResource(JsonNode ehrJson) throws FHIRException {

        Bundle retVal = new Bundle();

        retVal.setId(convertResourceId(ehrJson));
    //    retVal.setSubject(convertPatientReference(ehrJson));

        return retVal;
    }
}
