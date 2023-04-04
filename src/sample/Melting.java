package sample;

public class Melting {
    private long startTime;


    public Melting(){
        startTime = System.nanoTime();
    }

    public void resetStartTime(){
        startTime = System.nanoTime();
    }

    public long getStartTime(){
        return startTime;
    }
}
