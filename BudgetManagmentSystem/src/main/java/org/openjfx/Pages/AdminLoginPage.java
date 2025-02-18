package org.openjfx.Pages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.openjfx.BudgetApp;
import org.openjfx.database.DatabaseConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminLoginPage {

    public static Scene createAdminLoginPage(Stage stage, BudgetApp app) {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

       
        Label titleLabel = new Label("Admin Girişi");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Kullanıcı Adı");
        usernameField.setMaxWidth(300);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Şifre");
        passwordField.setMaxWidth(300);

        Button loginButton = new Button("Giriş Yap");
        loginButton.setStyle("-fx-background-color:rgb(26, 117, 170); -fx-text-fill: white;");

        Label messageLabel = new Label("");
        messageLabel.setStyle("-fx-text-fill: red;");

    
         loginButton.setOnAction(e -> {  
            String username = usernameField.getText();  
            String password = passwordField.getText();  

            try (Connection conn = DatabaseConnector.getInstance().getConnection()) {  
                String query = "SELECT role FROM users WHERE username = ? AND password = ?";  
                PreparedStatement pstmt = conn.prepareStatement(query);  
                pstmt.setString(1, username);  
                pstmt.setString(2, password);  
                ResultSet rs = pstmt.executeQuery();  

                if (rs.next()) {  
                    String role = rs.getString("role");  
                    if ("ADMIN".equals(role)) {  
                        messageLabel.setStyle("-fx-text-fill: green;");  
                        messageLabel.setText("Giriş başarılı! Yönlendiriliyorsunuz...");  
                        Scene adminPanel = AdminPanelPage.createAdminPanelPage(stage, app);  
                        stage.setScene(adminPanel);  
                    } else {  
                        messageLabel.setText("Yetkiniz bulunmamaktadır!");  
                    }  
                } else {  
                    messageLabel.setText("Kullanıcı adı veya şifre hatalı!");  
                }  
            } catch (Exception ex) {  
                messageLabel.setText("Bağlantı hatası: " + ex.getMessage());  
                ex.printStackTrace();  
            }  
        });  

        
        Button backButton = new Button("Geri Dön");
        backButton.setStyle("-fx-background-color:rgb(26, 117, 170); -fx-text-fill: white;");
        backButton.setOnAction(e -> {
            stage.setScene(LoginPage.createLoginPage(stage, app));  
        });

       
        layout.getChildren().addAll(
            titleLabel,
            usernameField,
            passwordField,
            loginButton,
            backButton,
            messageLabel
           
        );

        Scene scene = new Scene(layout, 750, 750);
        

        return scene;
    }
}