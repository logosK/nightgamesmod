package nightgames.pet.arms.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.items.clothing.Clothing;
import nightgames.items.clothing.ClothingSlot;
import nightgames.pet.PetCharacter;
import nightgames.pet.arms.Arm;

public class DefabRay extends ArmSkill {
    public DefabRay() {
        super("Defabrication Ray", 30);
    }

    @Override
    public boolean usable(Combat c, Arm arm, Character owner, Character target) {
        return super.usable(c, arm, owner, target) && !target.outfit.isNude();
    }

    @Override
    public boolean resolve(Combat c, Arm arm, Character owner, Character target) {
        if (target.outfit.isNude()) return resolveBackup(c, arm, owner, target);
        boolean sub = c.getStance().dom(owner);
        boolean success = sub || Global.random(100) < 10 + owner.get(Attribute.Science);
        
        if (success) {
            ClothingSlot slot = target.outfit.getRandomShreddableSlot();
            Clothing item = target.outfit.getTopOfSlot(slot);
            if (item == null) {
                return false;
            }
            target.shred(slot);
            c.write(PetCharacter.DUMMY, Global.format("{self:NAME-POSSESSIVE} %s points at you, its"
                            + " head faintly glowing with a blue light. Suddenly, an eerily similar light"
                            + " surrounds {other:name-possessive} %s, and it soon disappears entirely!"
                            , owner, target, arm.getName(), item.toString()));
            return true;
        }
        return false;
    }
    
    public boolean resolveBackup(Combat c, Arm arm, Character owner, Character target) {
        boolean sub = c.getStance().dom(owner);
        boolean success = sub || Global.random(100) < 10 + owner.get(Attribute.Science);
        double damage = (sub?20:10) + owner.get(Attribute.Science);
        if (success) {
            BodyPart part = target.body.getRandomErogenous();
            target.body.pleasure(owner, null, part, damage, c);
            c.write(PetCharacter.DUMMY, Global.format("{self:NAME-POSSESSIVE} %s points at you, its"
                            + " head faintly glowing with a blue light. Suddenly, an eerily similar light"
                            + " surrounds {target:name-possessive} %s, and you feel it start tingling in an arousing way!"
                            , owner, target, arm.getName(), part.toString()));
            return true;
        }
        
        return false;
    }

}
