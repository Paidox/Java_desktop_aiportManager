package main.io;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;

public class View 
{
  public static void showText(String title, String content)
  {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    DialogPane dialogPane = alert.getDialogPane();
    dialogPane.setExpandableContent(new Label(alert.getContentText()));
    dialogPane.setExpanded(true);
    alert.setContentText(null);
    alert.showAndWait();
  }

  public static void showError(String message)
  {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

}