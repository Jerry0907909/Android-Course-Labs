package com.example.himalaya;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProfileExperimentActivity extends AppCompatActivity {

    private ImageView avatarView;
    private final ActivityResultLauncher<Intent> avatarSelectorLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() != RESULT_OK || result.getData() == null) {
                    return;
                }
                int avatarResId = result.getData().getIntExtra(
                        AvatarSelectActivity.EXTRA_SELECTED_AVATAR,
                        AvatarSelectActivity.AVATAR_OPTIONS[0]
                );
                avatarView.setImageResource(avatarResId);
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_experiment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.profile_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView chooseAvatarView = findViewById(R.id.tv_choose_avatar);
        TextView addressEntryView = findViewById(R.id.tv_address_entry);
        TextView chapter05EntryView = findViewById(R.id.tv_chapter05_entry);
        TextView chapter06EntryView = findViewById(R.id.tv_chapter06_entry);
        TextView chapter06ClientEntryView = findViewById(R.id.tv_chapter06_client_entry);
        TextView chapter07EntryView = findViewById(R.id.tv_chapter07_entry);
        avatarView = findViewById(R.id.iv_avatar);

        avatarView.setImageResource(AvatarSelectActivity.AVATAR_OPTIONS[0]);
        chooseAvatarView.setOnClickListener(v ->
                avatarSelectorLauncher.launch(new Intent(this, AvatarSelectActivity.class)));
        avatarView.setOnClickListener(v ->
                avatarSelectorLauncher.launch(new Intent(this, AvatarSelectActivity.class)));
        addressEntryView.setOnClickListener(v ->
                startActivity(new Intent(this, AddressEditActivity.class)));
        chapter05EntryView.setOnClickListener(v ->
                startActivity(new Intent(this, Chapter05DatabaseActivity.class)));
        chapter06EntryView.setOnClickListener(v ->
                startActivity(new Intent(this, Chapter06ShopActivity.class)));
        chapter06ClientEntryView.setOnClickListener(v ->
                startActivity(new Intent(this, Chapter06ClientActivity.class)));
        chapter07EntryView.setOnClickListener(v ->
                startActivity(new Intent(this, Chapter07Activity.class)));
    }
}
