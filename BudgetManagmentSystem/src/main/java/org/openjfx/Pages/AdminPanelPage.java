package org.openjfx.Pages;  

import javafx.collections.FXCollections;  
import javafx.collections.ObservableList;  
import javafx.geometry.Insets;  
import javafx.scene.Scene;  
import javafx.scene.control.*;  
import javafx.scene.control.cell.PropertyValueFactory;  
import javafx.scene.layout.*;  
import javafx.stage.Stage;  
import java.sql.*;  
import org.openjfx.BudgetApp;  
import org.openjfx.database.DatabaseConnector;  

public class AdminPanelPage {  

    public static class User {  
        private int id;  
        private String username;  
        private String role;  
        private boolean isActive;  
        private String password;  
    
        public User(int id, String username, String role, boolean isActive, String password) {  
            this.id = id;  
            this.username = username;  
            this.role = role;  
            this.isActive = isActive;  
            this.password = password;  
        }  
    
        public int getId() { return id; }  
        public String getUsername() { return username; }  
        public String getRole() { return role; }  
        public String getPassword() { return password; }  
        public boolean isActive() { return isActive; }  
    }  
    
    public static Scene createAdminPanelPage(Stage stage, BudgetApp app) {  
        BorderPane mainLayout = new BorderPane();  
        mainLayout.setPadding(new Insets(20));  
    
        Label titleLabel = new Label("Admin Paneli - Kullanıcı Yönetimi");  
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");  
        mainLayout.setTop(titleLabel);  
    
      
        TableView<User> userTable = new TableView<>();  
    
        TableColumn<User, Integer> idColumn = new TableColumn<>("ID");  
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));  
    
        TableColumn<User, String> usernameColumn = new TableColumn<>("Kullanıcı Adı");  
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));  
    
        TableColumn<User, String> passwordColumn = new TableColumn<>("Şifre");   
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));   
    
        TableColumn<User, String> roleColumn = new TableColumn<>("Rol");  
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));  
    
        TableColumn<User, Boolean> statusColumn = new TableColumn<>("Durum");  
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("active"));  
        statusColumn.setCellFactory(col -> new TableCell<User, Boolean>() {  
            @Override  
            protected void updateItem(Boolean isActive, boolean empty) {  
                super.updateItem(isActive, empty);  
                if (empty || isActive == null) {  
                    setText(null);  
                } else {                                                  
                    setText(isActive ? "Aktif" : "Pasif");  
                }  
            }  
        });  
    
        userTable.getColumns().addAll(idColumn, usernameColumn, passwordColumn, roleColumn, statusColumn);  
        
        loadUsers(userTable);  
        TextField newPasswordField = new TextField();  
        newPasswordField.setPromptText("Yeni Şifre"); // Placeholder  

        Button changePasswordButton = new Button("Şifreyi Değiştir");  

       
        VBox controlPanel = new VBox(10);  
        controlPanel.setPadding(new Insets(0, 0, 0, 20));  
        controlPanel.setPrefWidth(200);  

        Button viewDetailsButton = new Button("Detayları Görüntüle");  
        Button toggleStatusButton = new Button("Durum Değiştir");  
        Button deleteUserButton = new Button("Kullanıcıyı Sil");  
        Button returnButton = new Button("Çıkış Yap");  
        ComboBox<String> roleComboBox = new ComboBox<>(FXCollections.observableArrayList("USER", "ADMIN"));  
        Button changeRoleButton = new Button("Rolü Değiştir");  

      
        viewDetailsButton.setOnAction(e -> {  
            User selectedUser = userTable.getSelectionModel().getSelectedItem();  
            if (selectedUser != null) {  
                showUserDetails(selectedUser);  
            }  
        });  

        returnButton.setOnAction(e -> {  
            stage.setScene(AdminLoginPage.createAdminLoginPage(stage, app));  
            app.showAlert("Çıkış Yapıldı", "Başarıyla Çıkış Yapıldı.");  
        });   

        toggleStatusButton.setOnAction(e -> {  
            User selectedUser = userTable.getSelectionModel().getSelectedItem();  
            if (selectedUser != null) {  
                toggleUserStatus(selectedUser, userTable);  
            }  
        });  

        deleteUserButton.setOnAction(e -> {  
            User selectedUser = userTable.getSelectionModel().getSelectedItem();  
            if (selectedUser != null) {  
                deleteUser(selectedUser, userTable);  
            }  
        });  

        changeRoleButton.setOnAction(e -> {  
            User selectedUser = userTable.getSelectionModel().getSelectedItem();  
            String newRole = roleComboBox.getValue();  
            if (selectedUser != null && newRole != null) {  
                changeUserRole(selectedUser, newRole, userTable);  
            }  
        });  
        
        changePasswordButton.setOnAction(e -> {  
            User selectedUser = userTable.getSelectionModel().getSelectedItem();  
            String newPassword = newPasswordField.getText();  
            if (selectedUser != null && !newPassword.isEmpty()) {  
                changeUserPassword(selectedUser, newPassword, userTable);  
            } else {  
                showError("Lütfen geçerli bir şifre girin.");  
            }  
        });  

        controlPanel.getChildren().addAll(  
        viewDetailsButton,  
        toggleStatusButton,  
        deleteUserButton,  
        new Label("Yeni Rol:"),  
        roleComboBox,  
        changeRoleButton,  
        new Label("Yeni Şifre:"),   
        newPasswordField,   
        changePasswordButton,  
        returnButton  
        );   


      
        mainLayout.setCenter(userTable);  
        mainLayout.setRight(controlPanel);  

        Scene scene = new Scene(mainLayout, 900, 600);  

        return scene;  
    }  

    private static void changeUserPassword(User user, String newPassword, TableView<User> userTable) {  
        try (Connection conn = DatabaseConnector.getInstance().getConnection()) {  
            String query = "UPDATE users SET password = ? WHERE id = ?";  
            PreparedStatement pstmt = conn.prepareStatement(query);  
            pstmt.setString(1, newPassword);  
            pstmt.setInt(2, user.getId());  
            pstmt.executeUpdate();  

            loadUsers(userTable); // Tabloyu yenile  
            showInfo("Şifre başarıyla değiştirildi");  

        } catch (SQLException e) {  
            showError("Şifre güncellenirken hata oluştu: " + e.getMessage());  
        }  
    }   
   
    private static void loadUsers(TableView<User> userTable) {  
        try (Connection conn = DatabaseConnector.getInstance().getConnection()) {  
            String query = "SELECT id, username, role, password, is_active FROM users";  
            PreparedStatement pstmt = conn.prepareStatement(query);  
            ResultSet rs = pstmt.executeQuery();  

            ObservableList<User> users = FXCollections.observableArrayList();  
            while (rs.next()) {  
                users.add(new User(  
                    rs.getInt("id"),  
                    rs.getString("username"),  
                    rs.getString("role"),  
                    rs.getBoolean("is_active"),  
                    rs.getString("password")  
                ));  
            }  
            userTable.setItems(users);  

        } catch (SQLException e) {  
            showError("Kullanıcılar yüklenirken hata oluştu: " + e.getMessage());  
            e.printStackTrace();  
        }  
    }  

    private static void toggleUserStatus(User user, TableView<User> userTable) {  
        try (Connection conn = DatabaseConnector.getInstance().getConnection()) {  
            String query = "UPDATE users SET is_active = ? WHERE id = ?";  
            PreparedStatement pstmt = conn.prepareStatement(query);  
            pstmt.setBoolean(1, !user.isActive());  
            pstmt.setInt(2, user.getId());  
            pstmt.executeUpdate();  

            loadUsers(userTable);  
            showInfo("Kullanıcı durumu güncellendi");  

        } catch (SQLException e) {  
            showError("Durum güncellenirken hata oluştu: " + e.getMessage());  
        }  
    }   

    private static void deleteUser(User user, TableView<User> userTable) {  
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);  
        confirm.setTitle("Kullanıcı Silme");  
        confirm.setContentText("Bu kullanıcıyı silmek istediğinizden emin misiniz?");  

        confirm.showAndWait().ifPresent(response -> {  
            if (response == ButtonType.OK) {  
                deleteUserTransactions(user.getId());  
                try (Connection conn = DatabaseConnector.getInstance().getConnection()) {  
                    String query = "DELETE FROM users WHERE id = ?";  
                    PreparedStatement pstmt = conn.prepareStatement(query);  
                    pstmt.setInt(1, user.getId());  
                    pstmt.executeUpdate();  

                    loadUsers(userTable);  
                    showInfo("Kullanıcı silindi");  

                } catch (SQLException e) {  
                    showError("Kullanıcı silinirken hata oluştu: " + e.getMessage());  
                }  
            }  
        });  
    }   
     
    private static void deleteUserTransactions(int userId) {  
        try (Connection conn = DatabaseConnector.getInstance().getConnection()) {  
             
            String deleteTransactionsQuery = "DELETE FROM transactions WHERE user_id = ?";  
            try (PreparedStatement pstmt = conn.prepareStatement(deleteTransactionsQuery)) {  
                pstmt.setInt(1, userId);  
                pstmt.executeUpdate();  
            }  

            String deleteGoalsQuery = "DELETE FROM goals WHERE user_id = ?";  
            try (PreparedStatement pstmt = conn.prepareStatement(deleteGoalsQuery)) {  
                pstmt.setInt(1, userId);  
                pstmt.executeUpdate();  
            }  

            String deleteNotificationsQuery = "DELETE FROM notifications WHERE user_id = ?";  
            try (PreparedStatement pstmt = conn.prepareStatement(deleteNotificationsQuery)) {  
                pstmt.setInt(1, userId);  
                pstmt.executeUpdate();  
            }  
        
            String deleteRegularPaymentsQuery = "DELETE FROM regular_payments WHERE user_id = ?";  
            try (PreparedStatement pstmt = conn.prepareStatement(deleteRegularPaymentsQuery)) {  
                pstmt.setInt(1, userId);  
                pstmt.executeUpdate();  
            }  
            System.out.println("Kullanıcının tüm işlemleri başarıyla silindi.");  
        } catch (SQLException e) {  
            showError("İşlemler silinirken hata oluştu: " + e.getMessage());  
        }  
    }  
  
    private static void changeUserRole(User user, String newRole, TableView<User> userTable) {  
        try (Connection conn = DatabaseConnector.getInstance().getConnection()) {  
            String query = "UPDATE users SET role = ? WHERE id = ?";  
            PreparedStatement pstmt = conn.prepareStatement(query);  
            pstmt.setString(1, newRole);  
            pstmt.setInt(2, user.getId());  
            pstmt.executeUpdate();  

            loadUsers(userTable); 
            showInfo("Kullanıcı rolü güncellendi");  

        } catch (SQLException e) {  
            showError("Rol güncellenirken hata oluştu: " + e.getMessage());  
        }  
    }  

    private static void showUserDetails(User user) {  
        Alert details = new Alert(Alert.AlertType.INFORMATION);  
        details.setTitle("Kullanıcı Detayları");  
        details.setHeaderText("Kullanıcı: " + user.getUsername());  
        details.setContentText(  
            "ID: " + user.getId() + "\n" +  
            "Rol: " + user.getRole() + "\n" +  
            "Durum: " + (user.isActive() ? "Aktif" : "Pasif")  
        );  
        details.showAndWait();  
    }  

    private static void showError(String message) {  
        Alert alert = new Alert(Alert.AlertType.ERROR);  
        alert.setTitle("Hata");  
        alert.setContentText(message);  
        alert.showAndWait();  
    }  

    private static void showInfo(String message) {  
        Alert alert = new Alert(Alert.AlertType.INFORMATION);  
        alert.setTitle("Bilgi");  
        alert.setContentText(message);  
        alert.showAndWait();  
    }  
}