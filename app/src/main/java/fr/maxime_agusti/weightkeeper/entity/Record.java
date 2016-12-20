package fr.maxime_agusti.weightkeeper.entity;

import java.io.Serializable;

public class Record implements Serializable {

    private long id;
    private String instant;
    private double weight;
    
    public long getId() {
        return this.id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public String getInstant() {
        return this.instant;
    }
    
    public void setInstant(String instant) {
        this.instant = instant;
    }
    
    public double getWeight() {
        return this.weight;
    }
    
    public void setWeight(double weight) {
        this.weight = weight;
    }
    
}
