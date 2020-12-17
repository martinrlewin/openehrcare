package com.inidus.platform.fhir.careteam;

import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import com.fasterxml.jackson.databind.JsonNode;
import com.inidus.platform.fhir.openehr.OpenEhrConnector;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Connects to an openEHR backend and returns selected Medication Statement data
 */
@ConfigurationProperties(prefix = "cdr-connector", ignoreUnknownFields = false)
@Service
public class CareTeamConnector extends OpenEhrConnector {

    protected String getAQLQuery() {
        // TODO: replace with real content

        return  "SELECT c/context/start_time/value as compositionStartTime,\n" +
        "e/ehr_id/value as ehrId,\n" +
        "e/ehr_status/subject/external_ref/id/value as subjectId,\n" +
        "e/ehr_status/subject/external_ref/namespace as subjectNamespace,\n" +
        "c/uid/value as compositionId,\n" +
        "h/items[at0001]/items[openEHR-EHR-EVALUATION.clinical_synopsis.v1,'ReSPECT Summary']/data[at0001]/items[at0002,'Narrative Summary']/value/value as s2_summary,\n" +
        "h/items[at0001]/items[openEHR-EHR-SECTION.adhoc.v1,'Details of other relevant care planning documents and where to find them']/items[openEHR-EHR-EVALUATION.advance_care_directive.v1,'Advance planning document']/data[at0001]/items[at0006]/value/value as s2_care_planning,\n" +
        "h/items[at0001]/items[openEHR-EHR-SECTION.ehr_reference.v0,'Legal welfare proxies']/items[openEHR-EHR-ADMIN_ENTRY.legal_authority.v0,'Legal welfare proxy in place']/data[at0001]/items[at0004]/value/value as s2_legal_proxy,\n" +
        "h/items[at0001]/items[openEHR-EHR-SECTION.ehr_reference.v0,'Legal welfare proxies']/items[openEHR-EHR-ADMIN_ENTRY.legal_authority.v0,'Legal welfare proxy in place']/data[at0001]/items[at0004]/value/value as s2_legal_proxy_status,\n" +
        "h/items[at0006]/items[openEHR-EHR-EVALUATION.about_me.v0,'What matters to me']/data[at0001]/items[at0002,'What I most value']/value/value as s3_what_i_value,\n" +
        "h/items[at0006]/items[openEHR-EHR-EVALUATION.about_me.v0,'What matters to me']/data[at0001]/items[at0002,'What I most fear / wish to avoid']/value/value as s3_what_i_fear,\n" +
        "h/items[at0007]/items[openEHR-EHR-EVALUATION.recommendation.v1]/data[at0001]/items[at0002,'Clinical focus']/value/value as s4_recommended_clinical_focus,\n" +
        "h/items[at0007]/items[openEHR-EHR-EVALUATION.recommendation.v1]/data[at0001]/items[at0003,'Clinical guidance on interventions']/value/value as s4_recommended_clinical_guidance,\n" +
        "h/items[at0007]/items[openEHR-EHR-EVALUATION.cpr_decision_uk.v0]/data[at0001]/items[at0003]/value/value as s4_cpr_decision,\n" +
        "h/items[at0007]/items[openEHR-EHR-EVALUATION.cpr_decision_uk.v0]/data[at0001]/items[at0002]/value/value as s4_cpr_date,\n" +
        "h/items[at0008]/items[openEHR-EHR-EVALUATION.mental_capacity.v0]/data[at0001]/items[at0009]/value/value as s5_mental_decision,\n" +
        "h/items[at0008]/items[openEHR-EHR-EVALUATION.mental_capacity.v0]/data[at0001]/items[at0002,'Does the person have capacity to participate in making recommendations on this plan?']/value/value as s5_mental_has_capacity,\n" +
        "h/items[at0008]/items[openEHR-EHR-EVALUATION.mental_capacity.v0]/data[at0001]/items[at0006,'If no, in what way does this person lack capacity?']/value/value as s5_mental_if_no,\n" +
        "SQUASH(h/items[at0009]/items[openEHR-EHR-ADMIN_ENTRY.respect_involvement.v0]/data[at0001]/items[at0012]/items[at0002]/value/value) as s6_involvement,\n" +
        "h/items[at0009]/items[openEHR-EHR-ADMIN_ENTRY.respect_involvement.v0]/data[at0001]/items[at0012]/items[at0007]/value/value as s6_option_d,\n" +
        "h/items[at0010]/items[openEHR-EHR-ACTION.service.v0,'Clinician signature']/description[at0001]/items[at0011]/value/value as s7_clinician_signatures,\n" +
        "h/items[at0011]/items[openEHR-EHR-ADMIN_ENTRY.care_team.v0,'Emergency contacts']/data[at0001]/items[openEHR-EHR-CLUSTER.care_team.v0]/items[at0018]/value/value as s8_care_team_name,\n" +
        "SQUASH(h/items[at0011]/items[openEHR-EHR-ADMIN_ENTRY.care_team.v0,'Emergency contacts']/data[at0001]/items[openEHR-EHR-CLUSTER.care_team.v0]/items[at0021]/items[at0022]) as s8_care_team_participant,\n" +
        "h/items[at0012]/items[openEHR-EHR-ACTION.service.v0,'Review of plan']/description[at0001]/items[at0011]/value/value as s9_service_name\n" +
        "FROM EHR e \n" +
        "CONTAINS COMPOSITION c \n" +
        "CONTAINS EVALUATION h[openEHR-EHR-SECTION.respect_headings.v0] \n ";
    }

    public String getAQLWhere() {
        return "";
    }

    public String getAQLOrderBy() {
        return "ORDER BY compositionStartTime DESC \n" +
                "OFFSET 0 LIMIT 1000";
    }

    public JsonNode getFilteredCareTeams(
            StringParam patientId,
            TokenParam patientIdentifier
    ) throws IOException {

        String filter = "";

        // patient identifier provided
        if (null != patientIdentifier) {
            if (filter.isEmpty()) filter += " where ";
            filter += getPatientIdentifierFilterAql(patientIdentifier);
        }

        // patient id provided
        if (null != patientId) {
            if (filter.isEmpty()) {
                filter += " where ";
            } else {
                filter += " and ";
            }
            filter += getPatientIdFilterAql(patientId);
        }

        return getEhrJson(getAQLQuery() + getAQLWhere() + filter + getAQLOrderBy());
    }
}
