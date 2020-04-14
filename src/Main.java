import java.awt.MouseInfo;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.io.File;

public class Main{

    public static void main(String []args){

           File outputFile = new File("output.txt");
            if (!outputFile.exists()){
                try{
                    outputFile.createNewFile();
                }catch (IOException e){e.printStackTrace();}
            }

            try{
                PrintWriter pw = new PrintWriter(outputFile);
                int HowManyGenerate = 1000;
                while (HowManyGenerate > 0){
                    int mouseX = 1, mouseY = 1;
                    int x = mouseX , y = mouseY ;
                    int counter = 0;
                    int [][]image = new int[65][65];
                    boolean flag = true;
                    while (flag) {
                        mouseX = (int)MouseInfo.getPointerInfo().getLocation().getX();
                        mouseY = (int) MouseInfo.getPointerInfo().getLocation().getY();

                        if ((mouseX/30 != x || mouseY/16 != y) && mouseY <= 1024 && mouseX <= 1920 ) {
//                            System.out.println("X = " + mouseX/30 + "  ||  Y = " + mouseY/16);
                            if(image [(int)mouseY/16][(int)mouseX/30] != 5)
                                counter++;
                            image [(int)mouseY/16][(int)mouseX/30] = 5;
                            if (counter > 256) flag = false;
                            x = mouseX/30;
                            y = mouseY/16;
                        }
                    }
//
                    int[][] new64x64 = arrayProcessing(image);
                    int [][] new2x4 = generate8BytesIntArray(new64x64);

                    int abc = get8BinaryNumber(new2x4);
                    System.out.println("binary => " + Integer.toBinaryString(abc) + "   ||  decimal => " + abc );
                    pw.println(abc);
                    HowManyGenerate--;
                }

            pw.close();
            }catch (IOException e){e.printStackTrace();}
    }

    static public int[][] generate256BytesIntArray (int [][]new64x64) {
        //tablice 16x16
        // gdy liczba 5 jest nieparzysta to 1 a jak jest albo nie ma nic to 0
        int [][] new16x16 = new int [16][16];
        int xOperationNumber = 0;
        int yOperationNumber = 0;
        int fivesCounter = 0;

        while(yOperationNumber < 16){
            while(xOperationNumber < 16){
                fivesCounter = 0;
                for(int i  = xOperationNumber*4; i < 4*xOperationNumber+4 ; i++){
                    for (int j = yOperationNumber*4 ; j < 4*yOperationNumber+4 ; j++){
                        if(new64x64[i][j] == 1){
                            fivesCounter++;
                        }
                    }
                }
                if(fivesCounter%2 == 1)
                    new16x16[xOperationNumber][yOperationNumber] = 1;
                else
                    new16x16[xOperationNumber][yOperationNumber] = 0;
                xOperationNumber++;
            }
            xOperationNumber = 0;
            yOperationNumber++;
        }
        return new16x16;
    }

    static public int[][] generate8BytesIntArray (int [][]new64x64) {
        //tablice 16x16
        // gdy liczba 5 jest nieparzysta to 1 a jak jest albo nie ma nic to 0
        int [][] new2x4 = new int [2][4];
        int xOperationNumber = 0;
        int yOperationNumber = 0;
        int fivesCounter = 0;

        while(yOperationNumber < 4){
            while(xOperationNumber < 2){
                fivesCounter = 0;
                for(int i  = yOperationNumber*16; i < 16*yOperationNumber+16 ; i++){
                    for (int j = 32*xOperationNumber ; j < 32*xOperationNumber+32 ; j++){
                        if(new64x64[j][i] == 1){
                            fivesCounter++;
                        }
                    }
                }
                if(fivesCounter%2 == 1)
                    new2x4[xOperationNumber][yOperationNumber] = 1;
                else
                    new2x4[xOperationNumber][yOperationNumber] = 0;
                xOperationNumber++;
            }
            xOperationNumber = 0;
            yOperationNumber++;
        }
        return new2x4;
    }

    static public int[][] arrayProcessing( int [][]image){
        int[][] new64x64 = new int [64][64];
        for (int[] row : new64x64)
            Arrays.fill(row, 0);
        int newX, newY;
        int N = 64, K=5000;
        for(int i = 0 ; i < 64 ; i++)
            for (int j = 0 ; j < 64; j++){
                if(image[i][j] == 5){
                    newX = (i +j)%N;
                    newY = (int) (j + K * Math.sin((N/2.0)*Math.PI)%N);
                    new64x64[newX][newY] = 1;
                }
            }

//        for(int i = 0 ; i < 64 ; i++){
//            for (int j = 0 ; j < 64; j++){
//                System.out.print(new64x64[i][j] + " ");
//            }
//            System.out.println();
//        }
        return new64x64;
    }

    static public void showNew2x4arrayAnd8BytesInt(int [][]array){
        //wypisanie tablicy
        System.out.println();
        for (int i = 0 ; i < 2 ; i++){
            for(int j = 0 ; j < 4 ; j++){
                System.out.print(array[i][j]);
            }
            System.out.println();
        }

        //wypisanie liczby 8 bitowej
        System.out.println();
        for(int i = 0 ; i < 4 ; i++){
            for(int j = 0 ; j < 2 ; j++){
                System.out.print(array[j][i]);
            }
        }
    }

    static public void  showNew16x16arrayAnd256BytesInt (int [][]array){
        // wypisanie new16x16
        System.out.println();
        for (int i = 0 ; i < 16 ; i++){
            for(int j = 0 ; j < 16 ; j++){
                System.out.print(array[i][j]+" ");
            }
            System.out.println();
        }
        //wypisanie liczby 256 bitowej
        System.out.println();
        for(int i = 0 ; i < 16 ; i++){
            for(int j = 0 ; j < 16 ; j++){
                System.out.print(array[j][i]);
            }
        }
    }

    static public int get8BinaryNumber(int [][]array) {
        int decimalNumber = 0;
        int binaryNumber = 0b0;
        int [][]a = {{128,32,8,2},{64,16,4,1}};
        for(int i = 0 ; i < 4 ; i++){
            for(int j = 0 ; j < 2 ; j++){
                if(array[j][i] == 1)
                    decimalNumber += a[j][i];
                binaryNumber = array[j][i];
                binaryNumber = binaryNumber >> 1;
            }
        }
        return decimalNumber;
    }

}