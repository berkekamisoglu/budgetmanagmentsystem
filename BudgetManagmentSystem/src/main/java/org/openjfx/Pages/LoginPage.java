package org.openjfx.Pages;  

import javafx.geometry.Insets;  
import javafx.geometry.Pos;  
import javafx.scene.Scene;  
import javafx.scene.control.*;  
import javafx.scene.image.Image;  
import javafx.scene.image.ImageView;  
import javafx.scene.layout.VBox;  
import javafx.scene.layout.HBox;  
import javafx.stage.Stage;  
import javafx.scene.layout.Background;  
import javafx.scene.layout.BackgroundFill;  
import javafx.scene.layout.CornerRadii;  
import javafx.scene.paint.Color;  
import javafx.scene.text.Font;  
import org.openjfx.BudgetApp;  

public class LoginPage {  

    public static Scene createLoginPage(Stage stage, BudgetApp app) {  
       
        VBox mainLayout = new VBox(20);  
        mainLayout.setAlignment(Pos.CENTER);  
        mainLayout.setPadding(new Insets(40));  
        mainLayout.setBackground(new Background(new BackgroundFill(  
            Color.web("#acccdc"),   
            CornerRadii.EMPTY,   
            Insets.EMPTY  
        )));  

  
        ImageView logoView = createLogo();  
    
        TextField usernameField = createTextField("Kullanıcı Adı", "Kullanıcı Adınızı Girin");  
        
        PasswordField passwordField = createPasswordField("Şifre", "Şifrenizi Girin");  

        Button loginButton = createStyledButton("Giriş Yap");  
        
      
        Button resetPasswordButton = createStyledButton("Şifremi Unuttum");  
        Button adminButton = createStyledButton("Admin Girişi");  
        Button registerButton = createStyledButton("Kayıt Ol");  

        
        Label errorLabel = createErrorLabel();  

       
        loginButton.setOnAction(e -> handleLogin(stage, app, usernameField, passwordField, errorLabel));  
        registerButton.setOnAction(e -> stage.setScene(RegisterPage.createRegisterPage(stage, app)));  
        resetPasswordButton.setOnAction(e -> stage.setScene(ResetPasswordPage.createResetPasswordPage(stage, app)));  
        adminButton.setOnAction(e -> stage.setScene(AdminLoginPage.createAdminLoginPage(stage, app)));  

        
        mainLayout.getChildren().addAll(  
            logoView,   
            createLabel("Kullanıcı Adı:"),  
            usernameField,   
            createLabel("Şifre:"),  
            passwordField,   
            createButtonLayout(loginButton, registerButton, resetPasswordButton, adminButton),   
            errorLabel  
        );  

    
        Scene scene = new Scene(mainLayout, 750, 750);  
        return scene;  
    }  

    
    private static ImageView createLogo() {  
        ImageView logoView = new ImageView();  
        try {  
            Image logo = new Image(BudgetApp.class.getResourceAsStream("icons/logo.png"));  
            logoView.setImage(logo);  
            logoView.setFitWidth(350);  
            logoView.setPreserveRatio(true);  
        } catch (Exception e) {  
            System.out.println("Logo yüklenirken hata: " + e.getMessage());  
        }  
        return logoView;  
    }  

   
    private static Label createLabel(String text) {  
        Label label = new Label(text);  
        label.setTextFill(Color.rgb(0, 41, 82));  
        label.setFont(Font.font(18));  
        return label;  
    }  

    
    private static TextField createTextField(String promptText, String helperText) {  
        TextField textField = new TextField();  
        textField.setPromptText(helperText);  
        textField.setStyle(  
            "-fx-background-color: rgba(255, 255, 255, 0.43);" +  
            "-fx-text-fill: rgb(0, 58, 116);" +  
            "-fx-background-radius: 20px;" +  
            "-fx-padding: 10px;" +  
            "-fx-font-size: 14px;"  
        );  
        return textField;  
    }  

   
    private static PasswordField createPasswordField(String promptText, String helperText) {  
        PasswordField passwordField = new PasswordField();  
        passwordField.setPromptText(helperText);  
        passwordField.setStyle(  
            "-fx-background-color: rgba(255, 255, 255, 0.43);" +  
            "-fx-text-fill: rgb(0, 58, 116);" +  
            "-fx-background-radius: 20px;" +  
            "-fx-padding: 10px;" +  
            "-fx-font-size: 14px;"  
        );  
        return passwordField;  
    }  

  
    private static Button createStyledButton(String text) {  
        Button button = new Button(text);  
        button.setStyle(  
            "-fx-background-color: rgb(0, 58, 116);" +  
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
            "-fx-background-color: rgb(0, 58, 116);" +  
            "-fx-text-fill: white;" +  
            "-fx-background-radius: 20px;" +  
            "-fx-padding: 10px;" +  
            "-fx-font-size: 14px;"  
        ));  

        return button;  
    }  

    
    private static Label createErrorLabel() {  
        Label errorLabel = new Label();  
        errorLabel.setTextFill(Color.RED);  
        errorLabel.setFont(Font.font(14));  
        return errorLabel;  
    }  

   
    private static HBox createButtonLayout(Button loginButton, Button registerButton, Button resetPasswordButton, Button adminButton) {  
        HBox buttonLayout = new HBox(20);  
        buttonLayout.setAlignment(Pos.CENTER);  
        buttonLayout.getChildren().addAll(loginButton, registerButton, resetPasswordButton, adminButton);  
        return buttonLayout;  
    }  

  
    private static void handleLogin(Stage stage, BudgetApp app, TextField usernameField, PasswordField passwordField, Label errorLabel) {  
        String username = usernameField.getText();  
        String password = passwordField.getText();  
    
        
        if (username.isEmpty() || password.isEmpty()) {  
            errorLabel.setText("Kullanıcı adı ve şifre boş olamaz!");  
            return;  
        }  
    
      
        int userId = app.authenticateUser(username, password);  
    
        if (userId != -1) {  
             
            boolean isActive = app.isUserActive(userId); 
            if (isActive) {  
                app.setActiveUserId(userId);  
                stage.setScene(HomePage.createHomePage(stage, app));  
            } else {  
                errorLabel.setText("Bu kullanıcı aktif değil. Giriş yapamazsınız!");  
            }  
        } else {  
            errorLabel.setText("Geçersiz kullanıcı adı veya şifre!");  
        }  
    } 
    
    
}