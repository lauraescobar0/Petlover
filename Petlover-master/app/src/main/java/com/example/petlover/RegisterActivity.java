package com.example.petlover;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.petlover.comun.FirebaseCloudFirestoreAPI;
import com.example.petlover.databinding.ActivityRegisterBinding;
import com.example.petlover.pojo.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseCloudFirestoreAPI cloudFirestoreAPI;
    Usuario usuarioRegistrando;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register);
        binding=ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        cloudFirestoreAPI=FirebaseCloudFirestoreAPI.getFirestoreInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        firebaseAuth.setLanguageCode("es");
        binding.btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegistrarUsuario();
            }
        });

        binding.txtVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void RegistrarUsuario() {
        String usuario=binding.textNombreUsuario.getText().toString();
        String correo=binding.textCorreo.getText().toString();
        String pass=binding.textpassword.getText().toString();
        String pass2=binding.textpassWordConfirmar.getText().toString();

        if(EsUnUsuarioValido(usuario,correo,pass,pass2)){
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Procesando....");
            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(correo, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAGUSERCREATED", "createUserWithEmail:success");
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                usuarioRegistrando=new Usuario();
                                usuarioRegistrando.setCorreo(correo);
                                usuarioRegistrando.setUsuario(usuario);
                                usuarioRegistrando.setRolUsuario(Usuario.ROL_USUARIO);
                                usuarioRegistrando.setProveedor("Correo");
                                cloudFirestoreAPI.getFirestoreReference().collection(FirebaseCloudFirestoreAPI.PathUsers).document(user.getUid()).set(usuarioRegistrando).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        // Creando el perfil del usuario en Firestore DB
                                        progressDialog.dismiss();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                        builder.setTitle("Registro exitoso");
                                        builder.setIcon(R.drawable.ic_baseline_check_circle_outline_24);
                                        builder.setMessage("Se ha creado su cuenta de usuario, ya podrá ingresar a la aplicación...");
                                        // Add the buttons
                                        builder.setPositiveButton("Regresar al login para iniciar sesión", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                firebaseAuth.signOut();
                                                startActivity(new Intent(RegisterActivity.this, LoginInicial.class));
                                                finish();
                                                dialog.dismiss();
                                            }
                                        });
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                    }
                                });

                            } else {
                                Log.w("TAGUSERCREATEDERROR", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Ha ocurrido un error en la autenticación, No se ha creado el registro...",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


        }
    }

    private boolean EsUnUsuarioValido(String user, String correo, String contra1, String contra2){
        boolean valido=true;

        if(user==null || user.isEmpty()){
            valido=false;
            binding.textNombreUsuario.setError(getString(R.string.ingrese_usuario_valido));
        }

        if(correo==null || correo.isEmpty()){
            valido=false;
            binding.textCorreo.setError(getString(R.string.ingrese_email_valido));
        }

        if(contra1==null || contra1.isEmpty()){
            valido=false;
            binding.textpassword.setError(getString(R.string.ingrese_contrasenia_valida));
        }

        if(contra1!=null && contra1.length()<=8){
            valido=false;
            binding.textpassword.setError(getString(R.string.utilice_al_menos_8_caracteres));
        }

        if(contra2==null || contra2.isEmpty()){
            valido=false;
            binding.textpassWordConfirmar.setError(getString(R.string.ingrese_contrasenia_valida));
        }

        if(contra1!=null && contra2!=null && !contra1.isEmpty() && !contra2.isEmpty()){
            if(!contra1.equals(contra2)){ //Si son distintas
                valido=false;
                binding.textpassWordConfirmar.setError("Las contraseñas no coínciden...");
            }
        }

        return valido;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        volverAlLogin();
    }

    private void volverAlLogin() {
        startActivity(new Intent(RegisterActivity.this, LoginInicial.class));
        finish();
    }

}