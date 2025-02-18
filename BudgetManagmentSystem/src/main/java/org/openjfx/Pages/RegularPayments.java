package org.openjfx.Pages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.openjfx.BudgetApp;

public class RegularPayments {
    private static final String MAIN_BACKGROUND = 
        "-fx-background-color: linear-gradient(to bottom right, #ECF0F1, #BDC3C7);";
    
    private static final String LIST_STYLE = 
        "-fx-background-color: rgba(255,255,255,0.8);" +
        "-fx-background-radius: 20;" +
        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0.0, 0.0, 3);";
    
    private static final String INPUT_STYLE =
        "-fx-background-color: white;" +
        "-fx-background-radius: 10;" +
        "-fx-border-color: #BDC3C7;" +
        "-fx-border-radius: 10;" +
        "-fx-padding: 8;";
    
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

    public static Scene createRegularPaymentsPage(Stage stage, BudgetApp app) {
       
        Label titleLabel = new Label("Düzenli Ödemeler");
        titleLabel.setFont(Font.font(24));
        titleLabel.setTextFill(Color.rgb(44, 62, 80));

        
        ListView<String> regularPaymentsList = new ListView<>();
        regularPaymentsList.setStyle(LIST_STYLE);
        regularPaymentsList.setPrefHeight(300);
        regularPaymentsList.getItems().addAll(app.getRegularPayments());

       
        ComboBox<String> paymentTypeComboBox = new ComboBox<>();
        paymentTypeComboBox.setStyle(INPUT_STYLE);
        paymentTypeComboBox.setPromptText("Ödeme Türü");
        paymentTypeComboBox.getItems().addAll("Kira", "Aidat", "Elektrik", "Su", "İnternet");
        paymentTypeComboBox.setMaxWidth(300);

        ComboBox<String> frequencyComboBox = new ComboBox<>();
        frequencyComboBox.setStyle(INPUT_STYLE);
        frequencyComboBox.setPromptText("Ödeme Sıklığı");
        frequencyComboBox.getItems().addAll("Aylık", "Yıllık");
        frequencyComboBox.setMaxWidth(300);

       
        TextField amountField = new TextField();
        amountField.setStyle(INPUT_STYLE);
        amountField.setPromptText("Miktar");
        amountField.setMaxWidth(300);

        DatePicker nextPaymentDateField = new DatePicker();
        nextPaymentDateField.setStyle(INPUT_STYLE);
        nextPaymentDateField.setPromptText("Sonraki Ödeme Tarihi (yyyy-mm-dd)");
        nextPaymentDateField.setMaxWidth(300);

        
        Button addButton = new Button("Ekle");
        addButton.setStyle(BUTTON_STYLE);
        addButton.setOnMouseEntered(e -> addButton.setStyle(BUTTON_STYLE + BUTTON_HOVER_STYLE));
        addButton.setOnMouseExited(e -> addButton.setStyle(BUTTON_STYLE));
        addButton.setMaxWidth(300);

        Button backButton = new Button("Geri");
        backButton.setStyle(BUTTON_STYLE + "-fx-background-color: #E74C3C;");
        backButton.setOnMouseEntered(e -> backButton.setStyle(BUTTON_STYLE + "-fx-background-color: #C0392B;" + BUTTON_HOVER_STYLE));
        backButton.setOnMouseExited(e -> backButton.setStyle(BUTTON_STYLE + "-fx-background-color: #E74C3C;"));
        backButton.setMaxWidth(300);

        
        addButton.setOnAction(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                String paymentType = paymentTypeComboBox.getValue();
                String frequency = frequencyComboBox.getValue();
                String nextPaymentDate = nextPaymentDateField.getValue().toString();

                if (paymentType == null || frequency == null || nextPaymentDate.isEmpty()) {
                    app.showAlert("Hata", "Lütfen ödeme türü, sıklığı ve tarihini girin.");
                    return;
                }

                app.addRegularPayment(paymentType, amount, frequency, nextPaymentDate);

                amountField.clear();
                nextPaymentDateField.setValue(null);
                paymentTypeComboBox.setValue(null);
                frequencyComboBox.setValue(null);

                regularPaymentsList.getItems().clear();
                regularPaymentsList.getItems().addAll(app.getRegularPayments());

                app.showAlert("Başarılı", "Düzenli ödeme başarıyla eklendi.");
            } catch (NumberFormatException ex) {
                app.showAlert("Hata", "Lütfen geçerli bir sayı girin.");
            }
        });

        backButton.setOnAction(e -> stage.setScene(HomePage.createHomePage(stage, app)));

        
        VBox root = new VBox(20);
        root.setStyle(MAIN_BACKGROUND);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(30));
        
      
        VBox inputContainer = new VBox(15);
        inputContainer.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 20;" +
            "-fx-padding: 20;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0.0, 0.0, 3);"
        );
        inputContainer.setMaxWidth(400);
        inputContainer.setAlignment(Pos.CENTER);
        inputContainer.getChildren().addAll(
            paymentTypeComboBox,
            frequencyComboBox,
            amountField,
            nextPaymentDateField,
            addButton
        );

        root.getChildren().addAll(
            titleLabel,
            regularPaymentsList,
            inputContainer,
            backButton
        );

        return new Scene(root, 800, 700);
    }
}