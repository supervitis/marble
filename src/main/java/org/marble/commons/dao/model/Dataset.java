package org.marble.commons.dao.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "mrbl_datasets")
public class Dataset implements Serializable {

    private static final long serialVersionUID = -6137928572799267601L;

    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String description;

    @NotNull
    @NotEmpty
    private String name;

  

    public Dataset() {
        this.description = null;
        this.name = null;
    }

   

    public Dataset(Integer id, String description, String name) {
		super();
		this.id = id;
		this.description = description;
		this.name = name;
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



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}

	

	public String toString() {
        return ("Name: " + this.name + ", Description: " + this.description);
    }
}