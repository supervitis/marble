package org.marble.commons.dao;

import org.marble.commons.dao.model.User;

import org.springframework.data.repository.CrudRepository;

public interface UserDao extends CrudRepository<User,Long> {
}
