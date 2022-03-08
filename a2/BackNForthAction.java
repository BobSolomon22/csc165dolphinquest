package a2;

import tage.*;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;
import org.joml.*;

public class BackNForthAction extends AbstractInputAction {
    private MyGame game;
    private GameObject avatar;
    private Camera camera;
    private Vector3f oldPos, newPos, fwdDirectionCamera;
    private Vector4f fwdDirectionAvatar;

    public BackNForthAction(MyGame g) {
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
            oldPos = avatar.getWorldLocation();
            fwdDirectionAvatar = new Vector4f(0f,0f,1f,1f);
            fwdDirectionAvatar.mul(avatar.getWorldRotation());

            if(keyValue < -0.2) {
                fwdDirectionAvatar.mul(0.005f * time);
            }
            else {
                fwdDirectionAvatar.mul(-0.005f * time);
            }
            newPos = oldPos.add(fwdDirectionAvatar.x(), fwdDirectionAvatar.y(), fwdDirectionAvatar.z());
            avatar.setLocalLocation(newPos);
        }
        else {
            camera = game.getCamera();
            oldPos = camera.getLocation();
            fwdDirectionCamera = camera.getN();
            
            if(keyValue < 0.02) {
                fwdDirectionCamera.mul(0.005f * time);
            }
            else {
                fwdDirectionCamera.mul(-0.005f * time);
            }
            newPos = oldPos.add(fwdDirectionCamera.x(), fwdDirectionCamera.y(), fwdDirectionCamera.z());
            camera.setLocation(newPos);
        }

        
    }
}
