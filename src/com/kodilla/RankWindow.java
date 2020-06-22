package com.kodilla;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

public class RankWindow {

    private static File savedPlayerRanks = new File("rank.list");
    private static HashMap<String, int[]> playersRanks = new HashMap<>();

    public static void display(){

        loadPlayersRanks();

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);

        window.setTitle("Ranking");
        window.setMinWidth(300);


        HBox layout = new HBox(10);
        layout.setPadding(new Insets(10, 10 ,10, 10));
        VBox nameColumn = new VBox(10);
        VBox winsColumn = new VBox(10);
        VBox lossColumn = new VBox(10);

        Text nameHeader = new Text("Gracz");
        nameHeader.setFont(new Font(18));
        nameHeader.setFill(Color.DARKGREEN);
        Text winsHeader = new Text("Zwycięstwa");
        winsHeader.setFont(new Font(18));
        winsHeader.setFill(Color.DARKBLUE);
        Text lossHeader = new Text("Przegrane");
        lossHeader.setFont(new Font(18));
        lossHeader.setFill(Color.DARKRED);

        nameColumn.getChildren().add(nameHeader);
        winsColumn.getChildren().add(winsHeader);
        lossColumn.getChildren().add(lossHeader);

        nameColumn.setAlignment(Pos.CENTER);
        winsColumn.setAlignment(Pos.CENTER);
        lossColumn.setAlignment(Pos.CENTER);

        int highestRank = 0;
        for(Map.Entry<String, int[]> entry: playersRanks.entrySet()){
            if ( highestRank < entry.getValue()[0] - entry.getValue()[1]){
                highestRank = entry.getValue()[0] - entry.getValue()[1];
            }
        }

        for (int i = highestRank; i >= 0  ; i--){
            for(Map.Entry<String, int[]> entry: playersRanks.entrySet()){
                if ( i == entry.getValue()[0] - entry.getValue()[1]){
                    nameColumn.getChildren().add(new Text(entry.getKey()));
                    winsColumn.getChildren().add(new Text("" + entry.getValue()[0]));
                    lossColumn.getChildren().add(new Text(""+entry.getValue()[1]));
                }
            }
        }

        layout.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(nameColumn, winsColumn, lossColumn);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.show();
    }

    public static void loadPlayersRanks() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(savedPlayerRanks));
            Object readObject = ois.readObject();
            if (readObject instanceof HashMap) {
                playersRanks = (HashMap) readObject;
            }
            ois.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
