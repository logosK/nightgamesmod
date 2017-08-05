package nightgames.requirements;

import nightgames.actions.Movement;
import nightgames.areas.Area;
import nightgames.characters.*;
import nightgames.characters.body.CockPart;
import nightgames.characters.body.PussyPart;
import nightgames.characters.body.mods.FieryMod;
import nightgames.characters.body.mods.SizeMod;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.items.clothing.Clothing;
import nightgames.modifier.standard.NoModifier;
import nightgames.stance.*;
import nightgames.status.Alert;
import nightgames.status.Stsflag;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import static nightgames.requirements.RequirementShortcuts.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests for Requirement functionality.
 */
public class RequirementTest {
    private static TrueRequirement trueReq() {
        return new TrueRequirement();
    }

    private static FalseRequirement falseReq() {
        return new FalseRequirement();
    }

    private NPC self;
    private NPC other;
    private Combat combat;

    @BeforeClass public static void setUpClass() throws Exception {
        Clothing.buildClothingTable();
        Global.initForTesting();
        Global.newGame("TestPlayer", Optional.empty(), new ArrayList<>(), CharacterSex.asexual, new HashMap<>());
    }

    @Before public void setUp() throws Exception {
        self = new BlankPersonality("SelfTestNPC").character;
        other = new BlankPersonality("OtherTestNPC").character;
        Area area = new Area("TestArea", "TestArea description", Movement.beer);
        Global.setUpMatch(new NoModifier());
        combat = new Combat(self, other, area);
    }

    @Test public void analTest() throws Exception {
        combat.setStance(new Anal(other, self));
        assertThat(anal().meets(combat, self, other), is(true));
        assertThat(anal().meets(combat, other, self), is(false));
    }

    @Test public void andTest() throws Exception {
        // truth table tests
        assertThat(and().meets(combat, self, other), is(false));
        assertThat(and(trueReq()).meets(combat, self, other), is(true));
        assertThat(and(falseReq()).meets(combat, self, other), is(false));
        assertThat(and(trueReq(), trueReq()).meets(combat, self, other), is(true));
        assertThat(and(trueReq(), falseReq()).meets(combat, self, other), is(false));
        assertThat(and(falseReq(), trueReq()).meets(combat, self, other), is(false));
        assertThat(and(falseReq(), falseReq()).meets(combat, self, other), is(false));
        assertThat(and(trueReq(), trueReq(), trueReq()).meets(combat, self, other), is(true));
        assertThat(and(trueReq(), trueReq(), falseReq()).meets(combat, self, other), is(false));
        assertThat(and(trueReq(), falseReq(), trueReq()).meets(combat, self, other), is(false));
        assertThat(and(trueReq(), falseReq(), falseReq()).meets(combat, self, other), is(false));
        assertThat(and(falseReq(), trueReq(), trueReq()).meets(combat, self, other), is(false));
        assertThat(and(falseReq(), trueReq(), falseReq()).meets(combat, self, other), is(false));
        assertThat(and(falseReq(), falseReq(), trueReq()).meets(combat, self, other), is(false));
        assertThat(and(falseReq(), falseReq(), falseReq()).meets(combat, self, other), is(false));
    }

    @Test public void attributeTest() throws Exception {
        self.att.put(Attribute.Seduction, 20);
        other.att.put(Attribute.Seduction, 18);
        AttributeRequirement req = attribute(Attribute.Seduction, 19);
        assertThat(req.meets(combat, self, other), is(true));
        assertThat(req.meets(combat, other, self), is(false));
        other.att.put(Attribute.Seduction, 19);
        assertThat(req.meets(combat, other, self), is(true));
    }

    @Test public void bodypartTest() throws Exception {
        self.body.addReplace(PussyPart.generic, 1);
        other.body.addReplace(PussyPart.generic.applyMod(FieryMod.INSTANCE), 1);
        assertThat(bodypart("pussy").meets(combat, self, other), is(true));
        assertThat(bodypart("pussy").meets(combat, other, self), is(true));
        other.body.removeAll("pussy");
        assertThat(bodypart("pussy").meets(combat, other, self), is(false));
    }

    @Test public void domTest() throws Exception {
        combat.setStance(new HeldOral(self, other));
        assertThat(dom().meets(combat, self, other), is(true));
        assertThat(dom().meets(combat, other, self), is(false));
    }

    @Test public void durationTest() throws Exception {
        DurationRequirement duration = duration(5);
        assertThat(duration.meets(combat, self, other), is(true));
        duration.tick(4);
        assertThat(duration.meets(combat, self, other), is(true));
        duration.tick(1);
        assertThat(duration.meets(combat, self, other), is(false));
        duration.reset(-5);
        assertThat(duration.remaining() >= 0, is(true));
        assertThat(duration.meets(combat, self, other), is(false));
        duration.reset(10);
        assertThat(duration.meets(combat, self, other), is(true));
    }

    @Test public void insertedTest() throws Exception {
        self.body.addReplace(new CockPart().applyMod(new SizeMod(SizeMod.COCK_SIZE_HUGE)), 1);
        combat.setStance(new FlyingCarry(self, other));
        assertThat(inserted().meets(combat, self, other), is(true));
        assertThat(inserted().meets(combat, other, self), is(false));
        assertThat(eitherinserted().meets(combat, self, other), is(true));
        assertThat(eitherinserted().meets(combat, other, self), is(true));
        combat.setStance(new Neutral(self, other));
        assertThat(eitherinserted().meets(combat, self, other), is(false));
        assertThat(eitherinserted().meets(combat, other, self), is(false));
    }

    @Test public void itemTest() throws Exception {
        self.gain(Item.Beer, 6);
        other.gain(Item.Beer, 1);
        ItemRequirement sixpack = item(Item.Beer.amount(6));
        assertThat(sixpack.meets(combat, self, other), is(true));
        assertThat(sixpack.meets(combat, other, self), is(false));
        assertThat(sixpack.meets(null, self, null), is(true));
        self.consume(Item.Beer, 1);
        assertThat(sixpack.meets(combat, self, other), is(false));
        assertThat(sixpack.meets(null, self, null), is(false));
    }

    @Test public void levelTest() throws Exception {
        LevelRequirement sophomore = level(2);
        assertThat(sophomore.meets(combat, self, other), is(false));
        assertThat(sophomore.meets(combat, other, self), is(false));
        self.ding(null);
        assertThat(sophomore.meets(combat, self, other), is(true));
        assertThat(sophomore.meets(combat, other, self), is(false));
    }

    @Test public void moodTest() throws Exception {
        MoodRequirement inTheMood = mood(Emotion.horny);
        self.mood = Emotion.horny;
        other.mood = Emotion.nervous;
        assertThat(inTheMood.meets(combat, self, other), is(true));
        assertThat(inTheMood.meets(combat, other, self), is(false));
        self.emote(Emotion.dominant, 100);
        self.moodSwing(combat);
        other.emote(Emotion.horny, 100);
        other.moodSwing(combat);
        assertThat(inTheMood.meets(combat, self, other), is(false));
        assertThat(inTheMood.meets(combat, other, self), is(true));
    }

    @Test public void noneTest() throws Exception {
        assertThat(none().meets(combat, self, other), is(true));
        assertThat(none().meets(combat, other, self), is(true));
    }

    @Test public void notTest() throws Exception {
        assertThat(not(trueReq()).meets(combat, self, other), is(false));
        assertThat(not(trueReq()).meets(combat, other, self), is(false));
        assertThat(not(falseReq()).meets(combat, self, other), is(true));
        assertThat(not(falseReq()).meets(combat, other, self), is(true));
    }

    @Test public void orgasmTest() throws Exception {
        assertThat(orgasms(1).meets(combat, self, other), is(false));
        assertThat(orgasms(1).meets(combat, other, self), is(false));
        self.doOrgasm(combat, other, self.body.getRandomPussy(), other.body.get("hands").get(0));
        assertThat(orgasms(1).meets(combat, self, other), is(true));
        assertThat(orgasms(1).meets(combat, other, self), is(false));
    }

    @Test public void orTest() throws Exception {
        // truth table tests
        assertThat(or().meets(combat, self, other), is(false));
        assertThat(or(trueReq()).meets(combat, self, other), is(true));
        assertThat(or(falseReq()).meets(combat, self, other), is(false));
        assertThat(or(trueReq(), trueReq()).meets(combat, self, other), is(true));
        assertThat(or(trueReq(), falseReq()).meets(combat, self, other), is(true));
        assertThat(or(falseReq(), trueReq()).meets(combat, self, other), is(true));
        assertThat(or(falseReq(), falseReq()).meets(combat, self, other), is(false));
        assertThat(or(trueReq(), trueReq(), trueReq()).meets(combat, self, other), is(true));
        assertThat(or(trueReq(), trueReq(), falseReq()).meets(combat, self, other), is(true));
        assertThat(or(trueReq(), falseReq(), trueReq()).meets(combat, self, other), is(true));
        assertThat(or(trueReq(), falseReq(), falseReq()).meets(combat, self, other), is(true));
        assertThat(or(falseReq(), trueReq(), trueReq()).meets(combat, self, other), is(true));
        assertThat(or(falseReq(), trueReq(), falseReq()).meets(combat, self, other), is(true));
        assertThat(or(falseReq(), falseReq(), trueReq()).meets(combat, self, other), is(true));
        assertThat(or(falseReq(), falseReq(), falseReq()).meets(combat, self, other), is(false));
    }

    @Test public void positionTest() throws Exception {
        self.body.addReplace(new CockPart(), 1);
        PositionRequirement flyfuck = position("FlyingCarry");
        combat.setStance(new FlyingCarry(self, other));
        assertThat(flyfuck.meets(combat, self, other), is(true));
        assertThat(flyfuck.meets(combat, other, self), is(true));
        assertThat(position("flying").meets(combat, self, other), is(false));
        combat.setStance(new Neutral(self, other));
        assertThat(flyfuck.meets(combat, self, other), is(false));
        assertThat(flyfuck.meets(combat, other, self), is(false));
    }

    @Test public void proneTest() throws Exception {
        assertThat(prone().meets(combat, self, other), is(false));
        assertThat(prone().meets(combat, other, self), is(false));
        combat.setStance(new FaceSitting(self, other));
        assertThat(prone().meets(combat, self, other), is(false));
        assertThat(prone().meets(combat, other, self), is(true));
    }

    @Test public void randomTest() throws Exception {
        assertThat(random(0f).meets(combat, self, other), is(false));
        assertThat(random(1f).meets(combat, self, other), is(true));
        assertThat(random(0.5f).meets(combat, self, other), notNullValue());
    }

    @Test public void resultTest() throws Exception {
        ResultRequirement strappedOn = result(Result.strapon);
        assertThat(strappedOn.meets(combat, self, other), is(false));
        assertThat(strappedOn.meets(combat, other, self), is(false));
        combat.state = Result.strapon;
        assertThat(strappedOn.meets(combat, other, self), is(true));
        assertThat(strappedOn.meets(combat, self, other), is(true));
    }

    @Test public void reverseTest() throws Exception {
        combat.setStance(new Anal(other, self));
        assertThat(rev(anal()).meets(combat, self, other), is(false));
        assertThat(rev(anal()).meets(combat, other, self), is(true));
    }

    @Test public void specificBodyPartTest() throws Exception {
        self.body.addReplace(PussyPart.generic, 1);
        other.body.addReplace(PussyPart.generic.applyMod(FieryMod.INSTANCE), 1);
        SpecificBodyPartRequirement fierypussy = specificpart(PussyPart.generic.applyMod(FieryMod.INSTANCE));
        assertThat(fierypussy.meets(combat, self, other), is(false));
        assertThat(fierypussy.meets(combat, other, self), is(true));
        other.body.removeAll("pussy");
        assertThat(fierypussy.meets(combat, other, self), is(false));
    }

    @Test public void statusTest() throws Exception {
        StatusRequirement caffeinated = status(Stsflag.alert);
        self.add(combat, new Alert(self));
        assertThat(caffeinated.meets(combat, self, other), is(true));
        assertThat(caffeinated.meets(combat, other, self), is(false));
    }

    @Test public void subTest() throws Exception {
        combat.setStance(new HeldOral(self, other));
        assertThat(sub().meets(combat, self, other), is(false));
        assertThat(sub().meets(combat, other, self), is(true));
    }

    @Test public void traitTest() throws Exception {
        self.add(Trait.alwaysready);
        TraitRequirement dtf = trait(Trait.alwaysready);
        assertThat(dtf.meets(combat, self, other), is(true));
        assertThat(dtf.meets(combat, other, self), is(false));
    }

    @Test public void winningTest() throws Exception {
        assertThat(winning().meets(combat, self, other), is(true));
        assertThat(winning().meets(combat, other, self), is(true));

        self.getWillpower().setTemporaryMax(50);
        self.getWillpower().set(25);
        assertThat(winning().meets(combat, self, other), is(false));
        assertThat(winning().meets(combat, other, self), is(true));

        other.getWillpower().setTemporaryMax(1000);
        other.getWillpower().set(100);
        assertThat(winning().meets(combat, self, other), is(true));
        assertThat(winning().meets(combat, other, self), is(false));
    }
}
