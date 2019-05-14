package com.example.abraz;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.abraz.config.ConfiguracaoFirebase;
import com.example.abraz.helper.UsuarioFirebase;
import com.example.abraz.helper.permissao;
import com.example.abraz.model.Usuario;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class MeuPerfil extends AppCompatActivity {

    ImageView fotoPerfil;

    TextView nomePerfil;

    FirebaseAuth autenticacao;

    UsuarioFirebase teste;

    private String identificadorUsuario;
    private EditText editnomeConfig;
    private ImageView AtulizarNomeUSer;
    private Usuario usuarioLogado;

    private ImageButton ImageButtonCamera;
    private ImageButton ImageButtonGaleria;

    FirebaseUser usuario = UsuarioFirebase.getUusarioAtual();




    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };


    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;
    private ImageView circleImageViewPerfil;
    private StorageReference storageReference;
    private String identificadorDoUsuario;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_meu_perfil );

        Toolbar toolbar = findViewById(R.id.toolbar_meuPerfil);
        //setSupportActionBar(toolbar);
        getSupportActionBar().setTitle( "Meu Perfil" );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        nomePerfil = (TextView) findViewById( R.id.nomeUsuario );

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();


        storageReference = ConfiguracaoFirebase.getFirebaseStorage();
        identificadorDoUsuario = UsuarioFirebase.getIdentificadorUsuario();
        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();

        permissao.validarPermissoes(permissoesNecessarias, this, 1);



        FirebaseUser firebaseUser = getUusarioAtual();

        //usuario.setNome(firebaseUser.getDisplayName());

        String nomePerfiltexto =  ((String)(firebaseUser.getDisplayName()));
        nomePerfil.setText( nomePerfiltexto );

        circleImageViewPerfil = findViewById( R.id.circleImageViewPerfil );

        getDadosUsuarioLogado();

        identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        ImageButtonCamera = findViewById(R.id.imageButtoncamera);
        ImageButtonGaleria = findViewById(R.id.imageButtongaleria);


        //Recuperar dados do usuário

        FirebaseUser usuario = UsuarioFirebase.getUusarioAtual();
        Uri url =  usuario.getPhotoUrl();

        if(url != null){
            Glide.with(MeuPerfil.this)
                    .load( url )
                    .into( circleImageViewPerfil );

        }else{
            circleImageViewPerfil.setImageResource( R.drawable.bigperfilusr);
        }

        //Atulializar nome de usuario



        ImageButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE);
                if(intent.resolveActivity(getPackageManager())!=null) {
                    startActivityForResult(intent, SELECAO_CAMERA);
                }


            }
        });

        ImageButtonGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if(intent.resolveActivity(getPackageManager())!=null) {
                    startActivityForResult(intent, SELECAO_GALERIA);
                }


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bitmap imagem = null;

            try{

                switch (requestCode){
                    case SELECAO_CAMERA:
                        imagem=(Bitmap) data.getExtras().get("data");
                        break;
                    case SELECAO_GALERIA:
                        Uri localImagemSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(),localImagemSelecionada);
                        break;
                }

                if(imagem!=null){
                    circleImageViewPerfil.setImageBitmap(imagem);



                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    imagem.compress(Bitmap.CompressFormat.JPEG,70, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    //salvar imagem no firebase

                    final StorageReference imagemRef = storageReference
                            .child("imagens")
                            .child("perfil")
                            .child(identificadorDoUsuario + ".jpeg");

                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.continueWithTask( new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return imagemRef.getDownloadUrl();
                        }
                    } ).addOnCompleteListener( new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri url = task.getResult();
                                atualizaFotoUsuario( url );
                            } else {
                                Toast.makeText( MeuPerfil.this, "Erro ao fazer upload da imagem",
                                        Toast.LENGTH_SHORT ).show();

                            }
                        }
                    } );

                }

            } catch (Exception e){
                e.printStackTrace();

            }


        }
    }


    public static FirebaseUser getUusarioAtual() {
        FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAutenticacao();
        return usuario.getCurrentUser();
    }
    public static Usuario getDadosUsuarioLogado() {

        FirebaseUser firebaseUser = getUusarioAtual();

        Usuario usuario = new Usuario();
        usuario.setEmail(firebaseUser.getEmail());
        usuario.setNome(firebaseUser.getDisplayName());

        return usuario;

    }



    public void atualizaFotoUsuario(Uri url){
        boolean retorno = UsuarioFirebase.atualizarFotoUsuario(url);
        if ( retorno ){
            usuarioLogado.setFoto( url.toString() );
            usuarioLogado.atualizar();

            Toast.makeText(MeuPerfil.this,
                    "Sua foto foi alterada!",
                    Toast.LENGTH_SHORT).show();

        }

    }
}
