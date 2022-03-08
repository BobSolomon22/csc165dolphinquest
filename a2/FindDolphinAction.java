package a2;

import tage.Camera;
import tage.GameObject;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;

public class FindDolphinAction extends AbstractInputAction {
    private MyGame game;
    private GameObject avatar;
    private Camera camera;

    public FindDolphinAction(MyGame g) {
        game = g;
    }

    @Override
    public void performAction(float time, Event e) {
        avatar = game.getAvatar();
        camera = game.getCamera();

        if(game.isRidingDolphin() == false) {
            camera.lookAt(avatar);
        }
    }
}