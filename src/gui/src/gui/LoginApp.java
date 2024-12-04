package gui;

import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class LoginApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login Form");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/images/stx.png")));
        Image backgroundImage = new Image(getClass().getResource("/resources/images/Cover_00000.png").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(1000);
        backgroundView.setFitHeight(550);
        primaryStage.setResizable(false);

        Label userLabel = createLabel("Username:");
        TextField userTextField = createTextField("Enter Username");
        Label passLabel = createLabel("Password:");
        PasswordField passField = createPasswordField("Enter Password");
        Button loginButton = new Button("Sign In");
        styleButton(loginButton);
        loginButton.setOnAction(e -> {
            String username = userTextField.getText();
            String password = passField.getText();
            if (username.equalsIgnoreCase("admin") && password.equalsIgnoreCase("password")) {
                SchedulerApp schedulerApp = new SchedulerApp();
                schedulerApp.start(new Stage());
                primaryStage.close();
            } else {
                showAlert("Error", "اعقل شويه !!! .");
            }
        });

        ImageView logoImage = new ImageView(new Image(getClass().getResource("/resources/images/SE logo png_00000.png").toExternalForm()));
        logoImage.setFitWidth(120);
        logoImage.setFitHeight(120);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(300), logoImage);
        scaleTransition.setFromX(1);
        scaleTransition.setFromY(1);
        scaleTransition.setToX(1.2);
        scaleTransition.setToY(1.2);
        logoImage.setOnMouseEntered(event -> scaleTransition.play());
        logoImage.setOnMouseExited(event -> {
            scaleTransition.setFromX(1.2);
            scaleTransition.setFromY(1.2);
            scaleTransition.setToX(1);
            scaleTransition.setToY(1);
            scaleTransition.play();
        });

        VBox loginForm = new VBox(20, userLabel, userTextField, passLabel, passField, loginButton);
        loginForm.setAlignment(javafx.geometry.Pos.CENTER);
        loginForm.setPadding(new Insets(50));
        loginForm.setStyle("-fx-background-radius: 25px; -fx-background-color: rgba(255, 255, 255, 0.8);");

        HBox root = new HBox(20, logoImage, loginForm);
        root.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        root.setPadding(new Insets(50));
        root.setStyle("-fx-background-color: rgba(255, 255, 255, 0.4); -fx-background-radius: 25px;");

        StackPane mainLayout = new StackPane();
        mainLayout.getChildren().addAll(backgroundView, root);

        Scene scene = new Scene(mainLayout, 1000, 550);
        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
        scene.getStylesheets().add(getClass().getResource("/resources/login.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

        animateLoginForm(root);
    }

    private void animateLoginForm(HBox root) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(2000), root);
        translateTransition.setFromX(-800);
        translateTransition.setToX(0);
        translateTransition.setCycleCount(1);
        translateTransition.setAutoReverse(false);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(root.translateXProperty(), 0)),
                new KeyFrame(Duration.seconds(2), new KeyValue(root.translateXProperty(), 50)),
                new KeyFrame(Duration.seconds(4), new KeyValue(root.translateXProperty(), -50)),
                new KeyFrame(Duration.seconds(6), new KeyValue(root.translateXProperty(), 0))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        translateTransition.play();
        timeline.play();
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("label");
        return label;
    }

    private TextField createTextField(String promptText) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.getStyleClass().add("text-field");
        return textField;
    }

    private PasswordField createPasswordField(String promptText) {
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText(promptText);
        passwordField.getStyleClass().add("password-field");
        return passwordField;
    }

    private void styleButton(Button button) {
        button.getStyleClass().add("login-button");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
