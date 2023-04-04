package sample;

public class Prey extends Stats {

    private long startTime;
    private int[][] gameGrid;
    private int widthx;
    private int heigty;
    private int age;
    private int gender; // 0 = male, 1 = female
    //lower the better for The skills
    private int speed; //speed of the animal Moves faster.
    private int intell;  //intelligence of the animal, (Knows where it should not be, steers away from the predator)
    private int reproduce; //reproduction rate of the animal, (if both have a high rate, child is 100%)
    private int survival; //Survival rate against a attack


    public Prey(int[][] Gamegrid) { //creates each prey entity
        gameGrid = Gamegrid;
        gender = (int) Math.floor(Math.random() * 2); //creates a random gender for the prey , 0 = male, 1 = female
        startTime = System.nanoTime();
        boolean taken = true;
        while (taken == true) {
            randomLocation();
            if (gameGrid[widthx][heigty] == 0) {
                gameGrid[widthx][heigty] = 1;
                taken = false;
            }
        }
        /////////////////////////////////CHANGE THE STARING AGE ONLY 10+ FOR TESTING
        age = 1; //creates a random age for the prey
        skills(); //creates the skills for the character
    }

    private void skills(){ //creates the skills for the Prey
        int[] temp = GenerateStat(); //takes stats from a array created in the Stats Class, the superclass
        speed = temp[0]; //GenerateStat Array = [speed, intell, reproduce, survival]
        intell = temp[1];
        reproduce = temp[2];
        survival = temp[3];
    }

    private void randomLocation() {
        widthx = (int) Math.floor(Math.random() * 20); //creates a random start location
        heigty = (int) Math.floor(Math.random() * 30);
    }

    public void MovePrey() { //moves the prey to the next location, one way from the previous location, it is possible to stand still
        boolean check = false;
        while(!check){
            int tempx = widthx;
            int tempy = heigty;
            int changex = (int)Math.floor(Math.random()*3);
            //changex = 0; //TEST
            if(changex ==0) {
            }
            else if(changex == 1){
                tempx++;
            }
            else {
                tempx--;
            }
            int changey = (int)Math.floor(Math.random()*3);
            //changey = 0; //TEST
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
                        if (gameGrid[tempx][tempy]==0 || ((tempx == widthx) &&(tempy == heigty) )){
                            check=true;
                            gameGrid[widthx][heigty]=0;
                            gameGrid[tempx][tempy]=1;
                            widthx=tempx;
                            heigty=tempy;
                        }
                }
            }
        }
    }

    public void ParentsValues( int speedParent, int intellParent, int reproduceParent, int survivalParent){ //Gets the average of the parents stats and makes it its own, way of evolution
        speed = speedParent;
        intell = intellParent;
        reproduce = reproduceParent;
        survival = survivalParent;
    }

    public void resetStartTime(){
        startTime = System.nanoTime();
    } //resets the time that has passed since moved

    public long getStartTime(){
        return startTime;
    }

    public void addAge() {
        age++;
    } //adds the age every year

    public int getHeigty() {
        return heigty;
    }

    public int getWidthx() {
        return widthx;
    }

    public int getSpeed() {
        return speed;
    }

    public int getReproduce() {
        return reproduce;
    }

    public int getIntell() {
        return intell;
    }

    public int getSurvival() {
        return survival;
    }

    public int getAge() {
        return age;
    }


    public int getGender() {
        return gender;
    }

    public void changeSurvival(){
        survival= survival+10;
    } //every time it is attacked, the chance to survive the next attack gets lower
}
