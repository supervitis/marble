package org.marble.commons.service;

import java.util.List;

import org.marble.commons.dao.TwitterApiKeyDao;
import org.marble.commons.dao.model.TwitterApiKey;
import org.marble.commons.exception.InvalidTwitterApiKeyException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TwitterApiKeyServiceImpl implements TwitterApiKeyService {

	@Autowired
	TwitterApiKeyDao twitterApiKeyDao;

	@Override
	public TwitterApiKey updateTwitterApiKey(TwitterApiKey twitterApiKey)
			throws InvalidTwitterApiKeyException {
		twitterApiKey = twitterApiKeyDao.save(twitterApiKey);
		if (twitterApiKey == null) {
			throw new InvalidTwitterApiKeyException();
		}
		return twitterApiKey;
	}

	@Override
	public TwitterApiKey getTwitterApiKey(Integer id)
			throws InvalidTwitterApiKeyException {
		TwitterApiKey key = twitterApiKeyDao.findOne(id);
		if (key == null) {
			throw new InvalidTwitterApiKeyException();
		}
		return key;
	}

	@Override
	public List<TwitterApiKey> getTwitterApiKeys() {
		List<TwitterApiKey> keys = twitterApiKeyDao.findAll();
		return keys;
	}
	
	@Override
    public List<TwitterApiKey> getEnabledTwitterApiKeys() {
        List<TwitterApiKey> keys = twitterApiKeyDao.findByEnabled(Boolean.TRUE);
        return keys;
    }

	@Override
	public void deleteTwitterApiKey(Integer id) {
		twitterApiKeyDao.delete(id);
		return;
	}

	@Override
	public TwitterApiKey createTwitterApiKey(TwitterApiKey twitterApiKey)
			throws InvalidTwitterApiKeyException {
		twitterApiKey = twitterApiKeyDao.save(twitterApiKey);
		if (twitterApiKey == null) {
			throw new InvalidTwitterApiKeyException();
		}
		return twitterApiKey;
	}
}
