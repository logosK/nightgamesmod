package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.custom.CharacterLine;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.status.Satiated;

public class LevelDrain extends Drain {
    public LevelDrain(Character self) {
        super("Level Drain", self);

        addTag(SkillTag.drain);
        addTag(SkillTag.staminaDamage);
        addTag(SkillTag.fucking);
        addTag(SkillTag.dark);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Dark) >= 20 && !user.has(Trait.leveldrainer);
        //The second clause may seem incorrect, but it isn't. Characters with this
        //trait drain levels passively and cannot also use this skill.
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && c.getStance().canthrust(c, getSelf()) && c.getStance().havingSexNoStrapped(c)
                        && getSelf().getLevel() < 100 && getSelf().getLevel() < target.getLevel();
    }

    @Override
    public String describe(Combat c) {
        return "Drain your opponent of their levels";
    }

    @Override
    public int getMojoCost(Combat c) {
        return 60;
    }

    private int stealXP(Combat c, Character target) {
        int xpStolen = target.getXP();
        if (xpStolen <= 0) {
            return 0;
        }
        target.loseXP(xpStolen);
        getSelf().gainXPPure(xpStolen);
        getSelf().levelUpIfPossible(c);
        return xpStolen;
    }

    @Override
    public float priorityMod(Combat c) {
        return 5.0f;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        int type = Global.centeredrandom(2, getSelf().get(Attribute.Dark) / 20.0f, 2);
        writeOutput(c, type, Result.normal, target);
        switch (type) {
            case 0:
                getSelf().arouse(getSelf().getArousal().max(), c);
                break;
            case 1:
                int stolen = stealXP(c, target);
                if (stolen > 0) {
                    getSelf().add(c, new Satiated(getSelf(), stolen, 0));
                    if (getSelf().human()) {
                        c.write(getSelf(), "You have absorbed " + stolen + " XP from " + target.getName() + "!\n");
                    } else {
                        c.write(getSelf(), getSelf().getName() + " has absorbed " + stolen + " XP from you!\n");
                    }
                }
                break;
            case 2:
                int xpStolen = 95 + 5 * target.getLevel();
                getSelf().add(c, new Satiated(getSelf(), xpStolen, 0));
                c.write(target, target.dong());
                if (getSelf().human()) {
                    c.write(getSelf(), "You have stolen a level from " + target.getName() + "'s levels and absorbed it as " + xpStolen
                                    + " XP!\n");
                } else {
                    c.write(getSelf(), getSelf().getName() + " has stolen a level from "+target.subject()+" and absorbed it as " + xpStolen
                                    + " XP!\n");
                }
                getSelf().gainXPPure(xpStolen);
                getSelf().levelUpIfPossible(c);
                target.arouse(target.getArousal().max(), c);
                String levelDrainLine = getSelf().getRandomLineFor(CharacterLine.LEVEL_DRAIN_LINER, c, target);
                if (!levelDrainLine.isEmpty()) {
                    c.write(getSelf(), levelDrainLine);
                }
                break;
            default:
                break;
        }
        return type != 0;
    }

    @Override
    public Skill copy(Character target) {
        return new LevelDrain(target);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.pleasure;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (getSelf().hasPussy()) {
            String base = "You put your powerful vaginal muscles to work whilst" + " transfixing " + target.getName()
                            + "'s gaze with your own, goading "+target.possessiveAdjective()+" energy into "+target.possessiveAdjective()+" cock." + " Soon it erupts from "+target.nameDirectObject()+", ";
            switch (damage) {
                case 0:
                    return base + "but unfortunately you made a mistake, and the feedback leaves"
                                    + " you on the edge of climax!";
                case 1:
                    return base + "and you can feel "+target.possessiveAdjective()+" memories and experiences flow"
                                    + " into you, adding to your skill.";
                case 2:
                    return base + "far more powerfully than you even thought possible."
                                    + " You feel a fragment of "+target.possessiveAdjective()+" soul break away from "+target.nameDirectObject()+" and"
                                    + " spew into you, taking with it a portion of "+target.possessiveAdjective()+" very being"
                                    + "and merging with your own. You have clearly"
                                    + " won this fight, and a lot more than that.";
                default:
                    // Should never happen
                    return " but nothing happens, you feel strangely impotent.";
            }
        } else {
            String base = "With your cock deep inside " + target.getName()
                            + ", you can feel the heat from her core. You draw the energy from her, mining her depths. ";
            switch (damage) {
                case 0:
                    return "You attempt to drain " + target.getName()
                                    + "'s energy through your intimate connection, but it goes wrong. You feel intense pleasure feeding "
                                    + "back into you and threatening to overwhelm you. You brink the spiritual link as fast as you can, but you're still left on the brink of "
                                    + "climax.";
                case 1:
                    return "You attempt to drain " + target.getName()
                                    + "'s energy through your intimate connection, taking a bit of her experience.";
                case 2:
                    return base + "You succeed in siphoning off a portion of her soul, stealing a portion of her very being. This energy permanently "
                                    + "settles within you!";
                default:
                    // Should never happen
                    return " but nothing happens, you feel strangely impotent.";
            }
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        String demon = getSelf().useFemalePronouns() ? "succubus" : "incubus";
        
        String base = String.format("%s the %s' pussy suddenly tighten around %s. "
                        + "%s starts kneading %s dick, bringing %s immense pleasure and soon"
                        + " %s %s %s erupt into %s, but %s %s %s %s shooting"
                        + " something far more precious than semen into %s; as more of the ethereal"
                        + " fluid leaves %s, %s ",
                        target.subjectAction("feel"), demon, target.directObject(),
                        getSelf().subject(), target.possessiveAdjective(), target.directObject(),
                        target.subject(), target.action("feel"), target.reflexivePronoun(),
                        getSelf().directObject(), target.pronoun(), target.action("realize"),
                        target.pronoun(), target.action("are", "is"), getSelf().nameDirectObject(),
                        target.directObject(), target.subjectAction("feel"));
        switch (damage) {
            case 0:
                return String.format("%s squeezes %s with %s pussy and starts to milk %s, "
                                + "but %s suddenly %s %s shudder and moan loudly. "
                                + "Looks like %s plan backfired.", getSelf().subject(),
                                target.nameDirectObject(), getSelf().possessiveAdjective(),
                                target.directObject(), target.pronoun(), target.action("feel"),
                                getSelf().directObject(), getSelf().possessiveAdjective());
            case 1:
                return base + String.format("%s experiences and memories escape %s mind and flowing into %s.",
                                target.possessiveAdjective(), target.possessiveAdjective(), getSelf().directObject());
            case 2:
                return base + String.format("%s very being snap loose inside of %s and it seems to flow right "
                                + "through %s dick and into %s. When it is over %s... empty "
                                + "somehow. At the same time, %s seems radiant, looking more powerful,"
                                + " smarter and even more seductive than before. Through all of this,"
                                + " %s has kept on thrusting and %s right on the edge of climax."
                                + " %s defeat appears imminent, but %s %s already lost something"
                                + " far more valuable than a simple sex fight...",
                                target.possessiveAdjective(), target.directObject(), target.possessiveAdjective(),
                                getSelf().subject(), target.subjectAction("feel"), getSelf().subject(),
                                getSelf().pronoun(), target.subjectAction("are", "is"), 
                                Global.capitalizeFirstLetter(target.possessiveAdjective()),
                                target.pronoun(), target.action("have", "has"));
            default:
                // Should never happen
                return " nothing. You should be feeling something, but you're not.";
        }
    }

    @Override
    public boolean makesContact(Combat c) {
        return true;
    }
}
