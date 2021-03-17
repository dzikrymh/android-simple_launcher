package id.mdsolusindo.partriplauncher.models;

import android.graphics.drawable.Drawable;

public class AppObject {
    private String name, packageName;
    private Drawable image;
    private Boolean isAppInDrawer;

    public AppObject(String packageName, String name, Drawable image, Boolean isAppInDrawer){
        this.name = name;
        this.packageName = packageName;
        this.image = image;
        this.isAppInDrawer = isAppInDrawer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public Boolean getAppInDrawer() {
        return isAppInDrawer;
    }

    public void setAppInDrawer(Boolean appInDrawer) {
        isAppInDrawer = appInDrawer;
    }
}
