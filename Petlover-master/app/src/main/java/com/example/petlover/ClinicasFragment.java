package com.example.petlover;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.petlover.comun.FirebaseCloudFirestoreAPI;
import com.example.petlover.comun.adaptadores.AdaptadorClinicas;
import com.example.petlover.comun.interfaces.OnItemClickClinicasListener;
import com.example.petlover.databinding.FragmentClinicasBinding;
import com.example.petlover.databinding.PlantillaPublicacionesOfertasBinding;
import com.example.petlover.pojo.Clinica;
import com.example.petlover.pojo.Usuario;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ClinicasFragment extends Fragment implements OnItemClickClinicasListener {

    FragmentClinicasBinding binding;
    List<Clinica> listaCLinica;
    FirebaseCloudFirestoreAPI firebaseCloudFirestoreAPI;
    AdaptadorClinicas adaptadorClinicas;
    Query query;
    LinearLayoutManager layoutManagerma;
    ListenerRegistration listenerRegistration;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentClinicasBinding.inflate(inflater,container,false);
        listaCLinica=new ArrayList<>();
        firebaseCloudFirestoreAPI=FirebaseCloudFirestoreAPI.getFirestoreInstance();
        CollectionReference collectionReference=firebaseCloudFirestoreAPI.getFirestoreReference().collection(FirebaseCloudFirestoreAPI.PathClinicas);
        query=collectionReference.orderBy("nombreClinica", Query.Direction.ASCENDING);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        adaptadorClinicas=new AdaptadorClinicas(new ArrayList<Clinica>(),ClinicasFragment.this,getContext());
        layoutManagerma = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        layoutManagerma.setStackFromEnd(false);
        binding.rvClinicas.setLayoutManager(layoutManagerma);
        binding.rvClinicas.setAdapter(adaptadorClinicas);
        binding.searchField.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adaptadorClinicas.getFilter().filter(s);
                adaptadorClinicas.notifyDataSetChanged();
                return false;
            }
        });
        traerClinicasDeFirebase();
    }

    private void detenerLimpiarAdaptadorListener(){
        listenerRegistration.remove();
        listenerRegistration=null;
        adaptadorClinicas.clearAdapter();
    }

/*
    private void insertarClinicas(){
        Clinica clinica1=new Clinica();
        clinica1.setNombreClinica("Veterinaria \"Fenix\"");
        clinica1.setDireccion("Dirección: Plaza Santa Lucia local 3 Sonzacate, Sonsonate, \n" +
                "00000 Sonzacate, El Salvador");
        clinica1.setDescripcion("Servicios médicos para mascotas, somos especialistas en cirugía , \n" +
                "con una trayectoria de 20 años de experiencia que nos respalda.¡Se parte de nuestro servicio!");
        clinica1.setNumeroCelular("7782-7204");
        clinica1.setNumeroTelefo("2407-8877");
        clinica1.setFotoPerfilUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ-puWB4AJuQMT8Kx_aFkH-lh2KpRLzBd1A-MYQjnY9fMq3L7Dbk0S6hGtJkDO5gn1gqUA&usqp=CAU");

        Clinica clinica2=new Clinica();
        clinica2.setNombreClinica("VetLove");
        clinica2.setDireccion("Av. Claudia Lars, Barrrio Centro, Sonsonate");
        clinica2.setDescripcion("Somos de las mejores clínicas veterinarias para tu mascota, contamos son\n" +
                "servicios las 24h del día. ¡Ven y únete a Vetlover!");
        clinica2.setNumeroCelular("7538-8311");
        clinica2.setNumeroTelefo("2235-8956");
        clinica2.setFotoPerfilUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRcQk1wu1Pq_ojTVqzc8J4kfvL_U_R3iV-uQQ&usqp=CAU");


        Clinica clinica3=new Clinica();
        clinica3.setNombreClinica("El Surco");
        clinica3.setDireccion(" 6 av.sur, 1-8, Santa Tecla, El Salvador, CP 1501 Santa Tecla");
        clinica3.setDescripcion("Somos una veterinaria responsable y amante de los animales, confia a tu \n" +
                "amigo en las mejores manos de surco");
        clinica3.setNumeroCelular("7538-8714");
        clinica3.setNumeroTelefo("2564-1912");


        Clinica clinica4=new Clinica();
        clinica4.setNombreClinica("Veterinaria de diagnóstico");
        clinica4.setDireccion("Bo Mejicanos Av Morazán 8 10 Sonsonate, Sonsonate.");
        clinica4.setDescripcion("Consultas especializadas, exámenes, tomografías y mucho más.");
        clinica4.setNumeroCelular("7023-2425");
        clinica4.setNumeroTelefo("2451-9309");
        clinica4.setFotoPerfilUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQC3R_poGt7DTN0mfchR8RY46J0dcdPq4I-3ENT2miqqFJDvPUqGaclt8LziaSwMR65KX8&usqp=CAU");

        FirebaseCloudFirestoreAPI.getFirestoreInstance().getFirestoreReference().collection(FirebaseCloudFirestoreAPI.PathClinicas).add(clinica1);
        FirebaseCloudFirestoreAPI.getFirestoreInstance().getFirestoreReference().collection(FirebaseCloudFirestoreAPI.PathClinicas).add(clinica2);
        FirebaseCloudFirestoreAPI.getFirestoreInstance().getFirestoreReference().collection(FirebaseCloudFirestoreAPI.PathClinicas).add(clinica3);
        FirebaseCloudFirestoreAPI.getFirestoreInstance().getFirestoreReference().collection(FirebaseCloudFirestoreAPI.PathClinicas).add(clinica4);
    }
*/

    public void AgregarAlAdaptador(Clinica clinica){
        adaptadorClinicas.add(clinica);
    }

    public void removerAdaptador(Clinica clinica){
        adaptadorClinicas.remove(clinica);
    }

    public void ActualizarElAdaptador(Clinica clinica){
        adaptadorClinicas.update(clinica);
    }

    private void traerClinicasDeFirebase(){
        listenerRegistration=query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value!=null){
                    for (DocumentChange dc : value.getDocumentChanges()) {
                        Clinica clinica;
                        switch (dc.getType()) {
                            case ADDED:
                                clinica=dc.getDocument().toObject(Clinica.class);
                                clinica.setIdClinica(dc.getDocument().getId());
                                adaptadorClinicas.add(clinica);
                                break;
                            case MODIFIED:
                                clinica=dc.getDocument().toObject(Clinica.class);
                                clinica.setIdClinica(dc.getDocument().getId());
                                adaptadorClinicas.update(clinica);
                                break;
                            case REMOVED:
                                clinica=dc.getDocument().toObject(Clinica.class);
                                clinica.setIdClinica(dc.getDocument().getId());
                                adaptadorClinicas.remove(clinica);
                                break;
                        }
                    }

                }

                if(error!=null){
                    Toast.makeText(getContext(),"Ha ocurrido un error al descargarlo...",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onItemClick(Clinica clinica) {
        Toast.makeText(getContext(),"CLINICA CLICK "+clinica.getNombreClinica(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLongItemClick(Clinica clinica) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Agregar a favoritos");
        builder.setIcon(R.drawable.icon_presionado);
        builder.setMessage("Está seguro que desea agregar a favoritos");
        // Add the buttons
        builder.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Si ya existe un registro de usuario
                String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
                if(MainActivity.usuarioSesion!=null){
                    List<String> listaFav=MainActivity.usuarioSesion.getListaIdsClinicas();
                    if(listaFav!=null){
                        if(!listaFav.contains(clinica.getIdClinica())){
                            listaFav.add(clinica.getIdClinica());
                        }
                    }else{//Si está nula la lista, hay que crearla y agregarla como la primera clinica fav
                        listaFav=new ArrayList<>();
                        listaFav.add(clinica.getIdClinica());
                    }
                    FirebaseCloudFirestoreAPI.getFirestoreInstance().getFirestoreReference().collection(FirebaseCloudFirestoreAPI.PathUsers).document(uid).update(Usuario.LISTA_ID_CLINICAS,listaFav);
                    Toast.makeText(getContext(),"Se ha agregado",Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private Clinica getClinica(DocumentChange dc){
        Clinica clinica;
        clinica=dc.getDocument().toObject(Clinica.class);
        clinica.setIdClinica(dc.getDocument().getId());
        return clinica;
    }

}