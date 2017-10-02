package nightgames.combat;

import nightgames.characters.Character;
import nightgames.pet.CharacterPet;

/**For Combat rewrite. Contains the essential data necessary to pass between combat phases without problems.*/
public class CombatData {

    protected String m_Name;                                        //The name of this phase.
    protected String m_Description;                                 //Description.
    protected nightgames.characters.Character m_Player1;            //Presumably the player, may also be an NPC.
    protected nightgames.characters.Character m_Player2;          
    protected nightgames.pet.CharacterPet m_Player1Pets;            //The pets that are currently out for player 1;
    protected nightgames.pet.CharacterPet m_Player2Pets;
    protected CombatPhase m_currPhase;                              //The current phase that this combat is one.
    protected int m_CurrRound;

    //TODO: PetCharacter and CharacterPet? Which is what? 

    protected int scorePlayer1;
    protected int scorePlayer2;


    /*
        public Character p1;
        public Character p2;
        public List<PetCharacter> otherCombatants;
        public Map<String, CombatantData> combatantData;
        public Optional<Character> winner;
        public CombatPhase phase;
        protected Skill p1act;
        protected Skill p2act;
        public Area location;
        private String message;
        private Position stance;
        public Character lastTalked;
        protected int timer;
        public Result state;
        private HashMap<String, String> images;
        boolean lastFailed = false;
        private CombatLog log;
        private boolean beingObserved;
        private int postCombatScenesSeen;
        private boolean wroteMessage;
        private boolean cloned;
        private List<CombatListener> listeners;
     */

    public boolean isComplete(){
        return false;
    } 

    public boolean hasPlayer(){
        return false;
    }

    public boolean isBeingObserved(){
        return false;
    }

    /**Default contructor. Used for making dummy Data.*/
    public CombatData(){
        
    }
    
    /**Custom Constructor for the purpose of reciving from CombatPhases.*/
    public CombatData(CombatData CD) {
        
    }
    

    
    


}
