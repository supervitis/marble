package org.marble.commons.dao.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "mrbl_twitter_api_keys")
public class TwitterApiKey implements Serializable {

    private static final long serialVersionUID = -6137928572799267601L;

    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String description;

    @NotNull
    @NotEmpty
    private String consumerKey;

    @NotNull
    @NotEmpty
    private String consumerSecret;

    @NotNull
    @NotEmpty
    @Column(unique = true)
    private String accessToken;

    @NotNull
    @NotEmpty
    private String accessTokenSecret;

    @NotNull
    private Boolean enabled;

    public TwitterApiKey() {
        this.description = null;
        this.consumerKey = null;
        this.consumerSecret = null;
        this.accessToken = null;
        this.accessTokenSecret = null;
        this.setEnabled(Boolean.FALSE);
    }

    public TwitterApiKey(String description, String consumerKey, String consumerSecret, String accessToken,
            String accessTokenSecret) {
        this.description = description;
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.accessToken = accessToken;
        this.accessTokenSecret = accessTokenSecret;
        this.setEnabled(Boolean.FALSE);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String toString() {
        return ("Consumer Key: " + this.consumerKey + ", Consumer Secret: " + this.consumerSecret
                + ", Access Token: " + this.accessToken + ", Access Token Secret: " + this.accessTokenSecret);
    }
}