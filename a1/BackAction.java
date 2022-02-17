package a1;

import tage.*;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;
import org.joml.*;

public class BackAction extends AbstractInputAction {
    private MyGame game;
    private GameObject avatar;
    private Vector3f oldPos, newPos;
    private Vector4f backDirection;

    public BackAction(MyGame g) {
        game = g;
    }

    @Override
    public void performAction(float time, Event e) {
        avatar = game.getAvatar();
        oldPos = avatar.getWorldLocation();
        backDirection = new Vector4f(0f,0f,1f,1f);
        backDirection.mul(avatar.getWorldRotation());
        backDirection.mul(-0.01f);
        newPos = oldPos.add(backDirection.x(), backDirection.y(), backDirection.z());
        avatar.setLocalLocation(newPos);
    }
}