package com.example.petlover;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.petlover.databinding.ActivityResetPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    ActivityResetPasswordBinding binding;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_reset_password);
        binding= ActivityResetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);

        binding.txtVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.textCorreo.getText()!=null && !binding.textCorreo.getText().toString().isEmpty()){
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setMessage("Procesando....");
                    progressDialog.show();
                    String correoReset= binding.textCorreo.getText().toString();
                    firebaseAuth.setLanguageCode("es");
                    firebaseAuth.sendPasswordResetEmail(correoReset).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                                builder.setTitle("El correo ha sido enviado con éxito");
                                builder.setIcon(R.drawable.ic_baseline_check_circle_outline_24);
                                builder.setMessage("Puede revisar su correo y proseguir con el proceso de recuperación...");
                                // Add the buttons
                                builder.setPositiveButton("Regresar al login", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        startActivity(new Intent(ResetPasswordActivity.this, LoginInicial.class));
                                        finish();
                                        dialog.dismiss();
                                    }
                                });
                                // Create the AlertDialog
                                AlertDialog dialog = builder.create();
                                dialog.show();

                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"Ha ocurrido un error: no se ha enviado el correo.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(getApplicationContext(),getString(R.string.ingrese_email_valido),Toast.LENGTH_SHORT).show();
                    binding.textCorreo.setError(getString(R.string.ingrese_email_valido));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        volverAlLogin();
    }

    private void volverAlLogin() {
        startActivity(new Intent(ResetPasswordActivity.this, LoginInicial.class));
        finish();
    }

}