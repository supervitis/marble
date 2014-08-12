package org.marble.commons.dao.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table
public class GlobalConfiguration implements Serializable {

	private static final long serialVersionUID = -946199796905909629L;

	@Id
    @NotNull
    @NotEmpty
    @Column(unique=true)
    private String            name;
    
    private String            value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}