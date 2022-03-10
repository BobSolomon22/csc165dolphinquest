package a2;

import tage.*;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;
import org.joml.*;

public class MiniMapBackAction extends AbstractInputAction {
    private MyGame game;
    private Camera camera;
    private Vector3f oldPos, newPos, fwdDirection;

    public MiniMapBackAction(MyGame g) {
        game = g;
    }

    @Override
    public void performAction(float time, Event e) {
        camera = game.getEngine().getRenderSystem().getViewport("MINIMAP").getCamera();
        oldPos = camera.getLocation();
        fwdDirection = camera.getV();
        fwdDirection.mul(-0.005f * time);
        newPos = oldPos.add(fwdDirection.x(), fwdDirection.y(), fwdDirection.z());
        camera.setLocation(newPos);
    }
}