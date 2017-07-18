package de.android.nerdlauncher.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.android.nerdlauncher.R;

public class NerdLauncherFragment extends Fragment{
    private static final String TAG = NerdLauncherFragment.class.getSimpleName();

    private RecyclerView recyclerView;

    public static Fragment newInstance() {
        return new NerdLauncherFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nerd_launcher, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.fragment_nerd_launcher_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setupAdapter();
        return view;
    }

    private void setupAdapter() {
        Intent startupIntent = new Intent(Intent.ACTION_MAIN);
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(startupIntent, 0);
        Collections.sort(activities, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo a, ResolveInfo b) {
                PackageManager pm = getActivity().getPackageManager();
                return String.CASE_INSENSITIVE_ORDER.compare(
                        a.loadLabel(pm).toString(),
                        b.loadLabel(pm).toString()
                );
            }
        });
        Log.i(TAG, "Found " +activities.size() + " activities.");
        recyclerView.setAdapter(new ActivityAdapter(activities  ));
    }

    private class ActivityHolder extends RecyclerView.ViewHolder {
        private ResolveInfo resolveInfo;
        private TextView nameTextView;

        public ActivityHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView)itemView;
        }

        public void bindActivity(ResolveInfo resolveInfo) {
            this.resolveInfo = resolveInfo;
            PackageManager pm = getActivity().getPackageManager();
            String appName = resolveInfo.loadLabel(pm).toString();
            nameTextView.setText(appName);
        }
    }

    private class ActivityAdapter extends RecyclerView.Adapter<ActivityHolder> {
        private final List<ResolveInfo> activities;

        public ActivityAdapter(List<ResolveInfo> activities) {
            this.activities = activities;
        }

        @Override
        public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ActivityHolder(view);
        }

        @Override
        public void onBindViewHolder(ActivityHolder holder, int position) {
            ResolveInfo resolveInfo = activities.get(position);
            holder.bindActivity(resolveInfo);
        }

        @Override
        public int getItemCount() {
            return activities.size();
        }
    }
}
