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

public class RegisterPage {  
    public static Scene createRegisterPage(Stage stage, BudgetApp app) {  
  
        VBox mainLayout = new VBox(20);  
        mainLayout.setAlignment(Pos.CENTER);  
        mainLayout.setPadding(new Insets(40));  
        mainLayout.setBackground(new Background(new BackgroundFill(  
            Color.web("#2C3E50"),   
            CornerRadii.EMPTY,   
            Insets.EMPTY  
        )));  

       
        Label titleLabel = new Label("Kayıt Ol");  
        titleLabel.setFont(Font.font(24));  
        titleLabel.setTextFill(Color.WHITE);  

        
        Label usernameLabel = createLabel("Kullanıcı Adı:");  
        TextField usernameField = createTextField("Kullanıcı Adınızı Girin");  

       
        Label passwordLabel = createLabel("Şifre:");  
        PasswordField passwordField = createPasswordField("Şifrenizi Girin");  

        
        Button registerButton = createStyledButton("Kayıt Ol");  
        Button returnButton = createStyledButton("Geri Dön");  

        
        Label feedbackLabel = createFeedbackLabel();  

      
        returnButton.setOnAction(e -> stage.setScene(LoginPage.createLoginPage(stage, app)));  

        registerButton.setOnAction(e -> {  
            String username = usernameField.getText();  
            String password = passwordField.getText();  
            
            if (username.isEmpty() || password.isEmpty()) {  
                feedbackLabel.setText("Kullanıcı adı ve şifre gereklidir.");  
                feedbackLabel.setTextFill(Color.RED);  
                return;  
            }  
            
            if (app.registerUser(username, password)) {  
                feedbackLabel.setText("Başarıyla kayıt olundu.");  
                feedbackLabel.setTextFill(Color.GREEN);  
            } else {  
                feedbackLabel.setText("Kayıt sırasında bir hata oluştu.");  
                feedbackLabel.setTextFill(Color.RED);  
            }  
        });  

      
        HBox buttonLayout = new HBox(20);  
        buttonLayout.setAlignment(Pos.CENTER);  
        buttonLayout.getChildren().addAll(registerButton, returnButton);  

      
        mainLayout.getChildren().addAll(  
            titleLabel,  
            usernameLabel,   
            usernameField,   
            passwordLabel,   
            passwordField,   
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

   
    private static PasswordField createPasswordField(String helperText) {  
        PasswordField passwordField = new PasswordField();  
        passwordField.setPromptText(helperText);  
        passwordField.setStyle(  
            "-fx-background-color: rgba(255,255,255,0.2);" +  
            "-fx-text-fill: white;" +  
            "-fx-background-radius: 20px;" +  
            "-fx-padding: 10px;" +  
            "-fx-font-size: 14px;"  
        );  
        return passwordField;  
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