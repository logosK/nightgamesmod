package nightgames.stance;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class Mount extends AbstractFacingStance {

    public Mount(Character top, Character bottom) {
        super(top, bottom, Stance.mount);
    }

    @Override
    public String describe(Combat c) {
        if (top.human()) {
            return "You're on top of " + bottom.getName() + ".";
        } else {
            return String.format("%s straddling %s, with %s enticing breasts right in front of %s.",
                            top.subjectAction("are", "is"), bottom.nameDirectObject(),
                            top.possessiveAdjective(), bottom.directObject());
        }
    }

    @Override
    public String image() {
        if (!top.hasPussy()) {return "mount_m.jpg";}
        if (bottom.hasPussy() && Math.random()<0.5) {return "mount_ff.jpg";}
        else {return "mount_f.jpg";}
    }

    @Override
    public boolean mobile(Character c) {
        return c != bottom;
    }

    @Override
    public boolean kiss(Character c, Character target) {
        return true;
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
    public float priorityMod(Character self) {
        return getSubDomBonus(self, 4.0f);
    }

    @Override
    public double pheromoneMod(Character self) {
        return 3;
    }
    
    @Override
    public Position.Dominance dominance() {
        return Position.Dominance.SLIGHT;
    }

    @Override
    public int distance() {
        return 1;
    }

    @Override
    public void struggle(Combat c, Character struggler) {
        c.write(struggler, Global.format("{self:SUBJECT-ACTION:try} to struggle out of {other:name-possessive} hold, but with"
                        + " {other:direct-object} sitting firmly on {self:possessive} chest, there is nothing {self:pronoun} can do.",
                        struggler, top));
        super.struggle(c, struggler);
    }

    @Override
    public void escape(Combat c, Character escapee) {
        c.write(escapee, Global.format("{self:SUBJECT-ACTION:try} to escape {other:name-possessive} hold, but with"
                        + " {other:direct-object} sitting firmly on {self:possessive} chest, there is nothing {self:pronoun} can do.",
                        escapee, top));
        super.escape(c, escapee);
    }
}
