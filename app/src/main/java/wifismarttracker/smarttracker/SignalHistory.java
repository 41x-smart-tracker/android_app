package wifismarttracker.smarttracker;

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by graydensmith on 15-03-22.
 */
public class SignalHistory {
    private ArrayList<Integer> distances;
    private int _lastDiff;

    public SignalHistory() {
        distances = new ArrayList<Integer>();
    }

    public void addSignal(int level) {
        distances.add(0, level);

        if (distances.size() > 6) {
            distances.remove(6);
        }
    }

    public double runningAverage(int num, int offset) {
        double sum = 0;

        // abort since there are not enough values for the requested average
        if ((num + offset) > distances.size())
            return 0;

        for(int i=offset; i < num + offset; i++) {
            sum += distances.get(i);
        }
        return sum / (num);
    }

    public int runningDiff() {
        if (increasingTrend(4)) {
            _lastDiff = 1;
            return 1;
        } else if (decreasingTrend(4)) {
            _lastDiff = -1;
            return -1;
        } else {
            return _lastDiff;
        }
    }

    private boolean increasingTrend(int num) {
        for(int i=0; i < num - 1; i++) {
            if (runningAverage(3, i) < runningAverage(3, i + 1))
                return false;
        }

        return true;
    }

    private boolean decreasingTrend(int num) {
        for(int i=0; i < num - 1; i++) {
            if (runningAverage(3, i) > runningAverage(3, i + 1))
                return false;
        }

        return true;
    }

    public double runningAverage() {
        return runningAverage(3, 0);
    }

    public double lastRunningAverage() {
        return runningAverage(3, 1);
    }

}
