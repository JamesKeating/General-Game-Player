package GUI;

import DescriptionProcessing.Player;
import GDLParser.LexicalAnalyser;
import Player.*;
import SylmbolTable.DescriptionTable;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import Player.GameManager;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by siavj on 31/01/2017.
 */
public class Grahpics extends Application {

    private int count = 0;
    private ArrayList<ArrayList<String>> p_moves = new ArrayList<>();
    private ArrayList<Player> p_names = new ArrayList<>();
    private DescriptionTable dt = null;
    private boolean first =true;

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("GGP Console");
        primaryStage.setScene(getSelectionScene(primaryStage));
//
//        ArrayList<Gamer> x =new ArrayList<Gamer>();
//        ArrayList<String> y =new ArrayList<>();
//        y.add("white");
//        y.add("red");
////        y.add("player");
//
//        x.add(new HumanPlayer());
//        x.add(new HumanPlayer());
////        x.add(new HumanPlayer());
//
//
//        GameManager gm = new GameManager(x);
//        LexicalAnalyser l = new LexicalAnalyser();
//
//        l.analyseFile("D:\\FYP\\General-Game-Player\\Data\\ArthursGame");
////        l.analyseFile("D:\\FYP\\General-Game-Player\\Data\\buttons");
//
//        dt = new DescriptionTable(l.getTokenStream());
//
//        gm.setupGame(dt, y);
//        System.out.println("1");

//        primaryStage.setScene(getPlayableScene(gm));
        primaryStage.show();

    }

    public GridPane drawGrid(GridPane gridPane, PropnetPlayer gm){
//        System.out.println(gm.getContents());
        gridPane.getChildren().clear();
        for (Drawable drawable : gm.getDrawable(gm.getContents())){
            gridPane.add(drawable.getImage(), drawable.gety(), drawable.getx());
        }

        return gridPane;

    }

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

    public void update(GameManager gm){
        gm.updateGame();
        count = 0;
        p_names.clear();
        p_moves.clear();
        p_moves.addAll(gm.getAllCurrentLegalMoves().values());
        p_names.addAll(gm.getAllCurrentLegalMoves().keySet());

        if (gm.getGameManager().allAI()){
            while (noopCheck(count)){
                count++;
            }
            return;
        }

        while(!gm.getGameManager().isTerminal(gm.getGameManager().getContents())){
            if (noopCheck(count) && p_names.get(count).isHuman()){
                gm.getGamer(p_names.get(count)).setSelectedMove(p_moves.get(count).get(0));
            }

            else if (p_names.get(count).isHuman()){
                break;
            }

            count++;

            if (count == p_names.size() ){
                update(gm);
            }
        }
    }

    public Scene getPlayableScene(GameManager gm){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "As a human player you must manually select a move from the list to continue");

        p_moves.addAll(gm.getAllCurrentLegalMoves().values());
        p_names.addAll(gm.getAllCurrentLegalMoves().keySet());

        BorderPane root = new BorderPane();
        GridPane board = new GridPane();
        GridPane options = new GridPane();

        ListView<String> list = new ListView<String>();

        drawGrid(board, gm.getGameManager());

        list.getSelectionModel().selectedIndexProperty().addListener((ChangeListener<Number>) (observable, oldValue, newValue) -> {
            if (p_names.get(count).isHuman() && newValue.intValue() >= 0){
                gm.getGamer(p_names.get(count)).setSelectedMove(list.getItems().get(newValue.intValue()));

                for (int j = 0; j < p_names.size(); j++){
                    if (noopCheck(j) && j != count && p_names.get(j).isHuman())
                        gm.getGamer(p_names.get(j)).setSelectedMove(p_moves.get(j).get(0));
                }
                movePreview(gm, board);
            }
        });

        while(count < p_names.size()-1 && !gm.getGameManager().allAI()){

            if (p_names.get(count).isHuman() && !noopCheck(count))
                break;

            count++;

            if (count == p_names.size() -1) {
                for (int j = 0; j < p_names.size(); j++){
                    if (noopCheck(j) && p_names.get(j).isHuman())
                        gm.getGamer(p_names.get(j)).setSelectedMove(p_moves.get(j).get(0));

                }
            }
        }
        if (p_moves.size() > 0)
            list.setItems(FXCollections.observableArrayList (p_moves.get(count)));

        Button btn = new Button();
        btn.setText("Submit move for: " + p_names.get(count));

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String buttonText = "Submit move for: ";


                if (p_names.get(count).isHuman() && list.getSelectionModel().getSelectedItem() == null) {

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        return;
                    }
                }



                while (count < p_names.size() -1) {
                    count++;
                    if ((p_names.get(count).isHuman())) {
                        if (!noopCheck(count))
                            break;

                        else{
                            gm.getGamer(p_names.get(count)).setSelectedMove(p_moves.get(count).get(0));
                        }
                    }
                }


                if (count == p_names.size() -1) {
                    update(gm);
                    drawGrid(board, gm.getGameManager());
                }

                System.out.println(gm.getGameManager().getContents());
                list.getSelectionModel().select(-1);
                list.setItems(FXCollections.observableArrayList (p_moves.get(count)));
                btn.setText(buttonText + p_names.get(count));

                if (gm.getGameManager().isTerminal(gm.getGameManager().getContents())){
                    root.getChildren().remove(list);
                    root.getChildren().remove(options);
                }
            }
        });

        options.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        options.setPadding(new Insets(10, 10, 10, 10));
        options.setAlignment(Pos.BOTTOM_CENTER);
        options.add(btn, 0, 0);

        root.setRight(list);
        root.setCenter(board);
        root.setBottom(options);
        return new Scene(root, 800, 800);
    }

    public Scene getSelectionScene(Stage stage){
        GridPane root = new GridPane();
        TextField textField = new TextField();
        HashMap<String, String> assign = new HashMap<>();
        ChoiceBox cb = new ChoiceBox();
        CheckBox cbHuman = new CheckBox("Human");
        CheckBox cbAI = new CheckBox("AI");

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

        playButton.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(final ActionEvent e) {

                if (playButton.getText().equals("Assign Players")){
                    LexicalAnalyser l = new LexicalAnalyser();
                    try {
                        l.analyseFile(textField.getText());
                        dt = new DescriptionTable(l.getTokenStream());
                        PropnetPlayer gm = new PropnetPlayer();
                        gm.initialize(dt.listTable());

                        ObservableList options = FXCollections.observableArrayList();
                        options.addAll("Default", new Separator());
                        assign.put("Default", "human");
                        for (Player player : gm.getRoles()){
                            if (!player.toString().equals("RANDOM")){
                                options.add(player.toString());
                                assign.put(player.toString(), "human");
                            }
                        }
                        cb.setItems(FXCollections.observableArrayList(options));
                        cb.getSelectionModel().selectFirst();

                        root.add(cb, 6, 4);
                        root.add(cbHuman, 7, 4);
                        root.add(cbAI, 8, 4);
                        playButton.setText("Play Game");
                    }catch (Exception e1){
                        System.out.println("file invalid");
                    }
                }

                else{
                    ArrayList<Gamer> gamers = new ArrayList<>();

                    assign.remove(assign.get(0));
                    for (String gamerType : assign.keySet()){
                        if (assign.get(gamerType).equals("human"))
                            gamers.add(new HumanPlayer());
                        else{
                            gamers.add(new PureMC());
                        }

                    }
                    GameManager gameManager = new GameManager(gamers);

                    gameManager.setupGame(dt, new ArrayList<>(assign.keySet()));
                    stage.setScene(getPlayableScene(gameManager));
                }



            }
        });


        root.setAlignment(Pos.BASELINE_LEFT);
        root.setHgap(2);
        root.setVgap(10);
        root.setPadding(new Insets(25, 25, 25, 25));


        root.add(fileButton, 6 ,3);
        root.add(playButton, 6 ,5);
        root.add(textField, 7, 3);
        return new Scene(root, 800, 800);
    }

    public void movePreview(GameManager gm, GridPane board){
        gm.updateGame();
        drawGrid(board, gm.getGameManager());
        gm.undo();
    }

}
