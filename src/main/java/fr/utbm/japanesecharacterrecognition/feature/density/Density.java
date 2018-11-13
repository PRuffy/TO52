package fr.utbm.japanesecharacterrecognition.feature.density;

import fr.utbm.japanesecharacterrecognition.feature.Feature;
import fr.utbm.japanesecharacterrecognition.math.VectorDouble;
import fr.utbm.japanesecharacterrecognition.util.RectangleExtraction;
import org.opencv.core.Core;
import org.opencv.core.Mat;
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
        image = RectangleExtraction.extractRectangle(Imgcodecs.imread(imagePath,CV_LOAD_IMAGE_GRAYSCALE));
        calculFeature();

    }


    private void calculFeature(){
        //Create the step for defining the zone of interest.
        Double verticalStep =  image.rows() / numberOfLine;
        Double horizontalStep =  image.cols() / numberOfColumn;
        int positionInVector = 0;

        for (Double row = 0.0; row < image.rows(); row+=verticalStep){
            int baseRow = (int) Math.ceil(row);
            int endRow = (int) Math.ceil(row+verticalStep);

            for (Double column = 0.0; column < image.cols(); column+=horizontalStep){
                int baseColumn = (int) Math.ceil(column);
                int endColumn = (int) Math.ceil(column+horizontalStep);

                Mat subImage =  image.submat(baseRow, endRow, baseColumn,endColumn);
                Double nbOfBlackPixel =  (double) ((endRow-baseRow) * (endColumn-baseColumn)) - Core.countNonZero(subImage);
                densityVector.getVector().add(positionInVector, nbOfBlackPixel / ((endRow-baseRow) * (endColumn-baseColumn)));

                positionInVector++;


            }
        }
    }
}
