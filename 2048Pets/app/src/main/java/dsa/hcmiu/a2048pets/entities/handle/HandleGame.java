package dsa.hcmiu.a2048pets.entities.handle;

import android.content.res.TypedArray;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import dsa.hcmiu.a2048pets.MyApplication;
import dsa.hcmiu.a2048pets.R;
import dsa.hcmiu.a2048pets.entities.model.Board;
import dsa.hcmiu.a2048pets.entities.model.Pets;

import static dsa.hcmiu.a2048pets.entities.model.Board.max;
import static dsa.hcmiu.a2048pets.entities.model.Features.user;


/**
 * Created by Admin on 3/25/2018.
 */

public class HandleGame { //singleton

    private static int maxValue = 8192;
    private static int numCount = 13;
    public static final Pets[] typePet = new Pets[numCount + 1];
    public static final int[] arrId = new int[maxValue + 1];

    private static HandleGame instance;
    private int countEmpty = 0;
    public static Board curBoard = new Board(0);
    private Stack<Board> boardStack;
    int row;
    int col;
    int countMove;


    private Random random = new Random();

    public static HandleGame getInstance() {
        if (instance == null) {
            instance = new HandleGame();
        }
        return instance;
    }

    private HandleGame() {
        setTheme(0);
        init();
    }

    private void init() {
        curBoard.setNewBoard();
        boardStack = new Stack<>();
        int pos1 = random.nextInt(max*max - 1);
        int pos2 = random.nextInt(max*max - 1);
        while (pos1 == pos2) {
            pos2 = random.nextInt(max*max - 1);
        }
        curBoard.setElement(pos1, 2);
        curBoard.setElement(pos2, (random.nextInt(1) + 1) * 2);
        countEmpty=max*max - 1;
    }

    public void newGame() {
        user.totalGold +=curBoard.getScoreBoard();
        if (user.highScore <curBoard.getScoreBoard()) {
            user.highScore = curBoard.getScoreBoard();
            user.UserHighScore = user.getName();
        }
        init();
        log("New game");
    }

    public void setTheme(int choice) {
        //get resources
        int resid;
        if (choice == 0) resid= R.array.arrImage1;
        else resid = R.array.arrImage2;
        TypedArray images = MyApplication.getContext().getResources().obtainTypedArray(resid);
        int countNo = 2;
        typePet[0] = new Pets(0);
        typePet[0].setId(0);
        typePet[0].setPic(images.getResourceId(0, -1));
        arrId[0] = 0;
        for (int i = 1; i < 14; i++) {
            typePet[i] = new Pets(countNo);
            typePet[i].setId(i);
            typePet[i].setPic(images.getResourceId(i, -1));
            arrId[countNo] = i;
            countNo *= 2;
        }
    }

    public void saveHis() {
        Board boardTemp = new Board(curBoard);
        boardStack.push(boardTemp);
    }

    public Board undoSaveHis() {
        return boardStack.pop();
    }

    public void addRandomNumber() {
        int addPosition;
        if (countEmpty > 0) {
            if (countEmpty==1){
                addPosition=0;
            }
            else addPosition = random.nextInt(countEmpty-1);
            int count0 = 0;
            for (int i = 0; i < Board.max * Board.max - 1; i++) {
                if (curBoard.getEValue(i)==0) {
                    if (count0 == addPosition) {
                        curBoard.setElement(i, (random.nextInt(1) + 1) * 2);
                        return;
                    }
                    count0++;
                }
            }
        }
        countEmpty--;
    }

    public boolean hammer() {
        if (user.hammer ==0 || countEmpty > max*max - 2) return false;
        log(String.valueOf(countEmpty));
        int del = random.nextInt(max*max-countEmpty)+1;
        for (int i = 0; i<max*max; i++) {
            if(curBoard.getEValue(i)>0) {
                if (del == 0) {
                    curBoard.setElement(i, 0);
                    break;
                }
                else del--;
            }
        }
        user.hammer--;
        countEmpty++;
        return true;
    }

    public boolean Undo() {
        int u = user.undo;
        if (u == 0||boardStack.isEmpty()) return false;
        curBoard.setBoard(undoSaveHis());
        user.undo=--u;
        log("undo count" + String.valueOf(u));
        return true;
    }

    public void pushUp() {
        countEmpty = 0;
        for (col = 0; col < Board.max; col++) {
            int count0 = 0;
            for (row = 0; row < Board.max; row++) {
                if (curBoard.getEValue(row, col) == 0) count0++;
                else if (count0!=0) {
                    int t = curBoard.getEValue(row, col);
                    curBoard.setElement(row - count0, col, t);
                    curBoard.setElement(row, col, 0);
                    countMove++;
                }
            }
            countEmpty += count0;
        }
    }

    public void moveUp() {
        countMove = 0;
        saveHis();
        pushUp();
        for (col = 0; col < Board.max; col++) {
            for (row = 1; row < Board.max; row++) {
                int t = curBoard.getEValue(row, col);
                if (t == curBoard.getEValue(row - 1, col) && t!=0) {
                    curBoard.setElement(row - 1, col, t * 2);
                    curBoard.setElement(row, col, 0);
                    long score = curBoard.getScoreBoard() + curBoard.getEValue(row - 1, col);
                    curBoard.setScoreBoard(score);
                    countMove++;
                }
            }
        }
        if (countMove > 0) {
            countMove = 0;
            pushUp();
            addRandomNumber();
        } else {
            undoSaveHis();
        }
    }

    public void pushDown() { //row
        countEmpty = 0;
        for (col = 0; col < Board.max; col++) {
            int count0 = 0;
            for (row = Board.max - 1; row >= 0; row--) {
                if (curBoard.getEValue(row, col) == 0) count0++;
                else if (count0!=0) {
                    int t = curBoard.getEValue(row, col);
                    curBoard.setElement(row + count0, col, t);
                    curBoard.setElement(row, col, 0);
                    countMove++;
                }
            }
            countEmpty += count0;
        }
    }

    public void moveDown() { //0-3
        countMove = 0;
        saveHis();
        pushDown();
        for (col = 0; col < Board.max; col++) {
            for (row = Board.max - 2; row >= 0; row--) {
                int t = curBoard.getEValue(row, col);
                if (t>0 && t == curBoard.getEValue(row + 1, col)) {
                    curBoard.setElement(row + 1, col, t * 2);
                    curBoard.setElement(row, col, 0);
                    long score = curBoard.getScoreBoard() + curBoard.getEValue(row + 1, col);
                    curBoard.setScoreBoard(score);
                    countMove++;
                }
            }
        }
        if (countMove > 0) {
            countMove = 0;
            pushDown();
            addRandomNumber();
        } else undoSaveHis();
    }

    public void pushRight() {
        countEmpty = 0;
        for (row = 0; row < Board.max; row++) {
            int count0 = 0;
            for (col = Board.max - 1; col >= 0; col--) {
                if (curBoard.getEValue(row, col) == 0) count0++;
                else if (count0!=0) {
                    int t = curBoard.getEValue(row, col);
                    curBoard.setElement(row, col + count0, t);
                    curBoard.setElement(row, col, 0);
                    countMove++;
                }
            }
            countEmpty += count0;
        }
    }

    public void moveRight() { //0-3
        countMove = 0;
        saveHis();
        pushRight();
        for (row = 0; row < Board.max; row++) {
            for (col = Board.max - 2; col >= 0; col--) {
                int t = curBoard.getEValue(row, col);
                if (t>0 && t == curBoard.getEValue(row, col + 1)) {
                    curBoard.setElement(row, col + 1, t * 2);
                    curBoard.setElement(row, col, 0);
                    long score = curBoard.getScoreBoard() + curBoard.getEValue(row, col + 1);
                    curBoard.setScoreBoard(score);
                    countMove++;
                }
            }
        }
        if (countMove > 0) {
            countMove = 0;
            pushRight();
            addRandomNumber();
        } else undoSaveHis();
    }

    public void pushLeft() {
        countEmpty = 0;
        for (row = 0; row < Board.max; row++) {
            int count0 = 0;
            for (col = 0; col < Board.max; col++) {
                if (curBoard.getEValue(row, col) == 0) count0++;
                else if (count0!=0) {
                    int t = curBoard.getEValue(row, col);
                    curBoard.setElement(row, col - count0, t);
                    curBoard.setElement(row, col, 0);
                    countMove++;
                }
            }
            countEmpty += count0;
        }
    }

    public void moveLeft() { //3-0
        countMove = 0;
        saveHis();
        pushLeft();
        for (row = 0; row < Board.max; row++) {
            for (col = 1; col < Board.max; col++) {
                int t = curBoard.getEValue(row, col);
                if (t>0 && t == curBoard.getEValue(row, col - 1)) {
                    curBoard.setElement(row, col - 1, t * 2);
                    curBoard.setElement(row, col, 0);
                    long score = curBoard.getScoreBoard() + curBoard.getEValue(row, col - 1);
                    curBoard.setScoreBoard(score);
                    countMove++;
                }
            }
        }
        if (countMove > 0) {
            countMove = 0;
            pushLeft();
            addRandomNumber();
        } else undoSaveHis();
    }

    public boolean gameOver() {
        if(countEmpty==0) return curBoard.fullBoard();
        return false;
    }

    private void log(String msg) {
        Log.d("HANDLE GAME",msg);
    }

}
