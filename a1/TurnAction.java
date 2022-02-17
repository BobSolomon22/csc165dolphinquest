package a1;

import tage.*;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;
import org.joml.*;

public class TurnAction extends AbstractInputAction {
    private MyGame game;
    private GameObject avatar;
    private Vector4f oldUp;
    private Matrix4f rotAroundAvatarUp, oldRotation, newRotation;

    public TurnAction(MyGame g) {
        game = g;
    }

    @Override
    public void performAction(float time, Event e) {
        float keyValue = e.getValue();
        if (keyValue > -0.2 && keyValue < 0.2) {
            return;
        }

        avatar = game.getAvatar();
        oldRotation = new Matrix4f(avatar.getWorldRotation());
        oldUp = new Vector4f(0f,1f,0f,1f).mul(oldRotation);

        if(keyValue > 0.2) {
            rotAroundAvatarUp = new Matrix4f().rotation(-0.005f, new Vector3f(oldUp.x(), oldUp.y(), oldUp.z()));
        }
        else {
            rotAroundAvatarUp = new Matrix4f().rotation(0.005f, new Vector3f(oldUp.x(), oldUp.y(), oldUp.z()));
        }
        newRotation = oldRotation;
        newRotation.mul(rotAroundAvatarUp);
        avatar.setLocalRotation(newRotation);
    }
}