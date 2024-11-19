package app;

public class result3Awaste {

    double similarity;
    int kerbside, cleanUp, dropOff = 0;
    String LGAName = "none";

    public result3Awaste() {
    }

    public result3Awaste(String LGAName, double similarity, int kerbside, int dropOff, int cleanUp) {
        this.LGAName = LGAName;
        this.similarity = similarity;
        this.kerbside = kerbside;
        this.dropOff = dropOff;
        this.cleanUp = cleanUp;
    }

}
