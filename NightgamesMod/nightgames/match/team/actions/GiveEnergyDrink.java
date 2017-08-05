package nightgames.match.team.actions;

import nightgames.characters.Character;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.match.team.TeamMatch;

public class GiveEnergyDrink extends TeammateAction {

    private static final long serialVersionUID = 4714807951439177317L;

    public GiveEnergyDrink(TeamMatch match, Character self) {
        super("Give Energy Drink", match, self);
    }

    @Override
    public String describe(Character doer) {
        String msg = doer.getName() + " use <b>Give Energy Drink<\b>.\n";
        return msg + Global.format("<i>\"Hey {other:name}!\"</i> You turn to {self:name-do} just"
                        + " in time to catch the can of Energy Drink {self:pronoun} threw"
                        + " at you. You thank {self:direct-object} quickly and chug down the"
                        + " disgusting - but admittedly invigorating - drink.", self, getCaptain());
    }

    @Override
    void effects() {
        self.consume(Item.EnergyDrink, 1);
        getCaptain().getStamina().fill();
        getCaptain().getMojo().restore(30);
    }
    
    @Override
    boolean additionalRequirements() {
        return !getCaptain().getStamina().isFull() && self.has(Item.EnergyDrink);
    }

}
