package com.kodilla;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox {

    static boolean answer;

    public static boolean display(String title, String message){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        Label label = new Label(message);

        Button yesButton = new Button("Tak");
        Button noButton = new Button("Nie");

        yesButton.setOnAction(event -> {
            answer = true;
            window.close();
        });

        noButton.setOnAction(event -> {
            answer = false;
            window.close();
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10, 10 ,10, 10));
        HBox lowBox = new HBox(10);
        layout.setAlignment(Pos.CENTER);
        lowBox.setAlignment(Pos.CENTER);
        lowBox.getChildren().addAll(yesButton, noButton);
        layout.getChildren().addAll(label, lowBox);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return answer;
    }
}
