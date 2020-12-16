package com.inidus.platform.fhir.condition;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.util.ElementUtil;
import org.hl7.fhir.r4.model.Condition;


/**
 * Subclass that represents the Care Conect Profile
 */
@ResourceDef(name = "Condition", profile = "https://www.hl7.org/fhir/condition.html")
public class ConditionCC extends Condition {
    @Child(name = "episode")
    @Extension(url = "https://www.hl7.org/fhir/extension-workflow-episodeofcare.html", definedLocally = false, isModifier = false)
    @Description(shortDefinition = "The episodicity status of a condition")
    protected EpisodeExtension episodeExtension;

    EpisodeExtension getEpisodeExtension() {
        return episodeExtension;
    }

    void setEpisodeExtension(EpisodeExtension episodeExtension) {
        this.episodeExtension = episodeExtension;
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty() && ElementUtil.isEmpty(episodeExtension);
    }
}

