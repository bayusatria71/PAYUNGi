package com.example.jphone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note,NoteAdapter.NoteHolder>{

    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteHolder holder, int position, @NonNull Note model) {
        holder.tanggalPeminjaman.setText(model.getTanggalPeminjaman());
        holder.tanggalPengembalian.setText(model.getTanggalDikembalikan());
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,parent,false);
        return new NoteHolder(v);
    }

    class NoteHolder extends RecyclerView.ViewHolder{
        TextView tanggalPeminjaman;
        TextView tanggalPengembalian;
        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            tanggalPeminjaman = itemView.findViewById(R.id.pinjamTextView);
            tanggalPengembalian = itemView.findViewById(R.id.kembaliTextView);

        }
    }
}
