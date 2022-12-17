package com.example.oop2022finalp2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.oop2022finalp2.Activities.ChangeWordBaseActivity;
import com.example.oop2022finalp2.Activities.UISwitchActivity;
import com.example.oop2022finalp2.Data.IsWordBaseFactory;
import com.example.oop2022finalp2.Data.TestBase;
import com.example.oop2022finalp2.Data.WordBase;
import com.example.oop2022finalp2.Data.WordBaseManager;
import com.example.oop2022finalp2.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.oop2022finalp2.databinding.ActivityMainBinding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    boolean isRefuse;

    /**
     * @author Zhilong Cao
     * @description:
     * @date :2022/12/03 13:15
     *
     **/
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {

        switch (requestCode) {
            case PERMISSION_REQUEST:
                Toast.makeText(getApplicationContext(), "已申请权限", Toast.LENGTH_SHORT).show();
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    String[] permissions = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int PERMISSION_REQUEST = 1;

    List<String> mPermissionList = new ArrayList<>();
    /**
     * @author Zhilong Cao
     * @description:
     * @date :2022/12/03 13:15
     *
     **/
    private void checkPermission() {
        mPermissionList.clear();
        //判断哪些权限未授予
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }
        /**
         * 判断是否为空
         */
        if (mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了
        } else {//请求权限方法
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(MainActivity.this, permissions, PERMISSION_REQUEST);
        }
    }

    /**
     * @author Zhilong Cao
     * @description:
     * @date :2022/12/03 13:15
     *
     **/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1024 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            // 检查是否有权限
            if (Environment.isExternalStorageManager())
            {
                isRefuse = false;
                // 授权成功
            }
            else
            {
                isRefuse = true;
                // 授权失败
            }
        }
    }
    static public InputStream inputStream;
    static public InputStream inputStreamCet6;
    static public InputStream inputStreamGre;
    /**
     * @author Zhilong Cao
     * @description:
     * @date :2022/12/03 13:14
     *
     **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //create WordBaseManager


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !isRefuse)
        {// android 11  且 不是已经被拒绝
            // 先判断有没有权限
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1024);
            }
        }
        else if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.R )
        {//android 8 Permission management
            checkPermission();
        }
        super.onCreate(savedInstanceState);
        //get Stream from res.raw.directory
        inputStream = getResources().openRawResource(R.raw.cet);
        inputStreamCet6 = getResources().openRawResource(R.raw.cet6);
        inputStreamGre = getResources().openRawResource(R.raw.gre);

        //
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        //get res



        WordBaseManager wordBaseManager = WordBaseManager.getWordBaseManager();
        TestBase testBase = wordBaseManager.getTestBase();



        //read file in raw


    }



    /**
     * @author Yuntao Jiang
     * @description: add right_top_menu
     * @date :2022/12/03 13:12
     *
     **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.right_top_menu,menu);
        return true;
    }

    /**
     * @author Yuntao Jiang
     * @description: Register menu event
     * @date :2022/12/03 13:11
     *
     **/
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //如果需要使用这个右上角菜单，可以继续添加case
        switch (item.getItemId())
        {
            case R.id.change_wordbase:
                Intent intent=new Intent(MainActivity.this, ChangeWordBaseActivity.class);
                startActivity(intent);
                break;
            case R.id.day_night:
                Intent intent2=new Intent(MainActivity.this, UISwitchActivity.class);
                startActivity(intent2);
                break;

            default:
        }
        return true;
    }
}