package org.openjfx.Pages;  

import javafx.animation.FadeTransition;  
import javafx.animation.Timeline;  
import javafx.animation.KeyFrame;  
import javafx.util.Duration;  
import javafx.geometry.Insets;  
import javafx.geometry.Pos;  
import javafx.scene.Scene;  
import javafx.scene.control.Button;  
import javafx.scene.control.Label;  
import javafx.scene.control.ListView;  
import javafx.scene.image.Image;  
import javafx.scene.image.ImageView;  
import javafx.scene.layout.*;  
import javafx.stage.Stage;  
import java.util.List;  
import org.openjfx.BudgetApp;  

public class HomePage {  
    private static int currentNotificationIndex = 0;  
    private static Timeline notificationTimer;  
    private static Label notificationLabel;  
    private static BudgetApp app;  

    private static final String SIDE_MENU_STYLE =   
        "-fx-background-color: derive(#2196f3, 90%);" +   
        "-fx-padding: 20;" +  
        "-fx-spacing: 10;";  

    private static final String BUTTON_STYLE =   
        "-fx-background-color:rgb(0, 58, 116);" +  
        "-fx-text-fill: white;" +  
        "-fx-font-size: 14px;" +  
        "-fx-font-family: 'Segoe UI';" +  
        "-fx-padding: 10 20;" +  
        "-fx-background-radius: 5;" +  
        "-fx-alignment: CENTER-LEFT;" +  
        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 3, 0.0, 0.0, 1);";  

    private static final String BUTTON_HOVER_STYLE =   
        "-fx-background-color:rgb(1, 105, 209);" +  
        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 5, 0.0, 0.0, 2);";  


    private static Button createButtonWithIcon(String text, String iconPath) {  
        Button button = new Button();  
        button.setStyle(BUTTON_STYLE);  
        button.setMaxWidth(Double.MAX_VALUE);  

        try {  
            ImageView icon = new ImageView(new Image(BudgetApp.class.getResourceAsStream(iconPath)));  
            icon.setFitHeight(40);  
            icon.setFitWidth(40);  
            
            Label textLabel = new Label(text);  
            textLabel.setStyle(  
                "-fx-text-fill: white;" +  
                "-fx-font-size: 14px;"  
            );  
            
            HBox content = new HBox(10);  
            content.setAlignment(Pos.CENTER_LEFT);  
            content.getChildren().addAll(icon, textLabel);  
            
            button.setGraphic(content);  
        } catch (Exception e) {  
            System.out.println("Icon yüklenirken hata: " + e.getMessage());  
            button.setText(text);  
            button.setStyle(  
                BUTTON_STYLE +   
                "-fx-text-fill: white;"   
            );  
        }  

        button.setOnMouseEntered(e -> button.setStyle(  
            BUTTON_STYLE +   
            BUTTON_HOVER_STYLE  
        ));  
        button.setOnMouseExited(e -> button.setStyle(BUTTON_STYLE));  

        return button;  
    }  

    public static Scene createHomePage(Stage stage, BudgetApp application) {  
        app = application;  

        
        VBox sideMenu = new VBox(20);  
        sideMenu.setStyle(SIDE_MENU_STYLE);  
        sideMenu.setPrefWidth(250);  

        ImageView logoView = new ImageView();  
        try {  
            Image logo = new Image(BudgetApp.class.getResourceAsStream("icons/logo.png"));  
            logoView.setImage(logo);  
            logoView.setFitWidth(200);  
            logoView.setPreserveRatio(true);   
        } catch (Exception e) {  
            System.out.println("Logo yüklenirken hata: " + e.getMessage());  
        }   

   
        Button addBudgetButton = createButtonWithIcon("Gelir/Gider Gir", "icons/add-money.png");  
        Button viewExpensesButton = createButtonWithIcon("Hesap Hareketleri", "icons/transaction.png");  
        Button regularPaymentsButton = createButtonWithIcon("Düzenli Ödemeler", "icons/calendar.png");  
        Button financialGoalsButton = createButtonWithIcon("Finansal Hedefler", "icons/target.png");  
        Button returnButton = createButtonWithIcon("Çıkış Yap", "icons/logout.png");  
        Button profileSettingsButton = createButtonWithIcon("Profilim", "icons/profile.png");  
        returnButton.setStyle(BUTTON_STYLE + "-fx-background-color: #f44336;");  

    
        addBudgetButton.setOnAction(e -> stage.setScene(AddBudgetPage.createAddBudgetPage(stage, app)));  
        viewExpensesButton.setOnAction(e -> stage.setScene(ExpensesPage.createExpensesPage(stage, app)));  
        regularPaymentsButton.setOnAction(e -> stage.setScene(RegularPayments.createRegularPaymentsPage(stage, app)));  
        financialGoalsButton.setOnAction(e -> stage.setScene(FinancialGoalsPage.createFinancialGoalsPage(stage, app)));  
        profileSettingsButton.setOnAction(e -> stage.setScene(ProfileSettingsPage.createProfileSettingsPage(stage, app)));  
        returnButton.setOnAction(e -> {  
            stopNotificationTimer();  
            stage.setScene(LoginPage.createLoginPage(stage, app));  
            app.showAlert("Çıkış Yapıldı", "Başarıyla Çıkış Yapıldı.");  
        });  

        sideMenu.getChildren().addAll(logoView, addBudgetButton, viewExpensesButton,  
                regularPaymentsButton, financialGoalsButton, profileSettingsButton, returnButton);  

      
        VBox notificationsBox = new VBox(15);  
        notificationsBox.setStyle(  
            "-fx-background-color: white;" +  
            "-fx-background-radius: 5;" +  
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0.0, 0.0, 2);" +  
            "-fx-padding: 20;"  
        );  

        app.calculateTotalBudget();  
        HBox budgetBox = new HBox(10);  
        budgetBox.setAlignment(Pos.CENTER_LEFT);  
        
        try {  
            ImageView moneyIcon = new ImageView(new Image(BudgetApp.class.getResourceAsStream("icons/money.png")));  
            moneyIcon.setFitHeight(80);  
            moneyIcon.setFitWidth(80);  
            
            Label budgetLabel = new Label(String.format("%.2f TL", app.getTotalBudget()));  
            budgetLabel.setStyle(  
                "-fx-font-size: 32px;" +  
                "-fx-font-weight: bold;" +  
                "-fx-text-fill: #34495E;"  
            );  
            
            budgetBox.getChildren().addAll(moneyIcon, budgetLabel);  
        } catch (Exception e) {  
            Label budgetLabel = new Label(String.format("Toplam Bütçe: %.2f TL", app.getTotalBudget()));  
            budgetLabel.setStyle(  
                "-fx-font-size: 32px;" +  
                "-fx-font-weight: bold;" +  
                "-fx-text-fill: #34495E"  
            );  
            budgetBox.getChildren().add(budgetLabel);  
        }  

    
        Label welcomeLabel = new Label("Merhaba " + app.getCurrentUser() + "!");  
        welcomeLabel.setStyle(  
            "-fx-font-size: 24px;" +  
            "-fx-font-weight: bold;" +  
            "-fx-text-fill: #34495E;"  
        );  

     
        HBox notificationTitleBox = new HBox(10);  
        notificationTitleBox.setAlignment(Pos.CENTER_LEFT);  

        try {  
            ImageView notificationIcon = new ImageView(new Image(BudgetApp.class.getResourceAsStream("icons/notification.png")));  
            notificationIcon.setFitHeight(24);  
            notificationIcon.setFitWidth(24);  

            Label notificationsTitle = new Label("Bildirimler");  
            notificationsTitle.setStyle(  
                "-fx-font-size: 18px;" +  
                "-fx-font-weight: bold;" +  
                "-fx-text-fill: #424242;"  
            );  

            notificationTitleBox.getChildren().addAll(notificationIcon, notificationsTitle);  
        } catch (Exception e) {  
            System.out.println("Bildirim ikonu yüklenirken hata: " + e.getMessage());  
            Label notificationsTitle = new Label("+ Bildirimler");  
            notificationsTitle.setStyle(  
                "-fx-font-size: 18px;" +  
                "-fx-font-weight: bold;" +  
                "-fx-text-fill: #424242;"  
            );  
            notificationTitleBox.getChildren().add(notificationsTitle);  
        }  

        notificationLabel = new Label();  
        notificationLabel.setStyle(  
            "-fx-font-size: 20px;" +  
            "-fx-text-fill: #757575;" +  
            "-fx-padding: 10 0 0 0;"  
        );  

        notificationsBox.getChildren().addAll(  
            welcomeLabel,  
            budgetBox,  
            notificationTitleBox,  
            notificationLabel  
        );  
  
        List<String> notifications = app.getUserNotifications(app.getCurrentUserId());  

        startNotificationAnimation(notifications);  

        ListView<String> expenseListView = new ListView<>();  
        expenseListView.setStyle(  
            "-fx-background-color: rgba(255,255,255,0.7);" +  
            "-fx-background-radius: 10;" +  
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0.0, 0.0, 2);"  
        );  

        refreshExpenses(expenseListView, app);  
        Label expensesTitle = new Label("Son Hesap Hareketleri");  
        expensesTitle.setStyle(  
            "-fx-font-size: 18px;" +  
            "-fx-font-weight: bold;" +  
            "-fx-text-fill: #424242;"  
        );  

    
        Button viewDetailsButton = new Button("Tüm Hareketleri Görüntüle");  
        viewDetailsButton.setStyle(  
            "-fx-background-color:rgb(0, 41, 82);" +  
            "-fx-text-fill: white;" +  
            "-fx-background-radius: 20;"  
        );  
        viewDetailsButton.setOnAction(e -> stage.setScene(ExpensesPage.createExpensesPage(stage, app)));  

      
        VBox expensesSection = new VBox(10);  
        expensesSection.setStyle(  
            "-fx-background-color: white;" +  
            "-fx-background-radius: 5;" +  
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0.0, 0.0, 2);" +  
            "-fx-padding: 20;"  
        );  
        expensesSection.getChildren().addAll(expensesTitle, expenseListView, viewDetailsButton);  

     
        VBox centerContent = new VBox(20);  
        centerContent.getChildren().addAll(notificationsBox, expensesSection);  

       
        BorderPane mainLayout = new BorderPane();  
        mainLayout.setStyle("-fx-background-color: #f5f5f5;");  
        mainLayout.setLeft(sideMenu);  

        StackPane centerPane = new StackPane(centerContent);  
        centerPane.setPadding(new Insets(20));  
        mainLayout.setCenter(centerPane);  

        Scene scene = new Scene(mainLayout, 1000, 700);  
        stage.setOnCloseRequest(e -> stopNotificationTimer());  
        
        return scene;  
    }  

    private static void refreshExpenses(ListView<String> listView, BudgetApp app) {  
        listView.getItems().clear();  
        List<String> expenses = app.getExpenses();  
        
    
        int startIndex = Math.max(0, expenses.size() - 5);  
        for (int i = startIndex; i < expenses.size(); i++) {  
            listView.getItems().add(expenses.get(i));  
        }  

        if (listView.getItems().isEmpty()) {  
            listView.getItems().add("Henüz bir hareket bulunmuyor.");  
        }  
    }  

    private static void startNotificationAnimation(List<String> notifications) {  
        stopNotificationTimer();  
        if (notifications.isEmpty()) {  
            notificationLabel.setText("Hiç bildirim yok.");  
            return;  
        }  
        notificationTimer = new Timeline(  
            new KeyFrame(Duration.seconds(3), event -> {  
                FadeTransition fadeOut = new FadeTransition(Duration.millis(500), notificationLabel);  
                fadeOut.setFromValue(1.0);  
                fadeOut.setToValue(0.0);  
                fadeOut.setOnFinished(e -> {  
                    app.markNotificationAsSeen(currentNotificationIndex); 
    
                    currentNotificationIndex = (currentNotificationIndex + 1) % notifications.size();  
                    notificationLabel.setText(notifications.get(currentNotificationIndex));  
                    
                    FadeTransition fadeIn = new FadeTransition(Duration.millis(500), notificationLabel);  
                    fadeIn.setFromValue(0.0);  
                    fadeIn.setToValue(1.0);  
                    fadeIn.play();  
                });  
                fadeOut.play();  
            })  
        );  
    
        notificationTimer.setCycleCount(Timeline.INDEFINITE);  
        notificationLabel.setText(notifications.get(0));  
        notificationTimer.play();  
    }  
    
    private static void stopNotificationTimer() {  
        if (notificationTimer != null) {  
            notificationTimer.stop();  
        }  
    }  
}