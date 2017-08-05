package nightgames.skills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.FocussedBase;
import nightgames.status.FocussedForeplay;
import nightgames.status.FocussedRecovery;
import nightgames.status.FocussedSex;
import nightgames.status.Stsflag;

public abstract class Focus extends Skill {

    private final FocussedBase focus;
    
    private Focus(String name, Character self, FocussedBase focus) {
        super(name, self);
        this.focus = focus;
    }

    @Override
    public final boolean requirements(Combat c, Character user, Character target) {
        return true;
    }

    @Override
    public final boolean usable(Combat c, Character target) {
        return !getSelf().is(Stsflag.focussed) && getSelf().canRespond();
    }

    protected abstract String message(Combat c, Character target);
    
    @Override
    public final boolean resolve(Combat c, Character target) {
        if (getSelf().human() || !target.is(Stsflag.blinded)) {
            c.write(getSelf(), Global.format(message(c, target), getSelf(), target));
        }
        getSelf().add(c, focus.instance(getSelf(), target));
        return true;
    }

    @Override
    public final Tactics type(Combat c) {
        return Tactics.recovery;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return null;
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return null;
    }

    public static class OnForeplay extends Focus {
        
        public OnForeplay(Character user) {
            super("Focus: Foreplay", user, new FocussedForeplay(user));
        }

        @Override
        protected String message(Combat c, Character target) {
            return "{self:SUBJECT-ACTION:wiggle} {self:possessive} fingers and"
                            + " {self:action:wet} {self:possessive} tongue, ready"
                            + " to use them to their full effect on {other:name-do}."
                            + " This does mean that {self:pronoun-action:will} be less"
                            + " prepared for more intense carnal acts.";
        }

        @Override
        public String describe(Combat c) {
            return "Focus on foreplay, leaving yourself vulnerable to sex."
                 + "\nThis will last for 10 turns, and cannot be cancelled or overridden.";
        }

        @Override
        public Skill copy(Character user) {
            return new OnForeplay(user);
        }
        
    }
    
    public static class OnSex extends Focus {
        
        public OnSex(Character user) {
            super("Focus: Sex", user, new FocussedSex(user));
        }

        @Override
        protected String message(Combat c, Character target) {
            return "{self:SUBJECT-ACTION:steel} {self:reflective} mind in anticipation"
                            + " of sex. While this will make {self:direct-object} more capable"
                            + " in that regard, it also creates a distinct uninspiredness"
                            + " when it comes to other activities.";
        }

        @Override
        public String describe(Combat c) {
            return "Focus on sex, leaving yourself vulnerable to other sources of pleasure."
                 + "\nThis will last for 10 turns, and cannot be cancelled or overridden.";
        }

        @Override
        public Skill copy(Character user) {
            return new OnSex(user);
        }
        
    }
    
    public static class OnRecovery extends Focus {
        
        public OnRecovery(Character user) {
            super("Focus: Recovery", user, new FocussedRecovery(user));
        }

        @Override
        protected String message(Combat c, Character target) {
            return "{self:SUBJECT-ACTION:take} a couple of deep breaths, focussing"
                            + " {self:reflective} on getting {self:reflective} back in"
                            + " order before diving back in.";
        }

        @Override
        public String describe(Combat c) {
            return "Focus on recovery, increasing your stamina and lowering arousal but"
                            + " leaving yourself vulnerable to any attacks."
                            + "\nThis will last for 10 turns, and cannot be cancelled or overridden.";
        }

        @Override
        public Skill copy(Character user) {
            return new OnRecovery(user);
        }
    }
}
