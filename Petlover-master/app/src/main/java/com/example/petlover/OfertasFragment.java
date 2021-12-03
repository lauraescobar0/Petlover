package com.example.petlover;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.petlover.comun.FirebaseCloudFirestoreAPI;
import com.example.petlover.comun.adaptadores.AdaptadorPublicaciones;
import com.example.petlover.comun.interfaces.OnItemClickPublicacionesListener;
import com.example.petlover.databinding.FragmentOfertasBinding;
import com.example.petlover.pojo.Clinica;
import com.example.petlover.pojo.PublicacionOferta;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class OfertasFragment extends Fragment implements OnItemClickPublicacionesListener {

    FragmentOfertasBinding binding;
    FirebaseCloudFirestoreAPI firebaseCloudFirestoreAPI;
    AdaptadorPublicaciones adaptadorPublicaciones;
    LinearLayoutManager layoutManagerma;
    ListenerRegistration listenerRegistration;
    Query query;
    List<Clinica> listaCLinicas;

    public OfertasFragment() {
        // Required empty public constructor
    }

    public static OfertasFragment newInstance(String param1, String param2) {
        OfertasFragment fragment = new OfertasFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        listaCLinicas=new ArrayList<>();
        firebaseCloudFirestoreAPI=FirebaseCloudFirestoreAPI.getFirestoreInstance();
        layoutManagerma=new LinearLayoutManager(getContext());
        firebaseCloudFirestoreAPI=FirebaseCloudFirestoreAPI.getFirestoreInstance();
        binding = FragmentOfertasBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
//        CargarPublicaciones();
        binding.recyclerPublicaciones.setLayoutManager(layoutManagerma);
        traerClinicasDeFirebase();
    }

    public void traerClinicasDeFirebase(){
        CollectionReference collectionReference=firebaseCloudFirestoreAPI.getFirestoreReference().collection(FirebaseCloudFirestoreAPI.PathClinicas);
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots!=null){
                    for(DocumentSnapshot item:queryDocumentSnapshots){
                        Clinica clinica= item.toObject(Clinica.class);
                        clinica.setIdClinica(item.getId());
                        if(!listaCLinicas.contains(clinica)){
                            listaCLinicas.add(clinica);
                        }
                    }
                    adaptadorPublicaciones=new AdaptadorPublicaciones(new ArrayList<PublicacionOferta>(),listaCLinicas,OfertasFragment.this,getContext(),FirebaseAuth.getInstance().getCurrentUser().getUid());
                    CargarPublicaciones();
                }
            }
        });
    }

    public void CargarPublicaciones(){
        binding.recyclerPublicaciones.setAdapter(adaptadorPublicaciones);
        CollectionReference collectionReference=FirebaseCloudFirestoreAPI.getFirestoreInstance().getFirestoreReference().collection(FirebaseCloudFirestoreAPI.PathPublicaciones);
        query=collectionReference.orderBy("titulo", Query.Direction.ASCENDING);
       /*try {
            CollectionReference collectionReference=FirebaseCloudFirestoreAPI.getFirestoreInstance().getFirestoreReference().collection(FirebaseCloudFirestoreAPI.PathPublicaciones);
            query=collectionReference.orderBy("titulo", Query.Direction.ASCENDING);
            query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if(queryDocumentSnapshots!=null){
                        for(DocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                            PublicacionOferta oferta=documentSnapshot.toObject(PublicacionOferta.class);
                            oferta.setIdPublicacion(documentSnapshot.getId());
                            adaptadorPublicaciones.add(oferta);
                        }
                    }
                }
            });
        }catch (Exception e){
            Log.d("TAGPUBLICACIONES", "CargarPublicaciones: ."+e.getMessage());
        }*/

        listenerRegistration=query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value!=null){
                    for (DocumentChange dc : value.getDocumentChanges()) {
                        PublicacionOferta publicacion;
                        switch (dc.getType()) {
                            case ADDED:
                                publicacion=dc.getDocument().toObject(PublicacionOferta.class);
                                publicacion.setIdPublicacion(dc.getDocument().getId());
                                adaptadorPublicaciones.add(publicacion);
                                break;
                            case MODIFIED:
                                publicacion=dc.getDocument().toObject(PublicacionOferta.class);
                                publicacion.setIdPublicacion(dc.getDocument().getId());
                                adaptadorPublicaciones.update(publicacion);
                                break;
                            case REMOVED:
                                publicacion=dc.getDocument().toObject(PublicacionOferta.class);
                                publicacion.setIdPublicacion(dc.getDocument().getId());
                                adaptadorPublicaciones.remove(publicacion);
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
    public void onItemClick(PublicacionOferta publicacion) {

    }

    @Override
    public void onLongItemClick(PublicacionOferta publicacion) {

    }
}