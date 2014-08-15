package org.marble.commons.dao;

import org.marble.commons.dao.model.ConfigurationItem;
import org.springframework.data.repository.CrudRepository;

public interface ConfigurationItemDao extends CrudRepository<ConfigurationItem,String> {
}
