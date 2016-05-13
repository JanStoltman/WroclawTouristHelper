package com.example.yggdralisk.wroclawtouristhelper;

/**
 * Created by yggdralisk on 13.05.16.
 */
public class BikePoint {
    int lp;
    String system;
    String lokalizacja;
    Double szerokoscGeo;
    Double DlugoscGeo;

    public BikePoint(int lp, String system, String lokalizacja, Double szerokoscGeo, Double dlugoscGeo) {
        this.lp = lp;
        this.system = system;
        this.lokalizacja = lokalizacja;
        this.szerokoscGeo = szerokoscGeo;
        DlugoscGeo = dlugoscGeo;
    }

    public BikePoint() {
    }

    public void setLp(int lp) {
        this.lp = lp;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public void setLokalizacja(String lokalizacja) {
        this.lokalizacja = lokalizacja;
    }

    public void setSzerokoscGeo(Double szerokoscGeo) {
        this.szerokoscGeo = szerokoscGeo;
    }

    public void setDlugoscGeo(Double dlugoscGeo) {
        DlugoscGeo = dlugoscGeo;
    }

    public int getLp() {

        return lp;
    }

    public String getSystem() {
        return system;
    }

    public String getLokalizacja() {
        return lokalizacja;
    }

    public Double getSzerokoscGeo() {
        return szerokoscGeo;
    }

    public Double getDlugoscGeo() {
        return DlugoscGeo;
    }
}
