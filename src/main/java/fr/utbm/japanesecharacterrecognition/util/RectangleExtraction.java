package fr.utbm.japanesecharacterrecognition.util;

import org.opencv.core.Core;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

public class RectangleExtraction {

    public static Rect extractRectangle(Mat img){

        //Value for the rectangle
        int x = -1 ;
        int y = -1;
        int height = 0; //Vertical
        int width = 0; //Horizontal

        //Information about the image
        int rows = (int) img.size().height;
        int cols = (int) img.size().width;

        int currentRow = 0;
        //Search for the top y value with black pixel
        while (y == -1 && currentRow < rows-1){
            if(Core.countNonZero(img.row(currentRow))<cols)
                y = currentRow;
            else
                currentRow++;
        }

        //Search for the height
        while (currentRow < rows-1){
            currentRow++;
            if(Core.countNonZero(img.row(currentRow))<cols)
                height = currentRow-y;
        }


        int currentCol = 0;
        //Search for the x
        while(x==-1 && currentCol < cols){
            if(Core.countNonZero(img.col(currentCol))<rows)
                x = currentCol;
            else
                currentCol++;
        }

        while (currentCol < cols-1){
            currentCol++;
            if(Core.countNonZero(img.col(currentCol))<rows)
                width = currentCol-x;
        }


        return new Rect(x,y,width,height);
    }
}
