package myGame;

import tage.*;
import tage.shapes.*;

// import java.awt.event.KeyEvent;

public class MyGame extends VariableFrameRateGame {
    private static Engine engine;
    public static Engine getEngine() { return engine; }

    // GameObject declarations
    // ObjShape declarations
    // TextureImage declarations
    // Light declarations

    public MyGame() { super(); }

    public static void main(String[] args) {
        MyGame game = new MyGame();
        engine = new Engine(game);
        game.initializeSystem();
        game.game_loop();
    }

    @Override
    public void loadShapes() {

    }

    @Override
    public void loadTextures() {

    }

    @Override
    public void buildObjects() {

    }

    @Override
    public void initializeGame() {

    }

    @Override
    public void update() {

    }
/*
    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            // Desired keyboard assignments go here
        }
        super.keyPressed(e); // ESC already mapped to game exit. "=" key already mapped to window/fullscreen toggle.
    }
*/
}