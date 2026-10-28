package com.example.airlinebookingapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airlinebookingapp.R;
import com.example.airlinebookingapp.models.Flight;

import java.util.List;

public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.FlightViewHolder> {
    private List<Flight> flights;
    private OnFlightClickListener listener;

    public interface OnFlightClickListener {
        void onBookClick(Flight flight);
    }

    public FlightAdapter(List<Flight> flights, OnFlightClickListener listener) {
        this.flights = flights;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_flight, parent, false);
        return new FlightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightViewHolder holder, int position) {
        Flight flight = flights.get(position);
        holder.bind(flight);
    }

    @Override
    public int getItemCount() {
        return flights.size();
    }

    public void updateFlights(List<Flight> newFlights) {
        this.flights = newFlights;
        notifyDataSetChanged();
    }

    class FlightViewHolder extends RecyclerView.ViewHolder {
        private TextView tvFlightNumber;
        private TextView tvDepartureTime;
        private TextView tvArrivalTime;
        private TextView tvPrice;
        private Button btnBook;

        FlightViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFlightNumber = itemView.findViewById(R.id.tvFlightNumber);
            tvDepartureTime = itemView.findViewById(R.id.tvDepartureTime);
            tvArrivalTime = itemView.findViewById(R.id.tvArrivalTime);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnBook = itemView.findViewById(R.id.btnBook);
        }

        void bind(Flight flight) {
            tvFlightNumber.setText(flight.getFlightNumber());
            tvDepartureTime.setText(flight.getDepartureTime());
            tvArrivalTime.setText(flight.getArrivalTime());
            tvPrice.setText(String.format("$%.2f", flight.getPrice()));

            btnBook.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onBookClick(flight);
                }
            });
        }
    }
}
