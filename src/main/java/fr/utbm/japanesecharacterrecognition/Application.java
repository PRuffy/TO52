package fr.utbm.japanesecharacterrecognition;


import fr.utbm.japanesecharacterrecognition.classifier.density.DensityClassifier;

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

        System.out.println("END OF PROGRAM");
    }
}
