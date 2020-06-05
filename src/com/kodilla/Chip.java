package com.kodilla;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Chip {
    ImageView look;
    Location location;

    public Chip(ImageView look, Location location) {
        this.location = location;
        this.look = look;
    }

    public Location getLocation() {
        return location;
    }

    public ImageView getLook() {
        return look;
    }

    public void setLocation(Location location) {

        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Chip chip = (Chip) o;

        return look.equals(chip.look);
    }

    @Override
    public int hashCode() {
        return look.hashCode();
    }

    @Override
    public String toString() {
        return "Chip{" +
                "location=" + location +
                '}';
    }
}
