package sample;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Board extends Parent{
    private VBox rows = new VBox();
    private boolean enemy  = false;
    public int ships = 5;
    public int shots = 0;
    public int totalshots=40;
    public int points = 0;
    public int times = 0;
    IntegerProperty eships = new SimpleIntegerProperty(ships);
    IntegerProperty eshots = new SimpleIntegerProperty(shots);
    IntegerProperty etotalshots = new SimpleIntegerProperty(totalshots);
    IntegerProperty epoints = new SimpleIntegerProperty(points);

    public int[][] places = new int[5][4];
    public String[][] states = new String[5][2];
    public int[][] shotsfired = new int[50][5];
    public int myfcounter = 0;
    IntegerProperty efcounter = new SimpleIntegerProperty(myfcounter);
    public int countshots = 0;




    public Board(boolean enemy, EventHandler<? super MouseEvent> handler){
        this.enemy=enemy;
        states[0][0] = "Carrier";
        states[1][0] = "Battleship";
        states[2][0] = "Cruiser";
        states[3][0] = "Submarine";
        states[4][0] = "Destroyer";


        for (int t = 0 ; t < 5 ;  t++ ){
            states[t][1] = "Intact";
        }
        for (int x = 0; x < 10; x++) {
            HBox row = new HBox();
            for (int y = 0; y < 10; y++) {
                Cell c = new Cell(x, y, this);
                c.setOnMouseClicked(handler);
                row.getChildren().add(c);
            }

            rows.getChildren().add(row);
        }

        getChildren().add(rows);
    }

    public boolean placeShip(Ship ship, int x, int y){
        boolean goesin = true;
        try{
            canPlaceShip(ship, x, y);
        }
        catch(OversizeException ovex){
            goesin=false;
        }
        catch (OverlapTilesException olex){
            goesin=false;
        }
        catch (AdjacentTileException adex){
            goesin= false;
        }
        if (goesin) {
            int length = ship.type;
            places[times][0]= times+1;
            places[times][1]= x;
            places[times][2]= y;
            if (ship.vertical) places[times][3]= 2;
            else places[times][3]=1;
            times++;

            if (!ship.vertical) {
                for (int i = y; i < y + length; i++) {
                    Cell cell = getCell(x, i);
                    cell.ship = ship;
                    if (!enemy) {
                        Color a = Color.web("rgba(30,144,255,0.8)");
                        cell.setFill(a);
                        cell.setStroke(a);
                    }
                }
            }
            else {
                for (int i = x; i < x + length; i++) {
                    Cell cell = getCell(i, y);
                    cell.ship = ship;
                    if (!enemy) {
                        Color a = Color.web("rgba(30,144,255,0.8)");
                        cell.setFill(a);
                        cell.setStroke(a);
                    }
                }
            }

            return true;
        }

        return false;
    }

    public Cell getCell(int x, int y){
        return (Cell)((HBox)rows.getChildren().get(x)).getChildren().get(y);
    }

    private Cell[] getNeighbors(int x, int y){
        Point2D[] points = new Point2D[] {
                new Point2D(x - 1, y),
                new Point2D(x + 1, y),
                new Point2D(x, y - 1),
                new Point2D(x, y + 1)
        };

        List<Cell> neighbors = new ArrayList<Cell>();

        for (Point2D p : points) {
            if (isValidPoint(p)) {
                neighbors.add(getCell((int)p.getX(), (int)p.getY()));
            }
        }

        return neighbors.toArray(new Cell[0]);
    }

    private boolean canPlaceShip(Ship ship, int x, int y){
        int length = ship.type;

        if (!ship.vertical) {
            for (int i = y; i < y + length; i++) {
                if (!isValidPoint(x, i)){
                    throw new OversizeException("The ship must be inside table limits");
                }

                Cell cell = getCell(x, i);
                if (cell.ship != null){
                    throw new OverlapTilesException("The ship cannot overlap with another ship");
                }

                for (Cell neighbor : getNeighbors(x, i)) {
                    if (!isValidPoint(x, i)) {
                        throw new OversizeException("The ship must be inside table limits");
                    }

                    if (neighbor.ship != null){
                            throw new AdjacentTileException("The ship must not be adjacent to other ships");
                    }
                }
            }
        }
        else {
            for (int i = x; i < x + length; i++) {
                if (!isValidPoint(i, y)){
                    throw new OversizeException("The ship must be inside table limits");
                }

                Cell cell = getCell(i, y);
                if (cell.ship != null){
                    throw new OverlapTilesException("The ship cannot overlap with another ship");
                }

                for (Cell neighbor : getNeighbors(i, y)) {
                    if (!isValidPoint(i, y)) {
                        throw new OversizeException("The ship must be inside table limits");
                    }

                    if (neighbor.ship != null){
                        throw new AdjacentTileException("The ship must not be adjacent to other ships");
                    }
                }
            }
        }

        return true;
    }

    private boolean isValidPoint(Point2D point){
        return isValidPoint(point.getX(), point.getY());
    }

    private boolean isValidPoint(double x, double y){
        return x >= 0 && x < 10 && y >= 0 && y < 10;
    }

    public class Cell extends Rectangle{
        public int x,y;
        public Ship ship = null;
        public boolean wasShot = false;

        private Board board;
        public Cell(int x, int y, Board board){
            super(30,30);
            this.x=x;
            this.y=y;
            this.board=board;
            Color b = Color.web("rgba(211,211,211,0.8)");
            setFill(b);
            Color c = Color.web("rgba(135,206,250,0.8)");
            setStroke(c);
        }

        public boolean shoot(){
            wasShot=true;
            board.totalshots--;
            etotalshots.set(board.totalshots);
            setFill(Color.DARKBLUE);
           // System.out.println(board);
            shotsfired[countshots][0] = x;
            shotsfired[countshots][1] = y;
            shotsfired[countshots][2] = 0;
            myfcounter++;
            efcounter.set(myfcounter);
            if (ship != null){
                shotsfired[countshots][2]=1;
                /*System.out.println(board + " hit something at x: " + x + " and y: " + y);
                for(int i=0 ; i<=4 ; i++) {
                    System.out.println();
                    for (int j = 0; j <= 3; j++)
                        System.out.print(board.places[i][j]);
                }*/

                if (board.places[0][1] ==  x && board.places[0][3] == 1 && board.places[0][2] <= y && board.places[0][2]+ 4 >= y ){
                    board.points= board.points + 350;
                    states[0][1] = "Injured";
                    System.out.println("Scored 350 points");
                    shotsfired[countshots][3]=5;
                }
                if (board.places[1][1] ==  x && board.places[1][3] == 1 && board.places[1][2] <= y && board.places[1][2]+ 3 >= y ){
                    board.points= board.points + 250;
                    states[1][1] = "Injured";
                    System.out.println("Scored 250 points");
                    shotsfired[countshots][3]=4;

                }
                if (board.places[2][1] ==  x && board.places[2][3] == 1 && board.places[2][2] <= y && board.places[2][2]+ 2 >= y ){
                    board.points= board.points + 100;
                    states[2][1] = "Injured";
                    System.out.println("Scored 100 points");
                    shotsfired[countshots][3]=32;


                }
                if (board.places[3][1] ==  x && board.places[3][3] == 1 && board.places[3][2] <= y && board.places[3][2]+ 2 >= y ){
                    board.points= board.points + 100;
                    states[3][1] = "Injured";
                    System.out.println("Scored 100 points");
                    shotsfired[countshots][3]=31;


                }
                if (board.places[4][1] ==  x && board.places[4][3] == 1 && board.places[4][2] <= y && board.places[4][2]+ 1 >= y ){
                    board.points= board.points + 50;
                    states[4][1] = "Injured";
                    System.out.println("Scored 50 points");
                    shotsfired[countshots][3]=2;

                }
                if (board.places[0][2] ==  y && board.places[0][3] == 2 && board.places[0][1] <= x && board.places[0][1]+ 4 >= x ) {
                    board.points= board.points + 350;
                    states[0][1] = "Injured";
                    System.out.println("Scored 350 points");
                    shotsfired[countshots][3]=5;


                }
                if (board.places[1][2] ==  y && board.places[1][3] == 2 && board.places[1][1] <= x && board.places[1][1]+ 3 >= x ){
                    board.points= board.points + 250;
                    states[1][1] = "Injured";
                    System.out.println("Scored 250 points");
                    shotsfired[countshots][3]=4;


                }
                if (board.places[2][2] ==  y && board.places[2][3] == 2 && board.places[2][1] <= x && board.places[2][1]+ 2 >= x ){
                    board.points= board.points + 100;
                    states[2][1] = "Injured";
                    System.out.println("Scored 100 points");
                    shotsfired[countshots][3]=32;


                }
                if (board.places[3][2] ==  y && board.places[3][3] == 2 && board.places[3][1] <= x && board.places[3][1]+ 2 >= x ){
                    board.points= board.points + 100;
                    states[3][1] = "Injured";
                    System.out.println("Scored 100 points");
                    shotsfired[countshots][3]=31;


                }
                if (board.places[4][2] ==  y && board.places[4][3] == 2 && board.places[4][1] <= x && board.places[4][1]+ 1 >= x ){
                    board.points= board.points + 50;
                    states[4][1] = "Injured";
                    System.out.println("Scored 50 points");
                    shotsfired[countshots][3]=2;


                }
                epoints.set(board.points);
                ship.hit();
                shots++;
                eshots.set(board.shots);
                Color t = Color.web("rgba(236,89,89,0.8)");
                setFill(t);
                setStroke(Color.RED);
                if (!ship.isAlive()){
                    if (board.places[2][1] ==  x && board.places[2][3] == 1 && board.places[2][2] <= y && board.places[2][2]+ 2 >= y ){
                        board.points= board.points + 250;
                        System.out.println("Bonus! Scored 250 points");
                        states[2][1] = "Sunk";

                    }
                    if (board.places[1][1] ==  x && board.places[1][3] == 1 && board.places[1][2] <= y && board.places[1][2]+ 3 >= y ){
                        board.points= board.points + 500;
                        System.out.println("Bonus! Scored 500 points");
                        states[1][1] = "Sunk";


                    }
                    if (board.places[0][1] ==  x && board.places[0][3] == 1 && board.places[0][2] <= y && board.places[0][2]+ 4 >= y ){
                        board.points= board.points + 1000;
                        System.out.println("Bonus! Scored 1000 points");
                        states[0][1] = "Sunk";


                    }

                    if (board.places[2][2] ==  y && board.places[2][3] == 2 && board.places[2][1] <= x && board.places[2][1]+ 2 >= x ){
                        board.points= board.points + 250;
                        System.out.println("Bonus! Scored 250 points");
                        states[2][1] = "Sunk";


                    }
                    if (board.places[1][2] ==  y && board.places[1][3] == 2 && board.places[1][1] <= x && board.places[1][1]+ 3 >= x ){
                        board.points= board.points + 500;
                        System.out.println("Bonus! Scored 500 points");
                        states[1][1] = "Sunk";


                    }
                    if (board.places[0][2] ==  y && board.places[0][3] == 2 && board.places[0][1] <= x && board.places[0][1]+ 4 >= x ){
                        board.points= board.points + 1000;
                        System.out.println("Bonus! Scored 1000 points");
                        states[0][1] = "Sunk";

                    }
                    board.ships--;
                    epoints.set(board.points);
                    eships.set(board.ships);
                }
                countshots++;
                return true;
            }
            countshots++;
            return false;
        }
    }

}
