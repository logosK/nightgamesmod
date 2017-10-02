package nightgames.traits;

import nightgames.characters.CharTrait;
import nightgames.characters.Character;

public class FastLearner extends CharTrait {

    public FastLearner(String n, String ds, String dl, CharTrait p) {
        super("Fast Learner", "Improved experience gain", "You are a fast learner - you get extra experience every time you gain experience.", null);
    }

    @Override
    public boolean checkRequirements(Character user) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void describe(Character c, StringBuilder b) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean checkIfActivates(Character user, Character opponent) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void sayActivation(Character user, Character opponent) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void doActivation(Character user, Character opponent) {
        // TODO Auto-generated method stub
        
    }
    
    
    
    
}
