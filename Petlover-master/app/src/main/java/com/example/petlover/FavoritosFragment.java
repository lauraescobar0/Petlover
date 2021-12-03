package com.example.petlover;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.petlover.comun.FirebaseCloudFirestoreAPI;
import com.example.petlover.comun.adaptadores.AdaptadorClinicas;
import com.example.petlover.comun.adaptadores.AdaptadorFavoritos;
import com.example.petlover.comun.interfaces.OnItemClickClinicasListener;
import com.example.petlover.databinding.FragmentClinicasBinding;
import com.example.petlover.databinding.FragmentFavoritosBinding;
import com.example.petlover.pojo.Clinica;
import com.example.petlover.pojo.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class FavoritosFragment extends Fragment implements OnItemClickClinicasListener {

    FragmentFavoritosBinding binding;
    FirebaseCloudFirestoreAPI firebaseCloudFirestoreAPI;
    AdaptadorFavoritos adaptadorFavoritos;
    DocumentReference documentReference;
    LinearLayoutManager layoutManagerma;
    ListenerRegistration listenerRegistration;
    Query query;
    private List<String> listaFavoritos;
//    List<Clinica> listaClinicasAux;

    public FavoritosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        firebaseCloudFirestoreAPI=FirebaseCloudFirestoreAPI.getFirestoreInstance();
        layoutManagerma=new LinearLayoutManager(getContext());
        listaFavoritos=new ArrayList<>();
        firebaseCloudFirestoreAPI=FirebaseCloudFirestoreAPI.getFirestoreInstance();
        CollectionReference collectionReference=firebaseCloudFirestoreAPI.getFirestoreReference().collection(FirebaseCloudFirestoreAPI.PathClinicas);
        query=collectionReference.orderBy("nombreClinica", Query.Direction.ASCENDING);
        binding = FragmentFavoritosBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        adaptadorFavoritos=new AdaptadorFavoritos(new ArrayList<>(),FavoritosFragment.this,getContext());
        binding.recyclerFavoritos.setLayoutManager(layoutManagerma);
        binding.recyclerFavoritos.setAdapter(adaptadorFavoritos);
    }

    public void traerClinicasDeFirebase(){
        binding.recyclerFavoritos.setVisibility(View.VISIBLE);
        binding.imgVacio.setVisibility(View.GONE);
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
                                if(listaFavoritos!=null){
                                    if(listaFavoritos.contains(clinica.getIdClinica())){
                                        adaptadorFavoritos.add(clinica);
                                    }
                                }
                                break;
                            case MODIFIED:
                                clinica=dc.getDocument().toObject(Clinica.class);
                                clinica.setIdClinica(dc.getDocument().getId());
                                if(listaFavoritos!=null){
                                    if(listaFavoritos.contains(clinica.getIdClinica())){
                                        adaptadorFavoritos.update(clinica);
                                    }
                                }
                                break;
                            case REMOVED:
                                clinica=dc.getDocument().toObject(Clinica.class);
                                clinica.setIdClinica(dc.getDocument().getId());
                                if(listaFavoritos!=null){
                                    if(listaFavoritos.contains(clinica.getIdClinica())){
                                        adaptadorFavoritos.remove(clinica);
                                    }
                                }
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

    public void VerificacionAgregadoAFavoritos(String idUsuario){
        DocumentReference documentReference;
        documentReference = FirebaseCloudFirestoreAPI.getFirestoreInstance().getFirestoreReference().collection(FirebaseCloudFirestoreAPI.PathUsers).document(idUsuario);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value!=null){
                    Usuario usuario=value.toObject(Usuario.class);
                    if(usuario!=null){
                        if(usuario.getListaIdsClinicas()!=null){
                            if(usuario.getListaIdsClinicas().size()>0){
                                setListaUsuariosFavoritos(usuario.getListaIdsClinicas());
                                traerClinicasDeFirebase();
                            }else{
                                mostrarVacio();
                            }
                        }else{
                            mostrarVacio();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(listaFavoritos!=null){
            traerClinicasDeFirebase();
            VerificacionAgregadoAFavoritos(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }
    }

    public void setListaUsuariosFavoritos(List<String> listaFavs){
        this.listaFavoritos=listaFavs;
//        adaptadorFavoritos.setListaFavoritos(listaFavs);
    }

    public void AgregarAlAdaptador(Clinica clinica){
        adaptadorFavoritos.add(clinica);
    }

    public void removerAdaptador(Clinica clinica){
        adaptadorFavoritos.remove(clinica);
    }

    public void ActualizarElAdaptador(Clinica clinica){
        adaptadorFavoritos.update(clinica);
    }

    @Override
    public void onItemClick(Clinica clinica) {
        if(clinica!=null){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Quitar de favoritos");
            builder.setIcon(R.drawable.icon_no_presionado);
            builder.setMessage("¿Está seguro que desea quitar de favoritos?");
            // Add the buttons
            builder.setPositiveButton("Quitar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //Si ya existe un registro de usuario
                    String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
                    if(MainActivity.usuarioSesion!=null){
                        List<String> listaFav=MainActivity.usuarioSesion.getListaIdsClinicas();
                        if(listaFav!=null){
                            if(listaFav.contains(clinica.getIdClinica())){
                                int index=listaFav.indexOf(clinica.getIdClinica());
                                listaFav.remove(index);
                            }
                        }else{//Si está nula la lista, hay que crearla y agregarla como la primera clinica fav
                            listaFav=new ArrayList<>();
                        }
                        listaFavoritos=listaFav;
                        FirebaseCloudFirestoreAPI.getFirestoreInstance().getFirestoreReference().collection(FirebaseCloudFirestoreAPI.PathUsers).document(uid).update(Usuario.LISTA_ID_CLINICAS,listaFav);
                        Toast.makeText(getContext(),"Operación realizada con éxito",Toast.LENGTH_LONG).show();
                    }
                    adaptadorFavoritos.clearAdapter();
                    dialog.dismiss();
                    traerClinicasDeFirebase();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    public void onLongItemClick(Clinica clinica) {

    }

    public void mostrarVacio() {
        binding.recyclerFavoritos.setVisibility(View.GONE);
        binding.imgVacio.setVisibility(View.VISIBLE);
    }

}