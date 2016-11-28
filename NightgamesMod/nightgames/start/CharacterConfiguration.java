package nightgames.start;

import static nightgames.start.ConfigurationUtils.mergeOptionals;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.CharacterSex;
import nightgames.characters.Growth;
import nightgames.characters.Trait;
import nightgames.global.Flag;
import nightgames.global.Global;
import nightgames.items.clothing.Clothing;
import nightgames.json.JsonUtils;

public abstract class CharacterConfiguration {

    protected Optional<String> name;
    protected Optional<CharacterSex> gender;
    protected Map<Attribute, Integer> attributes;
    protected Optional<Integer> money;
    protected Optional<Integer> level;
    protected Optional<Integer> xp;
    protected Optional<Collection<Trait>> traits;
    protected Optional<BodyConfiguration> body;
    protected Optional<Collection<String>> clothing;
    protected Map<String,Float> growth;

    public CharacterConfiguration() {
        name = Optional.empty();
        gender = Optional.empty();
        attributes = new HashMap<>();
        money = Optional.empty();
        level = Optional.empty();
        xp = Optional.empty();
        traits = Optional.empty();
        body = Optional.empty();
        clothing = Optional.empty();
        growth = new HashMap<>();
    }

    /**
     * Merges the fields of two CharacterConfigurations into the a new CharacterConfiguration.
     *
     * @param primaryConfig   The primary configuration.
     * @param secondaryConfig The secondary configuration. Field values will be overridden by values in primaryConfig. return
     */
    protected CharacterConfiguration(CharacterConfiguration primaryConfig, CharacterConfiguration secondaryConfig) {
        this();
        name = mergeOptionals(primaryConfig.name, secondaryConfig.name);
        gender = mergeOptionals(primaryConfig.gender, secondaryConfig.gender);
        attributes.putAll(secondaryConfig.attributes);
        attributes.putAll(primaryConfig.attributes);
        money = mergeOptionals(primaryConfig.money, secondaryConfig.money);
        level = mergeOptionals(primaryConfig.level, secondaryConfig.level);
        xp = mergeOptionals(primaryConfig.xp, secondaryConfig.xp);
        clothing = mergeOptionals(primaryConfig.clothing, secondaryConfig.clothing);
        traits = mergeOptionals(primaryConfig.traits, secondaryConfig.traits);
        growth.putAll(secondaryConfig.growth);
        growth.putAll(primaryConfig.growth);
        if (primaryConfig.body.isPresent()) {
            if (secondaryConfig.body.isPresent()) {
                body = Optional.of(new BodyConfiguration(primaryConfig.body.get(), secondaryConfig.body.get()));
            } else {
                body = primaryConfig.body;
            }
        } else {
            body = secondaryConfig.body;
        }
    }

    private static final Field[] GROWTH_FIELDS = Growth.class.getFields();
    private static final List<String> GROWTH_FIELDS_NAMES=Stream.of(GROWTH_FIELDS).map(Field::getName).collect(Collectors.toList());
    
    protected final void apply(Character base) {
        name.ifPresent(n -> base.name = n);
        base.att.putAll(attributes);
        money.ifPresent(m -> base.money = m);
        Growth bg=base.getGrowth();
        for (String key : growth.keySet()) {
            if (GROWTH_FIELDS_NAMES.contains(key)) {
                try {GROWTH_FIELDS[GROWTH_FIELDS_NAMES.indexOf(key)].set(bg,growth.get(key));}
                catch (IllegalArgumentException | IllegalAccessException e) {e.printStackTrace();}
            } //This is a terrible way to do this, but the below is the only alternative without rewriting everything using Growth, and that might be even more terrible.
            //It actually works though, heh.
            
//            if (key=="arousal") bg.arousal=growth.get("arousal");
//            if (key=="stamina") bg.stamina=growth.get("stamina");
//            if (key=="mojo") bg.mojo=growth.get("mojo");
//            if (key=="willpower") bg.willpower=growth.get("willpower");

        }
        level.ifPresent(l -> {
            base.level = l;
            modMeters(base, l * 2); // multiplication to compensate for missed daytime gains
        });
        xp.ifPresent(x -> base.gainXP(x));
        traits.ifPresent(t -> base.traits = new CopyOnWriteArrayList<>(t));
        if (clothing.isPresent()) {
            List<Clothing> clothes = clothing.get().stream().map(Clothing::getByID).collect(Collectors.toList());
            base.outfitPlan = new ArrayList<>(clothes);
            base.closet = new HashSet<>(clothes);
            base.change();
        }
        body.ifPresent(b -> b.apply(base.body));
        base.levelUpIfPossible();
    }

    /**
     * Parses fields common to PlayerConfiguration and NpcConfigurations.
     *
     * @param object The configuration read from the JSON config file.
     */
    protected void parseCommon(JsonObject object) {
        name = JsonUtils.getOptional(object, "name").map(JsonElement::getAsString);
        gender = JsonUtils.getOptional(object, "gender").map(JsonElement::getAsString).map(String::toLowerCase)
                        .map(CharacterSex::valueOf);
        traits = JsonUtils.getOptionalArray(object, "traits")
                        .map(array -> JsonUtils.collectionFromJson(array, Trait.class));
        body = JsonUtils.getOptionalObject(object, "body").map(BodyConfiguration::parse);
        clothing = JsonUtils.getOptionalArray(object, "clothing").map(JsonUtils::stringsFromJson);
        money = JsonUtils.getOptional(object, "money").map(JsonElement::getAsInt);
        level = JsonUtils.getOptional(object, "level").map(JsonElement::getAsInt);
        xp = JsonUtils.getOptional(object, "xp").map(JsonElement::getAsInt);
        attributes = JsonUtils.getOptionalObject(object, "attributes")
                        .map(obj -> JsonUtils.mapFromJson(obj, Attribute.class, Integer.class)).orElse(new HashMap<>());
        growth = JsonUtils.getOptionalObject(object, "growth")
                        .map(obj -> JsonUtils.mapFromJson(obj, String.class, Float.class)).orElse(new HashMap<>());
    }

    private static void modMeters(Character character, int levels) {
        Growth growth = character.getGrowth();
        boolean hard = Global.checkFlag(Flag.hardmode);
        for (int i = 0; i < levels; i++) {
            character.getStamina().gain(growth.stamina);
            character.getArousal().gain(growth.arousal);
            character.getWillpower().gain(growth.willpower);
            if (hard) {
                character.getStamina().gain(growth.bonusStamina);
                character.getArousal().gain(growth.bonusArousal);
                character.getWillpower().gain(growth.bonusWillpower);
            }
        }
    }
    
    @Override public String toString() {
        return "CharacterConfiguration with name "+name+" gender "+gender+" attributes "+attributes+" money "+money+" level "+level+" traits "+traits+" XP "+xp+" body "+body+" clothing "+clothing+" growth "+growth;
    }
}
