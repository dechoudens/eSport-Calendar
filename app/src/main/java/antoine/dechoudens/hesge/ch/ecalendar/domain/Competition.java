package antoine.dechoudens.hesge.ch.ecalendar.domain;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by Meckanik on 26.05.2017.
 */

public class Competition implements Comparable, Serializable{
    String nom;
    String description;
    String hash;

    public Competition(String nom, String description, String hash) {
        this.nom = nom;
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
}
