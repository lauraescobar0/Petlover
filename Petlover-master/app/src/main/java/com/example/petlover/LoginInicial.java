package com.example.petlover;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.petlover.comun.FirebaseCloudFirestoreAPI;
import com.example.petlover.databinding.ActivityLoginInicialBinding;
import com.example.petlover.pojo.Usuario;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.example.petlover.comun.sharedPreferencesUtils;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Arrays;
import java.util.List;

public class LoginInicial extends AppCompatActivity{

    private static final int RC_SIGN_IN = 1000;
    ActivityLoginInicialBinding binding;
    FirebaseAuth firebaseAuth;

    // See: https://developer.android.com/training/basics/intents/result
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResultGoogle(result);
                }
            }
    );

    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.GoogleBuilder().build());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginInicialBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        firebaseAuth=FirebaseAuth.getInstance();
        binding.btnSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IniciarSesion();
            }
        });

        binding.signInButtonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loguearseConGoogle();
            }
        });

        binding.txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginInicial.this, RegisterActivity.class));
                finish();
            }
        });

        binding.txtReestablecer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginInicial.this, ResetPasswordActivity.class));
                finish();
            }
        });

    }

    private void loguearseConGoogle() {
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        signInLauncher.launch(signInIntent);
    }

  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
            }
        }
    }*/

    private void onSignInResultGoogle(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Usuario usuarioIniciando=new Usuario();
            usuarioIniciando.setId(user.getUid());
            usuarioIniciando.setCorreo(user.getEmail());
            usuarioIniciando.setRolUsuario(Usuario.ROL_USUARIO);
            usuarioIniciando.setUsuario(user.getDisplayName());
            usuarioIniciando.setFotoUrl(user.getPhotoUrl().getPath());
            usuarioIniciando.setProveedor("Google");

            FirebaseCloudFirestoreAPI.getFirestoreInstance().getFirestoreReference().collection(FirebaseCloudFirestoreAPI.PathUsers).document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){ //Ya existe
                        if(task.getResult().toObject(Usuario.class)!=null){
                            startActivity(new Intent(LoginInicial.this,MainActivity.class));
                            finish();
                        }else{ //Registrarlo en Firestore
                            FirebaseCloudFirestoreAPI.getFirestoreInstance().getFirestoreReference().collection(FirebaseCloudFirestoreAPI.PathUsers).document(user.getUid()).set(usuarioIniciando).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    startActivity(new Intent(LoginInicial.this,MainActivity.class));
                                    finish();
                                }
                            });
                        }
                    }else{ //Iniciar de todos modos
                        startActivity(new Intent(LoginInicial.this,MainActivity.class));
                        finish();
                    }
                }
            });

        } else {
            Toast.makeText(getApplicationContext(),"Ha ocurrido un error, no se pudo iniciar sesión con Google",Toast.LENGTH_LONG).show();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(this, authResult -> {

                    FirebaseUser firebaseUser = authResult.getUser();
                    //Registrar Usuario en firebase firestore
                    Usuario usuario= new Usuario(); // Datos básicos desde Google
                    usuario.setId(authResult.getUser().getUid());
                    usuario.setUsuario(acct.getDisplayName());
                    usuario.setProveedor("Google");
                    usuario.setFotoUrl(acct.getPhotoUrl().getPath());
                    FirebaseCloudFirestoreAPI firebaseCloudFirestoreAPI=FirebaseCloudFirestoreAPI.getFirestoreInstance();
                    firebaseCloudFirestoreAPI.getFirestoreReference()
                            .collection(FirebaseCloudFirestoreAPI.PathUsers)
                            .document(authResult.getUser().getUid()).set(usuario)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    startActivity(new Intent(LoginInicial.this, MainActivity.class));
                                    finish();
                                }
                            });
                })
                .addOnFailureListener(this, e -> Toast.makeText(LoginInicial.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show());
    }

    public void IniciarSesion(){
        String user=binding.emailSesion.getText().toString();
        String pass=binding.contraSesion.getText().toString();
        if(validarVacio(user,pass)){
            firebaseAuth.signInWithEmailAndPassword(user,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    //Traer desde SharedPreferences
                    boolean yaVioDemo= sharedPreferencesUtils.getSharedPreferencesBoolean(getApplicationContext(),getString(R.string.claveDemostracion),false);
                    if(yaVioDemo){ // Ya ha visto la demostración, irá al MainActivity
                        startActivity(new Intent(LoginInicial.this,MainActivity.class));
                    }else{
                        startActivity(new Intent(LoginInicial.this, AppDemostration.class));
                    }
                }
            }).addOnFailureListener(new OnFailureListener() { // No pudo iniciar sesión, mostrar mensaje informativo
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        binding.contraSesion.setError(getString(R.string.loginAuthCredentialError));
                        Toast.makeText(getApplicationContext(),getString(R.string.loginAuthCredentialError),Toast.LENGTH_SHORT).show();

                    } else if (e instanceof FirebaseAuthInvalidUserException) {

                        String errorCode = ((FirebaseAuthInvalidUserException) e) .getErrorCode ();

                        if (errorCode.equals ("ERROR_USER_NOT_FOUND")) {
                            binding.contraSesion.setError(getString(R.string.loginAUthCredentrialEmailError));
                            Toast.makeText(getApplicationContext(),getString(R.string.loginAUthCredentrialEmailError),Toast.LENGTH_LONG).show();
//                            callBackMsg.onError(LoginEvent.ERRORCREDENTIALS,R.string.loginAUthCredentrialEmailError);
                        } else if (errorCode.equals ("ERROR_USER_DISABLED")) {
                            binding.contraSesion.setError(getString(R.string.loginAuthErrorDisabledUser));
                            Toast.makeText(getApplicationContext(),getString(R.string.loginAuthErrorDisabledUser),Toast.LENGTH_LONG).show();
//                            callBackMsg.onError(LoginEvent.ERRORUSERBLOCK,R.string.loginAuthErrorDisabledUser);
                        } else {
                            binding.contraSesion.setError(getString(R.string.loginAuthErrorServermsg));
                            Toast.makeText(getApplicationContext(),getString(R.string.loginAuthErrorServermsg),Toast.LENGTH_LONG).show();
//                            callBackMsg.onError(LoginEvent.ERRORSERVER,R.string.loginAuthErrorServermsg);
                        }
                    }
                }
            });
        }
    }

    private boolean validarVacio(String user, String pass) {
        boolean valido=true;

        if(user.isEmpty()){
            valido=false;
            binding.emailSesion.setError(getString(R.string.ingrese_email_valido));
        }

        if(pass.isEmpty()){
            valido=false;
            binding.contraSesion.setError(getString(R.string.ingrese_contrasenia_valida));
        }

        return valido;
    }

}