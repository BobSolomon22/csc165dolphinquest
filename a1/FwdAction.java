package a1;

import tage.*;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;
import org.joml.*;

public class FwdAction extends AbstractInputAction {
    private MyGame game;
    private GameObject avatar;
    private Vector3f oldPos, newPos;
    private Vector4f fwdDirection;

    public FwdAction(MyGame g) {
        game = g;
    }

    @Override
    public void performAction(float time, Event e) {
        avatar = game.getAvatar();
        oldPos = avatar.getWorldLocation();
        fwdDirection = new Vector4f(0f,0f,1f,1f);
        fwdDirection.mul(avatar.getWorldRotation());
        fwdDirection.mul(0.005f * time);
        newPos = oldPos.add(fwdDirection.x(), fwdDirection.y(), fwdDirection.z());
        avatar.setLocalLocation(newPos);
    }
}