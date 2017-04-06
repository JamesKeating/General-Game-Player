package GUI;

import DescriptionProcessing.Player;
import GDLParser.LexicalAnalyser;
import Gameplay.*;
import DeductiveDatabase.DescriptionTable;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import Gameplay.GameManager;

import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by siavj on 31/01/2017.
 */

//The user interface for the system
public class Grahpics extends Application {

    private int count = 0;
    private ArrayList<ArrayList<String>> p_moves = new ArrayList<>();
    private ArrayList<Player> p_names = new ArrayList<>();
    private DescriptionTable dt = null;
    private boolean first =true;
    private boolean playing = false;
    private boolean piVal = false;


    public static void main(String[] args) {
        launch(args);
    }
    @Override

    //gets the initial scene
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("GGP Console");
        primaryStage.setScene(getSelectionScene(primaryStage));
        primaryStage.show();

    }

    //draws the actual game
    public GridPane drawGrid(GridPane gridPane, PropnetPlayer gm){
        gridPane.getChildren().clear();
        for (Drawable drawable : gm.getDrawable(gm.getContents())){
            gridPane.add(drawable.getImage(), drawable.gety(), drawable.getx());
        }
        return gridPane;
    }

    //checks if a player has no move
    public boolean noopCheck(int c){
        boolean op_has_only_noop = true;
        boolean i_have_only_noop = false;
        for (int i = 0; i < p_names.size(); i++) {

            if (p_moves.get(i).size() == 1){

                if (p_moves.get(i).get(0).contains("noop")){
                    if (c == i)
                        i_have_only_noop = true;
                }
                else if (c != i)
                    op_has_only_noop = false;
            }
            else if (c != i)
                op_has_only_noop = false;
        }
        return !op_has_only_noop && i_have_only_noop;
    }

    //updates the game state
    public void update(GameManager gm){
        gm.updateGame();
        count = 0;
        p_names.clear();
        p_moves.clear();
        p_moves.addAll(gm.getAllCurrentLegalMoves().values());
        p_names.addAll(gm.getAllCurrentLegalMoves().keySet());

        if (gm.getGameManager().allAI()){
            while (count < p_names.size()) {
                //System.out.println(p_names.get(count));
                if (!p_names.get(count).toString().equals("RANDOM")){
                    if (!noopCheck(count))
                        break;
                }

                if (count == p_names.size() - 1) {
                    update(gm);

                } else
                    count++;
            }

            return;
        }

        while(!gm.getGameManager().isTerminal(gm.getGameManager().getContents())){
            if (noopCheck(count) && p_names.get(count).isHuman())
                gm.getGamer(p_names.get(count)).setSelectedMove(p_moves.get(count).get(0));


            else if (p_names.get(count).isHuman())
                break;

            count++;

            if (count == p_names.size() )
                update(gm);

        }
    }

    //starts second scene (game playing scene)
    public Scene getPlayableScene(GameManager gm, Stage stage){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "As a human player you must manually select a move from the list to continue");
        p_moves.addAll(gm.getAllCurrentLegalMoves().values());
        p_names.addAll(gm.getAllCurrentLegalMoves().keySet());

        BorderPane root = new BorderPane();
        GridPane board = new GridPane();
        GridPane options = new GridPane();

        board.setStyle("-fx-background-color: #FFFFFF");
        board.setPadding(new Insets(10,10,10,10));
        ListView<String> list = new ListView<String>();

        drawGrid(board, gm.getGameManager());

        //listener to check if a move is selected (previews the move if its a human player)
        list.getSelectionModel().selectedIndexProperty().addListener((ChangeListener<Number>) (observable, oldValue, newValue) -> {
            if (p_names.get(count).isHuman() && newValue.intValue() >= 0){
                gm.getGamer(p_names.get(count)).setSelectedMove(list.getItems().get(newValue.intValue()));

                for (int j = 0; j < p_names.size(); j++){
                    if (noopCheck(j) && j != count && p_names.get(j).isHuman())
                        gm.getGamer(p_names.get(j)).setSelectedMove(p_moves.get(j).get(0));
                }

                if (!gm.getGameManager().getRoles().toString().contains("RANDOM"))
                    movePreview(gm, board);
            }
        });

        //control flow of the game is altered if only AI are playing
        if (!gm.getGameManager().allAI()){
            while(count < p_names.size()){

                if (p_names.get(count).isHuman() && !noopCheck(count))
                    break;

                if (noopCheck(count) && p_names.get(count).isHuman())
                    gm.getGamer(p_names.get(count)).setSelectedMove(p_moves.get(count).get(0));

                if (count == p_names.size() -1) {
                    update(gm);
                    drawGrid(board, gm.getGameManager());
                    count = 0;
                }

                else
                    count++;

            }
        }

        //control flow for a game with human players
        else {
            while (count < p_names.size()) {

                if (!noopCheck(count) && !p_names.get(count).toString().equals("RANDOM"))
                    break;

                if (count == p_names.size() - 1) {
                    update(gm);
                    drawGrid(board, gm.getGameManager());
                    count = 0;
                } else
                    count++;
            }
        }


        if (p_moves.size() > 0)
            list.setItems(FXCollections.observableArrayList (p_moves.get(count)));

        TextField moveInput = new TextField();
        Button btn = new Button();
        btn.setText("Submit move for: " + p_names.get(count));

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                //starts a new game
                if (btn.getText().equals("Start New Game")) {
                    getPlayableScene(gm, stage);
                    list.getSelectionModel().select(-1);
                    list.setItems(FXCollections.observableArrayList(p_moves.get(count)));
                    btn.setText("Submit move for: " + p_names.get(count));
                    drawGrid(board, gm.getGameManager());
                    return;
                }

                String buttonText = "Submit move for: ";

                //checks if a human player has submited a move
                if (p_names.get(count).isHuman() && list.getSelectionModel().getSelectedItem() == null) {

                    for (String move : list.getItems()) {
                        if (move.equals(moveInput.getText())){
                            gm.getGamer(p_names.get(count)).setSelectedMove(move);

                        }
                    }

                    if (gm.getGamer(p_names.get(count)).makeMove() == null ||
                            !gm.getGamer(p_names.get(count)).makeMove().equals(moveInput.getText())){
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.isPresent()) {
                            return;
                        }
                    }
                }

                boolean last = true;
                while (count < p_names.size() -1) {
                    count++;
                    if ((p_names.get(count).isHuman())) {

                        if (!noopCheck(count)){
                            last = false;
                            break;
                        }

                        else{
                            gm.getGamer(p_names.get(count)).setSelectedMove(p_moves.get(count).get(0));
                        }
                    }
                }

                //if all players moves are made updates the game
                if (count == p_names.size() -1 && last) {
                    update(gm);
                    drawGrid(board, gm.getGameManager());
                }

                //end of game
                if (gm.getGameManager().isTerminal(gm.getGameManager().getContents())){
                    gm.restartGame();
                    p_names.clear();
                    p_moves.clear();
                    btn.setText("Start New Game");
                }

                //not end of game set up for next turn
                else {
                    list.getSelectionModel().select(-1);
                    list.setItems(FXCollections.observableArrayList(p_moves.get(count)));
                    moveInput.clear();
                    btn.setText(buttonText + p_names.get(count));
                }
            }
        });

        options.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));
        options.setPadding(new Insets(10, 10, 10, 10));
        options.setAlignment(Pos.BOTTOM_LEFT);
        options.add(btn, 0, 0);
        moveInput.setPromptText("Select move from list or input here: ");
        moveInput.setPrefWidth(500);
        options.add(moveInput, 1,0);
        root.setRight(list);
        root.setCenter(board);
        root.setBottom(options);
        return new Scene(root, 800, 800);
    }

    //ititial scene for setting up game
    public Scene getSelectionScene(Stage stage){
        GridPane root = new GridPane();
        TextField textField = new TextField();
        HashMap<String, String> assign = new HashMap<>();
        ChoiceBox cb = new ChoiceBox();
        CheckBox cbHuman = new CheckBox("Human");
        CheckBox cbAI = new CheckBox("MCTS AI");
        Slider difficulty = new Slider();
        Label load = new Label("Loading game...");
        PropnetPlayer gm = new PropnetPlayer();


        //listeners for setting players to human or ai
        cb.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                first =false;
                if ("human".equals(assign.get(cb.getItems().get(newValue.intValue()))) != cbHuman.isSelected() || cbAI.isSelected() == cbHuman.isSelected())
                    first =true;
                cbHuman.setSelected("human".equals(assign.get(cb.getItems().get(newValue.intValue()))));
                cbAI.setSelected(!cbHuman.isSelected());

            }
        });

        //listeners for setting players to human or ai
        cbHuman.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                if (cb.getSelectionModel().getSelectedIndex() == 0){
                    String set = "human";
                    if (cbAI.isSelected())
                        set = "AI";

                    for (String key : assign.keySet()){
                        assign.put(key, set);
                    }
                }
                else if(!first){
                    if (cbHuman.isSelected())
                        assign.put((String) cb.getSelectionModel().getSelectedItem(), "human");
                    else
                        assign.put((String) cb.getSelectionModel().getSelectedItem(), "AI");
                }
                else
                    first = false;
                cbAI.setSelected(!newValue);


            }
        });

        //listeners for setting players to human or ai
        cbAI.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                if (cb.getSelectionModel().getSelectedIndex() == 0){
                    String set = "human";
                    if (cbAI.isSelected())
                        set = "AI";

                    for (String key : assign.keySet()){
                        assign.put(key, set);
                    }
                }
                else if(first){
                    if (cbAI.isSelected())
                        assign.put((String) cb.getSelectionModel().getSelectedItem(), "AI");
                    else
                        assign.put((String) cb.getSelectionModel().getSelectedItem(), "human");
                }
                else
                    first = false;
                cbHuman.setSelected(!newValue);


            }
        });


        //style for buttons
        cb.setStyle("-fx-font-size: 18px;");
        textField.setStyle("-fx-font-size: 18px; ");

        Button fileButton = new Button("Select Description Directory:");
        Button playButton = new Button("Assign Players");

        String dblue = "-fx-font: 22 arial; -fx-background-color: #090a0c, " +
                "linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%), " +
                "linear-gradient(#20262b, #191d22), radial-gradient(center 50% 0%, radius 100%," +
                " rgba(114,131,148,0.9), rgba(255,255,255,0));" +
                "-fx-background-radius: 5,4,3,5;" +
                "-fx-background-insets: 0,1,2,0;" +
                "-fx-text-fill: white;" +
                "-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );" +
                "-fx-font-family: \"Arial\";" +
                "-fx-text-fill: linear-gradient(white, #d0d0d0);" +
                "-fx-font-size: 20px;" +
                "-fx-padding: 10 20 10 20;";

        String win7 = "-fx-background-color: #707070, linear-gradient(#fcfcfc, #f3f3f3), " +
                "linear-gradient(#f2f2f2 0%, #ebebeb 49%, #dddddd 50%, #cfcfcf 100%); " +
                "-fx-background-insets: 0,1,2;-fx-background-radius: 3,2,1;" +
                "-fx-padding: 4 30 4 30;-fx-text-fill: black;-fx-font-size: 22px;";

        fileButton.setStyle(win7);
        playButton.setStyle(dblue);

        fileButton.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(final ActionEvent e) {
                FileChooser fileChooser = new FileChooser();
                File selectedDirectory = fileChooser.showOpenDialog(stage);

                if (selectedDirectory != null) {
                    textField.setText(selectedDirectory.getAbsolutePath());
                }

            }
        });

        //trigger new task thread and set loading icon
        playButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent e) {
                piVal = true;
                root.add(load,10,3);
            }
        });

        //set up the game and removes loading icon once complete
        Task task = new Task<Void>() {
            @Override
            public Void call() throws Exception {

                while(!playing){
                    if (piVal) {
                        piVal = false;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {

                                Alert invalidDescription = new Alert(Alert.AlertType.CONFIRMATION, "The game description you have selected " +
                                        "cannot be parsed. Please check description is valid.");
                                if (playButton.getText().equals("Assign Players")) {

                                    LexicalAnalyser l = new LexicalAnalyser();
                                    try {
                                        boolean validDescription = false;
                                        if (l.analyseFile(textField.getText())) {
                                            dt = new DescriptionTable(l.getTokenStream());
                                            if (dt.listTable() != null) {
                                                gm.initialize(dt.listTable());
                                                validDescription = true;
                                            }
                                        }

                                        if (!validDescription) {
                                            Optional<ButtonType> result = invalidDescription.showAndWait();
                                            if (result.isPresent()) {
                                                return;
                                            }
                                        }


                                        ObservableList options = FXCollections.observableArrayList();
                                        options.addAll("Default", new Separator());
                                        assign.put("Default", "human");
                                        for (Player player : gm.getRoles()) {
                                            if (!player.toString().equals("RANDOM")) {
                                                options.add(player.toString());
                                                assign.put(player.toString(), "human");
                                            }
                                        }
                                        cb.setItems(FXCollections.observableArrayList(options));
                                        cb.getSelectionModel().selectFirst();

                                        Label difLabel = new Label("Time for AI moves:");
                                        difLabel.setFont(Font.font("Arial", 18));
                                        difficulty.setMin(0);
                                        difficulty.setMax(60);
                                        difficulty.setValue(5);
                                        difficulty.setShowTickLabels(true);
                                        difficulty.setShowTickMarks(true);
                                        difficulty.setMajorTickUnit(30);
                                        difficulty.setMinorTickCount(5);
                                        difficulty.setBlockIncrement(5);

                                        root.add(difLabel, 6, 5);
                                        root.add(difficulty, 7, 5);
                                        root.add(cb, 6, 4);
                                        root.add(cbHuman, 7, 4);
                                        root.add(cbAI, 8, 4);
                                        playButton.setText("Play Game");

                                    } catch (Exception e1) {
                                        System.out.println("file invalid");
                                    }
                                }
                                else {
                                    ArrayList<Gamer> gamers = new ArrayList<>();


                                    assign.remove(assign.get(0));
                                    for (String gamerType : assign.keySet()) {
                                        if (assign.get(gamerType).equals("human"))
                                            gamers.add(new HumanPlayer());
                                        else {
                                            gamers.add(new MonteCarloPlayer(difficulty.getValue()));
                                        }

                                    }
                                    GameManager gameManager = new GameManager(gamers);
                                    gameManager.setupGame(gm, new ArrayList<>(assign.keySet()));
                                    gm.setMyRole("RANDOM");
                                    stage.setScene(getPlayableScene(gameManager, stage));
                                    playing = true;

                                }

                                root.getChildren().remove(load);
                            }

                        });

                    }
                    else
                        Thread.sleep(500);
                }
                return null;
            }
        };

        new Thread(task).start();

        root.setAlignment(Pos.BASELINE_LEFT);
        root.setHgap(2);
        root.setVgap(10);
        root.setPadding(new Insets(25, 25, 25, 25));


        root.add(fileButton, 6 ,3);
        root.add(playButton, 6 ,6);
        root.add(textField, 7, 3);
        return new Scene(root, 800, 800);
    }

    public void movePreview(GameManager gm, GridPane board){
        gm.updateGame();
        drawGrid(board, gm.getGameManager());
        gm.undo();
    }

}
