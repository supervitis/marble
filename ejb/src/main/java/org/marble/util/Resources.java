package org.marble.util;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * This class uses CDI to alias Java EE resources, such as the persistence context, to CDI beans
 * 
 * <p>
 * Example injection on a managed bean field:
 * </p>
 * 
 * <pre>
 * &#064;Inject
 * private EntityManager em;
 * </pre>
 */
public class Resources {
    @Produces
    @PersistenceContext
    private EntityManager em;

    @Produces
    public org.apache.log4j.Logger produceLog(InjectionPoint injectionPoint) {
        return org.apache.log4j.Logger.getLogger(injectionPoint.getMember().getDeclaringClass()
                .getName());
    }
}
