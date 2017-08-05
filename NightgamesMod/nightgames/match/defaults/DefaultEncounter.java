package nightgames.match.defaults;

import java.util.Optional;

import nightgames.actions.Movement;
import nightgames.areas.Area;
import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.State;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.global.DebugFlags;
import nightgames.global.Encs;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.match.Encounter;
import nightgames.status.Enthralled;
import nightgames.status.Flatfooted;
import nightgames.status.Hypersensitive;
import nightgames.status.Status;
import nightgames.status.Stsflag;
import nightgames.trap.Spiderweb;
import nightgames.trap.Trap;

public class DefaultEncounter implements Encounter {

    protected Character p1;
    protected Character p2;
    
    protected boolean p1ff;
    protected boolean p2ff;
    
    protected transient Optional<String> p1Guaranteed;
    protected transient Optional<String> p2Guaranteed;
    protected Area location;
    protected transient Combat fight;
    protected int checkin;
    protected int fightTime;

    public DefaultEncounter(Character first, Character second, Area location) {
        this.location = location;
        p1 = first;
        p2 = second;
        checkin = 0;
        fight = null;
        p1Guaranteed = Optional.empty();
        p2Guaranteed = Optional.empty();
        checkEnthrall(p1, p2);
        checkEnthrall(p2, p1);
    }

    protected void checkEnthrall(Character p1, Character p2) {
        Status enthrall = p1.getStatus(Stsflag.enthralled);
        if (enthrall != null) {
            if (((Enthralled) enthrall).master != p2) {
                p1.removelist.add(enthrall);
                p1.addNonCombat(new Flatfooted(p1, 2));
                p1.addNonCombat(new Hypersensitive(p1));
                if (p1.human()) {
                    Global.gui()
                          .message("At " + p2.getName() + "'s interruption, you break free from the"
                                          + " succubus' hold on your mind. However, the shock all but"
                                          + " short-circuits your brain; you "
                                          + " collapse to the floor, feeling helpless and"
                                          + " strangely oversensitive");
                } else if (p2.human()) {
                    Global.gui().message(String.format(
                                    "%s doesn't appear to notice you at first, but when you wave your hand close to %s face %s "
                                    + "eyes open wide and %s immediately drops to the floor. Although the display leaves you "
                                    + "somewhat worried about %s health, %s is still in a very vulnerable position and you never "
                                    + "were one to let an opportunity pass you by.",
                                    p1.getName(), p1.possessiveAdjective(), p1.possessiveAdjective(),
                                    p1.pronoun(),
                                    p1.possessiveAdjective(), p1.pronoun()));
                }
            }
        }
    }

    /**
     * Checks for and runs any scenarios that arise from two Characters encountering each other. 
     * Returns true if something has come up that prevents them from being presented with the usual
     * campus Actions.
     */
    public boolean spotCheck() {
        if (p1.eligible(p2) && p2.eligible(p1)) {
            eligibleSpotCheck();
            return true;
        } else {
            ineligibleSpotCheck();
            return false;
        }
    }
    
    private void eligibleSpotCheck() {
        if (p1.state == State.shower) {
            p2.showerScene(p1, this);
            return;
        } else if (p2.state == State.shower) {
            p1.showerScene(p2, this);
            return;
        } else if (p1.state == State.webbed) {
            spider(p2, p1);
            return;
        } else if (p2.state == State.webbed) {
            spider(p1, p2);
            return;
        } else if (p1.state == State.crafting || p1.state == State.searching) {
            p2.spy(p1, this);
            return;
        } else if (p2.state == State.crafting || p2.state == State.searching) {
            p1.spy(p2, this);
            return;
        } else if (p1.state == State.masturbating) {
            caught(p2, p1);
            return;
        } else if (p2.state == State.masturbating) {
            caught(p1, p2);
            return;
        }
        
        // We need to run both vision checks no matter what, and they have no
        // side effects besides.
        boolean p2_sees_p1 = p2.spotCheck(p1);
        boolean p1_sees_p2 = p1.spotCheck(p1);
        
        if (p2_sees_p1 && p1_sees_p2) {
            p1.faceOff(p2, this);
            p2.faceOff(p1, this);
        } else if (p2_sees_p1) {
            p2.spy(p1, this);
        } else if (p1_sees_p2) {
            p1.spy(p2,  this);
        } else {
            // Ships passing in the night :(
            location.endEncounter();
        }
    }
    
    private void ineligibleSpotCheck() {
        // We can skip a lot of flavor lines if there aren't any humans around
        if (p1.human() || p2.human()) {
            Character human = p1.human() ? p1 : p2;
            Character npc = p1 != human ? p1 : p2;
            Character masturbating =
                            p1.state == State.masturbating ? p1 :
                                (p2.state == State.masturbating ? p2 : null);
            
            if (masturbating != null) {
                if (human == masturbating) {
                    ineligibleHumanCaughtMasturbatingBy(npc);
                } else {
                    ineligibleNpcCaughtMasturbating(npc);
                }
            } else if (p1 == human && p1.eligible(p2)) {
                Global.gui().message("You encounter " + p2.getName() + ", but you still haven't recovered from your last fight.");
            } else if (p1 == human) {
                Global.gui().message(String.format(
                                "You find %s still naked from your last encounter, but %s's not fair game again until %s replaces %s clothes.",
                                p2.getName(), p2.pronoun(), p2.pronoun(), p2.possessiveAdjective()));
            } else if (!p1.eligible(p2)) {
                Global.gui().message("You encounter " + p1.getName() + ", but you still haven't recovered from your last fight.");
            } else {
                Global.gui().message(String.format(
                                "You find %s still naked from your last encounter, but %s's not fair game again until %s replaces %s clothes.",
                                p1.getName(), p1.pronoun(), p1.pronoun(), p1.possessiveAdjective()));
            }
        }
        location.endEncounter();
    }
    
    private void ineligibleHumanCaughtMasturbatingBy(Character npc) {
        Global.gui().message(String.format(
                        "%s catches you masturbating, but fortunately %s's not yet allowed to attack you, so %s just "
                        + "watches you pleasure yourself with an amused grin.",
                        npc.getName(), npc.pronoun(), npc.pronoun()));
    }
    
    private void ineligibleNpcCaughtMasturbating(Character npc) {
        Global.gui().message(String.format(
                        "You stumble onto %s with %s hand between %s legs, masturbating. Since you just fought you still can't touch %s, so "
                        + "you just watch the show until %s orgasms.",
                        npc.getName(), npc.possessiveAdjective(), npc.possessiveAdjective(), npc.directObject(),
                        npc.pronoun()));
    }

    /**
     * @param p The Character making the decision.
     * @param fight Whether the Character wishes to fight (true) or flee (false).
     * @param guaranteed Whether the Character's option is guaranteed to work. If so, the provided
     * String is messaged to the Character.
     */
    protected void fightOrFlight(Character p, boolean fight, Optional<String> guaranteed) {
        if (p == p1) {
            p1ff = fight;
            p1Guaranteed = guaranteed;
            checkin++;
        } else {
            p2ff = fight;
            p2Guaranteed = guaranteed;
            checkin++;
        }
        if (checkin >= 2) {
            doFightOrFlight();
        }
    }
    
    private void doFightOrFlight() {
        if (p1ff && p2ff) {
            startFight();
        } else if (p1ff) {
            fightOrFlee(p1, p2);
        } else if (p2ff) {
            fightOrFlee(p2, p1);
        } else {
            bothFlee();
        }
    }
    
    private void startFight() {
        startFightTimer();
        if (p1.human() || p2.human()) {
            this.fight = Global.gui().beginCombat(p1, p2);
        } else {
            this.fight = new Combat(p1, p2, location);
        }
    }
    
    // One Character wishes to Fight while the other attempts to flee.
    private void fightOrFlee(Character fighter, Character fleer) {
        Optional<String> fighterGuaranteed = (fighter == p1) ? p1Guaranteed : p2Guaranteed;
        Optional<String> fleerGuaranteed = (fleer == p1) ? p1Guaranteed : p2Guaranteed;
        
        // Fighter wins automatically
        if (fighterGuaranteed.isPresent() && !fleerGuaranteed.isPresent()) {
            if (fighter.human() || fleer.human()) {
                Global.gui().message(fighterGuaranteed.get());
            }
            startFightTimer();
            this.fight = Global.gui().beginCombat(fighter, fleer);
            return;
        }

        // Fleer wins automatically
        if (fleerGuaranteed.isPresent()) {
            if (fighter.human() || fleer.human()) {
                Global.gui().message(fleerGuaranteed.get());
            }
            p2.flee(location);
            return;
        }

        // Roll to see who's will triumphs
        if (rollFightVsFlee(fighter, fleer)) {
            if (fighter.human()) {
                Global.gui().message(fleer.getName() + " dashes away before you can move.");
            }
            fleer.flee(location);
        } else {
            startFightTimer();
            if (fighter.human() || fleer.human()) {
                if (fighter.human()) {
                    Global.gui().message(String.format(
                                    "%s tries to run, but you stay right on %s heels and catch %s.",
                                    fleer.getName(), fleer.possessiveAdjective(), fleer.directObject()));
                } else {
                    Global.gui().message(String.format(
                                    "You quickly try to escape, but %s is quicker. %s corners you and attacks.",
                                    fighter.getName(), Global.capitalizeFirstLetter(fighter.pronoun())));
                }
                this.fight = Global.gui()
                                   .beginCombat(fighter, fleer);
            } else {
                this.fight = new Combat(fighter, fleer, location);
            }
        }
    }
    
    /** Weights a roll with the fighter and fleers stats to determine who prevails. Returns
     * true if the fleer escapes, false otherwise.
     */
    private boolean rollFightVsFlee(Character fighter, Character fleer) {
        return fleer.check(Attribute.Speed, 10 + fighter.get(Attribute.Speed) + (fighter.has(Trait.sprinter) ? 5 : 0)
                        + (fleer.has(Trait.sprinter) ? -5 : 0));
    }

    private void startFightTimer() {
        fightTime = 2;
    }
    
    private void bothFlee() {
        boolean humanPresent = p1.human() || p2.human();
        if (p1Guaranteed.isPresent()) {
            if (humanPresent) {
                Global.gui().message(p1Guaranteed.get());
            }
            p1.flee(location);
        } else if (p2Guaranteed.isPresent()) {
            if (humanPresent) {
                Global.gui().message(p2Guaranteed.get());
            }
            p2.flee(location);
        } else if (p1.get(Attribute.Speed) + Global.random(10) >= p2.get(Attribute.Speed) + Global.random(10)) {
            if (p2.human()) {
                Global.gui()
                      .message(p1.getName() + " dashes away before you can move.");
            }
            p1.flee(location);
        } else {
            if (p1.human()) {
                Global.gui()
                      .message(p2.getName() + " dashes away before you can move.");
            }
            p2.flee(location);
        }
    }

    protected void ambush(Character attacker, Character target) {
        startFightTimer();
        target.addNonCombat(new Flatfooted(target, 3));
        if (p1.human() || p2.human()) {
            fight = Global.gui().beginCombat(attacker, target, 0);
            Global.gui().message(Global.format("{self:SUBJECT-ACTION:catch|catches} {other:name-do} by surprise and {self:action:attack|attacks}!", attacker, target));
        } else {
            fight = new Combat(attacker, target, location, 0);
        }
    }

    protected void showerambush(Character attacker, Character target) {
        startFightTimer();
        
        if (location.id() == Movement.shower) {
            showerAmbush(attacker, target);
        } else if (location.id() == Movement.pool) {
            poolAmbush(attacker, target);
        }
        
        if (p1.human() || p2.human()) {
            fight = Global.gui().beginCombat(p1, p2, 1);
        } else {
            fight = new Combat(p1, p2, location, 0);
        }
        
        target.add(fight, new Flatfooted(target, 4));
    }
    
    private void showerAmbush(Character attacker, Character target) {
        if (target.human()) {
            Global.gui().message(String.format(
                            "You aren't in the shower long before you realize you're not alone. %s %s has the drop on you.",
                            getShowerGrabLine(target), attacker.getName()));
        } else {
            Global.gui().message(String.format(
                        "You stealthily walk up behind %s, enjoying the view of %s wet naked body. When you pinch %s smooth butt, "
                                        + "%s jumps and lets out a surprised yelp. Before %s can recover from %s surprise, you pounce!",
                                        target.getName(), target.possessiveAdjective(), target.possessiveAdjective(),
                                        target.pronoun(), target.pronoun(), target.possessiveAdjective()));
        }
    }
    
    private String getShowerGrabLine(Character target) {
        if (target.hasDick()) {
            return "Before you can turn around, a soft hand grabs your exposed penis.";
        } else if (target.hasBreasts()) {
            return "Before you can turn around, hands reach around to cup your breasts.";
        } else {
            return "Before you can turn around you feel someone grab a handful of your ass.";
        }
    }
    
    private void poolAmbush(Character attacker, Character target) {
        if (target.human()) {
            Global.gui().message(String.format(
                            "The relaxing water causes you to lower your guard a bit, so you don't notice %s until %s's standing over you. "
                            + "There's no chance to escape; you'll have to face %s nude.",
                            attacker.getName(), attacker.pronoun(), attacker.directObject()));
        } else {
            String admireLine = target.hasBreasts() ?
                            String.format("You crouch by the edge of the jacuzzi for a few seconds and just admire %s nude body with %s breasts "
                                            + "just above the surface.", target.possessiveAdjective(), target.possessiveAdjective()) :
                            String.format("You crouch by the edge of the jacuzzi and just admire % nude body for a few seconds.",
                                            target.possessiveAdjective());
            Global.gui().message(String.format(
                            "You creep up to the jacuzzi where %s is soaking comfortably. As you get close, you notice that %s eyes are "
                                  + "closed and %s may well be sleeping. %s You lean down and give %s a light kiss on the forehead to wake %s "
                                  + "up. %s opens her eyes and swears under %s breath when %s sees you. %s scrambles out of the tub, but you "
                                  + "easily catch %s before %s can get away.",
                                  target.getName(), target.possessiveAdjective(),
                                  target.pronoun(), admireLine, target.directObject(), target.directObject(),
                                  Global.capitalizeFirstLetter(target.pronoun()), target.possessiveAdjective(), target.possessiveAdjective(),
                                  target.pronoun(), Global.capitalizeFirstLetter(target.pronoun()),
                                  target.possessiveAdjective(), target.pronoun()));
        }
    }

    protected void caught(Character attacker, Character target) {
        attacker.gainXP(attacker.getVictoryXP(target));
        target.gainXP(target.getDefeatXP(attacker));
        if (target.human()) {
            Global.gui()
                  .message("You jerk off frantically, trying to finish as fast as possible. Just as you feel the familiar sensation of imminent orgasm, you're grabbed from behind. "
                                  + "You freeze, cock still in hand. As you turn your head to look at your attacker, "
                                  + attacker.getName()
                                  + " kisses you on the lips and rubs the head of your penis with her "
                                  + "palm. You were so close to the edge that just you cum instantly.");
            if (!target.mostlyNude()) {
                Global.gui()
                      .message("You groan in resignation and reluctantly strip off your clothes and hand them over.");
            }
        } else if (attacker.human()) {
            Global.gui()
                  .message("You spot " + target.getName()
                                  + " leaning against the wall with her hand working excitedly between her legs. She is mostly, but not completely successful at "
                                  + "stifling her moans. She hasn't noticed you yet, and as best as you can judge, she's pretty close to the end. It'll be an easy victory for you as long as you work fast. "
                                  + "You sneak up and hug her from behind while kissing the nape of her neck. She moans and shudders in your arms, but doesn't stop fingering herself. She probably realizes "
                                  + "she has no chance of winning even if she fights back. You help her along by licking her neck and fondling her breasts as she hits her climax.");
        }
        if (!target.mostlyNude()) {
            attacker.gain(target.getTrophy());
        }
        target.nudify();
        target.defeated(attacker);
        target.getArousal()
              .empty();
        attacker.tempt(20);
        Global.getMatch()
              .score(attacker, target.has(Trait.event) ? 5 : 1);
        attacker.state = State.ready;
        target.state = State.ready;
        location.endEncounter();
    }

    protected void spider(Character attacker, Character target) {
        attacker.gainXP(attacker.getVictoryXP(target));
        target.gainXP(target.getDefeatXP(attacker));
        Spiderweb.onSpiderwebDefeat(attacker, target, (Spiderweb) location.get(Spiderweb.class));
    }

    public void intrude(Character intruder, Character assist) {
        fight.intervene(intruder, assist);
    }

    public boolean battle() {
        fightTime--;
        if (fightTime <= 0 && !fight.isEnded()) {
            fight.go();
            return true;
        } else {
            return false;
        }
    }

    public Combat getCombat() {
        return fight;
    }

    public Character getPlayer(int i) {
        if (i == 1) {
            return p1;
        } else {
            return p2;
        }
    }

    protected void steal(Character thief, Character target) {
        if (thief.human()) {
            Global.gui()
                  .message("You quietly swipe " + target.getName()
                                  + "'s clothes while she's occupied. It's a little underhanded, but you can still turn them in for cash just as if you defeated her.");
        }
        thief.gain(target.getTrophy());
        target.nudify();
        target.state = State.lostclothes;
        location.endEncounter();
    }

    public void trap(Character opportunist, Character target, Trap trap) {
        if (opportunist.human()) {
            Global.gui()
                  .message("You leap out of cover and catch " + target.getName() + " by surprise.");
        } else if (target.human()) {
            Global.gui()
                  .message("Before you have a chance to recover, " + opportunist.getName() + " pounces on you.");
        }
        trap.capitalize(opportunist, target, this);
    }

    public void engage(Combat fight) {
        this.fight = fight;
        if (fight.p1.human() || fight.p2.human()) {
            Global.gui().watchCombat(fight);
        }
    }

    public void parse(Encs choice, Character self, Character target) {
        parse(choice, self, target, null);
    }

    public void parse(Encs choice, Character self, Character target, Trap trap) {
        if (Global.isDebugOn(DebugFlags.DEBUG_SCENE)) {
            System.out.println(Global.format("{self:true-name} uses %s (%s) on {other:true-name}", self, target, choice, trap));
        }
        switch (choice) {
            case ambush:
                ambush(self, target);
                break;
            case capitalize:
                trap(self, target, trap);
                break;
            case showerattack:
                showerambush(self, target);
                break;
            case aphrodisiactrick:
                aphrodisiactrick(self, target);
                break;
            case stealclothes:
                steal(Global.getPlayer(), target);
                break;
            case fight:
                fightOrFlight(self, true, Optional.empty());
                break;
            case flee:
                fightOrFlight(self, false, Optional.empty());
                break;
            case fleehidden:
                checkin += 2;
                fightOrFlight(self, false, Optional.of(fleeHiddenMessage(self, target)));
                break;
            case smoke:
                fightOrFlight(self, false, Optional.of(smokeMessage(self)));
                self.consume(Item.SmokeBomb, 1);
                break;
            default:
                return;
        }
    }
    
    private String smokeMessage(Character c) {
        return String.format("%s a smoke bomb and %s.", 
                        Global.capitalizeFirstLetter(c.subjectAction("drop", "drops"))
                        , c.action("disappear", "disappears"));
    }

    private String fleeHiddenMessage(Character c, Character other) {
        return Global.format("{self:SUBJECT-ACTION:flee} before {other:subject-action:can} notice {self:direct-object}.", c, other);
    }

    @Override
    public boolean checkIntrude(Character c) {
        return fight != null && !c.equals(p1) && !c.equals(p2);
    }

    @Override
    public void watch() {
        Global.gui().watchCombat(fight);
        fight.go();
    }

    /** Causes the attacker to be defeated, sending the given message after XP has been gained
     * but before the rest of the defeat logic.
     * 
     * Note: this code seems like it might crop up elsewhere; it has already done so once
     * in the Spiderweb class. If any more do it's worth extracting this to somewhere
     * more general, but at the moment I'm not sure where that might be.
     */
    private void encounterDefeat(Character attacker, Character target, String message) {
        attacker.gainXP(attacker.getVictoryXP(target));
        target.gainXP(target.getDefeatXP(attacker));
        
        if (message != null) {
            Global.gui().message(message);
        }
        
        if (!target.mostlyNude()) {
            attacker.gain(target.getTrophy());
        }
        target.nudify();
        target.defeated(attacker);
        target.getArousal()
              .empty();
        attacker.tempt(20);
        Global.getMatch()
              .score(attacker, target.has(Trait.event) ? 5 : 1);
        attacker.state = State.ready;
        target.state = State.ready;
        location.endEncounter();
    }

    protected void aphrodisiactrick(Character attacker, Character target) {
        attacker.consume(Item.Aphrodisiac, 1);
        encounterDefeat(attacker, target, getAphrodisiacTrickMessage(attacker, target));
    }
    
    /** Returns null if no message is to be sent */
    private String getAphrodisiacTrickMessage(Character attacker, Character target) {
        if (location.id() == Movement.shower) {
            return getAphrodisiacTrickShowerMessage(attacker, target);
        } else if (location.id() == Movement.pool) {
            return getAphrodisiacTrickPoolMessage(attacker, target);
        }
        return null;
    }
    
    private String getAphrodisiacTrickShowerMessage(Character attacker, Character target) {
        if (attacker.human()) {
            if (target.hasPussy() && !target.hasDick()) {
                return Global.format("You empty the bottle of aphrodisiac onto the shower floor, letting the heat from the shower turn it to steam. You watch "
                            + "{other:name} and wait for a reaction. Just when you start to worry that it was all washed down the drain, you see {other:possessive} "
                            + "hand slip between {other:possessive} legs. {other:POSSESSIVE} fingers go to work pleasuring {other:reflexive} and soon {other:pronoun}'s "
                            + "utterly engrossed in {other:possessive} masturbation, allowing you to safely get closer without being noticed. {other:NAME}'s "
                            + "completely unreserved in {other:possessive} assumption of solitude and you feel a voyeuristic thrill at the show. You can't "
                            + "remain an observer, however. For this to count as a victory you need to be in physical contact with {other:direct-object} "
                            + "when {other:pronoun} orgasms. When you judge that {other:pronoun}'s in the home stretch, you embrace {other:direct-object} from "
                            + "behind and kiss {other:direct-object} neck. {other:NAME} freezes in surprise and you move your hand between {other:possessive} "
                            + "legs to replace {other:possessive} own. {other:POSSESSIVE} pussy is hot, wet, and trembling with need. You stick two fingers into "
                            + "{other:direct-object} and rub {other:possessive} clit with your thumb. {other:NAME} climaxes almost immediately. You give "
                            + "{other:direct-object} a kiss on the cheek and leave while {other:pronoun}'s still too dazed to realize what happened. You're "
                            + "feeling pretty horny, but after a show like that it's hardly surprising.\n",
                            attacker, target);
            } else if (target.hasDick()) {
                return Global.format("You empty the bottle of aphrodisiac onto the shower floor, letting the heat from the shower turn it to steam. You watch "
                            + "{other:name} and wait for a reaction. Just when you start to worry that it was all washed down the drain, you see {other:possessive} "
                            + "hand slip down to encircle {other:possessive} cock. {other:POSSESSIVE} hand builds a steady rhythm pleasuring {other:reflexive} "
                            + "and soon {other:pronoun}'s utterly engrossed in {other:possessive} masturbation, allowing you to safely get closer without being "
                            + "noticed. {other:NAME}'s completely unreserved in {other:possessive} assumption of solitude and you feel a voyeuristic thrill at "
                            + "the show. You can't remain an observer, however. For this to count as a victory you need to be in physical contact with "
                            + "{other:direct-object} when {other:pronoun} orgasms. When you judge that {other:pronoun}'s in the home stretch, you embrace "
                            + "{other:direct-object} from behind and kiss {other:possessive} neck. {other:NAME} freezes in surprise and you move your hand "
                            + "between {other:possessive} legs to replace {other:possessive} own. {other:POSSESSIVE} dick is hard, slick, and trembling with "
                            + "need. You begin stroking it rapidly and {other:pronoun} climaxes almost immediately. You give {other:direct-object} a kiss on "
                            + "the cheek and leave while {other:pronoun}'s still too dazed to realize what happened. You're feeling pretty horny, but after a show "
                            + "like that it's hardly surprising.\n",
                            attacker, target);
            } else {
                return Global.format("You empty the bottle of aphrodisiac onto the shower floor, letting the heat from the shower turn it to steam. You watch "
                            + "{other:name} and wait for a reaction. Just when you start to worry that it was all washed down the drain, you see {other:possessive} "
                            + "hand reach behind {other:reflexive}. {other:POSSESSIVE} fingers go to work pleasuring {other:possessive} ass and soon "
                            + "{other:pronoun}'s utterly engrossed in {other:possessive} masturbation, allowing you to safely get closer without being noticed. "
                            + "{other:NAME}'s completely unreserved in {other:possessive} assumption of solitude and you feel a voyeuristic thrill at the show. "
                            + "You can't remain an observer, however. For this to count as a victory you need to be in physical contact with {other:direct-object} "
                            + "when {other:pronoun} orgasms. When you judge that {other:pronoun}'s in the home stretch, you embrace {other:direct-object} from "
                            + "behind and kiss {other:direct-object} neck. {other:NAME} freezes in surprise and you move your hand between {other:possessive} "
                            + "cheeks to replace {other:possessive} own. {other:POSSESSIVE} asshole is hot, tight, and pulsing with need. You stick two fingers into "
                            + "{other:direct-object}, curling and probing. {other:NAME} climaxes almost immediately. You give {other:direct-object} a kiss "
                            + "and leave while {other:pronoun}'s still too dazed to realize what happened. You're feeling pretty horny, but after a show like that "
                            + "it's hardly surprising.\n",
                            attacker, target);
            }
        } else if (target.human()) {
            if (target.hasPussy() && !target.hasDick()) {
                return Global.format("The hot shower takes your fatigue away, but you can't seem to calm down. Your nipples are almost painfully hard. You need to deal with "
                            + "this while you have the chance. You rub your labia rapidly, hoping to finish before someone stumbles onto you. Right before you cum, you "
                            + "are suddenly grabbed from behind and spun around. {other:NAME} has caught you at your most vulnerable and, based on {other:possessive} "
                            + "expression, may have been waiting for this moment. {other:PRONOUN} kisses you and pushes two fingers into your swollen pussy. In just a "
                            + "few strokes you cum so hard it's almost painful.\n",
                                target, attacker);
            } else if (target.hasDick()) {
                return Global.format("The hot shower takes your fatigue away, but you can't seem to calm down. Your cock is almost painfully hard. You need to deal with "
                            + "this while you have the chance. You jerk off quickly, hoping to finish before someone stumbles onto you. Right before you cum, you are "
                            + "suddenly grabbed from behind and spun around. {other:NAME} has caught you at your most vulnerable and, based on {other:possessive} "
                            + "expression, may have been waiting for this moment. {other:PRONOUN} kisses you and firmly grasps your twitching dick. In just a few "
                            + "strokes you cum so hard it's almost painful.\n",
                            target, attacker);
            } else {
                return Global.format("The hot shower takes your fatigue away, but you can't seem to calm down. Your nipples are almost painfully hard. You need to deal with "
                            + "this while you have the chance. You rub your asshole rapidly, hoping to finish before someone stumbles onto you. Right before you cum, you "
                            + "are suddenly grabbed from behind and spun around. {other:NAME} has caught you at your most vulnerable and, based on {other:possessive} "
                            + "expression, may have been waiting for this moment. {other:PRONOUN} kisses you and pushes two fingers into your behind. In just a "
                            + "few strokes you cum so hard it's almost painful.\n", 
                            target, attacker);
            }
        }
        return null;
    }
    
    private String getAphrodisiacTrickPoolMessage(Character attacker, Character target) {
        if (attacker.human()) {
            if (target.hasPussy() && !target.hasDick()) {
                return Global.format("You sneak up to the jacuzzi and empty the aphrodisiac into the water without {other:name} noticing. You slip away and find a hiding "
                            + "spot. In a couple minutes, you notice {other:direct-object} stir. {other:PRONOUN} glances around, but fails to see you and closes "
                            + "{other:possessive} eyes and relaxes again. There's something different now, though, and {other:possessive} soft moan confirms it. "
                            + "You grin and quietly approach for a second time. You can see {other:possessive} hand moving under the surface of the water as "
                            + "{other:pronoun} enjoys {other:reflexive} tremendously. {other:POSSESSIVE} moans rise in volume and frequency. Now's the right moment. "
                            + "You lean down and kiss {other:direct-object} on the lips. {other:POSSESSIVE} masturbation stops immediately, but you reach underwater "
                            + "and finger {other:direct-object} to orgasm. When {other:name} recovers, {other:pronoun} glares at you for your unsportsmanlike trick, "
                            + "but {other:pronoun} can't manage to get really mad in the afterglow of {other:possessive} climax. You're pretty turned on by the "
                            + "encounter, but you can chalk this up as a win.\n",
                            attacker, target);
            } else if (target.hasDick()) {
                return Global.format("You sneak up to the jacuzzi and empty the aphrodisiac into the water without {other:name} noticing. You slip away and find a hiding "
                            + "spot. In a couple minutes, you notice {other:direct-object} stir. {other:PRONOUN} glances around, but fails to see you and closes "
                            + "{other:possessive} eyes and relaxes again. There's something different now, though, and {other:possessive} soft moan confirms it. "
                            + "You grin and quietly approach for a second time. You can see {other:possessive} hand moving under the surface of the water as "
                            + "{other:pronoun} enjoys {other:reflexive} tremendously. {other:POSSESSIVE} moans rise in volume and frequency. Now's the right moment. "
                            + "You lean down and kiss {other:direct-object} on the lips. {other:POSSESSIVE} masturbation stops immediately, but you reach underwater "
                            + "and stroke {other:direct-object} to orgasm. When {other:name} recovers, {other:pronoun} glares at you for your unsportsmanlike trick, "
                            + "but {other:pronoun} can't manage to get really mad in the afterglow of {other:possessive} climax. You're pretty turned on by the "
                            + "encounter, but you can chalk this up as a win.\n",
                            attacker, target);
            } else {
                return Global.format("You sneak up to the jacuzzi and empty the aphrodisiac into the water without {other:name} noticing. You slip away and find a hiding "
                            + "spot. In a couple minutes, you notice {other:direct-object} stir. {other:PRONOUN} glances around, but fails to see you. {other:name} "
                            + "shifts {other:possessive} legs to the side and a hand drifts behind {other:possessive} back.  You can barely make out muscles moving "
                            + "in {other:possessive} forearm, and a soft moan confirms your suspicions. You grin and quietly approach for a second time. You can see "
                            + "{other:possessive} hand moving under the surface of the water as {other:pronoun} enjoys {other:reflexive} tremendously. {other:POSSESSIVE} "
                            + "moans rise in volume and frequency. Now's the right moment. You lean down and kiss {other:direct-object} on the lips. {other:POSSESSIVE} "
                            + "masturbation stops immediately, but you pull {other:direct-object} half out of the water and face-down onto the tile.  You plunge your "
                            + "fingers into {other:name}'s upturned bottom and finger {other:direct-object} to a shuddering orgasm. When {other:name} recovers "
                            + "{other:pronoun} glares at you for your unsportsmanlike trick, but {other:pronoun} can't manage to get really mad in the afterglow of "
                            + "{other:possessive} climax. You're pretty turned on by the encounter, but you can chalk this up as a win.\n",
                            attacker, target);
            }
        } else if (target.human()) {
            if (target.hasPussy() && !target.hasDick()) {
                return Global.format("As you relax in the jacuzzi, you start to feel extremely horny. Your hand is between your legs before you're even aware of it. "
                                + "You rub yourself underwater and you're just about ready to cum when you hear nearby footsteps. Shit, you'd almost completely forgotten "
                                + "you were in the middle of a match! The footsteps are {other:name}'s, who sits down at the edge of the jacuzzi while smiling confidently. "
                                + "You look for a way to escape, but it's hopeless. You were so close to finishing you desperately need to cum. {other:NAME} seems to be "
                                + "thinking the same thing, as {other:pronoun} dips {other:possessive} bare feet into the water and grinds {other:possessive} heel into "
                                + "your vulva. You clutch {other:possessive} leg and buck helplessly against the back of {other:possessive} foot, cumming in seconds.\n",
                                target, attacker);
            } else if (target.hasDick()) {
                return Global.format("As you relax in the jacuzzi, you start to feel extremely horny. Your cock is in your hand before you're even aware of it. You stroke "
                            + "yourself off underwater and you're just about ready to cum when you hear nearby footsteps. Shit, you'd almost completely forgotten you "
                            + "were in the middle of a match! The footsteps are {other:name}'s, who sits down at the edge of the jacuzzi while smiling confidently. You "
                            + "look for a way to escape, but it's hopeless. You were so close to finishing you desperately need to cum. {other:NAME} seems to be thinking "
                            + "the same thing, as {other:pronoun} dips {other:possessive} bare feet into the water and grasps your penis between them. {other:PRONOUN} "
                            + "pumps you with {other:possessive} feet and you shoot your load into the water in seconds.\n",
                            target, attacker);
            } else {
                return Global.format("As you relax in the jacuzzi, you start to feel extremely horny. Your hand is between your cheeks before you're even aware of it. "
                                + "You play with your rear underwater and you're just about ready to cum when you hear nearby footsteps. Shit, you'd almost completely forgotten "
                                + "you were in the middle of a match! The footsteps are {other:name}'s, who sits down at the edge of the jacuzzi while smiling confidently. "
                                + "You look for a way to escape, but it's hopeless. You were so close to finishing you desperately need to cum. {other:NAME} seems to be "
                                + "thinking the same thing, as {other:pronoun} pulls you roughly out of the jacuzzi and plunges two fingers into your upturned ass. You "
                                + "writhe helplessly on the smooth tile under {other:name}'s ministrations, cumming in seconds.\n",
                                target, attacker);
            }
        }
        return null;
    }
}
