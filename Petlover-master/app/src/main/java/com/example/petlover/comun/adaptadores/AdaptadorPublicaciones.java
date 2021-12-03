package com.example.petlover.comun.adaptadores;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.petlover.OfertasFragment;
import com.example.petlover.R;
import com.example.petlover.comun.interfaces.OnItemClickClinicasListener;
import com.example.petlover.comun.interfaces.OnItemClickPublicacionesListener;
import com.example.petlover.databinding.PlantillaClinicaBinding;
import com.example.petlover.databinding.PlantillaPublicacionesOfertasBinding;
import com.example.petlover.pojo.Clinica;
import com.example.petlover.pojo.PublicacionOferta;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorPublicaciones extends RecyclerView.Adapter<AdaptadorPublicaciones.ViewHolder> {

    PlantillaPublicacionesOfertasBinding binding;
    private OnItemClickPublicacionesListener listener;
    private List<PublicacionOferta> listaPublicaciones;
    private List<Clinica> listaCLinicas;
    private Context context;
    private String idUsuarioLocal;
    Drawable myIcon;
    Drawable favLleno;
    Drawable favVacio;
    Drawable imgPorDefecto;

    public AdaptadorPublicaciones(List<PublicacionOferta> listaPublicaciones, List<Clinica> listaclinicas, OnItemClickPublicacionesListener listener, Context context,String idUsuario){
        this.listaPublicaciones=listaPublicaciones;
        this.listaCLinicas=listaclinicas;
        this.listener=listener;
        this.context=context;
        this.idUsuarioLocal=idUsuario;
        myIcon= ResourcesCompat.getDrawable(context.getResources(), R.drawable.clinicavacio1, null);
        favLleno= ResourcesCompat.getDrawable(context.getResources(), R.drawable.icon_presionado, null);
        favVacio= ResourcesCompat.getDrawable(context.getResources(), R.drawable.icon_no_presionado, null);
        imgPorDefecto= ResourcesCompat.getDrawable(context.getResources(), R.drawable.vaciogeneral, null);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        binding= PlantillaPublicacionesOfertasBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        view=binding.getRoot();
        return new AdaptadorPublicaciones.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Cargar la Sección de encabezado.
        PublicacionOferta temp=listaPublicaciones.get(position);
        Clinica clinicaTem=getClinicaPublicante(temp.getIdUsuarioPublicante());

        holder.txtTitulo.setText(temp.getTitulo());
        holder.txtDescripcion.setText(temp.getDescripcion());
        holder.txtCategoría.setText(temp.getCategoria());
        if(clinicaTem!=null){
            holder.txtNombreClinicaUser.setText(clinicaTem.getNombreClinica());
        }


        //Cargar imgPerfil
        if(clinicaTem!=null){
            if(clinicaTem.getFotoPerfilUrl()!=null && !clinicaTem.getFotoPerfilUrl().isEmpty()){
                Glide.with(context)
                        .load(clinicaTem.getFotoPerfilUrl())
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .centerCrop())
                        .into(holder.imgPerfil).onLoadFailed(myIcon);
            }
        }

        //Cargar imgGeneral
        if(temp!=null){
            if(temp.getFotoPrincipal()!=null && !temp.getFotoPrincipal().isEmpty()){
                Glide.with(context)
                        .load(temp.getFotoPrincipal())
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .centerCrop())
                        .into(holder.imgPublicacion).onLoadFailed(imgPorDefecto);
            }
        }

        //CargarFavoNo
        if(temp.getListaFavoritosPublicacionIdUsuarios()!=null){
            if(temp.getListaFavoritosPublicacionIdUsuarios().contains(idUsuarioLocal)){
                holder.imgFav.setImageDrawable(favLleno);
                holder.fav=true;
            }
            String num=String.valueOf(temp.getListaFavoritosPublicacionIdUsuarios().size());
            holder.txtNumero.setText(num);
        }
        holder.setOnClickListener(temp,listener);
    }

    public Clinica getClinicaPublicante(String idClinica){
        Clinica clinica=null;
        for(int i=0;i<listaCLinicas.size();i++){
            if(listaCLinicas.get(i).getIdClinica().equals(idClinica)){
                clinica=listaCLinicas.get(i);
                break;
            }
        }
        return clinica;
    }

    @Override
    public int getItemCount() {
        return listaPublicaciones.size();
    }

    public List<Clinica> getListaCLinicas() {
        return listaCLinicas;
    }

    public void setListaCLinicas(List<Clinica> listaCLinicas) {
        this.listaCLinicas = listaCLinicas;
    }

    public void addClinicaToList(Clinica clinica){
        this.listaCLinicas.add(clinica);
    }

    public void add(PublicacionOferta publicacion){
        if (!listaPublicaciones.contains(publicacion)){
            listaPublicaciones.add(publicacion);
            notifyItemInserted(listaPublicaciones.size()-1);
        } else {
            update(publicacion);
        }
    }

    public void update(PublicacionOferta publicacion) {
        if (listaPublicaciones.contains(publicacion)){
            final int index = listaPublicaciones.indexOf(publicacion);
            listaPublicaciones.set(index, publicacion);
            notifyItemChanged(index);
        }
    }

    public void remove(PublicacionOferta publicacion){
        if (listaPublicaciones.contains(publicacion)){
            final int index = listaPublicaciones.indexOf(publicacion);
            listaPublicaciones.remove(index);
            notifyItemRemoved(index);
        }
    }

    public void clearAdapter(){
        if(listaPublicaciones!=null){
            listaPublicaciones.clear();
            notifyDataSetChanged();
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        String idClinica="";
        Boolean fav=false;
        private View view;
        TextView txtNombreClinicaUser;
        TextView txtCategoría;
        TextView txtTitulo;
        TextView txtDescripcion;
        TextView txtNumero;
        ImageView imgMenu;
        ImageView imgPerfil;
        ImageView imgFav;
        ImageView imgPublicacion;
        CardView cardView;

        ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            cardView=binding.cardBase;
            txtNombreClinicaUser=binding.txtNombreClinicaUsuario;
            txtCategoría=binding.categoria;
            txtTitulo=binding.textTituloPublicacion;
            txtDescripcion=binding.textDescripcion;
            txtNumero=binding.favCount;
            imgMenu=binding.imgMenu;
            imgPerfil=binding.imgPerfilClinica;
            imgFav=binding.imgFavorito;
            imgPublicacion=binding.imgPublicacion;
        }

        void setOnClickListener(final PublicacionOferta publicacionOferta, final OnItemClickPublicacionesListener listener) {
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(publicacionOferta);
                }
            });

            cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onLongItemClick(publicacionOferta);
                    return true;
                }
            });

            imgFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(fav){ //Actualizar visualmente
                        fav=false;
                        imgFav.setImageDrawable(favVacio);
                    }else{
                        fav=true;
                        imgFav.setImageDrawable(favLleno);
                    }
                    listener.onItemClick(publicacionOferta); // en el fragmento se actaulizará el dato.
                }
            });

            imgPerfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(publicacionOferta);
                }
            });

            imgMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(publicacionOferta);
                }
            });
        }

    }


}
