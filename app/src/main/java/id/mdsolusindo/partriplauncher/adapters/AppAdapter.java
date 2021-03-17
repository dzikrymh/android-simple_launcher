package id.mdsolusindo.partriplauncher.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import java.util.List;

import id.mdsolusindo.partriplauncher.activities.MainActivity;
import id.mdsolusindo.partriplauncher.databinding.ItemAppBinding;
import id.mdsolusindo.partriplauncher.models.AppObject;

public class AppAdapter extends BaseAdapter {
    private Context context;
    private List<AppObject> appList;
    private int cellHeight;

    public AppAdapter(Context context, List<AppObject> appList, int cellHeight){
        this.context = context;
        this.appList = appList;
        this.cellHeight = cellHeight;
    }

    @Override
    public int getCount() {
        return appList.size();
    }

    @Override
    public Object getItem(int position) {
        return appList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ItemAppBinding binding = ItemAppBinding.inflate(inflater, parent, false);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, cellHeight);
        binding.layout.setLayoutParams(lp);

        binding.image.setImageDrawable(appList.get(position).getImage());
        binding.label.setText(appList.get(position).getName());

        binding.layout.setOnClickListener(v -> ((MainActivity) context).itemPress(appList.get(position)));
        binding.layout.setOnLongClickListener(v -> {
            ((MainActivity) context).itemLongPress(appList.get(position));
            return true;
        });
        return binding.getRoot();
    }
}
