package org.marble.commons.dao;

import java.util.List;

import org.marble.commons.dao.model.InstagramToken;

import org.springframework.data.repository.CrudRepository;

public interface InstagramTokenDao extends CrudRepository<InstagramToken,Integer> {
	List<InstagramToken> findAll();
	List<InstagramToken> findByEnabled(Boolean enabled);
}
