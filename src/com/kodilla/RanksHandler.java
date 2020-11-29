package com.kodilla;

import java.io.*;
import java.util.HashMap;

public class RanksHandler {
    private static final File savedPlayerRanks = new File("rank.list");
    private static HashMap<String, int[]> playersRanks = new HashMap<>();

    public static HashMap<String, int[]> loadRanks(){
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(savedPlayerRanks));
            Object readObject = ois.readObject();
            if (readObject instanceof HashMap) {
                playersRanks = (HashMap) readObject;
            }
            ois.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return playersRanks;
    }

    public static void saveRank(String playerName, int wins, int losses){
        int[] playerAndComputerWins = {wins, losses};
        playersRanks.put(playerName, playerAndComputerWins);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(savedPlayerRanks));
            oos.writeObject(playersRanks);
            oos.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
