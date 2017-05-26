package antoine.dechoudens.hesge.ch.ecalendar.domain;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by Meckanik on 26.05.2017.
 */

public class Game implements Comparable, Serializable{
    String nom;

    public Game(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        Game otherGame = (Game)o;
        return this.getNom().compareTo(otherGame.getNom());
    }
}
