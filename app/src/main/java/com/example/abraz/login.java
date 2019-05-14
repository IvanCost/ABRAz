package com.example.abraz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.abraz.config.ConfiguracaoFirebase;
import com.example.abraz.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {
    private TextInputEditText campoEmail, campoSenha;
    private FirebaseAuth autenticacao;
    private ProgressBar progresbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //FirebaseApp.initializeApp(this);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.login );


        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        campoEmail = findViewById(R.id.edit_login_email);
        campoSenha = findViewById(R.id.edit_login_senha);
        progresbar = findViewById(R.id.progressLogin);

        progresbar.setVisibility(View.GONE);

    }

    public void logarUsuario (Usuario usuario){

        progresbar.setVisibility(View.VISIBLE);


        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful() ){
                    progresbar.setVisibility(View.GONE);
                    abrirHome();

                }else{

                    String excecao = "";
                    try{
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e) {
                        excecao = "E-mail não cadastrado";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "E-mail e senha não correspondem a um usuário válido!";
                    } catch (Exception e) {
                        excecao = "Erro ao logar usuário" + e.getMessage(); e.printStackTrace();
                    }
                    Toast.makeText( login.this, "Erro ao fazer longin!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void ValidarAutenticacaoUsuario (View view){


        String textoEmail = campoEmail.getText().toString();
        String textoSenha = campoSenha.getText().toString();

        if (!textoEmail.isEmpty()) {
            if (!textoSenha.isEmpty()) {
                Usuario usuario = new Usuario();
                usuario.setEmail(textoEmail);
                usuario.setSenha(textoSenha);

                logarUsuario(usuario);

            } else {
                Toast.makeText( login.this, "Preencha com a senha!", Toast.LENGTH_SHORT).show();
            }

        }else {
            Toast.makeText( login.this, "Preencha com o e-mail!", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser usuarioAtual = autenticacao.getCurrentUser();
        if(usuarioAtual != null){
            abrirHome();
        }
    }

    public void abrirTelaCadastro (View view){
        Intent intent = new Intent ( login.this, cadastro.class);
        startActivity(intent);
    }

    private void abrirHome (){
        Intent intent = new Intent ( login.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
