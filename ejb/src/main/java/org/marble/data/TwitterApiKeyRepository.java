package org.marble.data;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import java.util.List;

import org.marble.model.TwitterApiKey;

@ApplicationScoped
public class TwitterApiKeyRepository {

    @Inject
    private EntityManager em;

    public TwitterApiKey getByName(String name) {
        return em.find(TwitterApiKey.class, name);
    }
    
    public List<TwitterApiKey> getAllOrderedByAccessToken() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TwitterApiKey> criteria = cb.createQuery(TwitterApiKey.class);
        Root<TwitterApiKey> key = criteria.from(TwitterApiKey.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(member).orderBy(cb.asc(member.get(Member_.name)));
        criteria.select(key).orderBy(cb.asc(key.get("accessToken")));
        return em.createQuery(criteria).getResultList();
    }
}
