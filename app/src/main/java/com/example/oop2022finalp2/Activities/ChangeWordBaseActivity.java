package com.example.oop2022finalp2.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oop2022finalp2.Data.Word;
import com.example.oop2022finalp2.Data.WordBase;
import com.example.oop2022finalp2.Data.WordBaseManager;
import com.example.oop2022finalp2.R;
import com.example.oop2022finalp2.databinding.ActivityChangeWordbaseBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ChangeWordBaseActivity extends AppCompatActivity {

    private ActivityChangeWordbaseBinding binding;

    /**
     * @author Yuntao Jiang
     * @description:
     * @date :2022/12/03 13:05
     *
     **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_wordbase);

        //获取当前选择的词库，如果还没选则选择默认词库
        WordBase selectWordBase=null;
        if(WordBaseManager.getWordBaseManager().getSelectWordBase()==null)
        {
            selectWordBase=WordBaseManager.getDefaultWordBase();
        }
        else selectWordBase=WordBaseManager.getWordBaseManager().getSelectWordBase();

        //左上角标题，“当前选择的词库：”，后面加上当前选择的词库名
        TextView textView=findViewById(R.id.textView_change_wordbase);
        textView.append(":"+selectWordBase.WordBase_name);

        //用一个ArrayList存放所有的词库的信息
        ArrayList<String> wordBaseInfo=new ArrayList<String>();
        WordBaseManager wordBaseManager = WordBaseManager.getWordBaseManager();
        //获取所有词库，id+名字+词数
        // 可以扩展的地方：后续可以根据学习进度进行展示
        for(WordBase tmpWordBase:wordBaseManager.WordBaseGroup.values())
        {
            if(tmpWordBase!=null)
            {
                String tmpStr=tmpWordBase.WordBase_id+"-"+tmpWordBase.WordBase_name+"   Words:"+tmpWordBase.WordMap.size();
                wordBaseInfo.add(tmpStr);
            }
        }

        //绑定ListView布局及内容
        ListView listView=findViewById(R.id.wordbase_listview);
        ListAdapter listAdapter=new ArrayAdapter<String>(ChangeWordBaseActivity.this,
                android.R.layout.simple_list_item_1,wordBaseInfo);
        listView.setAdapter(listAdapter);

        //弹出窗口，确认是否切换至点击的词库
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedWordBaseInfo=wordBaseInfo.get(i);
                String arg[]=selectedWordBaseInfo.split("-");

                //弹出窗口
                AlertDialog.Builder dialog=new AlertDialog.Builder(ChangeWordBaseActivity.this);
                dialog.setMessage("Change to this WordBase?");
                dialog.setCancelable(false);
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    //执行切换，注意需要将改变序列化保存下来
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        WordBaseManager.getWordBaseManager().setSelectWordBase(WordBaseManager.getWordBaseManager().getWordBase(Integer.valueOf(arg[0])));
                        WordBaseManager.setWordBaseManager();
                        refresh();
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    //不执行切换
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();
            }
        });


        FloatingActionButton button =findViewById(R.id.btn_addwordbase);
        //点击按钮开始创建新词库
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //绑定按钮的布局
                View dialogView = View.inflate(ChangeWordBaseActivity.this, R.layout.alertdialog_add_wordbase, null);
                AlertDialog dialog=new AlertDialog.Builder(ChangeWordBaseActivity.this).setView(dialogView).create();
                dialog.show();

                //获取编辑框
                EditText etWordBaseName = (EditText) dialogView.findViewById(R.id.et_wordbasename);
                //获取按钮
                Button btnConfirm = (Button) dialogView.findViewById(R.id.btn_confirm_add_wordbase);
                Button btnCancel = (Button) dialogView.findViewById(R.id.btn_cancel_add_wordbase);
                //绑定按钮点击事件
                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //获取到输入的词库名
                        final String wordBaseName = etWordBaseName.getText().toString();

                        //如果输入为空，发出提醒
                        if (TextUtils.isEmpty(wordBaseName) ) {
                            Toast.makeText(ChangeWordBaseActivity.this, "名称不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        //创建新的词库
                        //存在一个默认的114514，所以是size()
                        WordBase newWordBase=WordBase.getWordBaseFactory().creWordBase(WordBaseManager.getWordBaseManager().WordBaseGroup.size(),wordBaseName);
                        WordBaseManager.getWordBaseManager().addWordBase(
                                WordBaseManager.getWordBaseManager().WordBaseGroup.size()+1,newWordBase);

                        //序列化保存
                        WordBaseManager.setWordBaseManager();

                        //新加入的词库会被展示出来
                        wordBaseInfo.add(newWordBase.WordBase_id+"-"+newWordBase.WordBase_name+"   Words:"+newWordBase.WordMap.size());
                        ((ArrayAdapter<?>) listAdapter).notifyDataSetChanged();

                        dialog.dismiss();
                    }
                });
                //若点击取消，则无事发生
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    /**
     * @author Yuntao Jiang
     * @description: refresh to show the change of selectedWordBase
     * @date :2022/12/03 13:04
     *
     **/
    private void refresh() {
        //达到切换词库后界面刷新的效果，重启该活动
        finish();
        Intent intent = new Intent(ChangeWordBaseActivity.this, ChangeWordBaseActivity.class);
        startActivity(intent);
    }



}