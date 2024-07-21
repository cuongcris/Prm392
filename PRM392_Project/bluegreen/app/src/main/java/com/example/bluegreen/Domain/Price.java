package com.example.bluegreen.Domain;

public class Price {
    private int Id;
    private String Value;

    public Price() {
    }

    public Price(int id, String value) {
        this.Id = id;
        this.Value = value;
    }

    @Override
    public String toString() {
        return  Value ;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }
}
