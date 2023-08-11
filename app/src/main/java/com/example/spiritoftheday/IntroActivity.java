package com.example.spiritoftheday;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    private ViewPager screenPager;
    private IntroViewPagerAdapter introViewPagerAdapter;
    private Button btnNext;
    private Button btnGetStarted;
    private Animation btnAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Jika sudah login, langsung ke MainActivity
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(IntroActivity.this, MainActivity.class));
            finish();
            return;
        }

        if (notePrefData()) {
            // Jika sudah pernah menampilkan IntroActivity sebelumnya, langsung ke LoginActivity
            startActivity(new Intent(IntroActivity.this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_intro);

        // sembunyikan bilah tindakan
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // inisialisasi tampilan
        btnNext = findViewById(R.id.btn_next);
        btnGetStarted = findViewById(R.id.btn_get_started);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_animation);

        // isi layar daftar
        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("Selamat Datang", "Aplikasi catatan kami membantu Anda menyimpan dan mengatur ide, to-do list, dan informasi penting dengan mudah. Nikmati pengalaman pencatatan yang intuitif dan efisien.", R.drawable.img1));
        mList.add(new ScreenItem("Fitur Unggulan", "Aplikasi ini dilengkapi dengan berbagai fitur unggulan seperti pengelompokan catatan berdasarkan kategori, pengingat jadwal, sinkronisasi lintas perangkat, dan perlindungan kata sandi untuk menjaga kerahasiaan data Anda.", R.drawable.img2));
        mList.add(new ScreenItem("Organisasi Lebih Mudah", "Dengan tampilan yang bersih dan antarmuka yang mudah digunakan, Anda dapat dengan cepat mengakses dan mengelola catatan Anda. Jangan biarkan ide-ide brilian terlewatkan – gunakan aplikasi catatan kami untuk tetap terorganisir dalam segala hal.", R.drawable.img3));

        // atur viewpager
        screenPager = findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this, mList);
        screenPager.setAdapter(introViewPagerAdapter);

        // tambahkan pendengar perubahan halaman pada ViewPager
        screenPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == mList.size() - 1) {
                    // Jika halaman terakhir ditampilkan, tampilkan tombol "Get Started" dan sembunyikan tombol "Next"
                    btnGetStarted.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.INVISIBLE);
                    // mengatur animasi
                    btnGetStarted.setAnimation(btnAnim);
                } else {
                    // Jika bukan halaman terakhir, tampilkan tombol "Next" dan sembunyikan tombol "Get Started"
                    btnNext.setVisibility(View.VISIBLE);
                    btnGetStarted.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        // klik tombol berikutnya Pendengar
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = screenPager.getCurrentItem();
                if (position < mList.size() - 1) {
                    position++;
                    screenPager.setCurrentItem(position);
                }
            }
        });

        // Memulai klik tombol pendengar
        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser == null) {
                    Intent loginIntent = new Intent(IntroActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                } else {
                    Intent mainIntent = new Intent(IntroActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                }

                // Simpan preferensi bahwa layar intro telah ditampilkan
                savePrefsData();

                // Selesaikan IntroActivity
                finish();
            }
        });
    }

    private boolean notePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        return pref.getBoolean("isIntroOpnend", false);
    }

    private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpnend", true);
        editor.apply();
    }
}
