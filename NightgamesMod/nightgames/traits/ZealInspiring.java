package nightgames.traits;

import nightgames.characters.Character;

public class ZealInspiring extends nightgames.characters.CharTrait {
    

    public ZealInspiring() {
        super("Zeal Inspriring", "", "", null);
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
