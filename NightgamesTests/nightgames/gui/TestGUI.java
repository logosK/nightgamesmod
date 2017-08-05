package nightgames.gui;

import java.io.File;
import java.util.Observable;
import java.util.Optional;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.match.Encounter;
import nightgames.skills.Skill;

public class TestGUI extends GUI {
    /**
     * 
     */
    private static final long serialVersionUID = 1739250786661411957L;

    public TestGUI() {
        
    }

    @Override public void setVisible(boolean visible) {
        // pass
    }

    // Don't use save dialog in tests
    @Override public Optional<File> askForSaveFile() {
        return Optional.empty();
    }

    @Override
    public Combat beginCombat(Character p1, Character p2) {
        combat = new Combat(p1, p2, p1.location());
        combat.addObserver(this);
        combat.setBeingObserved(true);
        return combat;
    }

    @Override
    public void clearText() {}

    @Override
    public void message(String text) {}

    @Override
    public void clearCommand() {}

    @Override
    public void addSkill(Combat com, Skill action, Character target) {}

    @Override
    public void next(Combat combat) {}

    @Override
    public void promptAmbush(Encounter enc, Character target) {}

    @Override
    public void update(Observable arg0, Object arg1) {}

    @Override
    public void endCombat() {
        combat = null;
    }
}
