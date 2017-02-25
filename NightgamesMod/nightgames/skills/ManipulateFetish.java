package nightgames.skills;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.BodyFetish;
import nightgames.status.Enthralled;
import nightgames.status.Stsflag;

public class ManipulateFetish extends Skill {
    
    private Optional<BodyPart> withPart = null;
    
    public ManipulateFetish(Character self) {
        super("Manipulate Fetish", self, 5);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        List<BodyPart> validParts = getFetishes(target).filter(bf->bf.magnitude > 2.5 && user.body.has(bf.part)).map(bf -> (BodyPart)(user.body.getRandom(bf.part))).collect(Collectors.toList());
        withPart = Global.pickRandom(validParts);
        return user.get(Attribute.Fetish) >= 25;
    }

    public Stream<BodyFetish> getFetishes(Character c) {
        return (Stream<BodyFetish>) c.status.stream().filter(status -> status.flags().contains(Stsflag.bodyfetish)).map(s->(BodyFetish)s);
    }
    
    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canRespond() && !target.wary() && withPart.isPresent();
    }

    @Override
    public int getMojoCost(Combat c) {
        return 40;
    }

    @Override
    public int accuracy(Combat c, Character target) {
        return 100;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        Result result = Result.normal;
        writeOutput(c, result, target);
        target.add(c, new Enthralled(target, getSelf(), 5));
        target.body.getFetish(withPart.get().getType()).get().magnitude -= 1;
        getSelf().emote(Emotion.dominant, 50);
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new ManipulateFetish(user);
    }

    @Override
    public int speed() {
        return 9;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.debuff;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return Global.format(
                            "Noticing {other:name-possessive}'s obsession with "+withPart.get().getType()+"s, {self:subject} {other:subject-action:demand|demands} {other:possessive} obey, on pain of not getting {self:possessive} "+withPart.get().getType()+".",
                            getSelf(), target);
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return deal(c, damage, modifier, target);
    }

    @Override
    public String describe(Combat c) {
        return "Enthralls your opponent by threatening to deprive them of their fetish.";
    }
}
