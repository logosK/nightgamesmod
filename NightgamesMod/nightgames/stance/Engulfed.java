package nightgames.stance;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.skills.damage.DamageType;

public class Engulfed extends Position {

    private boolean slimePitches;

    public Engulfed(Character top, Character bottom) {
        super(top, bottom, Stance.engulfed);
        slimePitches = slimePitches();
    }

    @Override
    public String describe(Combat c) {
        if (top.human()) {
            return "You have engulfed " + bottom.getName() + " inside your slime body, with only "
                            + bottom.possessiveAdjective() + " face outside of you.";
        } else {
            return String.format("%s is holding %s entire body inside "
                            + "%s slime body, with only %s face outside.",
                            top.nameOrPossessivePronoun(), bottom.nameOrPossessivePronoun(),
                            top.possessiveAdjective(), bottom.possessiveAdjective());
        }
    }

    @Override
    public int pinDifficulty(Combat c, Character self) {
        return 15;
    }

    @Override
    public boolean mobile(Character c) {
        return c != bottom;
    }

    @Override
    public String image() {
        if (bottom.hasPussy()) {
            return "engulfed_f.jpg";
        } else {
            return "engulfed_m.jpg";
        }
    }

    @Override
    public boolean kiss(Character c, Character target) {
        return c == top || (target == top && c != bottom);
    }

    @Override
    public boolean dom(Character c) {
        return c == top;
    }

    @Override
    public boolean sub(Character c) {
        return c == bottom;
    }

    @Override
    public boolean reachTop(Character c) {
        return c == top;
    }

    @Override
    public boolean reachBottom(Character c) {
        return c == top;
    }

    @Override
    public boolean prone(Character c) {
        return false;
    }


    @Override
    public boolean feet(Character c, Character target) {
        return c == top;
    }

    @Override
    public boolean oral(Character c, Character target) {
        return c == top;
    }

    @Override
    public boolean behind(Character c) {
        return c == top;
    }

    @Override
    public boolean front(Character c) {
        return true;
    }

    @Override
    public boolean inserted(Character c) {
        return slimePitches == (c == top);
    }

    @Override
    public Position insertRandom(Combat c) {
        return new Neutral(top, bottom);
    }

    @Override
    public Position reverse(Combat c, boolean writeMessage) {
        if (bottom.has(Trait.slime)) {
            if (writeMessage) {
                c.write(bottom, String.format("%s %s slimy body a"
                                + "round %s, reversing %s hold.",
                                bottom.subjectAction("swirls", "swirl"), bottom.possessiveAdjective(),
                                top.nameOrPossessivePronoun(), top.possessiveAdjective()));
            }
            return super.reverse(c, writeMessage);
        }
        if (writeMessage) {
            c.write(bottom, String.format("%s loose from %s slimy grip and %s away from %s.", 
                            bottom.subjectAction("struggles", "struggle"), top.nameOrPossessivePronoun(),
                            bottom.action("stagger", "staggers"), top.directObject()));
        }
        return new Neutral(top, bottom);
    }

    @Override
    public void decay(Combat c) {
        time++;
        bottom.weaken(c, (int) top.modifyDamage(DamageType.stance, bottom, 5));
        top.emote(Emotion.dominant, 10);
    }

    @Override
    public float priorityMod(Character self) {
        return dom(self) ? 5 : 0;
    }

    @Override
    public List<BodyPart> topParts(Combat c) {
        List<BodyPart> parts = new ArrayList<>();
        if (slimePitches) {
            parts.addAll(top.body.get("cock"));
        } else {
            parts.addAll(top.body.get("pussy"));
            parts.addAll(top.body.get("ass"));
        }
        return parts.stream()
                    .filter(part -> part != null && part.present())
                    .collect(Collectors.toList());
    }

    @Override
    public List<BodyPart> bottomParts() {
        List<BodyPart> parts = new ArrayList<>();
        if (!slimePitches) {
            parts.addAll(bottom.body.get("cock"));
        } else {
            parts.addAll(bottom.body.get("pussy"));
            parts.addAll(bottom.body.get("ass"));
        }
        return parts.stream()
                    .filter(part -> part != null && part.present())
                    .collect(Collectors.toList());
    }

    @Override
    public boolean faceAvailable(Character target) {
        return target == top;
    }

    @Override
    public double pheromoneMod(Character self) {
        return 10;
    }

    private boolean slimePitches() {
        if (!top.hasDick())
            return false;
        if (!bottom.hasDick())
            return true;
        return Global.random(2) == 0;
    }
    
    @Override
    public Position.Dominance dominance() {
        return Position.Dominance.ABSURD;
    }
    
    @Override
    public int distance() {
        return 0;
    }

    private void pleasureRandomCombination(Combat c, Character self, Character opponent) {
        int selfM = Global.random(6, 11);
        int targM = Global.random(6, 11);
        List<Runnable> possibleActions = new ArrayList<>();
        if (opponent.hasDick()) {
            if (self.hasPussy()) {
                possibleActions.add(() -> {
                    opponent.body.pleasure(self, self.body.getRandomPussy(), opponent.body.getRandomCock(), selfM, c);
                    self.body.pleasure(opponent, opponent.body.getRandomCock(), self.body.getRandomPussy(), targM, c);
                });
            }
            possibleActions.add(() -> {
                opponent.body.pleasure(self, self.body.getRandomAss(), opponent.body.getRandomCock(), selfM, c);
                self.body.pleasure(opponent, opponent.body.getRandomCock(), self.body.getRandomAss(), targM, c);
            });
        }
        if (self.hasDick()) {
            if (opponent.hasPussy()) {
                possibleActions.add(() -> {
                    opponent.body.pleasure(self, self.body.getRandomCock(), opponent.body.getRandomPussy(), selfM, c);
                    self.body.pleasure(opponent, opponent.body.getRandomPussy(), self.body.getRandomCock(), targM, c);
                });
            }
            possibleActions.add(() -> {
                opponent.body.pleasure(self, self.body.getRandomCock(), opponent.body.getRandomAss(), selfM, c);
                self.body.pleasure(opponent, opponent.body.getRandomAss(), self.body.getRandomCock(), targM, c);
            });
        }
        Optional<Runnable> action = Global.pickRandom(possibleActions);
        if (action.isPresent()) {
            action.get().run();
        }
    }

    @Override
    public void struggle(Combat c, Character struggler) {
        Character opponent = getPartner(c, struggler);
        c.write(struggler, Global.format("{self:SUBJECT-ACTION:attempt} to find {self:possessive} way out of "
                        + "the endless slimey hell {self:pronoun-action:have} found {self:reflective} in. "
                        + "However, none of {self:possessive} attempts make any purchase, as {other:possessive} formless body merely swallows "
                        + "{self:direct-object} back up when {self:pronoun-action:try}. "
                        + "All it really ends up accomplishing is some friction between {self:possessive} genitals and {other:poss-pronoun}.", struggler, opponent));
        pleasureRandomCombination(c, struggler, opponent);
        super.struggle(c, struggler);
    }

    @Override
    public void escape(Combat c, Character escapee) {
        Character opponent = getPartner(c, escapee);
        c.write(escapee, Global.format("{self:SUBJECT-ACTION:attempt} to talk {self:possessive} way out of "
                        + "the endless slimey hell {self:pronoun-action:have} found {self:reflective} in. "
                        + "However, none of {self:possessive} attempts to have {other:name-do} release {self:direct-object} does any good, "
                        + "as {other:pronoun} just stares at {self:direct-object} emotionlessly while teasing {self:possessive} lower half encased in {other:possessive} slime.", escapee, opponent));
        pleasureRandomCombination(c, escapee, opponent);
        super.escape(c, escapee);
    }
}
