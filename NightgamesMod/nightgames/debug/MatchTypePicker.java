package nightgames.debug;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import nightgames.global.Global;
import nightgames.global.Scene;
import nightgames.gui.KeyableButton;
import nightgames.match.MatchType;

public class MatchTypePicker implements Scene {



    @Override
    public void respond(String response) {
        if (response.equals("Start")) {
            List<KeyableButton> buttons = Arrays.stream(MatchType.values())
                                                .map(MTButton::new)
                                                .collect(Collectors.toList());
            Global.gui().prompt("<b>DEBUG_MATCHTYPES is active. Select a match type below:</b>",
                            buttons);
        }
    }

    private static class MTButton extends KeyableButton {
        
        private static final long serialVersionUID = -3158804202952673759L;
        private final MatchType type;

        public MTButton(MatchType type) {
            super(type.name());
            this.type = type;
            getButton().addActionListener(this::startMatch);
        }

        @Override
        public String getText() {
            return type.name();
        }

        private void startMatch(ActionEvent e) {
            Global.currentMatchType = type;
            type.runPrematch();
        }
    }

}
