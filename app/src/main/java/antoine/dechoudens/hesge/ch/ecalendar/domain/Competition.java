package antoine.dechoudens.hesge.ch.ecalendar.domain;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Meckanik on 26.05.2017.
 */

public class Competition implements Comparable, Serializable{
    String nom;
    String description;
    String hash;
    List<String> dates;
    Game game;

    public Competition(String nom, String description, String hash, List<String> dates, Game game) {
        this.nom = nom;
        this.description = description;
        this.hash = hash;
        this.game = game;
        this.dates = dates;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        Competition otherComp = (Competition)o;
        return this.getNom().compareTo(otherComp.getNom());
    }

    public Game getGame() {
        return game;
    }

    @Override
    public String toString() {
        return nom + " - " + game.getNom();
    }

    public List<String> getDates() {
        return dates;
    }

    public String getValues(){
        String values = nom + ";" + description + ";";
        for (String date : dates){
            values += date + ";";
        }
        values.substring(0, values.length() - 1);
        return values;
    }
}
