package nightgames.debug;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import nightgames.global.Global;
import nightgames.global.Scene;
import nightgames.gui.KeyableButton;
import nightgames.match.MatchType;
import nightgames.modifier.Modifier;

public class MatchModifierPicker implements Scene {

    @Override
    public void respond(String response) {
        Modifier[] modifiers = Global.getModifierPool()
                        .toArray(new Modifier[Global.getModifierPool().size()]);

        if (response.equals("Start")) {
            List<KeyableButton> buttons = Arrays.stream(modifiers)
                            .map(MMButton::new).collect(Collectors.toList());
            Global.gui().prompt("<b>DEBUG_MATCHMODIFIERS is active. Select a match modifier below:</b>",
                            buttons);
        }
    }

    private static class MMButton extends KeyableButton {

        private static final long serialVersionUID = -3158804202952673759L;
        private final Modifier type;

        public MMButton(Modifier type) {
            super(type.name());
            this.type = type;
            getButton().addActionListener(this::startMatch);
        }

        @Override
        public String getText() {
            return type.name();
        }

        private void startMatch(ActionEvent e) {
            Global.currentMatchType = MatchType.NORMAL;
            MatchType.NORMAL.runWith(type);
        }
    }
}
