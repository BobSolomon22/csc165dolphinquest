package a2;

import tage.*;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;
import org.joml.*;

public class MountAction extends AbstractInputAction {
    private MyGame game;
    private GameObject avatar;
    private Camera camera;

    public MountAction(MyGame g) {
        game = g;
    }

    @Override
    public void performAction(float time, Event e) {
        avatar = game.getAvatar();
        camera = game.getCamera();
        Vector3f location = avatar.getLocalLocation();
        Vector3f forward = avatar.getLocalForwardVector();
        Vector3f up = avatar.getLocalUpVector();
        Vector3f right = avatar.getLocalRightVector();

        if(game.isRidingDolphin()) {
            camera.setLocation(location.add(right.mul(1.0f).add(forward.mul(1.5f))));
            game.toggleRide();
        }
        else if(game.getDistanceBetweenDolphinAndCamera() < 3) {
            game.toggleRide();
        }
    }
}
