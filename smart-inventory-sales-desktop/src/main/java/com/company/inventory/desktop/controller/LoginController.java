package com.company.inventory.desktop.controller;

import com.company.inventory.desktop.client.ApiClient;
import com.company.inventory.desktop.model.AuthResponseDto;
import com.company.inventory.desktop.util.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final ApiClient apiClient = new ApiClient();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            errorLabel.setText("Kullanıcı adı veya şifre boş olamaz!");
            return;
        }

        try {
            Map<String, String> request = new HashMap<>();
            request.put("username", username);
            request.put("password", password);

            ResponseEntity<AuthResponseDto> response =
                    apiClient.post("/auth/login", request, AuthResponseDto.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                AuthResponseDto authData = response.getBody();

                UserSession.setToken(authData.getAccessToken());
                UserSession.setUsername(authData.getUsername());
                UserSession.setRoles(authData.getRoles());

                switchToDashboard();
            } else {
                errorLabel.setText("Giriş başarısız!");
            }

        } catch (Exception e) {
            errorLabel.setText("Giriş başarısız! Bilgileri kontrol edin.");
        }
    }

    private void switchToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashboardView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Smart Inventory - Dashboard");
            stage.centerOnScreen();
        } catch (IOException e) {
            errorLabel.setText("Dashboard yüklenirken hata oluştu!");
            e.printStackTrace();
        }
    }
}
