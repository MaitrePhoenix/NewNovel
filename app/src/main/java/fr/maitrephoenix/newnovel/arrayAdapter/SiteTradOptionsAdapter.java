package fr.maitrephoenix.newnovel.arrayAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fr.maitrephoenix.newnovel.R;
import fr.maitrephoenix.newnovel.activity.main.RecupNovel;
import fr.maitrephoenix.newnovel.objet.SiteTrad;
import fr.maitrephoenix.newnovel.stokage.SettingsStockage;

public class SiteTradOptionsAdapter extends RecyclerView.Adapter<SiteTradOptionsAdapter.ViewHolder> {
    private ArrayList<SiteTrad> data;
    private ArrayList<String> siteExclu;

    public SiteTradOptionsAdapter(ArrayList<SiteTrad> data, ArrayList<String> siteExclu) {
        this.data = data;
        this.siteExclu = siteExclu;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_liste_site_options, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.checkBox.setText(data.get(position).getNom());
        if(!siteExclu.contains(data.get(position).getNom())) {
            holder.checkBox.setChecked(true);
        }
    }

    public void onCheckboxClicked(int position) {
        if(siteExclu.contains(data.get(position).getNom())) {
            siteExclu.remove(data.get(position).getNom());
        } else {
            siteExclu.add(data.get(position).getNom());
        }
        SettingsStockage.setSiteExclu(siteExclu);
        RecupNovel.forceRechargementListeNovel();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CheckBox checkBox;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.check_box_site_option);
            checkBox.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAbsoluteAdapterPosition();
            RecyclerView recyclerView = (RecyclerView) itemView.getParent();
            SiteTradOptionsAdapter adapter = (SiteTradOptionsAdapter) recyclerView.getAdapter();
            adapter.onCheckboxClicked(position);
        }
    }
}