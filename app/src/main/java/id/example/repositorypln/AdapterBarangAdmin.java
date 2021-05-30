package id.example.repositorypln;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.example.repositorypln.model.Barang;

public class AdapterBarangAdmin extends RecyclerView.Adapter<AdapterBarangAdmin.viewHolder> {
    Context context;
    List<Barang> arrayList;

    public AdapterBarangAdmin(Context context, List<Barang> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_barangadmin, parent, false);
        return new viewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.harga.setText("Harga : "+arrayList.get(position).getHarga());
        holder.nama.setText("Nama Barang : "+arrayList.get(position).getNama());
        holder.no.setText("No Inv : "+arrayList.get(position).getNoInventaris());
        holder.status.setText("Status : "+arrayList.get(position).getKeterangan());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView nama, no, status, harga;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            harga = itemView.findViewById(R.id.harga);
            nama = itemView.findViewById(R.id.namaBarang);
            no = itemView.findViewById(R.id.noInventaris);
            status = itemView.findViewById(R.id.status);
        }
    }
}
