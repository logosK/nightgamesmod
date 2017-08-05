package nightgames.match.team;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.CombatListener;
import nightgames.global.Global;
import nightgames.match.team.TeamMatch.Team;
import nightgames.pet.CharacterPet;
import nightgames.pet.Pet;
import nightgames.pet.PetCharacter;

public class TeamCombatListener extends CombatListener {

    private Map<Pet, Character> owners;

    public TeamCombatListener(Combat c) {
        super(c);
        owners = new HashMap<>();
    }

    @Override
    public void preStart() {
        TeamMatch match = (TeamMatch) Global.getMatch();
        Team team1 = match.teamOf.get(c.p1);
        Team team2 = match.teamOf.get(c.p2);
        assert team1.captain == c.p1;
        assert team2.captain == c.p2;
        team1.members.stream()
                     .filter(m -> m != c.p1)
                     .forEach(m -> c.addPet(c.p1, buildPet(c.p1, m)));
        team2.members.stream()
                     .filter(m -> m != c.p2)
                     .forEach(m -> c.addPet(c.p2, buildPet(c.p2, m)));
    }

    @Override
    public void postEnd(Optional<Character> winner) {
        TeamMatch match = (TeamMatch) Global.getMatch();
        if (winner.isPresent()) {
            match.score(winner.get(), 3,
                            Optional.of(" for defeating " + match.teamOf.get(c.getOpponent(winner.get())).name));
        } else {
            match.score(c.p1, 1, Optional.of(" for drawing"));
            match.score(c.p2, 1, Optional.of(" for drawing"));
        }
        Team team1 = match.teamOf.get(c.p1);
        Team team2 = match.teamOf.get(c.p2);
        team1.haveMercy(team2, 3);
        team2.haveMercy(team1, 3);
    }

    @Override
    public void prePetBattle(PetCharacter p1, PetCharacter p2) {
        c.write("<b><u>Error: Tried to do pet battle in team match</u></b>");
        System.err.println("Error: Tried to do pet battle in team match");
        Thread.dumpStack();
    }
/*    
    @Override
    public void onPetRemoved(PetCharacter self) {
        if (owners.containsKey(self)) {
            c.addPet(owners.get(self), self);
        }
    }
*/    
    private PetCharacter buildPet(Character captain, Character member) {
        int power = captain.getLevel();
        CharacterPet pet = new CharacterPet(captain, member, power, power);
        owners.put(pet, captain);
        return pet.getSelf();
    }
}
