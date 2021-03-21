package com.technek.parrotnight.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.technek.parrotnight.R;
import com.technek.parrotnight.models.ClientDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class customeradapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;

    private List<ClientDetails> allCustomers;
    private List<ClientDetails> filterdItems;


    public customeradapter(Context context, List<ClientDetails> customer_name) {

        this.context = context;
        inflater = LayoutInflater.from(context);

        this.filterdItems = new ArrayList<>();
        this.filterdItems.addAll(customer_name);

        this.allCustomers = new ArrayList<>();
        this.allCustomers.addAll(customer_name);
    }

    public class ViewHolder {
        TextView txtLoyaltycardno;
        TextView txtLedgerName;
        TextView txtLedgerNumber;
        TextView txtloyaltypoints;
    }

    @Override
    public int getCount() {
        return filterdItems.size();
    }

    @Override
    public String getItem(int position) {
        return filterdItems.get(position).getLoyaltycardno();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;

        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.customer_listview_layout, null);

            holder.txtLoyaltycardno = view.findViewById(R.id.tv_list_loyalty_card);
            holder.txtLedgerName = view.findViewById(R.id.tv_list_ledger_name);
            holder.txtLedgerNumber = view.findViewById(R.id.tv_ledger_card);
            holder.txtloyaltypoints = view.findViewById(R.id.tv_ledger_points);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.txtLoyaltycardno.setText(filterdItems.get(position).getLoyaltycardno());
        holder.txtLedgerName.setText(filterdItems.get(position).getLedgerName());
        holder.txtloyaltypoints.setText(filterdItems.get(position).getLedger_points());
        holder.txtLedgerNumber.setText(filterdItems.get(position).getLedger_card());


        return view;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        filterdItems.clear();
        if (charText.length() == 0) {
            filterdItems.addAll(allCustomers);
        } else {
            for (ClientDetails wp : allCustomers) {
                if (wp.getLoyaltycardno().toLowerCase(Locale.getDefault()).contains(charText)) {
                    filterdItems.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    public List<ClientDetails> getFilterdItems() {
        return filterdItems;
    }
}

