package com.example.appdoacao;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import java.util.Timer;
import java.util.TimerTask;

public class TelaPrincipal extends AppCompatActivity {

    private TextView nomeUsuario;
    private TextView stringCPF;
    private TextView nomeCartao;
    private TextView cpfCartao;
    private TextView tipoSangue;
    private ImageView btHome;
    private ImageView btInformacoes;
    private ImageView btDeslogar;
    private ImageView btConquistas;
    private ImageView btAgendamento;
    private ImageView btLembrete;
    private ViewPager viewPager;

    private int[] imageIds = {R.drawable.campanha1, R.drawable.campanha2, R.drawable.campanha3, R.drawable.campanha4};
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String usuarioID;

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);
        dadosGerais();
        configurarView();
        btInformacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaPrincipal.this, Informacoes.class);
                startActivity(intent);
            }
        });

        btAgendamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaPrincipal.this, Agendamentos.class);
                startActivity(intent);
            }
        });

        btConquistas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaPrincipal.this, Conquistas.class);
                startActivity(intent);
            }
        });

        btLembrete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaPrincipal.this, Lembretes.class);
                startActivity(intent);
            }
        });

        btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaPrincipal.this, TelaPrincipal.class);
                startActivity(intent);
            }
        });

        btDeslogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limparDados();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(TelaPrincipal.this, Login.class);
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
            // Usuário autenticado exibe todas as informações
            usuarioID = currentUser.getUid();
            atualizarDadosUsuarios(usuarioID);
        } else {
            limparDados();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        limparDados();
    }

    private void atualizarDadosUsuarios(String usuarioID) {
        this.usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentReference = db.collection("Usuarios").document(this.usuarioID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot != null) {
                    nomeUsuario.setText(documentSnapshot.getString("Nome"));
                    tipoSangue.setText(documentSnapshot.getString("TipoSanguineo"));
                    stringCPF.setText(documentSnapshot.getString("CPF"));
                    cpfCartao.setText(documentSnapshot.getString("CPF"));
                    nomeCartao.setText(documentSnapshot.getString("Nome"));
                }
            }
        });
    }

    private void configurarView() {
        viewPager = findViewById(R.id.viewPager);
        CampanhaPagerAdapter adapter = new CampanhaPagerAdapter(this, imageIds);
        viewPager.setAdapter(adapter);

        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            @Override
            public void run() {
                int currentPage = viewPager.getCurrentItem();
                if (currentPage == imageIds.length - 1) {
                    currentPage = 0;
                } else {
                    currentPage++;
                }
                viewPager.setCurrentItem(currentPage, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }

        }, 1000, 3000);

    }

    private void limparDados() {
        //exibição básicas para usuário sem login
        nomeUsuario.setText("Faça login");
        nomeCartao.setText("Nome usuário");
        cpfCartao.setText("CPF");
        stringCPF.setText("Nome usuário");
        tipoSangue.setText("A definir");
    }

    private void dadosGerais() {
        //inicialização das visualizações
        nomeUsuario = findViewById(R.id.nomeUsuario);
        btHome = findViewById(R.id.home);
        btInformacoes = findViewById(R.id.informacoes);
        btConquistas = findViewById(R.id.conquista);
        btAgendamento = findViewById(R.id.agendamento);
        btDeslogar = findViewById(R.id.deslogar);
        btLembrete = findViewById(R.id.lembrete);
        stringCPF = findViewById(R.id.stringCPF);
        cpfCartao = findViewById(R.id.cpfCartao);
        nomeCartao = findViewById(R.id.Usuario);
        tipoSangue = findViewById(R.id.sangue);
    }

    // Classe interna CampanhaPagerAdapter
    private class CampanhaPagerAdapter extends PagerAdapter {
        private Context mContext;
        private int[] mImagens;

        public CampanhaPagerAdapter(Context context, int[] imagens) {
            mContext = context;
            mImagens = imagens;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View itemView = inflater.inflate(R.layout.item_campanha, container, false);

            ImageView imageView = itemView.findViewById(R.id.imgCampanha);
            imageView.setImageResource(mImagens[position]);

            container.addView(itemView);

            return itemView;
        }

        @Override
        public int getCount() {
            return mImagens.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}