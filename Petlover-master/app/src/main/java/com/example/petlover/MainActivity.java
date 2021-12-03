package com.example.petlover;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.petlover.agregarUI.gestionUsuario;
import com.example.petlover.comun.FirebaseCloudFirestoreAPI;
import com.example.petlover.comun.adaptadores.AdaptadorClinicas;
import com.example.petlover.databinding.ActivityMainBinding;
import com.example.petlover.pojo.Clinica;
import com.example.petlover.pojo.PublicacionOferta;
import com.example.petlover.pojo.Usuario;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
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
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    ActivityMainBinding binding;
    FirebaseCloudFirestoreAPI firebaseCloudFirestoreAPI;
    TextView txtUsuario;
    CircleImageView circleImageView;
    public static Usuario usuarioSesion;

    //Datos clinicas
    List<Clinica> listaCLinica;
    ListenerRegistration listenerRegistration;
    Query query;

    FragmentManager fragmentManager= getSupportFragmentManager();
    private final ClinicasFragment clinicasFragment= new ClinicasFragment();
    private final FavoritosFragment favoritosFragment= new FavoritosFragment();
    private final OfertasFragment ofertasFragment= new OfertasFragment();
    private Fragment fragmentActive= ofertasFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        setContentView(R.layout.activity_main);
        firebaseCloudFirestoreAPI=FirebaseCloudFirestoreAPI.getFirestoreInstance();
        CollectionReference collectionReference=firebaseCloudFirestoreAPI.getFirestoreReference().collection(FirebaseCloudFirestoreAPI.PathClinicas);
        query=collectionReference.orderBy("nombreClinica", Query.Direction.ASCENDING);
        listaCLinica=new ArrayList<>();
        InicializarFragmentsTransactions();
        inicializarVariables();
        configurarListeners();
        traerDatosUsuarioDesdeFirebase();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void ActualizarLista(){
        List<String> clinicas=new ArrayList<>();
        clinicas.add("6reHftqjM4ZZf1DfgQDd");
        clinicas.add("c1QL1jL8w3LcVuyhpLh2");
        FirebaseCloudFirestoreAPI.getFirestoreInstance().getFirestoreReference().collection(FirebaseCloudFirestoreAPI.PathUsers).document("EkM7wtorinZFR4C0GW9VzDqSfNf1").update(Usuario.LISTA_ID_CLINICAS,clinicas);
    }

    public void InsertarPublicaciones(){
        List<String> clinicas=new ArrayList<>();
        clinicas.add("6reHftqjM4ZZf1DfgQDd");
        clinicas.add("c1QL1jL8w3LcVuyhpLh2");

        List<String> usuarios=new ArrayList<>();
        usuarios.add("EkM7wtorinZFR4C0GW9VzDqSfNf1");
        usuarios.add("XT3rgPppTIgqLk6WhrxZm9sbgfp2\n");
        CollectionReference collectionReference= FirebaseCloudFirestoreAPI.getFirestoreInstance().getFirestoreReference().collection(FirebaseCloudFirestoreAPI.PathPublicaciones);

        PublicacionOferta pub1=new PublicacionOferta();
        pub1.setIdUsuarioPublicante("6reHftqjM4ZZf1DfgQDd");
        pub1.setTitulo("Titulo de publicación ramdon");
        pub1.setListaFavoritosPublicacionIdUsuarios(usuarios);
        pub1.setDescripcion("psum fue concebido como un texto de relleno, formateado de una cierta manera para permitir la presentación de elementos gráficos en documentos, sin necesidad de una copia formal. El uso de Lorem Ipsum permite a los diseñadores reunir los diseños y la forma del contenido antes de que el contenido se haya creado");
        pub1.setCategoria("Producto");

        PublicacionOferta pub2=new PublicacionOferta();
        pub2.setIdUsuarioPublicante("c1QL1jL8w3LcVuyhpLh2");
        pub2.setTitulo("Titulo de publicación ramdon 2");
        pub2.setListaFavoritosPublicacionIdUsuarios(usuarios);
        pub2.setFotoPrincipal("https://www.petdarling.com/wp-content/uploads/2015/05/raza-pug-carlino.jpg");
        pub2.setDescripcion("psum fue concebido como un texto de relleno");
        pub2.setCategoria("Producto");

        PublicacionOferta pub3=new PublicacionOferta();
        pub3.setIdUsuarioPublicante("6reHftqjM4ZZf1DfgQDd");
        pub3.setTitulo("Titulo de publicación ramdon 3");
        pub3.setListaFavoritosPublicacionIdUsuarios(usuarios);
        pub3.setDescripcion("psum fue concebido como un texto de relleno");
        pub3.setCategoria("Producto");

        collectionReference.add(pub1);
        collectionReference.add(pub2);
        collectionReference.add(pub3);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(usuarioSesion.getId()!=null && !usuarioSesion.getId().isEmpty()){
            VerificacionAgregadoAFavoritos(usuarioSesion.getId());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void detenerLimpiarAdaptadorListener(){
        listenerRegistration.remove();
        listenerRegistration=null;
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
                                clinicasFragment.AgregarAlAdaptador(clinica);
//                                favoritosFragment.AgregarAlAdaptador(clinica);
                                listaCLinica.add(clinica);
                                break;
                            case MODIFIED:
                                clinica=dc.getDocument().toObject(Clinica.class);
                                clinica.setIdClinica(dc.getDocument().getId());
                                clinicasFragment.ActualizarElAdaptador(clinica);
  //                              favoritosFragment.ActualizarElAdaptador(clinica);
                                if (listaCLinica.contains(clinica)){
                                    final int index = listaCLinica.indexOf(clinica);
                                    listaCLinica.set(index, clinica);
                                }
                                break;
                            case REMOVED:
                                clinica=dc.getDocument().toObject(Clinica.class);
                                clinica.setIdClinica(dc.getDocument().getId());
                                clinicasFragment.removerAdaptador(clinica);
 //                               favoritosFragment.removerAdaptador(clinica);
                                if (listaCLinica.contains(clinica)){
                                    final int index = listaCLinica.indexOf(clinica);
                                    listaCLinica.remove(index);
                                }
                                break;
                        }
                    }

                }

                if(error!=null){
                    Toast.makeText(getApplicationContext(),"Ha ocurrido un error al descargarlo...",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void inicializarVariables(){
        View viewMenuLateral= binding.navigationViewTop.getHeaderView(0);
        this.txtUsuario=viewMenuLateral.findViewById(R.id.menuDrawerHeaderUser);
        circleImageView = findViewById(R.id.userProfileToolbarCircleView);
    }

    private void configurarListeners(){

        binding.bottomNavigationView.setOnItemSelectedListener(this);

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
                circleImageView.setEnabled(true);
            }
        });

        binding.topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        binding.navigationViewTop.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.itemd1GestionUsuario:{
                        startActivity(new Intent(getApplicationContext(),gestionUsuario.class));
                    }break;

/*                    case R.id.itemd2Reseñas:{
                        Toast.makeText(getApplicationContext(),"2",Toast.LENGTH_SHORT).show();
                    }break;*/

                    case R.id.item3GestionarPerfilClinica:{
                        Toast.makeText(getApplicationContext(),"3",Toast.LENGTH_SHORT).show();
                    }break;

                    case R.id.item4GestionarServicios:{
                        Toast.makeText(getApplicationContext(),"4",Toast.LENGTH_SHORT).show();
                    }break;

                    case R.id.item5GestionarPublicaciones:{
                        Toast.makeText(getApplicationContext(),"5",Toast.LENGTH_SHORT).show();
                    }break;

/*                    case R.id.item6VerEvaluaciones:{
                        Toast.makeText(getApplicationContext(),"6",Toast.LENGTH_SHORT).show();
                    }break;*/

                }
                binding.drawerLayout.closeDrawers();
                return true;
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
                    Toast.makeText(getApplicationContext(),"Event de solo el usuario",Toast.LENGTH_SHORT).show();
//                    listaClinicasAux=adaptadorFavoritos.getListaClinicas();
//                    adaptadorFavoritos.clearAdapter();
                    if(usuario.getListaIdsClinicas()!=null){
                        if(usuario.getListaIdsClinicas().size()>0){
                            favoritosFragment.setListaUsuariosFavoritos(usuario.getListaIdsClinicas());
                            favoritosFragment.traerClinicasDeFirebase();
                        }else{
                            favoritosFragment.mostrarVacio();
                        }
                    }else{
                        favoritosFragment.mostrarVacio();
                    }
                }

                if(error!=null){
                }
            }
        });
    }

    private void ocultarMenuParaUsuariosPropietarios(){
        Menu menuLateral= binding.navigationViewTop.getMenu();

        MenuItem perfilClinica=menuLateral.getItem(1);
        MenuItem servicioCliente=menuLateral.getItem(2);
        MenuItem misPublicaciones= menuLateral.getItem(3);

        if(usuarioSesion!=null){
            if(usuarioSesion.getRolUsuario()!=null){
                if(usuarioSesion.getRolUsuario().equals(Usuario.ROL_USUARIO) || usuarioSesion.getRolUsuario().isEmpty()){
                    perfilClinica.setVisible(false);
                    servicioCliente.setVisible(false);
                    misPublicaciones.setVisible(false);
                }//Acá la excepación es que sea Rol propietario, pero como en ese caso no se va a ocultar, no hacemos nada.
            }else{//No hay rol, oculto
                perfilClinica.setVisible(false);
                servicioCliente.setVisible(false);
                misPublicaciones.setVisible(false);
            }
        }else{ // por defecto oculto
            perfilClinica.setVisible(false);
            servicioCliente.setVisible(false);
            misPublicaciones.setVisible(false);
        }

    }

    private void traerDatosUsuarioDesdeFirebase(){
        this.usuarioSesion = new Usuario();
        FirebaseUser currentUser=FirebaseAuth.getInstance().getCurrentUser();
        Log.d("TAGUID",currentUser.getUid());
        FirebaseCloudFirestoreAPI firebaseCloudFirestoreAPI=FirebaseCloudFirestoreAPI.getFirestoreInstance();
        firebaseCloudFirestoreAPI.getFirestoreReference()
                .collection(FirebaseCloudFirestoreAPI.PathUsers)
                .document(currentUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                // Llenando el objeto usuario local con los datos en firebase:

                usuarioSesion=documentSnapshot.toObject(Usuario.class);
                // A partir de acá, se pretende mostrar los datos visualmente.
                if(usuarioSesion!=null){

                    if(usuarioSesion.getUsuario()!=null){
                        txtUsuario.setText(usuarioSesion.getUsuario());
                        Toast.makeText(getApplicationContext(),"Bienvenido/a "+usuarioSesion.getUsuario(),Toast.LENGTH_SHORT).show();
                    }

                    if(usuarioSesion.getCorreo()!=null){
                        txtUsuario.setText(usuarioSesion.getCorreo());
                    }

                    String photoUrl=usuarioSesion.getFotoUrl();
                    if(photoUrl!=null){
                        if(!photoUrl.isEmpty()){
                            Drawable myIcon =  ResourcesCompat.getDrawable(getResources(), R.drawable.ic_outline_account_circle_black, null);
                            RequestOptions options = new RequestOptions()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL).dontTransform();

                            Glide.with(getApplicationContext())
                                    .load(photoUrl)
                                    .apply(options)
                                    .into(circleImageView).onLoadFailed(myIcon);
                        }
                    }

                    if(usuarioSesion.getListaIdsClinicas()!=null){
                        favoritosFragment.setListaUsuariosFavoritos(usuarioSesion.getListaIdsClinicas());
                        favoritosFragment.traerClinicasDeFirebase();
                    }else{
                        favoritosFragment.mostrarVacio();
                    }

                }
                ocultarMenuParaUsuariosPropietarios();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                ocultarMenuParaUsuariosPropietarios();
            }
        });
    }


    private void InicializarFragmentsTransactions(){
        fragmentManager.beginTransaction().add(R.id.fragmentContainer,favoritosFragment,"3").hide(favoritosFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragmentContainer,clinicasFragment,"2").hide(clinicasFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragmentContainer,ofertasFragment,"1").commit();
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);

        LinearLayout cerrarSesion = dialog.findViewById(R.id.layoutExit);
        TextView usuario= dialog.findViewById(R.id.txtUsuario);
        TextView correo= dialog.findViewById(R.id.txtCorreo);
        ImageView imgUsuario= dialog.findViewById(R.id.userProfilew);
        ImageView imgCerrarSesion = dialog.findViewById(R.id.imgCerrarSesion);
        Drawable myIcon =  ResourcesCompat.getDrawable(getResources(), R.drawable.ic_outline_account_circle_black, null);
        imgCerrarSesion.setColorFilter(R.color.color_error);

        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                CerrarSesionApp();
            }
        });

        usuario.setText(usuarioSesion.getUsuario());
        correo.setText(usuarioSesion.getCorreo());
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL).circleCrop();

        Glide.with(getApplicationContext())
                .load(circleImageView.getDrawable())
                .apply(options)
                .into(imgUsuario).onLoadFailed(myIcon);

        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                circleImageView.setEnabled(true);
            }
        });
    }

    private void CerrarSesionApp(){
        try{
            AuthUI.getInstance().signOut(getApplicationContext());
        }catch (Exception e){}
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(this, Splash.class);
        startActivity(intent);
        finish();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_ofert:{
                fragmentManager.beginTransaction().hide(fragmentActive).show(ofertasFragment).commit();
                fragmentActive = ofertasFragment;
                return true;
            }

            case R.id.nav_vetLocal:{
                fragmentManager.beginTransaction().hide(fragmentActive).show(clinicasFragment).commit();
                fragmentActive = clinicasFragment;
                return true;
            }

            case R.id.nav_favet:{
                fragmentManager.beginTransaction().hide(fragmentActive).show(favoritosFragment).commit();
                fragmentActive = favoritosFragment;
                return true;
            }
        }
        return true;
    }

}