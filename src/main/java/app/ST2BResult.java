package app;

public class ST2BResult {
    String AnnualPeriod;
    Integer Collected;
    Integer Recycled;
    Integer Disposed;
    Integer WasteDisposedPercentage;

    public ST2BResult(String AnnualPeriod, Integer Collected, Integer Recycled, Integer Disposed, Integer WasteDisposedPercentage){
        this.AnnualPeriod = AnnualPeriod;
        this.Collected = Collected;
        this.Recycled = Recycled;
        this.Disposed = Disposed;
        this.WasteDisposedPercentage = WasteDisposedPercentage;
    }
}
