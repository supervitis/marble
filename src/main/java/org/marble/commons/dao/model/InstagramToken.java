package org.marble.commons.dao.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "mrbl_instagram_tokens")
public class InstagramToken implements Serializable {

    private static final long serialVersionUID = -6137928572799267601L;

    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String description;

    @NotNull
    @NotEmpty
    @Column(unique = true)
    private String accessToken;


    @NotNull
    private Boolean enabled;

    public InstagramToken() {
        this.description = null;
        this.accessToken = null;
        this.setEnabled(Boolean.FALSE);
    }

    public InstagramToken(String description, String clientId, String accessToken) {
        this.description = description;
        this.accessToken = accessToken;
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

	public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }


    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String toString() {
        return ("Access Token: " + this.accessToken);
    }
}