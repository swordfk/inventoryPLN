package id.example.repositorypln;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.example.repositorypln.model.Permintaan;

public class AdapterPermintaanUser extends RecyclerView.Adapter<AdapterPermintaanUser.viewHolder> {
    Context context;
    List<Permintaan> arrayList;

    public AdapterPermintaanUser(Context context, List<Permintaan> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_permintaan_user, parent, false);
        return new viewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        holder.harga.setText("Nilai Perol : "+arrayList.get(position).getPerolB());
        holder.namaB.setText("Nama Barang : "+arrayList.get(position).getNamaB());
        holder.no.setText("No Inv : "+arrayList.get(position).getNoInventarisB());
        holder.nama.setText("Peminjam : "+arrayList.get(position).getNama());
        holder.status.setText(arrayList.get(position).getStatusPermintaan());
        if (arrayList.get(position).getStatusPermintaan().contains("terima")){
//            holder.layout.setBackgroundColor(Color.BLUE);
            holder.status.setTextColor(Color.BLUE);
        }else if (arrayList.get(position).getStatusPermintaan().contains("tolak")){
//            holder.layout.setBackgroundColor(Color.RED);
            holder.status.setTextColor(Color.RED);
        }else if (arrayList.get(position).getStatusPermintaan().contains("selesai")){
//            holder.layout.setBackgroundColor(Color.GREEN);
            holder.status.setTextColor(Color.GREEN);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView namaB, no, nama, harga, status;
        LinearLayout layout;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            status = itemView.findViewById(R.id.statusPermintaan);
            harga = itemView.findViewById(R.id.harga);
            layout = itemView.findViewById(R.id.layoutParent);
            namaB = itemView.findViewById(R.id.namaBarang);
            no = itemView.findViewById(R.id.noInventaris);
            nama = itemView.findViewById(R.id.namaPeminjam);
        }
    }
}
