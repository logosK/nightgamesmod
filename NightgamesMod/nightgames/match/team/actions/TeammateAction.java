package nightgames.match.team.actions;

import nightgames.actions.Action;
import nightgames.actions.IMovement;
import nightgames.characters.Character;
import nightgames.global.Global;
import nightgames.match.team.TeamMatch;

public abstract class TeammateAction extends Action implements IMovement {

    private static final long serialVersionUID = -5629516196619447995L;
    protected final TeamMatch match;
    protected final Character self;
    
    TeammateAction(String name, TeamMatch match, Character self) {
        super(name);
        this.match = match;
        this.self = self;
    }

    abstract void effects();
    
    boolean additionalRequirements() {
        return true;
    }
    
    Character getCaptain() {
        return match.getTeamOf(self).captain;
    }
    
    @Override
    public final boolean usable(Character user) {
        return user == self && !match.isCaptain(user) && additionalRequirements();
    }

    @Override
    public final IMovement consider() {
        return this;
    }
    
    @Override
    public final IMovement execute(Character user) {
        if (self.human()) {
            Global.gui().message(describe());
        }
        effects();
        return this;
    }
}
