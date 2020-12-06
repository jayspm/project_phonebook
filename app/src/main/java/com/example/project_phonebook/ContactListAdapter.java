package com.example.project_phonebook;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_phonebook.models.Contact;

import java.util.ArrayList;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.MyViewHolder> {
    public ArrayList<Contact> dataset;
    Context context;
    private OnItemClickListener mListerner;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public Button setOnItemClickListener(OnItemClickListener listener) {
        this.mListerner = listener;
        return null;
    }

    public ContactListAdapter(ArrayList<Contact> dataset, Context context) {
        this.dataset = dataset;
        this.context = context;
    }

    public void reloadContactList (ArrayList<Contact> dataset) {
        this.dataset = dataset;
        this.notifyDataSetChanged();
    }

    public void reloadContactListAddNew (ArrayList<Contact> dataset, Context context) {
        this.dataset = dataset;
        this.context = context;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate((R.layout.item_contact), parent, false);
        MyViewHolder vh = new MyViewHolder(v, mListerner);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.txtContactName.setText(dataset.get(position).name);
        holder.txtContactPhone.setText(dataset.get(position).phone);
        holder.imgCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent makeACallIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + dataset.get(position).phone));
                context.startActivity(makeACallIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        Button imgCall;
        TextView txtContactName;
        TextView txtContactPhone;
        public MyViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            txtContactName = itemView.findViewById(R.id.item_contact_lblName);
            txtContactPhone = itemView.findViewById(R.id.item_contact_lblPhone);
            imgCall = itemView.findViewById(R.id.item_contact_btn_call);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public int getId(int position){
        return dataset.get(position).id;
    }
}