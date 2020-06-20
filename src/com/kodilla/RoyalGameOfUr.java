package com.kodilla;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class RoyalGameOfUr extends Application {

    Random gen = new Random();
    private final Image imageBack = new Image("resources/leather.jpg");
    private final Image gameBoard = new Image("resources/the-royal-game-of-ur-board-3d-effect.png");
    private final Image greenX = new Image("resources/greenX.png");
    private final List<ImageView> greenXViews = new ArrayList();
    private final ArrayList<Button> greenXButtons = new ArrayList();
    private final ArrayList<String> logs = new ArrayList();
    private final Image dice00 = new Image("resources/dice00.png");
    private final Image dice01 = new Image("resources/dice01.png");
    private final Image dice02 = new Image("resources/dice02.png");
    private final Image dice10 = new Image("resources/dice10.png");
    private final Image dice11 = new Image("resources/dice11.png");
    private final Image dice12 = new Image("resources/dice12.png");
    private final Image[] dices = {dice00, dice01, dice02, dice10, dice11, dice12};
    private final Image blackChip = new Image("resources/bChipSmall.png");
    private final Image whiteChip = new Image("resources/wChipSmall.png");
    private final Image directions = new Image("resources/directions.png");
    private final BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
    private final BackgroundImage backgroundImage = new BackgroundImage(imageBack, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
    private final Background gameBackground = new Background(backgroundImage);
    private final VBox startBox = new VBox();
    private Scene startScene;
    private Scene gameScene;
    private Location tempLocation;
    private final ArrayList<ImageView> whiteChipViews = new ArrayList();
    private final ArrayList<ImageView> blackChipViews = new ArrayList();
    Button rollButton = new Button("Rzut kośćmi");
    private int rollResult;
    private final Label instructionLabel = new Label("Rzuć kośćmi aby rozpocząć swoją turę.");
    private final Label logHeader = new Label();
    private final Label log = new Label();
    private final VBox logBox = new VBox();
    private final Label resultsHeader = new Label("Dotychczasowe zwycięstwa");
    private final Label resultsLabel = new Label();
    private final VBox resultsBox = new VBox();
    String logText;
    private final ArrayList<Location> whiteStackLocations = new ArrayList();
    private final ArrayList<Location> blackStackLocations = new ArrayList();
    private final ArrayList<Location> whiteTrackLocations = new ArrayList();
    private final ArrayList<Location> blackTrackLocations = new ArrayList();
    private final ArrayList<Location> whiteOffLocations = new ArrayList();
    private final ArrayList<Location> blackOffLocations = new ArrayList();
    private final ArrayList<Chip> whiteChips = new ArrayList();
    private final ArrayList<Chip> blackChips = new ArrayList();
    private final ArrayList<Chip> chipsOnStack = new ArrayList();
    private final ArrayList<Chip> chipsOnTrack = new ArrayList();
    private final ArrayList<Chip> chipsOff = new ArrayList();
    private HashMap<Chip, Location> possibleMoves = new HashMap();
    private HashMap<Chip, Location> selectedPossibleMoves = new HashMap();
    private boolean skipComputerMove;
    private boolean skipPlayersMove;
    private boolean computerFirst;
    private Location checkedLocation;
    private int computerAILevel = 1;
    private ComboBox<String> aILevels = new ComboBox<>();
    private ComboBox<String> playerNameBox = new ComboBox<>();
    private String playerName = "nazwa gracza";
    private Integer playerWins = 0;
    private Integer computerWins = 0;
    private final FlowPane dicesPane = new FlowPane(Orientation.HORIZONTAL, 10, 0);
    private final GridPane gameGrid = new GridPane();
    private final MenuBar menuBar = new MenuBar();
    private final Menu menu = new Menu("Menu");
    private final MenuItem loadGame = new MenuItem("Wczytaj ostatnią grę");
    private final MenuItem saveGame = new MenuItem("Zapisz grę");
    private final MenuItem restartGame = new MenuItem("Zrestartuj grę");
    private final MenuItem clearPlayerResults = new MenuItem("Wyczyść swoje wyniki");
    private final MenuItem exitGame = new MenuItem("Zakończ grę");
    private HashMap<String, int[]> playersRanks = new HashMap<>();
    private HashMap<String, ArrayList<Location>> savedGamesMap = new HashMap<String, ArrayList<Location>>();
    private ArrayList<Location> savedChipsLocations = new ArrayList<Location>();
    private File savedPlayerRanks = new File("rank.list");
    private File savedGames = new File("saved.games");


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        loadPlayersRanks();
        setUpGameScene(primaryStage, gameBackground);
        setUpStartScene(primaryStage, gameBackground);

        primaryStage.setTitle("Royal Game Of Ur");
        primaryStage.setScene(startScene);
        primaryStage.show();
    }


    public void loadPlayersRanks() {
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

    public void setUpStartScene(Stage primaryStage, Background gameBackground) {
        startBox.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        startBox.setBackground(Background.EMPTY);
        startBox.setSpacing(18);

        Label text1 = new Label("Witamy w Królewskiej Grze z Ur!");
        //text1.setWrapText(true);
        text1.setFont(Font.font("Cambria", FontWeight.NORMAL, 22));
        text1.setTextFill(Color.BLACK);

        Label text2 = new Label("Królewska gra z Ur jest rozgrywana przy użyciu dwóch zestawów po siedem pionków każdy. Pionki w jednym z zestawów są białe z ciemnymi kropkami, w drugim zestawie znajdują się natomiast czarne pionki z jasnymi kropkami. \n" +
                "Plansza składa się z trzech rzędów kwadratowych pól. Dwa zewnętrzne, niepełne rzędy (dolny i górny) są identyczne i składają się z sześciu pól. Pola te należą do pól własnych poszczególnych graczy. Rząd środkowy składa się z ośmiu pól, na których mogą lądować pionki obydwu z graczy. \n" +
                "Celem gry jest przejście przez planszę (i zejście z niej) wszystkimi siedmioma pionkami przed przeciwnikiem. Kierunki poruszania pionków z zestawu czarnego oraz białego zaznaczone są odpowiadającym im kolorem na poniższej grafice.");
        text2.setWrapText(true);
        text2.setFont(Font.font("Cambria", FontWeight.NORMAL, 18));
        text2.setTextFill(Color.BLACK);

        Label text3 = new Label("Gracz/komputer zaczyna turę od rzutu kośćmi i o wylosowaną liczbę pól może przesunąć dowolny ze swoich pionków na planszy lub spoza planszy, jeśli nadal ma pionki, które nie weszły do gry.\n" +
                "Kiedy pionek ląduje na którejkolwiek ze znajdujących się na jego torze trzech rozet, gracz otrzymuje dodatkowy ruch. \n" +
                "Kiedy pionek znajduje się na jednym z własnych pól gracza jest bezpieczny (nie ma możliwości jego strącenia). Natomiast kiedy znajduje się na jednym z pól środkowego rzędu planszy, pionki przeciwnika mogą go strącić, lądując na tym samym polu. Strącony pionek zostaje zdjęty z planszy i musi ponownie rozpocząć „wyścig” od początku. \n" +
                "Gracz nie musi strącić pionka przeciwnika za każdym razem, kiedy ma taką możliwość. \n" +
                "Gracze są zobowiązani do przesunięcia któregoś z pionków, kiedy tylko jest to możliwe (nawet jeśli jest to dla nich niekorzystne)\n" +
                "Jeśli pionek znajduje się w środkowym rzędzie na polu z rozetą, nie można go strącić. \n" +
                "Na jednym polu nigdy nie może znajdować się więcej niż jeden pionek. Gracz nie może zatem przesunąć pionka na pole które jest zajmowane przez jego inny pionek lub pionek przeciwnika jeśli jest to pole z rozetą.\n" +
                "Aby zejść pionkiem z planszy, gracz musi rzucić dokładnie tyle pól, ile pozostało do końca pola plus jeden. Jeśli gracz rzuci liczbę wyższą lub niższą od tej liczby, nie może usunąć pionka z planszy.");
        text3.setWrapText(true);
        text3.setFont(Font.font("Cambria", FontWeight.NORMAL, 18));
        text3.setTextFill(Color.BLACK);

        Label text4 = new Label("Jeżeli jesteś gotowy wprowadź nazwę gracza (lub wybierz jedną z wcześniej używnych)," +
                " wybierz poziom chytrości komputera oraz naciśnij któryś z przycisków 'Komputer' / 'Gracz' określający kto ma rozpocząć pierwszą rozgrywkę:");
        text4.setWrapText(true);
        text4.setFont(Font.font("Cambria", FontWeight.BOLD, 18));
        text4.setTextFill(Color.BLACK);

        aILevels.getItems().addAll("nierozgarnięty", "rozsądny", "całkiem bystry");
        aILevels.setPromptText("poziom komputera");
        aILevels.setPrefSize(180, 30);

        playerNameBox.setValue("nazwa gracza");
        for (Map.Entry<String, int[]> rank : playersRanks.entrySet()) {
            playerNameBox.getItems().add(rank.getKey());
        }

        playerNameBox.setEditable(true);
        aILevels.setPrefSize(180, 30);

        Button playerButton = new Button("Gracz");
        playerButton.setOnAction((e) -> {
            updateSettingsAccordingToPlayerInput();
            primaryStage.setScene(gameScene);
        });


        Button computerButton = new Button("Komputer");
        computerButton.setOnAction((e) -> {
            computerFirst = true;
            updateSettingsAccordingToPlayerInput();
            primaryStage.setScene(gameScene);
            computerMove();
        });

        HBox lowBox = new HBox();
        lowBox.setSpacing(20);
        lowBox.setAlignment(Pos.CENTER);
        lowBox.getChildren().addAll(playerNameBox, aILevels, computerButton, playerButton);


        startBox.getChildren().addAll(text1, text2, new ImageView(directions), text3, text4, lowBox);
        startBox.setAlignment(Pos.TOP_CENTER);
        startScene = new Scene(startBox, 1163, 774, Color.BEIGE);
    }

    public void updateSettingsAccordingToPlayerInput() {
        playerName = playerNameBox.getValue();
        updatePlayerAndComputerWins();
        setUpComputerAILevel();
        resultsLabel.setText(playerName + " " + playerWins + " : " + computerWins + " " + "komputer");
        log.setText("");
        if (!savedGamesMap.containsKey(playerName)) {
            loadGame.setDisable(true);
        }
    }

    public void updatePlayerAndComputerWins() {
        if (playersRanks.containsKey(playerName)) {
            playerWins = playersRanks.get(playerName)[0];
            computerWins = playersRanks.get(playerName)[1];
        }
    }

    public void setUpComputerAILevel() {
        if ("nierozgarnięty".equals(aILevels.getValue())) {
            computerAILevel = 1;
        } else if ("rozsądny".equals(aILevels.getValue())) {
            computerAILevel = 2;
        } else if ("całkiem bystry".equals(aILevels.getValue())) {
            computerAILevel = 3;
        } else {
            computerAILevel = 2;
        }
    }

    public void setUpGameScene(Stage primaryStage, Background background) {
        gameGrid.setAlignment(Pos.TOP_LEFT);
        gameGrid.setHgap(10);
        gameGrid.setVgap(10);
        //gameGrid.setGridLinesVisible(true);
        gameGrid.setBackground(background);
        ImageView boardView = new ImageView(gameBoard);

        createGreenXButtonList();
        rollTheDices();
        createChipsWithStartLocations();
        locateChips();

        rollButton.setOnAction((e) -> {
            rollResult = rollTheDices();
            updateLogByPlayerMove();
            System.out.println("Player roll: " + rollResult);
            nextTurn();
        });

        logHeader.setFont(new Font("Segoe UI Black", 18));
        logHeader.setTextFill(Color.BEIGE);
        logHeader.setText("Ostatnie 5 rzutów:");

        log.setFont(new Font("Segoe UI Black", 16));
        log.setTextFill(Color.BEIGE);

        logBox.getChildren().addAll(logHeader, log);
        logBox.setSpacing(5);

        instructionLabel.setFont(new Font("Cambria", 36));
        instructionLabel.setTextFill(Color.BEIGE);
        instructionLabel.setPrefWidth(600);
        instructionLabel.setWrapText(true);
        instructionLabel.setAlignment(Pos.BOTTOM_CENTER);

        resultsHeader.setFont(new Font("Segoe UI Black", 24));
        resultsHeader.setTextFill(Color.BLACK);

        resultsLabel.setText(playerName + " " + playerWins + " : " + computerWins + " " + "komputer");
        resultsLabel.setFont(new Font("Segoe UI Black", 28));
        resultsLabel.setTextFill(Color.BLACK);

        resultsBox.getChildren().addAll(resultsHeader, resultsLabel);
        resultsBox.setAlignment(Pos.TOP_CENTER);

        saveGame.setOnAction((e) -> {
            savedChipsLocations.clear();
            for (int i = 0; i < whiteChips.size(); i++) {
                savedChipsLocations.add(whiteChips.get(i).getLocation());
                savedChipsLocations.add(blackChips.get(i).getLocation());
            }
            savedGamesMap.put(playerName, savedChipsLocations);
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(savedGames));
                oos.writeObject(savedGamesMap);
                oos.close();
            } catch (Exception exc) {
                System.out.println(exc);
            }
            loadGame.setDisable(false);

        });

        loadGame.setOnAction((e) -> {
            loadSavedGamesMap();
            savedChipsLocations = savedGamesMap.get(playerName);
            int j = 0;
            for (int i = 0; i < whiteChips.size(); i++) {
                relocateChip(whiteChips.get(i), savedChipsLocations.get(j));
                relocateChip(blackChips.get(i), savedChipsLocations.get(j + 1));
                j = j + 2;
            }
        });

        restartGame.setOnAction((e) -> {
            logs.clear();
            resetChipsLocations();
            playerNameBox.setValue(playerName);
            if(computerAILevel == 1){
                aILevels.setValue("nierozgarnięty");
            }else if(computerAILevel == 3){
                aILevels.setValue("całkiem bystry");
            }else{
                aILevels.setValue("rozsądny");
            }
            primaryStage.setScene(startScene);
        });

        clearPlayerResults.setOnAction(event ->{
            boolean answer = ConfirmBox.display("Potwierdź",
                    "Czy jesteś pewien, że chcesz wyzerować " +
                            "swoje dotychczasowe wyniki?");
            if(answer){
                playerWins = 0;
                computerWins = 0;
                resultsLabel.setText(playerName + " " + playerWins + " : " + computerWins + " " + "komputer");
                savePlayersRanks();
            }
        });

        exitGame.setOnAction(event ->{
            boolean answer = ConfirmBox.display("Potwierdź",
                    "Czy jesteś pewien, że chcesz zakończyć" +
                            " grę bez jej zapisania?");
            if(answer){
                primaryStage.close();
            }
        });

        loadSavedGamesMap();



        menu.getItems().addAll(saveGame, loadGame, clearPlayerResults, restartGame, exitGame);
        menuBar.getMenus().addAll(menu);


        gameGrid.add(menuBar, 0, 0, 15, 3);
        gameGrid.add(boardView, 19, 17, 80, 31);
        gameGrid.add(dicesPane, 75, 57, 40, 30);
        gameGrid.add(rollButton, 83, 51, 20, 5);
        gameGrid.add(instructionLabel, 32, 55, 80, 30);
        gameGrid.add(logBox, 1, 60, 30, 30);
        gameGrid.add(resultsBox, 64, 2, 60, 50);
        gameScene = new Scene(gameGrid, 1163, 774, Color.BLACK);
    }

    public void loadSavedGamesMap() {
        savedChipsLocations.clear();
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(savedGames));
            Object readObject = ois.readObject();
            if (readObject instanceof HashMap) {
                savedGamesMap = (HashMap) readObject;
            }
            ois.close();
        } catch (Exception exce) {
            System.out.println(exce);
        }
    }


    public void setUpLocationLists() {
        for (int i = 0; i < 7; i++) {
            whiteStackLocations.add(new Location(22 + i * 5, 7));
            blackStackLocations.add(new Location(22 + i * 5, 48));
            whiteOffLocations.add(new Location(72 - i, 17));
            blackOffLocations.add(new Location(72 - i, 39));
        }
        for (int i = 0; i < 4; i++) {
            whiteTrackLocations.add(new Location(51 - i * 10, 18));
            blackTrackLocations.add(new Location(51 - i * 10, 38));
        }
        for (int i = 0; i < 8; i++) {
            tempLocation = new Location(21 + i * 10, 28);
            whiteTrackLocations.add(tempLocation);
            blackTrackLocations.add(tempLocation);
        }
        for (int i = 0; i < 2; i++) {
            whiteTrackLocations.add(new Location(91 - i * 10, 18));
            blackTrackLocations.add(new Location(91 - i * 10, 38));
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
            gameGrid.add(whiteChips.get(i).getLook(), whiteChips.get(i).getLocation().getX(),
                    whiteChips.get(i).getLocation().getY(), 10, 10);
            gameGrid.add(blackChips.get(i).getLook(), blackChips.get(i).getLocation().getX(),
                    blackChips.get(i).getLocation().getY(), 10, 10);
        }

    }

    public void resetChipsLocations() {
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
        gameGrid.getChildren().remove(chip.getLook());
        chip.setLocation(location);
        gameGrid.add(chip.getLook(), chip.getLocation().getX(), chip.getLocation().getY(), 10, 10);
    }

    public void locateButton(Button button, Location location) {
        gameGrid.add(button, location.getX(), location.getY(), 10, 10);
    }

    public void nextTurn() {
        rollButton.setDisable(true);
        saveGame.setDisable(true);
        skipComputerMove = false;
        skipPlayersMove = false;
        if (rollResult != 0) {
            startWithPlayerMove(rollResult);
        } else {
            computerMove();
        }
    }

    public void startWithPlayerMove(int rollResult) {
        determinePossibleMoves(rollResult);
        if (possibleMoves.size() > 0) {
            chooseMove();
        } else {
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
                skipComputerMove = blackTrackLocations.get(3).equals(possibleMove.getValue())
                        || blackTrackLocations.get(7).equals(possibleMove.getValue())
                        || blackTrackLocations.get(13).equals(possibleMove.getValue());

                for (Chip chip : whiteChips) {
                    if (possibleMove.getValue().equals(chip.getLocation())) {
                        removeWhiteChip(chip);
                    }
                }

                if (chipsOff.size() < blackChips.size()) {
                    instructionLabel.setText("Rzuć kośćmi");
                } else {
                    playerWins++;
                    savePlayersRanks();
                    resultsLabel.setText(playerName + " " + playerWins + " : " + computerWins + " " + "komputer");
                    instructionLabel.setText("BRAWO! Wygrałeś! Rzuć kośćmi aby rozpocząć nową partię");
                    logs.clear();
                    resetChipsLocations();
                    computerFirst = !computerFirst;
                    if (computerFirst) {
                        computerMove();
                        instructionLabel.setText("BRAWO! Wygrałeś! Komputer wykonał pierwszy ruch w nowej partii" +
                                " Rzuć kośćmi aby wykonać swój ruch");
                    }
                    skipComputerMove = true;
                }

                if (!skipComputerMove) {
                    computerMove();
                } else {
                    saveGame.setDisable(false);
                    rollButton.setDisable(false);
                }
            });
            j++;
        }
    }

    public void deleteAllGreenButtons(int n) {
        for (int i = 0; i < n; i++) {
            gameGrid.getChildren().remove(greenXButtons.get(i));
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

    public void removeWhiteChip(Chip chip) {
        for (Location location : whiteStackLocations) {
            if (isWLocationAvailable(location)) {
                relocateChip(chip, location);
                break;
            }
        }
    }

    public void savePlayersRanks() {
        int[] playerAndComputerWins = {playerWins, computerWins};
        playersRanks.put(playerName, playerAndComputerWins);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(savedPlayerRanks));
            oos.writeObject(playersRanks);
            oos.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void computerMove() {
        rollResult = rollTheDices();
        updateLogByComputerMove();
        if (rollResult != 0) {
            instructionLabel.setText("Komputer wykonuje ruch");
//            try{
//                Thread.sleep(3000);
//            }catch(Exception ex){
//            }
            makeComputerMove(rollResult);
        } else {
            rollButton.setDisable(false);
            saveGame.setDisable(false);
        }
    }

    public void makeComputerMove(int rollResult) {
        determinePossibleComputerMoves(rollResult);
        if (!skipComputerMove) {
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
        if (possibleMoves.size() == 0) {
            skipComputerMove = true;
            saveGame.setDisable(false);
            instructionLabel.setText("Komputer nie był w stanie wykonać ruchu. Rzuć kośćmi");
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
        if (computerAILevel == 1) {
            randomChoice();
        } else if (computerAILevel == 2) {
            mediumSmartChoice();
        } else {
            smartChoice();
        }

        if (skipPlayersMove) {
            computerMove();
        }
        rollButton.setDisable(false);
        saveGame.setDisable(false);
    }

    public void smartChoice() {
        for (Map.Entry<Chip, Location> possibleMove : possibleMoves.entrySet()) {
            for (Chip chip : blackChips) {
                if (possibleMove.getValue().equals(chip.getLocation())) {
                    executeChosenComputerMove(possibleMove);
                    return;
                }
            }
        }
        for (Map.Entry<Chip, Location> possibleMove : possibleMoves.entrySet()) {
            if (whiteTrackLocations.get(3).equals(possibleMove.getValue())
                    || whiteTrackLocations.get(7).equals(possibleMove.getValue())
                    || whiteTrackLocations.get(13).equals(possibleMove.getValue())) {
                executeChosenComputerMove(possibleMove);
                return;

            }
        }
        selectedPossibleMoves = (HashMap<Chip, Location>) possibleMoves.clone();
        if (possibleMoves.size() > 1) {
            for (Map.Entry<Chip, Location> possibleMove : possibleMoves.entrySet()) {
                if (whiteTrackLocations.get(7).equals(possibleMove.getKey().getLocation())) {
                    selectedPossibleMoves.remove(possibleMove.getKey());
                }
            }
        }
        possibleMoves = selectedPossibleMoves;

        for (Map.Entry<Chip, Location> possibleMove : possibleMoves.entrySet()) {
            if (whiteTrackLocations.subList(4, 12).contains(possibleMove.getKey().getLocation())) {
                for(Chip blackChip: blackChips){
                    if(whiteTrackLocations.indexOf(possibleMove.getKey().getLocation()) ==
                            blackTrackLocations.indexOf(blackChip.getLocation()) + 2){
                        executeChosenComputerMove(possibleMove);
                        return;
                    }
                }
            }
        }
        selectedPossibleMoves = (HashMap<Chip, Location>) possibleMoves.clone();
        for (Map.Entry<Chip, Location> possibleMove : possibleMoves.entrySet()) {
            if (whiteTrackLocations.subList(4, 12).contains(possibleMove.getValue())) {
                for(Chip blackChip: blackChips){
                    if(whiteTrackLocations.indexOf(possibleMove.getValue()) ==
                            blackTrackLocations.indexOf(blackChip.getLocation()) + 2 &&
                            selectedPossibleMoves.size() > 1){
                        selectedPossibleMoves.remove(possibleMove.getKey());
                    }
                }
            }
        }
        possibleMoves = selectedPossibleMoves;
        randomChoice();
    }

    public void mediumSmartChoice() {
        for (Map.Entry<Chip, Location> possibleMove : possibleMoves.entrySet()) {
            for (Chip chip : blackChips) {
                if (possibleMove.getValue().equals(chip.getLocation())) {
                    executeChosenComputerMove(possibleMove);
                    return;
                }
            }
        }
        for (Map.Entry<Chip, Location> possibleMove : possibleMoves.entrySet()) {
            if (whiteTrackLocations.get(3).equals(possibleMove.getValue())
                    || whiteTrackLocations.get(7).equals(possibleMove.getValue())
                    || whiteTrackLocations.get(13).equals(possibleMove.getValue())) {
                executeChosenComputerMove(possibleMove);
                return;

            }
        }
        randomChoice();
    }

    public void randomChoice() {
        int chosenMoveNo = gen.nextInt(possibleMoves.size());
        int c = 0;
        for (Map.Entry<Chip, Location> possibleMove : possibleMoves.entrySet()) {
            if (c == chosenMoveNo) {
                executeChosenComputerMove(possibleMove);
            }
            c++;
        }
    }

    public void executeChosenComputerMove(Map.Entry<Chip, Location> possibleMove) {
        relocateChip(possibleMove.getKey(), possibleMove.getValue());
        groupUpWChips();
        skipPlayersMove = whiteTrackLocations.get(3).equals(possibleMove.getValue())
                || whiteTrackLocations.get(7).equals(possibleMove.getValue())
                || whiteTrackLocations.get(13).equals(possibleMove.getValue());
        for (Chip chip : blackChips) {
            if (possibleMove.getValue().equals(chip.getLocation())) {
                removeBlackChip(chip);
            }
        }
        if (chipsOff.size() < whiteChips.size()) {
            instructionLabel.setText("Rzuć kośćmi");
        } else {
            computerWins++;
            savePlayersRanks();
            resultsLabel.setText(playerName + " " + playerWins + " : " + computerWins + " " + "komputer");
            instructionLabel.setText("Niestety przegrałeś. Rzuć kośćmi aby rozpocząć nową partię");
            logs.clear();
            resetChipsLocations();
            computerFirst = !computerFirst;
            if (computerFirst) {
                computerMove();
                instructionLabel.setText("Niestety przegrałeś. Komputer wykonał pierwszy ruch w nowej partii" +
                        " Rzuć kośćmi aby wykonać swój ruch");
            }
        }
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

    public void removeBlackChip(Chip chip) {
        for (Location location : blackStackLocations) {
            if (isBLocationAvailable(location)) {
                relocateChip(chip, location);
                break;
            }

        }

    }

    public void updateLogByPlayerMove() {
        if (logs.size() > 4) {
            logs.remove(0);
            logs.add("Gracz wyrzucił " + rollResult + "\n");
        } else {
            logs.add("Gracz wyrzucił " + rollResult + "\n");
        }
        logText = "";
        for (String text : logs) {
            logText += text;
        }
        log.setText(logText);
    }

    public void updateLogByComputerMove() {
        if (logs.size() > 4) {
            logs.remove(0);
            logs.add("Komputer wyrzucił " + rollResult + "\n");
        } else {
            logs.add("Komputer wyrzucił " + rollResult + "\n");
        }
        logText = "";
        for (String text : logs) {
            logText += text;
        }
        log.setText(logText);
    }

}