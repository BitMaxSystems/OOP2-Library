package org.bitmaxsystems.oop2library.controllers;

import javafx.fxml.FXML;
import org.bitmaxsystems.oop2library.view.SceneManager;
import org.bitmaxsystems.oop2library.view.View;

public class AdministrativeInventoryController {
    @FXML
    public void onBackToMainDialog() {
        SceneManager.showView(View.ADMINISTRATIVE_HOME_VIEW);
    }

    @FXML
    public void onViewRegisteredBooks()
    {
        SceneManager.showView(View.ADMINISTRATIVE_BOOK_REGISTRY_VIEW);
    }
}
