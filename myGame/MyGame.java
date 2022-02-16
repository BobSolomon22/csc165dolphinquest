package myGame;

import tage.*;
import tage.shapes.*;

import java.lang.Math;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import org.joml.*;

// import java.awt.event.KeyEvent;

public class MyGame extends VariableFrameRateGame {
    private static Engine engine;
    public static Engine getEngine() { return engine; }

    // GameObject declarations
    private GameObject dolphin;
    // ObjShape declarations
    private ObjShape dolphinS;
    // TextureImage declarations
    private TextureImage dolphintx;
    // Light declarations
    private Light light1;

    public MyGame() { super(); }

    public static void main(String[] args) {
        MyGame game = new MyGame();
        engine = new Engine(game);
        game.initializeSystem();
        game.game_loop();
    }

    @Override
    public void loadShapes() {
        dolphinS = new ImportedModel("dolphinHighPoly.obj");
    }

    @Override
    public void loadTextures() {
        dolphintx = new TextureImage("Dolphin_HighPolyUV.png");
    }

    @Override
    public void buildObjects() {
        Matrix4f initialTranslation, initialScale;

        dolphin = new GameObject(GameObject.root(), dolphinS, dolphintx);
        initialTranslation = (new Matrix4f()).translation(0,0,0);
        initialScale = (new Matrix4f()).scaling(3.0f);
        dolphin.setLocalTranslation(initialTranslation);
        dolphin.setLocalScale(initialScale);
    }

    @Override
    public void initializeGame() {
        (engine.getRenderSystem()).setWindowDimensions(1900,1000);
        
        Light.setGlobalAmbient(0.5f, 0.5f, 0.5f);

        light1 = new Light();
        light1.setLocation(new Vector3f(5.0f, 4.0f, 2.0f));
        (engine.getSceneGraph()).addLight(light1);

        (engine.getRenderSystem().getViewport("MAIN").getCamera()).setLocation(new Vector3f(0,0,5));
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