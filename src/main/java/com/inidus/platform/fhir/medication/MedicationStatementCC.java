package com.inidus.platform.fhir.medication;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.r4.model.MedicationStatement;


/**
 * Subclass that represents the Care Conect Profile
 */
@ResourceDef(name = "MedicationStatement", profile = "https://www.hl7.org/fhir/medicationstatement.html")
public class MedicationStatementCC extends MedicationStatement {

}

