package com.example.petlover.comun.adaptadores;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.petlover.R;
import com.example.petlover.comun.interfaces.OnItemClickClinicasListener;
import com.example.petlover.databinding.PlantillaFavoritosBinding;
import com.example.petlover.pojo.Clinica;
import java.util.List;

public class AdaptadorFavoritos extends RecyclerView.Adapter<AdaptadorFavoritos.ViewHolder> {

    private PlantillaFavoritosBinding binding;
    private OnItemClickClinicasListener listener;

    public List<Clinica> getListaClinicas() {
        return listaClinicas;
    }

    public void setListaClinicas(List<Clinica> listaClinicas) {
        this.listaClinicas = listaClinicas;
    }

    private List<Clinica> listaClinicas;

    public List<String> getListaFavoritos() {
        return listaFavoritos;
    }

    public void setListaFavoritos(List<String> listaFavoritos) {
        this.listaFavoritos = listaFavoritos;
        notifyDataSetChanged();
    }

    private List<String> listaFavoritos;
    private Context context;
    Drawable myIcon;

    public AdaptadorFavoritos(List<Clinica> listaClinicas,OnItemClickClinicasListener listener,Context context){
        this.listaClinicas=listaClinicas;
        this.listener=listener;
        this.context=context;
        myIcon= ResourcesCompat.getDrawable(context.getResources(), R.drawable.clinicavacio1, null);
    }

    public void add(Clinica clinica){
        if (!listaClinicas.contains(clinica)){
            listaClinicas.add(clinica);
            notifyItemInserted(listaClinicas.size()-1);
        } else {
            update(clinica);
        }
    }

    public void update(Clinica clinica) {
        if (listaClinicas.contains(clinica)){
            final int index = listaClinicas.indexOf(clinica);
            listaClinicas.set(index, clinica);
            notifyItemChanged(index);
        }
    }

    public void remove(Clinica clinica){
        if (listaClinicas.contains(clinica)){
            final int index = listaClinicas.indexOf(clinica);
            listaClinicas.remove(index);
            notifyItemRemoved(index);
        }
    }

    public void clearAdapter(){
        if(listaClinicas!=null){
            listaClinicas.clear();
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        binding=PlantillaFavoritosBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        view=binding.getRoot();
        return new AdaptadorFavoritos.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Clinica clinica=listaClinicas.get(position);
        holder.txtNombreClinica.setText(clinica.getNombreClinica());
        holder.txtEncargado.setText(clinica.getEncargado());
        holder.txtDireccion.setText(clinica.getDireccion());
        holder.idClinica=clinica.getIdClinica();
        if(clinica.getNumeroCelular()!=null && clinica.getNumeroTelefo()!=null){
            //Tiene ambos
            String contact="Tel: "+clinica.getNumeroTelefo()+" - Cel: "+clinica.getNumeroCelular();
            holder.txtContacto.setText(contact);
        }else{
            if(clinica.getNumeroCelular()!=null){
                String contact="Cel: "+clinica.getNumeroCelular();
                holder.txtContacto.setText(contact);
            }else{
                String contact="Tel: "+clinica.getNumeroTelefo();
                holder.txtContacto.setText(contact);
            }
        }

        if(clinica.getFotoPerfilUrl()!=null && !clinica.getFotoPerfilUrl().isEmpty()){
            Glide.with(context)
                    .load(clinica.getFotoPerfilUrl())
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .centerCrop())
                    .into(holder.imgPerfil).onLoadFailed(myIcon);
        }
        holder.setOnClickListener(clinica,listener);
        /*if(listaFavoritos!=null){
            if(!listaFavoritos.contains(clinica.getIdClinica())){
                holder.view.setVisibility(View.GONE);
            }
        }else{
            holder.view.setVisibility(View.GONE);
        }*/
    }

    @Override
    public int getItemCount() {
        return listaClinicas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        String idClinica="";
        private View view;
        TextView txtNombreClinica;
        TextView txtEncargado;
        TextView txtContacto;
        TextView txtDireccion;
        ImageView imgPerfil;
        ImageView imgMenu;
        CardView cardView;

        ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            cardView=binding.getRoot().findViewById(R.id.cardBase);
            imgMenu=binding.imgMenu;
            txtNombreClinica=binding.getRoot().findViewById(R.id.textClinnica);
            txtContacto=binding.getRoot().findViewById(R.id.textContacto);
            txtEncargado=binding.getRoot().findViewById(R.id.textEncargado);
            imgPerfil=binding.getRoot().findViewById(R.id.imgPerfilClinica);
            txtDireccion=binding.getRoot().findViewById(R.id.textDireccion);
        }

        void setOnClickListener(final Clinica clinicaSeleccionada, final OnItemClickClinicasListener listener){
            imgMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(clinicaSeleccionada);
                }
            });
        }

    }

}
