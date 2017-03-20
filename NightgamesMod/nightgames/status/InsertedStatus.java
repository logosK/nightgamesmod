package nightgames.status;

import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;

public interface InsertedStatus {
    BodyPart getHolePart();
    Character getReceiver();
    BodyPart getStickPart();
    Character getPitcher();
}
