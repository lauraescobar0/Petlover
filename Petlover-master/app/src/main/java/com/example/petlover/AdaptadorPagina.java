package com.example.petlover;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class AdaptadorPagina extends PagerAdapter {

    Context context;

    int images[] = {
        R.drawable.profesionales,
        R.drawable.certificado,
        R.drawable.emergencia,
        R.drawable.productos
    };

    int temas[] ={
        R.string.tema1,
        R.string.tema2,
        R.string.tema3,
        R.string.tema4
    };

    int descripcion[] = {
        R.string.desc_1,
        R.string.desc_2,
        R.string.desc_3,
        R.string.desc_4
    };

    public AdaptadorPagina(Context context){
        this.context = context;

    }

    @Override
    public int getCount() {
        return temas.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.profesionales, container,false);

        ImageView imagenSlide = (ImageView) view.findViewById(R.id.imgslide);
        TextView slideTexto = (TextView) view.findViewById(R.id.textotema);
        TextView slideDescripcion = (TextView) view.findViewById(R.id.textodesc);

        imagenSlide.setImageResource(images[position]);
        slideTexto.setText(temas[position]);
        slideDescripcion.setText(descripcion[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}
