package fr.utbm.japanesecharacterrecognition.comparator.density;

import fr.utbm.japanesecharacterrecognition.comparator.Comparator;
import fr.utbm.japanesecharacterrecognition.feature.density.Density;
import fr.utbm.japanesecharacterrecognition.math.VectorDouble;

import java.util.ArrayList;
import java.util.HashMap;

public class DensityComparator implements Comparator {
    private final String[] LIST_CLASSE = {"A","I","U","E","O",
            "KA","KI","KU","KE","KO",
            "SA","SHI","SU","SE","SO",
            "TA","CHI","TSU","TE","TO",
            "NA","NI","NU","NE","NO",
            "HA","HI","FU","HE","HO",
            "RA","RI","RU","RE","RO",
            "MA","MI","MU","ME","MO",
            "WA","YA","YU","YO","WO","N"};

    private HashMap<String, ArrayList<Density>> density;
    private Double numberOfLine;
    private Double numberOfColumn;



    public DensityComparator(){
        density = new HashMap<String, ArrayList<Density>>();
    }

    public void learnDensity(String imageLocation,int numberOfPictures, Double numberOfLine, Double numberOfColumn){
        this.numberOfColumn = numberOfColumn;
        this.numberOfLine = numberOfLine;
        for (String categorie : LIST_CLASSE){

            ArrayList<Density> tempDensityArray = new ArrayList<Density>();
            for(int i = 1; i <= numberOfPictures;i++){
                Density tempDensity = new Density(numberOfLine,numberOfColumn);
                String imagePath = imageLocation+categorie+" ("+i+").png";
                tempDensity.setImage(imagePath);

                tempDensityArray.add(tempDensity);
            }

            density.put(categorie,tempDensityArray);
        }
    }

    public ArrayList<DensityResultPair> compareDensity(String img){
        Density toCompareDensity = new Density(numberOfLine,numberOfColumn);
        toCompareDensity.setImage(img);
        VectorDouble vectorToCompare = toCompareDensity.getDensityVector();

        ArrayList<DensityResultPair> results = new ArrayList<DensityResultPair>();
        for(HashMap.Entry<String, ArrayList<Density>> entry : density.entrySet()){
            String categorie = entry.getKey();
            ArrayList<Density> densityArrayList = entry.getValue();

            for (int i = 0; i < densityArrayList.size();i++){
                VectorDouble vectReference = densityArrayList.get(i).getDensityVector();

                Double dist = vectorToCompare.computeEuclideanSubstraction(vectReference);
                insertResultIfLower(results,new DensityResultPair(categorie,dist));

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
            if(results.size()<10){
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
                while(i < 10 && !inserted){
                    if(results.get(i).getDistance()>pair.getDistance()){
                        results.set(i,pair);
                        inserted = true;
                    }
                    i++;
                }
            }



        }

    }

}
