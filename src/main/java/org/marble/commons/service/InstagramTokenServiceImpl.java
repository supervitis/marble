package org.marble.commons.service;

import java.util.List;

import org.marble.commons.dao.InstagramTokenDao;
import org.marble.commons.dao.model.InstagramToken;
import org.marble.commons.exception.InvalidInstagramTokenException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InstagramTokenServiceImpl implements InstagramTokenService {

	@Autowired
	InstagramTokenDao instagramTokenDao;

	@Override
	public InstagramToken updateInstagramToken(InstagramToken instagramToken)
			throws InvalidInstagramTokenException {
		instagramToken = instagramTokenDao.save(instagramToken);
		if (instagramToken == null) {
			throw new InvalidInstagramTokenException();
		}
		return instagramToken;
	}

	@Override
	public InstagramToken getInstagramToken(Integer id)
			throws InvalidInstagramTokenException {
		InstagramToken key = instagramTokenDao.findOne(id);
		if (key == null) {
			throw new InvalidInstagramTokenException();
		}
		return key;
	}

	@Override
	public List<InstagramToken> getInstagramTokens() {
		List<InstagramToken> keys = instagramTokenDao.findAll();
		return keys;
	}
	
	@Override
    public List<InstagramToken> getEnabledInstagramTokens() {
        List<InstagramToken> keys = instagramTokenDao.findByEnabled(Boolean.TRUE);
        return keys;
    }

	@Override
	public void deleteInstagramToken(Integer id) {
		instagramTokenDao.delete(id);
		return;
	}

	@Override
	public InstagramToken createInstagramToken(InstagramToken instagramToken)
			throws InvalidInstagramTokenException {
		instagramToken = instagramTokenDao.save(instagramToken);
		if (instagramToken == null) {
			throw new InvalidInstagramTokenException();
		}
		return instagramToken;
	}
}
