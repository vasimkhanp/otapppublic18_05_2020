    package com.otapp.net.adapter;

    import android.content.Context;
    import android.support.annotation.NonNull;
    import android.support.annotation.Nullable;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ArrayAdapter;
    import android.widget.TextView;

    import com.otapp.net.R;
    import com.otapp.net.model.CountryCodePojo;

    import java.util.List;

    public class CountryCodeSpinAdapter extends ArrayAdapter<CountryCodePojo.CountryCode> {

        private Context mContext;
        int resource;
        List<CountryCodePojo.CountryCode> mCountryCodeList = null;

        public CountryCodeSpinAdapter(Context mContext, int resource, List<CountryCodePojo.CountryCode> mCountryCodeList) {
            super(mContext, resource, 0, mCountryCodeList);

            this.mContext = mContext;
            this.resource = resource;
            this.mCountryCodeList = mCountryCodeList;

        }

        @Override
        public int getCount() {
            if (mCountryCodeList == null) {
                return 0;
            }
            return mCountryCodeList.size();
        }

        @Override
        public CountryCodePojo.CountryCode getItem(int i) {
            return mCountryCodeList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getDropDownView(final int i, View view, final ViewGroup viewGroup) {

            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(resource, null);
                ViewHolder holder = new ViewHolder();
                holder.tvCountryCode = view.findViewById(R.id.tvCountryCode);
                view.setTag(holder);
            }

            final ViewHolder holder = (ViewHolder) view.getTag();

            final CountryCodePojo.CountryCode mCountryCode = mCountryCodeList.get(i);

            if (mCountryCode != null) {

                holder.tvCountryCode.setText(mCountryCode.name + " (" + mCountryCode.code+")");

            }

    //        view.setOnClickListener(new View.OnClickListener() {
    //            @Override
    //            public void onClick(View view) {
    //
    //                for (int j = 0; j < mCountryCodeList.size(); j++) {
    //                    if (j == i) {
    //                        mCountryCodeList.get(j).isSelected = true;
    //                    } else {
    //                        mCountryCodeList.get(j).isSelected = false;
    //                    }
    //                }
    //                notifyDataSetChanged();
    //
    //                onMealSelectListener.onMealSelected(i);
    //
    //                try {
    //                    Method method = Spinner.class.getDeclaredMethod("onDetachedFromWindow");
    //                    method.setAccessible(true);
    //                    method.invoke(spinMeal);
    //
    //                } catch (Exception e) {
    //                    e.printStackTrace();
    //                }
    //            }
    //        });

            return view;
        }

        @Override
        public @NonNull
        View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
            view = View.inflate(mContext, R.layout.spin_country_code, null);
            TextView tvCountryCode = view.findViewById(R.id.tvCountryCode);
            tvCountryCode.setText("" + mCountryCodeList.get(position).code);
            return view;
        }


        private class ViewHolder {
            TextView tvCountryCode;
        }
    }
