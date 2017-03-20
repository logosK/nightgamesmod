package nightgames.match;

import java.util.Optional;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.CombatListener;
import nightgames.global.Global;

public class DefaultMatchEndListener extends CombatListener {

    private final Match match;
    
    public DefaultMatchEndListener(Combat c, Match match) {
        super(c);
        this.match = match;
    }

    @Override
    public void postEnd(Optional<Character> winner) {
        if (winner.isPresent()) {
            Global.getMatch().haveMercy(winner.get(), c.getOpponent(winner.get()));
            match.score(winner.get(), 1, Optional.of(" for defeating " + c.getOpponent(winner.get()).getName()));
        } else {
            Global.getMatch().haveMercy(c.p1, c.p2);
            Global.getMatch().haveMercy(c.p2, c.p1);
        }
    }
}
