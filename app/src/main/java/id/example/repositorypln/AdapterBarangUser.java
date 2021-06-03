package id.example.repositorypln;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import id.example.repositorypln.model.Barang;
import id.example.repositorypln.model.Permintaan;

public class AdapterBarangUser extends RecyclerView.Adapter<AdapterBarangUser.viewHolder> implements Filterable {
    Context context;
    List<Barang> arrayList;
    List<Barang> arrayListFull;
    Dialog dialog2;
    FirebaseDatabase database;
    DatabaseReference ref;

    public AdapterBarangUser(Context context, List<Barang> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        arrayListFull = new ArrayList<>(arrayList);
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_barang, parent, false);
        return new viewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("permintaan");

        holder.harga.setText("Nilai Perol : "+arrayList.get(position).getNilaiPerol());
        holder.nama.setText("Peminjam : "+arrayList.get(position).getNama());
        holder.no.setText("No Inv : "+arrayList.get(position).getNoInventaris());
        holder.status.setText("Status : "+arrayList.get(position).getKeterangan());
        holder.pinjam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2 = new Dialog(context);
                dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog2.setContentView(R.layout.dialog_pinjam);
                dialog2.getWindow().setGravity(Gravity.CENTER);
                dialog2.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog2.show();

                EditText nama = dialog2.findViewById(R.id.nama);
                EditText tempat = dialog2.findViewById(R.id.tempat);

                Button conrifm = dialog2.findViewById(R.id.konfirmasi);
                conrifm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Date c = Calendar.getInstance().getTime();
                        System.out.println("Current time => " + c);

                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MMM-dd", Locale.getDefault());
                        String formattedDate = df.format(c);

                        Permintaan permintaan = new Permintaan(formattedDate, nama.getText().toString(), tempat.getText().toString(), "proses",
                                arrayList.get(position).getNama(), arrayList.get(position).getKey(), arrayList.get(position).getNoInventaris(),
                                arrayList.get(position).getStatus(), arrayList.get(position).getKeterangan(), arrayList.get(position).getNilaiPerol());

                        String uploadId = ref.push().getKey();
                        ref.child(uploadId).setValue(permintaan);
                        dialog2.cancel();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView nama, no, status, harga;
        Button pinjam;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            harga = itemView.findViewById(R.id.harga);
            nama = itemView.findViewById(R.id.namaBarang);
            no = itemView.findViewById(R.id.noInventaris);
            status = itemView.findViewById(R.id.status);
            pinjam = itemView.findViewById(R.id.btnPinjam);
        }
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Barang> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(arrayListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Barang item : arrayListFull) {
                    if (item.getNama().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            arrayList.clear();
            arrayList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
