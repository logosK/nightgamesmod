package nightgames.match;

import java.util.Collections;
import java.util.List;

import nightgames.characters.Character;
import nightgames.characters.Player;
import nightgames.global.Global;
import nightgames.global.Scene;

public abstract class Postmatch implements Scene {

    protected final Player player;
    protected final List<Character> combatants;
    
    protected Postmatch(List<Character> combatants) {
        this.combatants = Collections.unmodifiableList(combatants);
        player = Global.getPlayer();
    }
    
    protected abstract void runInternal();
    
    protected final void run() {
        runInternal();
        Global.endNight();
    }
    
}
