package nightgames.requirement;

import nightgames.characters.Attribute;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Result;
import nightgames.items.ItemAmount;

import java.util.Arrays;

/**
 * Shortcut functions for requirement creation
 */
public class RequirementShortcuts {
    public static AnalRequirement anal() {
        return new AnalRequirement();
    }

    public static AndRequirement and(Requirement... subReqs) {
        return new AndRequirement(Arrays.asList(subReqs));
    }

    public static AttributeRequirement attribute(Attribute att, int amount) {
        return new AttributeRequirement(att, amount);
    }

    public static BodyPartRequirement bodypart(String type) {
        return new BodyPartRequirement(type);
    }

    public static DomRequirement dom() {
        return new DomRequirement();
    }

    public static DurationRequirement duration(int duration) {
        return new DurationRequirement(duration);
    }

    public static OrRequirement eitherinserted() {
        return or(inserted(), rev(inserted()));
    }

    public static InsertedRequirement inserted() {
        return new InsertedRequirement();
    }

    public static ItemRequirement item(ItemAmount item) {
        return new ItemRequirement(item);
    }

    public static LevelRequirement level(int level) {
        return new LevelRequirement(level);
    }

    public static MoodRequirement mood(Emotion mood) {
        return new MoodRequirement(mood);
    }

    public static NoneRequirement none() {
        return new NoneRequirement();
    }

    public static NotRequirement not(Requirement subReq) {
        return new NotRequirement(subReq);
    }

    public static OrgasmRequirement orgasm(int count) {
        return new OrgasmRequirement(count);
    }

    public static OrRequirement or(Requirement... subReqs) {
        return new OrRequirement(Arrays.asList(subReqs));
    }

    public static ProneRequirement prone() {
        return new ProneRequirement();
    }

    public static RandomRequirement random(float threshold) {
        return new RandomRequirement(threshold);
    }

    public static ResultRequirement result(Result result) {
        return new ResultRequirement(result);
    }

    public static ReverseRequirement rev(Requirement subReq) {
        return new ReverseRequirement(subReq);
    }

    public static SpecificBodyPartRequirement specificpart(BodyPart part) {
        return new SpecificBodyPartRequirement(part);
    }

    public static StanceRequirement stance(String stance) {
        return new StanceRequirement(stance);
    }

    public static StatusRequirement status(String status) {
        return new StatusRequirement(status);
    }

    public static SubRequirement sub() {
        return new SubRequirement();
    }

    public static TraitRequirement trait(Trait trait) {
        return new TraitRequirement(trait);
    }

    public static WinningRequirement winning() {
        return new WinningRequirement();
    }
}
