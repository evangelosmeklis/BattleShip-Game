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
    public boolean outside = false;
    IntegerProperty eships = new SimpleIntegerProperty(ships);
    IntegerProperty eshots = new SimpleIntegerProperty(shots);
    IntegerProperty etotalshots = new SimpleIntegerProperty(totalshots);
    IntegerProperty epoints = new SimpleIntegerProperty(points);

    public int[][] places = new int[5][4];



    public Board(boolean enemy, EventHandler<? super MouseEvent> handler){
        this.enemy=enemy;
        for (int y = 0; y < 10; y++) {
            HBox row = new HBox();
            for (int x = 0; x < 10; x++) {
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
            places[times][1]= y;
            places[times][2]= x;
            if (ship.vertical) places[times][3]= 2;
            else places[times][3]=1;
            times++;

            if (ship.vertical) {
                for (int i = y; i < y + length; i++) {
                    Cell cell = getCell(x, i);
                    cell.ship = ship;
                    if (!enemy) {
                        cell.setFill(Color.WHITE);
                        cell.setStroke(Color.GREEN);
                    }
                }
            }
            else {
                for (int i = x; i < x + length; i++) {
                    Cell cell = getCell(i, y);
                    cell.ship = ship;
                    if (!enemy) {
                        cell.setFill(Color.WHITE);
                        cell.setStroke(Color.GREEN);
                    }
                }
            }

            return true;
        }

        return false;
    }

    public Cell getCell(int x, int y){
        return (Cell)((HBox)rows.getChildren().get(y)).getChildren().get(x);
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

        if (ship.vertical) {
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
            setFill(Color.LIGHTGRAY);
            setStroke(Color.BLACK);
        }

        public boolean shoot(){
            wasShot=true;
            totalshots--;
            etotalshots.set(board.totalshots);
            setFill(Color.BLACK);
            if (ship != null){
                if (places[0][1] ==  y && places[0][3] == 1 && places[0][2] <= x && places[0][2]+ 1 >= x ) points= points + 350;
                if (places[1][1] ==  y && places[1][3] == 1 && places[1][2] <= x && places[1][2]+ 2 >= x ) points= points + 250;
                if (places[2][1] ==  y && places[2][3] == 1 && places[2][2] <= x && places[2][2]+ 2 >= x ) points= points + 100;
                if (places[3][1] ==  y && places[3][3] == 1 && places[3][2] <= x && places[3][2]+ 3 >= x ) points= points + 100;
                if (places[4][1] ==  y && places[0][3] == 1 && places[4][2] <= x && places[4][2]+ 4 >= x ) points= points + 50;
                if (places[0][1] ==  x && places[0][3] == 2 && places[0][2] <= y && places[0][2]+ 1 >= y ) points= points + 350;
                if (places[1][1] ==  x && places[1][3] == 2 && places[1][2] <= y && places[1][2]+ 2 >= y ) points= points + 250;
                if (places[2][1] ==  x && places[2][3] == 2 && places[2][2] <= y && places[2][2]+ 2 >= y ) points= points + 100;
                if (places[3][1] ==  x && places[3][3] == 2 && places[3][2] <= y && places[3][2]+ 3 >= y ) points= points + 100;
                if (places[4][1] ==  x && places[0][3] == 2 && places[4][2] <= y && places[4][2]+ 4 >= y ) points= points +  50;
                epoints.set(board.points);
                for(int i=0 ; i<=4 ; i++) {
                    System.out.println();
                    for (int j = 0; j <= 3; j++)
                        System.out.print(places[i][j]);
                }
                ship.hit();
                shots++;
                eshots.set(board.shots);
                setFill(Color.RED);
                if (!ship.isAlive()){
                    board.ships--;
                    eships.set(board.ships);
                }
                return true;
            }
            return false;
        }
    }

}
