package wifismarttracker.smarttracker;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by graydensmith on 15-01-30.
 */
public class NodeFragment extends Fragment{

    protected void onCreate() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState) {
        View view = inflater.inflate(R.layout.node_fragment, container, false);

        TextView name = (TextView) view.findViewById(R.id.nodeName);
        TextView distance = (TextView) view.findViewById(R.id.nodeDistance);

        name.setText(this.getArguments().getString("name"));
        distance.setText(String.valueOf(this.getArguments().getFloat("distance")));

        return view;
    }

    public void onPause()
    {
        super.onPause();
    }
}
