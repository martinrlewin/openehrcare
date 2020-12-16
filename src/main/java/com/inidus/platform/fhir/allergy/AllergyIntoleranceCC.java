package com.inidus.platform.fhir.allergy;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.r4.model.AllergyIntolerance;


/**
 * Subclass that represents the Care Conect Profile
 */
@ResourceDef(name = "AllergyIntolerance", profile = "https://www.hl7.org/fhir/allergyintolerance.html")
public class AllergyIntoleranceCC extends AllergyIntolerance {
}
