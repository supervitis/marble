package org.marble.commons.service;

import org.marble.commons.exception.InvalidUserException;
import org.marble.commons.model.SignupForm;

public interface SampleService {

    public SignupForm saveFrom(SignupForm signupForm) throws InvalidUserException;

}
