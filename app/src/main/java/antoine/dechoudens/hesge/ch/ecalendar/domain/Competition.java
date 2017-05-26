package antoine.dechoudens.hesge.ch.ecalendar.domain;

import android.support.annotation.NonNull;

/**
 * Created by Meckanik on 26.05.2017.
 */

public class Competition implements Comparable{
    String nom;

    public Competition(String nom) {
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
        Competition otherComp = (Competition)o;
        return this.getNom().compareTo(otherComp.getNom());
    }
}
