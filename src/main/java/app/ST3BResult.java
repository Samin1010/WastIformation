package app;

public class ST3BResult {
    int StartCollected;
    int StartRecycled;
    int EndCollected;
    int EndRecycled;
    boolean isPercent;

    public ST3BResult(int StartCollected, int StartRecycled, int EndCollected, int EndRecycled, boolean isPercent){
        this.EndCollected = EndCollected;
        this.StartCollected = StartCollected;
        this.StartRecycled = StartRecycled;
        this.EndRecycled = EndRecycled;
        this.isPercent = isPercent;
    }
}
