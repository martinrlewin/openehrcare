package com.inidus.platform.fhir.dnrflag;

import com.fasterxml.jackson.databind.JsonNode;
import com.inidus.platform.fhir.openehr.OpenEHRConverter;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class DNRFlagConverter extends OpenEHRConverter {

    /**
     * Converts the given json coming from openEHR into 1 {@link Condition} resource.
     * Duplicates in the json will be merged.
     *
     * @param ehrJson is the array contained inside the "resultSet" section
     */
    public Flag convertToFlag(JsonNode ehrJson) throws FHIRException {
        List<Flag> list = convertToFlagList(ehrJson);
        return list.get(0);
    }

    /**
     * Converts the given json coming from openEHR into a list of {@link Procedure} resources.
     * Duplicates in the json will be merged.
     *
     * @param ehrJson is the array contained inside the AQL "resultSet" section
     */
    public List<Flag> convertToFlagList(JsonNode ehrJson) throws FHIRException {
        List<Flag> flags = new ArrayList<>();
        Iterator<JsonNode> it = ehrJson.elements();
        while (it.hasNext()) {
            Flag resource = createFlagResource(it.next());
            flags.add(resource);
        }
        return flags;
    }

    private Flag createFlagResource(JsonNode ehrJson) throws FHIRException {

        Flag retVal = new Flag();

        retVal.setId(convertResourceId(ehrJson));
        retVal.setSubject(convertPatientReference(ehrJson));

        return retVal;
    }
}
