package fr.utbm.japanesecharacterrecognition.feature.density;

import fr.utbm.japanesecharacterrecognition.feature.Feature;
import fr.utbm.japanesecharacterrecognition.math.VectorDouble;
import fr.utbm.japanesecharacterrecognition.util.RectangleExtraction;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;

import static org.opencv.imgcodecs.Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;



public class Density implements Feature {

    private Double numberOfColumn;
    private Double numberOfLine;
    private VectorDouble densityVector;
    private Mat image;


    /**
     * Default constructors for the density feature
     * Create a number of rows and column to 4 each and a new Vector
     */
    public Density(){
        this.numberOfColumn = 4.0;
        this.numberOfLine = 4.0;
        this.densityVector = new VectorDouble (16);
        this.image = null;
    }

    public Density(Double nbLine, Double nbColumn){
        this.numberOfLine = nbLine;
        this.numberOfColumn = nbColumn;
        this.densityVector = new VectorDouble( (int)(numberOfLine*numberOfColumn));
        this.image = null;
    }

    public VectorDouble getDensityVector() {
        return densityVector;
    }


    public void setImage(String imagePath){
        //Set the image
        image = Imgcodecs.imread(imagePath,CV_LOAD_IMAGE_GRAYSCALE);
        Rect boundingRectangle = RectangleExtraction.extractRectangle(image);
        image = image.submat(boundingRectangle);

        calculFeature(imagePath);

    }


    private void calculFeature(String imgPath){
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
                densityVector.getVector().add(positionInVector, nbOfBlackPixel / ((upperBoundX-lowerBoundX)*(upperBoundY-lowerBoundY)));

                positionInVector++;
            }
        }

    }
}
