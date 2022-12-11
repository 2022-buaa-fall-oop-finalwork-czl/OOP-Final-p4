package com.example.oop2022finalp2.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.oop2022finalp2.Data.Word;
import com.example.oop2022finalp2.Data.WordBase;
import com.example.oop2022finalp2.Data.WordBaseManager;
import com.example.oop2022finalp2.R;
import com.example.oop2022finalp2.databinding.FragmentHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    //存放展示的单词
    private ArrayList<String> wordsToBeShow=new ArrayList<String>();

    /**
     * @author Yuntao Jiang
     * @description:
     * @date :2022/12/03 13:07
     *
     **/
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //获取当前选择的词库中的单词,退出后能够保存上次选择的词库
        WordBase selectWordBase=null;
        if(WordBaseManager.getWordBaseManager().getSelectWordBase()==null)
        {
            selectWordBase=WordBaseManager.getDefaultWordBase();
            //序列化保存
            WordBaseManager.getWordBaseManager().setSelectWordBase(selectWordBase);
            WordBaseManager.setWordBaseManager();
        }
        else selectWordBase=WordBaseManager.getWordBaseManager().getSelectWordBase();


        //判断isEmpty()，防止切换fragment重复添加
       if(wordsToBeShow.isEmpty())
       {
           //获取熟练度为0的单词,注意ListView的上下边界，确保展示完全
           for(Word tmpWord:selectWordBase.WordMap.values())
           {
               if(tmpWord!=null&&tmpWord.getProficiency()==0)
               {
                   wordsToBeShow.add( tmpWord.english + tmpWord.chinese);
               }
           }
       }

        //绑定布局与内容
        ListView listView=binding.wordListView;
        ListAdapter listAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,wordsToBeShow);
        listView.setAdapter(listAdapter);

        //单击跳转到有道
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //获取选中单词
                String selectedWordInfo=(String) listAdapter.getItem(i);
                //提取英文
                String arg[]=selectedWordInfo.split("\\s+");
                //转到网址
                Uri uri=Uri.parse("https://www.youdao.com/m/result?word="+arg[0]+"&lang=en");
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                listView.getContext().startActivity(intent);
            }
        });

        //长按删除，熟练度置为1，此时删除并非从词库中删除，而是指已经学会，仍需后续考试
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //弹出确认提示框
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("提示！");
                builder.setMessage("确定删除？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean isSuccess;
                        //从ListView中移出
                        if (wordsToBeShow.remove(position) != null) {
                            System.out.println("Success");
                            //获取要删除的单词的英文
                            String selectedWordInfo=wordsToBeShow.get(position - 1).toString();
                            String[] arg=selectedWordInfo.split("\\s+");
                            //将对应单词熟练度置为1
                            WordBase wordBase = WordBaseManager.getWordBaseManager().getSelectWordBase();
                            for(Word tmpWord:wordBase.WordMap.values())
                            {
                                if(tmpWord.english.equals(arg[0]))
                                {
                                    tmpWord.setProficiency(1);
                                }
                            }
                            //更新数据
                            WordBaseManager.setWordBaseManager();
                        } else {
                            System.out.println("Failed");
                        }
                        //刷新ListView
                        ((ArrayAdapter<?>) listAdapter).notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();
                // 返回true避免与点击事件冲突
                return true;
            }
        });

        FloatingActionButton button = (FloatingActionButton) binding.fraghomeaddbutton;
        //点击按钮开始创建新单词
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                final AlertDialog dialog = builder.create();
                View dialogView = View.inflate(getContext(), R.layout.alertdialog_add_word, null);
                //设置对话框布局
                dialog.setView(dialogView);
                dialog.show();
                //获取输入的内容和两个按钮
                EditText etEnglish = (EditText) dialogView.findViewById(R.id.et_english);
                EditText etChinese = (EditText) dialogView.findViewById(R.id.et_chinese);
                Button btnConfirm = (Button) dialogView.findViewById(R.id.btn_confirm);
                Button btnCancel = (Button) dialogView.findViewById(R.id.btn_cancel);
                //点击确认后
                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String english = etEnglish.getText().toString();
                        final String chinese = etChinese.getText().toString();
                        //输入为空，提醒
                        if (TextUtils.isEmpty(english) && TextUtils.isEmpty(chinese)) {
                            Toast.makeText(getContext(), "单词和释义均不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //创建新单词，加入当前词库中
                        Word newWord=Word.getWordFactory().creWord(english,chinese);
                        WordBaseManager.getWordBaseManager().getSelectWordBase().addWord(
                                WordBaseManager.getWordBaseManager().getSelectWordBase().WordMap.size()+1,newWord);

                        //更新数据
                        WordBaseManager.setWordBaseManager();

                        //新加入的单词会被展示出来
                        wordsToBeShow.add(english+"               "+chinese);
                        ((ArrayAdapter<?>) listAdapter).notifyDataSetChanged();

                        dialog.dismiss();

                    }
                });
                //点击取消，无事发生
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
        return root;
    }

    /**
     * @author Yuntao Jiang
     * @description:
     * @date :2022/12/03 13:07
     *
     **/
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * @author Yuntao Jiang
     * @description: refresh the HomeFragment to show the change
     * @date :2022/12/03 13:04
     *
     **/
    @Override
    public void onResume() {
        super.onResume();

        //更换词库后，重新获取单词，来刷新单词展示列表，目前还需要切换到另一个Fragment再切换回来才会有显示
        wordsToBeShow.clear();
        //获取当前选择的词库中的单词,为了退出后能够保存上次选择的词库，要将选择的词库
        WordBase selectWordBase=null;
        if(WordBaseManager.getWordBaseManager().getSelectWordBase()==null)
        {
            selectWordBase=WordBaseManager.getDefaultWordBase();
            WordBaseManager.getWordBaseManager().setSelectWordBase(selectWordBase);
            WordBaseManager.setWordBaseManager();
        }
        else selectWordBase=WordBaseManager.getWordBaseManager().getSelectWordBase();

        //获取熟练度为0的单词,注意ListView的上下边界，确保展示完全
        for(Word tmpWord:selectWordBase.WordMap.values())
        {
            if(tmpWord!=null&&tmpWord.getProficiency()==0)
            {
                wordsToBeShow.add(tmpWord.english
                        +"               "+ tmpWord.chinese);
            }
        }
    }
}