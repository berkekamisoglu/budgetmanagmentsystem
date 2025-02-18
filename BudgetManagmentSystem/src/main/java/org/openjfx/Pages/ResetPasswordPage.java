package org.openjfx.Pages;  

import javafx.geometry.Insets;  
import javafx.geometry.Pos;  
import javafx.scene.Scene;  
import javafx.scene.control.*;  
import javafx.scene.layout.VBox;  
import javafx.scene.layout.HBox;  
import javafx.stage.Stage;  
import javafx.scene.layout.Background;  
import javafx.scene.layout.BackgroundFill;  
import javafx.scene.layout.CornerRadii;  
import javafx.scene.paint.Color;  
import javafx.scene.text.Font;  
import org.openjfx.BudgetApp;  

public class ResetPasswordPage {  
    public static Scene createResetPasswordPage(Stage stage, BudgetApp app) {  
      
        VBox mainLayout = new VBox(20);  
        mainLayout.setAlignment(Pos.CENTER);  
        mainLayout.setPadding(new Insets(40));  
        mainLayout.setBackground(new Background(new BackgroundFill(  
            Color.web("#2C3E50"),   
            CornerRadii.EMPTY,   
            Insets.EMPTY  
        )));  
        
     
        Label titleLabel = new Label("Şifremi Sıfırla");  
        titleLabel.setFont(Font.font(24));  
        titleLabel.setTextFill(Color.WHITE);  

    
        Label mailLabel = createLabel("Mail Adresi");  
        TextField mailField = createTextField("Mail Adresinizi Girin");  

    

        Button resetPasswordButton = createStyledButton("Şifremi Sıfırla");  
        Button returnButton = createStyledButton("Geri Dön");  

     
        Label feedbackLabel = createFeedbackLabel();  

      
        returnButton.setOnAction(e -> stage.setScene(LoginPage.createLoginPage(stage, app))); 

        resetPasswordButton.setOnAction(e -> { 
            app.showAlert("Şifremi Unuttum", "Şifre Sıfırlama Linkiniz Mail Adresinize Gönderildi.");  
        }); 
       

        HBox buttonLayout = new HBox(20);  
        buttonLayout.setAlignment(Pos.CENTER);  
        buttonLayout.getChildren().addAll(resetPasswordButton, returnButton);  

     
        mainLayout.getChildren().addAll(  
            titleLabel,  
            mailLabel,   
            mailField,  
            buttonLayout,   
            feedbackLabel  
        );  

    
        Scene scene = new Scene(mainLayout, 750, 750);  
        return scene;  
    }  

   
    private static Label createLabel(String text) {  
        Label label = new Label(text);  
        label.setTextFill(Color.WHITE);  
        label.setFont(Font.font(14));  
        return label;  
    }  

 
    private static TextField createTextField(String helperText) {  
        TextField textField = new TextField();  
        textField.setPromptText(helperText);  
        textField.setStyle(  
            "-fx-background-color: rgba(255,255,255,0.2);" +  
            "-fx-text-fill: white;" +  
            "-fx-background-radius: 20px;" +  
            "-fx-padding: 10px;" +  
            "-fx-font-size: 14px;"  
        );  
        return textField;  
    }  


   
    private static Button createStyledButton(String text) {  
        Button button = new Button(text);  
        button.setStyle(  
            "-fx-background-color: #34495E;" +  
            "-fx-text-fill: white;" +  
            "-fx-background-radius: 20px;" +  
            "-fx-padding: 10px;" +  
            "-fx-font-size: 14px;"  
        );  
        
       
        button.setOnMouseEntered(e -> button.setStyle(  
            "-fx-background-color: #2980B9;" +  
            "-fx-text-fill: white;" +  
            "-fx-background-radius: 20px;" +  
            "-fx-padding: 10px;" +  
            "-fx-font-size: 14px;" +  
            "-fx-scale-x: 1.05;" +  
            "-fx-scale-y: 1.05;"  
        ));  
        
        button.setOnMouseExited(e -> button.setStyle(  
            "-fx-background-color: #34495E;" +  
            "-fx-text-fill: white;" +  
            "-fx-background-radius: 20px;" +  
            "-fx-padding: 10px;" +  
            "-fx-font-size: 14px;"  
        ));  

        return button;  
    }  

  
    private static Label createFeedbackLabel() {  
        Label feedbackLabel = new Label();  
        feedbackLabel.setFont(Font.font(14));  
        return feedbackLabel;  
    }  
}