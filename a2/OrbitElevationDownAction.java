package a2;

import tage.*;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;

import org.joml.*;

public class OrbitElevationDownAction extends AbstractInputAction {
    private MyGame game;
    private CameraOrbit3D orbitController;

    public OrbitElevationDownAction(MyGame g) {
        game = g;
    }

    @Override
    public void performAction(float time, Event e) {
        orbitController = game.getCameraController();

        float pitchAmount;

        if(e.getValue() > 0.5) {
            pitchAmount = -0.1f * time;
        }
        else {
            pitchAmount = 0.0f * time;
        }

        float currentCameraElevation = orbitController.getCameraElevation();

        if(currentCameraElevation + pitchAmount < 20.0f || currentCameraElevation + pitchAmount > 85.0f) {
            pitchAmount = 0.0f * time;
        }

        orbitController.setCameraElevation(currentCameraElevation + pitchAmount);
        orbitController.updateCameraPosition();
    }
}
