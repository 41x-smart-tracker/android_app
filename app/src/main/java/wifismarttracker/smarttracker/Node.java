package wifismarttracker.smarttracker;

/**
 * Created by graydensmith on 15-01-30.
 */
public class Node {

    private String securityKey;

    private String name;

    public Node(String securityKey, String name)
    {
        securityKey = securityKey;
        name = name;
    }

    public String name()
    {
        return name;
    }

    public void setName(String name)
    {

    }
}
