package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BreastsPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.Hypersensitive;

public class BreastGrowth extends Skill {
    public BreastGrowth(Character self) {
        super("Breast Growth", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Arcane) >= 12;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && c.getStance().mobile(getSelf())&& !c.getStance().prone(getSelf()) && !(new BreastGrowthSuper(getSelf()).usable(c, target));
    }

    @Override
    public float priorityMod(Combat c) {
        return 0;
    }

    @Override
    public int getMojoCost(Combat c) {
        return 25;
    }

    @Override
    public String describe(Combat c) {
        return "Grow your opponent's boobs to make her more sensitive.";
    }

    @Override
    public int accuracy(Combat c, Character target) {
        return 90;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        Result res;
        if (target.roll(getSelf(), c, accuracy(c, target))) {
            if (!target.hasBreasts()) {
                res = Result.special;
            } else {
                res = Result.normal;
            }
        } else {
            res = Result.miss;
        }
        boolean permanent = Global.random(20) == 0 && (getSelf().human() || c.shouldPrintReceive(target, c))
                        && !target.has(Trait.stableform);
        writeOutput(c, permanent ? 1 : 0, res, target);
        if (res != Result.miss) {
            target.add(c, new Hypersensitive(target, 10));
            BreastsPart part = target.body.getBreastsBelow(BreastsPart.f.getSize());
            if (permanent) {
                if (part != null) {
                    target.body.addReplace(part.upgrade(), 1);
                    target.body.temporaryAddOrReplacePartWithType(part.upgrade().upgrade().upgrade(), 10);
                }
            } else {
                if (part != null) {
                    target.body.temporaryAddOrReplacePartWithType(part.upgrade().upgrade().upgrade(), 10);
                }
            }
        }
        return res != Result.miss;
    }

    @Override
    public Skill copy(Character user) {
        return new BreastGrowth(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.debuff;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        String message;
        if (modifier == Result.normal) {
            message = String.format(
                            "You channel your arcane energies into %s breasts, "
                                            + "causing them to grow rapidly. %s knees buckle with the new"
                                            + " sensitivity you bestowed on %s boobs.",
                            target.nameOrPossessivePronoun(),
                            Global.capitalizeFirstLetter(target.possessiveAdjective()), target.possessiveAdjective());
            if (damage > 0) {
                message += " You realize the effects are permanent!";
            }
        } else if (modifier == Result.special) {
            message = String.format(
                            "You channel your arcane energies into %s flat chest, "
                                            + "causing small mounds to rapidly grow on %s. %s knees buckle with the"
                                            + " sensitivity you bestowed on %s new boobs.",
                            target.nameOrPossessivePronoun(), target.directObject(),
                            Global.capitalizeFirstLetter(target.possessiveAdjective()), target.possessiveAdjective());
            if (damage > 0) {
                message += " You realize the effects are permanent!";
            }
        } else {
            message = String.format(
                            "You attempt to channel your arcane energies into %s breasts, but "
                                            + "%s %s out of the way, causing your spell to fail.",
                            target.nameOrPossessivePronoun(), target.pronoun(), target.action("dodge"));
        }
        return message;
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        String message;
        if (modifier == Result.normal) {
            message = String.format(
                            "%s moving and begins chanting. %s %s breasts grow hot, and they start expanding!"
                                            + " %s to hold them back with %s hands, but the growth continues untill they are a full cup size"
                                            + " bigger than before. The new sensations from %s substantially larger breasts make %s tremble.",
                            getSelf().subjectAction("start"), Global.capitalizeFirstLetter(target.subjectAction("feel")),
                            target.possessiveAdjective(), Global.capitalizeFirstLetter(target.subjectAction("try", "tries")),
                            target.possessiveAdjective(), target.possessiveAdjective(), target.directObject());
            if (damage > 0) {
                message += Global.capitalizeFirstLetter(target.subjectAction("realize"))
                                + " the effects are permanent!";
            }
        } else if (modifier == Result.special) {
            message = String.format(
                            "%s moving and begins chanting. %s %s chest grow hot, and small, perky breasts start to form!"
                                            + " %s to hold them back with %s hands, but the growth continues untill they are a full A-cup."
                                            + " The new sensations from %s new breasts make %s tremble.",
                            getSelf().getName(), Global.capitalizeFirstLetter(target.subjectAction("feel")),
                            target.possessiveAdjective(), Global.capitalizeFirstLetter(target.pronoun()),
                            target.action("try", "tries"), target.possessiveAdjective(), target.directObject());
        } else {
            message = String.format(
                            "%s moving and begins chanting. %s feeling some tingling in %s breasts, "
                                            + "but it quickly subsides as %s %s out of the way.",
                            getSelf().subjectAction("stop"),
                            Global.capitalizeFirstLetter(target.subjectAction("start")), target.possessiveAdjective(),
                            target.pronoun(), target.action("dodge"));
        }
        return message;
    }

}
