package fr.utbm.japanesecharacterrecognition.util;

import org.opencv.core.Core;
import org.opencv.core.Mat;

public class RectangleExtraction {

    public static Mat extractRectangle(Mat img){

        int firstRow = 0;
        boolean firstRowSet = false;
        int lastRow = 0;
        int numberOfRows = img.rows();



        for(int row = 0; row < numberOfRows; row++){
           if(Core.countNonZero(img.row(row))< numberOfRows){
               if(!firstRowSet){
                   firstRowSet = true;
                   firstRow = row;
               }else{
                   lastRow = row;
               }

           }
        }


        int firstCol = 0;
        boolean firstColSet = false;
        int lastCol = 0;
        int numberOfCols = img.cols();

        for(int col = 0; col < numberOfCols; col++){
            int x = Core.countNonZero(img.col(col))+1;
            if(x<numberOfCols){
                if(!firstColSet){
                    firstColSet = true;
                    firstCol = col;
                }else{
                    lastCol = col;
                }

            }
        }
        return img.submat(firstRow,lastRow,firstCol,lastCol);
    }
}
