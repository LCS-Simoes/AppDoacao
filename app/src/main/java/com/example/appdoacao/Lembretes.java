package com.example.appdoacao;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Lembretes extends AppCompatActivity {

    private static final String TAG = "LembretesActivity";

    private ImageView btHome;
    private ImageView btInformacoes;
    private ImageView btConquistas;
    private ImageView btAgendamento;
    private ImageView btLembrete;
    private Button btDesagendar;
    private Button btLocal;
    private TextView horario;
    private TextView local;
    private TextView data;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    String usuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lembretes);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        iniciar();
        telas();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // Usuário autenticado exibe todas as informações
            usuarioID = currentUser.getUid();
            atualizarDadosUsuarios(usuarioID);
        } else {
            // Usuário não autenticado exibe informações básicas
            findViewById(R.id.containerdados).setVisibility(View.GONE);
            findViewById(R.id.maps).setVisibility(View.GONE);
        }
    }

    private void iniciar() {
        btHome = findViewById(R.id.home);
        btInformacoes = findViewById(R.id.informacoes);
        btConquistas = findViewById(R.id.conquista);
        btAgendamento = findViewById(R.id.agendamento);
        btLembrete = findViewById(R.id.lembrete);
        data = findViewById(R.id.dataOficial);
        horario = findViewById(R.id.horarioOficial);
        local = findViewById(R.id.localizacaoOficial);
        btDesagendar = findViewById(R.id.desagendar);
        btLocal = findViewById(R.id.maps);
    }

    private void atualizarDadosUsuarios(String usuarioID) {
        db.collection("agendamentos")
                .whereEqualTo("userId", usuarioID)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        findViewById(R.id.containerdados).setVisibility(View.VISIBLE);
                        findViewById(R.id.maps).setVisibility(View.VISIBLE);
                        DocumentSnapshot agendamentoDocumentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        local.setText(agendamentoDocumentSnapshot.getString("local"));
                        horario.setText(agendamentoDocumentSnapshot.getString("horario"));
                        data.setText(agendamentoDocumentSnapshot.getString("data"));
                        Log.d(TAG, "Documentos encontrados");
                    } else {
                        Log.d(TAG, "Nenhum agendamento encontrado para este usuário");
                        findViewById(R.id.containerdados).setVisibility(View.GONE);
                        limparDados();
                        findViewById(R.id.maps).setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "Erro ao recuperar dados do agendamento", e);
                });
    }

    private void limparDados() {
        if (local != null) {
            local.setText("");
        }
        if (horario != null) {
            horario.setText("");
        }
    }

    private void telas() {
        btInformacoes.setOnClickListener(v -> {
            Intent intent = new Intent(Lembretes.this, Informacoes.class);
            startActivity(intent);
        });

        btAgendamento.setOnClickListener(v -> {
            Intent intent = new Intent(Lembretes.this, Agendamentos.class);
            startActivity(intent);
        });

        btConquistas.setOnClickListener(v -> {
            Intent intent = new Intent(Lembretes.this, Conquistas.class);
            startActivity(intent);
        });

        btLembrete.setOnClickListener(v -> {
            Intent intent = new Intent(Lembretes.this, Lembretes.class);
            startActivity(intent);
        });

        btHome.setOnClickListener(v -> {
            Intent intent = new Intent(Lembretes.this, TelaPrincipal.class);
            startActivity(intent);
        });

        btDesagendar.setOnClickListener(v -> desagendarAgendamento());

        btLocal.setOnClickListener(v -> abrirLocalizacao());
    }

    private void abrirLocalizacao() {
        String localizacao = local.getText().toString();
        String uri;

        if (localizacao.equalsIgnoreCase("Hospital de Americana")) {
            uri = "geo:0,0?q=Hospital Municipal Dr. Waldemar Tebaldi, Americana, SP, Brazil";
        } else if (localizacao.equalsIgnoreCase("Hospital  de Santa Bárbara D Oeste")) {
            uri = "geo:0,0?q=Hospital Santa Bárbara, Santa Bárbara d'Oeste, SP, Brazil";
        } else {
            Toast.makeText(this, "Localização desconhecida", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "Google Maps não está instalado", Toast.LENGTH_SHORT).show();
        }
    }

    private void desagendarAgendamento() {
        db.collection("agendamentos")
                .whereEqualTo("userId", usuarioID)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot agendamentoDocumentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        String documentId = agendamentoDocumentSnapshot.getId();

                        db.collection("agendamentos").document(documentId)
                                .delete()
                                .addOnSuccessListener(aVoid -> {
                                    Log.d(TAG, "Agendamento deletado com sucesso!");
                                    Toast.makeText(Lembretes.this,"Agendamento cancelado com sucesso", Toast.LENGTH_SHORT).show();
                                    limparDados();
                                    findViewById(R.id.containerdados).setVisibility(View.GONE);
                                })
                                .addOnFailureListener(e -> Log.d(TAG, "Erro ao deletar o agendamento", e));
                    } else {
                        Log.d(TAG, "Nenhum agendamento encontrado para deletar");
                        Toast.makeText(Lembretes.this,"ERRO, Tente novamente", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Log.d(TAG, "Erro ao buscar agendamento para deletar", e));
    }
}