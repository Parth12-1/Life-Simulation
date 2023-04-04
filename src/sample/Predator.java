package sample;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.ListView;

public class Predator {
    private ArrayList<Prey> PreyList;
    private long startTime;
    private int[][]gameGrid;
    private int widthx;
    private int heigty;
    private int age;
    private int gender; // 0 = male, 1 = female
    private int hunger; //0 = Pretty Much Dead, 1 = Starving, malnourished, 2 = Man is a Stick, Unealthy case of being Underweight 3 = Starving, 4 = hungry, 5 = not full but not empty, 6 = full, 7 = too full, 8 = About to die overstuffed
    private boolean targetFound = false;
    private Prey Detected;
    private boolean PREYFOUND = false;
    private ListView News;


    public Predator(int[][] Gamegrid, ListView news){ //constructor
        gameGrid = Gamegrid; //pass-by reference
        News = news;
        startTime = System.nanoTime();
        boolean taken = true;
        while(taken == true){
            randomLocation();
            if(gameGrid[widthx][heigty] == 0){
                gameGrid[widthx][heigty] = 2;
                taken = false;
            }
        }
        age = 1;
        gender = (int)Math.floor(Math.random()*2);
        hunger = 5;
    }

    private void randomLocation(){ //Starts the character of at a random location
        widthx = (int)Math.floor(Math.random()*20);
        heigty = (int)Math.floor(Math.random()*30);
    }

    public void MovePredator(ArrayList<Prey> preylist){ //moves the Predator to the next location, one way from the previous location, it is possible to stand still
        PreyList = preylist;
        if (targetFound == false){ //if its targeting something, its not using random movement
            boolean check = false;
            while(!check){
                int tempx = widthx;
                int tempy = heigty;
                int changex = (int)Math.floor(Math.random()*3);
                if(changex ==0) {
                }
                else if(changex == 1){
                    tempx++;
                }
                else {
                    tempx--;
                }
                int changey = (int)Math.floor(Math.random()*3);
                if(changey ==0) {
                }
                else if(changey == 1){
                    tempy++;
                }
                else {
                    tempy--;
                }
                if (tempx>=0 && tempx < 20 ){
                    if (tempy>=0 && tempy < 30 ){
                        if (tempy == 0){
                            if ((gameGrid[tempx][tempy]==0 || gameGrid[tempx][tempy]==1) || ((tempx == widthx) &&(tempy == heigty) ) ){
                                if (gameGrid[tempx][tempy] ==1){ //If it lands on a prey, it kills it indefinitely
                                    Prey temppr = FindPrey(tempx,tempy);
                                    PreyList.remove(temppr);
                                    News.getItems().add("Feast, Predator Just killed a Prey");
                                    if (hunger >= 6){
                                        hunger = 8;
                                    }
                                    else{
                                        hunger +=2;
                                    }
                                    gameGrid[tempx][tempy] = 0;
                                }
                                check=true;
                                gameGrid[widthx][heigty]=0;
                                gameGrid[tempx][tempy]=2;
                                CheckNextTo(tempx,tempy);
                                widthx=tempx;
                                heigty=tempy;
                            }
                        }
                        else{
                            if ((gameGrid[tempx][tempy]==0 || gameGrid[tempx][tempy]==1) || ((tempx == widthx) &&(tempy == heigty) )){
                                if (gameGrid[tempx][tempy] ==1){
                                    Prey temppr = FindPrey(tempx,tempy);
                                    PreyList.remove(temppr);
                                    News.getItems().add("Feast, Predator Just killed a Prey");
                                    gameGrid[tempx][tempy] = 0;
                                }
                                check=true;
                                gameGrid[widthx][heigty]=0;
                                gameGrid[tempx][tempy]=2;
                                CheckNextTo(tempx,tempy);
                                widthx=tempx;
                                heigty=tempy;
                            }
                        }
                    }
                }
            }
        }
        else{
            TargetPrey(); //unrandom movement
        }
        }


    public void resetStartTime(){
        startTime = System.nanoTime();
    }

    public long getStartTime(){
        return startTime;
    }

    private void CheckNextTo(int tempx, int tempy){ //Checks if there is a prey next to it, if there is, then there is a chance to kills it
        for(int i =-1; i<=1; i++){
            for(int j = -1; j<=1; j++){
                if (tempx+i<10 && tempx+i>=0 && tempy+j<30 && tempy+j>=0){
                    if(gameGrid[tempx+i][tempy + j] == 1){
                        Prey temp;
                        temp = FindPrey(tempx+i,tempy+j);
                        DeletePrey(temp);
                        if (hunger >= 6){
                            hunger = 8;
                        }
                        else{
                            hunger +=2;
                        }
                    }
                }

            }
        }
    }

    private Prey FindPrey(int preyx, int preyy){ //finds the object of the Prey located
        Prey A = null;
        if (PreyList.size()!=0) {
            A = PreyList.get(0);
            for(Prey a: PreyList){
                if (a.getHeigty() == preyy && a.getWidthx() == preyx){
                    A = a;
                    break;
                }
            }
        }

        return A;
    }

    private void DeletePrey(Prey x){ //kills the prey by checking its chances to survive ////////////ERROR WITH LIGHTNING
        int chance = (int)Math.floor(Math.random()*101);
        if (x != null && hunger!=8){
            if (chance<=x.getSurvival()){ //checks its chance of survival from the stats of the prey.
                if(x.equals(Detected)){
                    targetFound = false;
                }
                PreyList.remove(x);
                News.getItems().add("Feast, Predator Just killed a Prey");
                gameGrid[x.getWidthx()][x.getHeigty()] = 0;
                if (hunger >= 6){
                    hunger = 8;
                }
                else{
                    hunger +=2;
                }
            }
            else{ //if the Prey runs away, its chance to survive another attack decreases by very little.
                x.changeSurvival();
            }
        }

    }

    public void Target(Prey tempTarget){ //stops random movement and makes movement correspond to the the Prey that the predator(s) are chasing.
        Detected = tempTarget;
        targetFound = true;
        PREYFOUND = true;  //ONE
    }

    public void TargetPrey(){  //This traces the prey and follows it
        int tempx = widthx;
        int tempy = heigty;
            if(Detected.getWidthx() > widthx){
                tempx++;
                if (gameGrid[tempx][tempy]==0){
                    gameGrid[tempx][tempy]=2;
                    gameGrid[widthx][heigty]=0;
                    widthx=tempx;
                    heigty=tempy;
                }
            }
            else if(Detected.getWidthx() == widthx){
            }
            else if(Detected.getWidthx() < widthx){
                tempx--;
                if (gameGrid[tempx][tempy]==0){
                    gameGrid[tempx][tempy]=2;
                    gameGrid[widthx][heigty]=0;
                    widthx=tempx;
                    heigty=tempy;
                }
            }
            if(Detected.getHeigty() > heigty){
                tempy++;
                if (gameGrid[tempx][tempy]==0){
                    gameGrid[tempx][tempy]=2;
                    gameGrid[widthx][heigty]=0;
                    widthx=tempx;
                    heigty=tempy;
                }
            }
            else if(Detected.getHeigty() == heigty){
            }
            else if(Detected.getHeigty() < heigty){
                tempy--;
                if (gameGrid[tempx][tempy]==0){
                    gameGrid[tempx][tempy]=2;
                    gameGrid[widthx][heigty]=0;
                    widthx=tempx;
                    heigty=tempy;
                }
            }
            if (gameGrid[tempx][tempy]==0 || gameGrid[tempx][tempy]==1 || ((tempx == widthx) &&(tempy == heigty) )){ //The same kill feature from the Random Movement.
                if (gameGrid[tempx][tempy] ==1){
                    Prey temppr = FindPrey(tempx,tempy);
                    if (Detected!= null && temppr != null){
                        if (temppr.equals(Detected)){
                        targetFound = false;
                        }
                    }

                    PreyList.remove(temppr);
                    News.getItems().add("Feast, Predator Just killed a Prey");
                    if (hunger >= 6){
                        hunger = 8;
                    }
                    else{
                        hunger +=2;
                    }
                    gameGrid[tempx][tempy] = 0;
                }
                gameGrid[widthx][heigty]=0;
                gameGrid[tempx][tempy]=2;
                CheckNextTo(tempx,tempy);
                widthx=tempx;
                heigty=tempy;
            }
    }

    public int getHunger(){
        return hunger;
    }

    public void setHunger(){ //changes hunger everyyear
        hunger--;
    }

    public void ParentLocationBirth(int x, int y){ //born near parnet
        gameGrid[widthx][heigty] = 0;
        widthx = x;
        heigty = y;
    }

    public void addAge() {
        age++;
    }

    public int getAge() {
        return age;
    }

    public int heigty() {
        return heigty;
    }

    public int widthx() {
        return widthx;
    }

    public boolean isTargetFound() { //if this object is following a prey and kills it then it found the prey
        if(PREYFOUND == true){ //if this is one of the objects that was tracing only then it can return the proper answer because the new borns are always true right when they are born
            return targetFound;
        }
        else{
            return true;
        }
    }

    public boolean isTargetfoundrly() { //the real answer without caring if its a new baby
        return targetFound;
    }

    public void TargetFoundFalse() { //changes to random movement
        targetFound = false;
    }

    public int getGender() {
        return gender;
    }

    public Prey getDetected() {
        return Detected;
    }
}
