package wifismarttracker.smarttracker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by graydensmith on 15-01-30.
 */
public class NodeFragment extends Fragment {

    protected void onCreate() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState) {
        View view = inflater.inflate(R.layout.node_fragment, container, false);

        TextView name = (TextView) view.findViewById(R.id.nodeName);
        TextView distance = (TextView) view.findViewById(R.id.nodeDistance);
        ImageView arrow = (ImageView) view.findViewById(R.id.arrow);

        name.setText(this.getArguments().getString("name"));
        distance.setText(String.valueOf(this.getArguments().getFloat("distance")));

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.arrow);
        Matrix mat = new Matrix();

        mat.postRotate(this.getArguments().getInt("angle"));

        Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true);

        arrow.setImageBitmap(bitmap1);

        return view;
    }

    public void onPause()
    {
        super.onPause();
    }
}
