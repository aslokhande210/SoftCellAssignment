package com.softcell.assignment.ui.adapter;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.softcell.assignment.R;
import com.softcell.assignment.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {

    private final Context context;
    private final ArrayList<User> users;
    private final Geocoder geocoder;

    public UserListAdapter(Context context, ArrayList<User> users) {
        geocoder = new Geocoder(context);
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_user_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.name.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
        holder.loanAmount.setText(String.format(Locale.getDefault(), "%d", user.getLoanAmount()));
        holder.geoLocation.setText(getGeoCodedAddress(user.getLatitude(), user.getLongitude()));
    }

    @Override
    public int getItemCount() {
        return users != null ? users.size() : 0;
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.loan_amount)
        TextView loanAmount;
        @BindView(R.id.geo_location)
        TextView geoLocation;

        UserViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private String getGeoCodedAddress(double lat, double lng) {
        String strAddress = "";
        if (Geocoder.isPresent()) {
            List<Address> list;
            try {
                list = geocoder.getFromLocation(lat, lng, 2);

                Address address = list.get(0);
                strAddress = ("Name: " + address.getLocality() + "\n") +
                        "Sub - Admin Ares: " + address.getSubAdminArea() + "\n" +
                        "Admin Area: " + address.getAdminArea() + "\n" +
                        "Country: " + address.getCountryName() + "\n" +
                        "Country Code: " + address.getCountryCode() + "\n";
            } catch (Exception e) {
                e.printStackTrace();
            }
            return strAddress;
        }
        return strAddress;
    }
}
