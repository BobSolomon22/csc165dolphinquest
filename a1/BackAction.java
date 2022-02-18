package a1;

import tage.*;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;
import org.joml.*;

public class BackAction extends AbstractInputAction {
    private MyGame game;
    private GameObject avatar;
    private Camera camera;
    private Vector3f oldPos, newPos, fwdDirectionCamera;
    private Vector4f fwdDirectionAvatar;

    public BackAction(MyGame g) {
        game = g;
    }

    @Override
    public void performAction(float time, Event e) {
        avatar = game.getAvatar();
        camera = game.getCamera();
        if(game.isRidingDolphin()) {
            oldPos = avatar.getWorldLocation();
            fwdDirectionAvatar = new Vector4f(0f,0f,1f,1f);
            fwdDirectionAvatar.mul(avatar.getWorldRotation());
            fwdDirectionAvatar.mul(-0.005f * time);
            newPos = oldPos.add(fwdDirectionAvatar.x(), fwdDirectionAvatar.y(), fwdDirectionAvatar.z());
            avatar.setLocalLocation(newPos);
        }
        else {
            oldPos = camera.getLocation();
            fwdDirectionCamera = camera.getN();
            fwdDirectionCamera.mul(-0.005f * time);
            newPos = oldPos.add(fwdDirectionCamera.x(), fwdDirectionCamera.y(), fwdDirectionCamera.z());
            camera.setLocation(newPos);
        }
    }
}