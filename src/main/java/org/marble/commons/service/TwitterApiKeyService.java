package org.marble.commons.service;

import java.util.List;

import org.marble.commons.dao.model.TwitterApiKey;
import org.marble.commons.exception.InvalidTwitterApiKeyException;

public interface TwitterApiKeyService {

	public TwitterApiKey updateTwitterApiKey(TwitterApiKey twitterApiKey) throws InvalidTwitterApiKeyException;

	public TwitterApiKey getTwitterApiKey(Integer id) throws InvalidTwitterApiKeyException;

	List<TwitterApiKey> getTwitterApiKeys();

	public void deleteTwitterApiKey(Integer id);

	public TwitterApiKey createTwitterApiKey(TwitterApiKey twitterApiKey) throws InvalidTwitterApiKeyException;

}
