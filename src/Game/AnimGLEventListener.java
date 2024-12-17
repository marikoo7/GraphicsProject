package Game;

import Texture.TextureReader;
import Texture.AnimListener;
import com.sun.opengl.util.GLUT;

import java.awt.event.*;
import java.io.IOException;
import java.util.BitSet;
import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;

public class AnimGLEventListener extends AnimListener implements MouseListener , MouseMotionListener{
    int maxWidth = 100;
    int maxHeight = 100;
    int mouseX;
    int mouseY;

    // menu selection
    int selectedOption = 0;

    // dino&tree-related variables
    int dinoIndex1=61; //for player1
    int dinoIndex2=68;//for player2

    // rocks-related variables
    int treeIndex1 = 65;//tree index for player1
    int treeIndex2 = 65;//tree index for player2

    int x11, y11; // tree coordinates for player1
    int x12, y12; // tree coordinates for player2

    boolean isJump1 = false; //check if player1 is jumping
    int jumpy1 =6; //jump height for player1
    boolean isJump2 = false; //check if player2 is jumping
    int jumpy2 =6; //jump height for player2
    boolean GameOver1 = false;
    boolean GameOver2 = false;


    int x =20, y = 60; //dino coordinates for player1
    int x2=27,y2=13; //dino coordinates for player2

    // Animation-related variables
    private int currentFrame = 0; // Current frame index
    private int frameDelay = 3;   // Number of display calls before moving to the next frame
    private int frameCounter = 0; // Counter to manage frame delay
    private float bounceTime = 0; // Tracks time for bouncing
    private float bounceSpeed = 0.1f; // Speed of the bounce
    private float bounceAmplitude = 2.0f; // Height of the bounce

    boolean isGameStarted1=false; //check if game started for player1
    boolean isGameStarted2=false;
    int dinoRate1=0; // Range of toggling between dino legs for player1
    int dinoMaxRate1=3;
    int dinoRate2=0;// Range of toggling between dino legs for player2
    int dinoMaxRate2=6;


    boolean homePageVisible = true;

    // Time and score related variables

    private int score1 = 0;
    private int score2 = 0;
    private long startTime ;

    // Lives
    int life1Index = 71;
    int life2Index = 72;
    int life3Index = 73;
    int playerLives1 = 3;
    int playerLives2 = 3;
    int lifeX = 15;
    int lifeY = 85;
    int lifeY2 = 36;
    boolean hasCollided1 = false;
    boolean hasCollided2 = false;

    // Level-up

    double treeSpeed1 = 2;
    double treeSpeed2 = 2;


    public AnimGLEventListener() {
    }

    String[] textureNames = { "frame_00_delay-0.08s.png","frame_01_delay-0.08s.png","frame_02_delay-0.08s.png","frame_03_delay-0.08s.png","frame_04_delay-0.08s.png","frame_05_delay-0.08s.png","frame_06_delay-0.08s.png","frame_07_delay-0.08s.png","frame_08_delay-0.08s.png","frame_09_delay-0.08s.png",
            "frame_10_delay-0.08s.png","frame_11_delay-0.08s.png","frame_12_delay-0.08s.png","frame_13_delay-0.08s.png", "frame_14_delay-0.08s.png" , "frame_15_delay-0.08s.png" , "frame_16_delay-0.08s.png" , "frame_17_delay-0.08s.png" , "frame_18_delay-0.08s.png" , "frame_19_delay-0.08s.png",
            "frame_20_delay-0.08s.png" , "frame_21_delay-0.08s.png" , "frame_22_delay-0.08s.png" , "frame_23_delay-0.08s.png" , "frame_24_delay-0.08s.png" , "frame_25_delay-0.08s.png" , "frame_26_delay-0.08s.png" , "frame_27_delay-0.08s.png" , "frame_28_delay-0.08s.png" , "frame_29_delay-0.08s.png" ,
            "frame_30_delay-0.08s.png", "frame_31_delay-0.08s.png" , "frame_32_delay-0.08s.png" , "frame_33_delay-0.08s.png" , "frame_34_delay-0.08s.png" , "frame_35_delay-0.08s.png" , "frame_36_delay-0.08s.png" , "frame_37_delay-0.08s.png" , "frame_38_delay-0.08s.png" , "frame_39_delay-0.08s.png" ,
            "frame_40_delay-0.08s.png" , "frame_41_delay-0.08s.png" , "frame_42_delay-0.08s.png" , "frame_43_delay-0.08s.png" , "frame_44_delay-0.08s.png" , "frame_45_delay-0.08s.png" , "frame_46_delay-0.08s.png" , "frame_47_delay-0.08s.png" , "frame_48_delay-0.08s.png" , "frame_49_delay-0.08s.png" ,
            "frame_50_delay-0.08s.png" , "frame_51_delay-0.08s.png" , "frame_52_delay-0.08s.png" , "frame_53_delay-0.08s.png" , "frame_54_delay-0.08s.png" , "frame_55_delay-0.08s.png" , "frame_56_delay-0.08s.png" , "frame_57_delay-0.08s.png" , "frame_58_delay-0.08s.png" , "frame_59_delay-0.08s.png" ,
            "frame_60_delay-0.08s.png" ,"playerOne-1.png","playerOne-2.png","playerOne-3.png","playerOne-1.png","rock4.png","rock5.png","rock6.png","playerTwo-2.png","playerTwo-3.png","playerTwo-1.png","life1.png","life2.png","life3.png","game-over.png","you-win!.png","how-to-play.png","you-win.png","go-back-to-menu.png","instructions.png","start-game.png","select.png","one-player.png","two-players.png","exit.png","T-ReX-GAME.png" ,"T-rexBG.png"
    };
    TextureReader.Texture[] texture = new TextureReader.Texture[textureNames.length];
    public int[] textures = new int[textureNames.length];

    public void init(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);

        for (int i = 0; i < textureNames.length; i++) {
            try {
                texture[i] = TextureReader.readTexture(assetsFolderName + "//" + textureNames[i], true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);
                new GLU().gluBuild2DMipmaps(
                        GL.GL_TEXTURE_2D,
                        GL.GL_RGBA,
                        texture[i].getWidth(),
                        texture[i].getHeight(),
                        GL.GL_RGBA,
                        GL.GL_UNSIGNED_BYTE,
                        texture[i].getPixels()
                );
            } catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void display(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();
        gl.glColor3f(1f,1f,1f);
        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

        if (score1 == 100){
            GameOver1 = true;
            DrawPlayerOne(gl , dinoIndex1);
            DrawWin(gl);
            return;
        }
        if (score1 == 50){
            treeSpeed1 = 3;
        }
        if (score2 == 50){
            treeSpeed2 = 3;
        }
        if (GameOver1 ){
            DrawPlayerOne(gl, dinoIndex1);
            DrawGameOver(gl);
            return;
        }
        if (GameOver2 ){
            DrawPlayerTwo(gl, dinoIndex1);
            DrawGameOver(gl);
            return;
        }

        // Time
        long currentTime = System.currentTimeMillis();
        double elapsedTimeInSeconds = (currentTime - startTime) / 1000.0;
        int seconds = (int) elapsedTimeInSeconds;

        // Animation logic
        frameCounter++;
        if (frameCounter >= frameDelay) {
            currentFrame = (currentFrame + 1) % 60; // Exclude background and overlay //60 is images of frame
            frameCounter = 0; // Reset the counter
        }

        // Increment time for bounce effect
        bounceTime += bounceSpeed;

        // Draw animation in the upper half
        DrawSprite(gl, 45, 25, currentFrame, 0.7f, 0.5f); // Bottom area
        // Draw background (bottom layer)
        DrawBackground(gl);

        // Draw animation in the lower half
        DrawSprite(gl, 45, 68, currentFrame, 0.7f, 0.4f); // Top area


        // Calculate bounce offset for the overlay
        float bounceOffset = (float) Math.sin(bounceTime) * bounceAmplitude;


        if (isGameStarted1){
            DrawPlayerOne(gl,dinoIndex1); //if we clicked enter
            DrawScore1(gl);
            Timer1(gl, seconds);
            if (startTime == 0){
                startTime = System.currentTimeMillis();
            }
        }
        else if(isGameStarted2){
            DrawPlayerTwo(gl,dinoIndex1);
            DrawScore1(gl);
            DrawScore2(gl);
            Timer1(gl, seconds);
            Timer2(gl, seconds);
            if (startTime == 0){
                startTime = System.currentTimeMillis();
            }
        }
        else {
            // Draw the list button
            DrawSprite(gl,15,83,textures.length - 9 , 0.09f,0.09f);
            // Draw the instructions button
            DrawSprite(gl,75,83,textures.length - 8 , 0.09f,0.09f);
            if(homePageVisible) {
                // Draw the T-ReX-GAME.png overlay with bouncing effect
                DrawSprite(gl, 46, (int) (68 + bounceOffset), textures.length - 2, 0.5f, 0.35f);
                DrawMenu(gl);
            }else{
                DrawInstructions(gl);
            }
        }

        // AI
        dinoRate1++;
        if(dinoRate1>=dinoMaxRate1) {
            dinoRate1=0;
            dinoIndex1 = (dinoIndex1 == 62) ? 63 : 62;// Toggle between dino's legs for player1
        }

        dinoRate2++;
        if(dinoRate2>=dinoMaxRate2) {
            dinoRate2=0;
            dinoIndex2 = (dinoIndex2 == 68) ? 69 : 68;// Toggle between dino's legs for player2
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public BitSet keyBits = new BitSet(256);



    public boolean isKeyPressed(final int keyCode) {
        return keyBits.get(keyCode);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        keyBits.set(key);
        // Up arrow key moves the selection up
        if (key == KeyEvent.VK_UP) {
            selectedOption = (selectedOption - 1 + 3) % 3; // Wrap around
        }
        // Down arrow key moves the selection down
         if (key == KeyEvent.VK_DOWN) {
            selectedOption = (selectedOption + 1) % 3;// Wrap around
        }
         // Enter key (or Space) to select an option
         if (key == KeyEvent.VK_ENTER) {
            handleMenuSelection();// Implement this method to handle the current selection
        }
        if (key==KeyEvent.VK_SPACE) {
            if (!isJump1) {
                isJump1 = true; //Space key makes dino jump for player1
            }
            dinoIndex1=64; //index of jumping dino of player1
        }
        if (key==KeyEvent.VK_W) {
            if (!isJump2) {//W key makes dino jump for player1
                isJump2 = true;
            }
            dinoIndex2=70; // index of jumping dino of player2
        }
    }


    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        keyBits.clear(key);
    }
    public void DrawSprite(GL gl,int x, int y, int index, float scaleX , float scaleY){
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated( x/(maxWidth/2.0) - 0.9, y/(maxHeight/2.0) - 0.9, 0);
        gl.glScaled(scaleX, scaleY, 1);
        //System.out.println(x +" " + y);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }
    public void DrawBackground(GL gl) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[textures.length - 1]);

        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);

        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);

    }
    public void drawLives1(GL gl, int lives) {
        if (lives >= 1) DrawSprite(gl, lifeX, lifeY, life1Index, 0.05f, 0.05f);
        if (lives >= 2) DrawSprite(gl, lifeX + 5, lifeY, life2Index, 0.05f, 0.05f);
        if (lives >= 3) DrawSprite(gl, lifeX + 10, lifeY, life3Index, 0.05f, 0.05f);
    }
    public void drawLives2(GL gl, int lives) {
        if (lives >= 1) DrawSprite(gl, lifeX +5, lifeY2, life1Index, 0.05f, 0.05f);
        if (lives >= 2) DrawSprite(gl, lifeX + 10, lifeY2, life2Index, 0.05f, 0.05f);
        if (lives >= 3) DrawSprite(gl, lifeX + 15, lifeY2, life3Index, 0.05f, 0.05f);
    }
    public  void resetTreeOption(){
        x11 = 100;
        x12 = 100;
    }
    public void DrawPlayerOne(GL gl, int index){ //method for drawing player1

        if (GameOver1) {

            DrawSprite(gl, 45, 25, currentFrame, 0.7f, 0.5f); // Bottom area
            DrawBackground(gl);  // Draw background to keep it visible
            DrawSprite(gl, 45, 68, currentFrame, 0.7f, 0.4f); // Top area
            DrawSprite(gl, x, y, dinoIndex1, 0.2f, 0.2f); // Show dinosaur
            DrawSprite(gl, x11, 55, treeIndex1, 0.09f, 0.09f); // Show tree

            return; // Don't update positions or move elements

        }
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);
        //lives
        drawLives1(gl, playerLives1);

        DrawSprite(gl, x, y, dinoIndex1, 0.2f, 0.2f); //draw player1
        if(x11>15 && x11<75) { //Range of upper frame
            DrawSprite(gl, x11, 55, treeIndex1, 0.09f, 0.09f); //draw tree
        }

        // collision
        if (checkCollision(x, y, x11, 55) && !isJump1) {
            if (!hasCollided1) {
                playerLives1--;
                System.out.println("Collision detected! Lives left: " + playerLives1);
                hasCollided1 = true;
                if (playerLives1 <= 0) {
                    GameOver1 = true;
                } else {
                    resetTreeOption();
                }
            }
        } else if (!checkCollision(x, y, x11, 55)) {
            hasCollided1 = false;
        }

        // tree
        x11 -= treeSpeed1 ; //make monster move to the left
        if(x11 < 0){
            x11 = maxWidth-10; //start from the beginning
            y11 = 27;
            treeIndex1 = (int)(Math.random()*3)+65; //for drawing random trees
        }

        //dino jump
        if (isJump1) { //jump handling
            y += jumpy1;
            jumpy1 -= 1;
            if (y <= 60) {
                y = 60;
                isJump1 = false;
                jumpy1 = 6;
            }
        }
        if (isJump1 && x - treeSpeed1 - 40 < x11 && x > x11) { // Check if the dino has passed the tree horizontally
            score1++;
            System.out.println("Jumped over tree! Score: " + score1);
        }

    }
    public void DrawPlayerTwo(GL gl, int index){
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);
       //lives
        drawLives1(gl, playerLives1);

        drawLives2(gl, playerLives2);

        if (GameOver2) {

            DrawSprite(gl, 45, 25, currentFrame, 0.7f, 0.5f); // Bottom area
            DrawBackground(gl);  // Draw background to keep it visible
            DrawSprite(gl, 45, 68, currentFrame, 0.7f, 0.4f); // Top area
            DrawSprite(gl, x, y, dinoIndex1, 0.2f, 0.2f); // Show dinosaur
            DrawSprite(gl, x2, y2, dinoIndex2, 0.2f, 0.2f); // Show dinosaur
            DrawSprite(gl, x12, 8, treeIndex2, 0.09f, 0.09f); // Show tree
            DrawSprite(gl, x11, 55, treeIndex1, 0.09f, 0.09f); // Show tree


            return; // Don't update positions or move elements

        }

        DrawSprite(gl, x, y, dinoIndex1, 0.2f, 0.2f); //draw player1
        DrawSprite(gl, x2, y2, dinoIndex2, 0.2f, 0.2f); //draw player2


        if(x11>15 && x11<75) {
            DrawSprite(gl, x11, 55, treeIndex1, 0.09f, 0.09f); //draw monster for player1

        }
        if(x12>20 && x12<67){
            DrawSprite(gl, x11, 8, treeIndex2, 0.09f, 0.09f); //draw monster for player2
        }

        //collision
        if (!isJump1 && checkCollision(x, y, x11, y11)) { // collision monster with player1
            if (!hasCollided1) {
                playerLives1--;
                System.out.println("Collision detected! Lives left: " + playerLives1);
                hasCollided1 = true;
                if (playerLives1 <= 0) {
                    GameOver2 = true;
                } else {
                    resetTreeOption();
                }
            }
        } else if (!checkCollision(x, y, x11, 55)) {
            hasCollided1 = false;

        }
        if (!isJump2 && checkCollision2(x2, y2, x12, y12)) {
            if (!hasCollided2) {
                playerLives2--;
                System.out.println("Collision detected! Lives left: " + playerLives2);
                hasCollided2 = true;
                if (playerLives2 <= 0) {
                    GameOver2 = true;
                } else {
                    resetTreeOption();
                }
            }
        } else if (!checkCollision(x, y, x11, 55)) {
            hasCollided2 = false;//collision monster with player2

        }

        // tree
        x11 -= treeSpeed1; // tree coordinates for player1
        if(x11<0){
            x11 = maxWidth-10;
            y11 = 27;
            treeIndex1 = (int)(Math.random()*3)+65;
        }
        x12 -= treeSpeed2;
        if(x12<0){ // tree coordinates for player2
            x12 = maxWidth-10;
            y12 = 27;
            treeIndex2 = (int)(Math.random()*3)+65;
        }

        //dino jump
        if (isJump1) { //jump handling for player1
            y += jumpy1;
            jumpy1 -= 1;
            if (y <= 60) {
                y = 60;
                isJump1 = false;
                jumpy1 = 6;
            }
        }
        if (isJump2) { //jump handling for player2
            y2 += jumpy2;
            jumpy2 -= 1;
            if (y2 <= 13) {
                y2 = 13;
                isJump2 = false;
                jumpy2 = 6;
            }
        }
        if (isJump1 && x - treeSpeed1 - 40 < x11 && x > x11) { // Check if the dino has passed the tree horizontally
            score1++;
            System.out.println("Jumped over tree! Score: " + score1); // Optional feedback
        }
        if (isJump2 && x - treeSpeed2 - 40 < x12 && x > x12) { // Check if the dino has passed the tree horizontally
            score2++;
            System.out.println("Jumped over tree! Score: " + score2); // Optional feedback
        }
    }
    private boolean checkCollision(double x1, double y1, double x2, double y2) {

        double dinoWidth = 8;
        double dinoHeight = 40;

        double obstacleWidth = 8;
        double obstacleHeight = 40;

        boolean collisionX = x1 < x2 + obstacleWidth && x1 + dinoWidth > x2;
        boolean collisionY = y1 < y2 + obstacleHeight && y1 + dinoHeight > y2;

        return collisionX && collisionY;
    }
    private boolean checkCollision2(double x1, double y1, double x2, double y2) {

        double dinoWidth = 8;
        double dinoHeight = 40;

        double obstacleWidth = 8;
        double obstacleHeight = 40;

        boolean collisionX = x1 < x2 + obstacleWidth && x1 + dinoWidth > x2;
        boolean collisionY = y1 < y2 + obstacleHeight && y1 + dinoHeight > y2;

        return collisionX && collisionY;
    }

    public void DrawMenu(GL gl) {
        // Draw "Start Game"
        int startGameX = 45;
        int startGameY = 33;
        float startGameScaleX = (float) (2.5 * 0.2f);
        float startGameScaleY = (float) (2.5 * 0.05f);
        DrawSprite(gl, startGameX, startGameY, textures.length - 7, startGameScaleX, startGameScaleY);

        // Draw "1-Player"
        int onePlayerX = 45;  // Adjust X position for centering
        int onePlayerY = startGameY - 10;  // 8 units below Start Game
        float onePlayerScaleX = 0.25f; // Adjust horizontal scale
        float onePlayerScaleY = 0.05f; // Adjust vertical scale
        DrawSprite(gl, onePlayerX, onePlayerY, textures.length - 5, onePlayerScaleX, onePlayerScaleY);

        // Draw "2-Players"
        int twoPlayersX = 45;
        int twoPlayersY = onePlayerY - 8; // 8 units below 1-Player
        float twoPlayersScaleX = 0.25f; // Adjust horizontal scale
        float twoPlayersScaleY = 0.05f; // Adjust vertical scale
        DrawSprite(gl, twoPlayersX, twoPlayersY, textures.length - 4, twoPlayersScaleX, twoPlayersScaleY);
        // Draw "Exit"
        int exitX = 45;
        int exitY = twoPlayersY - 8; // 8 units below 2-Players
        float exitScaleX = 0.5f * 0.25f; // Adjust horizontal scale
        float exitScaleY = 0.75f * 0.05f; // Adjust vertical scale
        DrawSprite(gl, exitX, exitY, textures.length - 3, exitScaleX, exitScaleY);

        // Draw the select next to the current option (1-Player, 2-Players, Exit)
        int arrowX = 30;
        int arrowY = onePlayerY;

        // Adjust arrow Y based on the selected option (1-Player = 0, 2-Players = 1, Exit = 2)
        switch (selectedOption) {
            case 0: // 1-Player
                arrowY = onePlayerY;
                break;
            case 1: // 2-Players
                arrowY = twoPlayersY;
                break;
            case 2: // Exit
                arrowY = exitY;
                break;
        }

        DrawSprite(gl, arrowX, arrowY, textures.length - 6, 0.05f, 0.05f); // select.png
    }

    public void DrawInstructions(GL gl){
        // Draws the instruction page
        DrawSprite(gl, 48, 68 , textures.length - 11, 0.55f, 0.4f);
        DrawSprite(gl, 46, 20, textures.length - 10, 0.5f, 0.35f);
    }

    public void handleMenuSelection() {
        switch (selectedOption) {
            case 0:
                System.out.println("1-Player Mode Selected");
                startGame1(); // start game for player1
                break;
            case 1:
                System.out.println("2-Players Mode Selected");
                startGame2(); //start game for player1 & player2
                break;
            case 2:
                System.out.println("Exit Selected");
                System.exit(0);
                break;
        }
    }

    // draw score
    public void DrawScore1(GL gl){
        gl.glColor3f(0.0f, 0.0f, 0.0f); // Set color to black
        gl.glRasterPos2f(0.4f, 0.8f); // Set text position (x, y)
        String Score = "Score: " + score1;
        for (char c:Score.toCharArray()) {
            GLUT glut =new GLUT();
            glut.glutBitmapCharacter(GLUT.BITMAP_HELVETICA_18, c);
        }
    }
    public void DrawScore2(GL gl){
        gl.glColor3f(0.0f, 0.0f, 0.0f); // Set color to black
        gl.glRasterPos2f(0.3f, -0.2f); // Set text position (x, y)
        String Score = "Score: " + score2;
        for (char c:Score.toCharArray()) {
            GLUT glut =new GLUT();
            glut.glutBitmapCharacter(GLUT.BITMAP_HELVETICA_18, c);
        }
    }

    // draw Timer

    public void Timer1(GL gl,int seconds) {
        gl.glColor3f(0.0f, 0.0f, 0.0f); // Set color to black
        gl.glRasterPos2f(0.4f, 0.7f); // Set text position (x, y)
        String Time = "Time: " + seconds + "s";
        for (char c:Time.toCharArray()) {
            GLUT glut =new GLUT();
            glut.glutBitmapCharacter(GLUT.BITMAP_HELVETICA_18, c);
        }
    }

    public void Timer2(GL gl,int seconds) {
        gl.glColor3f(0.0f, 0.0f, 0.0f); // Set color to black
        gl.glRasterPos2f(0.3f, -0.3f); // Set text position (x, y)
        String Time = "Time: " + seconds + "s";
        for (char c : Time.toCharArray()) {
            GLUT glut = new GLUT();
            glut.glutBitmapCharacter(GLUT.BITMAP_HELVETICA_18, c);
        }
    }

    // draw win
    public void DrawWin(GL gl){
        if(score1 == 100){
            DrawBackground(gl);

            DrawSprite(gl, 45, 70, textures.length- 12, 0.4f, 0.2f);
        }
    }

    // draw game over

    public void DrawGameOver(GL gl) {
        if (GameOver1 || GameOver2) {
                DrawBackground(gl);
                DrawSprite(gl, 45, 70, textures.length - 13, 0.4f, 0.2f); //  Game Over
                System.out.println("Game Over");
        }
    }
    public void startGame1() {
        isGameStarted1 = true;
    }
    public void startGame2() {
        isGameStarted2 = true;
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseX = (int) convertX(e.getX(), e.getComponent().getWidth(), 0, 100);
        mouseY = (int) convertY(e.getY(), e.getComponent().getHeight(), 0, 100);

        // Check if the mouse click is within the Instructions button
        if (mouseX <= 83 && mouseX >= 76 && mouseY <= 91 && mouseY >= 84){
            homePageVisible = false;
            System.out.println("instructions selected");
        }

        // Check if the mouse click is within the List button
        if (mouseX <= 23 && mouseX >= 16 && mouseY <= 91 && mouseY >= 84) {
            homePageVisible = true;
            System.out.println("list selected");
        }
    }


    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
            mouseX = (int) convertX(e.getX(), e.getComponent().getWidth(), 0, 100);
            mouseY = (int) convertY(e.getY(), e.getComponent().getHeight(), 0, 100);
    }
    public double convertX(double x, double screenWidth, double left, double right) {
        return left + (x / screenWidth) * (right - left);
    }

    public double convertY(double y, double screenHeight, double bottom, double top) {
        return top - (y / screenHeight) * (top - bottom);
    }
}
