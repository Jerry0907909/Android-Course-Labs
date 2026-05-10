package com.example.himalaya;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AvatarSelectActivity extends AppCompatActivity {

    public static final String EXTRA_SELECTED_AVATAR = "extra_selected_avatar";
    public static final int[] AVATAR_OPTIONS = {
            R.drawable.img1,
            R.drawable.img2,
            R.drawable.img3,
            R.drawable.img4,
            R.drawable.img5,
            R.drawable.img6
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_avatar_select);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.avatar_select_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView backView = findViewById(R.id.tv_avatar_back);
        GridView avatarGrid = findViewById(R.id.gv_avatar);

        avatarGrid.setAdapter(new AvatarAdapter(this, AVATAR_OPTIONS));
        avatarGrid.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_SELECTED_AVATAR, AVATAR_OPTIONS[position]);
            setResult(RESULT_OK, intent);
            finish();
        });
        backView.setOnClickListener(v -> finish());
    }
}
