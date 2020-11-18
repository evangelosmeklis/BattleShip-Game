package sample;

import java.io.*;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import sample.Board.Cell;

public class Main extends Application {

    private boolean running = false;
    private Board enemyBoard, playerBoard;
    public boolean finished = false;
    private int shipsToPlace = 5;
    public int twice2=0;
    public int start=0;
    public String thetext= " ";
    public int loaded=0;
    public int first = 0;
    private boolean enemyTurn= true;
    public int[][] entered = new int[1000][1000];

    StringProperty winner = new SimpleStringProperty();


    private Random random = new Random();

    private String readFile(File file){
        StringBuilder stringBuffer = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {

            bufferedReader = new BufferedReader(new FileReader(file));

            String text;
            while ((text = bufferedReader.readLine()) != null) {
                stringBuffer.append(text);
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return stringBuffer.toString();
    }

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

        EventHandler<ActionEvent> event3 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                StackPane root2 = new StackPane();
                Label label = new Label("Your are now in the second form");

                TilePane pane = new TilePane();
                for (int x = 0; x < enemyBoard.states.length; x++){
                        Label label1 = new Label(enemyBoard.states[x][0] + " state is: " + enemyBoard.states[x][1]);
                        pane.getChildren().add(label1);
                }

                root2.getChildren().add(label);
                Scene secondScene = new Scene(pane, 500,500);
                Stage newstage = new Stage();
                Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
                newstage.setTitle("Pop Up Window Enemy Ships State");
                newstage.setX(primaryScreenBounds.getMinX());
                newstage.setY(primaryScreenBounds.getMinY());
                newstage.setWidth(primaryScreenBounds.getWidth()-400);
                newstage.setHeight(primaryScreenBounds.getHeight()-100);
                newstage.setScene(secondScene);
                newstage.setResizable(false);
                newstage.show();
            }
        };

        EventHandler<ActionEvent> event4 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                StackPane root2 = new StackPane();
                Label label = new Label("Your are now in the second form");

                TilePane pane1 = new TilePane();
                TilePane pane2 = new TilePane();
                TilePane pane3 = new TilePane();
                TilePane pane4 = new TilePane();
                TilePane pane5 = new TilePane();
                int tempor = 0;

                int st=0;
                if (playerBoard.efcounter.get() < 5 ) st = 0;
                else st= playerBoard.efcounter.get();
                for (int x = st; x < enemyBoard.efcounter.get(); x++){
                        String res = "Not Available";
                        if (enemyBoard.shotsfired[x][3] == 5) res = "Carrier";
                        if (enemyBoard.shotsfired[x][3] == 4) res = "Battleship";
                        if (enemyBoard.shotsfired[x][3] == 32) res = "Cruiser";
                        if (enemyBoard.shotsfired[x][3] == 31) res = "Submarine";
                        if (enemyBoard.shotsfired[x][3] == 2) res = "Destroyer";
                        String res1 = "Not Available";
                        if (enemyBoard.shotsfired[x][2] == 0) res1 = "Not hit";
                        if (enemyBoard.shotsfired[x][2] == 1) res1 = "Hit";
                        Label label1 = new Label("Player shots cell with x: " + enemyBoard.shotsfired[x][0] + " and y: " + enemyBoard.shotsfired[x][1] + " with result: " + res1 + " and ship type: " + res  );
                        if (tempor == 0) pane1.getChildren().add(label1);
                        if (tempor == 1) pane2.getChildren().add(label1);
                        if (tempor == 2) pane3.getChildren().add(label1);
                        if (tempor == 3) pane4.getChildren().add(label1);
                        if (tempor == 4) pane5.getChildren().add(label1);
                        tempor++;
                }

                root2.getChildren().add(label);
                VBox vboxvag = new VBox(100, pane1,pane2,pane3,pane4,pane5);

                Scene secondScene = new Scene(vboxvag, 500,500);
                Stage newstage = new Stage();
                Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
                newstage.setTitle("Pop Up Window Player Shots");
                newstage.setX(primaryScreenBounds.getMinX());
                newstage.setY(primaryScreenBounds.getMinY());
                newstage.setWidth(primaryScreenBounds.getWidth()-400);
                newstage.setHeight(primaryScreenBounds.getHeight()-100);
                newstage.setScene(secondScene);
                newstage.setResizable(false);
                newstage.show();
            }
        };

        EventHandler<ActionEvent> event5 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                StackPane root2 = new StackPane();
                Label label = new Label("Your are now in the second form");

                TilePane pane1 = new TilePane();
                TilePane pane2 = new TilePane();
                TilePane pane3 = new TilePane();
                TilePane pane4 = new TilePane();
                TilePane pane5 = new TilePane();
                int tempor = 0;

                int st=0;
                System.out.println(playerBoard.efcounter.get());
                if (playerBoard.efcounter.get() < 5 ) st = 0;
                else st= playerBoard.efcounter.get();
                for (int x = st; x < playerBoard.efcounter.get(); x++){
                    String res = "Not Available";
                    if (playerBoard.shotsfired[x][3] == 5) res = "Carrier";
                    if (playerBoard.shotsfired[x][3] == 4) res = "Battleship";
                    if (playerBoard.shotsfired[x][3] == 32) res = "Cruiser";
                    if (playerBoard.shotsfired[x][3] == 31) res = "Submarine";
                    if (playerBoard.shotsfired[x][3] == 2) res = "Destroyer";
                    String res1 = "Not Available";
                    if (playerBoard.shotsfired[x][2] == 0) res1 = "Not hit";
                    if (playerBoard.shotsfired[x][2] == 1) res1 = "Hit";
                    Label label1 = new Label("Player shots cell with x: " + playerBoard.shotsfired[x][0] + " and y: " + playerBoard.shotsfired[x][1] + " with result: " + res1 + " and ship type: " + res  );
                    if (tempor == 0) pane1.getChildren().add(label1);
                    if (tempor == 1) pane2.getChildren().add(label1);
                    if (tempor == 2) pane3.getChildren().add(label1);
                    if (tempor == 3) pane4.getChildren().add(label1);
                    if (tempor == 4) pane5.getChildren().add(label1);
                    tempor++;
                }

                root2.getChildren().add(label);
                VBox vboxvag = new VBox(100, pane1,pane2,pane3,pane4,pane5);

                Scene secondScene = new Scene(vboxvag, 500,500);
                Stage newstage = new Stage();
                Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
                newstage.setTitle("Pop Up Window Player Shots");
                newstage.setX(primaryScreenBounds.getMinX());
                newstage.setY(primaryScreenBounds.getMinY());
                newstage.setWidth(primaryScreenBounds.getWidth()-400);
                newstage.setHeight(primaryScreenBounds.getHeight()-100);
                newstage.setScene(secondScene);
                newstage.setResizable(false);
                newstage.show();
            }
        };

        EventHandler<ActionEvent> event6 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                menuItem1.setDisable(true);
                FileChooser fileChooser = new FileChooser();

                //Set extension filter
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
                fileChooser.getExtensionFilters().add(extFilter);

                //Show save file dialog
                File file = fileChooser.showOpenDialog(null);
                TextArea textArea  = new TextArea();
                if(file != null){
                    loaded = 1;
                    thetext = readFile(file);
                    String[] splitted =  thetext.split("[,]",0);
                    int temp;
                    int counts=0,i=0,j=0;
                    while(j<splitted.length){
                        int temporary  = Integer.parseInt(splitted[j]);
                        if (temporary<=9) {
                            entered[counts][i] = temporary;
                            i++;
                            if (i >=3 && i % 4 == 0 ){
                                i=0;
                                counts++;
                            }
                        }
                        else {
                            int next = temporary % 10;
                            int prev = temporary/10;

                            entered[counts][i] = prev;
                            counts++;
                            i=0;

                            entered[counts][i] = next;
                            i++;
                        }
                        j++;
                    }
                    for(int t = 0 ; t <5 ; t++){
                        for(int m = 0 ; m<4 ; m++){
                            System.out.print(entered[t][m]);
                            System.out.print(",");
                        }
                        System.out.println();
                    }
                }

            }
        };
        menuItem1.setOnAction(event1);
        menuItem2.setOnAction(event6);
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

        menuItem21.setOnAction(event3);
        menuItem22.setOnAction(event4);
        menuItem23.setOnAction(event5);




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
            if (first > 0 && finished == false) {
                Cell cell = (Cell) event.getSource();
                if (cell.wasShot)
                    return;

                enemyTurn = !cell.shoot();
                if (enemyBoard.totalshots>40) enemyTurn = false;
                else enemyTurn = true;

                if (enemyBoard.ships == 0 || (playerBoard.totalshots<=0 && enemyBoard.totalshots<=0 && playerBoard.points<enemyBoard.points)) {
                    finished = true;
                    System.out.println("YOU WIN");
                    winner.set("PLAYER WINS");
                }
                if (enemyBoard.ships == 0 || (playerBoard.totalshots<=0 && enemyBoard.totalshots<=0 && playerBoard.points>enemyBoard.points)) {
                    finished= true;
                    System.out.println("YOU LOSE");
                    winner.set("COMPUTER WINS");
                }
            }
            if (enemyTurn)
                enemyMove();
        });

        playerBoard = new Board(false, event -> {
            if (running)
                return;

            Cell cell = (Cell) event.getSource();
            if (start==1 && loaded==0 ) {
                menuItem1.setDisable(true);
                menuItem2.setDisable(true);
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
            else {
                menuItem1.setDisable(true);
                menuItem2.setDisable(true);
                for (int i=0 ; i<5 ; i++){
                    System.out.println(i);
                    boolean orient = true;
                    if (entered[i][3] == 1 ) orient = false;
                    if (playerBoard.placeShip(new Ship(shipsToPlace, orient), entered[i][1], entered[i][2])) {
                        if (--shipsToPlace == 1) {
                            startGame();
                        }
                        if (shipsToPlace == 2 && twice2 == 0) {
                            shipsToPlace = 3;
                            twice2++;
                        }
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
        while (enemyTurn && finished ==false) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);

            Cell cell = playerBoard.getCell(x, y);
            if (cell.wasShot)
                continue;

            enemyTurn = cell.shoot();
            if (playerBoard.totalshots>40) enemyTurn = true;
            else enemyTurn=false;
            //System.out.println(playerBoard.totalshots);
            //System.out.println(enemyBoard.totalshots);

            if (playerBoard.ships == 0 || (playerBoard.totalshots<=0 && enemyBoard.totalshots<=0 && playerBoard.points>enemyBoard.points) ) {
                finished=true;
                System.out.println("YOU LOSE");
                winner.set("COMPUTER WINS");
            }
            if (playerBoard.ships == 0 || (playerBoard.totalshots<=0 && enemyBoard.totalshots<=0 && playerBoard.points<enemyBoard.points) ) {
                finished=true;
                System.out.println("YOU WIN");
                winner.set("PLAYER WINS");
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
        //System.out.println("Player board name: " + playerBoard);
        //System.out.println("Computer board name: " + enemyBoard);

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
