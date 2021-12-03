package com.example.petlover.comun.adaptadores;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
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
import com.example.petlover.R;
import com.example.petlover.comun.interfaces.OnItemClickClinicasListener;
import com.example.petlover.databinding.PlantillaClinicaBinding;
import com.example.petlover.pojo.Clinica;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AdaptadorClinicas extends RecyclerView.Adapter<AdaptadorClinicas.ViewHolder> implements Filterable {

    private PlantillaClinicaBinding binding;
    private OnItemClickClinicasListener listener;
    private List<Clinica> listaClinicas;
    private List<Clinica> sublistaClinicas;
    private Context context;
    Drawable myIcon;

    public AdaptadorClinicas(List<Clinica> listaClinicas,OnItemClickClinicasListener listener,Context context){
        this.listaClinicas=listaClinicas;
        sublistaClinicas=new ArrayList<>();
        sublistaClinicas.addAll(this.listaClinicas);
        this.listener=listener;
        this.context=context;
        myIcon=ResourcesCompat.getDrawable(context.getResources(), R.drawable.clinicavacio1, null);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        binding=PlantillaClinicaBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        view=binding.getRoot();
        return new ViewHolder(view);
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
    }

    @Override
    public int getItemCount() {
        return listaClinicas.size();
    }

    public void add(Clinica clinica){
        if (!listaClinicas.contains(clinica)){
            listaClinicas.add(clinica);
            sublistaClinicas.add(clinica);
            notifyItemInserted(listaClinicas.size()-1);
        } else {
            update(clinica);
        }
    }

    public void update(Clinica clinica) {
        if (listaClinicas.contains(clinica)){
            final int index = listaClinicas.indexOf(clinica);
            listaClinicas.set(index, clinica);
            sublistaClinicas.set(index,clinica);
            notifyItemChanged(index);
        }
    }

    public void remove(Clinica clinica){
        if (listaClinicas.contains(clinica)){
            final int index = listaClinicas.indexOf(clinica);
            listaClinicas.remove(index);
            sublistaClinicas.remove(index);
            notifyItemRemoved(index);
        }
    }

    public void clearAdapter(){
        if(listaClinicas!=null){
            listaClinicas.clear();
            sublistaClinicas.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public Filter getFilter() {
        return filtroClinicas;
    }

    public void filtrar(String charSequence){
        List<Clinica> lstFiltrada = new ArrayList<>();
        lstFiltrada.clear();
        if(charSequence == null || charSequence.length() == 0){
            lstFiltrada.addAll(sublistaClinicas);
        } else{
            String filterPattern = charSequence.toString().toLowerCase().trim();
            for(Clinica item : sublistaClinicas){
                if(item.getNombreClinica().toLowerCase().contains(filterPattern) || item.getEncargado().toLowerCase().contains(filterPattern)){
                    lstFiltrada.add(item);
                }
            }
        }

        // Unva vez ya se hayan agregado a la lista
        clearAdapter();
        listaClinicas.addAll(lstFiltrada);
        notifyDataSetChanged();
    }

    private Filter filtroClinicas=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Clinica> lstFiltrada = new ArrayList<>();
            lstFiltrada.clear();
            if(charSequence == null || charSequence.length() == 0){
                lstFiltrada.addAll(sublistaClinicas);
            } else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(Clinica item : sublistaClinicas){
                    if(item.getNombreClinica().toLowerCase().contains(filterPattern) || item.getEncargado().toLowerCase().contains(filterPattern)){
                        lstFiltrada.add(item);
                    }
                }
            }
            FilterResults resultados = new FilterResults();
            resultados.values = lstFiltrada;
            return resultados;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults){
            listaClinicas.clear();
            listaClinicas.addAll((Collection<? extends Clinica>) filterResults.values);
            notifyDataSetChanged();
        }

    };

    class ViewHolder extends RecyclerView.ViewHolder{

        String idClinica="";
        private View view;
        TextView txtNombreClinica;
        TextView txtEncargado;
        TextView txtContacto;
        TextView txtDireccion;
        ImageView imgPerfil;
        CardView cardView;

        ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            cardView=binding.cardBase;
            txtNombreClinica=binding.textClinnica;
            txtContacto=binding.textContacto;
            txtEncargado=binding.textEncargado;
            imgPerfil=binding.imgPerfilClinica;
            txtDireccion=binding.textDireccion;
        }

        void setOnClickListener(final Clinica clinicaSeleccionada, final OnItemClickClinicasListener listener) {
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(clinicaSeleccionada);
                }
            });

            cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onLongItemClick(clinicaSeleccionada);
                    return true;
                }
            });
        }

    }

}
