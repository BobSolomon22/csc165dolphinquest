package a2;

import tage.*;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;

public class OrbitResetAction extends AbstractInputAction {
    private MyGame game;
    private CameraOrbit3D orbitController;

    public OrbitResetAction(MyGame g) {
        game = g;
    }

    @Override
    public void performAction(float time, Event e) {
        orbitController = game.getCameraController();

        orbitController.initCameraPosition();
    }
}
