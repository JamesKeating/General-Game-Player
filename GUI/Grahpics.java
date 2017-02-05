package GUI;

import DescriptionProcessing.Player;
import Player.*;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import Player.GameManager;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

/**
 * Created by siavj on 31/01/2017.
 */
public class Grahpics extends Application {

    private int count = 0;
    ArrayList<ArrayList<String>> p_moves = new ArrayList<>();
    ArrayList<Player> p_names = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("GGP Console");
//        primaryStage.setScene(getSelectionScene());
        primaryStage.setScene(getPlayableScene());
        primaryStage.show();

    }

    public GridPane drawGrid(GridPane gridPane, PropnetPlayer gm){

        gridPane.getChildren().clear();
        for (Drawable drawable : gm.getDrawable(gm.getContents())){
            gridPane.add(drawable.getImage(), drawable.gety(), drawable.getx());
        }

        return gridPane;

    }

    public boolean noopCheck(){
        boolean op_has_only_noop = true;
        boolean i_have_only_noop = false;

        for (int i = 0; i < p_names.size(); i++) {

            if (p_moves.get(i).size() == 1){

                if (p_moves.get(i).get(0).contains("noop")){
                    if (count == i)
                        i_have_only_noop = true;
                }
                else if (count != i)
                    op_has_only_noop = false;
            }
            else if (count != i)
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
        while(!gm.getGameManager().isTerminal(gm.getGameManager().getContents())){
            if (noopCheck()){
                gm.getGamer(p_names.get(count)).setSelectedMove(p_moves.get(count).get(0));
            }

            else if (p_names.get(count).isHuman()){
                break;
            }

            count++;
            if (count == p_names.size() -1){
                update(gm);
            }
        }
    }

    public Scene getPlayableScene(){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "As a human player you must manually select a move from the list to continue");
//        GameManager gm = new GameManager(new PropnetPlayer(), new HumanPlayer());
//        gm.setupGame("lexerTest", new String[]{"xplayer", "oplayer"});

        GameManager gm = new GameManager(new HumanPlayer());
        gm.setupGame("buttons", new String[]{"player"});

        p_moves.addAll(gm.getAllCurrentLegalMoves().values());
        p_names.addAll(gm.getAllCurrentLegalMoves().keySet());

        BorderPane root = new BorderPane();
        GridPane board = new GridPane();
        GridPane options = new GridPane();

        ListView<String> list = new ListView<String>();
        if (p_moves.size() > 0)
            list.setItems(FXCollections.observableArrayList (p_moves.get(count)));

        drawGrid(board, gm.getGameManager());

        list.getSelectionModel().selectedIndexProperty().addListener((ChangeListener<Number>) (observable, oldValue, newValue) -> {
            if (p_names.get(count).isHuman() && newValue.intValue() >= 0){
                gm.getGamer(p_names.get(count)).setSelectedMove(list.getItems().get(newValue.intValue()));
                if (!noopCheck() || p_names.size() == 1){
                    movePreview(gm, board);
                }
            }

        });



        while(count < p_names.size() - 1){
            if (p_names.get(count).isHuman() && !noopCheck())
                break;
            count++;
        }

        Button btn = new Button();
        btn.setText("Submit move for: " + p_names.get(count));

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String buttonText = "Submit move for: ";

                if (p_names.get(count).isHuman() && list.getSelectionModel().getSelectedItem() == null){

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        return;
                    }

                }

                while(count < p_names.size() - 1){
                    count++;
                    if (p_names.get(count - 1).isHuman() && !noopCheck()){
                        break;
                    }
                }

                if (count == p_names.size() -1) {
                    update(gm);
                    drawGrid(board, gm.getGameManager());
                }

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

    public Scene getSelectionScene(){

        return null;
    }

    public void movePreview(GameManager gm, GridPane board){
        gm.updateGame();
        drawGrid(board, gm.getGameManager());
        gm.undo();
    }
}
