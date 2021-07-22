package ensan.patientapp.view.activity.familymember.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;
import ensan.patientapp.R;

public class DropDownFamilyAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> stringList;

    public DropDownFamilyAdapter(@NonNull Context context, List<String> stringList) {
        this.mContext = context;
        this.stringList = stringList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //get the inflater and inflate the XML layout for each item
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dropdownlist, null);
        TextView textView = view.findViewById(R.id.txt_drop);
        textView.setText(stringList.get(position));
        return view;
    }

    @Override
    public int getCount() {
        return stringList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
