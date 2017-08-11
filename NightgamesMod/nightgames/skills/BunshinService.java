package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;

public class BunshinService extends Skill {

    public BunshinService(Character self) {
        super("Bunshin Service", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return getSelf().getPure(Attribute.Ninjutsu) >= 12;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return c.getStance()
                .mobile(getSelf())
                        && !c.getStance()
                             .prone(getSelf())
                        && getSelf().canAct() && !c.getStance()
                                                   .behind(target)
                        && !c.getStance()
                             .penetrated(c, target)
                        && !c.getStance()
                             .penetrated(c, getSelf())
                        && target.mostlyNude();
    }

    @Override
    public int getMojoCost(Combat c) {
        return numberOfClones(c) * 2;
    }

    @Override
    public String describe(Combat c) {
        return "Pleasure your opponent with shadow clones: 4 mojo per attack (min 2))";
    }

    private int numberOfClones(Combat c) {
        return Math.min(Math.min(getSelf().getMojo().get()/2, getSelf().get(Attribute.Ninjutsu)/2), 15);
    }

    @Override
    public int accuracy(Combat c, Character target) {
        return 25 + getSelf().get(Attribute.Speed) * 5;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        int clones = numberOfClones(c);
        Result r;
        if(getSelf().human()){
            c.write(getSelf(), String.format("You form %d shadow clones and rush forward.",clones));
        }
        else if(c.shouldPrintReceive(target, c)){
            c.write(getSelf(), String.format("%s moves in a blur and suddenly %s %d of %s approaching %s.",getSelf().getName(),
                            target.subjectAction("see"),clones,getSelf().pronoun(),target.reflexivePronoun()));
        }
        for (int i = 0; i < clones; i++) {
            if (target.roll(getSelf(), c, accuracy(c, target))) {
                switch (Global.random(4)) {
                    case 0:
                        r = Result.weak;
                        target.tempt(Global.random(3) + getSelf().get(Attribute.Seduction) / 4);
                        break;
                    case 1:
                        r = Result.normal;
                        target.body.pleasure(getSelf(),  getSelf().body.getRandom("hands"),target.body.getRandomBreasts(),
                                        Global.random(3 + getSelf().get(Attribute.Seduction) / 2)
                                                        + target.get(Attribute.Perception) / 2,
                                        c, this);
                        break;
                    case 2:
                        r = Result.strong;
                        BodyPart targetPart = target.body.has("cock") ? target.body.getRandomCock()
                                        : target.hasPussy() ? target.body.getRandomPussy()
                                                        : target.body.getRandomAss();
                        target.body.pleasure(getSelf(), getSelf().body.getRandom("hands"),targetPart, 
                                        Global.random(4 + getSelf().get(Attribute.Seduction))
                                                        + target.get(Attribute.Perception) / 2,
                                        c, this);
                        break;
                    default:
                        r = Result.critical;
                        targetPart = target.body.has("cock") ? target.body.getRandomCock()
                                        : target.hasPussy() ? target.body.getRandomPussy()
                                                        : target.body.getRandomAss();
                        target.body.pleasure(getSelf(),getSelf().body.getRandom("hands"), targetPart, Global.random(6)
                                        + getSelf().get(Attribute.Seduction) / 2 + target.get(Attribute.Perception), c,
                                        this);
                        break;
                }
                writeOutput(c, r, target);
            } else {
                writeOutput(c, Result.miss, target);
            }
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new BunshinService(user);
    }

    @Override
    public int speed() {
        return 4;
    }
    
    @Override
    public Tactics type(Combat c) {
        return Tactics.pleasure;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if(modifier==Result.miss){
            return String.format("%s dodges your clone's groping hands.",target.getName());
        }else if(modifier==Result.weak){
            return String.format("Your clone darts close to %s and kisses %s on the lips.",target.getName(),target.directObject());
        }else if(modifier==Result.strong){
            if(target.hasDick()){
                return String.format("Your shadow clone grabs %s's dick and strokes it.",target.getName());
            }else{
                return String.format("Your shadow clone fingers and caresses %s's pussy lips.",target.getName());
            }
        }else if(modifier==Result.critical){
            if(target.hasDick()){
                return String.format("Your clone attacks %s's sensitive penis, rubbing and stroking %s glans.",target.getName(),target.possessiveAdjective());
            }else{
                return String.format("Your clone slips between %s's legs to lick and suck %s swollen clit.",target.getName(),target.possessiveAdjective());
            }
        }else{
            return String.format("A clone pinches and teases %s's nipples",target.getName());
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if(modifier==Result.miss){
            return String.format("%s to avoid one of the shadow clones.",
                            target.subjectAction("manage"));
        }else if(modifier==Result.weak){
            return String.format("One of the %ss grabs %s and kisses %s enthusiastically.",getSelf().getName(),
                            target.subject(), target.directObject());
        }else if(modifier==Result.strong){
            if(target.hasBalls()){
                return String.format("A clone gently grasps and massages %s sensitive balls.",
                                target.nameOrPossessivePronoun());
            }else{
                return String.format("A clone teases and tickles %s inner thighs and labia.",
                                target.nameOrPossessivePronoun());
            }
        }else if(modifier==Result.critical){
            if(target.hasDick()){
                return String.format("One of the %s clones kneels between %s legs to lick and suck %s cock.",getSelf().getName(),
                                target.nameOrPossessivePronoun(), target.possessiveAdjective());
            }else{
                return String.format("One of the %s clones kneels between %s legs to lick %s nether lips.",getSelf().getName(),
                                target.nameOrPossessivePronoun(), target.possessiveAdjective());
            }
        }else{
            if(getSelf().hasBreasts()){
                return String.format("A %s clone presses her boobs against %s and teases %s nipples.",getSelf().getName(),
                                target.subject(), target.possessiveAdjective());
            }else{
                return String.format("A %s clone caresses %s chest and teases %s nipples.",getSelf().getName(),
                                target.nameOrPossessivePronoun(), target.possessiveAdjective());
            }
            
        }
    }

}
