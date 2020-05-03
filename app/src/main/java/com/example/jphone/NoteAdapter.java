package com.example.jphone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note,NoteAdapter.NoteHolder>{

    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteHolder holder, int position, @NonNull Note model) {
        DateFormat df = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        holder.tanggalPeminjaman.setText(df.format(model.getTanggalPeminjaman()));
        holder.tanggalPengembalian.setText(df.format(model.getTanggalDikembalikan()));
        holder.price.setText("harga :" + model.getPrice());
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,parent,false);
        return new NoteHolder(v);
    }

    class NoteHolder extends RecyclerView.ViewHolder{
        TextView tanggalPeminjaman;
        TextView tanggalPengembalian, price;
        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            tanggalPeminjaman = itemView.findViewById(R.id.pinjamTextView);
            tanggalPengembalian = itemView.findViewById(R.id.kembaliTextView);
            price = itemView.findViewById(R.id.priceTextView);
        }
    }
}
