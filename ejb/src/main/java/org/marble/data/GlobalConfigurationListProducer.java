package org.marble.data;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import java.util.List;

import org.marble.model.GlobalConfiguration;

@RequestScoped
public class GlobalConfigurationListProducer {

    @Inject
    private GlobalConfigurationRepository globalConfigurationRepository;

    private List<GlobalConfiguration> globalConfiguration;

    // @Named provides access the return value via the EL variable name "members" in the UI (e.g.,
    // Facelets or JSP view)
    @Produces
    @Named
    public List<GlobalConfiguration> getGlobalConfiguration() {
        return globalConfiguration;
    }
    
    public void onGlobalConfigurationListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final GlobalConfiguration globalConfiguration) {
        retrieveAllGlobalConfigurationOrderedByName();
    }

    @PostConstruct
    public void retrieveAllGlobalConfigurationOrderedByName() {
        globalConfiguration = globalConfigurationRepository.getAllOrderedByName();
    }
}
