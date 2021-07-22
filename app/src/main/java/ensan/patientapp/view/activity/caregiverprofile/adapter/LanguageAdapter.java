package ensan.patientapp.view.activity.caregiverprofile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import ensan.patientapp.R;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder> {

    private Context mContext;
    private List<String> langList;

    public LanguageAdapter(Context mContext, List<String> langList) {
        this.mContext = mContext;
        this.langList = langList;
    }

    @NonNull
    @Override
    public LanguageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.language_layout, parent, false);
        return new LanguageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageViewHolder holder, int position) {
              holder.languageTV.setText(langList.get(position));
    }

    @Override
    public int getItemCount() {
        return langList.size();
    }

    public class LanguageViewHolder extends RecyclerView.ViewHolder {

        TextView languageTV;

        public LanguageViewHolder(@NonNull View itemView) {
            super(itemView);
            languageTV =  itemView.findViewById(R.id.languageTV);
        }
    }
}
