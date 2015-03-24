package wifismarttracker.smarttracker;

/**
 * Created by graydensmith on 15-02-03.
 */
public class DistanceResult {

    private String _name;

    private float _distance;

    public DistanceResult(String name, float distance)
    {
        _name = name;
        _distance = distance;
    }

    public String name()
    {
        return _name;
    }

    public float distance() {
        return _distance;
    }
}
