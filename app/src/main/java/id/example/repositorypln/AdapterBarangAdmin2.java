package id.example.repositorypln;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import id.example.repositorypln.model.Barang;

public class AdapterBarangAdmin2 extends RecyclerView.Adapter<AdapterBarangAdmin2.viewHolder> {
    Context context;
    List<Barang> arrayList;
    FirebaseDatabase database;
    DatabaseReference ref;

    public AdapterBarangAdmin2(Context context, List<Barang> arrayList) {
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
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("barangs");

        holder.harga.setText("Nilai Perol : "+arrayList.get(position).getNilaiPerol());
        holder.nama.setText("Nama barang : "+arrayList.get(position).getNama());
        holder.no.setText("No Inv : "+arrayList.get(position).getNoInventaris());
        holder.status.setText("Status : "+arrayList.get(position).getStatus());

        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Dialog dialog2 = new Dialog(context);
                dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog2.setContentView(R.layout.dialog_barang);
                dialog2.getWindow().setGravity(Gravity.CENTER);
                dialog2.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog2.show();

                Button hapus = dialog2.findViewById(R.id.hapus);
                hapus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ref.child(arrayList.get(position).getKey()).removeValue();
                        arrayList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, arrayList.size());
                        notifyDataSetChanged();
                    }
                });

                Button edit = dialog2.findViewById(R.id.edit);
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(context, AddBarang.class);
                        i.putExtra("nama", arrayList.get(position).getNama());
                        i.putExtra("no", arrayList.get(position).getNoInventaris());
                        i.putExtra("status", arrayList.get(position).getStatus());
                        i.putExtra("ket", arrayList.get(position).getKeterangan());
                        i.putExtra("harga", arrayList.get(position).getNilaiPerol());
                        i.putExtra("key", arrayList.get(position).getKey());
                        i.putExtra("edit", true);
                        context.startActivity(i);
                        ((AdminActivity) context).finish();
                    }
                });
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView nama, no, status, harga;
        LinearLayout layout;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            harga = itemView.findViewById(R.id.harga);
            layout = itemView.findViewById(R.id.layout);
            nama = itemView.findViewById(R.id.namaBarang);
            no = itemView.findViewById(R.id.noInventaris);
            status = itemView.findViewById(R.id.status);
        }
    }
}
