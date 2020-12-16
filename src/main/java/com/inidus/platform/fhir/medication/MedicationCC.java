package com.inidus.platform.fhir.medication;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.r4.model.Medication;


/**
 * Subclass that represents the Care Connect Medication Profile (currently unconstrained)
 */
@ResourceDef(name = "Medication", profile = "https://www.hl7.org/fhir/medication.html")
public class MedicationCC extends Medication {
}


