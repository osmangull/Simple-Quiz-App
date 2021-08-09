package com.example.sanalkasifapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sanalkasifapp.adapters.CevaplarAdapter;
import com.example.sanalkasifapp.helpers.MyCountDownTimer;
import com.example.sanalkasifapp.model.Cevaplar;
import com.example.sanalkasifapp.model.QuizElemanlari;
import com.example.sanalkasifapp.model.Soru;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class GameScreenActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView soruMetni;
    private CevaplarAdapter adapter;
    private ArrayList<Cevaplar> cevaplarArrayList;
    private ArrayList<Soru> soruArrayList;
    private FirebaseFirestore db ;
    private ProgressDialog progressDialog;
    private ArrayList<QuizElemanlari> quizElemanlariArrayList;
    private int soruIndex =0;
    private TextView timerText;
    private CountDownTimer countDownTimer;
    private static long countDownIntType = 90000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice_layout);
        db = FirebaseFirestore.getInstance();
        soruMetni = findViewById(R.id.soru);
        recyclerView = findViewById(R.id.recyclerView);
        timerText = findViewById(R.id.timer);
        recyclerView.setLayoutManager(new LinearLayoutManager(GameScreenActivity.this));
        soruArrayList = new ArrayList<>();
        cevaplarArrayList = new ArrayList<>();
        progressDialog = new ProgressDialog(GameScreenActivity.this);
        progressDialog.setCancelable(false);

        getSorular();
    }

    public void getSorular(){
        progressDialog.setMessage("Sorular Çekiiliyor");
        progressDialog.show();
        db.collection("sorular")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                soruArrayList.add(document.toObject(Soru.class));
                            }
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                            getCevaplar();
                        }
                        else{
                            Toast.makeText(GameScreenActivity.this, "Başarısız", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void getCevaplar(){
        progressDialog.setMessage("Cevaplar Belirleniyor");
        progressDialog.show();
        db.collection("cevaplar")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                cevaplarArrayList.add(document.toObject(Cevaplar.class));
                            }
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                            quizOlustur();
                        }
                        else{
                            Toast.makeText(GameScreenActivity.this, "Başarısız", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void quizOlustur(){
        progressDialog.setMessage("Oyun başlatılıyor");
        progressDialog.show();
        quizElemanlariArrayList = new ArrayList<>();
        ArrayList<Cevaplar> tmpArrayCevaplar;
        if (soruArrayList.size()>0 && cevaplarArrayList.size()>0){
            QuizElemanlari quizElemani = new QuizElemanlari();
            for (Soru s : soruArrayList){
                tmpArrayCevaplar = new ArrayList<>();
                for (Cevaplar c : cevaplarArrayList){
                        if (c.getSoruID().equals(s.getID())){
                            quizElemani.setQuizSorusu(s.getSoruMetni());
                            tmpArrayCevaplar.add(c);
                                quizElemani.setCevaplarList(tmpArrayCevaplar);
                        }
                }
            }
            quizElemanlariArrayList.add(quizElemani);
        }
        if (progressDialog.isShowing())
            progressDialog.dismiss();


        if (quizElemanlariArrayList.size()>0){
            adapter = new CevaplarAdapter(quizElemanlariArrayList.get(soruIndex).getCevaplarList(), new CevaplarAdapter.secenekIsaretleListener() {
                @Override
                public void secenekBelirle(Boolean dogruMu) {
                    if (dogruMu) {
                        Toast.makeText(GameScreenActivity.this, "Doğru Cevap", Toast.LENGTH_SHORT).show();
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                            countDownTimer = new MyCountDownTimer(timerText,countDownIntType + 5000, 1000).start();
                        }
/*                        countDownTimer = new MyCountDownTimer(timerText,countDownIntType + 5000, 1000) {
                        }.start();*/
                    }
                    else
                        Toast.makeText(GameScreenActivity.this, "Yanlış Cevap", Toast.LENGTH_SHORT).show();
                }
            });
            soruMetni.setText(quizElemanlariArrayList.get(soruIndex).getQuizSorusu());
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            countDownTimer=new MyCountDownTimer(timerText,countDownIntType,1000)//15000 kısmı kaç ms saniye olacağını gösterir.
            {
            }.start();
        }
    }


}