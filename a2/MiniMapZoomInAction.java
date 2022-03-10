package a2;

import tage.*;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;
import org.joml.*;

public class MiniMapZoomInAction extends AbstractInputAction {
    private MyGame game;
    private Camera camera;
    private Vector3f oldPos, newPos, fwdDirection;

    public MiniMapZoomInAction(MyGame g) {
        game = g;
    }

    @Override
    public void performAction(float time, Event e) {
        camera = game.getEngine().getRenderSystem().getViewport("MINIMAP").getCamera();
        oldPos = camera.getLocation();
        fwdDirection = camera.getN();
        fwdDirection.mul(0.005f * time);
        newPos = oldPos.add(fwdDirection.x(), fwdDirection.y(), fwdDirection.z());
        
        if(newPos.y() < 5.0f) {
            newPos = camera.getLocation();
        }
        
        camera.setLocation(newPos);
    }
}