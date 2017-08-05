package nightgames.skills;

import java.util.Collection;
import java.util.List;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.TailPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.skills.damage.DamageType;
import nightgames.stance.Stance;
import nightgames.status.BodyFetish;
import nightgames.status.Shamed;
import nightgames.status.Stsflag;
import nightgames.status.TailFucked;

public class TailPeg extends Skill {

    public TailPeg(Character self) {
        super("Tail Peg", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        Collection<BodyPart> tails = user.body.get("tail");
        boolean hasFuckableTail = tails.stream().anyMatch(p -> p.isType("tail") && p != TailPart.cat && p != TailPart.slimeycat);
        return hasFuckableTail && (user.get(Attribute.Dark) >= 1 || user.get(Attribute.Seduction) >= 20);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().getArousal().get() >= 30 && getSelf().canAct() && target.crotchAvailable()
                        && c.getStance().en != Stance.standing && c.getStance().en != Stance.standingover
                        && (!target.is(Stsflag.debuff, "Tail Pegged") || !target.is(Stsflag.debuff, "Tail Fucked"));
    }

    @Override
    public int getMojoCost(Combat c) {
        return 20;
    }

    @Override
    public String describe(Combat c) {
        if (c.getStance().anallyPenetrated(c, c.getOpponent(getSelf()))) {
            return "Fuck your opponent with your tail";
        }
        return "Shove your tail up your opponent's ass.";
    }

    @Override
    public String getLabel(Combat c) {
        if (c.getStance().anallyPenetrated(c, c.getOpponent(getSelf()))) {
            return "Tail Fuck";
        } else {
            return "Tail Peg";
        }
    }

    public int accuracy(Combat c, Character target) {
        boolean intercourse = !c.getStance().getPartsFor(c, getSelf(), target).isEmpty() && c.getStance().penisInserted(target);
        return intercourse ? 100 : 60;
    }
    @Override
    public boolean resolve(Combat c, Character target) {
        if (target.roll(getSelf(), c, accuracy(c, target))) {
            int strength = Math.min(20, 10 + getSelf().get(Attribute.Dark) / 4);
            boolean intercourse = !c.getStance().getPartsFor(c, getSelf(), target).isEmpty() && c.getStance().penisInserted(target);
            boolean shamed = false;
            if (!intercourse && Global.random(4) == 2) {
                target.add(c, new Shamed(target));
                shamed = true;
            }
            if (target.human()) {
                if (intercourse) {
                    c.write(getSelf(), receive(c, 0, Result.intercourse, target));
                } else if (c.getStance().inserted(target)) {
                    c.write(getSelf(), receive(c, 0, Result.special, target));
                } else if (c.getStance().dom(target)) {
                    c.write(getSelf(), receive(c, 0, Result.critical, target));
                } else if (c.getStance().behind(getSelf())) {
                    c.write(getSelf(), receive(c, 0, Result.strong, target));
                } else {
                    c.write(getSelf(), receive(c, 0, Result.normal, target));
                }
                if (shamed) {
                    c.write(getSelf(), "The shame of having your ass violated by " + getSelf().getName()
                                    + " has destroyed your confidence.");
                }
            } else if (getSelf().human()) {
                if (intercourse) {
                    c.write(getSelf(), deal(c, 0, Result.intercourse, target));
                }
                if (c.getStance().inserted(target)) {
                    c.write(getSelf(), deal(c, 0, Result.special, target));
                } else if (c.getStance().dom(target)) {
                    c.write(getSelf(), deal(c, 0, Result.critical, target));
                } else if (c.getStance().behind(getSelf())) {
                    c.write(getSelf(), deal(c, 0, Result.strong, target));
                } else {
                    c.write(getSelf(), deal(c, 0, Result.normal, target));
                }
                if (shamed) {
                    c.write(getSelf(), "The shame of having her ass violated by you has destroyed " + target.getName()
                                    + "'s confidence.");
                }
            }
            if (intercourse) {
                if (!c.getStance().vaginallyPenetrated(c, target)) {
                    target.body.pleasure(getSelf(), getSelf().body.getRandom("tail"), target.body.getRandom("pussy"),
                                    strength, c, this);
                    target.add(c, new TailFucked(target, getSelf(), "pussy"));
                } else if (!c.getStance().anallyPenetrated(c, target)) {
                    target.body.pleasure(getSelf(), getSelf().body.getRandom("tail"), target.body.getRandom("ass"),
                                    strength, c, this);
                    target.add(c, new TailFucked(target, getSelf(), "ass"));
                }
            }
            target.pain(c, getSelf(), (int) getSelf().modifyDamage(DamageType.physical, target, strength / 2));
            target.emote(Emotion.nervous, 10);
            target.emote(Emotion.desperate, 10);
            getSelf().emote(Emotion.confident, 15);
            getSelf().emote(Emotion.dominant, 25);
            if (Global.random(100) < 5 + 2 * getSelf().get(Attribute.Fetish)) {
                target.add(c, new BodyFetish(target, getSelf(), "tail", .25));
            }
        } else {
            if (target.human()) {
                c.write(getSelf(), receive(c, 0, Result.miss, target));
            } else {
                c.write(getSelf(), deal(c, 0, Result.miss, target));
            }
            return false;
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new TailPeg(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.pleasure;
    }

    @Override
    public String deal(Combat c, int magnitude, Result modifier, Character target) {
        switch (modifier) {
            case critical:
                return "You flex your prehensile tail and spread " + target.nameOrPossessivePronoun() + " legs apart. "
                                + "You quickly lube it up with " + target.possessiveAdjective()
                                + " juices and slide it into her ass and start pumping.";
            case miss:
                return "You try to peg " + target.getName() + " with your tail, but " + target.pronoun()
                                + " manages to clench " + target.possessiveAdjective()
                                + " butt cheeks together in time to keep you out.";
            case normal:
                return "You move towards " + target.getName() + " and hold " + target.possessiveAdjective() + " hands above "
                                + target.possessiveAdjective()
                                + " head. In the same motion, you swiftly plunge your thick tail into "
                                + target.possessiveAdjective() + " ass, pumping it in and out of "
                                + target.possessiveAdjective() + " tight hole.";
            case special:
                return "You smile down at " + target.getName() + " and move your flexible tail behind "
                                + target.directObject() + ". You spread " + target.possessiveAdjective()
                                + " cheeks with your tail and plunge it into " + target.possessiveAdjective()
                                + " tight pucker. " + target.getName() + " moans loudly at the sudden intrusion.";
            case intercourse:
                return "You smile down at " + target.getName() + " and move your flexible tail behind "
                                + target.directObject() + ". You spread " + target.possessiveAdjective()
                                + " legs with your tail and plunge it into " + target.possessiveAdjective()
                                + " wet slit. " + target.getName() + " moans loudly at the sudden intrusion.";
            case strong:
                if (target.body.getLargestBreasts().getSize() >= 2) {
                    return "You hug " + target.getName()
                                    + " from behind and cup her breasts with your hands. Taking advantage of her surprise, you shove your tail into her ass, and tickle her prostate with the tip.";
                } else {
                    return "You hug " + target.getName() + " from behind and twist " + target.possessiveAdjective()
                                    + " nipples. Taking advantage of " + target.possessiveAdjective()
                                    + " surprise, you shove your tail into " + target.possessiveAdjective()
                                    + " ass, and tickle " + target.possessiveAdjective() + " prostate with the tip.";
                }
            default:
                return "<<This should not be displayed, please inform The Silver Bard: TailPeg-deal>>";
        }
    }

    @Override
    public String receive(Combat c, int magnitude, Result modifier, Character target) {
        switch (modifier) {
            case critical:
                return String.format("Smiling down on %s, %s spreads %s legs and tickles %s butt with %s tail."
                                + " %s how the tail itself is slick and wet as it"
                                + " slowly pushes through %s anus, spreading %s cheeks apart. %s"
                                + " pumps it in and out a for a few times before taking it out again.",
                                target.nameDirectObject(), getSelf().subject(), target.possessiveAdjective(),
                                target.possessiveAdjective(), getSelf().possessiveAdjective(),
                                Global.capitalizeFirstLetter(target.subjectAction("notice")),
                                target.possessiveAdjective(), target.possessiveAdjective(),
                                getSelf().subject());
            case miss:
                return String.format("%s tries to peg %s with her tail but %s %s to clench"
                                + " %s butt cheeks together in time to keep it out.",
                                getSelf().subject(), target.nameDirectObject(),
                                target.pronoun(), target.action("manage"),
                                target.possessiveAdjective());
            case normal:
                return String.format("%s suddenly moves very close to %s. %s an attack from the front"
                                + " and %s to move back, but %s up shoving %s tail right up %s ass.",
                                getSelf().subject(), target.nameDirectObject(),
                                Global.capitalizeFirstLetter(target.subjectAction("expect")),
                                target.action("try", "tries"), target.action("end"),
                                getSelf().possessiveAdjective(), target.possessiveAdjective());
            case special:
                return String.format("%s smirks and wiggles %s tail behind %s back. %s briefly %s "
                                + "at it and %s the appendage move behind %s. %s to keep it"
                                + " out by clenching %s butt together, but a squeeze of %s"
                                + " vagina breaks %s concentration, so the tail slides up %s ass"
                                + " and %s almost %s it as %s cock and ass are stimulated so thoroughly"
                                + " at the same time.", getSelf().subject(), getSelf().possessiveAdjective(),
                                target.nameOrPossessivePronoun(),
                                Global.capitalizeFirstLetter(target.pronoun()), target.action("look"),
                                target.action("see"), target.directObject(), 
                                Global.capitalizeFirstLetter(target.subjectAction("try", "tries")),
                                target.possessiveAdjective(), getSelf().nameOrPossessivePronoun(),
                                target.possessiveAdjective(), target.possessiveAdjective(),
                                target.pronoun(), target.action("lose"), target.possessiveAdjective());
            case intercourse:
                List<BodyPart> parts = c.getStance().getPartsFor(c, getSelf(), target);
                String part = "hands";
                if (!parts.isEmpty()) {
                    part = Global.pickRandom(parts).get().describe(getSelf());
                }
                return String.format("%s smirks and coils %s tail around in front of %s. %s briefly %s "
                                + "at it and %s the appendage move under %s and %s. %s to keep it"
                                + " out by clamping %s legs together, but a squeeze of %s"
                                + " %s breaks %s concentration, so the tail slides smoothly into %s pussy.",
                                getSelf().subject(), getSelf().possessiveAdjective(), 
                                target.nameDirectObject(), Global.capitalizeFirstLetter(target.pronoun()),
                                target.action("look"), target.action("see"), target.directObject(),
                                target.action("panic"),
                                Global.capitalizeFirstLetter(target.subjectAction("try", "tries")),
                                target.possessiveAdjective(), getSelf().nameOrPossessivePronoun(),
                                part,
                                target.possessiveAdjective(), target.possessiveAdjective());
            case strong:
                return String.format("%s hugs %s from behind and rubs %s chest against %s back."
                                + " Distracted by that, %s managed to push %s tail between %s"
                                + " ass cheeks and started tickling %s %s with the tip.",
                                getSelf().subject(), target.nameDirectObject(),
                                getSelf().possessiveAdjective(), target.possessiveAdjective(),
                                getSelf().pronoun(), getSelf().possessiveAdjective(),
                                target.possessiveAdjective(), target.possessiveAdjective(),
                                target.hasBalls() ? "prostate" : "sensitive insides");
            default:
                return "<<This should not be displayed, please inform The Silver Bard: TailPeg-receive>>";
        }
    }

    @Override
    public boolean makesContact(Combat c) {
        return true;
    }
}
