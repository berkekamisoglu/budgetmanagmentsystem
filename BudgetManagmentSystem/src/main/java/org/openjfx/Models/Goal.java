package org.openjfx.Models;



public class Goal {
    
    private int id;
    private int userId;
    private String name;
    private float targetAmount;
    private float savedAmount;
    private String deadline;

    public Goal(int id,int userId,String name,float targetAmount,float savedAmount,String deadline){

        this.id = id;
        this.userId = userId;
        this.name = name;
        this.targetAmount = targetAmount;
        this.savedAmount = savedAmount;
        this.deadline = deadline;

    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getDeadline() {
        return deadline;
    }
    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public float getSavedAmount() {
        return savedAmount;
    }
    public void setSavedAmount(float savedAmount) {
        this.savedAmount = savedAmount;
    }
    public float getTargetAmount() {
        return targetAmount;
    }
    public void setTargetAmount(float targetAmount) {
        this.targetAmount = targetAmount;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }


}
