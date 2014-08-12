package org.marble.commons.service;

import org.marble.commons.dao.UserDao;
import org.marble.commons.dao.model.User;
import org.marble.commons.exception.InvalidUserException;
import org.marble.commons.model.SignupForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class SampleServiceImpl implements SampleService {

    @Autowired
    UserDao userDao;

    public SignupForm saveFrom(SignupForm signupForm) throws InvalidUserException{

        String firstName = signupForm.getFirstName();

        if(!StringUtils.isEmpty(firstName) && "Dave".equalsIgnoreCase(firstName)) {
            throw new InvalidUserException("Sorry Dave");
        }


        // Shown for example, you could use a constructor, builder pattern or Dozer
        // point is that the DAO only knows and cares about users and not any UI form.
        User user = new User();
        user.setFirstName(signupForm.getFirstName());
        user.setEmail(signupForm.getEmail());
        user.setLastName(signupForm.getLastName());

        user = userDao.save(user);

        signupForm.setId(user.getId());

        return signupForm;

    }
}
