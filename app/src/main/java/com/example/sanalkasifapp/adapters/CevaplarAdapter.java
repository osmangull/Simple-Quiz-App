package com.example.sanalkasifapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sanalkasifapp.R;
import com.example.sanalkasifapp.model.Cevaplar;
import com.example.sanalkasifapp.model.QuizElemanlari;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CevaplarAdapter extends RecyclerView.Adapter<CevaplarAdapter.ViewHolder> {
    private secenekIsaretleListener listener;
    private ArrayList<Cevaplar> sorununCevaplari;
    public interface secenekIsaretleListener{
        void secenekBelirle(Boolean dogruMu);
    }
    public CevaplarAdapter(ArrayList<Cevaplar> sorununCevaplari, secenekIsaretleListener listener){
        this.sorununCevaplari = sorununCevaplari;
        this.listener = listener;
    }
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_row_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Cevaplar cevap = sorununCevaplari.get(position);
        holder.cevapText.setText(cevap.getCevapMetni());
        holder.cevapText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.secenekBelirle(cevap.getDogruCevapMi());
            }
        });
    }

    @Override
    public int getItemCount() {
            return sorununCevaplari.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatButton cevapText;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            cevapText = itemView.findViewById(R.id.cevap_text);
        }
    }
    public void refreshAdapter(ArrayList<Cevaplar> old){
        this.sorununCevaplari = old;
        this.notifyDataSetChanged();
    }
}
