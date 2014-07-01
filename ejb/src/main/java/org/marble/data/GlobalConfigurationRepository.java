package org.marble.data;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import java.util.List;

import org.marble.model.GlobalConfiguration;

@ApplicationScoped
public class GlobalConfigurationRepository {

    @Inject
    private EntityManager em;

    public GlobalConfiguration getByName(String name) {
        return em.find(GlobalConfiguration.class, name);
    }
    
    public String getValueByName(String name) {
        GlobalConfiguration globalConfiguration = em.find(GlobalConfiguration.class, name);
        return globalConfiguration.getValue();
    }

    public List<GlobalConfiguration> getAllOrderedByName() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<GlobalConfiguration> criteria = cb.createQuery(GlobalConfiguration.class);
        Root<GlobalConfiguration> property = criteria.from(GlobalConfiguration.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(member).orderBy(cb.asc(member.get(Member_.name)));
        criteria.select(property).orderBy(cb.asc(property.get("name")));
        return em.createQuery(criteria).getResultList();
    }
}
