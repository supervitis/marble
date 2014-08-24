package org.marble.commons.dao;

import java.util.List;

import org.marble.commons.dao.model.TwitterApiKey;
import org.springframework.data.repository.CrudRepository;

public interface TwitterApiKeyDao extends CrudRepository<TwitterApiKey,Integer> {
	List<TwitterApiKey> findAll();
}
