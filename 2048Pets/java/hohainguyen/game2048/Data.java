package hohainguyen.game2048;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by User on 3/26/2018.
 */

public class Data {
    private static Data data;
    private ArrayList<Integer> arr = new ArrayList<>();
    private  int [][] arr2= new int[4][4];
    private int [] colorList;
    private  Random r = new Random();

    static {
        data = new Data();
    }

    public static Data getData(){
        return  data;
    }
    public void  intt(Context context){
        for(int i=0; i< 4 ;  i++){
            for(int j=0; j<4; j++){
                arr2[i][j]=0;
                arr.add(0);
            }
        }

        TypedArray typedArray= context.getResources().obtainTypedArray(R.array.numColor);
        colorList = new int[typedArray.length()];
        for(int i=0; i <typedArray.length(); i++){
            colorList[i]= typedArray.getColor(i,0);
        }
        typedArray.recycle();
        makeNum();
        transfer();
    }

    public ArrayList<Integer> getArr() {
        return arr;
    }

    public int colorr(int num){
        if (num==0)
            return Color.WHITE;
        else{
            int a = (int) (Math.log(num)/Math.log(2));
            return  colorList[a-1];
        }
    }

    public void makeNum() {
        int num0 = 0;
        for (int i = 0; i < 16; i++) {
            if (arr.get(i) == 0) {
                num0++;
            }
        }
        int newNum;
        if (num0 > 1)
            newNum = r.nextInt(2) + 1;
        else if (num0 == 1)
            newNum = 1;
        else
            newNum = 0;


        while(newNum!=0){
            int i = r.nextInt(4),j = r.nextInt(4);
            if(arr2[i][j]==0){
                arr2[i][j]=r.nextInt(8)+1;
                if(arr2[i][j]%2!=0||arr2[i][j]==6)
                    arr2[i][j]=2;
                newNum--;
            }
        }
    }
    public void transfer(){
        arr.clear();
        for(int i=0; i< 4 ;  i++){
            for(int j=0; j<4; j++){
                arr.add(arr2[i][j]);
            }
        }
    }

    public void onSwipeLeft(){
        //Plus number
        for(int i=0;i<4;i++)
            for(int j = 0;j<4;j++) {
                int num = arr2[i][j];
                if (num == 0)
                    continue;
                else{
                    for(int k = j+1;k<4;k++) {
                        int numx = arr2[i][k];

                        if (numx == 0)
                            continue;
                        else {
                            if (numx == num) {
                                arr2[i][j] = num * 2;
                                arr2[i][k] = 0;
                                break;
                            }
                            else
                                break;
                        }

                    }
                }
            }
        //No plus
        for(int i=0;i<4;i++)
            for(int j=0;j<4;j++){
                int num = arr2[i][j];
                if(num==0){
                    for (int k= j+1;k<4;k++) {
                        int num1 = arr2[i][k];
                        if (num1==0)
                            continue;
                        else{
                            arr2[i][j]=arr2[i][k];
                            arr2[i][k]=0;
                            break;
                        }
                    }
                }
            }
        makeNum();
        transfer();
    }
    public void onSwipeRight(){
        //plus number
        for(int i=3;i>=0;i--)
            for(int j = 3;j>=0;j--) {
                int num = arr2[i][j];
                if (num == 0)
                    continue;
                else{
                    for(int k = j-1;k>=0;k--) {
                        int numx = arr2[i][k];

                        if (numx == 0)
                            continue;
                        else {
                            if (numx == num) {
                                arr2[i][j] = num * 2;
                                arr2[i][k] = 0;
                                break;
                            }
                            else
                                break;
                        }

                    }
                }
            }

        //no plus
        for(int i=3;i>=0;i--)
            for(int j=3;j>=0;j--){
                int num = arr2[i][j];
                if(num==0){
                    for (int k= j-1;k>=0;k--) {
                        int num1 = arr2[i][k];
                        if (num1==0)
                            continue;
                        else{
                            arr2[i][j]=arr2[i][k];
                            arr2[i][k]=0;
                            break;
                        }
                    }
                }
            }
        makeNum();
        transfer();
    }

    public void onSwipeUp(){
        //plus
        for(int j=0; j<4;j++) {
            for (int i = 0; i < 4; i++) {
                int num = arr2[i][j];
                if (num == 0)
                    continue;
                else {
                    for (int k = i + 1; k < 4; k++) {
                        int num1 = arr2[k][j];
                        if (num1 == 0)
                            continue;
                        else {
                            if (num1 == num) {
                                arr2[i][j] *= 2;
                                arr2[i][k] = 0;
                                break;
                            } else {
                                break;
                            }

                        }
                    }
                }
            }
        }
        //No plus
        for(int j=0; j<4;j++) {
            for (int i = 0; i < 4; i++) {
                int num = arr2[i][j];
                if (num == 0){
                    for (int k = i + 1; k < 4; k++){
                        int num1= arr2[i][k];
                        if(num1==0)
                            continue;
                        else{
                            arr2[i][j]= arr2[i][k];
                            arr2[i][k]=0;
                            break;
                        }
                    }
                }

            }
        }
        makeNum();
        transfer();
    }
    public void onSwipeDown(){
        //plus
        for(int j=3; j>=3;j++) {
            for (int i = 3; i >=0; i++) {
                int num = arr2[i][j];
                if (num == 0)
                    continue;
                else {
                    for (int k = i + 1; k < 4; k++) {
                        int num1 = arr2[k][j];
                        if (num1 == 0)
                            continue;
                        else {
                            if (num1 == num) {
                                arr2[i][j] *= 2;
                                arr2[i][k] = 0;
                                break;
                            } else {
                                break;
                            }

                        }
                    }
                }
            }
        }
        //No plus
        for(int j=3; j>=0;j--) {
            for (int i = 3; i >=0; i--) {
                int num = arr2[i][j];
                if (num == 0){
                    for (int k = i + 1; k < 4; k++){
                        int num1= arr2[i][k];
                        if(num1==0)
                            continue;
                        else{
                            arr2[i][j]= arr2[i][k];
                            arr2[i][k]=0;
                            break;
                        }
                    }
                }

            }
        }
        makeNum();
        transfer();
    }
}
