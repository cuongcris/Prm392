package com.example.bluegreen.Domain;

public class Time {
    private int Id;
    private String Value;

    public Time() {
    }

    public Time(int id, String value) {
        this.Id = id;
        this.Value = value;
    }

    @Override
    public String toString() {
        return  Value;
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
