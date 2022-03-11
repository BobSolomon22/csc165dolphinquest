package a2;

import tage.*;
import tage.Light.LightType;
import tage.input.InputManager;
import tage.input.IInputManager.INPUT_ACTION_TYPE;
import tage.nodeControllers.*;
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
    private boolean linesRendered;
    private boolean resetReady;
    private Vector3f dolphinLocation;
    private Vector3f oldCameraLocation;
    private Vector3f newCameraLocation;
    private ArrayList<GameObject> prizes;
    private ArrayList<GameObject> miniPrizes;
    private float distanceBetweenAvatarAndCamera;
    private int prizesCollected;
    private int prizesCollectedThisRound;
    private float speedBoostTimer;

    // Rotation controllers
    private RotationController rc1, rc2, rc3, rcd;
    private ArrayList<RotationController> prizeRotationControllers;
    // Float controllers
    private FloatController fc1, fc2, fc3;
    private ArrayList<FloatController> prizeFloatControllers;
    // GameObject declarations
    private GameObject groundPlane, dolphin, prize1, prize2, prize3, miniPrize1, miniPrize2, miniPrize3, xAxis, yAxis, zAxis, diamondOfPower;
    // ObjShape declarations
    private ObjShape groundPlaneS, dolphinS, prize1S, prize2S, prize3S, miniPrize1S, miniPrize2S, miniPrize3S, xAxisS, yAxisS, zAxisS, diamondOfPowerS;
    // TextureImage declarations
    private TextureImage groundPlanetx, dolphintx, prize1tx, prize2tx, prize3tx, miniPrize1tx, miniPrize2tx, miniPrize3tx, diamondOfPowertx;
    // Light declarations
    private Light light1, spotlight;
    // Controllers
    private CameraOrbit3D orbitController;
    // Minimap camera
    private Camera miniCam;
    private Viewport miniMap;

    public MyGame() { super(); }

    public GameObject getAvatar() {
        return dolphin;
    }

    public Camera getCamera() {
        return (engine.getRenderSystem().getViewport("MAIN").getCamera());
    }

    public CameraOrbit3D getCameraController() {
        return orbitController;
    }

    public boolean isRidingDolphin() {
        return ridingDolphin;
    }

    public void toggleLinesRender() {
        if(linesRendered) {
            xAxis.getRenderStates().disableRendering();
            yAxis.getRenderStates().disableRendering();
            zAxis.getRenderStates().disableRendering();
            linesRendered = false;
        }
        else {
            xAxis.getRenderStates().enableRendering();
            yAxis.getRenderStates().enableRendering();
            zAxis.getRenderStates().enableRendering();
            linesRendered = true;
        }
    }

    public void initPrizes() {
        Vector3f prize1InitialLocation = new Vector3f(((rng.nextFloat()) * 100) - 50, 0.5f, ((rng.nextFloat()) * 100) - 50);
        Vector3f prize2InitialLocation = new Vector3f(((rng.nextFloat()) * 100) - 50, 1.0f, ((rng.nextFloat()) * 100) - 50);
        Vector3f prize3InitialLocation = new Vector3f(((rng.nextFloat()) * 100) - 50, 1.0f, ((rng.nextFloat()) * 100) - 50);

        prize1.setLocalLocation(prize1InitialLocation);
        prize2.setLocalLocation(prize2InitialLocation);
        prize3.setLocalLocation(prize3InitialLocation);

        rc1.disable();
        rc2.disable();
        rc3.disable();

        fc1.disable();
        fc2.disable();
        fc3.disable();

        for(GameObject prize : miniPrizes) {
            prize.getRenderStates().disableRendering();
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
        groundPlaneS = new Plane();
        dolphinS = new ImportedModel("dolphinHighPoly.obj");
        prize1S = new Cube();
        prize2S = new Torus();
        prize3S = new Sphere();
        miniPrize1S = new Cube();
        miniPrize2S = new Torus();
        miniPrize3S = new Sphere();
        xAxisS = new Line(new Vector3f(0f,0f,0f), new Vector3f(3f,0f,0f));
        yAxisS = new Line(new Vector3f(0f,0f,0f), new Vector3f(0f,3f,0f));
        zAxisS = new Line(new Vector3f(0f,0f,0f), new Vector3f(0f,0f,3f));
        diamondOfPowerS = new DiamondOfPower();
    }

    @Override
    public void loadTextures() {
        groundPlanetx = new TextureImage("plane.png");
        dolphintx = new TextureImage("Dolphin_HighPolyUV.png");
        prize1tx = new TextureImage("smile.png");
        prize2tx = new TextureImage("stripe.png");
        prize3tx = new TextureImage("divide.png");
        miniPrize1tx = new TextureImage("smile.png");
        miniPrize2tx = new TextureImage("stripe.png");
        miniPrize3tx = new TextureImage("divide.png");
        diamondOfPowertx = new TextureImage("diamondofpower.png");
    }

    @Override
    public void buildObjects() {
        // build ground plane
        groundPlane = new GameObject(GameObject.root(), groundPlaneS, groundPlanetx);
        groundPlane.setLocalTranslation((new Matrix4f()).translation(0,-1,0));
        groundPlane.setLocalScale((new Matrix4f()).scaling(100.0f));

        // build dolphin
        dolphin = new GameObject(GameObject.root(), dolphinS, dolphintx);
        dolphin.setLocalTranslation((new Matrix4f()).translation(0,0,0));
        dolphin.setLocalScale((new Matrix4f()).scaling(3.0f));

        // build prizes
        prize1 = new GameObject(GameObject.root(), prize1S, prize1tx);
        prize1.setLocalTranslation(new Matrix4f().translation(0,1,0));
        prize1.setLocalScale(new Matrix4f().scaling(0.3f));

        prize2 = new GameObject(GameObject.root(), prize2S, prize2tx);
        prize2.setLocalTranslation(new Matrix4f().translation(1,0,0));
        prize2.setLocalScale(new Matrix4f().scaling(0.5f));
        prize2.setLocalRotation(new Matrix4f().rotate(90, new Vector3f(1f,0f,0f)));
        prize2.getRenderStates().setTiling(1);

        prize3 = new GameObject(GameObject.root(), prize3S, prize3tx);
        prize3.setLocalTranslation(new Matrix4f().translation(-1,0,0));
        prize3.setLocalScale(new Matrix4f().scaling(0.5f));
        prize3.setLocalRotation(new Matrix4f().rotate(-90, new Vector3f(0f,1f,0f)));

        prizes = new ArrayList<GameObject>();
        prizes.add(prize1);
        prizes.add(prize2);
        prizes.add(prize3);

        // setup mini prizes
        float prizex = 3.0f * (float)Math.cos(Math.PI / 6);
        float prizez = 3.0f * (float)Math.sin(Math.PI / 6);

        miniPrize1 = new GameObject(dolphin, miniPrize1S, miniPrize1tx);
        miniPrize1.setLocalLocation(dolphin.getLocalLocation().add(new Vector3f(prizex, 0.0f, prizez)));
        miniPrize1.setLocalScale(new Matrix4f().scaling(0.05f));

        miniPrize2 = new GameObject(dolphin, miniPrize2S, miniPrize2tx);
        miniPrize2.setLocalLocation(dolphin.getLocalLocation().add(new Vector3f(-prizex, 0.0f, prizez)));
        miniPrize2.setLocalScale(new Matrix4f().scaling(0.075f));
        miniPrize2.setLocalRotation(new Matrix4f().rotate(90, new Vector3f(1f,0f,0f)));
        miniPrize2.getRenderStates().setTiling(1);

        miniPrize3 = new GameObject(dolphin, miniPrize3S, miniPrize3tx);
        miniPrize3.setLocalLocation(dolphin.getLocalLocation().add(new Vector3f(0.0f, 0.0f, -3.0f)));
        miniPrize3.setLocalScale(new Matrix4f().scaling(0.055f));
        miniPrize3.setLocalRotation(new Matrix4f().rotate(-90, new Vector3f(0f,1f,0f)));

        miniPrizes = new ArrayList<GameObject>();
        miniPrizes.add(miniPrize1);
        miniPrizes.add(miniPrize2);
        miniPrizes.add(miniPrize3);

        for(GameObject prize : miniPrizes) {
            prize.propagateTranslation(true);
            prize.propagateRotation(false);
        }
        

        // build axes
        xAxis = new GameObject(GameObject.root(), xAxisS);
        yAxis = new GameObject(GameObject.root(), yAxisS);
        zAxis = new GameObject(GameObject.root(), zAxisS);
        (xAxis.getRenderStates()).setColor(new Vector3f(1f,1f,0f));
        (yAxis.getRenderStates()).setColor(new Vector3f(0f,1f,0f));
        (zAxis.getRenderStates()).setColor(new Vector3f(0f,1f,1f));

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

        // setup camera orbit controller
        orbitController = new CameraOrbit3D(engine.getRenderSystem().getViewport("MAIN").getCamera(), dolphin);

        // add viewport for minimap
        (engine.getRenderSystem()).addViewport("MINIMAP", 0.75f, 0f, 0.25f, 0.25f);
        miniMap = (engine.getRenderSystem()).getViewport("MINIMAP");
        miniCam = miniMap.getCamera();

        miniMap.setHasBorder(true);
        miniMap.setBorderWidth(4);
        miniMap.setBorderColor(1.0f, 0.0f, 0.0f);

        miniCam.setLocation(new Vector3f(dolphin.getWorldLocation().x(),
                                         dolphin.getWorldLocation().y() + 10,
                                         dolphin.getWorldLocation().z()));
        miniCam.setU(new Vector3f(1,0,0));
        miniCam.setV(new Vector3f(0,0,1));
        miniCam.setN(new Vector3f(0,-1,0));

        // setup node controllers
        rc1 = new RotationController(engine, new Vector3f(0,1,0), 0.005f);
        rc2 = new RotationController(engine, new Vector3f(0,0,1), 0.005f);
        rc3 = new RotationController(engine, new Vector3f(0,1,0), 0.005f);
        rcd = new RotationController(engine, new Vector3f(0,1,0), 0.005f);
        rc1.addTarget(prize1);
        rc2.addTarget(prize2);
        rc3.addTarget(prize3);
        rc1.addTarget(miniPrize1);
        rc2.addTarget(miniPrize2);
        rc3.addTarget(miniPrize3);
        rcd.addTarget(diamondOfPower);

        (engine.getSceneGraph()).addNodeController(rc1);
        (engine.getSceneGraph()).addNodeController(rc2);
        (engine.getSceneGraph()).addNodeController(rc3);
        (engine.getSceneGraph()).addNodeController(rcd);

        prizeRotationControllers = new ArrayList<RotationController>();
        prizeRotationControllers.add(rc1);
        prizeRotationControllers.add(rc2);
        prizeRotationControllers.add(rc3);

        // initialize variables
        ridingDolphin = true;
        linesRendered = true;
        resetReady = false;
        prizesCollected = 0;
        prizesCollectedThisRound = 0;
        
        // setup float controllers using prizes' new locations
        fc1 = new FloatController(engine, prize1.getLocalLocation().y(), 0.5f, 2);
        fc2 = new FloatController(engine, prize2.getLocalLocation().y(), 0.5f, 2);
        fc3 = new FloatController(engine, prize3.getLocalLocation().y(), 0.5f, 2);
        fc1.addTarget(prize1);
        fc2.addTarget(prize2);
        fc3.addTarget(prize3);
        fc1.addTarget(miniPrize1);
        fc2.addTarget(miniPrize2);
        fc3.addTarget(miniPrize3);
        
        (engine.getSceneGraph()).addNodeController(fc1);
        (engine.getSceneGraph()).addNodeController(fc2);
        (engine.getSceneGraph()).addNodeController(fc3);

        prizeFloatControllers = new ArrayList<FloatController>();
        prizeFloatControllers.add(fc1);
        prizeFloatControllers.add(fc2);
        prizeFloatControllers.add(fc3);

        // randomly distribute prizes
        rng = new Random();
        initPrizes();

        // initialize diamond
        Vector3f diamondLocation = new Vector3f(((rng.nextFloat()) * 50) - 25, 1, ((rng.nextFloat()) * 50) - 25);
        diamondOfPower.setLocalLocation(diamondLocation);
        rcd.enable();

        // setup inputs
        im = engine.getInputManager();
        ArrayList<Controller> controllers = im.getControllers();

        FwdAction fwdAction = new FwdAction(this);
        BackAction backAction = new BackAction(this);
        LeftAction leftAction = new LeftAction(this);
        RightAction rightAction = new RightAction(this);
        BackNForthAction backNForthAction = new BackNForthAction(this);
        TurnAction turnAction = new TurnAction(this);
        OrbitAzimuthAction orbitAzimuthAction = new OrbitAzimuthAction(this);
        OrbitElevationAction orbitElevationAction = new OrbitElevationAction(this);
        OrbitZoomAction orbitZoomAction = new OrbitZoomAction(this);
        ToggleLinesAction toggleLinesAction = new ToggleLinesAction(this);
        MiniMapFwdAction miniMapFwdAction = new MiniMapFwdAction(this);
        MiniMapBackAction miniMapBackAction = new MiniMapBackAction(this);
        MiniMapLeftAction miniMapLeftAction = new MiniMapLeftAction(this);
        MiniMapRightAction miniMapRightAction = new MiniMapRightAction(this);
        MiniMapZoomInAction miniMapZoomInAction = new MiniMapZoomInAction(this);
        MiniMapZoomOutAction miniMapZoomOutAction = new MiniMapZoomOutAction(this);

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
                // controller azimuth
                im.associateAction(
                    c,
                    net.java.games.input.Component.Identifier.Axis.RX,
                    orbitAzimuthAction,
                    INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN
                );
                // controller elevation
                im.associateAction(
                    c,
                    net.java.games.input.Component.Identifier.Axis.RY,
                    orbitElevationAction,
                    INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN
                );
                // controller zoom
                im.associateAction(
                    c,
                    net.java.games.input.Component.Identifier.Axis.Z,
                    orbitZoomAction,
                    INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN
                );
                // controller toggle lines
                im.associateAction(
                    c,
                    net.java.games.input.Component.Identifier.Button._6,
                    toggleLinesAction,
                    INPUT_ACTION_TYPE.ON_PRESS_ONLY
                );
                // controller minimap forward
                im.associateAction(
                    c,
                    net.java.games.input.Component.Identifier.Button._3,
                    miniMapFwdAction,
                    INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN
                );
                // controller minimap backward
                im.associateAction(
                    c,
                    net.java.games.input.Component.Identifier.Button._0,
                    miniMapBackAction,
                    INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN
                );
                // controller minimap left
                im.associateAction(
                    c,
                    net.java.games.input.Component.Identifier.Button._2,
                    miniMapLeftAction,
                    INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN
                );
                // controller minimap right
                im.associateAction(
                    c,
                    net.java.games.input.Component.Identifier.Button._1,
                    miniMapRightAction,
                    INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN
                );
                // controller minimap zoom in
                im.associateAction(
                    c,
                    net.java.games.input.Component.Identifier.Button._5,
                    miniMapZoomInAction,
                    INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN
                );
                // controller minimap zoom out
                im.associateAction(
                    c,
                    net.java.games.input.Component.Identifier.Button._4,
                    miniMapZoomOutAction,
                    INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN
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

        // build and set HUD
		String counter = Integer.toString(prizesCollected);
		String display = "Prizes Collected = " + counter;
        String locationDisplay = "Dolphin Location = (" + (int)dolphin.getWorldLocation().x() +
                                                ", " + (int)dolphin.getWorldLocation().y() +
                                                ", " + (int)dolphin.getWorldLocation().z() +
                                                ")";
        Vector3f hudColor;
        if(speedBoostTimer > 0) {
            hudColor = new Vector3f(1,0,1);
            miniMap.setBorderColor(1.0f, 0.0f, 1.0f);
        }
        else {
            hudColor = new Vector3f(1,0,0);
            miniMap.setBorderColor(1.0f, 0.0f, 0.0f);
        }

        float mainRelativeLeft = engine.getRenderSystem().getViewport("MAIN").getRelativeLeft();
        float mainRelativeBottom = engine.getRenderSystem().getViewport("MAIN").getRelativeBottom();
        float mainActualWidth = engine.getRenderSystem().getViewport("MAIN").getActualWidth();
        float mainActualHeight = engine.getRenderSystem().getViewport("MAIN").getActualHeight();
        float miniMapRelativeLeft = engine.getRenderSystem().getViewport("MINIMAP").getRelativeLeft();
        float miniMapRelativeBottom = engine.getRenderSystem().getViewport("MINIMAP").getRelativeBottom();
        float miniMapActualWidth = engine.getRenderSystem().getViewport("MINIMAP").getActualWidth();
        float miniMapActualHeight = engine.getRenderSystem().getViewport("MINIMAP").getActualHeight();

		(engine.getHUDmanager()).setHUD1(display, hudColor, (int)(mainRelativeLeft * mainActualWidth) + 5,  (int)(mainRelativeBottom * mainActualHeight) + 5);
        (engine.getHUDmanager()).setHUD2(locationDisplay, hudColor, (int)((miniMapRelativeLeft * mainActualWidth) + 5), (int)(miniMapRelativeBottom * miniMapActualHeight) + 5);

        // decrement boost timer if it is above zero, otherwise reset
        if(speedBoostTimer > 0) {
            speedBoostTimer -= 0.1f * elapsedTime;
        }
        else {
            speedBoostTimer = 0;
        }
        
        // get current cam/dolphin locations for later distance calculation
        dolphinLocation = dolphin.getLocalLocation();
        oldCameraLocation = c.getLocation();
        
        // update input manager and update locations
        if(speedBoostTimer > 0) {
            im.update((float)elapsedTime * 2.0f);
        }
        else {
            im.update((float)elapsedTime);
        }

        // camera follows dolphin
        orbitController.updateCameraPosition();

        // diamond of power collection
        if(calculateDistanceBetweenObjects(dolphin, diamondOfPower) < 2) {
            speedBoostTimer = 1000;
            if(resetReady) {
                initPrizes();
                resetReady = false;
                prizesCollectedThisRound = 0;
            }
            diamondOfPower.setLocalLocation(new Vector3f(((rng.nextFloat()) * 50) - 25, 1, ((rng.nextFloat()) * 50) - 25));
        }

        // collect prize if one is close enough
        for(GameObject prize : prizes) {
            int prizeNumber = prizes.indexOf(prize);
            if(!(prizeRotationControllers.get(prizeNumber).isEnabled()) &&
            calculateDistanceBetweenObjects(dolphin, prize) < 2) {
                prizeRotationControllers.get(prizeNumber).enable();
                prizeFloatControllers.get(prizeNumber).enable();
                miniPrizes.get(prizeNumber).getRenderStates().enableRendering();
                prizesCollected++;
                prizesCollectedThisRound++;
            }
        }

        // move diamond of power to reset position once all prizes are collected
        if(prizesCollectedThisRound >= 3) {
            diamondOfPower.setLocalLocation(new Vector3f(0.0f, 1.0f, 0.0f));
            resetReady = true;
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

    private float calculateDistanceBetweenObjects(GameObject object1, GameObject object2) {
        Vector3f objectLocation1 = object1.getLocalLocation();
        Vector3f objectLocation2 = object2.getLocalLocation();

        double xs = objectLocation1.x() - objectLocation2.x();
        double ys = objectLocation1.y() - objectLocation2.y();
        double zs = objectLocation1.z() - objectLocation2.z();

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