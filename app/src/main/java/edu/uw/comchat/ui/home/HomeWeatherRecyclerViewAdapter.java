package edu.uw.comchat.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.comchat.R;
import edu.uw.comchat.databinding.FragmentHomeWeatherCardBinding;
import edu.uw.comchat.ui.weather.WeatherReport;

/**
 * A recycler view adapter that displays the weather in a column format.
 */
public class HomeWeatherRecyclerViewAdapter extends
        RecyclerView.Adapter<HomeWeatherRecyclerViewAdapter.HomeWeatherRecyclerViewHolder> {

    private final List<WeatherReport> mReports;

    public HomeWeatherRecyclerViewAdapter(List<WeatherReport> reports) {
        mReports = reports;
    }

    @NonNull
    @Override
    public HomeWeatherRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeWeatherRecyclerViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_home_weather_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeWeatherRecyclerViewHolder holder, int position) {
        holder.setReport(mReports.get(position));
    }

    @Override
    public int getItemCount() {
        return mReports.size();
    }

    /**
     * A view holder that represents the weather for single day in a column view adapter.
     */
    static class HomeWeatherRecyclerViewHolder extends RecyclerView.ViewHolder {

        private final FragmentHomeWeatherCardBinding mBinding;

        /**
         * Creates a new view holder.
         * @param view the view.
         */
        public HomeWeatherRecyclerViewHolder(@NonNull View view) {
            super(view);
            mBinding = FragmentHomeWeatherCardBinding.bind(view);
        }

        /**
         * Sets the report for the view holder.
         * @param report the report being set.
         */
        public void setReport(WeatherReport report) {
            mBinding.textHomeWeatherDate.setText(report.getTime());
            mBinding.textHomeWeatherDescription.setText(report.getDescription());
            mBinding.textHomeWeatherTemp.setText(report.getTemp());

            int id = report.getId();
            if (id >= 200 && id <= 232) { // ID for thunderstorm
                mBinding.imageViewHomeWeather.setImageResource(R.drawable.ic_home_thunder_storm);
            } else if (id >= 300 && id <= 321) { // ID for drizzle
                mBinding.imageViewHomeWeather.setImageResource(R.drawable.ic_home_drizzle);
            } else if (id >= 500 && id <= 531) { // ID for rain
                mBinding.imageViewHomeWeather.setImageResource(R.drawable.ic_home_rain);
            } else if (id >= 600 && id <= 622) { // ID for snow
                mBinding.imageViewHomeWeather.setImageResource(R.drawable.ic_home_snow);
            } else if (id >= 700 && id <= 781) { // ID for atmosphere
                mBinding.imageViewHomeWeather.setImageResource(R.drawable.ic_home_fog);
            } else if (id == 800) {
                mBinding.imageViewHomeWeather.setImageResource(R.drawable.ic_home_clear_sky_day);
            } else if (id >= 801 && id <= 804) { // ID for cloud
                mBinding.imageViewHomeWeather.setImageResource(R.drawable.ic_home_cloudy);
            }
        }
    }
}
