package nightgames.skills;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.status.addiction.Addiction;
import nightgames.status.addiction.AddictionType;

public class Bite extends Skill {

    public Bite(Character self) {
        super("Bite", self, 5);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.has(Trait.breeder);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return c.getStance().penetratedBy(c, getSelf(), target) && c.getStance().kiss(getSelf(), target);
    }

    @Override
    public String describe(Combat c) {
        return "Instill a lasting need to fuck";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        boolean katOnTop = c.getStance().dom(getSelf());
        if (katOnTop) {
            c.write(getSelf(), "Kat leans in close, grinding her breasts against you and biting your neck!"
                            + " You briefly panic, but you know Kat wouldn't seriously hurt you. She quickly sits"
                            + " back up, riding you with a fierce intensity. An unnatural warmth spreads from where"
                            + " she's bitten you, and her movements suddenly feel even better than before.");
        } else {
            c.write(getSelf(), "Kat grabs your head and pulls it down beside hers, then she twists and bites you!"
                            + " You think she's broken your skin, but you're not bleeding. A warmth spreads down"
                            + " from your neck as Kat smiles at you coyly. <i>\"It, ah, feels so much better with a"
                            + " little bit of animal instinct, nya?\"</i> You're not sure what she means, but"
                            + " you do realize you've sped up your thrusting and it does seem to feel even"
                            + " better than before.");            
        }
        target.addict(c, AddictionType.BREEDER, getSelf(), Addiction.MED_INCREASE);
        
        return true;
    }
    
    public float priorityMod(Combat c) {
        return 10.f;
    }

    @Override
    public Skill copy(Character user) {
        return new Bite(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.fucking;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        // TODO Auto-generated method stub
        return null;
    }

}
