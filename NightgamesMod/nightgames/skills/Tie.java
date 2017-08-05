package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.items.Item;
import nightgames.status.Bound;
import nightgames.status.Stsflag;

public class Tie extends Skill {

    public Tie(Character self) {
        super("Bind", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return true;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return !target.wary() && getSelf().canAct() && c.getStance().reachTop(getSelf())
                        && (getSelf().has(Item.ZipTie) || getSelf().has(Item.Handcuffs))
                        && c.getStance().dom(getSelf())
                        && !target.is(Stsflag.bound)
                        && !target.is(Stsflag.maglocked);
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (getSelf().has(Item.Handcuffs, 1)) {
            getSelf().consume(Item.Handcuffs, 1);
            writeOutput(c, Result.special, target);
            target.add(c, new Bound(target, (40 + 3 * Math.sqrt(getSelf().get(Attribute.Cunning))), "handcuffs"));
        } else {
            getSelf().consume(Item.ZipTie, 1);
            if (target.roll(getSelf(), c, accuracy(c, target))) {
                writeOutput(c, Result.normal, target);
                target.add(c, new Bound(target, (25 + 3 * Math.sqrt(getSelf().get(Attribute.Cunning))), "ziptie"));
            } else {
                writeOutput(c, Result.miss, target);
                return false;
            }
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new Tie(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.positioning;
    }

    @Override
    public int speed() {
        return 2;
    }

    @Override
    public int accuracy(Combat c, Character target) {
        return 80;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return "You try to catch " + target.getName() + "'s hands, but she squirms too much to keep your grip on her.";
        } else if (modifier == Result.special) {
            return "You catch " + target.getName() + "'s wrists and slap a pair of cuffs on her.";
        } else {
            return "You catch both of " + target.getName() + " hands and wrap a ziptie around her wrists.";
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return String.format("%s tries to tie %s down, but %s %s %s arms free.",
                            getSelf().subject(), target.nameDirectObject(),
                            target.pronoun(), target.action("keep"), target.possessiveAdjective());
        } else if (modifier == Result.special) {
            return String.format("%s restrains %s with a pair of handcuffs.",
                            getSelf().subject(), target.nameDirectObject());
        } else {
            return String.format("%s secures %s hands with a ziptie.",
                            getSelf().subject(), target.nameOrPossessivePronoun());
        }
    }

    @Override
    public String describe(Combat c) {
        return "Tie up your opponent's hands with a ziptie";
    }

    @Override
    public boolean makesContact(Combat c) {
        return true;
    }
}
