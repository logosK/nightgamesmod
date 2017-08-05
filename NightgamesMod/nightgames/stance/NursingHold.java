package nightgames.stance;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.skills.Escape;
import nightgames.skills.Nothing;
import nightgames.skills.Skill;
import nightgames.skills.Struggle;
import nightgames.skills.Suckle;
import nightgames.skills.Wait;
import nightgames.skills.damage.DamageType;

public class NursingHold extends AbstractFacingStance {
    public NursingHold(Character top, Character bottom) {
        super(top, bottom, Stance.nursing);
    }

    @Override
    public String describe(Combat c) {
        if (top.human()) {
            return "You are cradling " + bottom.nameOrPossessivePronoun()
                            + " head in your lap with your breasts dangling in front of " + bottom.directObject();
        } else {
            return String.format("%s is holding %s head in %s lap, with %s enticing "
                            + "breasts right in front of %s mouth.", top.subject(),
                            bottom.nameOrPossessivePronoun(), top.possessiveAdjective(),
                            top.possessiveAdjective(), bottom.possessiveAdjective());
        }
    }

    @Override
    public boolean mobile(Character c) {
        return c != bottom;
    }

    @Override
    public String image() {
        return "nursing.jpg";
    }

    @Override
    public boolean kiss(Character c, Character target) {
        return target == top && c != bottom;
    }

    @Override
    public boolean dom(Character c) {
        return c == top;
    }

    @Override
    public boolean sub(Character c) {
        return c == bottom;
    }

    @Override
    public boolean reachTop(Character c) {
        return true;
    }

    @Override
    public boolean reachBottom(Character c) {
        return c != bottom;
    }

    @Override
    public boolean prone(Character c) {
        return c == bottom;
    }

    @Override
    public boolean feet(Character c, Character target) {
        return target == bottom && c != top && c != bottom;
    }

    @Override
    public boolean oral(Character c, Character target) {
        return target == bottom && c != top && c != bottom;
    }

    @Override
    public boolean behind(Character c) {
        return false;
    }

    @Override
    public boolean inserted(Character c) {
        return false;
    }

    @Override
    public void decay(Combat c) {
        time++;
        bottom.weaken(c, (int) top.modifyDamage(DamageType.temptation, bottom, 3));
        top.emote(Emotion.dominant, 10);
    }

    @Override
    public float priorityMod(Character self) {
        return dom(self) ? self.has(Trait.lactating) ? 5 : 2 : 0;
    }

    @Override
    public Optional<Collection<Skill>> allowedSkills(Combat c, Character self) {
        if (self != bottom) {
            return Optional.empty();
        } else {
            Collection<Skill> avail = new HashSet<Skill>();
            avail.add(new Suckle(bottom));
            avail.add(new Escape(bottom));
            avail.add(new Struggle(bottom));
            avail.add(new Nothing(bottom));
            avail.add(new Wait(bottom));
            return Optional.of(avail);
        }
    }

    @Override
    public boolean faceAvailable(Character target) {
        return target == top;
    }

    @Override
    public double pheromoneMod(Character self) {
        return 3;
    }
    
    @Override
    public Position.Dominance dominance() {
        return Position.Dominance.AVERAGE;
    }
    @Override
    public int distance() {
        return 1;
    }

    @Override
    public void struggle(Combat c, Character struggler) {
        if (struggler.human()) {
            c.write(struggler, "You try to free yourself from " + top.getName()
                            + ", but she pops a teat into your mouth and soon you're sucking like a newborn again.");
        } else if (c.shouldPrintReceive(top, c)) {
            c.write(struggler, String.format("%s struggles against %s, but %s %s %s nipple "
                            + "against %s mouth again, forcing %s to suckle.", struggler.subject(),
                            top.nameDirectObject(), top.pronoun(), top.action("presses"),
                            top.possessiveAdjective(), struggler.possessiveAdjective(),
                            struggler.directObject()));
        }
        (new Suckle(struggler)).resolve(c, top);
        super.struggle(c, struggler);
    }

    @Override
    public void escape(Combat c, Character escapee) {
        c.write(escapee, Global.format("{self:SUBJECT-ACTION:try} to escape {other:name-possessive} hold, but with"
                        + " {other:direct-object} impressive chest in front of {self:possessive} face, {self:pronoun-action:are} easily convinced to stop.",
                        escapee, top));
        (new Suckle(escapee)).resolve(c, top);
        super.escape(c, escapee);
    }
}
