package com.example.criptobasewebservice.Controlador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.criptobasewebservice.Modelo.Moneda;
import com.example.criptobasewebservice.R;

import java.util.List;

public class MonedaAdapter extends BaseAdapter {
    private Context context;
    private List<Moneda> listaMonedas;

    public MonedaAdapter(Context context, List<Moneda> listaMonedas) {
        this.context = context;
        this.listaMonedas = listaMonedas;
    }

    @Override
    public int getCount() {
        return listaMonedas.size();
    }

    @Override
    public Object getItem(int position) {
        return listaMonedas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_moneda, parent, false);
            holder = new ViewHolder();
            holder.textViewToken = convertView.findViewById(R.id.textViewToken);
            holder.textViewPrecio = convertView.findViewById(R.id.textViewPrecio);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Moneda moneda = listaMonedas.get(position);

        holder.textViewToken.setText(moneda.getToken());
        holder.textViewPrecio.setText(String.valueOf(moneda.getPrecio() + "$"));

        int backgroundColor = determinarColorFondo(position);
        int textColor = determinarColorTexto(position);

        convertView.setBackgroundColor(backgroundColor);
        holder.textViewToken.setTextColor(textColor);
        holder.textViewPrecio.setTextColor(textColor);

        return convertView;
    }

    private int determinarColorFondo(int position) {
        if (position % 2 == 0) {
            return ContextCompat.getColor(context, R.color.purple);
        } else {
            return ContextCompat.getColor(context, R.color.white);
        }
    }

    private int determinarColorTexto(int position) {
        if (position % 2 == 0) {
            return ContextCompat.getColor(context, R.color.white);
        } else {
            return ContextCompat.getColor(context, R.color.purple);
        }
    }


    private static class ViewHolder {
        TextView textViewToken;
        TextView textViewPrecio;
    }
}

