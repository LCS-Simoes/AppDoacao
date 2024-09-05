package com.example.appdoacao;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Locale;

public class Agendamentos extends AppCompatActivity {
    private static final String TAG = "Agendamentos";
    private ImageView btHome;
    private ImageView btInformacoes;
    private Button btAgendar;
    private ImageView btConquistas;
    private ImageView btAgendamento;
    private ImageView btLembrete;
    private FirebaseFirestore db;

    private CheckBox doacao1;
    private CheckBox doacao2;
    //aaaaaaa
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendamentos);
        db = FirebaseFirestore.getInstance();
        botoes();
        configurarCheckboxes();
    }

    private void configurarCheckboxes() {
        doacao1 = findViewById(R.id.doacao1);
        doacao2 = findViewById(R.id.doacao2);

        doacao1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                doacao2.setChecked(false);
            }
        });

        doacao2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                doacao1.setChecked(false);
            }
        });
    }

    private class Agendamento {
        private String userId;
        private String nomeUsuario;
        private String cpf;
        private String tipoSanguineo;
        private String data;
        private String horario;
        private String local;

        public Agendamento() {
            // Construtor vazio
        }

        public Agendamento(String userId, String nomeUsuario, String cpf, String tipoSanguineo, String data, String horario, String local) {
            this.userId = userId;
            this.nomeUsuario = nomeUsuario;
            this.cpf = cpf;
            this.tipoSanguineo = tipoSanguineo;
            this.data = data;
            this.horario = horario;
            this.local = local;
        }

        // Getters e Setters
    }

    private void salvarAgendamento(String userId, String nomeUsuario, String cpf, String tipoSanguineo, String data, String horario, String local) {
        Agendamento agendamento = new Agendamento(userId, nomeUsuario, cpf, tipoSanguineo, data, horario, local);

        db.collection("agendamentos")
                .add(agendamento)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Agendamento salvo com sucesso");
                        Toast.makeText(Agendamentos.this, "Agendamento salvo com sucesso", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Erro ao salvar agendamento", e);
                        Toast.makeText(Agendamentos.this, "Erro ao salvar agendamento", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void botoes() {
        btHome = findViewById(R.id.home);
        btInformacoes = findViewById(R.id.informacoes);
        btConquistas = findViewById(R.id.conquista);
        btAgendamento = findViewById(R.id.agendamento);
        btLembrete = findViewById(R.id.lembrete);
        btAgendar = findViewById(R.id.agendar);

        btInformacoes.setOnClickListener(v -> {
            Intent intent = new Intent(Agendamentos.this, Informacoes.class);
            startActivity(intent);
        });

        btAgendamento.setOnClickListener(v -> {
            Intent intent = new Intent(Agendamentos.this, Agendamentos.class);
            startActivity(intent);
        });

        btConquistas.setOnClickListener(v -> {
            Intent intent = new Intent(Agendamentos.this, Conquistas.class);
            startActivity(intent);
        });

        btLembrete.setOnClickListener(v -> {
            Intent intent = new Intent(Agendamentos.this, Lembretes.class);
            startActivity(intent);
        });

        btHome.setOnClickListener(v -> {
            Intent intent = new Intent(Agendamentos.this, TelaPrincipal.class);
            startActivity(intent);
        });

        btAgendar.setOnClickListener(v -> {
            DatePicker datePicker = findViewById(R.id.calendario);
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth();
            int year = datePicker.getYear();
            String data = String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month + 1, year);

            TimePicker timePicker = findViewById(R.id.horario);
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();
            String horario = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);

            // Verifique o dia da semana e o horário
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            boolean isWeekday = (dayOfWeek >= Calendar.MONDAY && dayOfWeek <= Calendar.FRIDAY);
            boolean isWithinTimeRange = (hour > 8 || (hour == 8 && minute >= 0)) && (hour < 11 || (hour == 11 && minute <= 30));

            if (!isWeekday) {
                Toast.makeText(Agendamentos.this, "Agendamentos só podem ser feitos de segunda a sexta-feira.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isWithinTimeRange) {
                Toast.makeText(Agendamentos.this, "O horário deve estar entre 08:00 e 11:30.", Toast.LENGTH_SHORT).show();
                return;
            }

            final String[] local = {""};

            if (doacao1.isChecked()) {
                local[0] = "Hospital de Americana";
            } else if (doacao2.isChecked()) {
                local[0] = "Hospital de Santa Barbara D' Oeste";
            } else {
                Toast.makeText(Agendamentos.this, "Por favor, selecione apenas um local de doação.", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseAuth auth = FirebaseAuth.getInstance();
            if (auth.getCurrentUser() != null) {
                String userId = auth.getCurrentUser().getUid();
                DocumentReference userRef = db.collection("Usuarios").document(userId);

                userRef.get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String nomeUsuario = documentSnapshot.getString("Nome");
                        String cpf = documentSnapshot.getString("CPF");
                        String tipoSanguineo = documentSnapshot.getString("TipoSanguineo");

                        salvarAgendamento(userId, nomeUsuario, cpf, tipoSanguineo, data, horario, local[0]);
                    } else {
                        Log.d(TAG, "Documento do usuário não encontrado");
                    }
                }).addOnFailureListener(e -> {
                    Log.d(TAG, "Erro ao recuperar dados do usuário", e);
                });
            } else {
                Log.d(TAG, "Usuário não autenticado");
            }
        });
    }
}