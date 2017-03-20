package nightgames.skills.strategy;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.nskills.tags.SkillTag;
import nightgames.skills.Skill;

public abstract class AbstractStrategy implements CombatStrategy {
    protected Set<Skill> getAllowedSkills(Combat c, nightgames.characters.Character self) {
        Set<Skill> availableSkills = new HashSet<>(self.getSkills());
        Skill.filterAllowedSkills(c, availableSkills, self);
        Set<Skill> allowedSkills = availableSkills.stream().filter(skill -> Skill.isUsable(c, skill)).collect(Collectors.toSet());
        return allowedSkills;
    }
    /*
    protected Set<Skill> getAllowedSkillsWithMoreMojo(Combat c, nightgames.characters.Character self, int additionalmojo) {
        nightgames.characters.Character other = c.getOther(self);
        Set<Skill> availableSkills = new HashSet<>(self.getSkills());
        Skill.filterAllowedSkills(c, availableSkills, self, other);
        Set<Skill> allowedSkills = availableSkills.stream().filter(skill -> Skill.skillIsUsableWithMoreMojo(c, skill, other, additionalmojo)).collect(Collectors.toSet());
        return allowedSkills;
    }*/
    
    protected abstract Set<Skill> filterSkills(Combat c, nightgames.characters.Character self, Set<Skill> allowedSkills);
    
    public Set<Skill> nextSkills(Combat c, Character self) {
        return filterSkills(c, self, getAllowedSkills(c, self));
    }
    
    /*protected Set<Skill> filterSkillsWithTags(Set<SkillTag> goodTags, Set<SkillTag> badtags) {
        int a = (int) (self.getMojo().max()*0.15);
        return null;
    }*/
}
