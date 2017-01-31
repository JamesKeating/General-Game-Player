package GUI;

import DescriptionProcessing.Player;
import Player.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import Player.GameManager;

import java.util.*;

/**
 * Created by siavj on 31/01/2017.
 */
public class Grahpics extends Application {

    private int count;
    ArrayList<ArrayList<String>> p_moves = new ArrayList<>();
    ArrayList<Player> p_names = new ArrayList<>();

    public static void main(String[] args) {

        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("GGP Console");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "As a human player you must manually select a move from the list to continue");
        GameManager gm = new GameManager(new PropnetPlayer(), new HumanPlayer());
        gm.setupGame("lexerTest", new String[]{"xplayer", "oplayer"});

        p_moves.addAll(gm.getAllCurrentLegalMoves().values());
        p_names.addAll(gm.getAllCurrentLegalMoves().keySet());



        ListView<String> list = new ListView<String>();

        if (p_moves.size() > 0)
            list.setItems(FXCollections.observableArrayList (p_moves.get(count)));

        Button btn = new Button();
        if (p_names.get(count).isHuman())
            btn.setText("Submit move for: " + p_names.get(count));
        else
            btn.setText("AI select for: " + p_names.get(count));


        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String buttonText = "Submit move for: ";
                if (p_names.get(count).isHuman()){

                    if (list.getSelectionModel().getSelectedItem() != null){
                        gm.getGamer(p_names.get(count)).setSelectedMove(list.getSelectionModel().getSelectedItem());
                    }

                    else{
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            return;
                        }
                    }
                }


                if (count + 1 < p_names.size()){
                    count++;
                }

                else {
                    gm.updateGame();
                    count = 0;
                    p_names.clear();
                    p_moves.clear();
                    p_moves.addAll(gm.getAllCurrentLegalMoves().values());
                    p_names.addAll(gm.getAllCurrentLegalMoves().keySet());
                    if (gm.getGameManager().isTerminal(gm.getGameManager().getContents()))
                        System.exit(0);
                }

                if (!p_names.get(count).isHuman())
                    buttonText = "AI select for: ";
                list.setItems(FXCollections.observableArrayList (p_moves.get(count)));
                btn.setText(buttonText + p_names.get(count));
            }
        });


        StackPane root = new StackPane();
        root.getChildren().add(list);
        root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();

    }

}
