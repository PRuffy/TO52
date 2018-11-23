package fr.utbm.japanesecharacterrecognition.classifier.density;

public class DensityResultPair {
    private String Categorie;
    private Double distance;

    public DensityResultPair(String categorie, Double distance) {
        Categorie = categorie;
        this.distance = distance;
    }

    public String getCategorie() {
        return Categorie;
    }

    public void setCategorie(String categorie) {
        Categorie = categorie;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
}
