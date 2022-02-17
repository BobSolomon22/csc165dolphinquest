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
    private GameObject dolphin, prize1, prize2, prize3, xAxis, yAxis, zAxis;
    // ObjShape declarations
    private ObjShape dolphinS, prize1S, prize2S, prize3S, xAxisS, yAxisS, zAxisS;
    // TextureImage declarations
    private TextureImage dolphintx, prize1tx, prize2tx, prize3tx;
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
        prize1S = new Cube();
        prize2S = new Torus();
        prize3S = new Sphere();
        xAxisS = new Line(new Vector3f(0f,0f,0f), new Vector3f(3f,0f,0f));
        yAxisS = new Line(new Vector3f(0f,0f,0f), new Vector3f(0f,3f,0f));
        zAxisS = new Line(new Vector3f(0f,0f,0f), new Vector3f(0f,0f,3f));
    }

    @Override
    public void loadTextures() {
        dolphintx = new TextureImage("Dolphin_HighPolyUV.png");
        prize1tx = new TextureImage("smile.png");
        prize2tx = new TextureImage("stripe.png");
        prize3tx = new TextureImage("divide.png");
    }

    @Override
    public void buildObjects() {
        // build dolphin
        dolphin = new GameObject(GameObject.root(), dolphinS, dolphintx);
        dolphin.setLocalTranslation((new Matrix4f()).translation(0,0,0));
        dolphin.setLocalScale((new Matrix4f()).scaling(3.0f));

        // build prizes
        prize1 = new GameObject(GameObject.root(), prize1S, prize1tx);
        prize1.setLocalTranslation(new Matrix4f().translation(0,2,0));
        prize1.setLocalScale(new Matrix4f().scaling(0.3f));

        prize2 = new GameObject(GameObject.root(), prize2S, prize2tx);
        prize2.setLocalTranslation(new Matrix4f().translation(2,0,0));
        prize2.setLocalScale(new Matrix4f().scaling(0.5f));
        prize2.setLocalRotation(new Matrix4f().rotate(90, new Vector3f(1f,0f,0f)));

        prize3 = new GameObject(GameObject.root(), prize3S, prize3tx);
        prize3.setLocalTranslation(new Matrix4f().translation(-2,0,0));
        prize3.setLocalScale(new Matrix4f().scaling(0.5f));
        prize3.setLocalRotation(new Matrix4f().rotate(-90, new Vector3f(0f,1f,0f)));

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