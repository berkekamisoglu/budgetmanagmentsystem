package org.openjfx.Models;


public class Transaction {

    private int id;
    private int userId;
    private Type type;
    private float amount;
    private String date;
    private int categoryId;
    private int merchantId;
    private String description;

    public Transaction(int id, int userId, Type type,float amount,String date, int categoryId, int merchantId, String description){

        this.id=id;
        this.userId=userId;
        this.type=type;
        this.amount=amount;
        this.date=date;
        this.categoryId=categoryId;
        this.merchantId=merchantId;
        this.description=description;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public Type getType() {
        return type;
    }
    public void setType(Type type) {
        this.type = type;
    }
    public float getAmount() {
        return amount;
    }
    public void setAmount(float amount) {
        this.amount = amount;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public int getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getMerchantId() {
        return merchantId;
    }
    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }


}
enum Type {INCOME,EXPENSE}
