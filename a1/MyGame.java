package a1;

import tage.*;
import tage.Light.LightType;
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
import java.util.Random;

import net.java.games.input.*;
import net.java.games.input.Component.Identifier.*;

// import java.awt.event.KeyEvent;

public class MyGame extends VariableFrameRateGame {
    private static Engine engine;
    private InputManager im;
    public static Engine getEngine() { return engine; }

    public Random rng;

    private double startTime, prevTime, elapsedTime, amount;
    private boolean ridingDolphin;
    private Vector3f dolphinLocation;
    private Vector3f oldCameraLocation;
    private Vector3f newCameraLocation;
    private ArrayList<GameObject> prizes;
    private float distanceBetweenAvatarAndCamera;
    private int prizesCollected;
    private float speedBoostTimer;

    // GameObject declarations
    private GameObject dolphin, prize1, prize2, prize3, xAxis, yAxis, zAxis, diamondOfPower;
    // ObjShape declarations
    private ObjShape dolphinS, prize1S, prize2S, prize3S, xAxisS, yAxisS, zAxisS, diamondOfPowerS;
    // TextureImage declarations
    private TextureImage dolphintx, prize1tx, prize2tx, prize3tx, diamondOfPowertx;
    // Light declarations
    private Light light1, spotlight;

    public MyGame() { super(); }

    public GameObject getAvatar() {
        return dolphin;
    }

    public Camera getCamera() {
        return (engine.getRenderSystem().getViewport("MAIN").getCamera());
    }

    public boolean isRidingDolphin() {
        return ridingDolphin;
    }

    public void toggleRide() {
        if(ridingDolphin) {
            ridingDolphin = false;
        }
        else {
            ridingDolphin = true;
        }
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
        diamondOfPowerS = new DiamondOfPower();
    }

    @Override
    public void loadTextures() {
        dolphintx = new TextureImage("Dolphin_HighPolyUV.png");
        prize1tx = new TextureImage("smile.png");
        prize2tx = new TextureImage("stripe.png");
        prize3tx = new TextureImage("divide.png");
        diamondOfPowertx = new TextureImage("diamondofpower.png");
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
        prize2.getRenderStates().setTiling(1);

        prize3 = new GameObject(GameObject.root(), prize3S, prize3tx);
        prize3.setLocalTranslation(new Matrix4f().translation(-2,0,0));
        prize3.setLocalScale(new Matrix4f().scaling(0.5f));
        prize3.setLocalRotation(new Matrix4f().rotate(-90, new Vector3f(0f,1f,0f)));

        prizes = new ArrayList<GameObject>();
        prizes.add(prize1);
        prizes.add(prize2);
        prizes.add(prize3);

        // build axes
        xAxis = new GameObject(GameObject.root(), xAxisS);
        yAxis = new GameObject(GameObject.root(), yAxisS);
        zAxis = new GameObject(GameObject.root(), zAxisS);
        (xAxis.getRenderStates()).setColor(new Vector3f(1f,0f,0f));
        (yAxis.getRenderStates()).setColor(new Vector3f(0f,1f,0f));
        (zAxis.getRenderStates()).setColor(new Vector3f(0f,0f,1f));

        // build diamond of power
        diamondOfPower = new GameObject(GameObject.root(), diamondOfPowerS, diamondOfPowertx);
        diamondOfPower.setLocalLocation(new Vector3f(0f, 0f, 0f));
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

        spotlight = new Light();
        spotlight.setType(LightType.POSITIONAL);
        spotlight.setLocation(dolphin.getLocalLocation());
        (engine.getSceneGraph()).addLight(spotlight);
        
        // setup camera location
        (engine.getRenderSystem().getViewport("MAIN").getCamera()).setLocation(new Vector3f(0,0,5));

        // initialize variables
        ridingDolphin = true;
        prizesCollected = 0;

        // randomly distribute prizes
        rng = new Random();
        Vector3f prize1InitialLocation = new Vector3f(((rng.nextFloat()) * 100) - 50, ((rng.nextFloat()) * 100) - 50, ((rng.nextFloat()) * 100) - 50);
        Vector3f prize2InitialLocation = new Vector3f(((rng.nextFloat()) * 100) - 50, ((rng.nextFloat()) * 100) - 50, ((rng.nextFloat()) * 100) - 50);
        Vector3f prize3InitialLocation = new Vector3f(((rng.nextFloat()) * 100) - 50, ((rng.nextFloat()) * 100) - 50, ((rng.nextFloat()) * 100) - 50);

        prize1.setLocalLocation(prize1InitialLocation);
        prize2.setLocalLocation(prize2InitialLocation);
        prize3.setLocalLocation(prize3InitialLocation);

        // randomly move diamond
        Vector3f diamondLocation = new Vector3f(((rng.nextFloat()) * 50) - 25, ((rng.nextFloat()) * 50) - 25, ((rng.nextFloat()) * 50) - 25);
        diamondOfPower.setLocalLocation(diamondLocation);

        // setup inputs
        im = engine.getInputManager();
        ArrayList<Controller> controllers = im.getControllers();

        FwdAction fwdAction = new FwdAction(this);
        BackAction backAction = new BackAction(this);
        LeftAction leftAction = new LeftAction(this);
        RightAction rightAction = new RightAction(this);
        PitchUpAction pitchUpAction = new PitchUpAction(this);
        PitchDownAction pitchDownAction = new PitchDownAction(this);
        BackNForthAction backNForthAction = new BackNForthAction(this);
        TurnAction turnAction = new TurnAction(this);
        PitchUpNDownAction pitchUpNDownAction = new PitchUpNDownAction(this);
        MountAction mountAction = new MountAction(this);
        FindDolphinAction findDolphinAction = new FindDolphinAction(this);

        for(Controller c : controllers) {
            if(c.getType() == Controller.Type.KEYBOARD) {
                // keyboard forward
                im.associateAction(
                    c,
                    net.java.games.input.Component.Identifier.Key.W,
                    fwdAction,
                    INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN
                );
                // keyboard backward
                im.associateAction(
                    c,
                    net.java.games.input.Component.Identifier.Key.S,
                    backAction,
                    INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN
                // keyboard left
                );
                im.associateAction(
                    c,
                    net.java.games.input.Component.Identifier.Key.A,
                    leftAction,
                    INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN
                );
                // keyboard right
                im.associateAction(
                    c,
                    net.java.games.input.Component.Identifier.Key.D,
                    rightAction,
                    INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN
                );
                // keyboard pitch up
                im.associateAction(
                    c,
                    net.java.games.input.Component.Identifier.Key.UP,
                    pitchUpAction,
                    INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN
                );
                // keyboard pitch down
                im.associateAction(
                    c,
                    net.java.games.input.Component.Identifier.Key.DOWN,
                    pitchDownAction,
                    INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN
                );
                // keyboard mount
                im.associateAction(
                    c,
                    net.java.games.input.Component.Identifier.Key.SPACE,
                    mountAction,
                    INPUT_ACTION_TYPE.ON_PRESS_ONLY
                );
                // keyboard find dolphin
                im.associateAction(
                    c,
                    net.java.games.input.Component.Identifier.Key.E,
                    findDolphinAction,
                    INPUT_ACTION_TYPE.ON_PRESS_ONLY
                );
            }
            else if(c.getType() == Controller.Type.GAMEPAD || c.getType() == Controller.Type.STICK) {
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
                // controller pitch
                im.associateAction(
                    c,
                    net.java.games.input.Component.Identifier.Axis.RY,
                    pitchUpNDownAction,
                    INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN
                );
                // controller mount
                im.associateAction(
                    c,
                    net.java.games.input.Component.Identifier.Button._1,
                    mountAction,
                    INPUT_ACTION_TYPE.ON_PRESS_ONLY
                );
                // controller find dolphin
                im.associateAction(
                    c,
                    net.java.games.input.Component.Identifier.Button._9,
                    findDolphinAction,
                    INPUT_ACTION_TYPE.ON_PRESS_ONLY
                );
                
            }
        }
        
        
    }

    @Override
    public void update() {
        elapsedTime = System.currentTimeMillis() - prevTime;
        prevTime = System.currentTimeMillis();
        amount = elapsedTime * 0.03;
        Camera c = (engine.getRenderSystem()).getViewport("MAIN").getCamera();

        // adjust light
        spotlight.setLocation(dolphin.getLocalLocation());

        // decrement boost timer if it is above zero, otherwise reset
        if(speedBoostTimer > 0) {
            speedBoostTimer -= 0.1f * elapsedTime;
        }
        else {
            speedBoostTimer = 0;
        }

        // build and set HUD
		String counter = Integer.toString(prizesCollected);
		String display = "Prizes Collected = " + counter;
        Vector3f hudColor;
        if(speedBoostTimer > 0) {
            hudColor = new Vector3f(1,0,1);
        }
        else {
            hudColor = new Vector3f(1,0,0);
        }
		(engine.getHUDmanager()).setHUD1(display, hudColor, 500, 15);
        
        // get current cam/dolphin locations for later distance calculation
        dolphinLocation = dolphin.getLocalLocation();
        oldCameraLocation = c.getLocation();
        
        // update input manager and update locations
        if(ridingDolphin && speedBoostTimer > 0) {
            im.update((float)elapsedTime * 2.0f);
        }
        else {
            im.update((float)elapsedTime);
        }

        // calculate distance between camera's new location and dolphin
        newCameraLocation = c.getLocation();
        distanceBetweenAvatarAndCamera = calculateDistanceBetweenObjectAndCamera(dolphin);

        // camera follows dolphin if it is riding
        if(ridingDolphin) {
            positionCameraBehindAvatar();
        }
        // if camera is not riding and strays too far, reset to previous position
        else if(distanceBetweenAvatarAndCamera > 10) {
            c.setLocation(oldCameraLocation);
        }

        // diamond of power collection
        if(ridingDolphin && calculateDistanceBetweenObjectAndCamera(diamondOfPower) < 2) {
            speedBoostTimer = 1000;
            diamondOfPower.setLocalLocation(new Vector3f(((rng.nextFloat()) * 50) - 25, ((rng.nextFloat()) * 50) - 25, ((rng.nextFloat()) * 50) - 25));
        }

        // collect prize if one is close enough
        for(GameObject prize : prizes) {
            if(ridingDolphin == false && calculateDistanceBetweenObjectAndCamera(prize) < 1) {
                prize.setLocalLocation(new Vector3f(((rng.nextFloat()) * 100) - 50, ((rng.nextFloat()) * 100) - 50, ((rng.nextFloat()) * 100) - 50));
                prizesCollected++;
            }
        }
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
        cam.setLocation(location.add(up.mul(1.0f).add(forward.mul(-1.5f))));
    }

    private float calculateDistanceBetweenObjectAndCamera(GameObject object) {
        Vector3f objectLocation = object.getLocalLocation();

        double xs = objectLocation.x() - newCameraLocation.x();
        double ys = objectLocation.y() - newCameraLocation.y();
        double zs = objectLocation.z() - newCameraLocation.z();

        float result = (float)(Math.abs(Math.sqrt((Math.pow(xs, 2)) + (Math.pow(ys, 2)) + (Math.pow(zs, 2)))));
        return result;
    }

    public float getDistanceBetweenDolphinAndCamera() {
        return distanceBetweenAvatarAndCamera;
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