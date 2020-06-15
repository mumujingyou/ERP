package com.example.qunxin.erp.modle;
//ShangpinDatus
import android.widget.CheckBox;

import java.io.Serializable;

public class ShangpinBaseDatus implements Serializable {

    private int buyTotal;
    private int imgRes;
    private String norms;
    private String property;
    private float price;
    private String proId;
    private String proName;
    private String proNo;
    private String remarks="";
    private int total;
    private float totalAmount;
    private String unit;
    private String id;
    private String purchaseId;
    private String deportName;
    private String supplierName;
    private int realTotal;
    private  float realPrice;
    private float sellPrice;
    private int rejectTotal;
    private int maxTotal;
    private String createTime;
    private int actualTotal;
    private int bookTotal;
    private int chukuCount;
    private int stockTotal;
    private float stockAmount;

    public int getStockTotal() {
        return stockTotal;
    }

    public void setStockTotal(int stockTotal) {
        this.stockTotal = stockTotal;
    }

    public float getStockAmount() {
        return stockAmount;
    }

    public void setStockAmount(float stockAmount) {
        this.stockAmount = stockAmount;
    }

    public int getChukuCount() {
        return chukuCount;
    }

    public void setChukuCount(int chukuCount) {
        this.chukuCount = chukuCount;
    }

    public int getActualTotal() {
        return actualTotal;
    }



    public void setActualTotal(int actualTotal) {
        this.actualTotal = actualTotal;
    }

    public int getBookTotal() {
        return bookTotal;
    }

    public void setBookTotal(int bookTotal) {
        this.bookTotal = bookTotal;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getRejectTotal() {
        return rejectTotal;
    }

    public void setRejectTotal(int rejectTotal) {
        this.rejectTotal = rejectTotal;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public float getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(float sellPrice) {
        this.sellPrice = sellPrice;
    }

    public int getRealTotal() {
        return realTotal;
    }

    public void setRealTotal(int realTotal) {
        this.realTotal = realTotal;
    }

    public float getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(float realPrice) {
        this.realPrice = realPrice;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getDeportName() {
        return deportName;
    }

    public void setDeportName(String deportName) {
        this.deportName = deportName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    private boolean isChecked;


    private CheckBox checkBox;


    private int kucunCount;

    private int tuihuoCount;


    private float marketAmount;
    private float purchaseAmount;
    private int actualPurchaseQuantity;


    public float getMarketAmount() {
        return marketAmount;
    }

    public void setMarketAmount(float marketAmount) {
        this.marketAmount = marketAmount;
    }

    public float getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(float purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public int getActualPurchaseQuantity() {
        return actualPurchaseQuantity;
    }

    public void setActualPurchaseQuantity(int actualPurchaseQuantity) {
        this.actualPurchaseQuantity = actualPurchaseQuantity;
    }

    public int getKucunCount() {
        return kucunCount;
    }

    public void setKucunCount(int kucunCount) {
        this.kucunCount = kucunCount;
    }

    public int getTuihuoCount() {
        return tuihuoCount;
    }

    public void setTuihuoCount(int tuihuoCount) {
        this.tuihuoCount = tuihuoCount;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getByTotal() {
        return buyTotal;
    }

    public void setByTotal(int buyTotal) {
        this.buyTotal = buyTotal;
    }

    public int getImgRes() {
        return imgRes;
    }

    public void setImgRes(int imgRes) {
        this.imgRes = imgRes;
    }

    public String getNorms() {
        return norms;
    }

    public void setNorms(String norms) {
        this.norms = norms;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getProNo() {
        return proNo;
    }

    public void setProNo(String proNo) {
        this.proNo = proNo;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}

