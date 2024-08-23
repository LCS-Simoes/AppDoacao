package com.example.appdoacao;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Cadastro extends AppCompatActivity {

    private EditText edit_nome, edit_cpf, edit_email, edit_senha;
    private Button btn_cadastrar;
    private Spinner spinnerTipoSanguineo;
    String usuarioID;
    String [] mensagensErros = {"Preencha todos os campos", "CPF inválido", "Cadastro realizado com sucesso"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        pegarDados();

        btn_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = edit_nome.getText().toString();
                String cpf = edit_cpf.getText().toString();
                String email = edit_email.getText().toString();
                String senha = edit_senha.getText().toString();
                String tipoSanguineo = spinnerTipoSanguineo.getSelectedItem().toString();

                if(nome.isEmpty() || cpf.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                    Toast.makeText(Cadastro.this, "Preencha todos os campos necessários", Toast.LENGTH_SHORT).show();
                } else if(!cpfValidacao.cpfValidar(cpf)){
                    Toast.makeText(Cadastro.this, "CPF Inválido!", Toast.LENGTH_SHORT).show();
                } else {
                    CadastrarUsuario(v, email, senha, tipoSanguineo);
                }
            }
        });
    }
    //
    private void SalvarDados(String tipoSanguineo) {
        String nome = edit_nome.getText().toString();
        String cpf = edit_cpf.getText().toString();
        String email = edit_email.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> usuarios = new HashMap<>();
        usuarios.put("Nome", nome);
        usuarios.put("CPF", cpf);
        usuarios.put("TipoSanguineo", tipoSanguineo);
        usuarios.put("Email", email);

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);
        documentReference.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("db","Sucesso ao salvar os dados");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("db","Erro ao salvar" + e);
                    }
                });
    }

    private void CadastrarUsuario(View v, String email, String senha, String tipoSanguineo) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    SalvarDados(tipoSanguineo);
                    Toast.makeText(Cadastro.this, "Cadastro realizado", Toast.LENGTH_SHORT).show();
                    retornarLogin();
                } else {
                    String erro;
                    try {
                        throw Objects.requireNonNull(task.getException());
                    } catch (FirebaseAuthWeakPasswordException e) {
                        erro = "Digite uma senha com no mínimo 6 caracteres";
                    } catch (FirebaseAuthUserCollisionException e) {
                        erro = "E-mail já cadastrado";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erro = "E-mail ou senha inválida!";
                    } catch (Exception e) {
                        erro = "Erro ao cadastrar";
                    }
                    Toast.makeText(v.getContext(), erro, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void retornarLogin(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Cadastro.this, Login.class);
                startActivity(intent);
                finish();
            }
        },3000);
    }

    private void pegarDados() {

        edit_nome = findViewById(R.id.editTextNome);
        edit_email = findViewById(R.id.Email);
        edit_cpf = findViewById(R.id.CPF);
        edit_senha = findViewById(R.id.edit_Senha);
        btn_cadastrar = findViewById(R.id.ButaoCadastro);
        spinnerTipoSanguineo = findViewById(R.id.spinnerTipoSanguineo);


        String[] tiposSangue = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tiposSangue);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoSanguineo.setAdapter(adapter);
    }
}