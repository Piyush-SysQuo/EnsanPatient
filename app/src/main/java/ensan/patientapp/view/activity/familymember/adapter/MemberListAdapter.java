package ensan.patientapp.view.activity.familymember.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ensan.patientapp.R;
import ensan.patientapp.view.activity.familymember.model.Datum;
import ensan.patientapp.view.activity.familymember.model.GetFamilyPosition;

public class MemberListAdapter extends RecyclerView.Adapter<MemberListAdapter.MyViewHolder> {

    private List<Datum> datumList = new ArrayList();
    private GetFamilyPosition getFamilyPosition;

    public MemberListAdapter(List<Datum> datumList, GetFamilyPosition getFamilyPosition) {
        this.datumList = datumList;
        this.getFamilyPosition = getFamilyPosition;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.family_member_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
           holder.name.setText(datumList.get(position).getName());
           holder.phone.setText(datumList.get(position).getPhone());
           holder.relation.setText(datumList.get(position).getRelation());
           holder.type.setText(datumList.get(position).getType());

           holder.family_layout.setOnClickListener(v -> getFamilyPosition.getPosition(position));
           holder.medicalHistory.setOnClickListener(v -> getFamilyPosition.getPosition(position));
           holder.deleteMember.setOnClickListener(v -> getFamilyPosition.deletePosition(position));
    }

    @Override
    public int getItemCount() {
        return datumList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView name, phone, type, relation, medicalHistory;
        private ImageView deleteMember;
        private LinearLayout family_layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txtname);
            phone = itemView.findViewById(R.id.txtphone);
            type = itemView.findViewById(R.id.txttype);
            relation = itemView.findViewById(R.id.txt_relation);
            family_layout = itemView.findViewById(R.id.family_layout);
            medicalHistory = itemView.findViewById(R.id.medicalHistory);
            deleteMember = itemView.findViewById(R.id.deleteMember);
        }
    }


    // add member to the list
    public void addMember(Datum datum){

        if(datumList == null){
            datumList = new ArrayList<>();
        }

        datumList.add(datum);
        notifyDataSetChanged();
    }

    // add member to the list
    public void addAllMember(List<Datum> list){

        if(list == null){
            list = new ArrayList<>();
        }
        datumList.clear();

        datumList.addAll(list);
        notifyDataSetChanged();
    }

    // add member to the list
    public void setMemebers(List<Datum> list){

        if(list == null){
            list = new ArrayList<>();
        }

        datumList = list;
        notifyDataSetChanged();
    }

    // remove family member
    public void removeMember(Datum datum){

        if(datumList == null){
            datumList = new ArrayList<>();
        }

        datumList.remove(datum);

        notifyDataSetChanged();
    }
}
