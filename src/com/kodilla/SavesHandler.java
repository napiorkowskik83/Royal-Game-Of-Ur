package com.kodilla;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class SavesHandler {
    private static HashMap<String, ArrayList<Location>> savedGamesMap = new HashMap<>();
    private static final ArrayList<Location> savedChipsLocations = new ArrayList<>();
    private static final File savedGames = new File("saved.games");

    public static void saveGame(String playerName, ArrayList<Chip> whiteChips, ArrayList<Chip> blackChips){
        savedChipsLocations.clear();
        for (int i = 0; i < whiteChips.size(); i++) {
            savedChipsLocations.add(whiteChips.get(i).getLocation());
            savedChipsLocations.add(blackChips.get(i).getLocation());
        }
        savedGamesMap.put(playerName, savedChipsLocations);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(savedGames));
            oos.writeObject(savedGamesMap);
            oos.close();
        } catch (Exception exc) {
            System.out.println(exc.getMessage());
        }
    }

    public static HashMap<String, ArrayList<Location>> loadSavedGames(){
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(savedGames));
            Object readObject = ois.readObject();
            if (readObject instanceof HashMap) {
                savedGamesMap = (HashMap) readObject;
            }
            ois.close();
        } catch (Exception exce) {
            System.out.println(exce.getMessage());
        }
        return savedGamesMap;
    }

}
