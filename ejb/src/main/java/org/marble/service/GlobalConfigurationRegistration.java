package org.marble.service;

import org.apache.log4j.Logger;
import org.marble.model.GlobalConfiguration;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class GlobalConfigurationRegistration {

    @Inject
    private Logger                     log;

    @Inject
    private EntityManager              em;

    @Inject
    private Event<GlobalConfiguration> globalPropertyEventSrc;

    public void register(GlobalConfiguration globalConfiguration) throws Exception {
        log.info("Updating " + globalConfiguration.getName());
        globalConfiguration = em.merge(globalConfiguration);
        globalPropertyEventSrc.fire(globalConfiguration);
    }

    public void delete(GlobalConfiguration globalConfiguration) throws Exception {
        log.info("Deleting " + globalConfiguration.getName());
        globalConfiguration = em.find(GlobalConfiguration.class, globalConfiguration.getName());

        em.remove(globalConfiguration);
        globalPropertyEventSrc.fire(globalConfiguration);
    }
}
