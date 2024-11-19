package app;

public class ST2AResult {
    int Population;
    int HouseSurveyed;
    int TotalWasteCollected;
    int TotalWasteRecycled;
    int AvgPercentageRecycled;
    int AverageWastePerHousehold;
    String Name;

    public ST2AResult(String Name, int Population, int HouseSurveyed, int TotalWasteCollected, int TotalWasteRecycled, int AveragePercentageRecycled, int AverageWastePerHousehold){
        this.Name = Name;
        this.Population = Population;
        this.HouseSurveyed = HouseSurveyed;
        this.TotalWasteCollected = TotalWasteCollected;
        this.TotalWasteRecycled = TotalWasteRecycled;
        this.AverageWastePerHousehold = AverageWastePerHousehold;
        this.AvgPercentageRecycled = AveragePercentageRecycled;
    }
}
