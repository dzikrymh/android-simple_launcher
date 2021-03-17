package id.mdsolusindo.partriplauncher.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

import id.mdsolusindo.partriplauncher.databinding.PagerLayoutBinding;
import id.mdsolusindo.partriplauncher.models.PagerObject;

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<PagerObject> pagerAppList;
    int cellHeight, numColumn;
    ArrayList<AppAdapter> appAdapterList = new ArrayList<>();

    public ViewPagerAdapter(Context context, ArrayList<PagerObject> pagerAppList, int cellHeight, int numColumn){
        this.context = context;
        this.pagerAppList = pagerAppList;
        this.cellHeight = cellHeight;
        this.numColumn = numColumn;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        PagerLayoutBinding layout = PagerLayoutBinding.inflate(inflater, container, false);

        layout.grid.setNumColumns(numColumn);

        AppAdapter mGridAdapter = new AppAdapter(context, pagerAppList.get(position).getAppList(), cellHeight);
        layout.grid.setAdapter(mGridAdapter);

        appAdapterList.add(mGridAdapter);

        container.addView(layout.getRoot());
        return layout.getRoot();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return pagerAppList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public void notifyGridChanged(){
        for(int i = 0; i < appAdapterList.size();i++){
            appAdapterList.get(i).notifyDataSetChanged();
        }
    }
}
