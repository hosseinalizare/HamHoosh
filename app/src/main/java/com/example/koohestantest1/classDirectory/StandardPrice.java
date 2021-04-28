package com.example.koohestantest1.classDirectory;

public class StandardPrice {
    private int StandardCost;
    private int offPrice;
    private int Price;
    private String ShowStandardCost;
    private String ShowoffPrice;
    private String ShowPrice;

    public int getStandardCost() {
        return StandardCost;
    }

    public void setStandardCost(int standardCost) {
        StandardCost = standardCost;
    }

    public int getOffPrice() {
        return offPrice;
    }

    public void setOffPrice(int offPrice) {
        this.offPrice = offPrice;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public String getShowStandardCost() {
        return ShowStandardCost;
    }

    public void setShowStandardCost(String showStandardCost) {
        ShowStandardCost = showStandardCost;
    }

    public String getShowoffPrice() {
        return ShowoffPrice;
    }

    public void setShowoffPrice(String showoffPrice) {
        ShowoffPrice = showoffPrice;
    }

    public String getShowPrice() {
        return ShowPrice;
    }

    public void setShowPrice(String showPrice) {
        ShowPrice = showPrice;
    }
}
