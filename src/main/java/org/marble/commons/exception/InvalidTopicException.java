package org.marble.commons.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="No such Topic") 
public class InvalidTopicException extends Exception {
	private static final long serialVersionUID = 1527573240130617355L;
}
