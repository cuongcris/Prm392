package com.example.bluegreen.Domain;

public class Coupon {
    String Id;
    String Code;
    String StartDate;
    String EndDate;
    int Total;
    double Discount;
    public Coupon() {
    }
    public Coupon(String id, String code, String startDate, String endDate, int total, double discount) {
        Id = id;
        Code = code;
        StartDate = startDate;
        EndDate = endDate;
        Total = total;
        Discount = discount;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }

    public double getDiscount() {
        return Discount;
    }

    public void setDiscount(double discount) {
        Discount = discount;
    }

    @Override
    public String toString() {
        return "Coupon{" +
                "Id='" + Id + '\'' +
                ", Code='" + Code + '\'' +
                ", StartDate='" + StartDate + '\'' +
                ", EndDate='" + EndDate + '\'' +
                ", Total=" + Total +
                ", Discount=" + Discount +
                '}';
    }
}
