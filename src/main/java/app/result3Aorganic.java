package app;

public class result3Aorganic {

    double similarity;
    int kerbside, kerbsideFOGO, cleanUp, dropOff, other = 0;
    String LGAName = "none";

    public result3Aorganic(String LGAName, double similarity, int kerbside, int kerbsideFOGO, int dropOff, int cleanUp,
            int other) {
        this.LGAName = LGAName;
        this.similarity = similarity;
        this.kerbside = kerbside;
        this.dropOff = dropOff;
        this.cleanUp = cleanUp;
        this.kerbsideFOGO = kerbsideFOGO;
        this.other = other;
    }
}
