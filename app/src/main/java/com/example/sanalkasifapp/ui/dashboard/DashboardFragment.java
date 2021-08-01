package com.example.sanalkasifapp.ui.dashboard;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sanalkasifapp.R;
import com.example.sanalkasifapp.adapters.UserAdapter;
import com.example.sanalkasifapp.databinding.FragmentDashboardBinding;
import com.example.sanalkasifapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;
    private FirebaseFirestore db ;
    private RecyclerView recyclerView;
    private AppCompatButton btnKaydet, btnYenile;
    private UserAdapter adapter;
    private ArrayList<User> userArrayList = new ArrayList<>() ;
    private EditText kullaniciAdi;
    private ProgressDialog progressDialog;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        recyclerView = binding.recyclerView;
        db = FirebaseFirestore.getInstance();
        btnKaydet = binding.kisiEkle;
        btnYenile = binding.yenile;
        kullaniciAdi = binding.kullaniciAdi;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new UserAdapter(userArrayList);
        recyclerView.setAdapter(adapter);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        btnKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Ekleniyor");
                if (kullaniciAdi.getText().toString().replace(" ","").equals(""))
                    Toast.makeText(getActivity(), "Kullanıcı adını boş bırakmayınız.", Toast.LENGTH_SHORT).show();
                else
                saveUser(kullaniciAdi.getText().toString());
            }
        });

        btnYenile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Veri geliyor");
                progressDialog.show();
                readUsers();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void saveUser(String userName){
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        Map<String, Object> user = new HashMap<>();
        user.put("name", "asdasd");
        user.put("ID", "userName");
        user.put("surname", "Gül");
        user.put("mail", "osman@osman.com");
        user.put("age", 23);
        user.put("userName", userName);


        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Eklendi", Toast.LENGTH_SHORT).show();
                        readUsers();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Başarısız", Toast.LENGTH_SHORT).show();

                    }
                });


    }


    public void readUsers(){
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            if (userArrayList.size()>0)
                                userArrayList.removeAll(userArrayList);
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                userArrayList.add(document.toObject(User.class));
                            }
                            adapter.refreshAdapter(userArrayList);
                        }
                        else{
                            Toast.makeText(getActivity(), "Başarısız", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}