package nightgames.match.team;

import nightgames.areas.Area;
import nightgames.characters.Character;
import nightgames.match.defaults.DefaultEncounter;

public class TeamEncounter extends DefaultEncounter {

    public TeamEncounter(Character first, Character second, Area location) {
        super(first, second, location);
    }

    @Override
    public boolean checkIntrude(Character c) {
        return false;
    }

    @Override
    public void intrude(Character intruder, Character assist) {
        throw new IllegalStateException(intruder.getTrueName() + " tried to help " + assist.getTrueName() + " in 3p in "
                        + intruder.location().name + " (Team Match)");
    }

}
