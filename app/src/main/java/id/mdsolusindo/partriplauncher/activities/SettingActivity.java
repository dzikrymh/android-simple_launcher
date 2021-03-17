package id.mdsolusindo.partriplauncher.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import id.mdsolusindo.partriplauncher.R;
import id.mdsolusindo.partriplauncher.databinding.ActivitySettingBinding;
import id.mdsolusindo.partriplauncher.viewmodels.SettingViewModel;

public class SettingActivity extends AppCompatActivity {

    private ActivitySettingBinding binding;
    private SettingViewModel viewModel;

    private int REQUEST_CODE_IMAGE = 1;
    private String PREFS_NAME = "PartripLauncherPrefs";

    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(SettingViewModel.class);
        setContentView(binding.getRoot());

        binding.gridSizeButton.setOnClickListener(v -> saveData());

        binding.homeScreenButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE_IMAGE);
        });

        getData();
    }

    private void getData(){
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String imageUriString = sharedPreferences.getString("imageUri", null);
        int numRow = sharedPreferences.getInt("numRow", 7);
        int numColumn = sharedPreferences.getInt("numColumn", 5);

        if(imageUriString != null){
            imageUri = Uri.parse(imageUriString);
            binding.homeScreenImage.setImageURI(imageUri);
        }

        binding.numRow.setText(String.valueOf(numRow));
        binding.numColumn.setText(String.valueOf(numColumn));

    }

    private void saveData(){
        SharedPreferences.Editor sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();

        if(imageUri != null)
            sharedPreferences.putString("imageUri", imageUri.toString());

        sharedPreferences.putInt("numRow", Integer.parseInt(binding.numRow.getText().toString()));
        sharedPreferences.putInt("numColumn", Integer.parseInt(binding.numColumn.getText().toString()));
        sharedPreferences.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_IMAGE && resultCode == Activity.RESULT_OK){
            imageUri = data.getData();
            binding.homeScreenImage.setImageURI(imageUri);
            saveData();
        }
    }
}