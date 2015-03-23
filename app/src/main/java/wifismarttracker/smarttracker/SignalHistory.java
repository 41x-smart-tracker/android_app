package wifismarttracker.smarttracker;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by graydensmith on 15-03-22.
 */
public class SignalHistory {
    private Queue<Integer> distances;

    public SignalHistory() {
        distances = new LinkedList<Integer>();
    }

    public void addSignal(int level) {
        distances.add(level);

        if (distances.size() > 3) {
            distances.remove();
        }
    }

    public int runningAverage(int last) {
        Iterator<Integer> iterator = distances.iterator();

        if (distances.size() == 0) {
            return 0;
        }

        int sum = 0;
        int index = 0;

        while (iterator.hasNext()) {
            sum += iterator.next();
            index++;

            if (index == last) {
                break;
            }
        }

        return sum / index;
    }

    public double runningAverage() {
        return runningAverage(3);
    }
}
