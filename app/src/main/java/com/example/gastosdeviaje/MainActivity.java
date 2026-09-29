package com.example.gastosdeviaje;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.NumberFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int CANTIDAD_AMIGOS = 4;

    private final String[] descripciones = {
            "Alquiler de cabaña", "Nafta", "Cena en el centro",
            "Excursión Cerro Catedral", "Supermercado"
    };
    private final String[] pagadores = {"Lu", "Tomi", "Cami", "Nico", "Lu"};
    private final int[] montos = {180000, 45000, 62000, 38000, 29500};

    private int indice = 0;
    private int total = 0;

    private LinearLayout listaGastos;
    private TextView tvTotal;
    private TextView tvPorPersona;
    private Button btnAgregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listaGastos = findViewById(R.id.listaGastos);
        tvTotal = findViewById(R.id.tvTotal);
        tvPorPersona = findViewById(R.id.tvPorPersona);
        btnAgregar = findViewById(R.id.btnAgregar);

        btnAgregar.setOnClickListener(v -> agregarGasto());
    }

    private void agregarGasto() {
        if (indice >= descripciones.length) {
            Toast.makeText(this, R.string.sin_mas_ejemplos, Toast.LENGTH_SHORT).show();
            return;
        }

        LinearLayout fila = crearFila(descripciones[indice], pagadores[indice], montos[indice]);
        listaGastos.addView(fila);

        total += montos[indice];
        indice++;
        actualizarResumen();
    }

    private LinearLayout crearFila(String descripcion, String pagador, int monto) {
        int padding = getResources().getDimensionPixelSize(R.dimen.padding_fila);
        float tamanoTexto = getResources().getDimension(R.dimen.texto_normal);

        LinearLayout fila = new LinearLayout(this);
        fila.setOrientation(LinearLayout.HORIZONTAL);
        fila.setGravity(Gravity.CENTER_VERTICAL);
        fila.setPadding(0, padding, 0, padding);

        LinearLayout textos = new LinearLayout(this);
        textos.setOrientation(LinearLayout.VERTICAL);
        textos.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        TextView tvDescripcion = new TextView(this);
        tvDescripcion.setText(descripcion);
        tvDescripcion.setTextSize(TypedValue.COMPLEX_UNIT_PX, tamanoTexto);
        tvDescripcion.setTextColor(ContextCompat.getColor(this, R.color.texto_principal));

        TextView tvPagador = new TextView(this);
        tvPagador.setText(getString(R.string.pago_format, pagador));
        tvPagador.setTextColor(ContextCompat.getColor(this, R.color.texto_secundario));

        textos.addView(tvDescripcion);
        textos.addView(tvPagador);

        TextView tvMonto = new TextView(this);
        tvMonto.setText(getString(R.string.total_format, formatear(monto)));
        tvMonto.setTextSize(TypedValue.COMPLEX_UNIT_PX, tamanoTexto);
        tvMonto.setTextColor(ContextCompat.getColor(this, R.color.texto_principal));
        tvMonto.setTypeface(tvMonto.getTypeface(), android.graphics.Typeface.BOLD);

        fila.addView(textos);
        fila.addView(tvMonto);
        return fila;
    }

    private void actualizarResumen() {
        tvTotal.setText(getString(R.string.total_format, formatear(total)));
        tvPorPersona.setText(getString(R.string.total_format, formatear(total / CANTIDAD_AMIGOS)));
    }

    private String formatear(int monto) {
        return NumberFormat.getInstance(new Locale("es", "AR")).format(monto);
    }
}