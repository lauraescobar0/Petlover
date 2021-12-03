package com.example.petlover.agregarUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.petlover.MainActivity;
import com.example.petlover.R;
import com.example.petlover.comun.FirebaseCloudFirestoreAPI;
import com.example.petlover.databinding.ActivityGestionUsuarioBinding;
import com.example.petlover.pojo.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.jakewharton.processphoenix.ProcessPhoenix;

import java.util.ArrayList;
import java.util.List;

public class gestionUsuario extends AppCompatActivity {

    ActivityGestionUsuarioBinding binding;
    Drawable imgPorDefecto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityGestionUsuarioBinding.inflate(getLayoutInflater());
//        setContentView(R.layout.activity_gestion_usuario);
        setContentView(binding.getRoot());
        CargarDatosUsuario();
        imgPorDefecto= ResourcesCompat.getDrawable(getApplicationContext().getResources(), R.drawable.vaciogeneral, null);
        setSupportActionBar(binding.topAppBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.btnCambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MainActivity.usuarioSesion.getRolUsuario().equals(Usuario.ROL_USUARIO)){
                    MainActivity.usuarioSesion.setRolUsuario(Usuario.ROL_PROPIETARIO);

                    AlertDialog.Builder builder = new AlertDialog.Builder(gestionUsuario.this);
                    builder.setTitle("Darse de alta como propietario");
                    builder.setIcon(R.drawable.ic_baseline_account_circle_violet);
                    builder.setMessage("¿Está seguro que desea actualizar su cuenta a cuenta de propietario de clínica? Se requiere reiniciar la aplicación.");
                    // Add the buttons
                    builder.setPositiveButton("Actualizar a propietario", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String currentUid= FirebaseAuth.getInstance().getCurrentUser().getUid();
                            Usuario temp=MainActivity.usuarioSesion;
                            FirebaseCloudFirestoreAPI.getFirestoreInstance().getFirestoreReference().collection(FirebaseCloudFirestoreAPI.PathUsers).document(currentUid).set(temp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        ProcessPhoenix.triggerRebirth(getApplicationContext());
                                    }else{
                                        Toast.makeText(getApplicationContext(),"No se pudo realizar la actualización",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        binding.btnGuardarANP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fotoURl=binding.textFotoURL.getText().toString();
                String usuario=binding.textNombreUsuario.getText().toString();
                String nombres=binding.textNombre.getText().toString();
                String apellidos=binding.textApellido.getText().toString();
                String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();

                if(!usuario.isEmpty()){
                    MainActivity.usuarioSesion.setUsuario(usuario);
                }

                if(!nombres.isEmpty()){
                    MainActivity.usuarioSesion.setNombres(nombres);
                }

                if(!apellidos.isEmpty()){
                    MainActivity.usuarioSesion.setApellidos(apellidos);
                }

                if(!fotoURl.isEmpty()){
                    MainActivity.usuarioSesion.setFotoUrl(fotoURl);
                }
                Usuario temp=MainActivity.usuarioSesion;
                FirebaseCloudFirestoreAPI.getFirestoreInstance().getFirestoreReference().collection(FirebaseCloudFirestoreAPI.PathUsers).document(uid).set(temp).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Se han actualizado los datos",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        binding.textRol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"El rol debe cambiarse a través del siguiente botón...",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void CargarDatosUsuario(){
        if(MainActivity.usuarioSesion!=null){

            if(MainActivity.usuarioSesion.getUsuario()!=null){
                binding.textNombreUsuario.setText(MainActivity.usuarioSesion.getUsuario());
            }

            if(MainActivity.usuarioSesion.getRolUsuario()!=null){
                binding.textRol.setText(MainActivity.usuarioSesion.getRolUsuario());
                if(MainActivity.usuarioSesion.getRolUsuario().equals("")){
                    binding.btnCambiar.setEnabled(false);
                }
            }

            if(MainActivity.usuarioSesion.getCorreo()!=null){
                binding.textCorreo.setText(MainActivity.usuarioSesion.getCorreo());
            }

            if(MainActivity.usuarioSesion.getNombres()!=null){
                binding.textNombre.setText(MainActivity.usuarioSesion.getNombres());
            }

            if(MainActivity.usuarioSesion.getApellidos()!=null){
                binding.textApellido.setText(MainActivity.usuarioSesion.getApellidos());
            }

            if(MainActivity.usuarioSesion.getFotoUrl()!=null){
                binding.textFotoURL.setText(MainActivity.usuarioSesion.getFotoUrl());
                Glide.with(getApplicationContext())
                        .load(MainActivity.usuarioSesion.getFotoUrl())
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .centerCrop())
                        .into(binding.imgfoto).onLoadFailed(imgPorDefecto);
            }
        }
    }
}