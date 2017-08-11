package nightgames.skills;

import nightgames.characters.Character;
import nightgames.characters.body.Body;
import nightgames.characters.body.CockMod;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.Knotted;
import nightgames.status.Stsflag;

public class ToggleKnot extends Skill {

    public ToggleKnot(Character self) {
        super("Toggle Knot", self);
    }

    private boolean isActive(Character target) {
        return target.hasStatus(Stsflag.knotted);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.body.get("cock").stream().anyMatch(cock -> cock.moddedPartCountsAs(user, CockMod.primal));
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return (getSelf().canRespond() && isActive(target)) || (getSelf().canAct() && c.getStance().inserted(getSelf()));
    }

    @Override
    public String describe(Combat c) {
        return "Inflate or deflate your knot.";
    }
    
    @Override
    public float priorityMod(Combat c) {
        if(c.getOpponent(getSelf()).hasStatus(Stsflag.knotted)) return -5.0f;
        return 0.0f;
    }

    @Override
    public String getLabel(Combat c) {
        if (isActive(c.getOpponent(getSelf()))) {
            return "Deflate Knot";
        }
        return "Inflate Knot";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (isActive(target)) {
            if (getSelf().human()) {
                c.write(getSelf(),
                                "Deciding she's had enough for now, you let your cock return to its regular shape, once again permitting movement.");
            } else if (c.shouldPrintReceive(target, c)) {
                String part = Global.pickRandom(c.getStance().getPartsFor(c, target, getSelf())).orElse(Body.nonePart).describe(target);
                c.write(getSelf(), String.format("%s the intense pressure in %s %s "
                                + "recede as %s allows %s knot to deflate.", target.subjectAction("feel"),
                                target.possessiveAdjective(), part, getSelf().subject(),
                                getSelf().possessiveAdjective()));
            }
            target.removeStatus(Stsflag.knotted);
        } else {
            if (getSelf().human()) {
                c.write(getSelf(),
                                "You'd like to stay inside " + target.getName() + " for a bit, so you "
                                                + (c.getStance().canthrust(c, getSelf()) ? "thrust" : "buck up")
                                                + " as deep inside of her as you can and send a mental command to the base of your cock, where your"
                                                + " knot soon swells up, locking you inside,");
            } else if (c.shouldPrintReceive(target, c)) {
                String firstPart;
                if (c.getStance().dom(getSelf())) {
                    firstPart = String.format("%s bottoms out inside of %s, and something quickly feels off%s.",
                                    getSelf().subject(), target.nameDirectObject(),
                                    c.isBeingObserved() ? " to " + target.directObject() : "");
                } else {
                    firstPart = String.format("%s pulls %s all the way onto %s cock. "
                                    + "As soon as %s pelvis touches %s, something starts happening.",
                                    getSelf().subject(), target.nameDirectObject(),
                                    getSelf().possessiveAdjective(), getSelf().possessiveAdjective(),
                                    (target.human() || target.useFemalePronouns()) 
                                    ? target.possessiveAdjective() + "s" : "s");
                }
                c.write(getSelf() ,String.format("%s A ball swells up at the base of %s dick,"
                                + " growing to the size of a small apple. %s not"
                                                + " getting <i>that</i> out of %s any time soon...",
                                                firstPart, getSelf().nameOrPossessivePronoun(),
                                                Global.capitalizeFirstLetter(target.subjectAction("are", "is")),
                                                target.reflexivePronoun()));
            }
            target.add(c, new Knotted(target, getSelf(), c.getStance().anallyPenetrated(c, target)));
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new ToggleKnot(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.positioning;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return null;
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return null;
    }

}
