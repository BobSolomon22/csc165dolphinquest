package a2;

import tage.*;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;
import org.joml.*;

public class RightAction extends AbstractInputAction {
    private MyGame game;
    private GameObject avatar;
    private Camera camera;
    private Vector4f oldUp;
    private Matrix4f rotAroundAvatarUp, oldRotation, newRotation;

    public RightAction(MyGame g) {
        game = g;
    }

    @Override
    public void performAction(float time, Event e) {
        float keyValue = e.getValue();
        if (keyValue > -0.2 && keyValue < 0.2) {
            return;
        }

        if(game.isRidingDolphin()) {
            avatar = game.getAvatar();
            avatar.yaw(-0.003f * time);
        }
        else {
            camera = game.getCamera();
            camera.yaw(-0.003f * time);
        }
    }
}