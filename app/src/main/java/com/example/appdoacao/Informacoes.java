package com.example.appdoacao;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Button;

public class Informacoes extends AppCompatActivity {
    private ImageView btHome;
    private ImageView btInformacoes;
    private ImageView btConquistas;
    private ImageView btAgendamento;
    private ImageView btLembrete;
    private Button btSaber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacoes);
        botoes();
        btInformacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Informacoes.this, Informacoes.class);
                startActivity(intent);
            }
        });

        btAgendamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Informacoes.this, Agendamentos.class);
                startActivity(intent);
            }
        });
        btConquistas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Informacoes.this, Conquistas.class);
                startActivity(intent);
            }
        });
        btLembrete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Informacoes.this, Lembretes.class);
                startActivity(intent);
            }
        });
        btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Informacoes.this, TelaPrincipal.class);
                startActivity(intent);
            }
        });
    }
    private void botoes(){
        btHome = findViewById(R.id.home);
        btInformacoes = findViewById(R.id.informacoes);
        btConquistas = findViewById(R.id.conquista);
        btAgendamento = findViewById(R.id.agendamento);
        btLembrete = findViewById(R.id.lembrete);
        btSaber = findViewById(R.id.saber);
    }
}
