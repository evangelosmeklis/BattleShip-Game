package sample;

import java.util.Random;

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import sample.Board.Cell;

public class Main extends Application {

    private boolean running = false;
    private Board enemyBoard, playerBoard;

    private int shipsToPlace = 5;
    public int twice2=0;
    public int start=0;
    public int first = 0;
    private boolean enemyTurn= true;

    StringProperty winner = new SimpleStringProperty();


    private Random random = new Random();

    private Parent createContent() {
        BorderPane root = new BorderPane();
        root.setPrefSize(600, 800);



        Menu menu1 = new Menu("APPLICATION");
        MenuItem menuItem1 = new MenuItem("START");
        MenuItem menuItem2 = new MenuItem("LOAD");
        MenuItem menuItem3 = new MenuItem("EXIT");


        menu1.getItems().add(menuItem1);
        menu1.getItems().add(menuItem2);
        menu1.getItems().add(menuItem3);

        EventHandler<ActionEvent> event1 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                start=1;
            }
        };

        EventHandler<ActionEvent> event2 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                System.exit(0);
            }
        };

        menuItem1.setOnAction(event1);
        menuItem3.setOnAction(event2);


        Menu menu2 = new Menu("DETAILS");
        MenuBar menuBar = new MenuBar();
        menuBar.addEventFilter(MouseEvent.MOUSE_PRESSED, ev -> {
            if (ev.getButton() == MouseButton.SECONDARY) {
                ev.consume();
            }
        });
        MenuItem menuItem21 = new MenuItem("ENEMY SHIPS");
        MenuItem menuItem22 = new MenuItem("PLAYER SHOTS");
        MenuItem menuItem23 = new MenuItem("ENEMY SHOTS");


        menu2.getItems().add(menuItem21);
        menu2.getItems().add(menuItem22);
        menu2.getItems().add(menuItem23);



        menuBar.getMenus().add(menu1);
        menuBar.getMenus().add(menu2);


        //Setting font to the label
        //Filling color to the label
        //Setting the position

        enemyBoard = new Board(true, event -> {
            if (!running)
                return;
            if (first == 0 && (int)Math.round(Math.random()) ==0){
                first++;
            }
            if (first > 0) {
                Cell cell = (Cell) event.getSource();
                if (cell.wasShot)
                    return;

                enemyTurn = !cell.shoot();
                enemyTurn = true;

                if (enemyBoard.ships == 0) {
                    System.out.println("YOU WIN");
                    winner.set("PLAYER WINS");
                }
            }
            if (enemyTurn)
                enemyMove();
        });

        playerBoard = new Board(false, event -> {
            if (running)
                return;

            Cell cell = (Cell) event.getSource();
            if (start==1) {
                if (playerBoard.placeShip(new Ship(shipsToPlace, event.getButton() == MouseButton.PRIMARY), cell.x, cell.y)) {
                    if (--shipsToPlace == 1) {
                        startGame();
                    }
                    if (shipsToPlace == 2 && twice2 == 0) {
                        shipsToPlace = 3;
                        twice2++;
                    }
                }
            }
        });
        Label label1 = new Label("Player Points: ");
        Label label11 = new Label();
        label11.textProperty().bind(enemyBoard.epoints.asString());
        Label label2 = new Label("Computer Points: ");
        Label label21 = new Label();
        label21.textProperty().bind(playerBoard.epoints.asString());
        Label label3 = new Label("Player Ships Available: ");
        Label label31 = new Label();
        label31.textProperty().bind(playerBoard.eships.asString());
        Label label4 = new Label("Computer Ships Available: ");
        Label label41 = new Label();
        label41.textProperty().bind(enemyBoard.eships.asString());
        Label label5 = new Label("Player Successful shots: ");
        Label label51 = new Label();
        label51.textProperty().bind(enemyBoard.eshots.asString());
        Label label6 = new Label("Computer Successful shots: ");
        Label label61 = new Label();
        label61.textProperty().bind(playerBoard.eshots.asString());
        ToolBar toolBar = new ToolBar(label1,label11,label2,label21,label3,label31,label4,label41,label5,label51,label6,label61);

        VBox vbox1 = new VBox(0,menuBar,toolBar);
        vbox1.setAlignment(Pos.TOP_LEFT);

        Label labelen = new Label("COMPUTER BOARD - Shots Left: ");
        Label labeletotalshots = new Label();
        labeletotalshots.textProperty().bind(playerBoard.etotalshots.asString());


        ToolBar toolBar2 = new ToolBar(labelen,labeletotalshots);
        VBox vbox2 = new VBox(0, toolBar2,enemyBoard);
        vbox2.setAlignment(Pos.CENTER_RIGHT);

        Label labelpl = new Label("PLAYER BOARD - Shots Left: ");
        Label labelpltotalshots = new Label();
        labelpltotalshots.textProperty().bind(enemyBoard.etotalshots.asString());
        ToolBar toolBar3 = new ToolBar(labelpl,labelpltotalshots);
        VBox vbox3 = new VBox(0,toolBar3, playerBoard);
        vbox3.setAlignment(Pos.CENTER_LEFT);


        VBox vbox4 = new VBox(100, label1,label2,label3,label4,label5,label6);
        vbox4.setAlignment(Pos.CENTER_LEFT);
        Label labelwin = new Label();
        labelwin.textProperty().bind(winner);


        root.setTop(vbox1);
        root.setRight(vbox2);
        root.setLeft(vbox3);
        root.setCenter(labelwin);

        return (root);
    }

    private void enemyMove() {
        first++;
        while (enemyTurn) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);

            Cell cell = playerBoard.getCell(x, y);
            if (cell.wasShot)
                continue;

            enemyTurn = cell.shoot();
            enemyTurn = false;

            if (playerBoard.ships == 0) {
                System.out.println("YOU LOSE");
                winner.set("COMPUTER WINS");
            }
        }
    }

    private void startGame() {
        // place enemy ships
        int type = 5;
        int twice = 0;
        while (type > 1) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);

            if (enemyBoard.placeShip(new Ship(type, Math.random() < 0.5), x, y)) {
                if (type == 3 && twice == 0) twice++;
                else type--;
            }
        }

        running = true;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Scene scene = new Scene(createContent());
        primaryStage.setTitle("MediaLab Battleship");
        primaryStage.setResizable(false);


        primaryStage.setScene(scene);

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        //set Stage boundaries to visible bounds of the main screen

        primaryStage.setX(primaryScreenBounds.getMinX());
        primaryStage.setY(primaryScreenBounds.getMinY());
        primaryStage.setWidth(primaryScreenBounds.getWidth()-400);
        primaryStage.setHeight(primaryScreenBounds.getHeight()-100);



        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }


}
