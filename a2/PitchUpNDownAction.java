package a2;

import tage.*;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;
import org.joml.*;

public class PitchUpNDownAction extends AbstractInputAction {
    private MyGame game;
    private GameObject avatar;
    private Camera camera;

    public PitchUpNDownAction(MyGame g) {
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
                avatar.pitch(-0.003f * time);
            }
            else {
                avatar.pitch(0.003f * time);
            }
        }
        else {
            if(keyValue > 0.3) {
                camera.pitch(-0.003f * time);
            }
            else {
                camera.pitch(0.003f * time);
            }
        }
    }
}
