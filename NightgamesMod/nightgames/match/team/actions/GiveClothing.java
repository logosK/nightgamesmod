package nightgames.match.team.actions;

import nightgames.characters.Character;
import nightgames.characters.CharacterSex;
import nightgames.global.Global;
import nightgames.items.clothing.Clothing;
import nightgames.items.clothing.ClothingSlot;
import nightgames.items.clothing.Outfit;
import nightgames.match.team.TeamMatch;

public class GiveClothing extends TeammateAction {

    private static final long serialVersionUID = -8837472478312309455L;
    private Clothing given;

    public GiveClothing(TeamMatch match, Character self) {
        super("Give Clothing", match, self);
    }

    @Override
    boolean additionalRequirements() {
        return decideClothing() != null;
    }
    
    @Override
    public String describe(Character doer) {
        boolean appropriate = isAppropriate(given, getCaptain());
        String msg = doer.getName() + " use <b>Give Clothing<\b>.\n";
        if (getCaptain().getOutfit().slotEmpty(ClothingSlot.bottom) && given.getSlots().contains(ClothingSlot.bottom)) {
            msg += "Looking at {other:name-possessive} exposed {other:main-genitals} thoughtfully for"
                            + " a moment, {self:subject-action:say|says} <i>\"You know, {other:name}, with you"
                            + " being our leader and all, I think you should at least have some underwear on you.\"</i>"
                            + " Without waiting for a response, {self:action:strip|strips} {self:possessive} "
                            + given.getName() + " off and hands it to {other:direct-object}. ";
            if (appropriate) {
                return Global.format(msg + "{other:SUBJECT} graciously {other:action:accept|accepts}"
                                + " the garment and {other:action:put|puts} it on.", self, getCaptain());
            } else {
                return Global.format(msg + "{other:SUBJECT-ACTION:look|looks} at {self:name-do} askance"
                                + " for a bit, wondering if this is some kind of joke. It soon becomes clear"
                                + " that it's not, and {other:pronoun} somewhat hesitantly {other:action:put|puts}"
                                + " it on.", self, getCaptain());
            }
        }
        msg += "{self:SUBJECT-ACTION:step|steps} in front of {other:name-do} and {self:action:stop|stops}"
                        + " {other:direct-object}. {self:PRONOUN} then {self:action:strip|strips} off"
                        + " {self:possessive} " + given.getName() + " and {self:action:hand|hands}"
                        + " it to {other:direct-object}. ";
        if (appropriate) {
            return Global.format(msg + "{other:SUBJECT-ACTION:look|looks} it over briefly, but really"
                            + " {other:action:have|has} no complaints and {other:action:put|puts} it on."
                            , self, getCaptain());            
        } else {
            return Global.format(msg + "{other:SUBJECT-ACTION:look|looks} down at it, then {other:action:turn|turns}"
                            + " eyes up at {self:name-do}. <i>\"Don't complain, cap. It's better than nothing, right?"
                            + "\"</i> With a sigh, {other:pronoun-action} puts on the %s.", 
                            self, getCaptain(), given.getName());
        }
    }

    @Override
    void effects() {
        given = decideClothing();
        self.getOutfit().unequip(given);
        getCaptain().getOutfit().equip(given);
    }

    private Clothing decideClothing() {
        if (self.getOutfit().isNude()) {
            return null;
        }
        
        Outfit own = self.getOutfit();
        Outfit capt = getCaptain().getOutfit();
        if (capt.getEquipped().equals(capt.getAll())) {
            return null;
        }
        
        if (capt.slotEmpty(ClothingSlot.bottom) && !own.slotEmpty(ClothingSlot.bottom)) {
            return findAppropriate(own, ClothingSlot.bottom);
        }

        if (capt.slotEmpty(ClothingSlot.top) && !own.slotEmpty(ClothingSlot.top)) {
            return findAppropriate(own, ClothingSlot.top);
        }

        return null;
    }
    
    private boolean isAppropriate(Clothing article, Character c) {
        return article.isForGender(c.body.guessCharacterSex())
                        || c.useFemalePronouns() && article.isForGender(CharacterSex.female);
    }
    
    private Clothing findAppropriate(Outfit outfit, ClothingSlot slot) {
        Clothing picked;
        int layer = Clothing.N_LAYERS;
        do {
            picked = outfit.getSlotAt(slot, --layer);
            if (picked != null && !isAppropriate(picked, getCaptain())) {
                picked = null;
            }
        } while (picked == null && layer > 0);
        
        return picked == null ? outfit.getTopOfSlot(slot) : picked;
    }
}
