package nightgames.status;

import java.util.Optional;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;

public class CockChoked extends DurationStatus {
    Character other;

    public CockChoked(Character affected, Character other, int duration) {
        super("Cock Choked", affected, duration);
        this.other = other;
        flag(Stsflag.orgasmseal);
        flag(Stsflag.debuff);
        flag(Stsflag.purgable);
    }

    @Override
    public String initialMessage(Combat c, Optional<Status> replacement) {
        return String.format("%s now preventing %s from cumming.\n", other.subjectAction("are", "is"),
                        affected.subject());
    }

    @Override
    public String describe(Combat c) {
        return String.format("%s preventing %s from cumming.\n", other.subjectAction("are", "is"), affected.subject());
    }

    @Override
    public float fitnessModifier() {
        if (affected.getArousal().percent() > 80) {
            return -10;
        }
        return 0;
    }

    @Override
    public int mod(Attribute a) {
        return 0;
    }

    @Override
    public void onRemove(Combat c, Character other) {
        affected.addlist.add(new Wary(affected, 2));
    }

    @Override
    public int regen(Combat c) {
        super.regen(c);
        if (affected.getArousal().percent() > 80) {
            affected.emote(Emotion.desperate, 10);
            affected.emote(Emotion.horny, 10);
        }
        return 0;
    }

    @Override
    public int damage(Combat c, int x) {
        return 0;
    }

    @Override
    public double pleasure(Combat c, BodyPart withPart, BodyPart targetPart, double x) {
        return 0;
    }

    @Override
    public int weakened(Combat c, int x) {
        return 0;
    }

    @Override
    public int tempted(Combat c, int x) {
        return 0;
    }

    @Override
    public int evade() {
        return 0;
    }

    @Override
    public int escape() {
        return 0;
    }

    @Override
    public int gainmojo(int x) {
        return 0;
    }

    @Override
    public int spendmojo(int x) {
        return 0;
    }

    @Override
    public int counter() {
        return 0;
    }

    @Override
    public String toString() {
        return "Cock Choked";
    }

    @Override
    public int value() {
        return 0;
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new CockChoked(newAffected, newOther, getDuration());
    }

    @Override  public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getClass().getSimpleName());
        obj.addProperty("duration", getDuration());
        return obj;
    }

    @Override public Status loadFromJson(JsonObject obj) {
        return new CockChoked(null, null, obj.get("duration").getAsInt());
    }
}
