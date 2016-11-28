package nightgames.stance;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class BehindFootjob extends AbstractBehindStance {
    public BehindFootjob(Character top, Character bottom, boolean analPenetration) {
        super(top, bottom, Stance.behindfootjob);
        this.analPenetration=analPenetration;
    }
    
    private boolean analPenetration;

    @Override
    public String describe(Combat c) {
        return Global.format(
                        "{self:SUBJECT-ACTION:are|is} holding {other:name-do} from behind with {self:possessive} legs wrapped around {other:direct-object}",
                        top, bottom)+(analPenetration?" "+top.name()+" is also fucking "+bottom+" in the ass.":"");
    }

    @Override
    public int pinDifficulty(Combat c, Character self) {
        return analPenetration?8:6;
    }

    @Override
    public boolean mobile(Character c) {
        return c != bottom;
    }

    @Override
    public String image() {
        if (bottom.hasDick() && (!bottom.hasPussy() || Global.random(2)==0)) {
            return "behind_footjob.jpg";
        } else {
            return "heelgrind.jpg";
        }
    }

    @Override
    public boolean kiss(Character c, Character target) {
        return c != top && c != bottom;
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
        return c != bottom;
    }

    @Override
    public boolean reachBottom(Character c) {
        return c != top && c != bottom;
    }

    @Override
    public boolean prone(Character c) {
        return false;
    }

    @Override
    public boolean feet(Character c, Character target) {
        return target == bottom;
    }

    @Override
    public boolean oral(Character c, Character target) {
        return target == bottom && c != top;
    }

    @Override
    public boolean behind(Character c) {
        return c == top;
    }

    @Override
    public boolean inserted(Character c) {
        return analPenetration && (c == top);
    }

    @Override
    public float priorityMod(Character self) {
        return getSubDomBonus(self, analPenetration?6.0f:4.0f );
    }

    @Override
    public Position reverse(Combat c, boolean writeMessage) {
        if (writeMessage) {
            c.write(bottom, Global.format(
                            "{self:SUBJECT-ACTION:summon what little willpower you have left and grab|grabs} {other:name-possessive} feet and pull them off {self:name-possessive} crotch. Taking advantage of the momentum, {self:subject} push {other:direct-object} back with {self:name-possessive} body and hold {other:direct-object} down while sitting on top of {other:direct-object}.",
                            bottom, top));
        }
        return new ReverseMount(bottom, top);
    }

    @Override
    public double pheromoneMod(Character self) {
        return analPenetration?2.0:1.5;
    }
    
    @Override
    public int dominance() {
        return analPenetration?6:4;
    }
    //@Override
    public List<BodyPart> topParts() {
        if(!analPenetration) return Collections.emptyList();
        return Arrays.asList(top.body.getRandomInsertable()).stream().filter(part -> part != null && part.present())
                        .collect(Collectors.toList());
    }

    @Override
    public List<BodyPart> bottomParts() {
        if(!analPenetration) return Collections.emptyList();
        return Arrays.asList(bottom.body.getRandomAss()).stream().filter(part -> part != null && part.present())
                        .collect(Collectors.toList());
    }

    @Override
    public int distance() {
        return 1;
    }
    

}
