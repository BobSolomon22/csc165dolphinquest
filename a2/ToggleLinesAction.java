package a2;

import tage.*;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;

public class ToggleLinesAction extends AbstractInputAction {
    private MyGame game;

    public ToggleLinesAction(MyGame g) {
        game = g;
    }

    @Override
    public void performAction(float time, Event e) {
        game.toggleLinesRender();
    }
}
