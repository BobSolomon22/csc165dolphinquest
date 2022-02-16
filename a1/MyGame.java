package a1;

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
    private GameObject dolphin, xAxis, yAxis, zAxis;
    // ObjShape declarations
    private ObjShape dolphinS, xAxisS, yAxisS, zAxisS;
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
        xAxisS = new Line(new Vector3f(0f,0f,0f), new Vector3f(3f,0f,0f));
        yAxisS = new Line(new Vector3f(0f,0f,0f), new Vector3f(0f,3f,0f));
        zAxisS = new Line(new Vector3f(0f,0f,0f), new Vector3f(0f,0f,3f));
    }

    @Override
    public void loadTextures() {
        dolphintx = new TextureImage("Dolphin_HighPolyUV.png");
    }

    @Override
    public void buildObjects() {
        Matrix4f initialTranslation, initialScale;
        
        // build dolphin
        dolphin = new GameObject(GameObject.root(), dolphinS, dolphintx);
        initialTranslation = (new Matrix4f()).translation(0,0,0);
        initialScale = (new Matrix4f()).scaling(3.0f);
        dolphin.setLocalTranslation(initialTranslation);
        dolphin.setLocalScale(initialScale);

        // build axes
        xAxis = new GameObject(GameObject.root(), xAxisS);
        yAxis = new GameObject(GameObject.root(), yAxisS);
        zAxis = new GameObject(GameObject.root(), zAxisS);
        (xAxis.getRenderStates()).setColor(new Vector3f(1f,0f,0f));
        (yAxis.getRenderStates()).setColor(new Vector3f(0f,1f,0f));
        (zAxis.getRenderStates()).setColor(new Vector3f(0f,0f,1f));
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