package com.kodilla;

import java.util.List;
import javafx.scene.control.Label;

public class LogHandler {

    public void updateLogByPlayerMove(List<String> logs, int rollResult, Label log) {
        if (logs.size() > 4) {
            logs.remove(logs.size()-1);
        }
        logs.add(0, "Gracz wyrzucił " + rollResult + "\n");
        StringBuilder logText = new StringBuilder();
        for (String text : logs) {
            logText.append(text);
        }
        log.setText(logText.toString());
    }

    public void updateLogByComputerMove(List<String> logs, int rollResult, Label log) {
        if (logs.size() > 4) {
            logs.remove(logs.size()-1);
        }
        logs.add(0, "Komputer wyrzucił " + rollResult + "\n");
        StringBuilder logText = new StringBuilder();
        for (String text : logs) {
            logText.append(text);
        }
        log.setText(logText.toString());
    }
}
