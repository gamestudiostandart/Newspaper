package newspaper.gamestudiostandart.newspaper.activitys.search;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import newspaper.gamestudiostandart.newspaper.R;
import newspaper.gamestudiostandart.newspaper.model.ResourseModel;


public class AddResourseAdapter extends RecyclerView.Adapter<AddResourseAdapter.ViewHolder> {

    private ArrayList<ResourseModel> list;

    AddResourseAdapter() {
        this.list = new ArrayList<>();
    }

    @NonNull
    @Override
    public AddResourseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.i_add_resourses, parent, false);
        return new AddResourseAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final AddResourseAdapter.ViewHolder holder, final int position) {

        final ResourseModel model = list.get(position);

        holder.cb_chack.setChecked(model.isCheck());
        holder.tv_name.setText(model.getName());
        holder.tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.setCheck(!model.isCheck());
                holder.cb_chack.setChecked(model.isCheck());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name;
        private CheckBox cb_chack;

        ViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            cb_chack = itemView.findViewById(R.id.cb_chack);
        }

    }

    void addAll(ArrayList<ResourseModel> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }
}
