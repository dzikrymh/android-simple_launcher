package id.mdsolusindo.partriplauncher.models;

import java.util.ArrayList;

import id.mdsolusindo.partriplauncher.models.AppObject;

public class PagerObject {
    private ArrayList<AppObject> appList;

    public PagerObject(ArrayList<AppObject> appList){
        this.appList = appList;
    }

    public ArrayList<AppObject> getAppList() {
        return appList;
    }
}
