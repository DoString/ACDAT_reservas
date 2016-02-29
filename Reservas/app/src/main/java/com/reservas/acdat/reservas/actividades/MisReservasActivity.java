package com.reservas.acdat.reservas.actividades;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.reservas.acdat.reservas.MainActivity;
import com.reservas.acdat.reservas.PeriodoOcupado;
import com.reservas.acdat.reservas.R;
import com.reservas.acdat.reservas.Reserva;
import com.reservas.acdat.reservas.Reservas;
import com.reservas.acdat.reservas.resultado.Result;
import com.reservas.acdat.reservas.usuarios.PeriodoLibre;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MisReservasActivity extends AppCompatActivity {

    private Switch aSwitch;
    private ImageButton imageButton;
    private ListView listView;
    private ArrayList<Reserva> list;
    private ArrayAdapter<Reserva> adapter;
    private ArrayAdapter<PeriodoLibre> adapter2;
    private ArrayAdapter<PeriodoOcupado> adapter3;
    private Reservas reservas;
    private ArrayList<PeriodoLibre> libres;
    private ArrayList<PeriodoOcupado> ocupados;
    private Spinner aulaSpin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_reservas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        reservas = (Reservas) getApplicationContext();

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


        final TextView cab = (TextView) findViewById(R.id.cab);
        aSwitch = (Switch) findViewById(R.id.switch1);
        listView = (ListView) findViewById(R.id.lista);
        imageButton = (ImageButton) findViewById(R.id.add);

        final String[] aulas = {"Audiovisuales", "Informatica", "Usos varios"};

        aulaSpin = (Spinner) findViewById(R.id.aulaSpin);
        LinearLayout layout = (LinearLayout) findViewById(R.id.aulas);



        layout.setVisibility(View.INVISIBLE);

        if (reservas.getAccion() == Reservas.LIBRES) {
            layout.setVisibility(View.INVISIBLE);
            libres = new ArrayList<>();
            imageButton.setVisibility(View.INVISIBLE);
            aSwitch.setVisibility(View.INVISIBLE);

            String aula = "";
            switch (reservas.getAula()) {
                case 1: aula = "Audiovisuales"; break;
                case 2: aula = "Informática"; break;
                case 3: aula = "Usos varios"; break;
            }
            cab.setText("Periodos libres: " + aula);

            adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, libres);
            listView.setAdapter(adapter2);

            mostrarLibres();

        } else if (reservas.getAccion() == Reservas.OCUPADOS) {
            imageButton.setVisibility(View.INVISIBLE);
            aSwitch.setVisibility(View.INVISIBLE);
            //layout.setVisibility(View.VISIBLE);

            ocupados = new ArrayList<>();

            String aula = "";
            switch (reservas.getAula()) {
                case 1: aula = "Audiovisuales"; break;
                case 2: aula = "Informática"; break;
                case 3: aula = "Usos varios"; break;
            }
            cab.setText("Periodos ocupados: " + aula);


            adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ocupados);
            listView.setAdapter(adapter3);

            mostrarOcupados();

        } else {
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reservas.setAccion(Reservas.ADD);
                    startActivityForResult(new Intent(MisReservasActivity.this, AddReservaActivity.class), Reservas.ADD);
                }
            });

            list = new ArrayList<>();
            adapter = new ArrayAdapter<Reserva>(this, android.R.layout.simple_list_item_1, list);

            listView.setAdapter(adapter);


            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    reservas.setSeleccionada((Reserva) parent.getItemAtPosition(position));

                    if (aSwitch.isChecked()) {
                        borrarReserva();
                    } else {
                        reservas.setAccion(Reservas.UPDATE);
                        startActivityForResult(new Intent(MisReservasActivity.this, AddReservaActivity.class), Reservas.UPDATE);
                    }
                    return true;
                }
            });
            mostrarReservas();
        }

    }

    private void mostrarOcupados() {

        RequestParams params = new RequestParams();
        params.put("aula", reservas.getAula());
        params.put("fechaIn", reservas.getFechaIn());
        params.put("fechaFin", reservas.getFechaFin());
        reservas.getClient().get(Reservas.URL_OCUPADOS_PERIODOS, params, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(MisReservasActivity.this, responseString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(MisReservasActivity.this, String.valueOf(errorResponse), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                Gson gson = new Gson();
                Result result = gson.fromJson(String.valueOf(response), Result.class);

                if (result != null) {
                    if (!result.getCode()) {
                        Toast.makeText(MisReservasActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {

                        if (result.getOcupados() != null) {

                            ocupados.addAll(result.getOcupados());
                            adapter3.notifyDataSetChanged();
                        } else {
                            Toast.makeText(MisReservasActivity.this, "No hay reservas disponibles", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(MisReservasActivity.this, "Se ha producido un error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void mostrarLibres() {
        //int id = reservas.getUsuario().getCuenta();
        RequestParams params = new RequestParams();
        params.put("aula", reservas.getAula());
        params.put("fechaIn", reservas.getFechaIn());
        params.put("fechaFin", reservas.getFechaFin());
        reservas.getClient().get(Reservas.URL_LIBRES, params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(MisReservasActivity.this, responseString, Toast.LENGTH_SHORT).show();
                Log.e("ERROR", responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(MisReservasActivity.this, String.valueOf(errorResponse), Toast.LENGTH_SHORT).show();
                Log.e("ERROR", String.valueOf(errorResponse));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                Gson gson = new Gson();
                Result result = gson.fromJson(String.valueOf(response), Result.class);

                if (result != null) {
                    if (!result.getCode()) {
                        Toast.makeText(MisReservasActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {

                        if (result.getLibres() != null) {

                            libres.addAll(result.getLibres());
                            adapter2.notifyDataSetChanged();
                        } else {
                            Toast.makeText(MisReservasActivity.this, "No hay reservas disponibles", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(MisReservasActivity.this, "Se ha producido un error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Reservas.ADD || requestCode == Reservas.UPDATE) {
            if (resultCode == Activity.RESULT_OK) {
                mostrarReservas();
            }
        }
    }

    private void borrarReserva() {
        int usuario = reservas.getUsuario().getId();
        final RequestParams params = new RequestParams();
        params.put("user", usuario);
        AlertDialog.Builder builder = new AlertDialog.Builder(MisReservasActivity.this)
                .setTitle("Borrar reserva")
                .setMessage(reservas.getSeleccionada().toString() + "\n" + "Esta seguro?");
        builder.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reservas.getClient().post(Reservas.URL_BORRAR + "/" + reservas.getSeleccionada().getId(),
                        params,
                        new JsonHttpResponseHandler() {

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);
                                Toast.makeText(MisReservasActivity.this, responseString, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                                Toast.makeText(MisReservasActivity.this, String.valueOf(errorResponse), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                Gson gson = new Gson();
                                Result result = gson.fromJson(String.valueOf(response), Result.class);

                                if (result != null && result.getCode()) {
                                    mostrarReservas();
                                    Toast.makeText(MisReservasActivity.this, "Se ha borrado la reserva", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (result != null && result.getMessage() != null)
                                        Toast.makeText(MisReservasActivity.this, "Error: " + result.getMessage(), Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(MisReservasActivity.this, "No se ha borrado la reserva debido a un error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    private void mostrarReservas() {
        int id = reservas.getUsuario().getId();
        RequestParams params = new RequestParams();
        params.put("user", id);
        reservas.getClient().get(Reservas.URL_RESERVAS_MIAS, params, new JsonHttpResponseHandler(){
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(MisReservasActivity.this, responseString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(MisReservasActivity.this, String.valueOf(errorResponse), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                Gson gson = new Gson();
                Result result = gson.fromJson(String.valueOf(response), Result.class);

                if (result != null) {
                    if (!result.getCode()) {
                        Toast.makeText(MisReservasActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {

                        if (result.getReservas() != null) {
                            list.clear();
                            list.addAll(result.getReservas());
                            adapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    Toast.makeText(MisReservasActivity.this, "Se ha producido un error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
