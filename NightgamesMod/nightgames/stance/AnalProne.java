package nightgames.stance;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class AnalProne extends AnalSexStance {

    public AnalProne(Character top, Character bottom) {
        super(top, bottom, Stance.anal);
    }

    @Override
    public String describe(Combat c) {
        if (top.human()) {
            return String.format("You're holding %s legs over your shoulder while your cock is buried in %s's ass.",
                            bottom.nameOrPossessivePronoun(), bottom.possessiveAdjective());
        } else if (top.has(Trait.strapped)) {
            return String.format("%s flat on %s back with %s feet over %s head while %s pegs %s with %s strapon dildo.",
                            bottom.subjectAction("are", "is"), bottom.possessiveAdjective(),
                            bottom.possessiveAdjective(), bottom.possessiveAdjective(),
                            top.subject(), bottom.directObject(), top.possessiveAdjective());
        } else {
            return String.format("%s flat on %s back with %s feet over %s head while %s pegs %s with %s %s.",
                            bottom.subjectAction("are", "is"), bottom.possessiveAdjective(),
                            bottom.possessiveAdjective(), bottom.possessiveAdjective(),
                            top.subject(), bottom.directObject(),
                            top.possessiveAdjective(), top.body.getRandomInsertable().describe(top));
        }
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
        return false;
    }

    @Override
    public boolean oral(Character c, Character target) {
        return false;
    }

    @Override
    public boolean behind(Character c) {
        return false;
    }

    @Override
    public boolean inserted(Character c) {
        return c == top;
    }

    @Override
    public Position insertRandom(Combat c) {
        return new Mount(top, bottom);
    }

    @Override
    public void checkOngoing(Combat c) {
        Character inserter = inserted(top) ? top : bottom;
        Character inserted = inserted(top) ? bottom : top;

        if (!inserter.hasInsertable()) {
            if (inserted.human()) {
                c.write("With " + inserter.getName() + "'s pole gone, your ass gets a respite.");
            } else {
                c.write(inserted.getName() + " sighs with relief with "+inserter.nameOrPossessivePronoun()
                            +" dick gone.");
            }
            c.setStance(insertRandom(c));
        }
        if (inserted.body.getRandom("ass") == null) {
            if (inserted.human()) {
                c.write("With your asshole suddenly disappearing, " + inserter.getName()
                                + "'s dick pops out of what was once your sphincter.");
            } else {
                c.write("Your dick pops out of " + inserted.getName() + " as her asshole shrinks and disappears.");
            }
            c.setStance(insertRandom(c));
        }
    }

    @Override
    public Position reverse(Combat c, boolean writeMessage) {
        if (top.has(Trait.strapped)) {
            if (writeMessage) {
                c.write(bottom, Global.format(
                                "As {other:subject-action:are|is} thrusting into {self:name-do} with {other:possessive} strapon, {self:subject-action:suddenly pull|suddenly pulls} {self:possessive} face up towards {other:direct-object}, and kisses {other:direct-object} deeply. "
                                                + "Taking advantage of {other:possessive} surprise, {self:SUBJECT-ACTION:quickly pushes|quickly pushes} {other:direct-object} down and {self:action:pull|pulls} {other:possessive} fake cock out of {self:reflective}.",
                                bottom, top));
            }
            return new Mount(bottom, top);
        } else {
            if (writeMessage) {
                c.write(bottom, Global.format(
                                "As {other:subject-action:are|is} thrusting into {self:name-do}, {self:subject-action:suddenly pull|suddenly pulls} {self:possessive} face up towards {other:direct-object}, and {self:action:kiss|kisses} {other:direct-object} deeply. "
                                                + "Taking advantage of {other:possessive} surprise, {self:SUBJECT-ACTION:quickly push|quickly pushes} {other:direct-object} down and {self:action:start|starts} fucking {other:direct-object} back on top of {other:direct-object}.",
                                                bottom, top));
            }
            return new AnalCowgirl(bottom, top);
        }
    }

    @Override
    public String image() {
        if (!top.hasPussy()) {
            return "analf.jpg";
        } else {
            return "pegging.jpg";
        }
    }
}
