package org.openjfx.Pages;  

import javafx.geometry.Insets;  
import javafx.geometry.Pos;  
import javafx.scene.Scene;  
import javafx.scene.control.*;  
import javafx.scene.layout.VBox;  
import javafx.stage.Stage;  
import org.openjfx.BudgetApp;
import org.openjfx.database.DatabaseConnector;

import java.sql.*;  

public class ProfileSettingsPage {  
   
    private static final String MAIN_BACKGROUND = "-fx-background-color: linear-gradient(to right,rgb(53, 144, 248),rgb(0, 44, 114));";  
    private static final String CARD_STYLE =  
        "-fx-background-color: white;" +  
        "-fx-background-radius: 10;" +  
        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);";  
    private static final String TITLE_STYLE =  
        "-fx-font-size: 28px;" +  
        "-fx-font-weight: bold;" +  
        "-fx-text-fill: #34495E;";  
    private static final String INPUT_STYLE =  
        "-fx-background-color: #f1f3f6;" +  
        "-fx-background-radius: 5;" +  
        "-fx-padding: 10;" +  
        "-fx-font-size: 14px;";  
    private static final String SAVE_BUTTON_STYLE =  
        "-fx-background-color: #2196F3;" +  
        "-fx-text-fill: white;" +  
        "-fx-font-size: 16px;" +  
        "-fx-font-weight: bold;" +  
        "-fx-background-radius: 20;" +  
        "-fx-padding: 10 20;";  
    private static final String BACK_BUTTON_STYLE =  
        "-fx-background-color: #f44336;" +  
        "-fx-text-fill: white;" +  
        "-fx-font-size: 14px;" +  
        "-fx-background-radius: 20;" +  
        "-fx-padding: 10 20;";  


    private static boolean updatePassword(String username, String newPassword) {  
        DatabaseConnector dbConnector = DatabaseConnector.getInstance();  
        try (Connection connection = dbConnector.getConnection()) {  
            String query = "UPDATE users SET password = ? WHERE username = ?";  
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {  
                pstmt.setString(1, newPassword);  
                pstmt.setString(2, username);  
                int affectedRows = pstmt.executeUpdate();  
                return affectedRows > 0;  
            }  
        } catch (SQLException e) {  
            e.printStackTrace();  
            return false;  
        }  
    }  

   
    private static boolean updateUsername(String currentUsername, String newUsername) {  
        DatabaseConnector dbConnector = DatabaseConnector.getInstance();  
        try (Connection connection = dbConnector.getConnection()) {  
            String query = "UPDATE users SET username = ? WHERE username = ?";  
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {  
                pstmt.setString(1, newUsername);  
                pstmt.setString(2, currentUsername);  
                int affectedRows = pstmt.executeUpdate();  
                return affectedRows > 0;  
            }  
        } catch (SQLException e) {  
            e.printStackTrace();  
            return false;  
        }  
    } 

    
    private static boolean validatePassword(String username, String password) {  
        DatabaseConnector dbConnector = DatabaseConnector.getInstance();  
        try (Connection connection = dbConnector.getConnection()) {  
            String query = "SELECT password FROM users WHERE username = ? AND password = ?";  
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {  
                pstmt.setString(1, username);  
                pstmt.setString(2, password);  
                try (ResultSet rs = pstmt.executeQuery()) {  
                    return rs.next();  
                }  
            }  
        } catch (SQLException e) {  
            e.printStackTrace();  
            return false;  
        }  
    }  

    public static Scene createProfileSettingsPage(Stage stage, BudgetApp app) {  
         
        VBox mainLayout = new VBox(20);  
        mainLayout.setPadding(new Insets(30));  
        mainLayout.setStyle(MAIN_BACKGROUND);  
        mainLayout.setAlignment(Pos.CENTER);  

        
        VBox profileCard = new VBox(20);  
        profileCard.setStyle(CARD_STYLE);  
        profileCard.setPadding(new Insets(30));  
        profileCard.setAlignment(Pos.CENTER);  

       
        Label titleLabel = new Label("Profil Ayarları");  
        titleLabel.setStyle(TITLE_STYLE);  

        
        Label currentUsernameLabel = new Label("Mevcut Kullanıcı Adı: " + app.getCurrentUser());  
        currentUsernameLabel.setStyle("-fx-font-size: 16px;" + "-fx-text-fill: #7f8c8d;");  

        
        Label newUsernameLabel = new Label("Yeni Kullanıcı Adı");  
        newUsernameLabel.setStyle("-fx-font-weight: bold;");  
        TextField newUsernameField = new TextField();  
        newUsernameField.setStyle(INPUT_STYLE);  

        
        Label currentPasswordLabel = new Label("Mevcut Şifre");  
        currentPasswordLabel.setStyle("-fx-font-weight: bold;");  
        PasswordField currentPasswordField = new PasswordField();  
        currentPasswordField.setStyle(INPUT_STYLE);  

        Label newPasswordLabel = new Label("Yeni Şifre");  
        newPasswordLabel.setStyle("-fx-font-weight: bold;");  
        PasswordField newPasswordField = new PasswordField();  
        newPasswordField.setStyle(INPUT_STYLE);  

        Label confirmPasswordLabel = new Label("Yeni Şifre Tekrar");  
        confirmPasswordLabel.setStyle("-fx-font-weight: bold;");  
        PasswordField confirmPasswordField = new PasswordField();  
        confirmPasswordField.setStyle(INPUT_STYLE);  

         
        Button saveButton = new Button("Kaydet");  
        saveButton.setStyle(SAVE_BUTTON_STYLE);  
        saveButton.setOnAction(e -> {  
            String currentPassword = currentPasswordField.getText().trim();  
            String newPassword = newPasswordField.getText().trim();  
            String confirmPassword = confirmPasswordField.getText().trim();  
            String newUsername = newUsernameField.getText().trim();  

             
            if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty() || newUsername.isEmpty()) {  
                app.showAlert("Uyarı", "Lütfen tüm alanları doldurun.");  
                return;  
            }  
            if (!newPassword.equals(confirmPassword)) {  
                app.showAlert("Uyarı", "Yeni şifreler eşleşmiyor.");  
                return;  
            }  
            if (!validatePassword(app.getCurrentUser(), currentPassword)) {  
                app.showAlert("Hata", "Mevcut şifre yanlış.");  
                return;  
            }  

          
            boolean passwordUpdated = updatePassword(app.getCurrentUser(), newPassword);  
            
            boolean usernameUpdated = updateUsername(app.getCurrentUser(), newUsername);  

            if (passwordUpdated && usernameUpdated) {  
                app.showAlert("Başarılı", "Kullanıcı adı ve şifreniz başarıyla güncellendi.");  
                app.setCurrentUser(newUsername);  
            } else {  
                app.showAlert("Hata", "Güncellenirken bir sorun oluştu.");  
            }  
            
            currentPasswordField.clear();  
            newPasswordField.clear();  
            confirmPasswordField.clear();  
            newUsernameField.clear();  
        });  

         
        Button backButton = new Button("Ana Sayfaya Dön");  
        backButton.setStyle(BACK_BUTTON_STYLE);  
        backButton.setOnAction(e -> stage.setScene(HomePage.createHomePage(stage, app)));  

      
        profileCard.getChildren().addAll(  
            titleLabel,  
            currentUsernameLabel,  
            newUsernameLabel,  
            newUsernameField,  
            currentPasswordLabel,  
            currentPasswordField,  
            newPasswordLabel,  
            newPasswordField,  
            confirmPasswordLabel,  
            confirmPasswordField,  
            saveButton,  
            backButton  
        );  

        mainLayout.getChildren().add(profileCard);  

        Scene scene = new Scene(mainLayout, 500, 700);  
        return scene;  
    }  
}