package nightgames.daytime;

import java.util.ArrayList;
import java.util.Optional;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.CockMod;
import nightgames.characters.body.CockPart;
import nightgames.characters.body.mods.SizeMod;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.requirements.BodyPartRequirement;
import nightgames.requirements.NotRequirement;
import nightgames.requirements.RequirementShortcuts;
import nightgames.status.addiction.Addiction;
import nightgames.status.addiction.AddictionType;

public class EveTime extends BaseNPCTime {
    
    public EveTime(Character player) {
        super(player, Global.getNPC("Eve"));
        knownFlag = "Eve";        
        //TODO: EveKnown - You have to pay Aesop to find her, Aesop says that he's just as scared of Eve as he is of Maya.
        giftedString = "\"Hey, thanks.\"";
        giftString = "\"A present? Okay.\"";
        transformationOptionString = "Freaky Stuff";
        advTrait = Trait.cursed;        //TODO: Eve Uses Fetish
        transformationIntro = "[Placeholder]Eve explains she can train you to like freaking shit.";
        loveIntro = "";
        transformationFlag = "";
    }

    @Override
    public void buildTransformationPool() {
        transformationOptions = new ArrayList<>();
        {
            TransformationOption growCock = new TransformationOption();
            growCock.ingredients.put(Item.PriapusDraft, 2);
            growCock.addRequirement(RequirementShortcuts.rev(new NotRequirement(new BodyPartRequirement("cock"))), "Has no penis");
            growCock.option = "Eve: Grow a cock";
            growCock.scene = "[Placeholder]<br/>Eve does some sort of thing and grows a cock. She's glad she has it back.";
            growCock.effect = (c, self, other) -> {
                other.body.add(new CockPart().applyMod(new SizeMod(SizeMod.COCK_SIZE_BIG)).applyMod(CockMod.bionic));
                return true;
            };
            transformationOptions.add(growCock);
        }
        {
            TransformationOption removeCock = new TransformationOption();
            removeCock.ingredients.put(Item.FemDraft, 3);
            removeCock.addRequirement(RequirementShortcuts.rev(new BodyPartRequirement("cock")), "Has a penis");
            removeCock.option = "Eve: Remove her cock";
            removeCock.scene = "[Placeholder]<br/>Eve is pissed that you're bribing her to remove her cock.";
            removeCock.effect = (c, self, other) -> {
                other.body.removeAll("cock");
                return true;
            };
            transformationOptions.add(removeCock);
        }

    
    }

    @Override
    public void subVisitIntro(String choice) {
        if (npc.getAffection(player) > 0) {
            Global.gui().message("You find Eve hanging out in an alleyway");
            Global.gui().choose(this, "Games");
            Global.gui().choose(this, "Sparring");
            Global.gui().choose(this, "Sex");
            
            if(player.getPure(Attribute.Fetish) >= 3){
                Global.gui().choose(this,"SPECIAL");
            }
            if (Global.getPlayer().checkAddiction(AddictionType.MIND_CONTROL)) {        //TODO: Eve's Addiction will be freaky.
                Global.gui().choose(this, "Confront about Fetish");
            }
        } else if (Global.getPlayer().checkAddiction(AddictionType.MIND_CONTROL)) {
            Global.gui().message("Eve low-affection addiction intro");
            if (npc.getAttraction(player) < 15) {
                npc.gainAttraction(player, 2); 
                player.gainAttraction(npc, 2);
            } else {
                npc.gainAffection(player, 1);
                player.gainAffection(npc, 1);
                Global.gui().choose(this, "Games");
                Global.gui().choose(this, "Sparring");
                //Global.gui().choose(this, "Sex");
            }
            Global.gui().choose(this, "Get Fetished");
        } else if (npc.getAttraction(player) < 15) {
            Global.gui().message("[PLACEHOLDER] You find Eve in a secluded alleyway, groping herself.<br/><br/>"
                            + "<i>\"Unnhh, hey, " + player.getTrueName() + ".\"</i> She seems busy. "
                            + "<i>\"Fff-Fuck...oh yeah...so good...\"</i><br/><br/>"
                            + "You ask her if she'd like some help with that<br/><br/>"
                            + "<i>\"Unngh, FUCK off! Fuck...I'm softening up...\"</i><br/><br/> "
                                  + "She stomps in frustration and hits the wall with a fist.<br/><br/>"
                                  + "<i>\"Fuck! You ruined it. I'm outta here.\"</i>"
                                  + "<br/><br/>She leaves. You wonder if it was something you said...<br/>");
            npc.gainAttraction(player, 1);
            player.gainAttraction(npc, 1);
        } else {
            Global.gui().message("[PLACEHOLDER] You find Eve in a secluded alleyway, groping herself.<br/><br/>"
                            + "<i>\"Unnhh, hey, " + player.getTrueName() + ".\"</i> She seems busy. "
                            + "<i>\"Fff-Fuck...oh yeah...so good...\"</i><br/><br/>"
                            + "You ask her if she'd like some help with that<br/><br/>"
                            + "<i>\"Oh yeah...maybe. Trying to come with just my tits. Come here and play with my ass.\"</i><br/><br/> "
                                  + "You comply with the really odd request. You grope Eve's nice ass while she massages and tweaks her tits and nipples through her T-Shirt.<br/><br/>"
                                  + "<i>\"Ohshit...ohFUCK. Yeah! Almost! Yeah!\"</i><br/><br/>"
                                  + "With persistance, you manage to get Eve off. She thanks you casually and goes about her day. Always nice to be a helping hand.<br/><br/>");
            
            //TODO: Even should give the player something in terms of dialogue about herself like other characters do. Perhaps explaining herself a bit.
            npc.gainAffection(player, 1);
            player.gainAffection(npc, 1);
            Global.gui().choose(this, "Games");
            Global.gui().choose(this, "Sparring");
            //Global.gui().choose(this, "Sex");
        }
        Global.gui()
              .choose(this, "Leave");
    }

    @Override
    public void subVisit(String choice) {
        if (choice.equals("Get Fetished")) {
            if (npc.getAffection(player) == 0) {
                Global.gui().message("[Placeholder] Eve has some fun doing freaky, deviant things with her. You feel like she's a bit crazy to be into the freaky shit that she's into.");
            // The buttslut scene isn't fully written yet
            } else /* if (!player.hasStatus(Stsflag.buttsluttraining)) */ {
                Global.gui().message("[Placeholder] Eve Addiction Sate Scene.");
            } /*else {
               
            }*/
            Global.gui().choose(this, "Leave");
            Global.getPlayer().addict(null, AddictionType.MIND_CONTROL, npc, Addiction.MED_INCREASE);
            Global.getPlayer().getAddiction(AddictionType.MIND_CONTROL).ifPresent(Addiction::flagDaytime);
        }
        if (choice.equals("Sex")) {
            if (npc.getAffection(player) >= 8 && (!player.has(Trait.ticklemonster) || Global.random(2) == 1)) {
                Global.gui()
                      .message("[Placeholder] Eve Sex Scene - Can give Trait.");
                /*//GIVE TRAIT FOR SEX SCENE
                if (!player.has(Trait.ticklemonster)) {
                    Global.gui().message("<br/><br/><b>You've gotten better at finding sensitive spots when tickling nude opponents.</b>");
                    player.add(Trait.ticklemonster);
                    npc.getGrowth().addTrait(0, Trait.ticklemonster);
                }
                */
            } else {
                Global.gui().message("[Placeholder] Eve Sex Scene - Normal and unremarkable, but depends on how vulnerable your holes are.");

            }
            Global.gui().choose(this, "Leave");
            Daytime.train(player, npc, Attribute.Seduction);
            npc.gainAffection(player, 1);
            player.gainAffection(npc, 1);
        } else if (choice.equals("Games")) {
            if (npc.getAffection(player) >= 16 && (!player.has(Trait.spider) || Global.random(2) == 1)) {
                Global.gui().message("[Placeholder] Eve games scene - this one gives you a trait.");
                /*
                //Gain trait
                if (!player.has(Trait.spider)) {
                    Global.gui()
                          .message("<br/><br/><b>Mara has taught you to make the brilliant and insanely complex Spiderweb Trap.</b>");
                    player.add(Trait.spider);
                    npc.getGrowth().addTrait(0, Trait.spider);
                }
                */
            } else {
                Global.gui().message("[Placeholder] Eve games scene - normal version.");
            }
            Global.gui().choose(this, "Leave");
            Daytime.train(player, npc, Attribute.Cunning);
            npc.gainAffection(player, 1);
            player.gainAffection(npc, 1);
        } else if (choice.equals("Sparring")) {
            if (npc.getAffection(player) >= 12 && (!player.has(Trait.heeldrop) || Global.random(2) == 1)) {
                Global.gui().message("[Placeholder] Eve Sparring scene - Trait gain version.");
                
                /*
                if (!player.has(Trait.heeldrop)) {
                   // Global.gui().message("<br/><br/><b>You've experienced Mara's most painful technique and learned how to use it yourself.</b>");
                    player.add(Trait.heeldrop);
                    npc.getGrowth().addTrait(0, Trait.heeldrop);
                }
                */
            } else {
                Global.gui().message("[Placeholder] Eve Sparring scene - normal version.");

            }
            Global.gui().choose(this, "Leave");
            Daytime.train(player, npc, Attribute.Power);
            npc.gainAffection(player, 1);
            player.gainAffection(npc, 1);
            
        } else if (choice.equals("SPECIAL2")){
            Global.gui().message("[Placeholder] Functional scene for Eve.");
            /*
            if(!player.has(Trait.faefriend)){
                //Global.gui().message("");
                //player.add(Trait.faefriend);
            }
            */
            Global.gui().choose(this,"Leave");
            npc.gainAffection(player,1);
            player.gainAffection(npc,1);
        } else if (choice.equals("Leave")) {
            done(true);
        } else {

        }
    }
    
    @Override
    public Optional<String> getAddictionOption() {
        return Global.getPlayer().checkAddiction(AddictionType.MIND_CONTROL) ? 
                        Optional.of("Confront about Fetith") : Optional.empty();
    }
    
    private void choiceTraitBolster(){}

    @Override
    public void doPlayGamesWith() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void doHaveSex() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void doSparring() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void doAddictionScene() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void doGivePresent() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void doSpecialFunction1() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void doSpecialFunction2() {
        // TODO Auto-generated method stub
        
    }
}
