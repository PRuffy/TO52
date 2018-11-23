package fr.utbm.japanesecharacterrecognition.math;


import java.util.Vector;

public class VectorUtils {

    public static Double calculateEuclideanNorm(Vector<Double> vector1, Vector<Double> vector2){
        try{
            Vector<Double> resultVect = substractVector(vector1,vector2);

            Double result=  0.0;
            for(Double vectorElement : resultVect)
                result+=Math.pow(vectorElement,2);

            return Math.sqrt(result);

        }catch(ArrayIndexOutOfBoundsException e){
            return Double.MAX_VALUE;
        }


    }

    public static Vector<Double> addVector(Vector<Double> vector1, Vector<Double> vector2) {
        if (vector1.size() == vector2.size()) {
            Vector<Double> result = new Vector<Double>();
            for (int i = 0; i < vector1.size(); i++)
                result.add(i,vector1.get(i) + vector2.get(i));

            return result;
        }else{
            return vector1;
        }

    }

    public static Vector<Double> scalarDivision(Vector<Double> vector,int coeffictient){
        if(coeffictient == 0)
            return vector;

        Vector<Double> result = new Vector<Double>();
        for(int i = 0 ; i < vector.size();i++)
           result.add(i,vector.get(i)/coeffictient);

        return result;

    }

    public static Vector<Double> substractVector(Vector<Double> vector1, Vector<Double> vector2) {

        if(vector1.size()==vector2.size()){
            Vector<Double> result = new Vector<Double>();
            for(int i = 0 ; i < vector1.size();i++){
                result.add(i,vector1.get(i)-vector2.get(i));
            }

            return result;
        }else {
            return vector1;
        }

    }
}
