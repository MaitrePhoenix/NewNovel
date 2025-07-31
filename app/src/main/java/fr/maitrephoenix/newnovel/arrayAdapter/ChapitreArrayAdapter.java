package fr.maitrephoenix.newnovel.arrayAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import fr.maitrephoenix.newnovel.R;
import fr.maitrephoenix.newnovel.objet.Chapitre;
import fr.maitrephoenix.newnovel.stokage.ChapitreLuStockage;

public class ChapitreArrayAdapter extends ArrayAdapter<Chapitre> {

    public ChapitreArrayAdapter(Context context, ArrayList<Chapitre> resource) {
        super(context, 0, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Chapitre chapitre = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.element_liste_chapitre, parent, false);
        }

        TextView nomNovel = (TextView) convertView.findViewById(R.id.nomChapitre);
        nomNovel.setText(chapitre.getTitre());

        //afficher le numero du chapitre
        TextView numeroChapitre = (TextView) convertView.findViewById(R.id.numero);
        numeroChapitre.setText("#" + chapitre.getNumero());

        if(ChapitreLuStockage.estLu(chapitre)){
            convertView.findViewById(R.id.lu).setVisibility(View.VISIBLE);
        }
        else{
            convertView.findViewById(R.id.lu).setVisibility(View.INVISIBLE);
        }

        return convertView;
    }


}
