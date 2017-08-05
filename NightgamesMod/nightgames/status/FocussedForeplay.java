package nightgames.status;

import com.google.gson.JsonObject;

import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.skills.Skill;

public class FocussedForeplay extends FocussedBase {

    public FocussedForeplay(Character affected) {
        super("Focussed on Foreplay", affected, "foreplay", "staying power in full-on sex");
    }

    @Override
    public double sensitivity(double x, BodyPart withPart, BodyPart targetPart, Skill skill) {
        if (withPart.isGenitalOrToy() && targetPart.isGenitalOrToy()) {
            return 2.5;
        }
        return -1;
        // The penalty for genital-on-genital contact is much greater than the resistance,
        // since it should be pretty easy to avoid most of the time.
    }

    @Override
    public double opponentSensitivity(double x, BodyPart withPart, BodyPart targetPart, Skill skill) {
        if (withPart.isGenitalOrToy() && targetPart.isGenitalOrToy()) {
            return -2.5;
        }
        return 1;
    }
    
    @Override
    public int gainmojo(int x) {
        return (int) (-x * .75);
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new FocussedForeplay(newAffected);
    }

    @Override
    public JsonObject saveToJson() {
        return null;
    }

    @Override
    public Status loadFromJson(JsonObject obj) {
        return null;
    }

}
