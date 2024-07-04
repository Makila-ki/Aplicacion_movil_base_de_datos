package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;


public class Ajustes_tema extends AppCompatActivity {

    private int colorActual;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes_tema);

    }

    public void onClick(View view) {
        if (view.getId() == R.id.salir_edit) {
            finish();
        } else if (view.getId() == R.id.cardAmarillo) {
            colorActual = obtenerColorDeRecursos(R.color.Amarillo); // Ejemplo: cambiar al color azul
            getWindow().getDecorView().setBackgroundColor(colorActual);
        }

    }
    private int obtenerColorDeRecursos(int colorResId) {
        return getResources().getColor(colorResId, null); // Obtener el color de los recursos
    }
}