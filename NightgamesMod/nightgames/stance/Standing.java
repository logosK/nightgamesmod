package nightgames.stance;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.skills.damage.DamageType;

public class Standing extends MaledomSexStance {
    public Standing(Character top, Character bottom) {
        super(top, bottom, Stance.standing);
    }

    @Override
    public String describe(Combat c) {
        if (top.human()) {
            return "You are holding " + bottom.getName() + " in the air while buried deep in her pussy.";
        } else {
            return String.format("%s is holding %s in %s arms while pumping into %s girl parts.",
                            top.subject(), bottom.nameDirectObject(), top.possessiveAdjective(),
                            bottom.possessiveAdjective());
        }
    }

    @Override
    public boolean mobile(Character c) {
        return c != top && c != bottom;
    }

    @Override
    public boolean inserted(Character c) {
        return top == c;
    }

    @Override
    public boolean kiss(Character c, Character target) {
        return c == top || c == bottom;
    }

    @Override
    public String image() {
        return "standing.jpg";
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
        return c != top && c != bottom;
    }

    @Override
    public boolean reachBottom(Character c) {
        return c != top && c != bottom;
    }

    @Override
    public boolean prone(Character c) {
        return false;
    }

    @Override
    public boolean behind(Character c) {
        return false;
    }

    @Override
    public Position insertRandom(Combat c) {
        return new Neutral(top, bottom);
    }

    @Override
    public void decay(Combat c) {
        time++;
        top.weaken(c, (int) bottom.modifyDamage(DamageType.stance, top, 2));
    }

    @Override
    public void checkOngoing(Combat c) {
        if (top.getStamina().get() < 10) {
            if (top.human()) {
                c.write("Your legs give out and you fall on the floor. " + bottom.getName()
                                + " lands heavily on your lap.");
                c.setStance(new Cowgirl(bottom, top));
            } else {
                c.write(Global.format("{self:SUBJECT-ACTION:lose} {self:possessive} balance and {self:action:fall}, "
                                + "pulling {other:name-do} down on top of {self:direct-object}.", top, bottom));
                c.setStance(new Cowgirl(bottom, top));
            }
        } else {
            super.checkOngoing(c);
        }
    }

    @Override
    public Position reverse(Combat c, boolean writeMessage) {
        if (writeMessage) {
            c.write(bottom, Global.format(
                            "{self:SUBJECT-ACTION:wrap|wraps} {self:possessive} legs around {other:name-possessive} waist and suddenly {self:action:pull|pulls} {other:direct-object} into a deep kiss. {other:SUBJECT-ACTION:are|is} so surprised by this sneak attack that {other:subject-action:don't|doesn't} "
                                            + "even notice {other:reflective} falling forward until {other:subject-action:feel|feels} {self:possessive} limbs wrapped around {other:possessive} body. {self:PRONOUN} {self:action:move|moves} {self:possessive} hips experimentally, enjoying the control "
                                            + "{self:pronoun} {self:action:have|has} coiled around {other:direct-object}.",
                            bottom, top));
        }
        return new CoiledSex(bottom, top);
    }
    
    @Override
    public Position.Dominance dominance() {
        return Position.Dominance.AVERAGE;
    }
}
