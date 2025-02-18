package org.openjfx.Models;

public class Budget{
    
    private int id;
    private int userId;
    private int budgetId;
    private float budgetAmount;
    private float usedAmount;

    public Budget(int id,int userId,int budgetId,int budgetAmount,int usedAmount){

        this.id=id;
        this.userId = userId;
        this.budgetId = budgetId;
        this.budgetAmount = budgetAmount;
        this.usedAmount = usedAmount;

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
    public int getbudgetId() {
        return budgetId;
    }
    public void setbudgetId(int budgetId) {
        this.budgetId = budgetId;
    }
    public float getBudgetAmount() {
        return budgetAmount;
    }
    public void setBudgetAmount(float budgetAmount) {
        this.budgetAmount = budgetAmount;
    }
    public void setUsedAmount(float usedAmount) {
        this.usedAmount = usedAmount;
    }
    public float getUsedAmount() {
        return usedAmount;
    }



}
