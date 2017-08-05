package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.skills.damage.DamageType;
import nightgames.status.Drained;

public class Drain extends Skill {
    public Drain(Character self) {
        super("Drain", self, 5);
        addTag(SkillTag.drain);
        addTag(SkillTag.dark);
        addTag(SkillTag.fucking);
    }

    public Drain(String name, Character self) {
        super(name, self, 5);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Dark) >= 15 || user.has(Trait.energydrain) || (user.has(Trait.leveldrainer) && user.getLevel() >= 10);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && c.getStance().havingSexNoStrapped(c);
    }

    @Override
    public int getMojoCost(Combat c) {
        return drainsAttributes(c) ? 30 : 0;
    }

    private boolean drainsAttributes(Combat c) {
        return getSelf().getMojo().get() >= 30;
    }

    @Override
    public float priorityMod(Combat c) {
        return 2.0f;
    }

    @Override
    public String describe(Combat c) {
        return "Drain your opponent of their energy";
    }

    private void steal(Combat c, Character target, Attribute att, int amount) {
        Drained.drain(c, getSelf(), target, att, amount, 20, true);
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        return resolve(c, target, false);
    }

    public boolean resolve(Combat c, Character target, boolean nocost) {
        int strength = Math.max(10, 1 + getSelf().get(Attribute.Dark) / 4);
        int staminaStrength = 50;
        int type = Math.max(1, Global.centeredrandom(6, getSelf().get(Attribute.Dark) / 3.0, 3));
        if (!drainsAttributes(c) && type > 2) {
            type = 1;
            staminaStrength /= 2;
        }

        writeOutput(c, type, Result.normal, target);
        switch (type) {
            case 0:
                getSelf().arouse(getSelf().getArousal().max() / 4, c);
            case 1:
                target.drain(c, getSelf(), (int) getSelf().modifyDamage(DamageType.drain, target, staminaStrength));
                break;
            case 2:
                target.loseMojo(c, staminaStrength / 2);
                getSelf().buildMojo(c, staminaStrength / 2);
                break;
            case 3:
                steal(c, target, Attribute.Cunning, strength);
                target.drainMojo(c, getSelf(), target.getMojo().get() / 2);
                break;
            case 4:
                steal(c, target, Attribute.Power, strength);
                target.drain(c, getSelf(), (int) getSelf().modifyDamage(DamageType.drain, target, staminaStrength));
                break;
            case 5:
                steal(c, target, Attribute.Seduction, strength);
                target.temptNoSource(c, getSelf(), 10, this);
                break;
            case 6:
                steal(c, target, Attribute.Power, strength);
                steal(c, target, Attribute.Seduction, strength);
                steal(c, target, Attribute.Cunning, strength);
                target.drain(c, getSelf(), (int) getSelf().modifyDamage(DamageType.drain, target, staminaStrength));
                target.drainMojo(c, getSelf(), target.getMojo().get());
                target.temptNoSource(c, getSelf(), 20, this);
                break;
            default:
                break;
        }
        return type != 0;
    }

    @Override
    public Skill copy(Character target) {
        return new Drain(target);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.pleasure;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (c.getStance().inserted(target)) {
            String muscDesc = c.getStance().anallyPenetrated(c, getSelf()) ? "anal" : "vaginal";
            String partDesc = c.getStance().anallyPenetrated(c, getSelf())
                            ? getSelf().body.getRandom("ass").describe(getSelf())
                            : getSelf().body.getRandomPussy().describe(getSelf());
            String base = "You put your powerful " + muscDesc + " muscles to work whilst" + " transfixing "
                            + target.getName() + "'s gaze with your own, goading " + target.possessiveAdjective()
                            + " energy into " + target.possessiveAdjective() + " cock."
                            + " Soon it erupts from "+ target.directObject() + " into your " + partDesc + ", ";
            switch (damage) {
                case 4:
                    return base + "and you can feel " + target.possessiveAdjective() + " strength pumping into you.";
                case 3:
                    return base + "and you can feel " + target.possessiveAdjective() + " memories and experiences flow"
                                    + " into you, adding to your skill.";
                case 5:
                    return base + "taking " + target.possessiveAdjective() + " raw sexual energy and"
                                    + " adding it to your own";
                case 1:
                    return base + "but unfortunately you made a mistake, and only feel a small" + " bit of "
                                    + target.possessiveAdjective() + " energy traversing the space between you.";
                case 2:
                    return base + "but unfortunately you made a mistake, and only feel a small" + " bit of "
                                    + target.possessiveAdjective() + " energy traversing the space between you.";
                case 6:
                    return base + "far more powerfully than you even thought possible." + " You feel a fragment of "
                                    + target.possessiveAdjective() + " soul break away from "+target.directObject()+" and"
                                    + " gush into you, taking along a portion of " + target.possessiveAdjective()
                                    + " strength," + " skill and wits, merging with your own. You have clearly"
                                    + " won this fight, and a lot more than that.";
                default:
                    // Should never happen
                    return " but nothing happens, you feel strangely impotent.";
            }
        } else {
            String base = "With your cock deep inside " + target.getName()
                            + ", you can feel the heat from her core. You draw the energy from her, mining her depths. ";
            switch (damage) {
                case 4:
                    return base + "You feel yourself grow stronger as you steal her physical power.";
                case 5:
                    return base + "You manage to steal some of her sexual experience and skill at seduction.";
                case 3:
                    return base + "You draw some of her wit and cunning into yourself.";
                case 1:
                    return "You attempt to drain " + target.getName()
                                    + "'s energy through your intimate connection, taking a bit of her energy.";
                case 2:
                    return "You attempt to drain " + target.getName()
                                    + "'s energy through your intimate connection, stealing some of her restraint.";
                case 0:
                    return "You attempt to drain " + target.getName()
                                    + "'s energy through your intimate connection, but it goes wrong. You feel intense pleasure feeding "
                                    + "back into you and threatening to overwhelm you. You brink the spiritual link as fast as you can, but you're still left on the brink of "
                                    + "climax.";
                case 6:
                    return base + "You succeed in siphoning off a portion of her soul, stealing both her physical and mental strength. This energy will eventually "
                                    + "return to its owner, but for now, you're very powerful!";
                default:
                    // Should never happen
                    return " but nothing happens, you feel strangely impotent.";
            }
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        String demon = getSelf().useFemalePronouns() ? "succubus" : "incubus";
        
        if (c.getStance().inserted(target)) {
            String muscDesc = c.getStance().anallyPenetrated(c, getSelf()) ? "anal" : "vaginal";
            String partDesc = c.getStance().anallyPenetrated(c, getSelf())
                            ? getSelf().body.getRandom("ass").describe(getSelf())
                            : getSelf().body.getRandomPussy().describe(getSelf());

            String base = String.format("%s %s  powerful %s muscles suddenly tighten around %s. "
                            + "%s starts kneading %s dick, bringing %s immense pleasure and soon"
                            + " %s %s erupt into %s, but %s %s %s %s shooting"
                            + " something far more precious than semen into her %s; "
                            + "as more of the ethereal fluid leaves %s, %s ", 
                            target.subjectAction("feel"), getSelf().nameOrPossessivePronoun(),
                            muscDesc, target.directObject(), getSelf().subject(),
                            target.possessiveAdjective(), target.directObject(),
                            target.subjectAction("feel"), target.reflectivePronoun(), getSelf().nameDirectObject(),
                            target.pronoun(), target.action("realize"), target.pronoun(), target.action("are", "is"),
                            partDesc, target.directObject(), target.subjectAction("feel"));
            switch (damage) {
                case 4:
                    return base + String.format("%s strength leaving %s with it, "
                                    + "making %s more tired than %s have ever felt.",
                                    target.possessiveAdjective(), target.directObject(),
                                    target.directObject(), target.pronoun());
                case 5:
                    return base + String.format("memories of previous sexual experiences escape %s mind,"
                                    + " numbing %s skills, rendering %s more sensitive and"
                                    + " perilously close to the edge of climax.",
                                    target.possessiveAdjective(), target.possessiveAdjective(),
                                    target.directObject());
                case 3:
                    return base + String.format("%s mind go numb, causing %s confidence and cunning to flow into %s.",
                                    target.possessiveAdjective(), target.possessiveAdjective(), getSelf().nameDirectObject());
                case 1:
                    return String.format("Clearly the %s is trying to do something really special to %s, "
                                    + "as %s can feel the walls of %s %s squirm against %s in a way "
                                    + "no human could manage, but all %s is some drowsiness.",
                                    demon, target.nameDirectObject(), target.pronoun(),
                                    getSelf().nameOrPossessivePronoun(), partDesc, target.directObject(),
                                    target.subjectAction("feel"));
                case 2:
                    return String.format("Clearly the %s is trying to do something really special to %s, "
                                    + "as %s can feel the walls of %s %s squirm against %s in a way "
                                    + "no human could manage, but all %s is %s focus waning a bit.",
                                    demon, target.nameDirectObject(), target.pronoun(),
                                    getSelf().nameOrPossessivePronoun(), partDesc, target.directObject(),
                                    target.subjectAction("feel"), target.possessiveAdjective());
                case 0:
                    return String.format("%s squeezes %s with %s %s and starts to milk %s,"
                                    + " but %s suddenly %s %s shudder and moan loudly."
                                    + " Looks like %s plan backfired.", getSelf().getName(), target.subject(),
                                    getSelf().possessiveAdjective(), partDesc, target.directObject(),
                                    target.pronoun(), target.action("feel"), getSelf().directObject(),
                                    target.nameOrPossessivePronoun());
                case 6:
                    return base + String.format("something snap loose inside of %s and it seems to flow right "
                                    + "through %s dick and into %s. When it is over %s... empty "
                                    + "somehow. At the same time, %s seems radiant, looking more powerful,"
                                    + " smarter and even more seductive than before. Through all of this,"
                                    + " %s has kept on thrusting and %s right on the edge of climax."
                                    + " %s defeat appears imminent, but %s %s already lost something"
                                    + " far more valuable than a simple sex fight...",
                                    target.directObject(), target.possessiveAdjective(), getSelf().nameDirectObject(),
                                    target.subjectAction("feel"), getSelf().subject(), getSelf().pronoun(),
                                    target.subjectAction("are", "is"), Global.capitalizeFirstLetter(target.possessiveAdjective()),
                                    target.pronoun(), target.action("have", "has"));
                default:
                    // Should never happen
                    return " nothing. You should be feeling something, but you're not.";
            }
        } else {
            String base = String.format("%s feel %s powerful will drawing some of %s energy into %s cock, ",
                            target.subjectAction("feel"), getSelf().nameOrPossessivePronoun(), 
                            target.possessiveAdjective(), getSelf().possessiveAdjective());
            switch (damage) {
                case 4:
                    return base + String.format("%s strength leaving %s with it, "
                                    + "making %s more tired than %s have ever felt.",
                                    target.possessiveAdjective(), target.directObject(),
                                    target.directObject(), target.pronoun());
                case 5:
                    return base + String.format("memories of previous sexual experiences escape %s mind,"
                                    + " numbing %s skills, rendering %s more sensitive and"
                                    + " perilously close to the edge of climax.",
                                    target.possessiveAdjective(), target.possessiveAdjective(),
                                    target.directObject());
                case 3:
                    return base + String.format("%s mind go numb, causing %s confidence and cunning to flow into %s.",
                                    target.possessiveAdjective(), target.possessiveAdjective(), getSelf().nameDirectObject());
                case 1:
                    return String.format("Clearly the %s is trying to do something really special to %s, "
                                    + "as %s can feel %s cock thrust against %s in a way "
                                    + "no human could manage, but all %s is some drowsiness.",
                                    demon, target.nameDirectObject(), target.pronoun(),
                                    getSelf().nameOrPossessivePronoun(), target.directObject(),
                                    target.subjectAction("feel"));
                case 2:
                    return String.format("Clearly the %s is trying to do something really special to %s, "
                                    + "as %s can feel %s cock thrust against %s in a way "
                                    + "no human could manage, but all %s is %s focus waning a bit.",
                                    demon, target.nameDirectObject(), target.pronoun(),
                                    getSelf().nameOrPossessivePronoun(), target.directObject(),
                                    target.subjectAction("feel"), target.possessiveAdjective());
                case 0:
                    return String.format("%s draws upon %s will through %s connection, but %s"
                                    + " suddenly %s %s shudder and moan loudly. Looks like %s plan backfired.",
                                    getSelf().nameOrPossessivePronoun(), target.nameOrPossessivePronoun(),
                                    c.bothPossessive(target), target.subject(),
                                                    target.action("feel"), getSelf().subject(),
                                                    getSelf().possessiveAdjective());
                case 6:
                    return base + String.format("something snap loose inside of %s and it seems to flow right "
                                    + "through %s pussy and into %s. When it is over %s... empty "
                                    + "somehow. At the same time, %s seems radiant, looking more powerful,"
                                    + " smarter and even more seductive than before. Through all of this,"
                                    + " %s has kept on thrusting and %s right on the edge of climax."
                                    + " %s defeat appears imminent, but %s %s already lost something"
                                    + " far more valuable than a simple sex fight...",
                                    target.directObject(), target.possessiveAdjective(), getSelf().nameDirectObject(),
                                    target.subjectAction("feel"), getSelf().subject(), getSelf().pronoun(),
                                    target.subjectAction("are", "is"), Global.capitalizeFirstLetter(target.possessiveAdjective()),
                                    target.pronoun(), target.action("have", "has"));
                default:
                    // Should never happen
                    return " nothing. You should be feeling something, but you're not.";
            }
        }
    }

    @Override
    public boolean makesContact(Combat c) {
        return true;
    }
    
    @Override
    public Stage getStage() {
        return Stage.FINISHER;
    }
}
