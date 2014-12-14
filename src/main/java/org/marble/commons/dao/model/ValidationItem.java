package org.marble.commons.dao.model;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "validation")
public class ValidationItem {

    @Id
    @Indexed
    private String text;
    private Float polarity;

    public ValidationItem() {

    }

    public ValidationItem(String text, Float polarity) {
        this.text = text;
        this.polarity = polarity;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Float getPolarity() {
        return polarity;
    }

    public void setPolarity(Float polarity) {
        this.polarity = polarity;
    }

    public String toString() {
        return "(" + this.text + "; " + this.polarity + ")";
    }
}
