package nightgames.global.start;

import nightgames.global.Global;
import nightgames.modifier.standard.NoModifier;

public class DefaultStart implements GameStarter {

    @Override
    public void startGame() {
        Global.setUpMatch(new NoModifier());
    }

}
