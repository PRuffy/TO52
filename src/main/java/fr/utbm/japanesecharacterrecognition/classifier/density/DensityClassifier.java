package fr.utbm.japanesecharacterrecognition.classifier.density;

import fr.utbm.japanesecharacterrecognition.comparator.density.DensityComparator;
import fr.utbm.japanesecharacterrecognition.comparator.density.DensityResultPair;

import java.util.ArrayList;
import java.util.HashMap;

public class DensityClassifier {


    private final String[] LIST_CLASSE = {"A","I","U","E","O",
            "KA","KI","KU","KE","KO",
            "SA","SHI","SU","SE","SO",
            "TA","CHI","TSU","TE","TO",
            "NA","NI","NU","NE","NO",
            "HA","HI","FU","HE","HO",
            "RA","RI","RU","RE","RO",
            "MA","MI","MU","ME","MO",
            "WA","YA","YU","YO","WO","N"};

    private static final String TRAINING_IMAGE_PATH ="D:/japanesecharacterrecognition/src/main/resources/fr/utbm/japanesecharacterrecognition/hiragana/training/";
    private static final String TEST_IMAGE_PATH ="D:/japanesecharacterrecognition/src/main/resources/fr/utbm/japanesecharacterrecognition/hiragana/test/";
    private static final Integer NUMBER_OF_TEST_IMAGE = 300;
    private static final Double NUMBER_LINE = 9.0;
    private static final Double NUMBER_COLUMN = 8.0;

    private DensityComparator densityComparator;

    private HashMap<String,Integer> resultPerClass;
    public DensityClassifier(){
        System.out.println("*************************************");
        System.out.println("*** INITIALIZE DENSITY CLASSIFIER ***");
        System.out.println("*************************************");

        densityComparator = new DensityComparator();
        resultPerClass = new HashMap<String, Integer>();
    }

    public void learn(){
        System.out.println("**********************");
        System.out.println("*** START LEARNING ***");
        System.out.println("**********************");
        densityComparator.learnDensity(TRAINING_IMAGE_PATH,400,NUMBER_LINE,NUMBER_COLUMN);
        System.out.println("***********************");
        System.out.println("*** FINISH LEARNING ***");
        System.out.println("***********************");
    }

    public void test(){

        System.out.println("*********************");
        System.out.println("*** START TESTING ***");
        System.out.println("*********************");

        //Creating the first loop for the class to test :
        for(String str : LIST_CLASSE){
            //Creating the loop for extracting the image :
            Integer succesfulGuess = 0;
            for(int count = 1 ; count <= NUMBER_OF_TEST_IMAGE; count++){
                String imagePath = TEST_IMAGE_PATH+str+" ("+count+").png";


                if(checkResult(str, densityComparator.compareDensity(imagePath)))
                    succesfulGuess+=1;

            }

            resultPerClass.put(str,succesfulGuess);
        }

    }

    private boolean checkResult(String expectedResult,ArrayList<DensityResultPair> densityResultPairs){
        HashMap<String, Integer> resultOccurence = new HashMap<String, Integer>();

        for(DensityResultPair drp :densityResultPairs){
            String key = drp.getCategorie();
            if(resultOccurence.containsKey(key))
                resultOccurence.put(key,resultOccurence.get(key)+1);
            else
                resultOccurence.put(key,1);
        }


        boolean result = false;
        String mostRecurentKey = "";
        Integer mostRecurentInt = 0;

        for(HashMap.Entry<String,Integer> entry : resultOccurence.entrySet()){
            if(entry.getValue() > mostRecurentInt){
                mostRecurentInt = entry.getValue();
                mostRecurentKey = entry.getKey();
            }
        }

        if(mostRecurentKey.equals(expectedResult))
            result = true;

        return result;
    }

    public void showSuccessInConsole(){
        for(String str : LIST_CLASSE)
            System.out.println("Class : " + str + " Correct anwser (out of 300 test) :" + resultPerClass.get(str));

    }

}
