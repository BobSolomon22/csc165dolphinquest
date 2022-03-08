package a2;

import tage.*;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;
import org.joml.*;

public class TurnAction extends AbstractInputAction {
    private MyGame game;
    private GameObject avatar;
    private Camera camera;

    public TurnAction(MyGame g) {
        game = g;
    }

    @Override
    public void performAction(float time, Event e) {
        float keyValue = e.getValue();
        if (keyValue > -0.5 && keyValue < 0.5) {
            return;
        }

        avatar = game.getAvatar();
        camera = game.getCamera();

        if(game.isRidingDolphin()) {
            if(keyValue > 0.3) {
                avatar.yaw(-0.003f * time);
            }
            else {
                avatar.yaw(0.003f * time);
            }
        }
        else {
            if(keyValue > 0.3) {
                camera.yaw(-0.003f * time);
            }
            else {
                camera.yaw(0.003f * time);
            }
        }
    }
}