package com.reservas.acdat.reservas;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.reservas.acdat.reservas.actividades.AddReservaActivity;
import com.reservas.acdat.reservas.actividades.MisReservasActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

    Reservas reservas;
    private TextView usuario;
    Button misReservas,reservasAula,libres;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }); */

        usuario = (TextView) findViewById(R.id.user);
        misReservas = (Button) findViewById(R.id.r1);
        reservasAula = (Button) findViewById(R.id.r2);
        libres = (Button) findViewById(R.id.r3);

        //-- callbacks
        misReservas.setOnClickListener(this);
        reservasAula.setOnClickListener(this);
        libres.setOnClickListener(this);

        reservas = (Reservas) getApplicationContext();

        if (reservas.getUsuario() != null)
            usuario.setText(reservas.getUsuario().getNombre() + ", " + reservas.getUsuario().getApellidos());
        else {
            Toast.makeText(MainActivity.this, "Error al obtener el usuario", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onClick(View v) {
        int id = v.getId();


        switch (id) {
            case R.id.r1:// mis reservas
                startActivity(new Intent(this, MisReservasActivity.class));
                reservas.setAccion(-1);
                break;
            case R.id.r2:// reservas por aula
                reservas.setAccion(Reservas.OCUPADOS);
                startActivity(new Intent(this, AddReservaActivity.class));
                break;
            case R.id.r3:// periodos libres
                reservas.setAccion(Reservas.LIBRES);
                startActivity(new Intent(this, AddReservaActivity.class));
                break;
            default:
                break;
        }
    }
}
