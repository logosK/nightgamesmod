package nightgames.stance;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.skills.BreastSmother;
import nightgames.skills.Escape;
import nightgames.skills.Finger;
import nightgames.skills.FondleBreasts;
import nightgames.skills.Nothing;
import nightgames.skills.Nurple;
import nightgames.skills.Skill;
import nightgames.skills.Struggle;
import nightgames.skills.Suckle;
import nightgames.skills.Tickle;
import nightgames.skills.Wait;
import nightgames.skills.damage.DamageType;

public class BreastSmothering extends AbstractFacingStance {
    public BreastSmothering(Character top, Character bottom) {
        super(top, bottom, Stance.breastsmothering);
    }
    
    public String image() {
        return "breast_smother.png";
    }

    @Override
    public String describe(Combat c) {
        return Global.format("{self:subject-action:keep} {other:name-possessive} face between {self:possessive} tits, with {self:possessive} large breasts fully encompassing {other:possessive} view. {other:SUBJECT} cannot even breathe except for the short pauses when {self:subject-action:allow|allows} {other:direct-object} to by loosening {self:possessive} grip.", top, bottom);
    } 

    @Override
    public int distance() {
        return 1;
    }

    @Override
    public boolean mobile(Character c) {
        return c != bottom;
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
        return c != bottom;
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
    public Position.Dominance dominance() {
        return Position.Dominance.ABSURD;
    }
    
    @Override
    public void decay(Combat c) {
        time++;
        bottom.weaken(c, (int) top.modifyDamage(DamageType.temptation, bottom, 3));
        top.emote(Emotion.dominant, 10);
    }
    
    @Override
    public Optional<Collection<Skill>> allowedSkills(Combat c, Character self) {
        if (self != bottom) {
            return Optional.empty();
        } else {
            Collection<Skill> avail = new HashSet<Skill>();
            avail.add(new FondleBreasts(bottom));
            avail.add(new Suckle(bottom));
            avail.add(new Tickle(bottom));
            avail.add(new Finger(bottom));
            avail.add(new Nurple(bottom));
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
    public void struggle(Combat c, Character struggler) {
        c.write(struggler, Global.format("{self:SUBJECT-ACTION:attempt} to struggle out of {other:name-possessive} {other:body-part:breasts}, "
                        + "but {other:pronoun-action:have} other ideas.", struggler, top));
        (new BreastSmother(top)).resolve(c, bottom);
        super.struggle(c, struggler);
    }

    @Override
    public void escape(Combat c, Character escapee) {
        c.write(escapee, Global.format("{self:SUBJECT-ACTION:attempt} to extract {self:reflective} out of {other:name-possessive} {other:body-part:breasts}, "
                        + "but {other:pronoun-action:have} other ideas.", escapee, top));
        (new BreastSmother(top)).resolve(c, bottom);
        super.escape(c, escapee);
    }
}
