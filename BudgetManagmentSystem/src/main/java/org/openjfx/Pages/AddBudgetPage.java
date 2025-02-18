package org.openjfx.Pages;  

import javafx.geometry.Insets;  
import javafx.geometry.Pos;  
import javafx.scene.Scene;  
import javafx.scene.control.Button;  
import javafx.scene.control.ComboBox;  
import javafx.scene.control.Label;  
import javafx.scene.control.TextField;  
import javafx.scene.image.Image;  
import javafx.scene.image.ImageView;  
import javafx.scene.layout.VBox;  
import javafx.scene.paint.Color;  
import javafx.scene.text.Font;  
import javafx.stage.Stage;  
import org.openjfx.BudgetApp;  

public class AddBudgetPage {  
    // Stil sabitleri  
    private static final String MAIN_BACKGROUND =   
        "-fx-background-color: linear-gradient(to bottom right, #ECF0F1, #BDC3C7);";  
    
    private static final String FORM_BACKGROUND =   
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

    public static Scene createAddBudgetPage(Stage stage, BudgetApp app) {  
       
        app.calculateTotalBudget();  

     

        ImageView logoView = new ImageView();  
        try {  
            Image logo = new Image(BudgetApp.class.getResourceAsStream("icons/logo.png"));  
            logoView.setImage(logo);  
            logoView.setFitWidth(250);  
            logoView.setPreserveRatio(true);  
        } catch (Exception e) {  
            System.out.println("Logo yüklenirken hata: " + e.getMessage());  
        }   

        
        Label titleLabel = new Label("Gelir/Gider Ekle");  
        titleLabel.setFont(Font.font(24));  
        titleLabel.setTextFill(Color.rgb(44, 62, 80)); 

  
        Label transactionTypeLabel = new Label("İşlem Türü:");  
        transactionTypeLabel.setTextFill(Color.rgb(44, 62, 80));  
        ComboBox<String> transactionTypeComboBox = new ComboBox<>();  
        transactionTypeComboBox.getItems().addAll("Gelir", "Gider");  
        transactionTypeComboBox.setStyle(  
            "-fx-background-color: white;" +  
            "-fx-background-radius: 20;" +  
            "-fx-padding: 10;"  
        );  

       
        Label categoryLabel = new Label("Kategori:");  
        categoryLabel.setTextFill(Color.rgb(44, 62, 80));  
        ComboBox<String> categoryComboBox = new ComboBox<>();  
        categoryComboBox.getItems().addAll(app.getCategories());  
        categoryComboBox.setStyle(  
            "-fx-background-color: white;" +  
            "-fx-background-radius: 20;" +  
            "-fx-padding: 10;"  
        );  

       
        Label amountLabel = new Label("Tutar:");  
        amountLabel.setTextFill(Color.rgb(44, 62, 80));  
        TextField amountField = new TextField();  
        amountField.setPromptText("Miktar");  
        amountField.setStyle(  
            "-fx-background-color: white;" +  
            "-fx-background-radius: 20;" +  
            "-fx-padding: 10;"  
        );  

        Button addButton = new Button("Ekle");  
        Button backButton = new Button("Geri");  


        addButton.setStyle(BUTTON_STYLE);  
        backButton.setStyle(BUTTON_STYLE + "-fx-background-color: #E74C3C;");  

   
        addButton.setOnMouseEntered(e -> addButton.setStyle(BUTTON_STYLE + BUTTON_HOVER_STYLE));  
        addButton.setOnMouseExited(e -> addButton.setStyle(BUTTON_STYLE));  
        backButton.setOnMouseEntered(e -> backButton.setStyle(BUTTON_STYLE + "-fx-background-color: #C0392B;" + BUTTON_HOVER_STYLE));  
        backButton.setOnMouseExited(e -> backButton.setStyle(BUTTON_STYLE + "-fx-background-color: #E74C3C;"));  

        addButton.setOnAction(e -> {  
            try {  
                double amount = Double.parseDouble(amountField.getText());  
                String transactionType = transactionTypeComboBox.getValue();  
                String selectedCategory = categoryComboBox.getValue();  

                if (transactionType == null || selectedCategory == null) {  
                    app.showAlert("Hata", "Lütfen gelir/gider türü ve kategori seçin.");  
                    return;  
                }  

                
                if (transactionType.equals("Gelir")) {  
                    app.addIncome(amount, selectedCategory); 
                } else {  
                    app.addExpense(amount, selectedCategory);   
                }  
                amountField.clear();  
                app.showAlert("Başarılı", transactionType + " başarıyla eklendi.");  
            } catch (NumberFormatException ex) {  
                app.showAlert("Hata", "Lütfen geçerli bir sayı girin.");  
            }  
        });  

        backButton.setOnAction(e -> stage.setScene(HomePage.createHomePage(stage, app)));  

        VBox formLayout = new VBox(15);  
        formLayout.setStyle(FORM_BACKGROUND);  
        formLayout.setAlignment(Pos.CENTER);  
        formLayout.setPadding(new Insets(30));  
        formLayout.getChildren().addAll(  
            titleLabel,   
            transactionTypeLabel, transactionTypeComboBox,   
            categoryLabel, categoryComboBox,   
            amountLabel, amountField,   
            addButton, backButton  
        );  

     
        VBox root = new VBox(20);  
        root.setStyle(MAIN_BACKGROUND);  
        root.setAlignment(Pos.CENTER);  
        root.setPadding(new Insets(20));  
        root.getChildren().addAll(logoView, formLayout);  

        return new Scene(root, 750, 850);  
    }  
}