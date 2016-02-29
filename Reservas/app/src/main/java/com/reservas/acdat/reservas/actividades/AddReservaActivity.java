package com.reservas.acdat.reservas.actividades;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.reservas.acdat.reservas.R;
import com.reservas.acdat.reservas.Reserva;
import com.reservas.acdat.reservas.Reservas;
import com.reservas.acdat.reservas.resultado.Result;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class AddReservaActivity extends AppCompatActivity implements View.OnClickListener
{

    private EditText desde, hasta;
    private TextView dia;
    private DatePickerDialog dialog;
    private Reservas reservas;
    private Spinner aula, hora;
    private Button button;
    private boolean puedeInsertar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reserva);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        reservas = (Reservas) getApplicationContext();

        TextView lbldia = (TextView) findViewById(R.id.lbldia);
        LinearLayout layout = (LinearLayout) findViewById(R.id.lHoras);

        button = (Button) findViewById(R.id.guardar);
        aula = (Spinner) findViewById(R.id.spinAula);
        hora = (Spinner) findViewById(R.id.spinHora);
        desde = (EditText) findViewById(R.id.desde);
        hasta = (EditText) findViewById(R.id.hasta);
        dia = (TextView) findViewById(R.id.dia);
        final Calendar calendar = Calendar.getInstance();
        button.setOnClickListener(this);

        puedeInsertar = true;

        if (reservas.getAccion() == Reservas.LIBRES) {
            button.setText("Ver Periodos libres");
            dia.setVisibility(View.INVISIBLE);
            hora.setVisibility(View.INVISIBLE);
            lbldia.setVisibility(View.INVISIBLE);
            layout.setVisibility(View.INVISIBLE);

        }

        String[] horas = {"08:15 - 09:15",
                "09:15 - 10:15",
                "10:15 - 11:15",
                "11:45 - 12:45",
                "12:45 - 13:45",
                "13:45 - 14:45"};

        String[] aulas = {"Audiovisuales", "Informatica", "Usos varios"};

        aula.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, aulas));

        hora.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, horas));


        if (reservas.getAccion() == Reservas.UPDATE) {
            String[] periodo = reservas.getSeleccionada().getPeriodo().split(" - ");
            desde.setText(periodo[0]);

            if (periodo.length < 2)
                hasta.setText(periodo[0]);
            else
                hasta.setText(periodo[1]);

            hora.setSelection(reservas.getSeleccionada().getHoraInt() - 1);
            aula.setSelection(reservas.getSeleccionada().getAula() - 1);

            String[] d = desde.getText().toString().split("-");

            calendar.set(Calendar.YEAR, Integer.valueOf(d[0]));
            calendar.set(Calendar.MONTH, Integer.valueOf(d[1]) - 1);
            calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(d[2]));

            int _dia = calendar.get(Calendar.DAY_OF_WEEK);

            switch (_dia) {
                case 2:
                    dia.setText("Lunes");
                    break;
                case 3:
                    dia.setText("Martes");
                    break;
                case 4:
                    dia.setText("Miercoles");
                    break;
                case 5:
                    dia.setText("Jueves");
                    break;
                case 6:
                    dia.setText("Viernes");
                    break;
                case 7:
                    dia.setText("Sabado");
                    break;
                case 1:
                    dia.setText("Domingo");
                    break;
            }

        }

        if (reservas.getAccion() == Reservas.OCUPADOS) {
            button.setText("Ver Periodos Ocupados");
            dia.setVisibility(View.INVISIBLE);
            hora.setVisibility(View.INVISIBLE);
            lbldia.setVisibility(View.INVISIBLE);
            layout.setVisibility(View.INVISIBLE);
        }


        desde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fechaInicio(calendar);
            }
        });

        hasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fechaFin(calendar);
            }
        });
    }

    private void fechaFin(final Calendar calendar) {
        dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                hasta.setText(String.valueOf(year) + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth));
                calendar.set(year, monthOfYear, dayOfMonth);
                int _dia = calendar.get(Calendar.DAY_OF_WEEK);

                String diaFin = "";
                switch (_dia) {
                    case 2:
                        diaFin = "Lunes";
                        break;
                    case 3:
                        diaFin = "Martes";
                        break;
                    case 4:
                        diaFin = "Miercoles";
                        break;
                    case 5:
                        diaFin = "Jueves";
                        break;
                    case 6:
                        diaFin = "Viernes";
                        break;
                    case 7:
                        diaFin = "Sabado";
                        break;
                    case 1:
                        diaFin = "Domingo";
                        break;
                }

                if (reservas.getAccion() != Reservas.LIBRES &&
                        reservas.getAccion() != Reservas.OCUPADOS) {
                    if (!dia.getText().toString().equals(diaFin)) {
                        hasta.setTextColor(Color.RED);
                        Toast.makeText(AddReservaActivity.this, "Error: El dia de la fecha de fin debe coincidir con el dia de la fecha inicial", Toast.LENGTH_LONG).show();
                        puedeInsertar = false;
                    } else {
                        hasta.setTextColor(Color.BLACK);
                        puedeInsertar = true;
                    }
                }


            }
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        //dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        dialog.show();

    }

    private void fechaInicio(final Calendar calendar) {
        dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                desde.setText(String.valueOf(year) + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth));
                if (hasta.getText().toString().isEmpty())
                    hasta.setText(String.valueOf(year) + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth));
                calendar.set(year, monthOfYear, dayOfMonth);
                int _dia = calendar.get(Calendar.DAY_OF_WEEK);

                switch (_dia) {
                    case 2:
                        dia.setText("Lunes");
                        break;
                    case 3:
                        dia.setText("Martes");
                        break;
                    case 4:
                        dia.setText("Miercoles");
                        break;
                    case 5:
                        dia.setText("Jueves");
                        break;
                    case 6:
                        dia.setText("Viernes");
                        break;
                    case 7:
                        dia.setText("Sabado");
                        break;
                    case 1:
                        dia.setText("Domingo");
                        break;
                }

            }
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        //dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        dialog.show();
    }


    @Override
    public void onClick(View v) {
        if (reservas.getAccion() == Reservas.ADD) {
            insertarReserva();
        } else if (reservas.getAccion() == Reservas.UPDATE) {
            actualizarReserva();
        } else if (reservas.getAccion() == Reservas.LIBRES) {
            reservas.setAula(aula.getSelectedItemPosition() + 1);
            reservas.setFechaIn(desde.getText().toString());
            reservas.setFechaFin(hasta.getText().toString());
            startActivity(new Intent(AddReservaActivity.this, MisReservasActivity.class));
        } else if (reservas.getAccion() == Reservas.OCUPADOS) {
            reservas.setAula(aula.getSelectedItemPosition() + 1);
            reservas.setFechaIn(desde.getText().toString());
            reservas.setFechaFin(hasta.getText().toString());
            startActivity(new Intent(AddReservaActivity.this, MisReservasActivity.class));
        }
    }

    private void insertarReserva() {

        if (!puedeInsertar) {
            Toast.makeText(AddReservaActivity.this, "Existen errores en los datos rellenados", Toast.LENGTH_SHORT).show();
            return;
        }


        Reserva reserva = new Reserva();
        reserva.setAula(aula.getSelectedItemPosition() + 1);
        reserva.setFechaIn(desde.getText().toString());
        reserva.setFechaFin(hasta.getText().toString());
        reserva.setProfesorInt(reservas.getUsuario().getId());
        reserva.setHoraInt(hora.getSelectedItemPosition() + 1);

        Gson gson = new Gson();

        RequestParams params = new RequestParams();
        params.put("reserva", gson.toJson(reserva));

        reservas.getClient().post(Reservas.URL_RESERVAS_MIAS, params, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                setResult(Activity.RESULT_CANCELED);
                Toast.makeText(AddReservaActivity.this, responseString, Toast.LENGTH_SHORT).show();
                Log.e("ERROR", responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                setResult(Activity.RESULT_CANCELED);
                Toast.makeText(AddReservaActivity.this, String.valueOf(errorResponse), Toast.LENGTH_SHORT).show();
                Log.e("ERROR", String.valueOf(errorResponse));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                Gson gson = new Gson();
                Result result = gson.fromJson(String.valueOf(response), Result.class);

                if (result != null && result.getCode()) {
                    Toast.makeText(AddReservaActivity.this, "Datos insertados correctamente", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK);
                    //finish();
                } else {
                    try {
                        Toast.makeText(AddReservaActivity.this, "Error: " + result.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("ERROR", result.getMessage());
                    } catch (NullPointerException e) {
                        Toast.makeText(AddReservaActivity.this, "Error al insertar", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void actualizarReserva() {
        Reserva reserva = reservas.getSeleccionada();
        reserva.setAula(aula.getSelectedItemPosition() + 1);
        reserva.setFechaIn(desde.getText().toString());
        reserva.setFechaFin(hasta.getText().toString());
        reserva.setProfesorInt(reservas.getUsuario().getId());
        reserva.setHoraInt(hora.getSelectedItemPosition() + 1);

        RequestParams params = new RequestParams();
        params.put("reserva", new Gson().toJson(reserva));

        reservas.getClient().put(Reservas.URL_RESERVAS_MIAS + "/" + reserva.getId(), params, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                setResult(Activity.RESULT_CANCELED);
                Toast.makeText(AddReservaActivity.this, responseString, Toast.LENGTH_SHORT).show();
                Log.e("ERROR", responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                setResult(Activity.RESULT_CANCELED);
                Toast.makeText(AddReservaActivity.this, String.valueOf(errorResponse), Toast.LENGTH_SHORT).show();
                Log.e("ERROR", String.valueOf(errorResponse));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                Result result = new Gson().fromJson(String.valueOf(response), Result.class);

                if (result != null && result.getCode()) {
                    setResult(Activity.RESULT_OK);
                    Toast.makeText(AddReservaActivity.this, "Se actualizó la reserva", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        Toast.makeText(AddReservaActivity.this, "Error: " + result.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (NullPointerException e) {
                        Toast.makeText(AddReservaActivity.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
