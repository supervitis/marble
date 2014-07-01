package org.marble.service.processors;

public class SenticItem {
    private String text;
    private Float polarity;
    
    public SenticItem() {
        
    }
    
    public SenticItem(String text, Float polarity) {
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
