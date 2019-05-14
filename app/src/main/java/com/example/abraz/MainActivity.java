package com.example.abraz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        createDrawer();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createDrawer() {


        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar);
        toolbar.setTitle( "Home" );


        //( "Feed" );
        //final TextView textView = (TextView) findViewById(R.id.tvimenu);




        //Itens do Drawer
        final PrimaryDrawerItem nav_inicio = new PrimaryDrawerItem().withIdentifier( 1 ).withName( "Home" ).withIcon(R.drawable.homeico);
        final PrimaryDrawerItem nav_perfil = new PrimaryDrawerItem().withIdentifier( 2 ).withName( "Meu Perfil").withIcon(R.drawable.perfilusr);
        final PrimaryDrawerItem nav_feedback = new PrimaryDrawerItem().withIdentifier( 1 ).withName( "Feedback" ).withIcon(R.drawable.feedbackico);
        final PrimaryDrawerItem nav_share = new PrimaryDrawerItem().withIdentifier( 2 ).withName( "Compartilhar" ).withIcon(R.drawable.shareico);
        final PrimaryDrawerItem nav_about = new PrimaryDrawerItem().withIdentifier( 2 ).withName( "Sobre" ).withIcon(R.drawable.infoico);
        final PrimaryDrawerItem nav_logout = new PrimaryDrawerItem().withIdentifier( 2 ).withName( "Logout" ).withIcon(R.drawable.logoutico);
        final PrimaryDrawerItem nav_EstagDoe = new PrimaryDrawerItem().withIdentifier( 2 ).withName( "Estágios da Doença" ).withIcon(R.drawable.prescription);
        final PrimaryDrawerItem nav_faq = new PrimaryDrawerItem().withIdentifier( 2 ).withName( "Perguntas Frequentes" ).withIcon(R.drawable.faqico);



        //Definição do Drawer
        Drawer drawer = new DrawerBuilder()
                .withActivity( this )
                .withToolbar(toolbar)
                .withHeader( R.layout.nav_header)
                .addDrawerItems(
                        nav_inicio,
                        new DividerDrawerItem(),//Divisor
                        nav_perfil,
                        new DividerDrawerItem(),
                        nav_EstagDoe,
                        new DividerDrawerItem(),
                        nav_faq,
                        new DividerDrawerItem(),
                        nav_share,
                        new DividerDrawerItem(),
                        nav_about,
                        new DividerDrawerItem(),
                        nav_logout

                )
                .withOnDrawerItemClickListener( new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        if(drawerItem == nav_inicio) abrirHome();
                        else if (drawerItem == nav_perfil) abrirPerfil();
                        else if (drawerItem == nav_EstagDoe) abrirEstagios();
                        else if (drawerItem == nav_faq) abriFaq();
                        else if (drawerItem == nav_share) abrirShare();
                        else if (drawerItem == nav_about) abrirAbout();
                        else if (drawerItem == nav_logout) logout();

                        return false;
                    }
                } )
                //.withSelectedItemByPosition( 0 )
                .build();
    }


    public void abrirHome(){
        Intent editProfile = new Intent(MainActivity.this, MainActivity.class);
        startActivity(editProfile);
        finish();
    }

    public void abrirPerfil(){
        Intent intent=new Intent(MainActivity.this,MeuPerfil.class);
        startActivity(intent);
        finish();
    }

    public void abrirEstagios(){
        Intent intent=new Intent(MainActivity.this,Estagios.class);
        startActivity(intent);
        finish();
    }

    public void abriFaq() {
        Intent i = new Intent(getApplicationContext(), Faq.class );
        startActivity(i);
        finish();
    }

    public void abrirShare(){
        Intent share=new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        String body="Baixe o Abraz!";
        String sub="Insira aqui informações sobre o app";
        share.putExtra(Intent.EXTRA_TEXT,body);
        share.putExtra(Intent.EXTRA_SUBJECT,sub);
        share.putExtra(Intent.EXTRA_TEXT,"https://drive.google.com");
        startActivity(Intent.createChooser(share,"Compartilhar usando:"));

    }

    public void abrirAbout(){
        Intent intent=new Intent(MainActivity.this,Informacoes.class);
        startActivity(intent);
        finish();
    }

    public void logout(){

        SharedPreferences sharedPreferences=getSharedPreferences("Data",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("email","");
        editor.putString("password","");
        editor.commit();

        Intent login=new Intent(MainActivity.this,login.class);
        startActivity(login);
        finish();
    }
}

