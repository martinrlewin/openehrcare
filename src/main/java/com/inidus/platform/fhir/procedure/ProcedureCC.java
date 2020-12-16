package com.inidus.platform.fhir.procedure;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.r4.model.Procedure;


/**
 * Subclass that represents the Care Connect Procedure Profile (currently unconstrained)
 */
@ResourceDef(name = "Procedure", profile = "https://www.hl7.org/fhir/procedure.html")
public class ProcedureCC extends Procedure {
}


