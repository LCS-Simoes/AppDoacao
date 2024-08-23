package com.example.appdoacao;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private TextView telaCadastro;
    private TextView telaPrincipal;
    private EditText edit_Email, edit_Senha;
    private Button bt_entrar;
    private TextView bt_esqueceu;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(getActionBar() != null){
            getActionBar().hide();
        }

        iniciarComponentes();
        telaCadastro.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, Cadastro.class);
            startActivity(intent);
        });

        telaPrincipal.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, TelaPrincipal.class);
            startActivity(intent);
        });

        bt_entrar.setOnClickListener(v -> {
            String email = edit_Email.getText().toString();
            String senha = edit_Senha.getText().toString();
            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(Login.this, "Preencha os campos", Toast.LENGTH_SHORT).show();
            } else {
                Autenticar(v);
            }
        });

        bt_esqueceu.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, recuperarSenha.class);
            startActivity(intent);
        });

    }

    public void Autenticar(View view){
        String email = edit_Email.getText().toString();
        String senha = edit_Senha.getText().toString();

        FirebaseAuth .getInstance().signInWithEmailAndPassword(email,senha).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                progressBar.setVisibility(View.VISIBLE);
                new Handler().postDelayed(() -> TelaPrincipal(), 3000);
            }else {
                String erro;
                try {
                    throw task.getException();
                } catch (Exception e) {
                    erro = "Erro ao logar";
                }
                Toast.makeText(view.getContext(), erro, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void TelaPrincipal(){
        Intent intent = new Intent(Login.this,TelaPrincipal.class);
        startActivity(intent);
        finish();
    }

    private void iniciarComponentes(){
        telaCadastro = findViewById(R.id.telaCadastro);
        telaPrincipal = findViewById(R.id.telaPrincipal);
        edit_Email = findViewById(R.id.edit_email);
        edit_Senha = findViewById(R.id.edit_senha);
        bt_entrar = findViewById(R.id.btn_entrar);
        bt_esqueceu = findViewById(R.id.esqueceuSenha);
        progressBar = findViewById(R.id.progressBar);

    }
}