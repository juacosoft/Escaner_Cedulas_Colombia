package co.martketing.escanercedulas.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import co.martketing.escanercedulas.R;
import co.martketing.escanercedulas.models.PersonData;

public class PersonDataAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<PersonData> persons = new ArrayList<>();
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    public interface OnItemClickListener {
        void onItemClick(View view, PersonData obj, int position);
    }
    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public PersonDataAdapter(List<PersonData> persons,Context context) {
        this.persons = persons;
        this.ctx=context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
        vh = new PersonsViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof PersonsViewHolder) {
            PersonsViewHolder view = (PersonsViewHolder) holder;

            PersonData p = persons.get(position);
            view.names.setText(p.getNames()+" "+p.getLnames());
            view.doc_id.setText(p.getDoc_id());
            view.temperature.setText(p.getTempe());

            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, persons.get(position), position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

    public class PersonsViewHolder extends RecyclerView.ViewHolder {

        public TextView names,doc_id,temperature;
        public View lyt_parent;
        public PersonsViewHolder(View v) {
            super(v);
            names = (TextView) v.findViewById(R.id.itemperson_name);
            doc_id = (TextView) v.findViewById(R.id.itemperson_id);
            temperature=(TextView) v.findViewById(R.id.itemperson_temp);
            lyt_parent=v;
        }
    }
}
