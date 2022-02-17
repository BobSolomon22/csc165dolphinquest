package a1;

import tage.*;
import tage.input.InputManager;
import tage.input.IInputManager.INPUT_ACTION_TYPE;
import tage.shapes.*;

import java.lang.Math;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import org.joml.*;

import java.util.ArrayList;

import net.java.games.input.*;
import net.java.games.input.Component.Identifier.*;

// import java.awt.event.KeyEvent;

public class MyGame extends VariableFrameRateGame {
    private static Engine engine;
    private InputManager im;
    public static Engine getEngine() { return engine; }

    private double startTime, prevTime, elapsedTime, amount;

    // GameObject declarations
    private GameObject dolphin, prize1, prize2, prize3, xAxis, yAxis, zAxis;
    // ObjShape declarations
    private ObjShape dolphinS, prize1S, prize2S, prize3S, xAxisS, yAxisS, zAxisS;
    // TextureImage declarations
    private TextureImage dolphintx, prize1tx, prize2tx, prize3tx;
    // Light declarations
    private Light light1;

    public MyGame() { super(); }

    public GameObject getAvatar() {
        return dolphin;
    }

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
        // setup window
        (engine.getRenderSystem()).setWindowDimensions(1900,1000);
        
        // setup light
        Light.setGlobalAmbient(0.5f, 0.5f, 0.5f);
        light1 = new Light();
        light1.setLocation(new Vector3f(5.0f, 4.0f, 2.0f));
        (engine.getSceneGraph()).addLight(light1);
        
        // setup camera location
        (engine.getRenderSystem().getViewport("MAIN").getCamera()).setLocation(new Vector3f(0,0,5));

        // setup inputs
        im = engine.getInputManager();
        ArrayList controllers = im.getControllers();

        BackNForthAction backNForthAction = new BackNForthAction(this);
        FwdAction fwdAction = new FwdAction(this);
        TurnAction turnAction = new TurnAction(this);

        for(Controller c : controllers) {
            // controller backnforth
            im.associateAction(
            c,
            net.java.games.input.Component.Identifier.Axis.Y,
            backNForthAction,
            INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN
            );
            
            // controller turn
            im.associateAction(
            c,
            net.java.games.input.Component.Identifier.Axis.X,
            turnAction,
            INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN
            );

            // keyboard forward
            im.associateAction(
            kbName,
            net.java.games.input.Component.Identifier.Key.W,
            fwdAction,
            INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN
            );
        }
        
        
    }

    @Override
    public void update() {
        elapsedTime = System.currentTimeMillis() - prevTime;
        prevTime = System.currentTimeMillis();
        amount = elapsedTime * 0.03;
        Camera c = (engine.getRenderSystem()).getViewport("MAIN").getCamera();

        im.update((float)elapsedTime);
        positionCameraBehindAvatar();
    }

    private void positionCameraBehindAvatar() {
        Camera cam = (engine.getRenderSystem()).getViewport("MAIN").getCamera();
        Vector3f location = dolphin.getLocalLocation();
        Vector3f forward = dolphin.getLocalForwardVector();
        Vector3f up = dolphin.getLocalUpVector();
        Vector3f right = dolphin.getLocalRightVector();
        cam.setU(right);
        cam.setV(up);
        cam.setN(forward);
        cam.setLocation(location.add(up.mul(0.75f).add(forward.mul(-2.0f))));
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