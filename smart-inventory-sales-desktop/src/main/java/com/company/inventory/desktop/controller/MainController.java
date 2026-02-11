package com.company.inventory.desktop.controller;

import com.company.inventory.desktop.util.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainController {
    @FXML private Button productManageBtn;
    @FXML private Button orderBtn;

    public void initialize() {
        if (UserSession.getRoles().contains("ROLE_SALES") && !UserSession.getRoles().contains("ROLE_ADMIN")) {
            productManageBtn.setVisible(false);
        }
    }
}