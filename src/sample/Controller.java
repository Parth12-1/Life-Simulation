package sample;



import com.sun.prism.paint.Color;
import java.awt.Transparency;
import java.beans.beancontext.BeanContextChild;
import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.rmi.MarshalException;
import java.time.Year;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;


public class Controller {

    //GridPane gPane = new GridPane();
    @FXML
    private AnchorPane aPane;

    @FXML
    private GridPane gPane;

    @FXML private Button StartBTN , LightingBTNFX, MltBTNFX, AddRandBTNFX, DeleteBTNFX, StopBTN, BabyBoom;

    @FXML
    private ListView StatsLST, NewsLST;

    @FXML
    private Label Yearcount, Message;

    private int Day = 0;
    private int Melttiming = 0;
    private int MeltCompare = 7;
    private int year = 2020;
    private double LocalStopTime = System.nanoTime();
    private int btnWidth = 20; //width of each box thing
    private int btnHeight = 30; // btn[Width][Height]
    private ArrayList<Prey> PreyList = new ArrayList<>();
    private ArrayList<Predator> PredatorList = new ArrayList<>();
    private Button[][] btn = new Button[btnWidth][btnHeight];
    private int[][] gameGrid = new int[btnWidth][btnHeight]; //0 = Icecap (empty), 1 = prey, 2 = predator, 3 = water, 4 = Lighting
    private final String BaseButton = "-fx-background-radius : 0;";
    private Prey Chase;
    private boolean CurrentDetection = false;
    private Prey ChasingRN;
    private int counter = 0;
    Melting Melt = new Melting();
    private boolean GAME = false;
 //think about adding a after game



    @FXML
    private void handleStart(ActionEvent event) { //Starts the sim
        //Changes the UI color
        GAME = true;
        aPane.setStyle("-fx-background-color:#66CADE"); //This is the Logo Blue
        StartBTN.setStyle("-fx-background-color:#2E9DD4"); //This is the Ice Blue
        LightingBTNFX.setStyle("-fx-background-color:#2E9DD4");
        MltBTNFX.setStyle("-fx-background-color:#2E9DD4");
        AddRandBTNFX.setStyle("-fx-background-color:#2E9DD4");
        BabyBoom.setStyle("-fx-background-color:#2E9DD4");
        DeleteBTNFX.setStyle("-fx-background-color:#2E9DD4");  //Other colors is the Ice Grey #656D7E
        StatsLST.setStyle("-fx-background-color:#30BDD3");
        NewsLST.setStyle("-fx-background-color:#30BDD3");
        StopBTN.setStyle("-fx-background-color:#2E9DD4");
        //Creates the Buttons
        for (int i = 0; i < btnWidth; i++) { //creates the grid
            for (int j = 0; j < btnHeight; j++) {
                //Initializing 2D buttons with values i,j
                btn[i][j] = new Button();
                btn[i][j].setStyle(BaseButton + "-fx-background-color:#EDF5FE"); //Icecap:#EDF5FE, Prey color:#00ff00, Predator Color:#ff0000, Melting color:#0660C1, Lighting color:#F7B61C
                btn[i][j].setPrefWidth(15);
                //Paramters:  object, columns, rows
                gPane.add(btn[i][j], j, i);
                gameGrid[i][j] = 0;
            }
        }
        //Makes the Grid have gridlines
        gPane.setGridLinesVisible(true); //adds gridlines
        gPane.setVisible(true); //adds gridlines

        EventHandler z = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
            }
        };
        //Makes the Buttons, Buttons
        for (int i = 0; i < btnWidth; i++) {
            for (int j = 0; j < btnHeight; j++) {
                btn[i][j].setOnMouseClicked(z);
            }
        }
        StartGame(); //adds the start defults
        UpdateGamepersec(); //updates the game
    }

    @FXML
    private void Stop(){ //Stops the Game
        GAME = false;
    }

    @FXML
    private void BabyBoom(){ //Makes a Explosion of Babies ALWAYS CAUSES THE PREDATOR TO BE THE LAST ONE ALIVE
        int spaces = 0;
        for (int i = 0; i < btnWidth; i++) {
            for (int j = 0; j < btnHeight; j++) {
                if (gameGrid[i][j] ==0){
                    spaces++;
                }
            }
        }
        spaces = spaces/2; //Based on how many spaces in the game board left

        for (int i =0; i< spaces/2; i++){
            PreyList.add(new Prey(gameGrid));
        }
        for (int i =0; i< spaces/5; i++){
            PredatorList.add(new Predator(gameGrid, NewsLST));
        }
    }


    private void StartGame(){ //adds the game defults
        StartSmallG();
        //Makes a Certain amount of Preys, and Predators to sta
        // start the game
        for (int i = 0; i<16; i++){ //keep at 16 only more or less for testing
            PreyList.add(new Prey(gameGrid));
        }
        for (int i = 0; i<8; i++){ //keep at 8 only changed for testing
            PredatorList.add(new Predator(gameGrid, NewsLST));

        }
        detectPredator();
    }


    private void UpdateGrid(){ //changes how the grid looks by changing the colors
        //based on the number in the gameGrid array, it creates the simulation visual in the UI
        for (int i = 0; i < btnWidth; i++) {
            for (int j = 0; j < btnHeight; j++) {
                if(gameGrid[i][j] == 0){ //IceCap
                    btn[i][j].setStyle("-fx-background-color:#9BCEE2"); //Start Color was #EDF5FE
                }
                else if (gameGrid[i][j] == 1){ //Prey
                    btn[i][j].setStyle("-fx-background-color:#00ff00");
                }
                else if (gameGrid[i][j] == 2){ //Predator
                    btn[i][j].setStyle("-fx-background-color:#ff0000");
                }
                else if (gameGrid[i][j] == 3){ //Water
                    btn[i][j].setStyle("-fx-background-color:#0660C1");
                }
                else if (gameGrid[i][j] == 4){ //Lighting
                    btn[i][j].setStyle("-fx-background-color:#F7B61C");
                }
            }
        }
    }


    private void UpdateGamepersec(){ //updates the game every second

            new AnimationTimer() {
            @Override
            public void handle(long now) {
                //Prey have the time that they update changed for each one, so some might have 0.6 seconds, vs 0.8 sec, vs 1.2 sec's
                if (GAME){
                if (Melttiming >= MeltCompare){ //for the Melting or constricting of the grid
                    Melttiming = 0;
                    SmallerGameG();
                    counter++;
                }
                if (Day >= 7){ //Every 7 days, a year passes and causes interactions to occur
                    Hungercheck();
                    Day = 0; //reset the day counter
                    CurrentDetection = true;
                    detectPredator();
                    year++; //year counter goes us
                    Yearcount.setText("Year: " + year);
                    for (int i =0; i< PreyList.size(); i++) { //updates the Prey(s) ages
                        PreyList.get(i).addAge();
                    }
                    for (int i =0; i< PredatorList.size(); i++){ //updates the Predator(s) ages
                        PredatorList.get(i).addAge();
                        PredatorList.get(i).setHunger();
                    }
                    KillAge();
                }
                for (int i =0; i< PreyList.size(); i++) { //updates the Preys
                    double temp = (double)PreyList.get(i).getSpeed();
                    temp = temp*2;
                    temp = temp * 5000000; //add a 0
                    if(now - PreyList.get(i).getStartTime() > temp){
                        PreyList.get(i).MovePrey();
                        PreyList.get(i).resetStartTime();
                    }
                }
                // Predators each update once every second unlike the Prey
                for (int i =0; i< PredatorList.size(); i++){ //updates the Predators
                    if(now - PredatorList.get(i).getStartTime() > 500000000.0){ //add a 0
                        PredatorList.get(i).MovePredator(PreyList);
                        PredatorList.get(i).resetStartTime();
                    }
                }


                if(now - Melt.getStartTime() > 250000000.0){ //Game wide things
                    Stats();
                    Melt.resetStartTime();
                    Day++;
                    Melttiming++;
                    for (int z = 0; z < btnWidth; z++) {
                        for (int j = 0; j < btnHeight; j++) {
                            if (gameGrid[z][j] == 4) { //lightning
                                gameGrid[z][j] = 0;
                            }
                        }
                    }
                    gameGrid[0][0] = 3;
                    PreyPartner(); //Chances if a baby is possible
                    PredatorPartner(); //chance if a Predator baby is possible
                }
                UpdateGrid(); //updates the grid every time a change occurs
                Checkiffound();
            }
            }
        }.start();
    }


    @FXML
    private void LightningBTN() { //Causes a lighting bolt in a X figure to kill anything that happens, WILL NOT HAPPEN IF THE PREDATOR IS DETECTING A PREY
        boolean cando = false;
        if (PredatorList.size() == 0){
            cando = true;
        }
        else if (PredatorList.get(0).isTargetfoundrly() == false){
            cando = true;
        }
        if (cando == true){
            NewsLST.getItems().add(0,"God Just Got Angry, here's a Free Lightning Bolt");
            int x =1+(int)Math.floor(Math.random()*17);
            int y = 1+(int) Math.floor(Math.random()*27);
            gameGrid[x][y] = 4;
            if (FindPrey(x,y) != null){
                Prey tempPrey= FindPrey(x,y);
                if (tempPrey != null){
                    PreyList.remove(tempPrey);
                }
            }
            if (FindPredator(x,y) != null){
                Predator tempPred = FindPredator(x,y);
                if(tempPred != null){
                    PredatorList.remove(tempPred);
                }
            }
            for (int i = -1; i<=1 ; i+=2){
                for (int j = -1; j<=1; j+=2){
                    gameGrid[x+i][y+j] = 4;
                    if (FindPrey(x+i,y+j) != null){
                        Prey tempPrey= FindPrey(x+i,y+j);
                        if (tempPrey != null){
                            PreyList.remove(tempPrey);
                            NewsLST.getItems().add(0,"This Prey just died from lightning, Wrong Place Wrong Time");
                        }
                    }
                    if (FindPredator(x+i,y+j) != null){
                        Predator tempPred = FindPredator(x+i,y+j);
                        if(tempPred != null){
                            PredatorList.remove(tempPred);
                            NewsLST.getItems().add(0,"This Predator just died from lighting, Wrong Place Wrong Time");
                        }
                    }
                }
            }
        }
    }


    @FXML
    private void AddPrey(){ //adds a Prey to the game
        PreyList.add(new Prey(gameGrid));
    }

    @FXML
    private void AddPredator(){ //Adds a Predator to the game
        PredatorList.add(new Predator(gameGrid, NewsLST));
    }

    @FXML
    private void MltFstBTN(){ //Causes the Board to melt faster, only occurs once
        MeltCompare = MeltCompare/2;
        MltBTNFX.setDisable(true);
    }

    @FXML
    private void RESET(){ //Resets the game
        MltBTNFX.setDisable(false);
        Day = 0;
        Melttiming = 0;
        MeltCompare = 7;
        year = 2020;
        LocalStopTime = System.nanoTime();
        btnWidth = 20; //width of each box thing
        btnHeight = 30; // btn[Width][Height]
        PreyList.clear();
        PredatorList.clear();
        int[][] gameGrid = new int[btnWidth][btnHeight]; //0 = Icecap (empty), 1 = prey, 2 = predator, 3 = water, 4 = Lighting
        for (int i = 0; i<btnWidth; i++){
            for (int j = 0; j<btnHeight; j++){
                gameGrid[i][j] = 0;
                btn[i][j].setStyle("-fx-background-color:#9BCEE2");
            }
        }
        Chase = null;
        CurrentDetection = false;
        ChasingRN = null;
        counter = 0;
        Melt.resetStartTime();
        GAME = false;
    }


//    private void DeletePrey(Prey x){ //kills the prey by checking its chances to survive
//        int chance = (int)Math.floor(Math.random()*101);
//        if (chance<=x.getSurvival()){ //checks its chance of survival from the stats of the prey.
//            PreyList.remove(x);
//            gameGrid[x.getWidthx()][x.getHeigty()] = 0;
//        }
//        else{ //if the Prey runs away, its chance to survive another attack decreases by very little.
//            x.changeSurvival();
//        }
//    }


    @FXML
    private void AddRandBTN(){ //adds a random prey or predator (add on)
        int preyOPredator = (int) Math.floor(Math.random()*3); //adds a random either Prey, or Predator
        if (preyOPredator == 0 || preyOPredator == 1){
            PreyList.add(new Prey(gameGrid));
        }
        else if (preyOPredator ==2){
            PredatorList.add(new Predator(gameGrid, NewsLST));
        }
    }


    @FXML
    private void DeleteBTN(){ //DELETES A RANDOM PREY or PREDATOR
        if (Math.floor(Math.random()*2) == 1){
            if (PredatorList.size() >0 ){
                int Predfound = (int)Math.floor(Math.random()*PredatorList.size());
                ;
                gameGrid[PredatorList.get(Predfound).widthx()][PredatorList.get(Predfound).heigty()] = 0;
                PredatorList.remove(Predfound);
            }
        }
        else{
            if (PreyList.size() >0 ){
                int Preyfound = (int)Math.floor(Math.random()*PreyList.size());
                gameGrid[PreyList.get(Preyfound).getWidthx()][PreyList.get(Preyfound).getHeigty()] = 0;
                PreyList.remove(Preyfound);
            }
        }
    }

    private void PreyPartner(){ //Finds the partner for each Prey if there is a prey of opposite gender next to it.
        for (Prey x: PreyList) { //for each Prey //HORRIBLE CODING USE LOOPS SO ITS EASIER;
            Prey temp = null;
            boolean True = false;
            int percent = x.getReproduce();
            int tempx = x.getWidthx();
            int tempy = x.getHeigty();
            //checks every side of the prey
            for(int i =-1; i<=1; i++){
                for(int j = -1; j<=1; j++){
                    if (tempx+i<10 && tempx+i>=0 && tempy+j<30 && tempy+j>=0){
                        if(gameGrid[tempx+i][tempy + j] == 1){
                            temp = FindPrey(tempx+i,tempy+j);
                            if (temp!=null){
                                int GenderTemp = temp.getGender();
                                int GenderX = x.getGender();
                                if(GenderTemp != GenderX){
                                    True = true;
                                }
                            }
                        }
                    }
                }
            }
                if (True){ //if there is something next to it, it send the chosen temp and the found temp to the create child function
                    CreateChild(temp , x);
                    break;
                }
            }
        }

    private void PredatorPartner(){ //Finds the partner for each Predator if there is a prey of opposite gender next to it.
        for (Predator x: PredatorList) {
            Predator temp = null;
            boolean True = false;
            int percent = 40;
            int tempx = x.widthx();
            int tempy = x.heigty();
            //checks every side of the prey
            for(int i =-1; i<=1; i++){
                for(int j = -1; j<=1; j++){
                    if (tempx+i<10 && tempx+i>=0 && tempy+j<30 && tempy+j>=0){
                        if(gameGrid[tempx+i][tempy + j] == 2){
                            temp = FindPredator(tempx+i,tempy+j);
                            if (temp != null){
                                if(temp.getGender() != x.getGender()){
                                    True = true;
                                }
                            }
                        }
                    }
                }
            }
            if (True){ //if there is something next to it, it send the chosen temp and the found temp to the create child function
                PredatorCreateChild(temp , x);
                break;
            }
        }
    }


    private void CreateChild(Prey Partner , Prey Main){ //Creates Child using chances if a partner is found
        if (Partner!=null){
            int PartnerChance = 100 - Partner.getReproduce(); //partenrs chance to have a child
            int MainChance = 100 - Main.getReproduce(); //Chosen Prey's chance to reproduce
            int Chance = (int)Math.floor(Math.random()*101); //12%-24% Chance
            int Compare = (PartnerChance*MainChance)/80;
            if (Compare <= 1){
                Compare = 1;
            }
            if (Partner.getAge() >=5 && Main.getAge()>=5){ //if the ages of both Partner, and Main is greater than 10, it is old enough
                if (Chance <= Compare){ //chance is out of 500 for a child
                    PreyList.add(new Prey(gameGrid)); //creates the child
                    //EVOLUTION
                    int Avespeed = (Partner.getSpeed() + Main.getSpeed())/2 ;
                    int Aveintell = (Partner.getIntell() + Main.getIntell())/2;
                    int Avereproduce = (Partner.getReproduce() + Main.getReproduce())/2;
                    int Avesurvival = (Partner.getSurvival() + Main.getSurvival())/2;
                    PreyList.get(PreyList.size()-1).ParentsValues(Avespeed,Aveintell,Avereproduce,Avesurvival);
                    NewsLST.getItems().add(0,"Congratulations, Lil Prey was born");
                }
            }
        }
    }

    private void PredatorCreateChild(Predator Partner , Predator Main){ //Creates Child using chances if a partner is found
        int PartnerChance = 100 - 60; //partenrs chance to have a child
        int MainChance = 100 - 60; //Chosen Prey's chance to reproduce
        int Chance = (int)Math.floor(Math.random()*101);
        int Compare = (PartnerChance*MainChance)/100; //16% chance
        if (Compare <= 1){
            Compare = 1;
        }
        if (Partner.getAge() >=5 && Main.getAge()>=5){ //if the ages of both Partner, and Main is greater than 15, it is old enough
            if (Chance <= Compare){ //chance is out of 500 for a child
                PredatorList.add(new Predator(gameGrid, NewsLST)); //creates the child
                int x = Main.widthx();
                int y = Main.heigty()-1;
                PredatorList.get(PredatorList.size()-1).ParentLocationBirth(x, y);
                NewsLST.getItems().add(0,"Congratulations, Lil Predator was born");
            }
        }
    }


    private Prey FindPrey(int preyx, int preyy){ //finds the object of the Prey located
        if (PreyList.size() !=0){
            Prey A = null;//finds the object of the wanted Prey by its location in the Grid.
            for(Prey a: PreyList){
                if (a.getHeigty() == preyy && a.getWidthx() == preyx){
                    A = a;
                    break;
                }
            }
            return A;
        }
        else {
            return null;
        }
    } //Finds the prey by location

    private Predator FindPredator(int predx, int predy){ //finds the object of the Predator located
        if (PredatorList.size() !=0){
            Predator A = null; //finds the object of the wanted Predator by its location in the Grid.
            for(Predator a: PredatorList){
                if (a.heigty() == predy && a.widthx() == predx){
                    A = a;
                    break;
                }
            }
            return A;
        }
        else{
            return null;
        }
    } //Finds the predator by location

    private void KillAge(){ //natural death, age death
        for(int i = 0; i<PredatorList.size(); i++){
            int chance = (int)Math.floor(Math.random()*300);
            boolean Kill = false;
            int tempy = PredatorList.get(i).heigty();
            int tempx = PredatorList.get(i).widthx();
            int age = PredatorList.get(i).getAge();
            if (age >=50){
                if (chance<=16){
                    Kill = true;
                }
            }
            else if (age >= 40){
                if (chance<= 12){
                    Kill = true;
                }
            }
            else if(age >= 30){
                if (chance<6){
                    Kill = true;
                }
            }
            else if (age >=25){
                if (chance<4){
                    Kill = true;
                }
            }
            else if (age >= 15){ //teens, invincible :)
                if (chance<2){
                    Kill = true;
                }
            }
            else{
                if (chance<=10){
                    Kill = true;
                }
            }




            if(Kill){ //If it will kill then this happens
                NewsLST.getItems().add(0,"Natural Death was called upon this Predator");
                gameGrid[tempx][tempy] = 0;
                PredatorList.remove(i);
            }

        }

        for (int i = 0; i<PreyList.size(); i++){
            int chance = (int)Math.floor(Math.random()*300);
            boolean Kill = false;
            int tempy = PreyList.get(i).getHeigty();
            int tempx = PreyList.get(i).getWidthx();
            int age = PreyList.get(i).getAge();
            if (age >=50){
                if (chance<=16){
                    Kill = true;
                }
            }
            else if (age >= 40){
                if (chance<= 12){
                    Kill = true;
                }
            }
            else if(age >= 30){
                if (chance<6){
                    Kill = true;
                }
            }
            else if (age >=25){
                if (chance<4){
                    Kill = true;
                }
            }
            else if (age >= 15){ //teens, invincible ;)
                if (chance<2){
                    Kill = true;
                }
            }
            else{
                if (chance<=10){
                    Kill = true;
                }
            }
            if(Kill){
                NewsLST.getItems().add(0,"Natural Death was called upon this Prey");
                gameGrid[tempx][tempy] = 0;
                if (ChasingRN == PreyList.get(i)){
                    PredatorList.get(0).TargetFoundFalse();
                }
                PreyList.remove(i);
            }
        }
    }

    private void detectPredator(){ //detects predator in a radius, the prey finds the predator to use the intell skill of the prey, but the prey object is sent into each predator to hunt down the prey.
        //every time a prey dies??
        int chance = (int)Math.floor(Math.random()*10);
        Predator temp = null;
        boolean track = false;
        //if (chance <= 3){
            Gameloop:
            for (Prey x: PreyList){
                for(int i = -x.getIntell(); i<=x.getIntell(); i++){
                    for(int j = -x.getIntell(); j<=x.getIntell(); j++){
                        int tempx =x.getWidthx();
                        int tempy =x.getHeigty();
                        if (tempx+i<20 && tempx+i>=0 && tempy+j<30 && tempy+j>=0){
                            if(gameGrid[tempx+i][tempy + j] == 2){
                                temp = FindPredator(tempx+i,tempy+j);
                                track = true;
                            }
                        }
                        if (track == true){
                            //code track the prey here, assume predator is found
                            CurrentDetection = true;
                            NewsLST.getItems().add(0,"Lil Prey getting Hunted. Tough");
                            ChasePrey(x);
                            break Gameloop;
                        }
                    }
                }//for loops
            }//for each loop
        //}
    }

    private void ChasePrey(Prey Jerry){ //sends the prey object for each Predator to target
        if(CurrentDetection){
            for (Predator Tom: PredatorList) {
                Tom.Target(Jerry);
                ChasingRN = Jerry;
                CurrentDetection = false;
            }
        }
    }

    private void Checkiffound(){ //If any of the chasing predators are not chasing anymore. they all stop chasing,
        Boolean found = true;
        for (Predator x: PredatorList) {
            if (x.isTargetFound() == false){
                found = false;
            }
        }
        if (found == false){
            for (Predator x: PredatorList) {
                x.TargetFoundFalse();
            }
        }
    }

    private void StartSmallG(){ //starts the game off ith just one place that has water
        gameGrid[0][0] = 3;//left top
    }

    private void SmallerGameG(){ //CONSTRICTS THE GAME GRID
//            for (int i =18; i>= 0; i--){
//                for (int j =28; j>= 0; j--){
//                    if (gameGrid[i][j] == 3){
//                        gameGrid[i+1][j+1] =3;
//                        gameGrid[i][j+1] =3;
//                        gameGrid[i+1][j] =3;
//                    }
//                }
//            }

        for (int tempcounter = counter; tempcounter >=0; tempcounter--){
            int i = 0;
            if (tempcounter >= 29){
            i += (tempcounter-29);
        }
        for (int j=tempcounter; j>0; j--){
            for (i = i; i<20; i++){
                if (j-i >=0){
                    boolean end = false;
                    if (i == 19 && j-i == 29){
                        i = 18;
                        j = 46;
                        end = true;
                    }
                    Prey x = FindPrey(i,j-i);
                    Predator z = FindPredator(i,j-i);

                    if (x != null){
                        PreyList.remove(x);
                    }
                    else if(z!=null){
                        PredatorList.remove(z);
                    }
                    if (end == true){ //Last Spot stays
                        NewsLST.getItems().add(0,"Last Spot Hopefully Humans Stop Using CO2");
                        i = 19;
                        j = 47;
                        MessageAdd();
                    }
                    gameGrid[i][j-i] = 3;
                }
            }
        }
        }
    }

    private void Hungercheck(){ //Checks how hungry the Predator is and kills it if its very hungry, or overstuffed
        for (int i = 0; i < PredatorList.size(); i++) {
            if (PredatorList.get(i) != null){
                int hunger = PredatorList.get(i).getHunger();
                if (hunger == 0){
                    gameGrid[PredatorList.get(i).widthx()][PredatorList.get(i).heigty()] = 0;
                    PredatorList.remove(PredatorList.get(i));
                    NewsLST.getItems().add(0,"Predator Died from Hunger");
                }
//                else if(hunger ==8){ //death from eating too much
//                    gameGrid[PredatorList.get(i).widthx()][PredatorList.get(i).heigty()] = 0;
//                    PredatorList.remove(PredatorList.get(i));
//                    NewsLST.getItems().add(0,"Predator Died from OverStuffing, Something Exploded");
//                }
            }
        }
    }


    private void Stats(){ //Puts the amount of Prey and Predators into the list that says the size
        StatsLST.getItems().clear();
        StatsLST.getItems().add("Prey = " +PreyList.size());
        StatsLST.getItems().add("Predators = "+ PredatorList.size());
    }

    private void MessageAdd(){ //adds inspiring message at the end of the game because its a "Climate Change" Game
        if (gameGrid[19][29] == 0){
            Message.setText("No more animals huh?!, it makes sense Global Warming killing all of Them lets try not to make this into a REALITY");
        }
        else if (gameGrid[19][29] == 1){
            Message.setText("Nice one Prey survived, What's it gonna eat tomorrow, who's gonna carry his species's legacy, let's try not to make this into a REALITY");
        }
        else if (gameGrid[19][29] == 2){
            Message.setText("Nice one Predator survived, What's it gonna eat tomorrow, who's gonna carry his species's legacy, let's try not to make this into a REALITY");
        }
    }


}