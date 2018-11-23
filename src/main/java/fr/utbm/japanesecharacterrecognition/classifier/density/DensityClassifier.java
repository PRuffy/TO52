package fr.utbm.japanesecharacterrecognition.classifier.density;

import fr.utbm.japanesecharacterrecognition.feature.density.Density;
import fr.utbm.japanesecharacterrecognition.math.VectorUtils;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import static org.opencv.imgcodecs.Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;

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
    private static final Integer NUMBER_OF_TRAINING_IMAGE = 400;
    private static final Integer NUMBER_OF_TEST_IMAGE = 300;
    private static final Double NUMBER_LINE = 9.0;
    private static final Double NUMBER_COLUMN = 8.0;
    private static final Integer NUMBER_OF_NEIGHBOUR = 10;

    private HashMap<String, ArrayList<Density>> densityMap;
    private HashMap<String, RealMatrix> covarianceMap;
    private HashMap<String, Vector<Double>> meanVectorMap;

    private HashMap<String,Integer> resultPerClass;

    public DensityClassifier(){
        System.out.println("*************************************");
        System.out.println("*** INITIALIZE DENSITY CLASSIFIER ***");
        System.out.println("*************************************");
        densityMap = new HashMap<String, ArrayList<Density>>();
        resultPerClass = new HashMap<String, Integer>();
    }

    public void learn(){
        System.out.println("*******************************");
        System.out.println("*** START LEARNING  DENSITY ***");
        System.out.println("*******************************");

        for (String categorie : LIST_CLASSE){

            ArrayList<Density> tempDensityArray = new ArrayList<Density>();
            for(int i = 1; i <= NUMBER_OF_TRAINING_IMAGE;i++){
                Density tempDensity = new Density(NUMBER_LINE,NUMBER_COLUMN);
                String imagePath = TRAINING_IMAGE_PATH+categorie+" ("+i+").png";
                tempDensity.setImage(imagePath);

                if(tempDensity.getVector().size()== NUMBER_COLUMN * NUMBER_LINE)
                    tempDensityArray.add(tempDensity);
            }

            densityMap.put(categorie,tempDensityArray);
        }

        System.out.println("*************************************************");
        System.out.println("*** COMPUTE MEAN VECTOR AND COVARIANCE MATRIX ***");
        System.out.println("*************************************************");

        covarianceMap = new HashMap<String, RealMatrix>();
        meanVectorMap = new HashMap<String, Vector<Double>>();

        //Create all the covariance matrix and mean vector
        for(HashMap.Entry<String, ArrayList<Density>> entry : densityMap.entrySet()) {
            String categorie = entry.getKey();
            ArrayList<Density> densityArrayList = entry.getValue();

            //Create the meanVector
            Vector<Double> meanVector = new Vector<Double>(densityArrayList.get(0).getDensityVector());
            RealMatrix rmx = MatrixUtils.createRealMatrix(meanVector.size(),densityArrayList.size());


            for(int i = 1; i < densityArrayList.size();i++){
                meanVector = VectorUtils.addVector(meanVector,densityArrayList.get(i).getDensityVector());
                rmx.setColumn(i,densityArrayList.get(i).toArray());
            }

            meanVector = VectorUtils.scalarDivision(meanVector,densityArrayList.size());
            RealMatrix covarianceMatrix = new Covariance(rmx).getCovarianceMatrix();

            covarianceMap.put(categorie,covarianceMatrix);
            meanVectorMap.put(categorie,meanVector);
        }
    }


    public ArrayList<DensityResultPair> identifieImage(Mat img){
        Density density = new Density(NUMBER_LINE,NUMBER_COLUMN);
        density.setImage(img);

        return compareDensityClosestNeighbour(density);

    }

    /*KPPV METHOD*/
    private ArrayList<DensityResultPair> compareDensityClosestNeighbour(Density toCompareDensity){
        Vector<Double> vectorToCompare = toCompareDensity.getDensityVector();

        ArrayList<DensityResultPair> results = new ArrayList<DensityResultPair>();
        for(HashMap.Entry<String, ArrayList<Density>> entry : densityMap.entrySet()){
            String categorie = entry.getKey();
            ArrayList<Density> densityArrayList = entry.getValue();

            for (Density aDensityArrayList : densityArrayList) {
                Vector<Double> vectReference = aDensityArrayList.getDensityVector();

                Double dist = VectorUtils.calculateEuclideanNorm(vectorToCompare,vectReference);
                insertResultIfLower(results, new DensityResultPair(categorie, dist));

            }
        }

        return results;
    }


    private void insertResultIfLower(ArrayList<DensityResultPair>results, DensityResultPair pair){
        if (results.size()==0){
            results.add(pair);
        }else{
            int i = 0;
            boolean inserted = false;
            if(results.size()<NUMBER_OF_NEIGHBOUR){
                while(i < results.size() && !inserted){
                    if(results.get(i).getDistance()>pair.getDistance()){
                        results.add(i,pair);
                        inserted = true;
                    }
                    i++;
                }

                if(!inserted)
                    results.add(pair);

            }else{
                while(i < NUMBER_OF_NEIGHBOUR && !inserted){
                    if(results.get(i).getDistance()>pair.getDistance()){
                        results.set(i,pair);
                        inserted = true;
                    }
                    i++;
                }
            }



        }

    }

    public String compareDensityMahalanobis(Density density){

        Vector<Double> vector = density.getDensityVector();
        Vector<Double> differenceVector = null;
        Double distance = Double.MAX_VALUE;
        String closestClass = "";

        for(String classe :LIST_CLASSE){
            differenceVector = VectorUtils.substractVector(vector,meanVectorMap.get(classe));
            RealMatrix invertedCovariance =  MatrixUtils.inverse(covarianceMap.get(classe));

            Vector<Double> intermediateResult = new Vector<Double>();

            //calculating the intermediate vector

        }

        return closestClass;
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


                if(checkResult(str, this.identifieImage(Imgcodecs.imread(imagePath,CV_LOAD_IMAGE_GRAYSCALE))))
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
