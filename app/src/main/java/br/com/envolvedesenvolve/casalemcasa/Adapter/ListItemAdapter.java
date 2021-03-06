package br.com.envolvedesenvolve.casalemcasa.Adapter;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.envolvedesenvolve.casalemcasa.MainActivity;
import br.com.envolvedesenvolve.casalemcasa.Model.ToDo;
import br.com.envolvedesenvolve.casalemcasa.R;

/**
 * Created by Cristiano M. on 01/02/2020
 */

class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener{

    ItemClickListener itemClickListener;
    TextView item_title, item_description;

    public ListItemViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);

        item_title = itemView.findViewById(R.id.item_title);
        item_description = itemView.findViewById(R.id.item_description);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Selecione ação");
        menu.add(0,0, getAdapterPosition(), "APAGAR");
    }
}

public class ListItemAdapter extends RecyclerView.Adapter<ListItemViewHolder>{

    MainActivity mainActivity;
    List<ToDo> todoList;

    public ListItemAdapter(MainActivity mainActivity, List<ToDo> todoList) {
        this.mainActivity = mainActivity;
        this.todoList = todoList;
    }

    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mainActivity.getBaseContext());
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemViewHolder holder, int position) {

        holder.item_title.setText(todoList.get(position).getTitle());
        holder.item_description.setText(todoList.get(position).getDescription());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                try {
                    mainActivity.edtTitle.setText(todoList.get(position).getTitle());
                    mainActivity.edtDescription.setText(todoList.get(position).getDescription());

                    mainActivity.isUpdate = true;
                    mainActivity.idUpdate = todoList.get(position).getId();
                } catch(Exception e){
                    Log.e("ListImteAdapter", "ERRO onClick");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }
}
