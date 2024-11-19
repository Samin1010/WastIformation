package app;

public class result3Arecyclable {

    double similarity;
    int kerbside, cds, cleanUp, dropOff = 0;
    String LGAName = "none";

    public result3Arecyclable(String LGAName, double similarity, int kerbside, int cds, int dropOff, int cleanUp) {
        this.LGAName = LGAName;
        this.similarity = similarity;
        this.kerbside = kerbside;
        this.dropOff = dropOff;
        this.cleanUp = cleanUp;
        this.cds = cds;
    }
}
