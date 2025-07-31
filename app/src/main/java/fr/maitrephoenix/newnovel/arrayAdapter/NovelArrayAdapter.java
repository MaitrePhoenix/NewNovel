package fr.maitrephoenix.newnovel.arrayAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import fr.maitrephoenix.newnovel.Commun;
import fr.maitrephoenix.newnovel.R;
import fr.maitrephoenix.newnovel.objet.Novel;

public class NovelArrayAdapter extends ArrayAdapter<Novel>{

    public NovelArrayAdapter(Context context, ArrayList<Novel> resource) {
        super(context, 0, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Novel novel = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.element_liste_novel, parent, false);
        }

        TextView nomNovel = convertView.findViewById(R.id.nomNovel);
        nomNovel.setText(novel.getNom());

        TextView siteTrad = convertView.findViewById(R.id.siteTrad);
        siteTrad.setText(novel.getNomSiteTrad());

        ImageView imageNovel = convertView.findViewById(R.id.imageNovel);
        if(!Commun.isChargementHorsLigne() && novel.getImageLien() != null && !novel.getImageLien().equals("")) {
            Picasso.get().load(novel.getImageLien()).into(imageNovel);
        }
        else {
            imageNovel.setImageBitmap(novel.getImageBitmap());
        }
        return convertView;
    }
}
