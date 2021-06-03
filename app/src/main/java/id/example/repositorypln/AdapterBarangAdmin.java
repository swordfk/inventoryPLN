package id.example.repositorypln;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.example.repositorypln.model.Barang;

public class AdapterBarangAdmin extends RecyclerView.Adapter<AdapterBarangAdmin.viewHolder> implements Filterable {
    Context context;
    List<Barang> arrayList;
    List<Barang> arrayListFull;

    public AdapterBarangAdmin(Context context, List<Barang> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        arrayListFull = new ArrayList<>(arrayList);
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_barangadmin, parent, false);
        return new viewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.harga.setText("Nilai Perol : "+arrayList.get(position).getNilaiPerol());
        holder.nama.setText("Nama Barang : "+arrayList.get(position).getNama());
        holder.no.setText("No Inv : "+arrayList.get(position).getNoInventaris());
        holder.status.setText("Status : "+arrayList.get(position).getStatus());
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
