package org.openjfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.openjfx.Pages.LoginPage;
import org.openjfx.database.DatabaseConnector;

public class BudgetApp extends Application {


    public String getCurrentUser() {  
        String username = null;  
        String query = "SELECT username FROM users WHERE id = ?";  
        try (Connection conn = DatabaseConnector.getInstance().getConnection();  
             PreparedStatement stmt = conn.prepareStatement(query)) {  

            stmt.setInt(1, activeUserId);  

           
            ResultSet rs = stmt.executeQuery();  

             
            if (rs.next()) {  
                username = rs.getString("username");  
            }  

        } catch (Exception e) {  
            e.printStackTrace();  
        }  

        return username;  
    }
    public void setCurrentUser(String username) {  
        try (Connection connection = DatabaseConnector.getInstance().getConnection()) {  
            String query = "UPDATE users SET username = ? WHERE id = ?";  
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {  
                pstmt.setString(1, username);  
                pstmt.setInt(2, activeUserId);  

                int affectedRows = pstmt.executeUpdate();  
                if (affectedRows > 0) {  
                    
                    System.out.println("Kullanıcı adı güncellendi: " + username);  
                }  
            }  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    } 

    private double totalBudget = 0.0; 
    private int activeUserId = -1;

    
    public static void main(String[] args) {  
        launch(args);  
    }  

    @SuppressWarnings("exports")
    @Override
    public void start(Stage stage) {
        stage.setTitle("Budget Management System");

        
        Scene loginScene = LoginPage.createLoginPage(stage, this);
        stage.setScene(loginScene);
        stage.show();
    }

    public int authenticateUser(String username, String password) {  
        try (Connection connection = DatabaseConnector.getInstance().getConnection()) {  
            String query = "SELECT id FROM users WHERE username = ? AND password = ?";  
            try (PreparedStatement stmt = connection.prepareStatement(query)) {  
                stmt.setString(1, username);  
                stmt.setString(2, password);  

                ResultSet rs = stmt.executeQuery();  
                if (rs.next()) {  
                    return rs.getInt("id"); 
                }  
            }  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
        return -1;  
    } 

    public void setActiveUserId(int userId) {
        this.activeUserId = userId;
    }

    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void addIncome(double amount, String category) {  
        try (Connection connection = DatabaseConnector.getInstance().getConnection()) {  
            String query = "INSERT INTO transactions (amount, category_id, transaction_type_id, user_id) VALUES (?, ?, ?, ?)";  
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {  
                pstmt.setDouble(1, amount);  
                pstmt.setInt(2, getCategoryId(category));  
                pstmt.setInt(3, getTransactionTypeId("Gelir"));  
                pstmt.setInt(4, activeUserId);   
                pstmt.executeUpdate();  
            }  
            calculateTotalBudget(); 
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    }
    
    public void addExpense(double amount, String category) {  
        try (Connection connection = DatabaseConnector.getInstance().getConnection()) {  
            String query = "INSERT INTO transactions (amount, category_id, transaction_type_id, user_id) VALUES (?, ?, ?, ?)";  
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {  
                pstmt.setDouble(1, amount);  
                pstmt.setInt(2, getCategoryId(category));   
                pstmt.setInt(3, getTransactionTypeId("Gider"));  
                pstmt.setInt(4, activeUserId);   
                pstmt.executeUpdate();  
            }  
            calculateTotalBudget(); 
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    }

    public void calculateTotalBudget() {  
        try (Connection connection = DatabaseConnector.getInstance().getConnection();  
             PreparedStatement pstmt = connection.prepareStatement("SELECT " +  
                     "(SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE transaction_type_id = ? AND user_id = ?) - " +  
                     "(SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE transaction_type_id = ? AND user_id = ?) AS total_budget")) {  
             
            pstmt.setInt(1, getTransactionTypeId("Gelir")); 
            pstmt.setInt(2, activeUserId); 
            pstmt.setInt(3, getTransactionTypeId("Gider")); 
            pstmt.setInt(4, activeUserId);  
            
            try (ResultSet rs = pstmt.executeQuery()) {  
                if (rs.next()) {  
                    this.totalBudget = rs.getDouble("total_budget");  
                }  
            }  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    } 

    public ArrayList<String> getUserNotifications(int userId) {  
        ArrayList<String> notifications = new ArrayList<>();  
        try (Connection connection = DatabaseConnector.getInstance().getConnection()) {  
            String query = "SELECT message FROM notifications WHERE user_id = ? AND seen = FALSE";  
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {  
                pstmt.setInt(1, userId);  
                ResultSet rs = pstmt.executeQuery();  
                while (rs.next()) {  
                    notifications.add(rs.getString("message"));  
                }  
            }  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
        return notifications;  
    } 

    public void markNotificationAsSeen(int notificationId) {  
        try (Connection connection = DatabaseConnector.getInstance().getConnection()) {  
            String query = "UPDATE notifications SET seen = TRUE WHERE id = ?";  
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {  
                pstmt.setInt(1, notificationId);  
                pstmt.executeUpdate();  
            }  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    } 

    private int getTransactionTypeId(String typeName) {  
        try (Connection connection = DatabaseConnector.getInstance().getConnection()) {  
            String query = "SELECT id FROM transaction_types WHERE type_name = ?";  
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {  
                pstmt.setString(1, typeName);  
                ResultSet rs = pstmt.executeQuery();  
                if (rs.next()) {  
                    return rs.getInt("id");  
                } else {  
                    throw new SQLException("Transaction type not found for: " + typeName);  
                }  
            }  
        } catch (SQLException e) {  
            e.printStackTrace();  
            return -1; 
        }  
    }

    public ArrayList<String> getCategories() {  
        ArrayList<String> categories = new ArrayList<>();  
        try (Connection connection = DatabaseConnector.getInstance().getConnection()) {  
            String query = "SELECT name FROM categories";  
            try (PreparedStatement pstmt = connection.prepareStatement(query);  
                 ResultSet rs = pstmt.executeQuery()) {  
                while (rs.next()) {  
                    categories.add(rs.getString("name"));  
                }  
            }  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
        return categories;  
    }

    public boolean isUserActive(int userId) {  
        try (Connection conn = DatabaseConnector.getInstance().getConnection()) {  
            String query = "SELECT is_active FROM users WHERE id = ?";  
            PreparedStatement pstmt = conn.prepareStatement(query);  
            pstmt.setInt(1, userId);  
            ResultSet rs = pstmt.executeQuery();  
            
            if (rs.next()) {  
                return rs.getBoolean("is_active");  
            }  
        } catch (SQLException e) {  
            System.out.println("Kullanıcı durumu kontrol edilirken hata oluştu: " + e.getMessage());  
        }  
        return false; // Varsayılan olarak false döner  
    }

    public int getCurrentUserId() {  
        return activeUserId; // Kullanıcının aktif ID'sini döndür  
    }

    private int getCategoryId(String category) {  
        try (Connection connection = DatabaseConnector.getInstance().getConnection()) {  
            String query = "SELECT id FROM categories WHERE name = ?";  
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {  
                pstmt.setString(1, category);  
                ResultSet rs = pstmt.executeQuery();  
                if (rs.next()) {  
                    return rs.getInt("id");  
                }  
            }  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
        return -1;  
    }  

    public double getTotalBudget() {  
        return this.totalBudget;  
    } 

    public ArrayList<String> getExpenses() {  
        ArrayList<String> expenses = new ArrayList<>();  
        try (Connection connection = DatabaseConnector.getInstance().getConnection()) {  
            String query = "SELECT amount, category_id, transaction_type_id FROM transactions WHERE user_id = ?";  
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {  
                pstmt.setInt(1, activeUserId);  
                ResultSet rs = pstmt.executeQuery();  
                while (rs.next()) {  
                    double amount = rs.getDouble("amount");  
                    int categoryId = rs.getInt("category_id");  
                    String categoryName = getCategoryName(categoryId);  
                    int transactionTypeId = rs.getInt("transaction_type_id");  
                    String transactionType = transactionTypeId == 1 ? "Gelir" : "Gider";  
                    expenses.add(transactionType + ": " + amount + " | Kategori: " + categoryName);  
                }  
            }  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
        return expenses;  
    }

    public void addRegularPayment(String paymentType, double amount, String frequency, String nextPaymentDate) {  
        try (Connection connection = DatabaseConnector.getInstance().getConnection()) {  
            String query = "INSERT INTO regular_payments (payment_type, amount, frequency, next_payment_date, user_id) VALUES (?, ?, ?, ?, ?)";  
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {  
                pstmt.setString(1, paymentType);  
                pstmt.setDouble(2, amount);  
                pstmt.setString(3, frequency);  
                pstmt.setString(4, nextPaymentDate);  
                pstmt.setInt(5, activeUserId);    
                pstmt.executeUpdate();  
            }  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    }

    public ArrayList<String> getRegularPayments() {  
        ArrayList<String> regularPayments = new ArrayList<>();  
        try (Connection connection = DatabaseConnector.getInstance().getConnection()) {  
            String query = "SELECT payment_type, amount, frequency, next_payment_date FROM regular_payments WHERE user_id = ?";  
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {  
                pstmt.setInt(1, activeUserId);  
                ResultSet rs = pstmt.executeQuery();  
                while (rs.next()) {  
                    String paymentType = rs.getString("payment_type");  
                    double amount = rs.getDouble("amount");  
                    String frequency = rs.getString("frequency");  
                    String nextPaymentDate = rs.getString("next_payment_date");  
                    regularPayments.add(paymentType + ": " + amount + " | Sıklık: " + frequency + " | Sonraki Ödeme Tarihi: " + nextPaymentDate);  
                }  
            }  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
        return regularPayments;  
    }


    private String getCategoryName(int categoryId) {  
        try (Connection connection = DatabaseConnector.getInstance().getConnection()) {  
            String query = "SELECT name FROM categories WHERE id = ?";  
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {  
                pstmt.setInt(1, categoryId);  
                ResultSet rs = pstmt.executeQuery();  
                if (rs.next()) {  
                    return rs.getString("name");  
                }  
            }  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
        return "Bilinmiyor";   
    } 
    
    public int getUserId() {
        return activeUserId;
    }
    
    public void addFinancialGoal(String goalName, double targetAmount, double initialAmount, String deadline) {  
        try (Connection connection = DatabaseConnector.getInstance().getConnection()) {  
            String query = "INSERT INTO goals (goal_name, target_amount, current_amount, deadline, user_id) VALUES (?, ?, ?, ?, ?)";  
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {  
                pstmt.setString(1, goalName);  
                pstmt.setDouble(2, targetAmount);  
                pstmt.setDouble(3, initialAmount);  
                pstmt.setString(4, deadline);  
                pstmt.setInt(5, getUserId());  
                pstmt.executeUpdate();  
            }  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    }
    
    public void deleteFinancialGoal(String goalName) {  
        try (Connection connection = DatabaseConnector.getInstance().getConnection()) {  
            String query = "DELETE FROM goals WHERE user_id = ? AND goal_name = ?";  
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {  
                pstmt.setInt(1, getUserId());  
                pstmt.setString(2, goalName);  
                pstmt.executeUpdate();  
            }  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    } 


        public void addAmountToGoal(String goalName, double addedAmount) {  
            try (Connection connection = DatabaseConnector.getInstance().getConnection()) {  
                String query = "UPDATE goals SET current_amount = current_amount + ? WHERE user_id = ? AND goal_name = ?";  
                try (PreparedStatement pstmt = connection.prepareStatement(query)) {  
                    pstmt.setDouble(1, addedAmount);  
                    pstmt.setInt(2, getUserId());  
                    pstmt.setString(3, goalName);  
                    pstmt.executeUpdate();  
                }  
            } catch (SQLException e) {  
                e.printStackTrace();  
            }  
        }  
        
        public ArrayList<String> getFinancialGoals() {  
        ArrayList<String> financialGoals = new ArrayList<>();  
        try (Connection connection = DatabaseConnector.getInstance().getConnection()) {  
            String query = "SELECT goal_name, target_amount, current_amount, deadline FROM goals WHERE user_id = ?";  
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {  
                pstmt.setInt(1, getUserId());  
                ResultSet rs = pstmt.executeQuery();  
                while (rs.next()) {  
                    String goalName = rs.getString("goal_name");  
                    double targetAmount = rs.getDouble("target_amount");  
                    double currentAmount = rs.getDouble("current_amount");  
                    String deadline = rs.getString("deadline");  
                    financialGoals.add(goalName + "|" + targetAmount + "|" + currentAmount + "|" + deadline);  
                }  
            }  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
        return financialGoals;  
    }  

    public boolean registerUser(String username, String password) {  
        try (Connection connection = DatabaseConnector.getInstance().getConnection()) {  
            String query = "INSERT INTO users (username, password) VALUES (?, ?)";  
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {  
                pstmt.setString(1, username);  
                pstmt.setString(2, password);  
                pstmt.executeUpdate();  
                return true; 
            }  
        } catch (SQLException e) {  
            e.printStackTrace();  
            return false; 
        }  
    }  

} 