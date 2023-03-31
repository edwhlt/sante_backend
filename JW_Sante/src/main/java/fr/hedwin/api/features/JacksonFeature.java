package fr.hedwin.api.features;

import jakarta.ws.rs.core.Feature;
import jakarta.ws.rs.core.FeatureContext;

public class JacksonFeature implements Feature {

    @Override
    public boolean configure(FeatureContext featureContext) {
        featureContext.register(CustomJsonProvider.class);
        return true;
    }
}
