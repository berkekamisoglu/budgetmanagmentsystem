package org.openjfx.Models;


public class PeriodReport {
    
    private int id;
    private int userId;
    private String startDate;
    private String endDate;
    private float totalIncome;
    private float totalExpense;

    public PeriodReport(int id,int userId,String starDate,String endDate,float totalIncome,float totalExpense){
        this.id = id;
        this.userId = userId;
        this.startDate = starDate;
        this.endDate = endDate;
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
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
    public String getStartDate() {
        return startDate;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public String getEndDate() {
        return endDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    public float getTotalExpense() {
        return totalExpense;
    }
    public void setTotalExpense(float totalExpense) {
        this.totalExpense = totalExpense;
    }
    public float getTotalIncome() {
        return totalIncome;
    }
    public void setTotalIncome(float totalIncome) {
        this.totalIncome = totalIncome;
    }
    


}



