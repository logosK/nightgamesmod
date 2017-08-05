package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.Bound;
import nightgames.status.Oiled;
import nightgames.status.Slimed;
import nightgames.status.Stsflag;

public class TentacleRape extends Skill {

    public TentacleRape(Character self) {
        super("Tentacle Rape", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return true;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return !target.wary() && !c.getStance().sub(getSelf()) && !c.getStance().prone(getSelf())
                        && !c.getStance().prone(target) && getSelf().canAct() && getSelf().body.has("tentacles");
    }

    @Override
    public int getMojoCost(Combat c) {
        return 10;
    }

    @Override
    public String describe(Combat c) {
        return "Violate your opponent with your tentacles.";
    }

    BodyPart tentacles = null;

    @Override
    public int accuracy(Combat c, Character target) {
        return 90;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        tentacles = getSelf().body.getRandom("tentacles");
        if (target.roll(getSelf(), c, accuracy(c, target))) {
            if (target.mostlyNude()) {
                int m = 2 + Global.random(4);
                if (target.bound()) {
                    writeOutput(c, Result.special, target);
                    if (target.hasDick()) {
                        target.body.pleasure(getSelf(), tentacles, target.body.getRandom("cock"), m, c, this);
                        m = 2 + Global.random(4);
                    }
                    if (target.hasPussy()) {
                        target.body.pleasure(getSelf(), tentacles, target.body.getRandom("pussy"), m, c, this);
                        m = 2 + Global.random(4);
                    }
                    if (target.hasBreasts()) {
                        target.body.pleasure(getSelf(), tentacles, target.body.getRandom("breasts"), m, c, this);
                        m = 2 + Global.random(4);
                    }
                    if (target.body.has("ass")) {
                        target.body.pleasure(getSelf(), tentacles, target.body.getRandom("ass"), m, c, this);
                        target.emote(Emotion.horny, 10);
                    }
                } else {
                    writeOutput(c, Result.normal, target);
                    target.body.pleasure(getSelf(), tentacles, target.body.getRandom("skin"), m, c, this);
                }
                if (!target.is(Stsflag.oiled)) {
                    target.add(c, new Oiled(target));
                }
                target.emote(Emotion.horny, 20);
            } else {
                writeOutput(c, Result.weak, target);
            }
            target.add(c, new Bound(target, 30 + 2 * Math.sqrt(getSelf().get(Attribute.Fetish) + getSelf().get(Attribute.Slime)), "tentacles"));
        } else {
            writeOutput(c, Result.miss, target);
            return false;
        }
        if (getSelf().has(Trait.VolatileSubstrate) && getSelf().has(Trait.slime)) {
            target.add(c, new Slimed(target, getSelf(), Global.random(2, 5)));
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new TentacleRape(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.pleasure;
    }

    @Override
    public Stage getStage() {
        return Stage.FINISHER;
    }
    
    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return "You use your " + tentacles.describe(getSelf()) + " to snare " + target.getName()
                            + ", but she nimbly dodges them.";
        } else if (modifier == Result.weak) {
            return "You use your " + tentacles.describe(getSelf()) + " to wrap around " + target.getName()
                            + "'s arms, holding her in place.";
        } else if (modifier == Result.normal) {
            return "You use your " + tentacles.describe(getSelf()) + " to wrap around " + target.getName()
                            + "'s naked body. They squirm against her and squirt slimy fluids on her body.";
        } else {
            return "You use your " + tentacles.describe(getSelf()) + " to toy with " + target.getName()
                            + "'s helpless form. The tentacles toy with her breasts and penetrate her genitals and ass.";
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return String.format("%s shoots %s %s forward at %s. %s barely able to avoid them.",
                            getSelf().subject(), getSelf().possessiveAdjective(),
                            tentacles.describe(getSelf()), target.nameDirectObject(), 
                            Global.capitalizeFirstLetter(target.subjectAction("are", "is")));
        } else if (modifier == Result.weak) {
            return String.format("%s shoots %s %s forward at %s, entangling %s arms and legs.",
                            getSelf().subject(), getSelf().possessiveAdjective(), tentacles.describe(getSelf()),
                            target.nameDirectObject(), target.possessiveAdjective());
        } else if (modifier == Result.normal) {
            return String.format("%s shoots %s %s forward at %s, "
                            + "entangling %s arms and legs. The slimy appendages "
                            + "wriggle over %s body and coat %s in the slippery liquid.",
                            getSelf().subject(), getSelf().possessiveAdjective(), tentacles.describe(getSelf()),
                            target.nameDirectObject(), target.possessiveAdjective(),
                            target.nameOrPossessivePronoun(), target.directObject());
        } else {
            return String.format("%s %s cover %s helpless body, tease %s genitals, and probe %s ass.",
                            getSelf().nameOrPossessivePronoun(), tentacles.describe(getSelf()),
                            target.nameOrPossessivePronoun(), target.possessiveAdjective(),
                            target.possessiveAdjective());
        }
    }

}
