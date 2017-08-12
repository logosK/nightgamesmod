package nightgames.skills;

import java.util.ArrayList;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.status.BodyFetish;
import nightgames.status.Compulsion;
import nightgames.status.Enthralled;
import nightgames.status.Flatfooted;
import nightgames.status.FluidAddiction;
import nightgames.status.Plasticized;
import nightgames.status.Status;
import nightgames.status.Stunned;
import nightgames.status.Trance;
import nightgames.status.Winded;

public class Nothing extends Skill {

    public Nothing(Character self) {
        super("Nothing", self);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return true;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (getSelf().human()) {
            deal(c, 0, Result.normal, target);
        } else {
            receive(c, 0, Result.normal, target);
        }
        boolean legitreason=false;
        ArrayList<BodyFetish> fetishes = new ArrayList<BodyFetish>();
        for (Status s : getSelf().status) {
            if (s instanceof BodyFetish) {
                fetishes.add((BodyFetish)s);
            }
            if (s instanceof Flatfooted || s instanceof Trance || s instanceof Enthralled || s instanceof Winded || s instanceof Compulsion || s instanceof Plasticized || s instanceof Stunned || s instanceof FluidAddiction) {
                legitreason=true;
            }
        }
        if(!legitreason) {
            for(BodyFetish bf:fetishes) {
                if(bf.magnitude>0.75) bf.magnitude-=0.25;
            }
        }
        return true;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new Nothing(user);
    }

    @Override
    public int speed() {
        return 0;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.misc;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return "You are unable to do anything.";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return getSelf().subject() + "is unable to do anything.";
    }

    @Override
    public String describe(Combat c) {
        return "Do nothing";
    }
}
