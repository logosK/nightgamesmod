package nightgames.pet.arms.skills;

import java.util.Arrays;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.items.clothing.Clothing;
import nightgames.items.clothing.ClothingSlot;
import nightgames.items.clothing.ClothingTrait;
import nightgames.pet.PetCharacter;
import nightgames.pet.arms.Arm;

public class Strip extends ArmSkill {

    public Strip() {
        super("Strip", 20);
    }

    @Override
    public boolean usable(Combat c, Arm arm, Character owner, Character target) {
        return super.usable(c, arm, owner, target);
    }
    
    @Override
    public boolean resolve(Combat c, Arm arm, Character owner, Character target) {
        if (target.outfit.isNude()) return resolveDefault(c, arm, owner, target);
        boolean sub = c.getStance().dom(owner);
        double accuracy = 10 + owner.get(Attribute.Science);
        boolean hasTop = !target.outfit.slotEmpty(ClothingSlot.top);
        boolean hasBottom = !target.outfit.slotEmpty(ClothingSlot.bottom);
        ClothingSlot slot;
        
        if (hasTop && hasBottom) {
            slot = Global.random(2) == 0 ? ClothingSlot.top : ClothingSlot.bottom;
        } else if (hasTop) {
            slot = ClothingSlot.top;
        } else if (hasBottom) {
            slot = ClothingSlot.bottom;
        } else {
            accuracy *= 1.5;
            slot = Global.pickRandom(Arrays.stream(ClothingSlot.values()).filter(s -> !target.outfit.
                            slotEmpty(s)).toArray(ClothingSlot[]::new)).get();
        }
        
        if (sub || Global.random(100) < accuracy) {
            Clothing stripped = target.outfit.getTopOfSlot(slot);
            if (stripped.is(ClothingTrait.indestructible)) {
                c.write(PetCharacter.DUMMY, Global.format("A %s lunges at {other:name-do}, its attachment"
                                + " grabbing for {other:possessive} %s. It fails to remove it, however."
                                , owner, target, arm.getName(), stripped.getName()));
            } else {
                c.write(PetCharacter.DUMMY, Global.format("A steely blur flies at {other:name-do},"
                                + " revealing itself to be a %s which has already latched on"
                                + " to {other:possessive} %s. The poor garment is no match"
                                + " for {self:name-possessive} technological advantage and it falls"
                                + " to the ground in tatters as the arm rips it off of {other:direct-object}."
                                , owner, target, arm.getName(), stripped.getName()));
                target.shred(slot);
                return true;
            }
        } else {
            c.write(PetCharacter.DUMMY, Global.format("{other:SUBJECT-ACTION:manage|manages} to dodge"
                            + " {self:name-possessive} %s as it flies at {other:possessive} clothes.",
                            owner, target, arm.getName()));
        }
        
        return false;
    }
    
    public boolean resolveDefault(Combat c, Arm arm, Character owner, Character target) {
        boolean sub = c.getStance().dom(owner);
        double accuracy = 10 + owner.get(Attribute.Science);
        int m = Global.random(4, 7);
        if (sub || Global.random(100) < accuracy) {
            target.pain(c, owner, m);
            target.loseMojo(c, 5);
            target.emote(Emotion.angry, 15);
            c.write(PetCharacter.DUMMY, Global.format("A steely blur flies at {other:name-do},"
                            + " revealing itself to be a %s which has already latched on"
                            + " to {other:possessive} nipples. The %s pulls and twists painfully before losing its grip."
                            , owner, target, arm.getName(), arm.getName()));
            return true;
        } else {
            c.write(PetCharacter.DUMMY, Global.format("{other:SUBJECT-ACTION:manage|manages} to dodge"
                            + " {self:name-possessive} %s as it flies at {other:direct-object}.",
                            owner, target, arm.getName()));
        }
        return false;
    }
}
