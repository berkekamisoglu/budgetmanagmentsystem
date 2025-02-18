package org.openjfx.Pages;  

import javafx.geometry.Insets;  
import javafx.geometry.Pos;  
import javafx.scene.Scene;  
import javafx.scene.control.*;  
import javafx.scene.layout.HBox;  
import javafx.scene.layout.VBox;  
import javafx.scene.paint.Color;  
import javafx.scene.text.Font;  
import javafx.stage.Stage;  
import org.openjfx.BudgetApp;  

import java.util.ArrayList;  

public class FinancialGoalsPage {  
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

    public static Scene createFinancialGoalsPage(Stage stage, BudgetApp app) {  
    
        Label titleLabel = new Label("Finansal Hedefler");  
        titleLabel.setFont(Font.font(24));  
        titleLabel.setTextFill(Color.rgb(44, 62, 80));  

       
        ListView<HBox> financialGoalsList = new ListView<>();  
        financialGoalsList.setStyle(LIST_STYLE);  

        
        updateGoalsList(app, financialGoalsList);  

       
        VBox inputBox = new VBox(10);  
        inputBox.setStyle(  
            "-fx-background-color: white;" +  
            "-fx-background-radius: 10;" +  
            "-fx-padding: 15;"  
        );  

        TextField goalNameField = createStyledTextField("Hedef Adı");  
        TextField targetAmountField = createStyledTextField("Hedef Tutarı");  
        TextField initialAmountField = createStyledTextField("Başlangıç Tutarı");  
        DatePicker deadlinePicker = new DatePicker();  
        deadlinePicker.setStyle(  
            "-fx-background-color: white;" +  
            "-fx-background-radius: 10;" +  
            "-fx-border-color: #BDC3C7;" +  
            "-fx-border-radius: 10;"  
        );  
        deadlinePicker.setPromptText("Son Tarih");  

        Button addGoalButton = new Button("Hedef Ekle");  
        addGoalButton.setStyle(BUTTON_STYLE);  
        addGoalButton.setOnMouseEntered(e -> addGoalButton.setStyle(BUTTON_STYLE + BUTTON_HOVER_STYLE));  
        addGoalButton.setOnMouseExited(e -> addGoalButton.setStyle(BUTTON_STYLE));  

        addGoalButton.setOnAction(e -> {  
            try {  
                String goalName = goalNameField.getText();  
                double targetAmount = Double.parseDouble(targetAmountField.getText());  
                double initialAmount = Double.parseDouble(initialAmountField.getText());  
                String deadline = deadlinePicker.getValue().toString();  

                app.addFinancialGoal(goalName, targetAmount, initialAmount, deadline);  

                updateGoalsList(app, financialGoalsList);  
                clearInputFields(goalNameField, targetAmountField, initialAmountField, deadlinePicker);  
            } catch (Exception ex) {  
                showAlert("Hata", "Lütfen tüm alanları doğru şekilde doldurun.");  
            }  
        });  

        
        Button backButton = new Button("Geri");  
        backButton.setStyle(BUTTON_STYLE + "-fx-background-color: #E74C3C;");  
        backButton.setOnMouseEntered(e -> backButton.setStyle(BUTTON_STYLE + "-fx-background-color: #C0392B;" + BUTTON_HOVER_STYLE));  
        backButton.setOnMouseExited(e -> backButton.setStyle(BUTTON_STYLE + "-fx-background-color: #E74C3C;"));  
        backButton.setOnAction(e -> stage.setScene(HomePage.createHomePage(stage, app)));  

        inputBox.getChildren().addAll(  
            goalNameField,   
            targetAmountField,   
            initialAmountField,   
            deadlinePicker,   
            addGoalButton  
        );  

   
        VBox root = new VBox(20);  
        root.setStyle(MAIN_BACKGROUND);  
        root.setAlignment(Pos.CENTER);  
        root.setPadding(new Insets(20));  
        root.getChildren().addAll(  
            titleLabel,   
            financialGoalsList,   
            inputBox,   
            backButton  
        );  

        return new Scene(root, 800, 700);  
    }  

    private static void updateGoalsList(BudgetApp app, ListView<HBox> financialGoalsList) {  
        financialGoalsList.getItems().clear();  
        ArrayList<String> goals = app.getFinancialGoals();  
        
        for (String goal : goals) {  
            String[] goalParts = goal.split("\\|");  
            String goalName = goalParts[0];  
            double targetAmount = Double.parseDouble(goalParts[1]);  
            double currentAmount = Double.parseDouble(goalParts[2]);  
            String deadline = goalParts[3];  
            double remainingAmount = targetAmount - currentAmount;  

            ProgressBar progressBar = new ProgressBar(currentAmount / targetAmount);  
            progressBar.setStyle(  
                "-fx-accent: #2196F3;" +  
                "-fx-control-inner-background: #E0E0E0;"  
            );  

            Label goalLabel = new Label(goalName + " (" + currentAmount + " / " + targetAmount + " TL)");  
            goalLabel.setStyle("-fx-font-weight: bold;");  

            Label deadlineLabel = new Label("Son Tarih: " + deadline);  
            deadlineLabel.setStyle("-fx-text-fill: #757575;");  

            Button addAmountButton = createStyledButton("Ekleme Yap", "#2196F3");  
            addAmountButton.setOnAction(e -> {  
                TextInputDialog dialog = new TextInputDialog();  
                dialog.setTitle("Ekleme Yap");  
                dialog.setHeaderText(null);  
                dialog.setContentText("Eklemek istediğiniz tutarı girin:");  

                dialog.showAndWait().ifPresent(input -> {  
                    try {  
                        double addedAmount = Double.parseDouble(input);  
                        app.addAmountToGoal(goalName, addedAmount);  
                        updateGoalsList(app, financialGoalsList);  
                    } catch (Exception ex) {  
                        showAlert("Hata", "Geçerli bir tutar girin.");  
                    }  
                });  
            });  

            Button deleteButton = createStyledButton("Sil", "#F44336");  
            deleteButton.setOnAction(e -> {  
                app.deleteFinancialGoal(goalName);  
                updateGoalsList(app, financialGoalsList);  
            });  

            HBox goalBox = new HBox(15);  
            goalBox.setAlignment(Pos.CENTER_LEFT);  
            goalBox.getChildren().addAll(  
                new VBox(5, goalLabel, deadlineLabel),   
                progressBar,   
                new Label(String.format("Kalan: %.2f TL", remainingAmount)),   
                addAmountButton,   
                deleteButton  
            );  
            goalBox.setStyle(  
                "-fx-background-color: white;" +  
                "-fx-background-radius: 10;" +  
                "-fx-padding: 10;" +  
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 3, 0.0, 0.0, 1);"  
            );  

            financialGoalsList.getItems().add(goalBox);  
        }  

        if (financialGoalsList.getItems().isEmpty()) {  
            financialGoalsList.getItems().add(new HBox(new Label("Henüz bir hedef eklenmedi.")));  
        }  
    }  

    private static TextField createStyledTextField(String promptText) {  
        TextField textField = new TextField();  
        textField.setPromptText(promptText);  
        textField.setStyle(  
            "-fx-background-color: white;" +  
            "-fx-background-radius: 10;" +  
            "-fx-border-color: #BDC3C7;" +  
            "-fx-border-radius: 10;"  
        );  
        return textField;  
    }  

    private static Button createStyledButton(String text, String color) {  
        Button button = new Button(text);  
        button.setStyle(  
            "-fx-background-color: " + color + ";" +  
            "-fx-text-fill: white;" +  
            "-fx-background-radius: 20;" +  
            "-fx-padding: 5 10;"  
        );  
        button.setOnMouseEntered(e -> button.setStyle(  
            "-fx-background-color: derive(" + color + ", -20%);" +  
            "-fx-text-fill: white;" +  
            "-fx-background-radius: 20;" +  
            "-fx-padding: 5 10;" +  
            "-fx-scale-x: 1.05;" +  
            "-fx-scale-y: 1.05;"  
        ));  
        button.setOnMouseExited(e -> button.setStyle(  
            "-fx-background-color: " + color + ";" +  
            "-fx-text-fill: white;" +  
            "-fx-background-radius: 20;" +  
            "-fx-padding: 5 10;"  
        ));  
        return button;  
    }  

    private static void clearInputFields(TextField goalNameField, TextField targetAmountField, TextField initialAmountField, DatePicker deadlinePicker) {  
        goalNameField.clear();  
        targetAmountField.clear();  
        initialAmountField.clear();  
        deadlinePicker.setValue(null);  
    } 

    private static void showAlert(String title, String content) {  
        Alert alert = new Alert(Alert.AlertType.ERROR);  
        alert.setTitle(title);  
        alert.setHeaderText(null);  
        alert.setContentText(content);  
        alert.showAndWait();  
    }  
}