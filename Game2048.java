import java.lang.Math;
import java.io.*;

public class Game2048 
{
   //=== *** Provided class variables (Don't delete this section) *** ===//
   final public static int LEFT_INPUT 	= 0;
   final public static int DOWN_INPUT 	= 1;
   final public static int RIGHT_INPUT = 2;
   final public static int UP_INPUT 	= 3;
   
   final public static int VALUE_GRID_SETTING 	= 0;
   final public static int INDEX_GRID_SETTING	= 1;
   
   private String GAME_CONFIG_FILE = "game_config.txt";
   
   private Game2048GUI gui;
   
   /* position [0][0] represents the Top-Left corner and
    * position [max][max] represents the Bottom-Right corner */
   private int grid [][];
   
   //=== *** Your class variables can be added starting here *** ===//
   private final int EMPTY_SLOT = -1;
   
   private int winningValue;  
   private long currentScore;
   private int currentValue;
   private boolean shift;
   private boolean stop;
   
   /**
    * Constructs Game2048 object.
    *
    * @param gameGUI	The GUI object that will be used by this class.
    */   
   public Game2048(Game2048GUI gameGUI)
   {
      gui = gameGUI;
      	
      grid = new int [gui.NUM_ROW][gui.NUM_COLUMN];
      shift = true;
      currentScore = 0;
      currentValue = 2;
      	
      for(int i = 0; i < gui.NUM_ROW; i++){
         for(int j = 0; j < gui.NUM_COLUMN; j++){
            grid[i][j] = EMPTY_SLOT;
         }
      }
      try{
         BufferedReader in = new BufferedReader (new FileReader(GAME_CONFIG_FILE));
         in.readLine();
         winningValue = Integer.parseInt(in.readLine());
         in.close();
      }
      catch(IOException iox){
         System.out.println("Error reading file.");
      }
      newSlot();
      shift = true;
      newSlot();
   }
   
   
   /**
    * Place a new number tile on a random slot on the grid.
    * This method is called every time a key is released.
    */		
   public void newSlot()
   {
      int xCord,yCord;
      int newVal = genVal();
      boolean genFinish = false;
      if(shift == true){
         while(genFinish == false){
            xCord =(int)(Math.random()*(gui.NUM_ROW));
            yCord =(int)(Math.random()*(gui.NUM_COLUMN));
            if(grid[xCord][yCord]==EMPTY_SLOT){
               gui.setNewSlotBySlotValue(xCord,yCord,newVal);
               grid[xCord][yCord] = newVal;
               genFinish = true;
               shift = false;              
            }
         }
      }
      
      checkGameLost();
   }
   
   
   /**
    * Plays the game by the direction specified by the user.     
    * This method is called every time a button is pressed
    */		
   public void play(int direction)
   {
      if(direction == RIGHT_INPUT){
         slideRight();
      }
      else if(direction == LEFT_INPUT){
         slideLeft();
      }
      else if (direction == DOWN_INPUT){
         slideDown();
      }
      else if (direction == UP_INPUT){
         slideUp();
      }
      gui.setGridByValue(grid);
      gui.setScore(currentScore);
      checkGameWon();
   }  
   	
   private int genVal(){
      final int LOWBOUND = 1;
      final int HIGHBOUND = 100;
      final int SPAWNBOUND = 75;
      final int LIKELY_SPAWN = 2;
      final int UNLIKELY_SPAWN = 4;
      int select;
      	
      select = (int)(Math.random()*(HIGHBOUND-LOWBOUND+1))+LOWBOUND;
      	
      if(select <= SPAWNBOUND){
         return LIKELY_SPAWN;
      }
      else{
         return UNLIKELY_SPAWN;
      }
   }
   	
   private boolean slideLeft(){
   
      for(int i = 0; i < gui.NUM_ROW; i++){ 
         for(int j = 0; j < gui.NUM_COLUMN-1 ; j++)
         {
            stop = false;
            for(int k = j+1; k < gui.NUM_COLUMN && stop != true;k++){ 
               if(grid[i][k] != EMPTY_SLOT && grid[i][k] == grid[i][j]){
                  grid [i][j] = grid[i][j]*2;
                  grid[i][k] = EMPTY_SLOT; 
                  shift = true;
                  stop = true;
                  currentScore = currentScore+grid[i][j];
                  if(grid[i][j] > currentValue){
                     currentValue = grid[i][j];
                  }
               }
               else if(grid[i][k] != EMPTY_SLOT && grid[i][k] != grid[i][j]){
                  stop = true;
               }
               
            }
         } 
      } 
       
      for(int i = 0; i < gui.NUM_ROW; i++){
         for(int j = 1; j < gui.NUM_COLUMN; j++){
            stop = false;
            if(grid[i][j-1] == EMPTY_SLOT && grid[i][j] != EMPTY_SLOT){
               for(int k = j-1; k >= 0 && stop!=true;k--){ 
                  if(grid[i][k] != EMPTY_SLOT && k!=j-1){
                     grid [i][k+1] = grid[i][j];
                     grid[i][j] = EMPTY_SLOT; 
                     shift = true;
                     stop = true;
                  }
                  else if(k == 0){ 
                     grid [i][k] = grid[i][j];
                     grid[i][j] = EMPTY_SLOT;
                     shift = true; 
                     stop = true;
                  }
               }
            } 
         }
      } 
      
      
      return shift;
   }
       	
   private boolean slideRight(){
      for(int i = 0; i < gui.NUM_ROW; i++){ 
         for(int j = gui.NUM_COLUMN-1; j > 0 ; j--)
         {
            stop = false;
            for(int k = j-1; k >=0 && stop != true;k--){ 
               if(grid[i][k] != EMPTY_SLOT && grid[i][k] == grid[i][j]){
                  grid [i][j] = grid[i][j]*2;
                  grid[i][k] = EMPTY_SLOT; 
                  shift = true;
                  stop = true;
                  currentScore = currentScore+grid[i][j];
                  if(grid[i][j] > currentValue){
                     currentValue = grid[i][j];
                  }
               }
               else if(grid[i][k] != EMPTY_SLOT && grid[i][k] != grid[i][j]){
                  stop = true;
               }
               
            }
         } 
      } 
       
      for(int i = 0; i < gui.NUM_COLUMN; i++){
         for(int j = gui.NUM_COLUMN-2; j >= 0 ; j--){
            stop = false;
            if(grid[i][j+1] == EMPTY_SLOT && grid[i][j] != EMPTY_SLOT){
               for(int k = j+1; k < gui.NUM_COLUMN && stop!=true;k++){ 
                  if(grid[i][k] != EMPTY_SLOT && k!=j+1){
                     grid [i][k-1] = grid[i][j];
                     grid[i][j] = EMPTY_SLOT; 
                     shift = true;
                     stop = true;
                  }
                  else if(k == gui.NUM_COLUMN-1){ 
                     grid [i][k] = grid[i][j];
                     grid[i][j] = EMPTY_SLOT;
                     shift = true; 
                     stop = true;
                  }
               }
            } 
         }
      } 
       	
      
      
      return shift;
   }
     
   	
   private boolean slideUp(){
      for(int i = 0; i < gui.NUM_COLUMN; i++){ 
         for(int j = 0; j < gui.NUM_ROW ; j++)
         {
            stop = false;
            for(int k = j+1; k < gui.NUM_ROW && stop != true;k++){ 
               if(grid[k][i] != EMPTY_SLOT && grid[k][i] == grid[j][i]){
                  grid [j][i] = grid[j][i]*2;
                  grid[k][i] = EMPTY_SLOT; 
                  shift = true;
                  stop = true;
                  currentScore = currentScore+grid[j][i];
                  if(grid[j][i] > currentValue){
                     currentValue = grid[j][i];
                  }
               }
               else if(grid[k][i] != EMPTY_SLOT && grid[k][i] != grid[j][i]){
                  stop = true;				
               }          
            }
         } 
      } 
       
      for(int i = 0; i < gui.NUM_COLUMN; i++){
         for(int j = 1; j < gui.NUM_ROW; j++){
            stop = false;
            if(grid[j-1][i] == EMPTY_SLOT && grid[j][i] != EMPTY_SLOT){
               for(int k = j-1; k >= 0 && stop != true;k--){ 
                  if(grid[k][i] != EMPTY_SLOT && k!=j-1){
                     grid [k+1][i] = grid[j][i];
                     grid[j][i] = EMPTY_SLOT; 
                     shift = true;
                     stop = true;
                  }
                  else if(k == 0){ 
                     grid [k][i] = grid[j][i];
                     grid[j][i] = EMPTY_SLOT;
                     shift = true; 
                     stop = true;
                  }
               }
            } 
         }
      }
      
      return shift;
   }
       	
   private boolean slideDown(){
      for(int i = 0; i < gui.NUM_COLUMN; i++){ 
         for(int j = gui.NUM_ROW-1; j >= 0 ; j--)
         {
            stop = false;
            for(int k = j-1; k >= 0 && stop != true;k--){ 
               if(grid[k][i] != EMPTY_SLOT && grid[k][i] == grid[j][i]){
                  grid [j][i] = grid[j][i]*2;
                  grid[k][i] = EMPTY_SLOT; 
                  shift = true;
                  stop = true;
                  currentScore = currentScore+grid[j][i];
                  if(grid[j][i] > currentValue){
                     currentValue = grid[j][i];
                  }
               }
               else if(grid[k][i] != EMPTY_SLOT && grid[k][i] != grid[j][i]){
                  stop = true;
               }            
            }
         } 
      } 
      
       
      for(int i = 0; i < gui.NUM_COLUMN; i++){
         for(int j = gui.NUM_ROW-2; j >= 0 ; j--){
            stop = false;
            if(grid[j+1][i] == EMPTY_SLOT && grid[j][i] != EMPTY_SLOT){
               for(int k = j+1; k < gui.NUM_ROW && stop!=true;k++){ 
                  if(grid[k][i] != EMPTY_SLOT && k != j+1){
                     grid [k-1][i] = grid[j][i];
                     grid[j][i] = EMPTY_SLOT; 
                     shift = true;
                     stop = true;
                  }
                  else if(k == gui.NUM_ROW-1){ 
                     grid [k][i] = grid[j][i];
                     grid[j][i] = EMPTY_SLOT;
                     shift = true; 
                     stop = true;
                  }
               }
            } 
         }
      } 
       
      return shift;
   }
	
   private void checkGameLost(){
      boolean filled = true;
      boolean pair = false;
      stop = false;
      
      for(int i = 0; i < gui.NUM_ROW && stop!= true; i++){
         for(int j = 0; j < gui.NUM_COLUMN && stop!= true;j++){
            if(grid[i][j] == EMPTY_SLOT){
               filled = false;
               stop = true;
            }
            
            if(j!=gui.NUM_COLUMN-1 && grid[i][j] == grid[i][j+1]){
               pair = true;
               stop = true;
            }
         }
      }
      
      stop = false;
      if(pair != true && filled == true){
         for(int i = 0; i < gui.NUM_COLUMN && stop != true; i++){
            for(int j = 0; j < gui.NUM_ROW && stop != true; j++){
               if(j != gui.NUM_ROW-1 && grid[j][i] == grid[j+1][i]){
                  pair = true;
                  stop = true;
               }
            }
         }
      }
      
      if(pair == false && filled == true){
         gui.showGameOver();
      }
   }
   
   private void checkGameWon(){
      if(currentValue == winningValue){
         gui.showGameWon();
      }  
   }
}