package com.reservas.acdat.reservas.actividades;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.reservas.acdat.reservas.R;
import com.reservas.acdat.reservas.Reservas;
import com.reservas.acdat.reservas.resultado.Result;
import com.reservas.acdat.reservas.usuarios.Usuario;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class AdminActivity extends AppCompatActivity {

    private Reservas reservas;
    private ArrayList<Usuario> lista;
    private ArrayAdapter<Usuario> adapter;
    private ListView listView;
    private TextView textView;
    private EditText asunto;
    private EditText mensaje;
    private ArrayList<String> mails;
    private EditText pass;
    private TextView mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarMensaje();
                Snackbar.make(view, "Enviando mensaje", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mail = (TextView) findViewById(R.id.mail);
        pass = (EditText) findViewById(R.id.pass);
        listView = (ListView) findViewById(R.id.profes);
        textView = (TextView) findViewById(R.id.lbladmin);
        asunto = (EditText) findViewById(R.id.asunto);
        mensaje = (EditText) findViewById(R.id.mensaje);

        reservas = (Reservas) getApplicationContext();

        mail.setText(reservas.getUsuario().getEmail());

        textView.setText(reservas.getUsuario().getNombre() + ", " + reservas.getUsuario().getApellidos());

        lista = new ArrayList<>();
        adapter = new ArrayAdapter<Usuario>(this, android.R.layout.simple_list_item_checked, lista);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        mails = new ArrayList<>();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (Long i : listView.getCheckedItemIds()) {
                    if (id == i) {
                        listView.setItemChecked(position, false);
                        break;
                    }
                    else
                        listView.setItemChecked(position, true);
                }

                Usuario user = (Usuario) parent.getItemAtPosition(position);
                if (mails.contains(user.getEmail()))
                    mails.remove(user.getEmail());
                else
                    mails.add(user.getEmail());
            }
        });

        cargarLista();
    }

    private void enviarMensaje() {
        RequestParams params = new RequestParams();
        params.put("subject", asunto.getText().toString());
        params.put("message", mensaje.getText().toString());
        ArrayList<String> emails = new ArrayList<>();

        for (Usuario user : lista) {
                emails.add(user.getEmail());
        }
        params.put("sender", reservas.getUsuario().getEmail());
        params.put("emails", emails.toArray());
        params.put("pass", pass.getText().toString());

        Toast.makeText(AdminActivity.this, pass.getText().toString(), Toast.LENGTH_SHORT).show();

        reservas.getClient().post(Reservas.URL_EMAIL, params, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(AdminActivity.this, responseString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(AdminActivity.this, String.valueOf(errorResponse), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                Result result = new Gson().fromJson(String.valueOf(response), Result.class);

                if (result != null && result.getCode()) {
                    Toast.makeText(AdminActivity.this, "Mensaje enviado correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AdminActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void cargarLista() {
        reservas.getClient().get(this, Reservas.URL_USERS, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(AdminActivity.this, responseString, Toast.LENGTH_SHORT).show();
                Log.e("ERROR", responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(AdminActivity.this, String.valueOf(errorResponse), Toast.LENGTH_SHORT).show();
                Log.e("ERROR", String.valueOf(errorResponse));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                Result result = new Gson().fromJson(String.valueOf(response), Result.class);

                if (result != null && result.getCode()) {
                    lista.clear();
                    for (Usuario user : result.getUsers()) {
                        if (user.getNivel() < 1)
                            lista.add(user);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(AdminActivity.this, "Error al obtener los datos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
