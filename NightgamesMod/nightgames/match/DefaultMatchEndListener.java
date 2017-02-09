package nightgames.match;

import java.util.Optional;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.CombatListener;

public class DefaultMatchEndListener extends CombatListener {

    private final Match match;
    
    public DefaultMatchEndListener(Combat c, Match match) {
        super(c);
        this.match = match;
    }

    @Override
    public void postEnd(Optional<Character> winner) {
        if (winner.isPresent()) {
            match.mercy.get(winner.get()).add(c.getOpponent(winner.get()));
            match.score(winner.get(), 1, Optional.of(" for defeating " + c.getOpponent(winner.get()).getName()));
        } else {
            match.mercy.get(c.p1).add(c.p2);
            match.mercy.get(c.p2).add(c.p1);
        }
    }
}
