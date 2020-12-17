package com.inidus.platform.fhir.careteam;

import com.fasterxml.jackson.databind.JsonNode;
import com.inidus.platform.fhir.openehr.OpenEHRConverter;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.model.CareTeam;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Procedure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CareTeamConverter extends OpenEHRConverter {

    /**
     * Converts the given json coming from openEHR into 1 {@link Condition} resource.
     * Duplicates in the json will be merged.
     *
     * @param ehrJson is the array contained inside the "resultSet" section
     */
    public CareTeam convertToCareTeam(JsonNode ehrJson) throws FHIRException {
        List<CareTeam> list = convertToCareTeamList(ehrJson);
        return list.get(0);
    }

    /**
     * Converts the given json coming from openEHR into a list of {@link Procedure} resources.
     * Duplicates in the json will be merged.
     *
     * @param ehrJson is the array contained inside the AQL "resultSet" section
     */
    public List<CareTeam> convertToCareTeamList(JsonNode ehrJson) throws FHIRException {
        List<CareTeam> careTeams = new ArrayList<>();
        Iterator<JsonNode> it = ehrJson.elements();
        while (it.hasNext()) {
            CareTeam resource = createCareTeamResource(it.next());
            careTeams.add(resource);
        }
        return careTeams;
    }

    private CareTeam createCareTeamResource(JsonNode ehrJson) throws FHIRException {

        CareTeam retVal = new CareTeam();

        retVal.setId(convertResourceId(ehrJson));
      //  retVal.setSubject(convertPatientReference(ehrJson));

        return retVal;
    }
}
