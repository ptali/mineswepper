package minesweeper.data;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Score implements Comparable<Score> {

    private final SimpleStringProperty name = new SimpleStringProperty("");
    private final SimpleIntegerProperty time = new SimpleIntegerProperty();

    public Score(String name, int time) {
        this.name.set(name);
        this.time.set(time);
    }

    @Override
    public int compareTo(Score o) {
        return Integer.compare(time.get(), o.time.get());
    }

    public int getTime() {
        return time.get();
    }

    public String getName() {
        return name.get();
    }
}
