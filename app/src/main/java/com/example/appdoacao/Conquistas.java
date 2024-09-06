package com.example.appdoacao;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Conquistas extends AppCompatActivity {

    private TextView nomeUsuario;
    private TextView stringCPF;
    private ImageView btHome;
    private ImageView btInformacoes;
    private ImageView btDeslogar;
    private ImageView btConquistas;
    private ImageView btAgendamento;
    private ImageView btLembrete;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String usuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conquistas);
        dadosGerais();

        btInformacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Conquistas.this, Informacoes.class);
                startActivity(intent);
            }
        });

        btAgendamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Conquistas.this, Agendamentos.class);
                startActivity(intent);
            }
        });

        btConquistas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Conquistas.this, Conquistas.class);
                startActivity(intent);
            }
        });

        btLembrete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Conquistas.this, Lembretes.class);
                startActivity(intent);
            }
        });

        btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Conquistas.this, TelaPrincipal.class);
                startActivity(intent);
            }
        });

        btDeslogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limparDados();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Conquistas.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            usuarioID = currentUser.getUid();
            atualizarDadosUsuarios(usuarioID);
        } else {
            limparDados();
        }
    }
    private void atualizarDadosUsuarios(String usuarioID) {
        this.usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentReference = db.collection("Usuarios").document(this.usuarioID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if(documentSnapshot != null){
                    nomeUsuario.setText(documentSnapshot.getString("Nome"));
                    stringCPF.setText(documentSnapshot.getString("CPF"));
                }
            }
        });
    }
    private void dadosGerais(){
        nomeUsuario = findViewById(R.id.nomeUsuario);
        btHome = findViewById(R.id.home);
        btInformacoes = findViewById(R.id.informacoes);
        btConquistas = findViewById(R.id.conquista);
        btAgendamento = findViewById(R.id.agendamento);
        btDeslogar = findViewById(R.id.deslogar);
        btLembrete = findViewById(R.id.lembrete);
        stringCPF = findViewById(R.id.stringCPF);
    }
    private void limparDados(){
        //exibição básicas para usuário sem login
        nomeUsuario.setText("Faça login");
        stringCPF.setText("CPF");
    }






}