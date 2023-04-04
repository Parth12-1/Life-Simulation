package sample;

public class Stats {
    //GenerateStat Array = [speed, intell, reproduce, survival]
    private int[] x = new int[4];
    public int[] GenerateStat(){ //creates the stats for the prey
        x[0] = 40;
        x[1] = 3;
        x[2] = 40;
        x[3]= 60;
        int temp = (int)Math.floor(Math.random()*5);
        if(temp == 0){
            x[0] = 60;
        }
        else if (temp ==1){
            x[1] = 4;
        }
        else if (temp == 2) {
            x[2] = 60;
        }
        else if (temp == 3) {
            x[3] = 70;
        }
        int temp2 = (int)Math.floor(Math.random()*5);
        while(temp2 == temp){
            temp2 = (int)Math.floor(Math.random()*5);
        }
        if (temp2 == 0){
            x[0] = 30;
        }
        else if (temp2 == 1){
            x[1] = 2;
        }
        else if (temp2 ==2){
            x[2] = 30;
        }
        else if (temp2 ==3){
            x[3] = 50;
        }
        return x;
    }
}
