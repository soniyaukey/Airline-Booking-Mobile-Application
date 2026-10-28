package com.example.airlinebookingapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airlinebookingapp.R;
import com.example.airlinebookingapp.models.Booking;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {
    private List<Booking> bookings;

    public BookingAdapter(List<Booking> bookings) {
        this.bookings = bookings;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookings.get(position);
        holder.bind(booking);
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public void updateBookings(List<Booking> newBookings) {
        this.bookings = newBookings;
        notifyDataSetChanged();
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        private TextView tvBookingId;
        private TextView tvFlightNumber;
        private TextView tvBookingDate;
        private TextView tvPassengerCount;
        private TextView tvTotalAmount;
        private TextView tvStatus;

        BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookingId = itemView.findViewById(R.id.tvBookingId);
            tvFlightNumber = itemView.findViewById(R.id.tvFlightNumber);
            tvBookingDate = itemView.findViewById(R.id.tvBookingDate);
            tvPassengerCount = itemView.findViewById(R.id.tvPassengerCount);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }

        void bind(Booking booking) {
            tvBookingId.setText(String.format("Booking #%s", booking.getBookingId()));
            tvFlightNumber.setText(String.format("Flight: %s", booking.getFlightId()));
            tvBookingDate.setText(String.format("Date: %s", booking.getBookingDate()));
            tvPassengerCount.setText(String.format("%d Passengers", booking.getPassengerCount()));
            tvTotalAmount.setText(String.format("$%.2f", booking.getTotalAmount()));
            tvStatus.setText(booking.getBookingStatus());
        }
    }
}
