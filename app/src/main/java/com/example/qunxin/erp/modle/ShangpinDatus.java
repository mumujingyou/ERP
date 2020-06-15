package com.example.qunxin.erp.modle;
//ShangpinDatus
import java.io.Serializable;

public class ShangpinDatus implements Serializable {

    private String name;
    private int imgRes;
    private String price;
    private String id;
    private int pirceDanjia;
    private int count;




    public int getPirceDanjia() {
        return pirceDanjia;
    }

    public void setPirceDanjia(int pirceDanjia) {
        this.pirceDanjia = pirceDanjia;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImgRes() {
        return imgRes;
    }

    public void setImgRes(int imgRes) {
        this.imgRes = imgRes;
    }

    public String getPrice(){return price;}


    public void setPrice(String price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

