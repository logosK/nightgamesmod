package nightgames.characters;

import java.util.Arrays;
import java.util.Optional;

import nightgames.characters.body.BreastsPart;
import nightgames.characters.body.CockMod;
import nightgames.characters.custom.CharacterLine;
import nightgames.combat.Combat;
import nightgames.combat.CombatScene;
import nightgames.combat.CombatSceneChoice;
import nightgames.combat.Result;
import nightgames.global.Flag;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.items.clothing.Clothing;
import nightgames.start.NpcConfiguration;
import nightgames.status.Drowsy;
import nightgames.status.Energized;

public class Maya extends BasePersonality {

    private static final long serialVersionUID = 447375506153223682L;

    /* FIXME: Maya's image maya_dom is not named correctly. It should be maya_dominant
     * 
     * */
    
    public static final String MAYA_FIRSTTYPE1_FOCUS = "MayaFirst1Focus";
    public static final String MAYA_FIRSTTYPE2_FOCUS = "MayaFirst2Focus";
    public static final String MAYA_FIRSTTYPE_DONE = "MayaFirstFocusDone";
    public static final String MAYA_SECONDTYPE1_FOCUS = "MayaSecond1Focus";
    public static final String MAYA_SECONDTYPE2_FOCUS = "MayaSecond2Focus";
    public static final String MAYA_SECONDTYPE_DONE = "MayaSecondFocusDone";
    
    public Maya(int playerLevel) {
        this(playerLevel, Optional.empty(), Optional.empty());
    }

    public Maya(int playerLevel, Optional<NpcConfiguration> charConfig, Optional<NpcConfiguration> commonConfig) {
        super("Maya", charConfig, commonConfig, false);
        while (character.getLevel() < playerLevel + 20) {
            character.ding(null);
        }
        constructLines();
    }

    @Override
    public void applyStrategy(NPC self) {
        self.plan = Plan.hunting;
        self.mood = Emotion.confident;
    }

    @Override
    public void applyBasicStats(Character self) {
        self.outfitPlan.add(Clothing.getByID("camisole"));
        self.outfitPlan.add(Clothing.getByID("blouse"));
        self.outfitPlan.add(Clothing.getByID("lacepanties"));
        self.outfitPlan.add(Clothing.getByID("skirt"));
        self.outfitPlan.add(Clothing.getByID("sneakers"));
        self.outfitPlan.add(Clothing.getByID("socks"));
        self.change();
        self.modAttributeDontSaveData(Attribute.Dark, 10);
        self.modAttributeDontSaveData(Attribute.Seduction, 15);
        self.modAttributeDontSaveData(Attribute.Cunning, 15);
        self.modAttributeDontSaveData(Attribute.Speed, 2);
        self.modAttributeDontSaveData(Attribute.Power, 7);
        self.modAttributeDontSaveData(Attribute.Hypnosis, 5);
        self.getStamina().setMax(90);
        self.getArousal().setMax(150);
        self.getMojo().setMax(150);
        self.getWillpower().setMax(100);

        Global.gainSkills(self);
        self.setTrophy(Item.MayaTrophy);

        self.body.add(BreastsPart.d);
        self.initialGender = CharacterSex.female;
        preferredCockMod = CockMod.error;
    }

    @Override
    public void setGrowth() {
        character.getGrowth().stamina = 2;
        character.getGrowth().arousal = 10;
        character.getGrowth().willpower = 2.0f;
        character.getGrowth().bonusStamina = 2;
        character.getGrowth().bonusArousal = 5;

        this.addFirstFocusScene();     
        this.addSecondFocusScene();   

        character.getGrowth().addTrait(0, Trait.darkpromises);
        character.getGrowth().addTrait(0, Trait.tongueTraining1);
        character.getGrowth().addTrait(0, Trait.tongueTraining2);
        character.getGrowth().addTrait(0, Trait.limbTraining1);
        character.getGrowth().addTrait(0, Trait.limbTraining2);
        character.getGrowth().addTrait(0, Trait.sexTraining1);
        character.getGrowth().addTrait(0, Trait.sexTraining2);
        character.getGrowth().addTrait(0, Trait.sexTraining3);
        character.getGrowth().addTrait(0, Trait.analTraining1);
        character.getGrowth().addTrait(0, Trait.analTraining2);
        character.getGrowth().addTrait(0, Trait.Confident);
        character.getGrowth().addTrait(0, Trait.dickhandler);
        character.getGrowth().addTrait(0, Trait.tight);
        character.getGrowth().addTrait(0, Trait.insertion);
        character.getGrowth().addTrait(0, Trait.holecontrol);
        character.getGrowth().addTrait(0, Trait.autonomousPussy);
        character.getGrowth().addTrait(0, Trait.experienced);
        character.getGrowth().addTrait(0, Trait.responsive);
        character.getGrowth().addTrait(0, Trait.powerfulhips);

        character.getGrowth().addTrait(0, Trait.enchantingVoice);
        character.getGrowth().addTrait(0, Trait.unnaturalgrowth);
        character.getGrowth().addTrait(0, Trait.event);
        character.getGrowth().addTrait(0, Trait.cursed);
    }

    private void constructLines() {
        character.addLine(CharacterLine.BB_LINER, (c, self, other) -> {
            return "Maya looks at you sympathetically. <i>\"Was that painful? Don't worry, you aren't seriously injured. Our Benefactor protects us.\"</i>";
        });

        character.addLine(CharacterLine.CHALLENGE, (c, self, other) -> {
            if (other.human()) {
                return "Maya smiles softly as she confidently steps toward you. <i>\"Are you simply unfortunate or were you actually hoping to challenge me? What a brave "
                                + other.boyOrGirl() + ". I'll try not to disappoint you.\"</i>";
            } else {
                return "{self:SUBJECT} smiles softly as she confidently steps towards {other:name-do}.";
            }
        });

        character.addLine(CharacterLine.NAKED_LINER, (c, self, other) -> {
            return "Maya smiles, unashamed of her nudity. <i>\"Well done. Not many participants are able to get my clothes off anymore. You'll at least be able to look at a naked woman while you orgasm.\"</i>";
        });

        character.addLine(CharacterLine.STUNNED_LINER, (c, self, other) -> {
            return "You think you see something dangerous flicker in Maya's eyes. <i>\"Well done. I may need to get a little serious.\"</i>";
        });

        character.addLine(CharacterLine.TAUNT_LINER, (c, self, other) -> {
            return "Maya gives you a look of gentle disapproval. <i>\"You aren't putting up much of a fight, are you? Aren't you a little overeager to cum?\"</i>";
        });
        character.addLine(CharacterLine.NIGHT_LINER, (c, self, other) -> {
            //TODO: Maya might someday be able to play regularly, so this line will need multiple versions once that happens. For now, she plays monthly and is out of the player's league as an alumni. - DSM
            return "Maya approaches you after the night is over and puts her hand on your shoulder to get your attention. She sighs in a satisfied manner and gives you a subtle smile and a farewell. "
                            + "\"<i>Well, I'll see you next month. You're doing okay. Keep it up.</i>\" Maya fixes her glasses and puts out her hand for a handshake. You awkwardly shake her hand and she departs. "
                            + "You're left wondering what the hell that was about.";
        });

        character.addLine(CharacterLine.TEMPT_LINER, (c, self, other) -> {
            return "Maya lowers her voice to a smokey tone as she speaks. <i>\"Shall"
                            + " I show you what experience can do for a sexfighter?\"</i>";
        });

        character.addLine(CharacterLine.ORGASM_LINER, (c, self, other) -> {
            return "<i>\"Oh.. SHIT! Did I just actually... Fuck! Come here and let me return the favor, stud!\"</i>";
        });

        
        character.addLine(CharacterLine.LEVEL_DRAIN_LINER, (c, self, other) -> {
            String part = Global.pickRandom(c.getStance().getPartsFor(c, self, other)).map(bp -> bp.describe(self)).orElse("pussy");
            if (other.getLevel() < self.getLevel() - 5) {
                return "\"<i>Oh yeah! I can feel it! Go ahead and cum, stud! Cuuummmmmm!</i>\" "
                                + "{self:SUBJECT} closes her eyes while giving you a hard squeeze with {self:possessive} " + part + " - setting you off. You feel the desperate and alarming sensation of your strength entering {self:possessive} as you hopelessly climax beneath {self:possessive}. "
                                + "You attempt to move away, but {self:subject} holds you down effortlessly while riding you - they are definitely more powerful than you, now. You can't escape even if you tried. "
                                + "\"<i>Oh, no {other:NAME}, you're not going anywhere! Taking your power, strength, and even memories while you cum yourself into weakness <b>really</b> scratches my itch...I think I understand why succubi enjoy themselves so much! Want give me more?</i>\"";
            } else if (other.getLevel() >= self.getLevel()) {
                return "{self:SUBJECT}'s eyes narrow while savoring the feeling of robbing you of your hard-earned power into {self:possessive} " + part + " as you hopelessly cum. "
                                + "<i>\"Ohhhh~ I'm not sure how you got stronger than me in the first place, {other:boy}, but feel free to try again - sucking it out of you feels <b>really</b> good.\"</i>";
            } else {
                return "\"<i>Ha ha ha! You know, what, {other:NAME}? I don't know why you thought you'd take on a graduate, but I can't help but enjoy keeping you in your place.</i>\"";
            }
        });

        
        character.addLine(CharacterLine.MAKE_ORGASM_LINER, (c, self, other) -> {
            return "<i>\"Aaaand there we are. Think you can go again?\"</i>";
        });
        character.addLine(CharacterLine.DESCRIBE_LINER, (c, self, other) -> {
            return "Maya is a beautiful young woman in her early twenties, though she carries"
                            + " herself with the grace of a more mature lady. She has soft, shapely "
                            + "breasts, larger than her slim frame would imply. Her waist length, raven "
                            + "black hair is tied in a single braid down her back. She wears elbow-length"
                            + " silk gloves, giving the appearance of sensual elegance. Her eyes are a "
                            + "beautiful dark blue, behind her red-framed glasses, but every so often the "
                            + "light catches them in a way that makes you think there might "
                            + "be something dangerous inside.";
        });
        
        
        character.addLine(CharacterLine.LEVEL_DRAIN_LINER, (c, self, other) -> {
            String part = Global.pickRandom(c.getStance().getPartsFor(c, self, other)).map(bp -> bp.describe(self)).orElse("pussy");
            if (other.getLevel() < self.getLevel() - 5) {
                if (c.getStance().vaginallyPenetratedBy(c, other, self)) {
                    return "Maya works you with a wide smile on her face while you cum uncontrollably into her pussy; your experience and training leaving you yet again while she softly chuckles at you. "
                                    + "\"<i>Oh, {other:name}...You're such a small fry at this point that it's just too easy, but taking your strength and experience really scratches my itch!</i>\"";
                } else if (c.getStance().anallyPenetratedBy(c, other, self)) {
                    return "Maya works you with a wide smile on her face while you cum uncontrollably into her nightmarish backdoor; you feel it drawing you experience and training right out of you while she softly chuckles at you. "
                                    + "\"<i>Oh, {other:name}...You're such a small fry at this point that it's just too easy, but taking your strength and experience really scratches my itch!</i>\"";
                } else {
                    return "Maya presses her demonic cock deep into you while you cum uncontrollably around it. You feel it drawing more of your experience and training out of your very soul while she softly chuckles at you. "
                                    + "\"<i>Oh, {other:name}...You're such a small fry at this point that it's just too easy, but taking your strength and experience really scratches my itch!</i>\"";
                }
            } else if (other.getLevel() >= self.getLevel()) {
                if (c.getStance().inserted(other)) {
                    return "Maya tenses up and yelps on top of you as your power and experience snap off and flow into her. She turns her face to your cumming visage and cracks a cruel and smug-looking smile before giving you three more good squeezes. "
                                    + "\"<i>You like giving it up to us, don't you? I knew you'd be a good addition. You can keep thanking me Just. Like. This!</i>\"";
                } else {
                    return "Maya tenses up and yelps on top of you as your power and experience snap off and flow into her. She turns her face to your cumming visage and cracks a cruel and smug-looking smile before giving you three more good squeezes. "
                                    + "\"<i>You like giving it up to us, don't you? I knew you'd be a good addition. You can keep thanking me Just. Like. This!</i>\"";
                }
            } else {
                return "Maya stiffens up in rapture as bits and pieces of your soul break off from you and are absorbed by her demonic " + part + " "
                                + "\"<i>Ohhh-yeah, {other:name}, that's good. Shakes the rust right off. Want to do that again, stud?</i>\"";
            }
        });
        
    }

    @Override
    public String victory(Combat c, Result flag) {
        Character target = c.getOpponent(character);
        target.add(c, new Drowsy(target));
        character.arousal.empty();
        character.add(c, new Energized(character, 10));
        return "Maya completely outmatches you. How were you suppose to deal with"
                        + " someone this skilled? Your cock spurts a sticky white flag in her hands "
                        + "as you cum. You slump down to the floor as you catch your breath. Maya "
                        + "smiles gently at you, but there's a predatory glint in her eyes. <i>\"Don't "
                        + "feel bad. You put up an impressive fight, given your inexperience.\"</i> "
                        + "She seems to be looking into your soul. You feel an almost animal instinct "
                        + "to look away, but can't bring yourself to break eye contact. <i>\"I hope "
                        + "you learned something, because the lesson is over now. It's time for me to "
                        + "take what I came for.\"</i><br/><br/>You wake up on the floor, naked and alone."
                        + " It doesn't seem like much time passed, but you feel lethargic and almost"
                        + " hung-over. What the hell happened to you?";
    }

    @Override
    public String defeat(Combat c, Result flag) {
        return "As you pleasure and caress Maya's body, she lets out a lovely gasp. You've "
                        + "finally got her on the ropes, and she knows it. Despite her experience, "
                        + "her body is still subject to her sexual needs. She grasps for your dick "
                        + "in desperation, but you manage to ward off her silk-gloved hand. Her body "
                        + "is rocked by a shudder of ecstasy and she lets out an orgasmic cry.<br/><br/>You"
                        + " did it! You actually managed to beat Maya! She sits on the floor, staring"
                        + " at you in shock as she recovers from her climax. <i>\"Oh my. A freshman "
                        + "actually made me orgasm. Either I was very careless, or you're no ordinary"
                        + " freshman.\"</i> She smiles approvingly. <i>\"Probably some of both. I'll "
                        + "see that Lilly gives you a nice bonus.\"</i> She gently pulls you to the "
                        + "floor next to her. <i>\"I think you also deserve a more immediate reward."
                        + "\"</i><br/><br/>She climbs on top of you and presses her soft boobs against your "
                        + "cock. The head of your penis pokes out of her cleavage and she covers it "
                        + "with her mouth. Oh God. This feels amazing. Her breasts thoroughly massage"
                        + " your shaft and her skilled tongue teases your sensitive glans. You writhe"
                        + " in exquisite pleasure. You won't last long against her technique, but you"
                        + " want to enjoy it as long as possible. She sucks hard on your dick and you"
                        + " can't resist cumming in her mouth.<br/><br/>She swallows your semen as gracefully"
                        + " as she can and smiles at you. <i>\"I'm pleased to see you do enjoy my "
                        + "efforts. I was worried I might be losing my touch.\"</i>";

    }

    @Override
    public String victory3p(Combat c, Character target, Character assist) {
        if (target.human()) {
            return "<i>\"Oh my. I don't think the " + assist.boyOrGirl() + " had much of a chance to start with,"
                            + " but now the other girls are picking on him too.\"</i> She looks over "
                            + "your helpless body and sides of her mouth curl slightly with amusement."
                            + " She delicately extends her stocking-covered foot and teases the tip "
                            + "of your penis.<br/><i>\"I think you'll enjoy this bullying, at least.\""
                            + "</i> She rubs and squeezes your sensitive glans with her toes. Your "
                            + "hips buck involuntarily as she keeps moving her foot to stimulate "
                            + "different spots on your shaft. How can she give you this kind of "
                            + "pleasure with just one foot? You knew she was experienced, but this"
                            + " is insane! She steps down on your dick, grinding it with the sole of "
                            + "her foot. You can't hold on any more. Your cock throbs and you shoot "
                            + "your seed onto her foot.";
        }
        if (assist.eligible(character)) {
            assist.defeated(character);
        }
        return "Maya looks a bit surprised as you restrain her opponent. <i>\"You're going to "
                        + "side with me? I appreciate the thought.\"</i> She kneels down in front of " + target.getName()
                        + " and kisses her softly while staring deeply into her eyes. "
                        + "She turns her attention toward you and leans close to whisper in your"
                        + " ear. <i>\"But I don't need any help\"</i> "
                        + "You don't notice her hand snake between your legs until she suddenly "
                        + "gives your balls a painful flick. The shock and surprise makes you lose " + "your grip on "
                        + target.getName() + ", who twists out of your arms and pins you to the floor. Suddenly you're"
                        + " the one being double-teamed. Her eyes have a certain glazed over "
                        + "appearance that tells you " + "she's under Maya's influence.<br/><br/>"
                        + "Maya casually moves between your legs, where she can easily reach your " + "dick and "
                        + target.getName() + "'s slit. You groan despite yourself as she" + " starts to stroke your shaft "
                        + "with both hands. Her mouth goes to work on the other girl's pussy. "
                        + "The two of you writhe in pleasure, completely at the mercy of the " + "veteran sexfighter. "
                        + target.getName() + " kisses you passionately as you both cum simultaneously.";

    }

    @Override
    public String intervene3p(Combat c, Character target, Character assist) {
        if (target.human()) {
            return "In the middle of your fight with " + assist.getName()
                            + ", you feel an overwhelming presence approach from behind. "
                            + "She apparently senses it too, because you both stop and turn "
                            + "to see Maya confidently walking towards you. She smiles and looks "
                            + "at the two of you. <i>\"Eenie... Meenie... Minie....\"</i> Her "
                            + "finger flicks in your direction. <i>\"Mo.\"</i><br/>"
                            + "You take a step back and prepare to defend yourself, but she's "
                            + "too fast. She reaches you in an instant and sweeps your feet"
                            + " out from under you, depositing you on the floor. She "
                            + "pins your arms helplessly under her legs and sits casually on "
                            + "your chest. She's lighter than she looks, but you're still "
                            + "completely unable to move her. <i>\"Sorry, it's just your "
                            + "bad luck. I could have helped you instead, but I don't do this v"
                            + "ery often anymore and I wanted to watch a cute " + target.boyOrGirl() + " orgasm. You "
                            + "should try to enjoy your defeat, it's just another " + "part of the Game.\"</i><br/>";
        }
        return "In the middle of your fight with " + target.getName()
                        + ", you feel an overwhelming presence approach from behind. She apparently "
                        + "senses it too, because you both stop and turn "
                        + "to see Maya confidently walking towards you. She smiles and looks at"
                        + " the two of you. <i>\"Eenie... Meenie... Minie....\"</i> She points toward " + target.getName()
                        + ". <i>\"Mo.\"</i><br/>" + "Before the girl can react, Maya is on her and "
                        + "kisses her passionately. " + target.getName()
                        + " struggles for just a moment before going limp. Maya moves to licking and " + "sucking "
                        + "her neck, while she eases the helpless girl to the floor. "
                        + "She glances back at you and you feel a chill run down your spine "
                        + "as her eyes seem to bore into you. <i>\"Are you "
                        + "going to make me do all the work? There's a lovely, naked girl in "
                        + "front of you who needs a good licking. Don't keep her waiting.</i><br/>";
    }

    @Override
    public String draw(Combat c, Result flag) {
        Character target = c.getOpponent(character);
        if (target.human()) {
            Global.flag(Flag.Clue1);
        }
        return "You've got her, you're sure of it. Maya let her guard down long enough "
                        + "for you to get her exactly where you want her. She falls to her hands"
                        + " and knees, her defenseless rear facing you. It's risky to attempt an "
                        + "insertion against someone of Maya's skill, but you can't afford to pass"
                        + " up any advantage you're given. Before she can regain her composure, you "
                        + "thrust your cock into her wet pussy. She lets out a yelp in surprise"
                        + " and pleasure, while you fuck her relentlessly. If she get free before"
                        + " you can make her cum, you're probably screwed, but you can feel that "
                        + "she's close to the end. Your cock warns you that you're approaching your"
                        + " limit and her dark aura is gradually eating away at your willpower. "
                        + "You need to end this right now! You reach down under her waist and quickly "
                        + "locate her love button. She moans in pleasure as you simultaneously "
                        + "stimulate her pussy and clit. It's working! She's already trembling on "
                        + "the verge of orgasm.<br/><br/>In a remarkable display of strength and desperation,"
                        + " she manages to wrap her legs around your hips, holding you completely "
                        + "inside her. You're caught completely off guard when her vaginal walls "
                        + "squeeze and milk your throbbing cock. She's cumming! If you can just hold"
                        + " out for a few more seconds, you'll win, but this sensation is just too "
                        + "much! You spurt hot jets of semen into Maya's spasming womb. It's a draw, "
                        + "snatched from the jaws of victory.<br/><br/>You both collapse on the floor, "
                        + "exhausted. You hear a lovely giggle, almost melodic, coming from Maya. Her"
                        + " giggles turn into unrestrained laughter as she rolls onto her back."
                        + " <i>\"A draw!\"</i> Her mature air of dignity is gone as she shakes with"
                        + " mirth. <i>\"Barely a draw! I had to struggle to achieve a mutual orgasm!"
                        + " Against a rookie, no less! God, I haven't felt this human in years!\""
                        + "</i> She sits up, more breathless from her laughter than the sex.<br/><br/><i>"
                        + "\"Sorry. It probably sounds like I'm disparaging you, but I thought there"
                        + " was only one man who could do that to me anymore.\"</i> She wears a "
                        + "delighted smile, neither dominant nor elegant. <i>\"I think I owe you "
                        + "quite a reward, but it appears your carnal desires are already sated. I "
                        + "know, I'll tell you a secret that Tyler would probably charge you a "
                        + "fortune for.\"</i><br/><br/>She looks at you intently, her eyes shining. "
                        + "<i>\"Let's start with a trivia question: in all the years these Games "
                        + "have been going on, how many competitors do you think have gotten "
                        + "seriously hurt?\"</i> This is a bit of an unfair question since you"
                        + " don't know how long the Games have been running. There can't have been "
                        + "too many, or it would have attracted attention. The matches are pretty "
                        + "rough and the physical combat seems to escalate over time. Probably 5-10?"
                        + "<br/><br/><i>\"Wrong. The answer is zero. No one has ever been injured in a match."
                        + " It's not just due to Lilly's hardwork, as diligent as she and her "
                        + "predecessors have been. It's because we're under our Benefactor's "
                        + "protection.\"</i> Her expression has shifted slightly as she talks. "
                        + "It now appears more like hero worship, along with something else "
                        + "you can't quite identify. <i>\"There's pain, of course. If we girls"
                        + " couldn't take advantage of male vulnerabilities, it would interfere "
                        + "with match balance. Yet, no matter how hard you get hit, you'll be back"
                        + " on your feet in no time, with no long term effects.\"</i><br/><br/>Her smile"
                        + " turns wistful as she stares off into space. Her voice drops almost to"
                        + " a whisper. <i>\"Our Benefactor is kind. Maybe too kind. He cannot bear"
                        + " to see any of his chosen hurt. That's why he couldn't let me die.\"</i>"
                        + " You suddenly realize what she reminds you of. She looks and sounds like"
                        + " a nun at prayer. She turns her attention back to you. <i>\"I think that"
                        + " was more than one secret. I could just erase that last bit from your memory,"
                        + " but I'll just call it a bonus.\"</i>";
    }

    @Override
    public boolean fightFlight(Character opponent) {
        return fit();
    }

    @Override
    public boolean attack(Character opponent) {
        return true;
    }

    @Override
    public boolean fit() {
        return !character.outfit.isNude();
    }

    @Override
    public boolean checkMood(Combat c, Emotion mood, int value) {
        switch (mood) {
            case horny:
                return value >= 50;
            case confident:
            case desperate:
                return value >= 200;
            default:
                break;
        }
        return value >= 100;
    }

    @Override
    public String resist3p(Combat c, Character assist, Character target) {
        if (assist.human()) {
            return "As you attempt to hold off Maya, you see " + target.getName()
                            + " stealthily approaching behind her. Looks like you're about to get "
                            + "some help. Maya suddenly "
                            + "winks at you and her foot darts up almost playfully between your legs."
                            + " She barely seemed to use any force, but the impact is still staggering. "
                            + "You crumple to the floor, holding your balls in debilitating pain.<br/><br/>" + target.getName()
                            + " hesitates, realizing she doesn't have the advantage of superior numbers. Maya is already moving, though, and pounces on the other girl "
                            + "before she can escape. From your position on the floor, you can't see exactly what is happening, but it's clear Maya is overwhelming her. As soon as the "
                            + "pain subsides, you force yourself back to your feet. You'd hoped to team up with "
                            + target.getName() + ", but she is already trembling in orgasm under Maya's "
                            + "fingers. The older girl returns her attention to you and smiles. <i>\"Sorry about the interruption. Where were we?\"</i>";
        }
        return assist.getName()
                        + " isn't likely to be able to hold off Maya on her own. Your best bet is to work together to take her down. You creep up behind her, with Maya "
                        + "showing no sign of noticing you. When you've gotten close enough, you lunge toward her, hoping to catch her from behind. To your surprise, she vanishes the "
                        + "moment you touch her and you stumble forward into " + assist.getName()
                        + ". You turn around and see Maya standing a couple feet away. You've lost the element of "
                        + "surprise (you probably never had it), but it's still 2 on 1.<br/>" + "You suddenly feel "
                        + assist.getName()
                        + " grab you from behind. You turn your head and notice her eyes are dull and unfocused. Maya must have hypnotized her to "
                        + "help trap you. Maya speaks up in a melodic voice. <i>\"How rude of you to interrupt a perfectly enjoyable fight. Naughty " + target.boyOrGirl() + "s should be punished.\"</i> She strips off your clothes and "
                        + "runs her fingers over your exposed dick. You immediately grow hard under her touch. She's too skilled with her hands for you to hold back and you're completely "
                        + "unable to defend yourself. She makes you cum embarrassingly quickly and both girls discard you unceremoniously to the floor. Maya snaps her fingers in front of "
                        + assist.getName()
                        + "'s face to bring her out of the trance and the girl looks down at your defeated form in confusion. <i>\"Thank you for your cooperation. Now we can "
                        + "continue our fight without interruption.\"</i>";
    }
    
    
    /**Helper method to Add this character's first Combat focus scene 
     * MAYA: 
     * 
     * */
    private void addFirstFocusScene(){
        character.addCombatScene(new CombatScene(
                        (c, self, other) -> self.getLevel() >= 40 && !Global.checkFlag(MAYA_FIRSTTYPE1_FOCUS) && !Global.checkFlag(MAYA_FIRSTTYPE2_FOCUS) && !Global.checkFlag(MAYA_FIRSTTYPE_DONE),
                        (c, self, other) -> Global.format(
                                        "[Placeholder] You see {self:name} in some sort of setup scenario. She asks you a question relevant to her advancement."
                                        + "\n\n \"<i>You know what? I was thinking - Now that I'm playing again, I could focus on THIS or THAT. What do you think?</i>\"",
                                        self, other),
                        Arrays.asList(new CombatSceneChoice("Hypnotic Powers", (c, self, other) -> {
                            c.write(Global.format(
                                            "[Placeholder] You tell {self:direct-object} that you'd prefer the first thing. She responds in a manner befitting such a choice.",
                                            self, other,
                                            other.hasDick() ? "[Placeholder] {self:PRONOUN} does something tease-y to your {other:body-part:cock} in response to choosing the first choice."
                                                            : "[Placeholder] {self:PRONOUN} does something tease-y to your {other:body-part:pussy}"
                                                                            + " in response to choosing the first choice."));
                            useFirstType1();
                            return true;
                        }), new CombatSceneChoice("TYPE2", (c, self, other) -> {
                            c.write(Global.format(
                                            "[Placeholder] You tell {self:direct-object} that you'd prefer the second thing. She responds in a manner befitting such a choice.", self, other));
                            useFirstType2();
                            return true;
                        }), new CombatSceneChoice("Why not do both? [Hard Mode]", (c, self, other) -> {
                            c.write(Global.format(
                                            "[Placeholder] You tell {self:name} not to hold back on account of her experience. Her response suggest she's about to get much tougher.", self, other));
                            useFirstType2();
                            useFirstType1();
                            character.getGrowth().extraAttributes += 1;
                            Global.getPlayer()
                                  .getGrowth().addTraitPoints(new int[] {12, 39}, Global.getPlayer());
                            return true;
                        }))));

    }
    
  
    
    /**Helper method to Add this character's second Combat focus scene 
     * MAYA:  
     * 
     * */
    private void addSecondFocusScene(){
        character.addCombatScene(new CombatScene(
                        (c, self, other) -> self.getLevel() >= 50 && !Global.checkFlag(MAYA_SECONDTYPE1_FOCUS) && !Global.checkFlag(MAYA_SECONDTYPE2_FOCUS) && Global.checkFlag(MAYA_FIRSTTYPE_DONE) && !Global.checkFlag(MAYA_SECONDTYPE_DONE),
                        (c, self, other) -> Global.format(
                                        "[Placeholder] You see {self:name} consider how strong the competition is now. She wonders if she should really cut loose and go full power, but how?",
                                        self, other),
                        Arrays.asList(new CombatSceneChoice("TYPE1", (c, self, other) -> {
                            c.write(Global.format(
                                            "[Placeholder] You tell {self:name} that she should make full use of her WHATEVER. She agrees, and shows off a little.",
                                            self, other));
                            useSecondType1();
                            return true;
                        }), new CombatSceneChoice("Embrace the Curse", (c, self, other) -> {
                            c.write(Global.format(
                                            "[Placeholder] You tell {self:name} that she should make full use of her Cursed nature. She agrees, and shows off a little.",
                                            self, other));
                            useSecondType2();
                            return true;
                        }), new CombatSceneChoice("Tell her she's washed up. [Hard Mode]",
                                        (c, self, other) -> {
                                            c.write(Global.format(
                                                            "You joke at {self:name}, telling her that she's washed up. You have a laugh, but her face and eyes and narrow into a dangerous look that could kill a thousand soldiers. You've definitely crossed some kind of line. "
                                                            + "<i>\"Washed up, you say? I've had enough of your mouth {other:name}; you're about to find out what happens when an alumni gets <b>serious</b>. Thankfully, The Benefactor will protect you. <b>You're going to need it.</b>",
                                                            self, other, character.useFemalePronouns() ? "ess" : ""));
                                            useSecondType1();
                                            useSecondType2();
                                            character.getGrowth().extraAttributes += 1;
                                            Global.getPlayer().getGrowth().addTraitPoints(new int[] {21, 48}, Global.getPlayer());
                                            
                                            return true;
                                        }))));
    }
    
    private void useFirstType1(){
        Global.flag(MAYA_FIRSTTYPE1_FOCUS);
        Global.flag(MAYA_FIRSTTYPE_DONE);
    }
    private void useFirstType2(){
        Global.flag(MAYA_FIRSTTYPE2_FOCUS);
        Global.flag(MAYA_FIRSTTYPE_DONE);
    }
    
    private void useSecondType1(){
        Global.flag(MAYA_SECONDTYPE1_FOCUS);
        Global.flag(MAYA_SECONDTYPE_DONE);
    }
    private void useSecondType2(){
        Global.flag(MAYA_SECONDTYPE1_FOCUS);
        Global.flag(MAYA_SECONDTYPE_DONE);
    }
}
