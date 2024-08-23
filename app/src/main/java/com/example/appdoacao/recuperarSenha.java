package com.example.appdoacao;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.CountDownTimer;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class recuperarSenha extends AppCompatActivity {

    private TextView voltarLogin;
    private TextView criarCadastro;
    private Button recuperar;
    private FirebaseAuth Auth;
    private CountDownTimer timer;
    private boolean timerRunning = false;

    private TextView textViewTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);
        Auth = FirebaseAuth.getInstance();
        iniciarComponentes();

        voltarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(recuperarSenha.this, Login.class);
                startActivity(intent);
            }
        });

        criarCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(recuperarSenha.this, Cadastro.class);
                startActivity(intent);
            }
        });

        recuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText emailCliente = findViewById(R.id.emailEsqueceu);
                String email = emailCliente.getText().toString().trim();
                if (!email.isEmpty()) {
                    Auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(recuperarSenha.this, "E-mail de recuperação enviado!", Toast.LENGTH_SHORT).show();
                                startTimer();
                                recuperar.setEnabled(false);
                            }else {
                                Toast.makeText(recuperarSenha.this, "Falha ao enviar o e-mail de recuperação.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(recuperarSenha.this, "Por favor, insira um e-mail", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void iniciarComponentes(){
        voltarLogin = findViewById(R.id.Voltar);
        criarCadastro = findViewById(R.id.Criar);
        recuperar = findViewById(R.id.Enviar);
        textViewTimer = findViewById(R.id.tempo);
    }

    private void startTimer() {
        timerRunning = true;
        textViewTimer.setVisibility(View.VISIBLE);
        timer = new CountDownTimer(40000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = millisUntilFinished / 1000;
                textViewTimer.setText("Tempo restante: " + secondsRemaining + " segundos");
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                recuperar.setEnabled(true);
                textViewTimer.setVisibility(View.GONE);
                // Contagem regressiva concluída
            }
        }.start();
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (timer != null) {
            timer.cancel();
            timerRunning = false;
        }
    }
}