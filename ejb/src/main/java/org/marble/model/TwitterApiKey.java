package org.marble.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table
public class TwitterApiKey implements Serializable {

    private static final long serialVersionUID = 1L;

    
    @NotNull
    @NotEmpty
    private String            consumerKey;

    @NotNull
    @NotEmpty
    private String            consumerSecret;

    @Id
    @NotNull
    @NotEmpty
    @Column(unique = true)
    private String            accessToken;

    @NotNull
    @NotEmpty
    private String            accessTokenSecret;
    
    @NotNull
    @NotEmpty
    private Boolean           enabled;
    
    private String            owner;

    public TwitterApiKey() {
        this.consumerKey = null;
        this.consumerSecret = null;
        this.accessToken = null;
        this.accessTokenSecret = null;
        this.setEnabled(Boolean.FALSE);
    }

    public TwitterApiKey(String consumerKey, String consumerSecret, String accessToken,
            String accessTokenSecret) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.accessToken = accessToken;
        this.accessTokenSecret = accessTokenSecret;
        this.setEnabled(Boolean.FALSE);
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessTokenSecret() {
        return accessTokenSecret;
    }

    public void setAccessTokenSecret(String accessTokenSecret) {
        this.accessTokenSecret = accessTokenSecret;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String toString() {
        return ("Consumer Key: " + this.consumerKey + ", Consumer Secret: " + this.consumerSecret
                + ", Access Token: " + this.accessToken + ", Access Token Secret: " + this.accessTokenSecret);
    }
}
