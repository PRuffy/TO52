package fr.utbm.japanesecharacterrecognition;


import fr.utbm.japanesecharacterrecognition.classifier.density.DensityClassifier;
import fr.utbm.japanesecharacterrecognition.util.RectangleExtraction;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import static org.opencv.imgcodecs.Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;


public class Application {

    static{
        nu.pattern.OpenCV.loadShared();
    }

    public static void main(String[] args){
        System.out.println("START OF PROGRAM");

        DensityClassifier densityClassifier = new DensityClassifier();

        densityClassifier.learn();
        densityClassifier.test();
        densityClassifier.showSuccessInConsole();


        //Mat img = RectangleExtraction.extractRectangle(Imgcodecs.imread("D:/japanesecharacterrecognition/src/main/resources/fr/utbm/japanesecharacterrecognition/hiragana/training/A (2).png",CV_LOAD_IMAGE_GRAYSCALE));

        //Imgcodecs.imwrite("D:/imgTest.png",img);
        System.out.println("END OF PROGRAM");
    }
}
