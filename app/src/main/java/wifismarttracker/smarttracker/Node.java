package wifismarttracker.smarttracker;

/**
 * Created by graydensmith on 15-01-30.
 */
public class Node {

    private String _securityKey;

    private String _name;

    private String _ssid;

    public Node(String securityKey, String name)
    {
        _securityKey = securityKey;
        _name = name;
    }

    public String name()
    {
        return _name;
    }

    public String ssid() {
        return _ssid;
    }

    public void setName(String name)
    {
    }

    public Node clone() {
        return new Node(_securityKey, _name);
    }
}
