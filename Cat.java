import java.awt.*; //graphics
import java.util.LinkedList; //linked list
import java.util.Random; //random numbers

/***********Class Summary
 * This class is a parent class of the Customer and Player classes.
 * Both the customers and the player have a string name value to represent their png representation,
 * and they have an x and y position.
 * They have methods to draw them on the screen, and to check if the user has clicked on them in the window.
 *************/
public class Cat{
    public String Cat_name; //Cat's name for file-finding purposes
    public int Cat_x; //position x
    public int Cat_y; //position y

    public Cat(int Cat_x, int Cat_y) { //give each cat an x and y position when they are created
        this.Cat_x = Cat_x;
        this.Cat_y = Cat_y;
    }

    /*********** Method Summary
     * This method obtains the cat's name to retrieve the file path from the src folder.
     * It also accesses the cat's x and y positions to draw the cat image on the screen
     *************/
    public void Cat_drawCat(Graphics g){ //find which png to draw based on cat's name
        Cat_showImage(g, "Images/" +Cat_name+ ".png", Cat_x, Cat_y);
    }

    /*********** Method Summary
     *This method is a helper method fot the Cat_drawCat method so that we do not have to give as many
     * arguments in Cat_drawCat when the above method is called on an object in external classes.
     * It finds the image from the filepath, and draws it on the cat's x and y positions.
     * It is also used in the Customer class to draw the customer's desired order, which
     * will reduce repeated code.
     *************/
    public static void Cat_showImage(Graphics g, String filePath, int Image_x, int Image_y){ //get png file and draw it
        Toolkit t=Toolkit.getDefaultToolkit();
        Image i= t.getImage(filePath); //this is the file path
        g.drawImage(i, Image_x,Image_y,null);
    }

    /***********  Method Summary
     *This method checks if the user clicks within the cat's hitbox.
     * x and y are the mouse positions, and xmin,xmax,ymin,ymax define the cat's hitbox.
     * If x and y are within the hitbox, the method returns true.
     *************/
    public boolean Cat_checkHitBox(int Mouse_x, int Mouse_y, int xmin, int xmax, int ymin, int ymax){ //check if the player clicked on the cat
        return (Mouse_x >xmin && Mouse_x < xmax && Mouse_y > ymin && Mouse_y < ymax);
    }
}

/***********  Class Summary
 * This class inherits variables and methods from the Cat class.
 * A customer has a desired order and a name, and their order is not fulfilled until the user gives them
 * a completed order.
 * The customer's name and desired order are randomized so that the player cannot memorize which cat
 * wants which order.
 *************/
class Cat_Customer extends Cat{ //cat customers
    //inherit parent variables (do not overwrite)
    public String Customer_desiredOrder; //string value of what the customer wants
    public Boolean Customer_isOrderFilled; //check if the player is attempting to fulfill their order
    private final String[] Customer_PossibleNames = {"NyanCat","Cheshire","HelloKitty","FatGarfield","Pikachu","PinkPanther","Salem"}; //array of possible names that a cat can have (must have a png file in src folder)
    private final String[] Customer_PossibleOrders = {"Potion","Coffee","Seltzer","Pancakes", "VanillaDonut", "StrawberryDonut"}; //array of possible orders that a cat can have (must have a png file in src folder)
    private Random Customer_randIndex; //create a random object to select random names and orders
    public long Customer_startTime; //the time at which the customer entered the restaurant
    public long Customer_endTime; //the time at which the customer's order was completed

    /*********** Constructor Summary
     * When a customer is created, they are given an x and y position.
     * A random name and desired order are given to them, and their order
     * has not been fulfilled by the player yet
     * The time at which they enter is the current system time in nanoseconds
     *************/
    public Cat_Customer(int Customer_x, int Customer_y) {
        super(Customer_x,Customer_y); //inherit from parent constructor
        Customer_randIndex = new Random(); //create a random object to select random names and orders
        Customer_isOrderFilled = false; //when a customer is created, their order is not fulfilled yet
        Customer_giveRandomName(); //randomize the cat's graphics
        Customer_giveRandomOrder(); //randomize the cat's order
        Customer_startTime = System.nanoTime(); //The time at which they enter is the current system time in nanoseconds
    }

    /*********** Method Summary
     * This is a helper method to give the customer a random name
     * A random integer is generated from 0 to 6, and that integer is used
     * to access an index in the Customer_PossibleNames array.
     * The customer's name is then set to that randomized value.
     *************/
    private void Customer_giveRandomName(){ //randomize the customer's name
        int randomName = Customer_randIndex.nextInt(7); //pick an index from possible names: index 0 to 6
        Cat_name = Customer_PossibleNames[randomName]; //make cat's name that random name
    }

    /*********** Method Summary
     * This is a helper method to give the customer a random order
     * A random integer is generated from 0 to 5, and that integer is used
     * to access an index in the Customer_PossibleOrders array.
     * The customer's order is then set to that randomized value.
     *************/
    private void Customer_giveRandomOrder(){
        int randomOrder = Customer_randIndex.nextInt(6); //pick an index from possible orders: index 0 to 5
        Customer_desiredOrder = Customer_PossibleOrders[randomOrder]; //make cat's order that random order
    }

    /*********** Method Summary
     * This method obtains the cat's desired order to retrieve the file path from the src folder.
     * It also accesses the cat's x and y positions to draw the ingredient image on the screen.
     *************/
    public void Customer_drawOrder(Graphics g){
        Cat_showImage(g, "Images/" +Customer_desiredOrder+ ".png", Cat_x+15, Cat_y+35);
    }

}

/***********  Class Summary
 *This class inherits variables and methods from the Cat class.
 * It represents the player, who initially has 9 lives and a score of 0, and adds the ingredients that
 * the player clicks on to a list of strings so that the program knows what the player has clicked on.
 * The player also has a string value to represent the current order it has created so that
 * it can be easily compared with a customer's desired order string value.
 ***********/

class Cat_Player extends Cat{ //this class represents the player
    //inherit variables from Cat (do not overwrite)
    int Player_lives; //how many lives they have
    LinkedList<String> Player_AddedIngredients; //which ingredients the player has selected
    String Player_PreparedOrder; //the string value of the ingredients the player has selected
    int Player_score; //the player's score

    /***********  Constructor Summary
     * When a player is created, they are given an x and y position.
     * Their name will always be Player, and they initially have 9 lives and a score of 0.
     * Their current prepared order is an empty screen, and they have not added
     * ingredients to the added ingredients list yet.
     ***********/
    public Cat_Player(int Player_x, int Player_y, String name){
        super(Player_x,Player_y); //inherit constructor
        this.Cat_name = name; //set the player's name
        Player_lives = 9; //the player has 9 lives initially
        Player_score = 0; //the player has a score of 0 initially
        Player_PreparedOrder = ""; //the player has not prepared an order initially
        Player_AddedIngredients = new LinkedList<String>(); //create an empty list of the ingredients the player has added
    }

    /***********  Method Summary
     * This method is called whenever the player makes a mistake.
     * It subtracts 1 from the player's lives.
     * If the player does not have any lives left, the game is over and the program exits.
     ***********/
    public void Player_SubtractLife(){ //if the player gets an order wrong, they lose a life
        Player_lives--;
        if(Player_lives==0){
            System.err.println("Game Over: no lives left. Your final score is " + Player_score); //if player has no lives left, the game ends
            System.exit(1);
        }
    }

    /** Method Summary
     *This method is called whenever the player gives clicks on an order to give an order to them.
     * The list of added ingredients is cleared, and the string value of the order the player has created
     * is now empty.
     */
    public void Player_updateStatus(){ //clear the ingredients that the player has added, and make the string value empty
        Player_AddedIngredients.clear();
        Player_PreparedOrder = "";
    }
}



