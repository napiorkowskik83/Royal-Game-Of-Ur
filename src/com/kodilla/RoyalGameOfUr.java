package com.kodilla;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.*;

public class RoyalGameOfUr extends Application {

    Random gen = new Random();
    private final Image imageBack = new Image("resources/leather.jpg");
    private final Image gameBoard = new Image("resources/the-royal-game-of-ur-board-3d-effect.png");
    Image greenX = new Image("resources/greenX.png");
    private List<ImageView> greenXViews = new ArrayList();

    private List<Button> greenXButtons = new ArrayList();
    private final Image dice00 = new Image("resources/dice00.png");
    private final Image dice01 = new Image("resources/dice01.png");
    private final Image dice02 = new Image("resources/dice02.png");
    private final Image dice10 = new Image("resources/dice10.png");
    private final Image dice11 = new Image("resources/dice11.png");
    private final Image dice12 = new Image("resources/dice12.png");
    private final Image[] dices = {dice00, dice01, dice02, dice10, dice11, dice12};
    private final Image blackChip = new Image("resources/bChipSmall.png");
    private final Image whiteChip = new Image("resources/wChipSmall.png");
    Location tempLocation;
    private final List<ImageView> diceViews = new ArrayList();
    private List<ImageView> whiteChipViews = new ArrayList();
    private List<ImageView> blackChipViews = new ArrayList();
    Button rollButton = new Button("Rzut kośćmi");
    private int rollResult;
    private Label instructionLabel = new Label("Witamy w królewskiej grze z Ur! Rzuć kośćmi aby rozpocząć rozgrywkę.");
    private List<Location> whiteStackLocations = new ArrayList();
    private List<Location> blackStackLocations = new ArrayList();
    private List<Location> whiteTrackLocations = new ArrayList();
    private List<Location> blackTrackLocations = new ArrayList();
    private List<Location> whiteOffLocations = new ArrayList();
    private List<Location> blackOffLocations = new ArrayList();
    private List<Chip> whiteChips = new ArrayList();
    private List<Chip> blackChips = new ArrayList();
    private List<Chip> chipsOnStack = new ArrayList();
    private List<Chip> chipsOnTrack = new ArrayList();
    private List<Chip> chipsOff = new ArrayList();
    private HashMap<Chip, Location> possibleMoves = new HashMap();
    boolean skipComputerMove;
    boolean skipPlayersMove;
    Location checkedLocation;

    private FlowPane dicesPane = new FlowPane(Orientation.HORIZONTAL, 10, 0);
    GridPane grid = new GridPane();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(imageBack, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);

        grid.setAlignment(Pos.TOP_LEFT);
        grid.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        grid.setHgap(10);
        grid.setVgap(10);
        //grid.setGridLinesVisible(true);
        grid.setBackground(background);
        ImageView boardView = new ImageView(gameBoard);

        createGreenXButtonList();
        rollTheDices();
        createChipsWithStartLocations();
        locateChips();

        rollButton.setOnAction((e) -> {
            rollResult = rollTheDices();
            System.out.println("Player roll: " + rollResult);
            nextTurn();
        });

        instructionLabel.setFont(new Font("Arial", 34));
        instructionLabel.setTextFill(Color.BEIGE);
        instructionLabel.setPrefWidth(600);
        instructionLabel.setWrapText(true);


        grid.add(boardView, 15, 15, 80, 31);
        grid.add(dicesPane, 60, 58, 40, 30);
        grid.add(rollButton, 68, 52, 10, 5);
        grid.add(instructionLabel, 28, 55, 60, 30);
        Scene scene = new Scene(grid, 1163, 774, Color.BLACK);

        primaryStage.setTitle("Royal Game Of Ur");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public void setUpLocationLists() {
        for (int i = 0; i < 7; i++) {
            whiteStackLocations.add(new Location(18 + i * 5, 5));
            blackStackLocations.add(new Location(18 + i * 5, 46));
            whiteOffLocations.add(new Location(68 - i, 15));
            blackOffLocations.add(new Location(68 - i, 37));
        }
        for (int i = 0; i < 4; i++) {
            whiteTrackLocations.add(new Location(47 - i * 10, 16));
            blackTrackLocations.add(new Location(47 - i * 10, 36));
        }
        for (int i = 0; i < 8; i++) {
            tempLocation = new Location(17 + i * 10, 26);
            whiteTrackLocations.add(tempLocation);
            blackTrackLocations.add(tempLocation);
        }
        for (int i = 0; i < 2; i++) {
            whiteTrackLocations.add(new Location(87 - i * 10, 16));
            blackTrackLocations.add(new Location(87 - i * 10, 36));
        }

    }

    public void createChipViewLists() {
        for (int i = 0; i < 7; i++) {
            whiteChipViews.add(new ImageView(whiteChip));
            blackChipViews.add(new ImageView(blackChip));
        }
    }

    public void createGreenXButtonList() {
        for (int i = 0; i < 7; i++) {
            greenXViews.add(new ImageView(greenX));
            greenXButtons.add(new Button("", greenXViews.get(i)));
            greenXButtons.get(i).setStyle("-fx-background-color: transparent;");
        }
    }


    public void createChipsWithStartLocations() {
        createChipViewLists();
        setUpLocationLists();
        for (int i = 0; i < 7; i++) {
            whiteChips.add(new Chip(whiteChipViews.get(i), whiteStackLocations.get(i)));
            blackChips.add(new Chip(blackChipViews.get(i), blackStackLocations.get(i)));
        }
    }

    public void locateChips() {
        for (int i = 0; i < 7; i++) {
            grid.add(whiteChips.get(i).getLook(), whiteChips.get(i).getLocation().getX(),
                    whiteChips.get(i).getLocation().getY(), 10, 10);
            grid.add(blackChips.get(i).getLook(), blackChips.get(i).getLocation().getX(),
                    blackChips.get(i).getLocation().getY(), 10, 10);
        }

    }

    public void resetChipsLocations(){
        for (int i = 0; i < whiteChips.size(); i++) {
            relocateChip(whiteChips.get(i), whiteStackLocations.get(i));
            relocateChip(blackChips.get(i), blackStackLocations.get(i));
        }
    }


    public int rollTheDices() {
        //+change dicesPane
        dicesPane.getChildren().clear();
        int result = 0;
        int[] results = {gen.nextInt(2), gen.nextInt(2), gen.nextInt(2), gen.nextInt(2)};
        for (int i = 0; i < results.length; i++) {
            if (results[i] == 0) {
                dicesPane.getChildren().add(new ImageView(dices[gen.nextInt(3)]));
            } else {
                dicesPane.getChildren().add(new ImageView(dices[3 + gen.nextInt(3)]));
            }
            result += results[i];

        }

        return result;
    }

    public void relocateChip(Chip chip, Location location) {
        grid.getChildren().remove(chip.getLook());
        chip.setLocation(location);
        grid.add(chip.getLook(), chip.getLocation().getX(), chip.getLocation().getY(), 10, 10);
    }

    public void locateButton(Button button, Location location) {
        grid.add(button, location.getX(), location.getY(), 10, 10);
    }

    public void nextTurn() {
        rollButton.setDisable(true);
        skipComputerMove=false;
        skipPlayersMove=false;
        if(rollResult!=0){
            startWithPlayerMove(rollResult);
        }else{
            computerMove();
        }
    }

    public void startWithPlayerMove(int rollResult) {
        determinePossibleMoves(rollResult);
        if(possibleMoves.size()>0){
            chooseMove();
        }else{
            computerMove();
        }
    }

    public void determinePossibleMoves(int rollResult) {
        possibleMoves.clear();
        groupUpBChips();
        for (Chip chip : chipsOnTrack) {
            if (blackTrackLocations.indexOf(chip.getLocation()) + rollResult < blackTrackLocations.size()) {
                checkedLocation = blackTrackLocations.get(blackTrackLocations.indexOf(chip.getLocation()) + rollResult);
                if (isBLocationAvailable(checkedLocation)) {
                    possibleMoves.put(chip, checkedLocation);
                }
            } else if (blackTrackLocations.indexOf(chip.getLocation()) + rollResult - blackTrackLocations.size() == 0) {
                possibleMoves.put(chip, blackOffLocations.get(chipsOff.size()));
            }
        }
        if (chipsOnStack.size() > 0) {
            checkedLocation = blackTrackLocations.get(rollResult - 1);
            if (isBLocationAvailable(checkedLocation)) {
                possibleMoves.put(chipsOnStack.get(chipsOnStack.size() - 1), checkedLocation);
            }
        }
        System.out.println(chipsOnStack.size() + "" + chipsOnTrack.size() + "" + chipsOff.size());
    }

    public void groupUpBChips() {
        chipsOnStack.clear();
        chipsOnTrack.clear();
        chipsOff.clear();
        for (Chip chip : blackChips) {
            if (blackStackLocations.contains(chip.getLocation())) {
                chipsOnStack.add(chip);
            } else if (blackTrackLocations.contains(chip.getLocation())) {
                chipsOnTrack.add(chip);
            } else {
                chipsOff.add(chip);
            }
        }
    }

    public void chooseMove() {
        instructionLabel.setText("Wybierz jeden z zaznaczonych pionków, którym chcesz wykonać ruch");
        int j = 0;
        for (Map.Entry<Chip, Location> possibleMove : possibleMoves.entrySet()) {
            locateButton(greenXButtons.get(j), possibleMove.getKey().getLocation());
            greenXButtons.get(j).setOnAction((e) -> {
                deleteAllGreenButtons(possibleMoves.size());
                relocateChip(possibleMove.getKey(), possibleMove.getValue());
                groupUpBChips();
                System.out.println(chipsOnStack.size() + "" + chipsOnTrack.size() + "" + chipsOff.size());
                skipComputerMove = blackTrackLocations.get(3).equals(possibleMove.getValue())
                        || blackTrackLocations.get(7).equals(possibleMove.getValue())
                        || blackTrackLocations.get(13).equals(possibleMove.getValue());
                if(chipsOff.size()<blackChips.size()){
                    instructionLabel.setText("Rzuć kośćmi");
                }else{
                    instructionLabel.setText("BRAWO! Wygrałeś! Rzuć kośćmi aby rozpocząć nową partię");
                    resetChipsLocations();
                    skipComputerMove =true;
                }
                for(Chip chip: whiteChips){
                    if(possibleMove.getValue().equals(chip.getLocation())){
                        removeWhiteChip(chip);
                    }
                }
                if (!skipComputerMove){
                    computerMove();
                }else{
                    rollButton.setDisable(false);
                }
            });
            j++;
        }
    }

    public void deleteAllGreenButtons(int n) {
        for (int i = 0; i < n; i++) {
            grid.getChildren().remove(greenXButtons.get(i));
        }
    }

    public boolean isBLocationAvailable(Location location) {
        for (Chip chip : blackChips) {
            if (chip.getLocation().equals(location)) {
                return false;
            }
        }
        if (blackTrackLocations.get(7).equals(location)) {
            for (Chip chip : whiteChips) {
                if (chip.getLocation().equals(location)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void removeWhiteChip(Chip chip){
        for(Location location: whiteStackLocations){
            if (isWLocationAvailable(location)){
                relocateChip(chip, location);
                break;
            }
        }

    }

    public void computerMove() {
        rollResult = rollTheDices();
        System.out.println("Computer roll: " + rollResult);
        if (rollResult != 0) {
            makeComputerMove(rollResult);
        }else{
            rollButton.setDisable(false);
        }
    }
    public void makeComputerMove(int rollResult) {
        determinePossibleComputerMoves(rollResult);
        if(!skipComputerMove){
            chooseComputerMove();
        }
    }

    public void determinePossibleComputerMoves(int rollResult) {
        possibleMoves.clear();
        groupUpWChips();
        for (Chip chip : chipsOnTrack) {
            if (whiteTrackLocations.indexOf(chip.getLocation()) + rollResult < whiteTrackLocations.size()) {
                checkedLocation = whiteTrackLocations.get(whiteTrackLocations.indexOf(chip.getLocation()) + rollResult);
                if (isWLocationAvailable(checkedLocation)) {
                    possibleMoves.put(chip, checkedLocation);
                }
            } else if (whiteTrackLocations.indexOf(chip.getLocation()) + rollResult - whiteTrackLocations.size() == 0) {
                possibleMoves.put(chip, whiteOffLocations.get(chipsOff.size()));
            }
        }
        if (chipsOnStack.size() > 0) {
            checkedLocation = whiteTrackLocations.get(rollResult - 1);
            if (isWLocationAvailable(checkedLocation)) {
                possibleMoves.put(chipsOnStack.get(chipsOnStack.size() - 1), checkedLocation);
            }
        }
        if(possibleMoves.size()==0){
            skipComputerMove=true;
            rollButton.setDisable(false);
        }
    }

    public void groupUpWChips() {
        chipsOnStack.clear();
        chipsOnTrack.clear();
        chipsOff.clear();
        for (Chip chip : whiteChips) {
            if (whiteStackLocations.contains(chip.getLocation())) {
                chipsOnStack.add(chip);
            } else if (whiteTrackLocations.contains(chip.getLocation())) {
                chipsOnTrack.add(chip);
            } else {
                chipsOff.add(chip);
            }
        }
    }

    public void chooseComputerMove() {
        int chosenMoveNo = gen.nextInt(possibleMoves.size());
        int c = 0;
        for (Map.Entry<Chip, Location> possibleMove : possibleMoves.entrySet()) {
            if (c == chosenMoveNo) {
                relocateChip(possibleMove.getKey(), possibleMove.getValue());
                groupUpWChips();
                skipPlayersMove = whiteTrackLocations.get(3).equals(possibleMove.getValue())
                        || whiteTrackLocations.get(7).equals(possibleMove.getValue())
                        || whiteTrackLocations.get(13).equals(possibleMove.getValue());
                for(Chip chip: blackChips){
                    if(possibleMove.getValue().equals(chip.getLocation())){
                        removeBlackChip(chip);
                    }
                }
                if(chipsOff.size()<whiteChips.size()){
                    instructionLabel.setText("Rzuć kośćmi");
                }else{
                    instructionLabel.setText("Niestety przegrałeś. Rzuć kośćmi aby rozpocząć nową partię");
                    resetChipsLocations();
                }
            }
            c++;
        }
        if(skipPlayersMove){
            computerMove();
        }
        rollButton.setDisable(false);
    }

    public boolean isWLocationAvailable(Location location) {
        for (Chip chip : whiteChips) {
            if (chip.getLocation().equals(location)) {
                return false;
            }
        }
        if (whiteTrackLocations.get(7).equals(location)) {
            for (Chip chip : blackChips) {
                if (chip.getLocation().equals(location)) {
                    return false;
                }
            }
        }
        return true;
    }
    public void removeBlackChip(Chip chip){
        for(Location location: blackStackLocations){
            if (isBLocationAvailable(location)){
                relocateChip(chip, location);
                break;
            }

        }

    }
}