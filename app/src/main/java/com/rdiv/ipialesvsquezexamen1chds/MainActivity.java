package com.rdiv.ipialesvsquezexamen1chds;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



public class MainActivity extends AppCompatActivity {

    private TextView tvResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnFetch = findViewById(R.id.btnFetch);
        tvResponse = findViewById(R.id.tvResponse);

        btnFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchServerData();
            }
        });
    }

    private void fetchServerData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Cambia a la IP correcta según tu configuración
                    URL url = new URL("http://10.10.35.83:3001/ipiales"); // Usa 10.0.2.2 si estás en el emulador
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;

                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }

                        reader.close();

                        // Actualizar la UI en el hilo principal
                        runOnUiThread(() -> tvResponse.setText(response.toString()));
                    } else {
                        runOnUiThread(() -> {
                            try {
                                tvResponse.setText("Error en la respuesta del servidor: " + connection.getResponseCode());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }

                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> tvResponse.setText("Error al obtener los datos: " + e.getMessage()));
                }
            }
        }).start();
    }
}
