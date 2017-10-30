package snakeGame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application{
	// Size of each block that form a snake
	public static final int BLOCKSIZE  = 40;
	// height of the application
	public static final int HEIGHT = 20 * BLOCKSIZE;
	// Width of the application
	public static final int WIDTH = 30 * BLOCKSIZE;
	
	// To display as a rectangle
	private ObservableList<Node> snakeRectangle;
	
	// Animation 
	private Timeline animation = new Timeline();
	
	// To keep track so we don't move into the same direction at the same time
	private boolean didItMove = false;
	
	// Check if the application is running
	private boolean isItRunning = false;
	
	// Using enum to set the 4 direction
	public enum snakeDirection
	{
		// Name the four direction
		LEFT,RIGHT,UP,DOWN
	}
	
	// default direction is moving right,
	private snakeDirection defaultDirection = snakeDirection.RIGHT;
	
	// Creating a primary Stage
	@Override
	public void start(Stage primaryStage) throws Exception 
	{
		// Setting a scene 
		Scene mainScene = new Scene(insidePane());
		
		// Using key for input
		mainScene.setOnKeyPressed(event -> 
		{
			// To see if it call any keyPress
			if(!didItMove)
			{
				return;
			}
			
			// getcode will get the key for moving
			switch (event.getCode())
			{
				// If a was pressed then go left
				case A:
					// checking if the snake is going right since you can't go left if the snake is already going to the right
					if (defaultDirection != snakeDirection.RIGHT)
						defaultDirection = snakeDirection.LEFT;
					break;
				// If d was pressed then go right
				case D:
					// checking if the snake is going left since you can't go right if the snake is already going to the left
					if (defaultDirection != snakeDirection.LEFT)
						defaultDirection = snakeDirection.RIGHT;
					break;
				// If w was pressed then go UP
				case W:
					// checking if the snake is going down since you can't go up if the snake is already going to the down
					if (defaultDirection != snakeDirection.DOWN)
						defaultDirection = snakeDirection.UP;
					break;
				// If s was pressed then go down
				case S:
					// checking if the snake is going up since you can't go down if the snake is already going to the up
					if (defaultDirection != snakeDirection.UP)
						defaultDirection = snakeDirection.DOWN;
					break;
			}
			
			// So you can't change the direction again
			didItMove = false;
		});

		// Give the primaryStage a title
		primaryStage.setTitle("Snake Game");
		
		// Set the mainScene to primaryStage
		primaryStage.setScene(mainScene);
		
		// Show the primaryStage
		primaryStage.show();
		
		// Start the game after gui is set
		gameStart();
	}
	
	// To stop the game
	private void gamePause()
	{
		// false since it's not running
		isItRunning = false;
		// the animation timeline will also stop
		animation.stop();
		// there wouldn't have a snake
		snakeRectangle.clear();
	}
	
	// To start the game
	private void gameStart()
	{
		// Since the snake will go right at the beginning of the game
		defaultDirection = snakeDirection.RIGHT;
		
		// creating a head
		Rectangle blockHead = new Rectangle(BLOCKSIZE, BLOCKSIZE);
		
		// add the head to the snake
		snakeRectangle.add(blockHead);
		
		// the animation will start
		animation.play();
		
		// set it to true so all the code at the top will run
		isItRunning = true;
	}
	
	// To restart the game
	private void gameRestart()
	{
		gamePause();
		gameStart();
	}
	
	
	// private since only in this file, creating a big box
	private Parent insidePane(){
		// Creating a pane
		Pane pane = new Pane();
		
		// Setting the width and height of the pane
		pane.setPrefSize(WIDTH, HEIGHT);
		
		// Need something in the pane to display
		Group insideOfSnake = new Group();
		// Get children from insideOfSnake
		snakeRectangle = insideOfSnake.getChildren();
		
		// Creating the food for the snake, make the food is same size as the snake (width,height)
		Rectangle snakeFood = new Rectangle(BLOCKSIZE, BLOCKSIZE);
		// set the color of the food to red
		snakeFood.setFill(Color.RED);
		// randomly spawn the food by setting x and y coordinate, width-blocksize so it will within the pane
		snakeFood.setTranslateX((int)(Math.random() * (WIDTH-BLOCKSIZE)) / BLOCKSIZE * BLOCKSIZE);
		// setting it to int and BLOCKSIZE * BLOCKSIZE so it will be an actual value
		snakeFood.setTranslateY((int)(Math.random() * (HEIGHT-BLOCKSIZE)) / BLOCKSIZE * BLOCKSIZE);

		// Animation, make the game harder by lowering the value of duration 
		KeyFrame snakeSpeed = new KeyFrame(Duration.seconds(0.1), event -> 
		{
			if(!isItRunning)
			{
				return;
			}
			
			// check if the is at least two block in the snake body
			boolean twoBlock = snakeRectangle.size() > 1;
			
			// if there is two block get the tail, if not remove the tail and move it to the front, so it counts as a head(can't tell in the gui)
			Node block = twoBlock ? snakeRectangle.remove(snakeRectangle.size()-1) : snakeRectangle.get(0);
			
			// If the snake eat the food, tailx and y will keep track of it and create tail
			double xtail = block.getTranslateX();
			double ytail = block.getTranslateY();
			
			// 4 cases since there are only 4 direction to move
			switch(defaultDirection)
			{
				// Left movement
				case LEFT:
					// press left will minus one block size
					block.setTranslateX(snakeRectangle.get(0).getTranslateX() - BLOCKSIZE);
					// Only change the x coordinate since left-right direction
					block.setTranslateY(snakeRectangle.get(0).getTranslateY());
					break;
				// Right movement
				case RIGHT:
					// press right will add one block size
					block.setTranslateX(snakeRectangle.get(0).getTranslateX() + BLOCKSIZE);
					// Only change the x coordinate since left-right direction
					block.setTranslateY(snakeRectangle.get(0).getTranslateY());
					break;
				// Up movement
				case UP:
					// Only change the y coordinate since up-down direction
					block.setTranslateX(snakeRectangle.get(0).getTranslateX());
					// press up will minus one block size
					block.setTranslateY(snakeRectangle.get(0).getTranslateY() - BLOCKSIZE);
					break;
				// Down movement	
				case DOWN:
					// Only change the y coordinate since up-down direction
					block.setTranslateX(snakeRectangle.get(0).getTranslateX());
					// press down will add one block size
					block.setTranslateY(snakeRectangle.get(0).getTranslateY() + BLOCKSIZE);
					break;
			}
			
			// True because it moved, so change direction
			didItMove = true;
			
			// if there is at least two block, then remove one block and put one block back
			if(twoBlock)
			{
				// add one block back and put tail block in the front
				snakeRectangle.add(0,block);
			}
			
			// To know when collision occur
			for (Node rec : snakeRectangle)
			{
				// If it is not same block and tail will be the same one of he body and restart the game
				if(rec != block && block.getTranslateX() == rec.getTranslateX() && block.getTranslateY() == rec.getTranslateY())
				{
					// restart the game since you lose
					gameRestart();
					break;
				}
			}
			// Another collision against the wall of the screen, if head is less than 0 and go to the left of the screen and go to right of the screen
			if(block.getTranslateX() < 0 || block.getTranslateX() >= WIDTH || block.getTranslateY() < 0 || block.getTranslateY() >= HEIGHT)
			{
				// restart the game since you lose
				gameRestart();
			}
			
			// Check if snake eat the food
			if(block.getTranslateX() == snakeFood.getTranslateX() && block.getTranslateY() == snakeFood.getTranslateY())
			{
				// randomly spawn the food by setting x and y coordinate, width-blocksize so it will within the pane
				snakeFood.setTranslateX((int)(Math.random() * (WIDTH-BLOCKSIZE)) / BLOCKSIZE * BLOCKSIZE);
				// setting it to int and BLOCKSIZE * BLOCKSIZE so it will be an actual value
				snakeFood.setTranslateY((int)(Math.random() * (HEIGHT-BLOCKSIZE)) / BLOCKSIZE * BLOCKSIZE);
				
				// Set new created snake
				Rectangle rec = new Rectangle(BLOCKSIZE,BLOCKSIZE);
				rec.setTranslateX(xtail);
				rec.setTranslateY(ytail);
				
				// add to the same spot
				snakeRectangle.add(rec);
			}
		});
		
		// So the animation will be infinite, timeline will run the same frame since there is only one
		animation.getKeyFrames().add(snakeSpeed);
		animation.setCycleCount(Timeline.INDEFINITE);
		
		// easily to add or remove snake food and inside of snake
		pane.getChildren().addAll(snakeFood,insideOfSnake);
		return pane;
	}

	// Need a main to start
	public static void main(String[] args)
	{
		launch(args);
	}
}
