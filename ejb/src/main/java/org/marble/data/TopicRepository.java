package org.marble.data;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import java.util.List;

import org.marble.model.Topic;

@ApplicationScoped
public class TopicRepository {

    @Inject
    private EntityManager em;

    public Topic getByName(String name) {
        return em.find(Topic.class, name);
    }

    public List<Topic> getAllOrderedByName() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Topic> criteria = cb.createQuery(Topic.class);
        Root<Topic> topic = criteria.from(Topic.class);
        criteria.select(topic).orderBy(cb.asc(topic.get("name")));
        return em.createQuery(criteria).getResultList();
    }
}
