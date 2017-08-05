package nightgames.start;

import nightgames.characters.Attribute;
import nightgames.characters.CharacterSex;
import nightgames.json.JsonUtils;
import nightgames.items.clothing.Clothing;
import org.hamcrest.collection.IsMapContaining;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import nightgames.characters.TestAngel;
import nightgames.global.Global;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

/**
 *
 */
public class NpcConfigurationTest {
    StartConfiguration startConfig;
    NpcConfiguration angelConfig;

    @BeforeClass
    public static void setUpNpcConfigurationTest() {
        Clothing.buildClothingTable();
    }

    @Before public void setUp() throws Exception {
        Path file = new File("NightgamesTests/nightgames/start/TestStartConfig.json").toPath();
        startConfig = StartConfiguration.parse(JsonUtils.rootJson(file).getAsJsonObject());
        angelConfig = startConfig.findNpcConfig("TestAngel")
                        .orElseThrow(() -> new NoSuchElementException("TestAngel not found in test config."));
        new Global();
        Global.newGame("Dummy", Optional.empty(), Collections.emptyList(), CharacterSex.asexual, Collections.emptyMap());
    }

    @Test public void testConfigMerge() throws Exception {
        NpcConfiguration mergedConfig = new NpcConfiguration(angelConfig, startConfig.npcCommon);
        assertThat(mergedConfig.type, equalTo("TestAngel"));
        assertThat(mergedConfig.gender, is(Optional.empty()));
        assertThat(mergedConfig.attributes, allOf(IsMapContaining.hasEntry(Attribute.Power, 13),
                        IsMapContaining.hasEntry(Attribute.Seduction, 20),
                        IsMapContaining.hasEntry(Attribute.Cunning, 15),
                        IsMapContaining.hasEntry(Attribute.Divinity, 10),
                        IsMapContaining.hasEntry(Attribute.Arcane, 2)));
        assertThat(mergedConfig.body.map(body -> body.type).orElse(Optional.empty()),
                        equalTo(Optional.of(BodyConfiguration.Archetype.ANGEL)));
        assertThat(mergedConfig.xp.orElse(0), equalTo(5));
        assertThat(mergedConfig.level.orElse(0), equalTo(5));
        assertThat(mergedConfig.money.orElse(0), equalTo(5000));
    }

    @Test public void testNpcCreation() throws Exception {
        TestAngel angel = new TestAngel(Optional.of(angelConfig), Optional.of(startConfig.npcCommon));
        assertThat(angel.character.getType(), equalTo("TestAngel"));
        
        // These can't be exact matches, as the NPCs will level themselves up on creation
        assertThat(angel.character.att, allOf(Arrays.asList(
                        IsMapContaining.hasEntry(equalTo(Attribute.Power), greaterThanOrEqualTo(13)),
                        IsMapContaining.hasEntry(equalTo(Attribute.Seduction), greaterThanOrEqualTo(20)),
                        IsMapContaining.hasEntry(equalTo(Attribute.Cunning), greaterThanOrEqualTo(15)),
                        IsMapContaining.hasEntry(equalTo(Attribute.Divinity), greaterThanOrEqualTo(10)),
                        IsMapContaining.hasEntry(equalTo(Attribute.Arcane), greaterThanOrEqualTo(2)),
                        IsMapContaining.hasEntry(equalTo(Attribute.Perception), greaterThanOrEqualTo(6)),
                        IsMapContaining.hasEntry(equalTo(Attribute.Speed), greaterThanOrEqualTo(5)))));
        assertThat(angel.character.xp, equalTo(5));
        assertThat(angel.character.level, equalTo(5));
        assertThat(angel.character.money, equalTo(5000));
    }

    @Test public void testBodyMerge() throws Exception {
        TestAngel angel = new TestAngel(Optional.of(angelConfig), Optional.of(startConfig.npcCommon));

        // Starting stats should be at least as large as config (NPCs level up and distribute points on creation)
        // but breasts should be the same as base Angel if not overwritten in config.
        assertThat(angel.character.get(Attribute.Seduction), greaterThan(angelConfig.attributes.get(Attribute.Seduction)));
        assertThat(angel.character.body.getLargestBreasts(),
                        equalTo(TestAngel.baseTestAngelChar.body.getLargestBreasts()));
        assertEquals(TestAngel.baseTestAngelChar.body.getLargestBreasts(),
                        angel.getCharacter().body.getLargestBreasts());
    }
    
    @Test public void testGenderChange() throws Exception {
        angelConfig.gender = Optional.of(CharacterSex.male);
        TestAngel angel = new TestAngel(Optional.of(angelConfig), Optional.of(startConfig.npcCommon));

        assertFalse(angel.character.body.has("pussy"));
        assertTrue(angel.character.body.has("cock"));
        // Changing gender should not change (e.g.) breast size.
        assertThat(angel.character.body.getLargestBreasts(),
                        equalTo(TestAngel.baseTestAngelChar.body.getLargestBreasts()));
    }

    @Test public void testClothing() throws Exception {
        NpcConfiguration mergedConfig = new NpcConfiguration(angelConfig, startConfig.npcCommon);
        TestAngel angel = new TestAngel(Optional.of(angelConfig), Optional.of(startConfig.npcCommon));
        Clothing[] expectedClothing =
                        mergedConfig.clothing.orElseThrow(() -> new AssertionError("Merged npc clothing config has no"))
                                        .stream().map(Clothing::getByID).toArray(Clothing[]::new);
        assertThat(angel.character.outfit.getEquipped(), hasItems(expectedClothing));
    }
}
