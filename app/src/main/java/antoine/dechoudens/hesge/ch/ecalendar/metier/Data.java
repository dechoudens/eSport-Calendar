package antoine.dechoudens.hesge.ch.ecalendar.metier;

import antoine.dechoudens.hesge.ch.ecalendar.domain.Competition;

/**
 * Created by Meckanik on 08.06.2017.
 */

public class Data {

    private static Data instance = new Data();

    private Competition competitionSelected;

    public static Data getInstance(){
        return instance;
    }

    public Competition getCompetition() {
        return competitionSelected;
    }

    public void setCompetition(Competition competition) {
        this.competitionSelected = competition;
    }
}
