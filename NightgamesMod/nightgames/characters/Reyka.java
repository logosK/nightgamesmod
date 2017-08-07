package nightgames.characters;

import java.util.Arrays;
import java.util.Optional;

import nightgames.characters.body.BreastsPart;
import nightgames.characters.body.CockMod;
import nightgames.characters.body.EarPart;
import nightgames.characters.body.FacePart;
import nightgames.characters.body.PussyPart;
import nightgames.characters.body.TailPart;
import nightgames.characters.body.WingsPart;
import nightgames.characters.body.mods.DemonicMod;
import nightgames.characters.body.mods.ExtendedTonguedMod;
import nightgames.characters.custom.CharacterLine;
import nightgames.combat.Combat;
import nightgames.combat.CombatScene;
import nightgames.combat.CombatSceneChoice;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.items.clothing.Clothing;
import nightgames.skills.strategy.DisablingStrategy;
import nightgames.skills.strategy.FacesitStrategy;
import nightgames.skills.strategy.FootjobStrategy;
import nightgames.skills.strategy.KnockdownStrategy;
import nightgames.skills.strategy.OralStrategy;
import nightgames.start.NpcConfiguration;

public class Reyka extends BasePersonality {
    private static final long serialVersionUID = 8553663088141308399L;

    private static final String REYKA_DISABLING_FOCUS = "ReykaDisablingFocus";
    private static final String REYKA_SEDUCTION_FOCUS = "ReykaSeductionFocus";
    private static final String REYKA_DRAINING_FOCUS = "ReykaDrainingFocus";
    private static final String REYKA_CORRUPTION_FOCUS = "ReykaCorruptionFocus";

    public Reyka() {
        this(Optional.empty(), Optional.empty());
    }

    public Reyka(Optional<NpcConfiguration> charConfig, Optional<NpcConfiguration> commonConfig) {
        super("Reyka", charConfig, commonConfig, false);
        constructLines();
    }

    @Override
    public void applyStrategy(NPC self) {
        self.plan = Plan.hunting;
        self.mood = Emotion.confident;

        self.addPersonalStrategy(new OralStrategy());
        self.addPersonalStrategy(new FootjobStrategy());
        self.addPersonalStrategy(new FacesitStrategy());
        self.addPersonalStrategy(new KnockdownStrategy());
        self.addPersonalStrategy(new DisablingStrategy());
    }

    @Override
    public void applyBasicStats(Character self) {
        preferredCockMod = CockMod.incubus;
        self.outfitPlan.add(Clothing.getByID("tanktop"));
        self.outfitPlan.add(Clothing.getByID("miniskirt"));
        self.outfitPlan.add(Clothing.getByID("garters"));
        self.outfitPlan.add(Clothing.getByID("stilettopumps"));
        self.change();
        self.modAttributeDontSaveData(Attribute.Dark, 2);
        self.modAttributeDontSaveData(Attribute.Seduction, 3);
        self.modAttributeDontSaveData(Attribute.Cunning, 2);
        self.setTrophy(Item.ReykaTrophy);

        Global.gainSkills(self);
        self.getStamina().setMax(50);
        self.getArousal().setMax(120);
        self.getMojo().setMax(110);

        self.body.add(BreastsPart.dd);
        self.body.add(PussyPart.generic.applyMod(DemonicMod.INSTANCE));
        self.body.add(TailPart.demonic);
        self.body.add(WingsPart.demonic);
        self.body.add(EarPart.pointed);
        self.body.add(new FacePart(0.5, 5));
        self.initialGender = CharacterSex.female;
    }

    @Override
    public void setGrowth() {
        character.getGrowth().stamina = 1;
        character.getGrowth().arousal = 10;
        character.getGrowth().bonusStamina = 1;
        character.getGrowth().bonusArousal = 3;
        preferredAttributes.add(c -> c.get(Attribute.Dark) < 50 && c.get(Attribute.Dark) <= c.get(Attribute.Fetish) + 10
                        ? Optional.of(Attribute.Dark) : Optional.empty());
        preferredAttributes.add(c -> c.get(Attribute.Dark) > c.get(Attribute.Fetish) + 10 && c.get(Attribute.Fetish) < 50
                                        ? Optional.of(Attribute.Fetish) : Optional.empty());
        preferredAttributes.add(c -> Optional.of(Attribute.Seduction));

        character.getGrowth().addTrait(0, Trait.succubus);
        character.getGrowth().addTrait(0, Trait.proheels);
        character.getGrowth().addTrait(0, Trait.masterheels);
        character.getGrowth().addTrait(0, Trait.darkpromises);
        character.getGrowth().addTrait(0, Trait.Confident);
        character.getGrowth().addTrait(0, Trait.shameless);
        character.getGrowth().addTrait(3, Trait.sexTraining1);
        character.getGrowth().addTrait(6, Trait.tongueTraining1);
        character.getGrowth().addTrait(9, Trait.expertGoogler);
        character.getGrowth().addTrait(15, Trait.lacedjuices);
        character.getGrowth().addTrait(18, Trait.graceful);
        character.getGrowth().addTrait(24, Trait.spiritphage);
        character.getGrowth().addTrait(33, Trait.magicEyeTrance);
        character.getGrowth().addTrait(36, Trait.addictivefluids);
        character.getGrowth().addTrait(43, Trait.energydrain);
        character.getGrowth().addTrait(46, Trait.sexTraining2);
        character.getGrowth().addBodyPartMod(46, "pussy", ExtendedTonguedMod.INSTANCE);
        character.getGrowth().addTrait(49, Trait.soulsucker);
        character.getGrowth().addTrait(55, Trait.desensitized2);
        character.getGrowth().addTrait(58, Trait.carnalvirtuoso);

        
        this.addFirstFocusScene();      
        this.addSecondFocusScene();     
        
     
    }

    private void useDisabling() {
        Global.flag(REYKA_DISABLING_FOCUS);
        character.getGrowth().addTrait(12, Trait.SuccubusWarmth);
        character.getGrowth().addTrait(19, Trait.lactating);
        character.getGrowth().addTrait(19, Trait.Pacification);
        character.getGrowth().addTrait(28, Trait.DemonsEmbrace);
        character.getGrowth().addTrait(39, Trait.VampireWings);
    }

    private void useSeduction() {
        Global.flag(REYKA_SEDUCTION_FOCUS);
        character.getGrowth().addTrait(12, Trait.MelodiousInflection);
        character.getGrowth().addTrait(19, Trait.ComeHither);
        character.getGrowth().addTrait(28, Trait.TenderKisses);
        character.getGrowth().addTrait(39, Trait.PinkHaze);
    }

    private void useCorruption() {
        Global.flag(REYKA_CORRUPTION_FOCUS);
        character.getGrowth().addTrait(21, Trait.Corrupting);
        character.getGrowth().addTrait(30, Trait.InfernalAllegiance);
        character.getGrowth().addTrait(40, Trait.LastingCorruption);
        if (Global.checkFlag(REYKA_DISABLING_FOCUS)) {
            character.getGrowth().addTrait(52, Trait.TotalSubjugation);
        } if (Global.checkFlag(REYKA_SEDUCTION_FOCUS)) {
            character.getGrowth().addTrait(52, Trait.Subversion);
        }
    }

    private void useDraining() {
        Global.flag(REYKA_DRAINING_FOCUS);
        character.getGrowth().addTrait(21, Trait.Greedy);
        character.getGrowth().addTrait(30, Trait.RaptorMentis);
        character.getGrowth().addTrait(40, Trait.BottomlessPit);
        if (Global.checkFlag(REYKA_DISABLING_FOCUS)) {
            character.getGrowth().addTrait(52, Trait.SpecificSapping);
        } if (Global.checkFlag(REYKA_SEDUCTION_FOCUS)) {
            character.getGrowth().addTrait(52, Trait.WillingSacrifice);
        }
    }

    @Override
    public void rest(int time) {
        super.rest(time);
        if (!(character.has(Item.Dildo) || character.has(Item.Dildo2)) && character.money >= 250) {
            character.gain(Item.Dildo);
            character.money -= 250;
        }
        if (!(character.has(Item.Tickler) || character.has(Item.Tickler2)) && character.money >= 300) {
            character.gain(Item.Tickler);
            character.money -= 300;
        }
        if (!(character.has(Item.Strapon) || character.has(Item.Strapon2)) && character.money >= 600) {
            character.gain(Item.Strapon);
            character.money -= 600;
        }
        if (character.money > 0) {
            Global.getDay().visit("Body Shop", character, Global.random(character.money));
        }
        if (character.money > 0) {
            Global.getDay().visit("XXX Store", character, Global.random(character.money));
        }
        if (character.money > 0) {
            Global.getDay().visit("Black Market", character, Global.random(character.money));
        }
        Decider.visit(character);
        int r;
        for (int i = 0; i < time; i++) {
            r = Global.random(8);
            if (r == 1) {
                Global.getDay().visit("Exercise", this.character, 0);
            } else if (r == 0) {
                Global.getDay().visit("Browse Porn Sites", this.character, 0);
            }
        }
        character.gain(Item.semen, Global.random(3) + 1);
        buyUpTo(Item.semen, 5);
    }
    
    private void constructLines() {
        character.addLine(CharacterLine.BB_LINER, (c, self, other) -> {
            return "Reyka looks at you with a pang of regret: <i>\"In hindsight, damaging"
                            + " the source of my meal might not have been the best idea...\"</i>";
        });

        character.addLine(CharacterLine.NAKED_LINER, (c, self, other) -> {
            return "<i>\"You could have just asked, you know.\"</i> As you gaze upon her naked form,"
                            + " noticing the radiant ruby ardorning her bellybutton, you feel"
                            + " sorely tempted to just give in to your desires. The hungry look"
                            + " on her face as she licks her lips, though, quickly dissuades you" + " from doing so";
        });

        character.addLine(CharacterLine.STUNNED_LINER, (c, self, other) -> {
            return "Reyka is laying on the floor, her wings spread out behind her, panting for breath";
        });

        character.addLine(CharacterLine.TAUNT_LINER, (c, self, other) -> {
            return "\"You look like you will taste nice. Maybe if you let me have a taste, I will be nice to you too.\"";
        });

        character.addLine(CharacterLine.TEMPT_LINER, (c, self, other) -> {
            return "\"Why keep fighting? Wouldn't it just feel SO much better just to let me do what I do best?\"";
        });

        character.addLine(CharacterLine.NIGHT_LINER, (c, self, other) -> {
            return "You feel exhausted after yet another night of sexfighting. You're not complaining, of course; "
                            + "what " + other.guyOrGirl()
                            + " would when having this much sex with several different girls? Still, a weekend would "
                            + "be nice sometime... About half way to your room, Reyka steps in front of you. Where did she come from? "
                            + "<i>\"Listen, " + other.getName()
                            + ", I've been doing some thinking lately. You know very well I've had sex with a lot "
                            + "of " + other.guyOrGirl()
                            + "s and a fair amount of girls, too, right?\"</i> You just nod, wondering where this is going. <i>\"Well, "
                            + "in all that time no one has ever made me feel the way you can. I don't know why, really, but I can't help "
                            + "feeling there's something special about you.\"</i> You stand there, paralyzed, with a look of amazement "
                            + "on your face. Reyka intimidates you. Hell, she is downright terrifying at times. To see and hear "
                            + "her like this is like nothing you had ever expected from her. For a moment, you think this is all some "
                            + "elaborate trick of some sort, but that thought vanishes the instant you see tears welling in her eyes. "
                            + "<i>\"I just... We demons aren't supposed to feel like this, you know? We don't form relationships. It's all "
                            + "just a constant power struggle, constant scheming and looking over your shoulder and sleeping with a "
                            + "knife under your pillow. It has never bothered me before; it's simply what I am. That's what I used to "
                            + "think, anyway. Now, I'm not so sure... about anything...\"</i> She quitely sobs while saying this, and you "
                            + "embrace her. You hold her there for some time, before inviting her to spend the night at your place. "
                            + "You don't even have sex when you get there, you just both lay down in your single bed, close to "
                            + "each other, and enjoy a peaceful sleep together with your arms around her and her head on your shoulder.";
        });

        character.addLine(CharacterLine.ORGASM_LINER, (c, self, other) -> {
            return "Reyka shudders, <i>\"Mmm it's been a while since I've felt that. Here, I'll return the favor\"</i>";
        });

        character.addLine(CharacterLine.MAKE_ORGASM_LINER, (c, self, other) -> {
            return "With a devilish smile, Reyka brings her face close to yours <i>\"Mmmmm that smells great! Too bad I'm still pretty hungry.\"</i>";
        });

        character.addLine(CharacterLine.CHALLENGE, (c, self, other) -> {
            return "<i>\"Yum, I was just looking for a tasty little morsel.\"</i><br/><br/>"
                            + "Reyka strikes a seductive pose and the devilish smile"
                            + " on her face reveals just what, or more specifically,"
                            + " who she intends that morsel to be.";
        });

        character.addLine(CharacterLine.DESCRIBE_LINER, (c, self, other) -> {
            
            if (self.get(Attribute.Divinity) > 15) { 
               return "Reyka is no longer just a regular succubus - as if her being a succubus wasn't already scary enough: Standing before you is a dark goddess radiating a terrifying aura of unholy power. "
                                + "Reyka remains tall in stature, but her alluring face and beautiful body now call to you to prostrate yourself before her and submit to her will. Her hands with their "
                                + "red-polished nails now look as horribly cruel as they do enticing, gentle, and soft. Underneath, her long and perfectly formed legs and delicate feet stand in an "
                                + "imposing posture. Behind her, you see a pair of magnificent looking bat-like wings, commanding you to submit to her embrace and worship her.<br/><br/>"
                                + "Her gaze both captivates you and terrifies you - it's a gaze of indescribable pleasure and unknown power. She'll do worse than suck out your soul...far worse.";
            } else {
                 return "Reyka the succubus stands before you, six feet tall with"
                                + " the most stunningly beautiful body you have ever seen."
                                + " Her long black hair enshrines her perfect face like a priceless"
                                + " painting. Her arms are slim and end in long-fingered,"
                                + " soft hands, nails polished shining red. Underneath, her long and"
                                + " perfectly formed legs and delicate feet stand in an imposing posture."
                                + " Behind her, you see a pair of relatively small but powerful-looking bat wings.<br/>"
                                + " Her gaze speaks of indescribable pleasure, but your mind reminds you"
                                + " of the cost of indulging in a succubus' body: Give her half a chance"
                                + " and she will suck out your very soul.";
            }

        });

        character.addLine(CharacterLine.LEVEL_DRAIN_LINER, (c, self, other) -> {
            String part = Global.pickRandom(c.getStance().getPartsFor(c, self, other)).map(bp -> bp.describe(self)).orElse("pussy");
            if (other.getLevel() < self.getLevel() - 5) {
                if (c.getStance().vaginallyPenetratedBy(c, other, self)) {
                    return "The succubus gives you a sad smile as you cum uncontrollably into her nightmarish cunt, with your experience and training leaving you yet again. <i>{other:name}... You know, you're not really enough for me now. I'm still hungry!</i>";
                } else if (c.getStance().anallyPenetratedBy(c, other, self)) {
                    return "The succubus gives you a sad smile as you cum uncontrollably into her nightmarish backdoor, with your experience and training leaving you yet again. <i>{other:name}... You know, you're not really enough for me now. I'm still hungry!</i>";
                } else {
                    return "The succubus gives you a sad smile as you cum uncontrollably around her nightmarish cock, with your experience and training leaving you yet again. <i>{other:name}... You know, you're not really enough for me now. I'm still hungry!</i>";
                }
            } else if (other.getLevel() >= self.getLevel()) {
                if (c.getStance().inserted(other)) {
                    return "Reyka gives you a saucy grin as your cum floods her diabolic fuckhole \"<i>Oh ho ho, thank you for the donation kind sir! But you know what they say about charity right? It pays to make it a habit!</i>\"";
                } else {
                    return "Reyka gives you a saucy grin as she draws out your power with your orgasm \"<i>Oh ho ho, thank you for the donation kind sir! But you know what they say about charity right? It pays to make it a habit!</i>\"";
                }
            } else {
                return "Reyka gives off a rapturous air as bits and pieces of your soul is absorbed by her demonic " + part + "  <i>\"Mmmm that is <b>good</b>! This is usually the part where I turn on my summoner and do my demon thing, but I think I'll make an exception this time... for now.\"</i>";
            }
        });
    }

    @Override
    public String victory(Combat c, Result flag) {
        Character opponent = character.equals(c.p1) ? c.p2 : c.p1;
        if (c.getStance().anallyPenetrated(c, opponent)) {
            String outp= "Reyka alternates between long hard thrusts and sensual grinding to keep you from getting used to the stimulation, and the pleasure it is "
                            + "inflicting on you stops you from mustering the resolve to fight back. <i>\"I do love a good bit of pegging.\"</i> Reyka comments as she begins "
                            + "to gently rock the head of the strapon over your prostate, leaving you breathing hard as your mouth hangs open. <i>\"There's a special "
                            + "pleasure in making a " + opponent.boyOrGirl() + " a little butt slave.\"</i> Her words shock you and cause your resistance to slip a little. <i>\"Hmmm?\"</i> She purrs <i>\"Would "
                            + "you like that?\"</i> she asks, picking up the pace of her thrusting. <i>\"To be my little pet " + opponent.boyOrGirl() + " slut?\"</i> Your only response is to cum. Hard. Ropes "
                            + "of cum fall to the ground below you.<br/><br/>Reyka pouts as she pulls out <i>\"Such a good waste of semen though.\"</i> she tuts. <i>\"Looks like you "
                            + "still owe me a meal.\"</i> She smirks in a way that makes your eyes flash quickly left to right, looking for an escape route. Reyka is too quick "
                            + "however and soon you find yourself pinned with your still hard cock buried deep in her pussy.<br/><br/>She rides you until you cum again and she "
                            + "has cum twice herself. She stands up and begins collecting her clothes and her spoils as the victor. She turns to you. <i>\"The offer still "
                            + "stands; you'd make a great sub if you're ever interested in broadening your sexual horizons. Open minded "+opponent.boyOrGirl()+"s are hard to find.\"</i> She admits smiling. ";
            if(opponent.get(Attribute.Submissive)*2 < opponent.level) {outp+="You shake your head; you don't think that sort of thing would really suit you. Her smile deflates some but she nods her head and "
                            + "turns to go. <i>\"Let me know if that ever changes, I'd definitely enjoy opening your mind,\"</i> she calls over her shoulder as she leaves.";}
            else {outp+="You blush and look away. Reyka's smile expands until it almost seems to extend beyond the boundaries of her face. She licks her lips, looking at you with a predatory air, "
                            + "before stepping forward so that her crotch is right in front of your seated face. She reaches down and ruffles your hair, saying <i>\"If you're ever feeling"
                            + " adventurous, come and see me. I'll make sure you'll have a good time.\"</i> She walks forward to leave, pushing you over from her crotch hitting your face. You watch "
                            + "her go, shivering at the thought of what she might mean by \"a good time\".";}
            return outp;
        }
        character.arousal.empty();
        return "With a final cry of defeat (and pleasure) you erupt under Reyka's"
                        + " attentions. She immediatly pounces on you and draws your lips to hers."
                        + " As does so, she inhales deeply, drawing more than just air out of you."
                        + " You feel your strength flowing into her and even though your vision"
                        + " is already darkening, you see her starting to literally glow, surrounded"
                        + " by a soft, deep red aura. As she continues to drink in your energy,"
                        + " you feel something shift in you, as if something that was there all"
                        + " along but has always gone unnoticed by you suddenly got yanked on."
                        + " Just before you pass out, you see her wings enveloping you both"
                        + " in a dark, warm cocoon.<br/><br/> After what seems like an eternity,"
                        + " but what actually lasted for only a few minutes, you wake up and"
                        + " drowsily look around. You can see Reyka sitting cross-legged a few feet"
                        + " away, her wings folded neatly behind her back and her eyes fixed on" + " yours."
                        + (opponent.hasDick()
                                        ? " You notice a bottle of pearly"
                                                        + " white liquid behind her and can only guess where it came from."
                                        : "")
                        + " <i>\"You tasted heavenly\"</i>, she says, while you are still too"
                        + " far gone to catch on to the irony of the statement, <i>\"So here I was,"
                        + " drinking in your delicous energy, and it struck me that if I claimed"
                        + " your soul now, I wouldn't be able to drink from you again. So I simply"
                        + " took a little nibble into it and let you recover. I will expect you to"
                        + " repay this kindness soon, and there is only one thing I will accept"
                        + " as payment. I'll leave you to figure out what it is.\"</i><br/><br/> With that,"
                        + " she walks off, her hips, barely covered by her short miniskirt,"
                        + " seductively waving good-bye. For now.";
    }

    @Override
    public String defeat(Combat paramCombat, Result flag) {
        character.arousal.empty();
        if (character.has(Trait.lacedjuices) && Global.random(3) == 0 ) {
            return "Reyka shivers as she approaches her climax and her legs fall open defenselessly. You can't resist taking advantage of this opening to deliver the "
                            + "coup de grace. You grab hold of her thighs and run your tongue across her wet pussy. Her love juice is surprisingly sweet and almost intoxicating, "
                            + "but you stay focused on your goal. You ravage her vulnerable love button with your tongue and a flood of tasty wetness hits you as she cums. You "
                            + "prolong her climax by continuing to lick her while lapping up as much of her love juice as you can. The taste seems almost familiar, but you can't "
                            + "quite place it. Sweet and tangy like a desert wine? Not a perfect comparison, but not far off.<br/><br/>Reyka should be coming down from her peak, but "
                            + "she's still moaning quite passionately. Oh well, it can't hurt to drink up the last of her love juice. You're the one who made her juice herself, so "
                            + "it seems only fair. It is very tasty. Intoxicating was the word that came to mind early, but addictive seems to fit too. Reyka's flower is mostly "
                            + "clean, but you stick your tongue deep inside to be sure. There seems to be some fresh love juice in this bit... and this one.... Here too.<br/><br/>Reyka's "
                            + "pussy tenses up and you're treated to another flood of her wonderful flavor. You can't let this much juice go to waste. You diligantly continue to "
                            + "lick Reyka's trembling girl parts as she squeals in passion. You feel her hands grip your hair desperately and you have to hold her hips to keep her "
                            + "from squirming away. She's producing a decent amount of delicious nectar, but it occurs to you that she'll probably give you more if you focus on her "
                            + "clit. You target her pearl and lick it rapidly until she screams in pleasure and rewards you with another surge of juice. This seems like the best "
                            + "way to get more of her wonderful juice. You could just stay here drinking this stuff all night, and you just may.<br/><br/>You suddenly feel Reyka's tail wrap "
                            + "tightly around your balls. Your head jerks up in surprise and her thighs clamp together on it, holding you out of reach of her delicious honey pot. "
                            + "<i>\"Down lover\"</i> Reyka admonishes you as she covers her groin protectively. <i>\"I appreciate the dedication, but after a couple orgasms, I need a chance to "
                            + "catch my breath.\"</i> You feel your head clear a bit and realize you completely fell victim to her addictive love juice.<br/><br/>Reyka uses her grip on your "
                            + "head to force you onto your back. <i>\"I do love being eaten out, but right now I'm ready to be filled.\"</i> She releases the head scissor and positions herself "
                            + "over your dick before dropping her hips to engulf you to the hilt. A jolt goes through you and you realize exactly how horny you are. In addition to "
                            + "not having any relief, Reyka's fluids have started to affect you. You're incredibly hard and sensitive, but even though Reyka is riding you intensely, "
                            + "your ejaculation feels painfully out of reach. You don't feel your climax start to build until Reyka is moaning and approaching yet another orgasm. Is "
                            + "that an innate succubus ability? Is she controlling the timing of your orgasm? You don't have time to dwell on the question, your hips thrust involuntarily "
                            + "as you shoot your load into her waiting quim. Reyka gives you a deep, passionate kiss as she gets off of you. <i>\"Thanks lover. You sure know how to show a "
                            + "girl a good time.\"</i>";
        }
        return "As you bring Reyka ever closer to her climax, her prehensile"
                        + " tail suddenly pulls you to the ground and coils around your neck."
                        + " She squats down over your face and uses her tail to push your face"
                        + " up against her pussy. The scent is so enticing that you simply must"
                        + " have a taste, and you start lapping at her feverishly. Somewhere along"
                        + " the way her tail let go of your neck, the compulsion it provided having"
                        + " been replaced by that of her aphrodisac juices. As she finally screams out"
                        + " in orgasm, the sound reverberating through your soul, the amount of"
                        + " fluids gushing into your willing mouth nearly drown you. After a few"
                        + " seconds she rolls off of you, although you don't notice it, having passed"
                        + " out from the overdose of aphrodisiacs.<br/><br/>You come to your senses just"
                        + " in time to see Reyka drinking down the load of cum you are shooting"
                        + " into her mouth. Somehow, you keep from passing out as she drinks"
                        + " some of your energy and soon, you see her face hovering over yours."
                        + " <i>\"Sorry about that, but you wore me out so thoroughly I just needed"
                        + " a little snack to get back on my feet. You'll be perfectly fine in"
                        + " a few minutes, after that we can go back to hunting each other. I"
                        + " certainly hope we meet again soon, that treatment you gave me is well"
                        + " worth the trouble of fighting through the others to get at you.\"</i>"
                        + " As she walks away into the distance you can't help but feel like"
                        + " 'winning' is not quite the word you would use to describe what you" + " just went through.";
    }

    @Override
    public String victory3p(Combat c, Character target, Character assist) {
        if (target.human()) {
            return "<i>\"How kind of you to hold him for me, dear.\"</i> Reyka bows her head ever so slightly towards "
                            + assist.getName() + " and then turns her gaze upon you prone form. "
                            + "She pulls a blindfold out of a small pocket in her miniskirt and secures it tightly over your eyes. <i>\"Wouldn't want to spoil the surprise, would we?\"</i> For "
                            + "just a moment, you feel a slight pull on your mind, but the sensation passes quickly, replaced by that of one of her slender fingers invading your mouth. "
                            + "It is covered with a fragrant liquid and given what you already know about her, there is little doubt in your mind of its origins. Your suspicions "
                            + "are proven correct when the aphrodisiac reaches your loins, which respond as expected. Apparently not one to stand on ceremony, Reyka immediately "
                            + "settles over your now rock hard dick and lowers herself down onto it. The sensation is beyond comparison, her pussy wiggles and twists around you almost "
                            + "as if it has a mind of its own, a mind connected to your own, knowing what will bring you the most pleasure. Your experiences in sex-fighting have "
                            + "left you with impressive sexual stamina, but in the face of a succubus' unimpeded attentions, no man can hope to last. All too quickly you succumb "
                            + "to the feelings, pouring your seed into the succubus. Just your seed. You were expecting her take so much more, but you just feel a little tired, "
                            + "not more so than after a regular orgasm. The mystery is unveiled when Reyka removes the blindfold with her left hand. In her right hand, she is "
                            + "holding a bottle. That bottle is firmly planted against the head of your still twitching dick and filled with your cum. <i>\"You looked scrumptuous, "
                            + "sitting there all helpless, but I was really in need of some supplies. Still, I didn't want to deny you the pleasure, so I crafted a teeny tiny "
                            + "illusion just for you.\"</i> As she says this, she pours a small drop of your semen onto her finger and licks it up. <i>\"Yum, I might just have to find "
                            + "you again later.\"</i> Both she and " + assist.getName()
                            + " walk off, in opposite directions, the former holding your clothes and the latter quietly giggling at "
                            + "your embarrassment. Ah, well.";
        }
        return "<i>\"My my, what a cute little offering you have caught for me tonight\"</i>, Reyka says, looking you at you with a satisfied grin on her face. <i>\"Not very nutricious, "
                        + "but certainly a good deal of fun.\"</i> With that, she starts gently undressing "
                        + target.getName() + ". When she is finished she squats down in front of her, bringing "
                        + "her tail up between them. <i>\"Where would you prefer it dear?\"</i>, she asks "
                        + target.getName() + ", whose eyes grow wide in shock. She manages to stammer out a "
                        + "few syllables, but nothing quite coherent. <i>\"No preference? Then I guess I will simply choose for you.\"</i> She brings her spade-tipped tail between "
                        + target.getName() + "s "
                        + "legs and starts running the very tip rapidly across her labia. When it is sufficiently wet, she moves it slightly upwards and moves it briskly back and forth over "
                        + target.getName() + "'s clit.<br/><br/>" + target.getName()
                        + ", at first scared, now has her eyes closed and begins moaning feverishly. Just when she has almost reached her climax, "
                        + "Reyka digs her tail deep into " + target.getName() + "'s drooling pussy. This sends "
                        + target.getName() + " loudly over the edge. Her screams of pleasure are almost deafening, "
                        + "and you have to work really hard to restrain her convlusing body. After a minute or so, the orgasm subsides and "
                        + target.getName() + " falls asleep and you gently lay her " + "down. When you turn to look at "
                        + character.getName()
                        + ", you are startled by the predatory look in her eyes. <i>\"I'm afraid all the excitement has left me a tad peckish. Be a "
                        + "dear and help me out with that, will you?\"</i> You ponder whether or not you made a mistake in helping her.";
    }

    @Override
    public String intervene3p(Combat c, Character target, Character assist) {
        if (target.human()) {
            return "Your fight with " + assist.getName() + " starts out poorly; she already"
                            + " has you naked and aroused, wheras she seems as cool and calm as when"
                            + " you started. You haven't lost yet though, you just need to find an opening "
                            + "and turn things around. A noise behind you causes you to turn and your vision is "
                            + "filled with two piercing red eyes. <i>\"Kneel.\"</i> You drop to your knees involuntarily. "
                            + "The rational part of your brain is telling you that Reyka is trying to dominate your "
                            + "mind and you should resist, but what's the point? Reyka's tail binds your wrists and "
                            + "she forces you to turn back to a bewildered " + assist.getName()
                            + ". <i>\"I'm not poaching "
                            + "your prey,\"</i> you hear her say. <i>\"He's all yours.\"</i>";
        }

        return "Your fight with " + target.nameDirectObject() + " starts out poorly; she already"
                        + " has you naked and aroused, wheras she seems as cool and calm as when"
                        + " you started. Fortune, though, seems to have a strange sense of humor as"
                        + " your salvation comes in the form of a winged demon swooping down on " + target.nameDirectObject()
                        + ". The two are briefly entangled in a ball of limbs and wings,"
                        + " but soon Reyka comes out on top. She is pinning " + target.nameDirectObject()
                        + " helplessly to the ground, holding her arms behind her back and"
                        + " locking her shoulders in place with her wings. The struggle has " + "left " + target.nameDirectObject()
                        + " completely naked and ready for you to" + " take advantage of.";
    }

    @Override
    public String draw(Combat paramCombat, Result flag) {
        return "As you and Reyka are both thrusting against each other for all you're worth, you feel your inevitable climax approaching very rapidly. Just "
                        + "before you erupt into her, you notice a strange change come over her. Her tail and wings seem to evaporate before you and her white skin gains "
                        + "a slight tan. The challenging look in her eyes and confident little smile are replaced by an expression of surprise and shock. But you don't notice "
                        + "any of this. All you can see are her crimson irises giving way to a deep, radiant blue. Just as you spot what you think might be a single tear "
                        + "forming in her right eye, both of you reach your orgasms. You were expecting to feel her power wash over you, to feel it pour into every nook "
                        + "and cranny of your soul and then draw that soul out and into her, but none of that happens. The only sensations are the pure, tantric bliss of "
                        + "simultaneous climax and a strange affection for the two deep blue eyes staring into yours. When eventually the paradise you found yourself in "
                        + "once again gives way to the cold bitterness of reality and you see the first red spots already reappearing in Reyka's eyes, your "
                        + "rapidly souring mood is mollified by her passionate, warm embrace. She just holds you like that, not speaking, not moving, even as the demonic parts "
                        + "of her anatomy return to her shapely form. After a few minutes, which might as well have been days for you, she releases lets go of you, leans "
                        + "back some and softly speaks the two words you least expected ever to hear from her: <i>\"Thank you.\"</i> With that, she leaves what few clothes she "
                        + "usually wears, turns around and walks away. Not the domineering, seductively swaying stride you have grown used to, but rather a slower, more "
                        + "composed walk with her head slightly bowed. You are absolutely perplexed by this rare display of emotion, but after a while you gather your wits "
                        + "- and her clothes - and take off into the night. In the back of your mind, you know she will be back to hunt you down later, but this experience "
                        + "will remain entrenched in you memory for quite some time.";
    }

    @Override
    public boolean fightFlight(Character paramCharacter) {
        return !character.mostlyNude() || Global.random(3) == 1;
    }

    @Override
    public boolean attack(Character paramCharacter) {
        return !character.mostlyNude() || Global.random(3) == 1;
    }

    public double dickPreference() {
        return 2;
    }

    @Override
    public boolean fit() {
        return (!character.mostlyNude() || Global.random(3) == 1) && character.getStamina().percent() >= 50
                        && character.getArousal().percent() <= 50;
    }

    @Override
    public boolean checkMood(Combat c, Emotion mood, int value) {
        switch (mood) {
            case dominant:
                return value >= 50;
            case nervous:
                return value >= 150;
            default:
                return value >= 100;
        }
    }
    
    /**Helper method to Add this character's first Combat focus scene 
     * REYKA: Disable or Seduction
     * 
     * */
    private void addFirstFocusScene(){
        character.addCombatScene(new CombatScene((c, self, other) -> self.getLevel() >= 12 
                        && !Global.checkFlag(REYKA_DISABLING_FOCUS) && !Global.checkFlag(REYKA_SEDUCTION_FOCUS)
                        , (c, self, other) -> Global.format("You had turned your back to Reyka after your fight."
                                        + " Big mistake. Out of nowhere, {self:pronoun} crashes into"
                                        + " you from behind with great force, knocking you down."
                                        + " You quickly roll over, but Reyka binds your legs together at"
                                        + " the knees and holds them close to {self:direct-object}. <i>\"My,"
                                        + " {other:name}, I could get used to this.\"</i> Shocker. <i>\""
                                        + "The question, then, is simple. Do I make </i>you<i> want this"
                                        + " as well, or do I simply give you no choice in the matter?\"</i>", self, other), 
                        Arrays.asList(
                        new CombatSceneChoice("Seduction", (c, self, other) -> {
                            c.write(Global.format("Well... If you can't stop {self:direct-object} anyway -"
                                            + " and chances seem good you won't be able to, given present"
                                            + " circumstances - then you might as well enjoy it, right?"
                                            + " <i>\"How wise of you, {other:name}. This should come as no"
                                            + " suprise, but succubi are </i>very<i> good at making cute"
                                            + " {other:boy}s like you enjoy themselves. I shall bring all of"
                                            + " that considerable talent to bear on you. I'd tell you to prepare"
                                            + " yourself, but you can't.\"</i> Without another word, Reyka"
                                            + " disentangles {self:reflective} from you and walks off, leaving"
                                            + " you bewildered on the ground, staring after {self:direct-object}."
                                            , self, other));
                            useSeduction();
                            return true;
                        }),
                        new CombatSceneChoice("Helplessness", (c, self, other) -> {
                            c.write(Global.format("You're not about to just roll over and give up. Maybe you can"
                                            + " win, maybe you can't, but you are sure as hell going to fight"
                                            + " every step of the way. <i>\"Mmmm, excellent. I do like a challenge,"
                                            + " {other:name}. It makes winning so much more satisfying. Make sure"
                                            + " you train before we meet next, because I sure will.\"</i>"
                                            + " {self:PRONOUN} wiggles {self:possessive} hips against you."
                                            + " <i>\"In fact, you might want to get used to being in this position,"
                                            + " I think you'll find yourself down there quite a lot.\" {self:PRONOUN}"
                                            + " gives you a quick peck on your lips and flies off. After a few"
                                            + " seconds, you get back up and ready to go. It might be a good"
                                            + " idea to take {self:possessive} advice to heart...", self, other));
                            useDisabling();
                            return true;
                        }),
                        new CombatSceneChoice("What? You're just going to win! [Hard Mode]", (c, self, other) -> {
                            c.write(Global.format("{self:PRONOUN} is talking like {self:pronoun}'s already won!"
                                            + " You let {self:direct-object} know that you're confident you"
                                            + " can take {self:direct-object} on. Almost instantly, a wide,"
                                            + " elated grin appears on Reyka's face and you feel a suspicious wetness"
                                            + " down below. <i>\"Oooh, {other:name}! You're getting me all hot!\"</i>"
                                            + " Indeed, the grinding of {self:possessive} crotch against yours"
                                            + " is getting quite enthusiastic. <i>\"I am </i>really<i> going to"
                                            + " enjoy this. I want you to give it everything you've got, {other:name},"
                                            + " that way there will be that much more for me to take! Oh, I just"
                                            + " can't wait to see if you can back up your talk! You'd better"
                                            + " not dissapoint me, {other:name}!\"</i>", self, other));
                            useSeduction();
                            useDisabling();
                            character.getGrowth().extraAttributes += 1;
                            Global.getPlayer().getGrowth().addTraitPoints(new int[]{12,19},Global.getPlayer());
                            return true;
                        }))));
    }
    
    /**Helper method to Add this character's second Combat focus scene 
     * REYKA:  Drain or Corruption
     * 
     * */
    private void addSecondFocusScene(){
        character.addCombatScene(new CombatScene((c, self, other) -> self.getLevel() >= 22
                        && !Global.checkFlag(REYKA_DRAINING_FOCUS) && !Global.checkFlag(REYKA_CORRUPTION_FOCUS)
                        && (Global.checkFlag(REYKA_DISABLING_FOCUS) || Global.checkFlag(REYKA_SEDUCTION_FOCUS))
                        , (c, self, other) -> Global.format("After your fight, Reyka is staring at you"
                                        + " appraisingly. <i>\"{other:name}, your progress lately has"
                                        + " been impressive. I've been keeping a close eye on you -"
                                        + " sometimes a </i>very<i> close eye - and I've determined"
                                        + " that I could really use some your skills in the Games."
                                        + " We could do that in one of two ways. Either you let me..."
                                        + " tweak you a little bit to be more helpful, or I could just"
                                        + " take some of it directly. Which would you prefer?\"</i>", self, other), 
                         Arrays.asList(
                         new CombatSceneChoice("Go team demon!", (c, self, other) -> {
                             c.write(Global.format("Working with Reyka seems like a good deal. 'Tweaking'"
                                             + " sounds a little worrisome, but you know that for all"
                                             + " of {self:possessive} talk, Reyka isn't all that bad."
                                             + " Or, at least, {self:pronoun} hasn't been. You inform"
                                             + " {self:direct-object} of your decision. <i>\"How wonderful,"
                                             + " {other:name}! You just do what you always do, I'll have"
                                             + " to go perform a ritual. It will be pretty stressful, so"
                                             + " I will need plenty of kisses from my new partner, yes?"
                                             + " Now, if you'll excuse me, I need to go find a virgin or two"
                                             + " for the ritual. Maybe three, just to be safe.\"</i> Right."
                                             + " Not all that bad. Right?", self, other));
                             useCorruption();
                             return true;
                         }),
                         new CombatSceneChoice("'Tweaking' sounds really bad...", (c, self, other) -> {
                             c.write(Global.format("You don't know what Reyka means by 'tweaking you',"
                                             + " but you're pretty sure you don't <i>want</i> to know."
                                             + " You turn down {self:possessive} offer. <i>\"A shame. I had"
                                             + " hoped we could come to an understanding. In the end it doesn't"
                                             + " really matter, though. If you thought I was good at 'borrowing'"
                                             + " your strength before, wait until I find some clever ways around"
                                             + " the restrictions the Benefactor imposed. Don't worry, though."
                                             + " I'll replace every pebble of power I take with a mountain's"
                                             + " worth of ecstasy.\"</i> {self:PRONOUN} blows you a kiss which"
                                             + " impacts with physical force, and before you fully recover she's"
                                             + " already walked off.", self, other));
                             useDraining();
                             return true;
                         }),
                         new CombatSceneChoice("Why not let Reyka help you, instead? [Hard Mode]", (c, self, other) -> {
                             c.write(Global.format("<i>\"Hahaha! Oh, {other:name}, you really are quite charming,"
                                             + " you know that? But very well, you do not wish to aid me. Then I"
                                             + " will simply have to make you. I'm sure that after a little bit"
                                             + " of that 'tweaking' I mentioned you will be much more"
                                             + " amenable towards letting me borrow some of your talents."
                                             + " Ah, the thrill of the hunt! %s I'll see you around, {other:name},"
                                             + " that I can promise you\"</i> You think you might be in trouble..."
                                             , self, other, Global.checkFlag(REYKA_DISABLING_FOCUS) &&
                                             Global.checkFlag(REYKA_SEDUCTION_FOCUS) ? "First your complete "
                                                             + "self-confidence, now this. You're shaping up"
                                                             + " to be quite the worthy opponent!" : "Your"
                                                             + " determination is getting be all worked up!"
                                                             + " It's about time I got a proper rival!"));
                             useCorruption();
                             useDraining();
                             character.getGrowth().extraAttributes += 1;
                             Global.getPlayer().getGrowth().addTraitPoints(new int[]{40,52},Global.getPlayer());
                             return true;
                         })
                         )));
    }
}
