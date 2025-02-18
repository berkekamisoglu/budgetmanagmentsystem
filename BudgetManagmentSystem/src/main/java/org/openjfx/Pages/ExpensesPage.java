package org.openjfx.Pages;  

import javafx.geometry.Insets;  
import javafx.geometry.Pos;  
import javafx.scene.Scene;  
import javafx.scene.control.Button;  
import javafx.scene.control.Label;  
import javafx.scene.control.ListView;  
import javafx.scene.layout.HBox;  
import javafx.scene.layout.VBox;  
import javafx.scene.paint.Color;  
import javafx.scene.text.Font;  
import javafx.stage.Stage;  
import org.openjfx.BudgetApp;  

public class ExpensesPage {    
    private static final String MAIN_BACKGROUND =   
        "-fx-background-color: linear-gradient(to bottom right, #ECF0F1, #BDC3C7);";  
    
    private static final String LIST_STYLE =   
        "-fx-background-color: rgba(255,255,255,0.8);" +  
        "-fx-background-radius: 20;" +  
        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0.0, 0.0, 3);";  
    
    private static final String BUTTON_STYLE =   
        "-fx-background-color: #2196F3;" +  
        "-fx-text-fill: white;" +  
        "-fx-font-size: 14px;" +  
        "-fx-background-radius: 20;" +  
        "-fx-padding: 10 20;";  
    
    private static final String BUTTON_HOVER_STYLE =   
        "-fx-background-color: #1976D2;" +  
        "-fx-scale-x: 1.05;" +  
        "-fx-scale-y: 1.05;";  

    public static Scene createExpensesPage(Stage stage, BudgetApp app) {  
       
        Label titleLabel = new Label("Hesap Hareketleri");  
        titleLabel.setFont(Font.font(24));  
        titleLabel.setTextFill(Color.rgb(44, 62, 80));  

       
        ListView<String> expenseList = new ListView<>();  
        expenseList.setStyle(LIST_STYLE);  
       
        refreshExpenses(expenseList, app);  

     
        Button refreshButton = new Button("Yenile");  
        Button backButton = new Button("Geri");  

     
        refreshButton.setStyle(BUTTON_STYLE);  
        backButton.setStyle(BUTTON_STYLE + "-fx-background-color: #E74C3C;");  

        refreshButton.setOnMouseEntered(e -> refreshButton.setStyle(BUTTON_STYLE + BUTTON_HOVER_STYLE));  
        refreshButton.setOnMouseExited(e -> refreshButton.setStyle(BUTTON_STYLE));  
        
        backButton.setOnMouseEntered(e -> backButton.setStyle(BUTTON_STYLE + "-fx-background-color: #C0392B;" + BUTTON_HOVER_STYLE));  
        backButton.setOnMouseExited(e -> backButton.setStyle(BUTTON_STYLE + "-fx-background-color: #E74C3C;"));  

      
        refreshButton.setOnAction(e -> refreshExpenses(expenseList, app));  
        backButton.setOnAction(e -> stage.setScene(HomePage.createHomePage(stage, app)));  

    
        HBox buttonBox = new HBox(20, refreshButton, backButton);  
        buttonBox.setAlignment(Pos.CENTER);  

        
        VBox root = new VBox(20);  
        root.setStyle(MAIN_BACKGROUND);  
        root.setAlignment(Pos.CENTER);  
        root.setPadding(new Insets(20));  
        root.getChildren().addAll(  
            titleLabel,   
            expenseList,   
            buttonBox  
        );  

        return new Scene(root, 750, 750);  
    }  

    private static void refreshExpenses(ListView<String> listView, BudgetApp app) {  
      
        listView.getItems().clear();   
        
        
        listView.getItems().addAll(app.getExpenses());   
        

        if (listView.getItems().isEmpty()) {  
            listView.getItems().add("Henüz bir gider veya gelir yok.");  
            listView.setStyle(  
                "-fx-background-color: rgba(255,255,255,0.8);" +  
                "-fx-background-radius: 20;" +  
                "-fx-alignment: center;" +  
                "-fx-font-size: 16px;" +  
                "-fx-text-fill: #7F8C8D;"  
            );  
        } else {  
      
            listView.setStyle(  
                "-fx-background-color: rgba(255,255,255,0.8);" +  
                "-fx-background-radius: 20;" +  
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0.0, 0.0, 3);"  
            );  
        }  
    }  
}