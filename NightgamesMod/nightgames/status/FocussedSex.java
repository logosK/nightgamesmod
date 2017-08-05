package nightgames.status;

import com.google.gson.JsonObject;

import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.skills.Skill;

public class FocussedSex extends FocussedBase {

    public FocussedSex(Character affected) {
        super("Focussed on Sex", affected, "sex", "{self:possessive} foreplay skills");
    }

    @Override
    public double sensitivity(double x, BodyPart withPart, BodyPart targetPart, Skill skill) {
        if (withPart.isGenitalOrToy() && targetPart.isGenitalOrToy()) {
            return -1.25;
        }
        return 1.5;
    }

    @Override
    public double opponentSensitivity(double x, BodyPart withPart, BodyPart targetPart, Skill skill) {
        if (withPart.isGenitalOrToy() && targetPart.isGenitalOrToy()) {
            return 1;
        }
        return -1.5;
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new FocussedSex(newAffected);
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
