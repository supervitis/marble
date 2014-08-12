package org.marble.commons.dao;

import org.marble.commons.dao.model.User;
import org.springframework.data.repository.CrudRepository;

/**
 * User: ryan
 * Date: 2/20/13
 */
public interface UserDao extends CrudRepository<User,Long> {
}
