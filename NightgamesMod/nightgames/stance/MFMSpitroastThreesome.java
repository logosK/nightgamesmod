package nightgames.stance;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.skills.Skill;
import nightgames.skills.Tactics;

public class MFMSpitroastThreesome extends MaledomSexStance {
    protected Character domSexCharacter;

    public MFMSpitroastThreesome(Character domSexCharacter, Character top, Character bottom) {
        super(top, bottom, Stance.doggy);
        this.domSexCharacter = domSexCharacter;
    }
    
    @Override
    public final boolean isThreesome() {
        return true;
    }

    @Override
    public Character domSexCharacter(Combat c) {
        return domSexCharacter;
    }

    @Override
    public boolean inserted(Character c) {
        return c == domSexCharacter;
    }

    @Override
    public void checkOngoing(Combat c) {
        if (!c.getOtherCombatants().contains(domSexCharacter)) {
            c.write(bottom, Global.format("With the disappearance of {self:name-do}, {other:subject-action:manage|manages} to escape.", domSexCharacter, bottom));
            c.setStance(new Neutral(top, bottom));
        }
    }

    @Override
    public float priorityMod(Character self) {
        return super.priorityMod(self) + 3;
    }

    @Override
    public void setOtherCombatants(List<? extends Character> others) {
        for (Character other : others) {
            if (other.equals(domSexCharacter)) {
                domSexCharacter = other;
            }
        }
    }

    @Override
    public List<BodyPart> partsForStanceOnly(Combat combat, Character self, Character other) {
        if (self == domSexCharacter(combat) && other == bottom) {
            return topParts(combat);
        }
        if (self == top) {
                return Arrays.asList(top.body.getRandomInsertable()).stream().filter(part -> part != null && part.present())
                                .collect(Collectors.toList());
        } else if (self == bottom) {
            if (other == top) {
                return Arrays.asList(top.body.getRandom("mouth")).stream().filter(part -> part != null && part.present())
                                .collect(Collectors.toList());
            } else if (other == domSexCharacter) {
                return Arrays.asList(top.body.getRandomPussy()).stream().filter(part -> part != null && part.present())
                                .collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }

    public Character getPartner(Combat c, Character self) {
        Character domSex = domSexCharacter(c);
        if (self == top) {
            return bottom;
        } else if (domSex == self) {
            return bottom;
        } else {
            return domSex;
        }
    }

    @Override
    public String describe(Combat c) {
        if (top.human()) {
            return "";
        } else {
            return String.format("%s is fucking %s face while %s taking %s from behind.",
                            top.subject(), bottom.nameOrPossessivePronoun(), domSexCharacter(c).subjectAction("are", "is"), bottom.directObject());
        }
    }

    public List<Character> getAllPartners(Combat c, Character self) {
        if (self == bottom) {
            return Arrays.asList(top, domSexCharacter);
        }
        return Collections.singletonList(getPartner(c, self));
    }

    @Override
    public boolean mobile(Character c) {
        return c != bottom;
    }

    @Override
    public String image() {
        return "ThreesomeMFMSpitroast.jpg";
    }

    @Override
    public boolean kiss(Character c, Character target) {
        return c != bottom && target != bottom;
    }

    @Override
    public boolean dom(Character c) {
        return c == top || c == domSexCharacter;
    }

    @Override
    public boolean sub(Character c) {
        return c == bottom;
    }

    @Override
    public boolean reachTop(Character c) {
        return c != bottom;
    }

    @Override
    public boolean reachBottom(Character c) {
        return true;
    }

    @Override
    public boolean prone(Character c) {
        return c == bottom;
    }

    @Override
    public boolean behind(Character c) {
        return c == domSexCharacter;
    }

    @Override
    public Position insertRandom(Combat c) {
        return new Mount(top, bottom);
    }

    @Override
    public Position reverse(Combat c, boolean writeMessage) {
        if (writeMessage) {
            c.write(bottom, Global.format("{self:SUBJECT-ACTION:manage|manages} to unbalance {other:name-do} and push {other:direct-object} off {self:reflective}.", bottom, top));
        }
        return new Neutral(bottom, top);
    }
    
    @Override
    public Position.Dominance dominance() {
        return Position.Dominance.HIGH;
    }

    @Override
    public Optional<Collection<Skill>> allowedSkills(Combat c, Character self) {
        if (self != domSexCharacter) {
            return Optional.empty();
        } else {
            Collection<Skill> avail = self.getSkills().stream()
                            .filter(skill -> skill.requirements(c, self, bottom))
                            .filter(skill -> Skill.isUsableOn(c, skill, bottom))
                            .filter(skill -> skill.type(c) == Tactics.fucking).collect(Collectors.toSet());
            return Optional.of(avail);
        }
    }
}
