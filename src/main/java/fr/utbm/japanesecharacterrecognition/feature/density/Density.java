package fr.utbm.japanesecharacterrecognition.feature.density;

import fr.utbm.japanesecharacterrecognition.feature.Feature;
import fr.utbm.japanesecharacterrecognition.util.RectangleExtraction;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.Vector;

import static org.opencv.imgcodecs.Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;



public class Density implements Feature {

    private Double numberOfColumn;
    private Double numberOfLine;
    private Vector<Double> densityVector;
    private Mat image;


    /**
     * Default constructors for the density feature
     * Create a number of rows and column to 4 each and a new Vector
     */
    public Density(){
        this.numberOfColumn = 4.0;
        this.numberOfLine = 4.0;
        this.densityVector = new Vector<Double> (16);
        this.image = null;
    }

    public Density(Double nbLine, Double nbColumn){
        this.numberOfLine = nbLine;
        this.numberOfColumn = nbColumn;
        this.densityVector = new Vector<Double>( (int)(numberOfLine*numberOfColumn));
        this.image = null;
    }

    public Vector<Double> getDensityVector() {
        return densityVector;
    }


    public void setImage(String imagePath){
        setImage(Imgcodecs.imread(imagePath,CV_LOAD_IMAGE_GRAYSCALE));
    }


    public void setImage(Mat img){
        Rect boundingRectangle = RectangleExtraction.extractRectangle(img);
        image = img.submat(boundingRectangle);
        calculFeature();
    }

    private void calculFeature(){
        //Create the step for defining the zone of interest.
        Double height = image.size().height;
        Double width = image.size().width;
        Double verticalStep =  (height-1) / numberOfLine;
        Double horizontalStep =  (width-1) / numberOfColumn;

        int positionInVector = 0;

        //Line
        for(Double currentY = 0.0; currentY+verticalStep < height; currentY+=verticalStep){
            int lowerBoundY = (int) Math.ceil(currentY);
            int upperBoundY = (int) Math.ceil(currentY+verticalStep);

            //Column
            for(Double currentX = 0.0; currentX+horizontalStep < width; currentX+=horizontalStep){
                int lowerBoundX = (int) Math.ceil(currentX);
                int upperBoundX = (int) Math.ceil(currentX+horizontalStep);

                Mat subImg = image.submat(lowerBoundY,upperBoundY,lowerBoundX,upperBoundX);
                Double nbOfBlackPixel = (double) ((upperBoundX-lowerBoundX)*(upperBoundY-lowerBoundY)) - Core.countNonZero(subImg);
                densityVector.add(positionInVector, nbOfBlackPixel / ((upperBoundX-lowerBoundX)*(upperBoundY-lowerBoundY)));

                positionInVector++;
            }
        }

        if(positionInVector != numberOfColumn*numberOfLine){
            System.err.println(positionInVector);
        }

    }




    public Vector<Double> getVector(){
        return densityVector;
    }


    public double[] toArray() {
        double[] myArray = new double[densityVector.size()];

        for(int i = 0 ; i < densityVector.size(); i++)
            myArray[i] = densityVector.get(i);


        return myArray;
    }


}
