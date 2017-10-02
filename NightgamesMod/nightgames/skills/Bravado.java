package nightgames.skills;

import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;

public class Bravado extends Skill {
    int cost;

    public Bravado(Character self) {
        super("Determination", self, 5);
        cost = 0;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.has(Trait.fearless);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canRespond() && c.getStance().mobile(getSelf());
    }

    @Override
    public int getMojoCost(Combat c) {
        cost = Math.max(20, getSelf().getMojo().get());
        return cost;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        int x = cost;
        writeOutput(c, Result.normal, target);
        getSelf().calm(c, 20 + x / 2);                  //TODO: Consider Buffing this. Also Parenthesize it properly.
        getSelf().heal(c, x);                           //TODO: Consider nerfing this part. 
        getSelf().restoreWillpower(c, 2 + x / 10);      //TODO: Buff this. Also Parenthesize it properly.
        getSelf().emote(Emotion.confident, 30);
        getSelf().emote(Emotion.dominant, 20);
        getSelf().emote(Emotion.nervous, -20);
        getSelf().emote(Emotion.desperate, -30);
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new Bravado(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.recovery;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return "You grit your teeth and put all your willpower into the fight.";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character attacker) {
        return getSelf().getName() + " gives "+attacker.nameDirectObject()+" a determined glare as " + getSelf().pronoun() + " seems to gain a second wind.";
    }

    @Override
    public String describe(Combat c) {
        return "Consume mojo to restore stamina and reduce arousal";
    }

}
