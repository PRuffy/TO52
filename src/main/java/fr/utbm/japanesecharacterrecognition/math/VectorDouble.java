package fr.utbm.japanesecharacterrecognition.math;

import java.util.Vector;

public class VectorDouble{

    private Vector<Double> vector;

    public VectorDouble(int capacity){
        this.vector = new Vector<Double>(capacity);
    }

    public Double computeEuclideanSubstraction(VectorDouble vectorDouble){
        VectorDouble result = new VectorDouble(this.vector.capacity());
        for(int i = 0; i < this.vector.size();i++){
            result.getVector().add(i,this.vector.get(i)-vectorDouble.getVector().get(i));
        }

        return result.calculateEuclideanNorm();
    }

    public Double calculateEuclideanNorm(){
        Double result=  0.0;
        for(int i = 0; i < this.vector.size();i++)
            result+=Math.pow(this.vector.get(i),2);

        return Math.sqrt(result);
    }

    public Vector<Double> getVector(){
        return vector;
    }
}
