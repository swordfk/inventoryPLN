package id.example.repositorypln;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import id.example.repositorypln.model.Barang;
import id.example.repositorypln.model.Permintaan;

public class AdapterBarangGudang extends RecyclerView.Adapter<AdapterBarangGudang.viewHolder> {
    Context context;
    List<Permintaan> arrayList;
    FirebaseDatabase database;
    DatabaseReference ref;

    public AdapterBarangGudang(Context context, List<Permintaan> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_permintaan, parent, false);
        return new viewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("permintaan");

        holder.harga.setText("Harga : "+arrayList.get(position).getHargaB());
        holder.namaB.setText("Nama Barang : "+arrayList.get(position).getNamaB());
        holder.no.setText("No Inv : "+arrayList.get(position).getNoInventarisB());
        holder.nama.setText("Peminjam : "+arrayList.get(position).getNama());

        if (arrayList.get(position).getStatusPermintaan().contains("proses")){
            holder.terima.setVisibility(View.VISIBLE);
            holder.tolak.setVisibility(View.VISIBLE);
        }else {
            holder.terima.setVisibility(View.INVISIBLE);
            holder.tolak.setVisibility(View.INVISIBLE);
        }

        holder.terima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Permintaan permintaan = new Permintaan(arrayList.get(position).getTanggal(), arrayList.get(position).getNama(), arrayList.get(position).getTempat(), "terima",
                        arrayList.get(position).getNamaB(), arrayList.get(position).getKeyB(), arrayList.get(position).getNoInventarisB(),
                        arrayList.get(position).getStatusB(), arrayList.get(position).getKeteranganB(), arrayList.get(position).getHargaB());

                ref.child(arrayList.get(position).getKey()).setValue(permintaan);
            }
        });
        holder.tolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Permintaan permintaan = new Permintaan(arrayList.get(position).getTanggal(), arrayList.get(position).getNama(), arrayList.get(position).getTempat(), "tolak",
                        arrayList.get(position).getNama(), arrayList.get(position).getKey(), arrayList.get(position).getNoInventarisB(),
                        arrayList.get(position).getStatusB(), arrayList.get(position).getKeteranganB(),  arrayList.get(position).getHargaB());

                ref.child(arrayList.get(position).getKey()).setValue(permintaan);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView namaB, no, nama, harga;
        Button terima, tolak;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            harga = itemView.findViewById(R.id.harga);
            terima = itemView.findViewById(R.id.btnTerima);
            tolak = itemView.findViewById(R.id.btnTolak);
            namaB = itemView.findViewById(R.id.namaBarang);
            no = itemView.findViewById(R.id.noInventaris);
            nama = itemView.findViewById(R.id.namaPeminjam);
        }
    }
}
