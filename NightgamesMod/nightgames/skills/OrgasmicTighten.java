package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.stance.Stance;
import nightgames.status.BodyFetish;

public class OrgasmicTighten extends Tighten {
    public OrgasmicTighten(Character self) {
        super("Orgasmic Tighten", self);
        removeTag(SkillTag.pleasureSelf);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return false;
    }

    @Override
    public BodyPart getSelfOrgan(Combat c, Character target) {
        if (c.getStance().anallyPenetratedBy(c, getSelf(), target)) {
            return getSelf().body.getRandom("ass");
        } else if (c.getStance().vaginallyPenetratedBy(c, getSelf(), target)) {
            return getSelf().body.getRandomPussy();
        } else {
            return null;
        }
    }

    @Override
    public int[] getDamage(Combat c, Character target) {
        int[] result = new int[2];

        int m = Global.random(25, 40) + Math.min(getSelf().get(Attribute.Power) / 3, 20);
        result[0] = m;
        result[1] = 0;

        return result;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        BodyPart selfO = getSelfOrgan(c, target);
        BodyPart targetO = getTargetOrgan(c, target);
        Result result;
        if (c.getStance().anallyPenetratedBy(c, getSelf(), target)) {
            result = Result.anal;
        } else {
            result = Result.normal;
        }

        writeOutput(c, result, target);

        int[] m = getDamage(c, target);
        assert (m.length >= 2);

        if (m[0] != 0)
            target.body.pleasure(getSelf(), selfO, targetO, m[0], c, this);
        if (m[1] != 0)
            getSelf().body.pleasure(target, targetO, selfO, m[1], -10000, c, false, this);
        if (selfO.isType("ass") && Global.random(100) < 2 + getSelf().get(Attribute.Fetish)) {
            target.add(c, new BodyFetish(target, getSelf(), "ass", .25));
        }
        return true;
    }

    @Override
    public int getMojoBuilt(Combat c) {
        return 0;
    }

    @Override
    public Skill copy(Character user) {
        return new OrgasmicTighten(user);
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (c.getStance().en == Stance.anal) {
            return Global.format("While cumming {self:name-possessive} spasming backdoor seems to urge {other:name-do} to do the same.",
                            getSelf(), target);
        } else {
            return Global.format("While cumming {self:name-possessive} spasming honeypot seems to urge {other:name-do} to do the same.",
                            getSelf(), target);
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return deal(c, damage, modifier, target);
    }

    @Override
    public String describe(Combat c) {
        return "Squeeze opponent's dick, no pleasure to self";
    }

    @Override
    public boolean makesContact(Combat c) {
        return true;
    }
    
    @Override
    public Stage getStage() {
        return Stage.FINISHER;
    }
}
