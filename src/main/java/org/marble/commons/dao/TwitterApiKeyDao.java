package org.marble.commons.dao;

import org.marble.commons.dao.model.TwitterApiKey;
import org.springframework.data.repository.CrudRepository;

public interface TwitterApiKeyDao extends CrudRepository<TwitterApiKey,String> {
}
