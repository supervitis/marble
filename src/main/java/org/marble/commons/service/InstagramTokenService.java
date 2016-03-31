package org.marble.commons.service;

import java.util.List;

import org.marble.commons.dao.model.InstagramToken;
import org.marble.commons.exception.InvalidInstagramTokenException;

public interface InstagramTokenService {

	public InstagramToken updateInstagramToken(InstagramToken instagramToken) throws InvalidInstagramTokenException;

	public InstagramToken getInstagramToken(Integer id) throws InvalidInstagramTokenException;

	List<InstagramToken> getInstagramTokens();
	
	List<InstagramToken> getEnabledInstagramTokens();

	public void deleteInstagramToken(Integer id);

	public InstagramToken createInstagramToken(InstagramToken instagramToken) throws InvalidInstagramTokenException;

}
