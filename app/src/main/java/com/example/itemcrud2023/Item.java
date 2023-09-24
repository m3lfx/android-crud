package com.example.itemcrud2023;

public class Item {
    private String description;
    private String sell_price;
    private String cost_price;
    private String image_url;
    private Integer item_id;

    public String getCost_price() {
        return cost_price;
    }

    //,Integer item_id
    public Item(String description, String sell_price,String cost_price,String image_url,Integer item_id) {
        this.description = description;
        this.sell_price = sell_price;
        this.image_url = image_url;
        this.item_id = item_id;
        this.cost_price = cost_price;
    }

    public String getDescription() {
        return description;
    }

    public String getSell_price() {
        return sell_price;
    }

    public String getImage() {
        return image_url;
    }
    public Integer getId() {
        return item_id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSell_price(String sell_price) {
        this.sell_price = sell_price;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setItem_id(Integer item_id) {
        this.item_id = item_id;
    }
    public void setCost_price(String cost_price) {
        this.cost_price = cost_price;
    }
}
