package nightgames.traits;

import nightgames.characters.CharTrait;
import nightgames.characters.Character;

public class Energetic extends CharTrait {

    public Energetic(String n, String ds, String dl, CharTrait p) {
        super("Energetic", "Regain stamina rapidly.", "You are an energetic person - You regain lost stamina rapidly.", null);
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
