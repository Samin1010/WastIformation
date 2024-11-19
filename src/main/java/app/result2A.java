package app;

public class result2A {

    int population, house, waste, recycled, percRecycled;
    double avgWaste;
    String LGAname;

    public result2A(String LGAname, int population, int house, int waste, int recycled, int percRecycled,
            double avgWaste) {
        this.population = population;
        this.house = house;
        this.waste = waste;
        this.recycled = recycled;
        this.percRecycled = percRecycled;
        this.avgWaste = avgWaste;
        this.LGAname = LGAname;
    }

}
