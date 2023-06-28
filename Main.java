/***********
 * Commandline is having issues finding the file path for images in the src folder
 * Program runs fine from IDE
 * Currently working on issue
 *************/
import javax.swing.*; //jpanel
import java.awt.*; //graphics
import java.awt.event.KeyListener; //see if keys are pressed
import java.awt.event.KeyEvent; //see what keys are pressed
import java.awt.event.MouseListener; //see if user does something with mouse
import java.awt.event.MouseEvent; //see what user does with mouse
import java.io.File; //check necessary files
import java.io.FileNotFoundException;  //check necessary files
import java.io.PrintWriter; //create files to scan
import java.util.Scanner; //scan files

/*********** Class Summary
 * This class handles changes to graphics and sound during run time through user input (mouse and keyboard).
 * It implements the KeyListener and MouseListener interfaces to receive user input.
 * It implements the user-defined Music interface to play background music and sound effects during run time,
 * and is influenced by user input.
 *************/
public class Main extends JPanel implements KeyListener, MouseListener,Music {
    private static final int Main_WindowWIDTH = 900;     //width of screen
    private static final int Main_WindowHEIGHT = 400;    //height of screen
    private static final int Main_WindowFPS = 60;    //frames per second
    private GameWorld Main_world;    //gain access to gameWorld so that user input can affect the customers and the player status during run time
    private String Main_background;     //background png
    private long Main_timeSaved; //the last time the game was saved by the user

    /*********** Class Summary
     * The runner class handles repainting the background, customers, player, and ingredients in an infinite loop.
     * The game will stop when the user presses 'e' to exit the program.
     *************/
    class Runner implements Runnable{   //handles repainting
        public void run() {
            while (true) {
                repaint();
                try {
                    Thread.sleep(1000 / Main_WindowFPS);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    /*********** Constructor Summary
     * The Main constructor makes it possible to access the GameWorld, which updates the customers' and the
     * player's status as the game runs.
     * As the user presses the mouse or the keyboard, changes to the GameWorld will be made.
     * When the Main instance is first created, the background will be the start screen, and the infinite
     * repainting loop will begin. Music will start to play in the background as well.
     *************/
    public Main(){
        Main_world = new GameWorld();
        addMouseListener(this);  //can now listen to mouse
        addKeyListener(this);   //can now listen to keyboard
        Main_background = "StartScreen";    //start screen is the first background when game starts
        this.setPreferredSize(new Dimension(Main_WindowWIDTH, Main_WindowHEIGHT));    //set window size
        Thread mainThread = new Thread(new Main.Runner());  //make new thread that handles running indefinitely
        mainThread.start();     //start the infinite loop
        Music.Music_playBackground("sounds/Music.wav"); //make music play in the background
    }

    /*********** Method Summary
     * The code below checks if the user has every file we require to run the game.
     * I created an array with each of the files we use for the game, and run each item through a loop to
     * check if the file exists in the src folder using the file paths.
     *************/
    private static void Main_checkFilesExist(){
        File[] PossibleFiles= {
                new File ("Images/Cheshire.png"), new File ("Images/Coffee.png"), new File ("Images/FatGarfield.png"), new File ("Images/GrandpawMittens.png"),
                new File ("Images/HelloKitty.png"), new File ("Images/KitchenCounter.png"), new File ("Images/NyanCat.png"), new File ("Images/Pancakes.png"),
                new File ("Images/Pikachu.png"), new File ("Images/PinkPanther.png"), new File ("Images/Player.png"), new File ("Images/Potion.png"),
                new File ("Images/Register.png"), new File ("Images/Salem.png"), new File ("Images/Seltzer.png"), new File ("Images/StartScreen.png"),
                new File ("Images/StrawberryDonut.png"), new File ("Images/Tutorial1.png"), new File ("Images/Tutorial2.png"), new File ("Images/Tutorial3.png"),
                new File ("Images/VanillaDonut.png"), new File ("sounds/Hiss.wav"), new File ("sounds/Meow.wav"), new File ("sounds/Music.wav"),
                new File ("sounds/Plop.wav"), new File ("sounds/Pour.wav")}; //create an array with all of the necessary files

        for (File f : PossibleFiles){
            if (!f.exists()){
                System.out.println("ERROR: at least one file cannot be found. Check that " + f + " is in Images or sounds, and that you are in the src directory");
                System.exit(1);
            }// if a necessary file cannot be found, exit the program and inform the user of the first file they are missing
        }
        //End of check, program may run
    }

    /*********** Method Summary
     * This is the main method, which creates a Main instance with the above initial qualities.
     * A JFrame is created, which will show the window containing the graphics.
     * Dragging is disabled so that the x and y positions of the mouse will not be changed if the user
     * attempts to move the window (this was a bug during programming, and we implemented other ways to
     * close the window without relying on the x button on a decorated JFrame).
     *************/
    public static void main (String[] args){
        Main_checkFilesExist(); //check for necessary files
        Main mainInstance = new Main(); //make main object
        JFrame frame = new JFrame("GameTesting");   //make new window
        frame.setUndecorated(true);     //disable dragging
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(mainInstance);
        frame.pack();
        frame.setVisible(true);     //can now see window
    }

    /*********** Method Summary
     * This method overrides the paintComponent method from the JPanel class.
     * Depending on the current state of the game (which screen the player is currently viewing), the method
     * will draw a different background, will draw the customers, and/or will draw the player's added ingredients
     *************/
    @Override
    public void paintComponent(Graphics g) {    //draw everything on the screen
        super.paintComponent(g);
        Toolkit t=Toolkit.getDefaultToolkit();  //this allows us to get images from file paths

        switch (Main_background) {
            //if the background is the startscreen, show the start screen png
            case "KitchenCounter" -> {  //if the background is the kitchen, show the kitchen png and all of the ingredients that the player is adding to an order
                Main_drawBackground(g, t, "Images/KitchenCounter.png", 230, 260); //draw the kitchen background and the player
                for (String s : Main_world.GameWorld_player.Player_AddedIngredients) { //draw the ingredients that the player has clicked
                    Main_drawIngredients(g, t, s);    //decide which image to draw based on the string values in Player_AddedIngredients
                }
            }
            case "Register" -> {    //if the background is the register, show the register background and the customers
                Main_drawBackground(g, t, "Images/Register.png", 88, 84); //draw the register background and the player
                Main_world.GameWorld_drawCustomers(g);  //draw the current customers
            }
            case "StartScreen" -> Main_showImage(g, t, "Images/StartScreen.png", 0, 0);     //draw the startscreen background
            case "Tutorial1" -> Main_showImage(g, t, "Images/Tutorial1.png", 0, 0); //draw the tutorial1 background
            case "Tutorial2" -> Main_showImage(g, t, "Images/Tutorial2.png", 0, 0); //draw the tutorial2 background
            case "Tutorial3" -> Main_showImage(g, t, "Images/Tutorial3.png", 0, 0); //draw the tutorial3 background
        }
    }

    /*********** Method Summary
     * This is a helper method for the paintComponent method in order to reduce repeated code.
     * A toolkit object is used to obtain images from the filepaths (images are placed in the src folder),
     * and is passed to the Main_showImage method to draw the image on the screen.
     * The player is drawn on the screen based on the Player_x and y arguments.
     *************/
    private void Main_drawBackground(Graphics g, Toolkit t, String filePath, int Player_x, int Player_y){
        Main_showImage(g,t, filePath, 0,0);
        Main_world.GameWorld_player.Cat_x = Player_x;Main_world.GameWorld_player.Cat_y = Player_y; //player coordinates
        Main_world.GameWorld_player.Cat_drawCat(g);  //draw the player
    }

    /*********** Method Summary
     * This is a helper Method for the paintComponent method in order to help with code organization.
     * A toolkit object is used to obtain images from the filepaths (images are placed in the src folder),
     * and is passed to the Main_showImage method to draw the image on the screen.
     * Ingredients are drawn on the screen depending on the string value passed as an argument.
     *************/
    private static void Main_drawIngredients(Graphics g, Toolkit t, String ingredient){
        switch (ingredient) { //check which ingredient was added, and show it on the screen
            case "Coffee" -> Main_showImage(g, t, "Images/Coffee.png", Main_WindowWIDTH / 2, 300);
            case "Potion" -> Main_showImage(g, t, "Images/Potion.png", Main_WindowWIDTH / 2 - 40, 310);
            case "Seltzer" -> Main_showImage(g, t, "Images/Seltzer.png", 30 + Main_WindowWIDTH / 2, 300);
            case "Pancakes" -> Main_showImage(g, t, "Images/Pancakes.png", Main_WindowWIDTH / 2, 330);
            case "StrawberryDonut" -> Main_showImage(g, t, "Images/StrawberryDonut.png", Main_WindowWIDTH / 2 - 10, 350);
            case "VanillaDonut" -> Main_showImage(g, t, "Images/VanillaDonut.png", 40 + Main_WindowWIDTH / 2, 330);
        }
    }

    /*********** Method Summary
     * This is a helper method for the paintComponent method and its helper methods to help cut repeated code.
     * This method uses the file path to get the image from the src folder, and then draws it.
     *************/
    private static void Main_showImage(Graphics g, Toolkit t, String filePath, int Image_x, int Image_y){ //find image in src folder and draw it with appropriate dimensions
        Image i= t.getImage(filePath);  //this is the file path
        g.drawImage(i, Image_x,Image_y,null);     //draw the image
    }

    /*********** Method Summary
     * This method overrides the mousePressed method from the MouseListener interface.
     * It detects where the player clicks on the screen, and then decides whether to change the area of the
     * game that the player sees, whether to add ingredients to the player's prepared order, and whether to
     * check if the player clicks on a customer (so that the game can check if the player's prepared order
     * is equal to what the customer's desired order is).
     * If the player clicks on the player cat, then the player's current score and remaining lives will be printed to the terminal.
     *************/
    @Override
    public void mousePressed(MouseEvent e){
        PointerInfo a = MouseInfo.getPointerInfo();     //create a PointerInfo object to detect where the player clicked on the screen
        Point b = a.getLocation();      //returns mouse location as a point object
        int Mouse_x = (int) b.getX();     //gets x position of mouse click
        int Mouse_y = (int) b.getY();     //gets y position of mouse click

        switch(Main_background){    //the game will check where the player clicks depending on the current background

            case "KitchenCounter":  //if the player is currently seeing the kitchen screen
                if(Mouse_x < 880 && Mouse_x > 670 && Mouse_y<100 && Mouse_y > 0){       //if you click the register button, go to the register screen
                    Main_background = "Register";   //background is now register
                    return;}
                else if(Main_world.GameWorld_player.Cat_checkHitBox(Mouse_x,Mouse_y,Main_world.GameWorld_player.Cat_x,Main_world.GameWorld_player.Cat_x+130,Main_world.GameWorld_player.Cat_y,Main_world.GameWorld_player.Cat_y+130)){   //if you click on the player, print out the player's lives
                    System.out.println("You have " + Main_world.GameWorld_player.Player_lives + " lives left.");
                    System.out.println("You have a score of " + Main_world.GameWorld_player.Player_score + ".");
                    return;}
                Main_ClickIngredient(Mouse_x,Mouse_y);    //if the player wants to add an ingredient, check where the player clicks on the screen and add the appropriate ingredient to the player's ingredient list
                return;

            case "Register":    //if the player is currently seeing the register screen
                if(Mouse_x < 880 && Mouse_x > 670 && Mouse_y<100 && Mouse_y > 0){   //if you click the kitchen button, go to the kitchen screen
                    Main_background = "KitchenCounter";
                    return;}
                else if(Main_world.GameWorld_player.Cat_checkHitBox(Mouse_x,Mouse_y, Main_world.GameWorld_player.Cat_x,Main_world.GameWorld_player.Cat_x+130, Main_world.GameWorld_player.Cat_y,Main_world.GameWorld_player.Cat_y+130)){   //if you click on the player, print out the player's remaining lives
                    System.out.println("You have " + Main_world.GameWorld_player.Player_lives + " lives left.");
                    System.out.println("You have a score of " + Main_world.GameWorld_player.Player_score + ".");
                    return;}
                Main_world.GameWorld_clickAcustomer(Mouse_x,Mouse_y,0);     //check which customer the player clicks on, and then see if the player's prepared order is correct
                Main_world.GameWorld_clickAcustomer(Mouse_x,Mouse_y,1);
                return;

            case "StartScreen":     //if the player is currently seeing the start screen
                if(Mouse_x < 320 && Mouse_x > 60 && Mouse_y<350 && Mouse_y > 270){  //if you click the start button, go to the register screen
                    Main_world = new GameWorld(); //restart the game
                    Main_background = "Register";   //background is now register
                    return;}
                else if(Mouse_x < 840 && Mouse_x > 598 && Mouse_y<350 && Mouse_y > 260){    //if you click the start button, go to the register screen
                    Main_startNerdMode(); //enable NerdMode
                    Main_background = "Register";   //background is now register
                    return;}
                else if(Mouse_x < 560 && Mouse_x > 340 && Mouse_y<350 && Mouse_y > 270){    //if you click the start button, go to the register screen
                    Main_background = "Tutorial1";   //background is now tutorial
                    return;}
                return;
        }
        Main_PlayTutorial(Mouse_x, Mouse_y); // if the current state of the game is the tutorial, then check that the user clicks the buttons in the tutorial
    }

    /*********** Method Summary
     * This method is a helper method for MousePressed to make code more organized.
     * It detects if the player selects NerdMode, and makes a new game with the NerdMode aspects.
     *************/

    private void Main_startNerdMode(){
        Main_world = new GameWorld(); //restart the game
        Main_world.GameWorld_NerdMode = true; //enable nerd mode
        Main_world.GameWorld_enableNerdMode(0); //convert the customers to nerds
        Main_world.GameWorld_enableNerdMode(1);
    }

    /*********** Method Summary
     * This is a helper method for the above mousePressed Method for better code organization.
     * If the player wants to add an ingredient, check where the player clicks on the screen and add the appropriate ingredient
     * to the player's ingredient list.
     *************/
     private void Main_ClickIngredient(int Mouse_x,int Mouse_y){
         //if the following ingredient was clicked, add it to the added ingredients list
        Main_checkIngredientHitBox(Mouse_x,Mouse_y,300,70,235,145,"Coffee");
        Main_checkIngredientHitBox(Mouse_x,Mouse_y,160,100,140,80,"Potion");
        Main_checkIngredientHitBox(Mouse_x,Mouse_y,250,190,140,65,"Seltzer");
        Main_checkIngredientHitBox(Mouse_x,Mouse_y,120,18,380,295,"Pancakes");
        Main_checkIngredientHitBox(Mouse_x,Mouse_y,860,710,250,180,"VanillaDonut");
        Main_checkIngredientHitBox(Mouse_x,Mouse_y,860,710,300,265,"StrawberryDonut");
    }

    /*********** Method Summary
     * This is a helper method for the above Main_ClickIngredient method.
     * It detects of the player clicks on an ingredient. If so, then a sound effect will play based on
     * which ingredient was clicked, it will add the ingredient to the list of the player's added ingredients,
     * and it will add the ingredient's string value to the player's prepared order value.
     * The list of ingredients is used to make drawing the ingredients easier, and the string value is used
     * to make comparing its value to the customer's desired order easier.
     *************/
    private void Main_checkIngredientHitBox(int Mouse_x, int Mouse_y, int xmax, int xmin, int ymax, int ymin, String ingredient){ //check the hitbox of an ingredient
        if(Mouse_x < xmax && Mouse_x > xmin && Mouse_y<ymax && Mouse_y > ymin){
            if (ingredient.equals("Coffee") || ingredient.equals("Seltzer") || ingredient.equals("Potion")) {
                Music.Music_playSound("sounds/Pour.wav");   //play the pouring sound if the ingredient is one of the above
            }
            else if (ingredient.equals("Pancakes")|| ingredient.equals("StrawberryDonut") || ingredient.equals("VanillaDonut")) {
                Music.Music_playSound("sounds/Plop.wav");   //play the plopping sound if the ingredient is pancakes
            }
            Main_world.GameWorld_player.Player_AddedIngredients.add(ingredient); //if player clicks on the ingredient, add it to the ingredient list
            Main_world.GameWorld_player.Player_PreparedOrder += ingredient;     //add the ingredient to the player's prepared order string value
        }
    }


    /*********** Method Summary
     * This method is a helper method for MousePressed to make code more organized.
     * It detects if the player is in the tutorial state of the game, and then takes user input
     * to decide if the tutorial should move on to the next part.
     *************/

    private void Main_PlayTutorial(int Mouse_x,int Mouse_y){
        switch(Main_background){
            case "Tutorial1":
                if(Mouse_x < 540 && Mouse_x > 345 && Mouse_y<380 && Mouse_y > 295){   //if you click the okay button, go to the tutorial2 screen
                    Main_background = "Tutorial2";
                }
                return;
            case "Tutorial2":
                if(Mouse_x < 535 && Mouse_x > 350 && Mouse_y<430 && Mouse_y > 350){   //if you click the okay button, go to the tutorial3 screen
                    Main_background = "Tutorial3";
                }
                return;
            case "Tutorial3":
                if(Mouse_x < 535 && Mouse_x > 350 && Mouse_y<380 && Mouse_y > 300){   //if you click the okay button, go to the start screen
                    Main_background = "StartScreen";
                }
        }
    }

    /*********** Method Summary
     * This method overrides the keyPressed method in the KeyListener interface.
     * It detects which key the player pressed, and then decides to either exit the program, save the game,
     * load the last save, or go back to the start screen.
     *************/
    @Override
    public void keyPressed(KeyEvent e) {
        char c = e.getKeyChar();
        switch (c) {
            case 's': //save the current game state
                if( (Main_background.equals("Register") || Main_background.equals("KitchenCounter")) && Main_world.GameWorld_NerdMode==false){ quickSave("qs.txt"); }
                else{ System.out.println("You cannot save if you are not currently playing in normal game."); }
                return;
            case 'l': //load the previous save
                if((Main_background.equals("Register") || Main_background.equals("KitchenCounter")) && Main_world.GameWorld_NerdMode==false){  quickLoad("qs.txt");}
                else{ System.out.println("You cannot load last save if you are not currently playing in normal game."); }
                return;
            case 'e':   //exit program
                System.out.println("Exiting program");
                System.exit(1);
                return;
            case 't':   //go back to start screen
                    Main_background = "StartScreen";

        }
    }

    /*********** Method Summary
     * This method saves the current state of the game: the current customers, the player's score and
     * the player's remaining lives. The player's prepared order is NOT saved.
     *************/
    private void quickSave(String fileName) {
        try {
            PrintWriter writer = new PrintWriter(fileName); //make new printwriter
            System.out.print("Saving..."); //tell player it is being saved
            Main_timeSaved = System.nanoTime(); //save time saved as the current system time
            for (Cat_Customer c : Main_world.GameWorld_customerLists) { //for each customer, write their x and y positions, their name and order, and their start time
                writer.write(c.Cat_x + " ");writer.write(c.Cat_y + " ");
                writer.write(c.Cat_name + " ");writer.write(c.Customer_desiredOrder + " ");
                writer.write(c.Customer_startTime + " ");writer.write("\n");
            }
            writer.write(Main_world.GameWorld_player.Player_score + " "); //write the player score
            writer.write(Main_world.GameWorld_player.Player_lives + " "); //write the player's remaining lives
            writer.close();
            System.out.println("Saved!"); //tell user the file was saved
        }
        catch (FileNotFoundException e){
            System.out.println("Unable to write to qs.");
            System.err.println(e);
        }

    }
    /*********** Method Summary
     * This method loads the previous save's game state: the current customers, the player's score and
     * the player's remaining lives.
     *************/
    private void quickLoad(String fileName) {
        File f = new File ("qs.txt"); //provide f with the path to the previous save
        if(!f.exists()){
            System.out.println("You cannot load a game if you do not have a previous save."); //if the user did not save a previous game, then there is no file to load
            return; //return from method
        }
        //provide variables to set as the variables' values in the file
        String desiredOrder; Boolean isOrderFilled = false;
        long startTime;
        String name;int Cat_x;int Cat_y;
        int score; int lives;
        long timeLoaded = System.nanoTime(); //time that the file was loaded

        try {
            System.out.print("Loading...");
            Scanner scan = new Scanner(new File(fileName)); //create new scanner
            for (Cat_Customer c : Main_world.GameWorld_customerLists) { //for each customer, set their qualities to those in the file
                Cat_x = scan.nextInt();Cat_y = scan.nextInt();
                name = scan.next();desiredOrder = scan.next();
                startTime = scan.nextLong();
                c.Cat_x = Cat_x;c.Cat_y = Cat_y;
                c.Cat_name = name;c.Customer_desiredOrder = desiredOrder;
                c.Customer_startTime = startTime + (timeLoaded-Main_timeSaved); //add elapsed time between load and save
                c.Customer_isOrderFilled = isOrderFilled;
            }
            score = scan.nextInt(); //set score to file value
            lives = scan.nextInt(); //set lives to file value
            Main_world.GameWorld_player.Player_score = score; //set player's score to file value
            Main_world.GameWorld_player.Player_lives = lives; //set player's lives to file value

            Main_world.GameWorld_player.Player_PreparedOrder = ""; //player's prepared order is wiped
            Main_world.GameWorld_player.Player_AddedIngredients.clear(); //player's prepared order is wiped

            System.out.println("Loaded!"); //tell user it was loaded
        }
        catch (Exception e){
            System.out.println("Quickload encountered an issue.");
            System.err.println(e);
        }
    }
    public void addNotify() {    //makes a component displayable ._.
        super.addNotify();
        requestFocus();
    }

    /***********
     Methods that must be included because we are implementing interfaces. We do not want these
     methods to change the functionality of the program.
     *************/
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
    public void mouseClicked(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
}
