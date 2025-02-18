package org.openjfx.Models;


public class Notification {
    
    private int id;
    private int userId;
    private String message;
    private String date;
    private boolean isRead; 

    public Notification(int id,int userId,String message,String date, boolean isRead){

        this.id = id;
        this.userId =  userId;
        this.message = message;
        this.date = date;
        this.isRead = isRead;

    }
public int getId() {
    return id;
}
public void setId(int id) {
    this.id = id;
}
public String getDate() {
    return date;
}
public void setDate(String date) {
    this.date = date;
}
public String getMessage() {
    return message;
}
public void setMessage(String message) {
    this.message = message;
}
public int getUserId() {
    return userId;
}
public void setUserId(int userId) {
    this.userId = userId;
}

public boolean getisRead(){
    return isRead;
}

public void setisRead(boolean isRead){
    this.isRead = isRead;
}





}
