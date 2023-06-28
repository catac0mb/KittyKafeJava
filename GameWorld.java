import java.awt.*; //graphics
import java.util.ArrayList; //can now use built-in Array lists
import java.util.concurrent.TimeUnit; //find the current system time

/*********** Class Summary
 * This class handles updating the status of the customers in the customer list, updating the player's status,
 * and comparing player variables with customer variables.
 * It implements the Music interface so that different sound effects can play based on the player-customer
 * comparisons.
 *************/
class GameWorld implements Music{
    public Cat_Player GameWorld_player; //this represents the player
    public ArrayList<Cat_Customer> GameWorld_customerLists; //this represents the current customers
    public Boolean GameWorld_NerdMode; //this controls the game-mode

    /*********** Constructor Summary
     *When a new world is created, a new list of customers is created, two customers are added to it,
     * and a new player is created.
     *************/
    public GameWorld (){
        GameWorld_customerLists = new ArrayList<Cat_Customer>(); //create a list of customers
        GameWorld_customerLists.add(new Cat_Customer(400,200)); //add a customer
        GameWorld_customerLists.add(new Cat_Customer(600,200)); //add a customer
        GameWorld_player = new Cat_Player(88,84,"Player"); //make player object
        GameWorld_NerdMode = false; //by default, the game is not on nerd mode
    }

    /*********** Method Summary
     * This method enables another game mode, where every customer has the same name and order.
     *************/
    public void GameWorld_enableNerdMode(int index){
        GameWorld_customerLists.get(index).Customer_desiredOrder = "Seltzer";
        GameWorld_customerLists.get(index).Cat_name = "GrandpawMittens";
    }

    /*********** Method Summary
     * This method checks if the player clicks on a customer by checking if the customer clicked within the
     * customer's hitbox, and then compares the player's prepared order with the customer's desired order.
     *************/
    public void GameWorld_clickAcustomer(int Mouse_x, int Mouse_y, int Customer_index){ //check if the player clicked within a customer's hitbox
        if(GameWorld_customerLists.get(Customer_index).Cat_checkHitBox(Mouse_x,Mouse_y,
                GameWorld_customerLists.get(Customer_index).Cat_x,GameWorld_customerLists.get(Customer_index).Cat_x+140,
                GameWorld_customerLists.get(Customer_index).Cat_y,GameWorld_customerLists.get(Customer_index).Cat_y+250)){
            GameWorld_checkPlayerPreparedOrder(Customer_index); //if you click on a customer, check if the player's prepared order is correct
        }
    }

    /*********** Method Summary
     * This method is called when a player clicks on a customer.
     * The player's prepared order is compared with the cat's desired order, and the method enters an if
     * statement depending on if the orders match or not.
     * If they match, then the player's added ingredients become empty, the customer is removed from the list,
     * and a new customer is added to the customer list.
     * If they do not match, then the method checks if the player has any lives left. If there is at least
     * two lives left, it subtracts a life, and the player's added ingredients become empty. If there is one
     * life left, then the player loses their last life and the program exits.
     *************/
    private void GameWorld_checkPlayerPreparedOrder(int Customer_index) {
        boolean isOrderCorrect = GameWorld_compareOrder(Customer_index); //check if the player's prepared ingredients are equal to what the customer orders
        if (isOrderCorrect) { //if the player gets it correct:
            Music.Music_playSound("sounds/Meow.wav"); //play happy sound effect
            System.out.println("Order is correct"); //tell player they got it correct
            GameWorld_player.Player_updateStatus(); //update the player's list of ingredients (which is now empty because the player gave it to a customer)
            GameWorld_updateCustomerStatus(Customer_index); //remove the customer that the player clicked on, add another customer, and update the customer indexes
        }
        if (!isOrderCorrect) {//if the player gets it wrong:
            Music.Music_playSound("sounds/Hiss.wav");//play angry sound effect
            GameWorld_player.Player_SubtractLife(); //remove a life from the player, if they now have 0, immediately exit the program
            //executes if the player still has lives left
            System.out.println("Order is wrong. Try again");
            GameWorld_player.Player_updateStatus();  //update the player's list of ingredients (which is now empty because the player gave it to a customer)
        }
    }
    /*********** Method Summary
     * This is a helper method for the GameWorld_checkPlayerPreparedOrder method in order to help with organization.
     * It obtains the index of the customer in the list that was clicked on, and checks if its desired order
     * matches with the player's prepared order.
     *************/
    private boolean GameWorld_compareOrder(int Customer_index){ //check if the player got the order correct
        return (GameWorld_customerLists.get(Customer_index).Customer_desiredOrder.equals(this.GameWorld_player.Player_PreparedOrder));
    }

    /*********** Method Summary
     * This is a helper method for the GameWorld_checkPlayerPreparedOrder method in order to help with organization.
     * It obtains the index of the customer in the list that was clicked on, removes that customer from the list,
     * and adds a new customer to the list.
     *************/
    private void GameWorld_updateCustomerStatus(int Customer_index){ //change the cats in line
        GameWorld_calculateScore(Customer_index); //calculate the player's score
        GameWorld_customerLists.remove(Customer_index); //remove the cat that the player clicked on
        GameWorld_customerLists.add(new Cat_Customer(600, 200)); //add a new customer to the end of the line
        if(GameWorld_NerdMode) { //if nerd mode is enabled
            GameWorld_enableNerdMode(0); //make new customers have same name and order
            GameWorld_enableNerdMode(1);
        }
        GameWorld_customerLists.get(0).Cat_x = 400; //change the cat's position so that the player can see who is first in line
        GameWorld_customerLists.get(1).Cat_x = 600;
    }

    /*********** Method Summary
     * This calculates the player's current score.
     * It takes the elapsed time between when the customer first arrived and when their order was fulfilled.
     * If the elapsed time is smaller, the player is given more points.
     * If the elapsed time is bigger, the player is given fewer points.
     * If the elapsed time is too big, the player is given no points.
     *************/
    public void GameWorld_calculateScore(int index) { //calculate the player's current score
        GameWorld_customerLists.get(index).Customer_endTime = System.nanoTime(); //the customer's end time is the system's current time in nanoseconds
        long elapsedTime = GameWorld_customerLists.get(index).Customer_endTime - GameWorld_customerLists.get(index).Customer_startTime; //calculate waiting time
        long elapsedTimeInSeconds = TimeUnit.SECONDS.convert(elapsedTime,TimeUnit.NANOSECONDS); //convert elapsedTime to seconds
        System.out.print("It took you " + elapsedTimeInSeconds + " seconds to fulfill the order. ");

        if(elapsedTimeInSeconds >= 0 && elapsedTimeInSeconds < 10){GameWorld_player.Player_score += 5;} //if the time falls between these values, add this amount of points to the player's score
        else if(elapsedTimeInSeconds >= 10 && elapsedTimeInSeconds <25){GameWorld_player.Player_score += 1;}
        else{GameWorld_player.Player_score += 0;}

        System.out.println("Your current score is: " + GameWorld_player.Player_score);
    }

    /*********** Method Summary
     * This method draws each customer in the list of customers, and draws each customer's order
     *************/
    public void GameWorld_drawCustomers(Graphics g){ //draw all the customers in the list of customers
        for(Cat_Customer c : GameWorld_customerLists){
            c.Cat_drawCat(g);
            c.Customer_drawOrder(g);
        }
    }
}