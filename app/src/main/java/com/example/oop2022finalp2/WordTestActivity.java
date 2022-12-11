package com.example.oop2022finalp2;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.oop2022finalp2.Data.Question;
import com.example.oop2022finalp2.Data.WordBaseManager;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Fanyu Xia
 * @description:单词测试功能
 * @date :2022/12/09 22:43
 **/
public class WordTestActivity extends AppCompatActivity {

    private int count;
    private int current;
    private  boolean wrongMode;//标志变量，判断是否进入错题模式

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_test);

        WordBaseManager.getWordBaseManager();
        final List<Question> list =WordBaseManager.getWordBaseManager().getQuestion();

        count = list.size();
        current = 0;
        wrongMode=false;//默认情况

        final TextView tv_question = findViewById(R.id.question);
        final RadioButton[] radioButtons = new RadioButton[4];
        radioButtons[0] = findViewById(R.id.answerA);
        radioButtons[1] = findViewById(R.id.answerB);
        radioButtons[2] = findViewById(R.id.answerC);
        radioButtons[3] = findViewById(R.id.answerD);
        Button btn_previous = findViewById(R.id.btn_previous);
        Button btn_next = findViewById(R.id.btn_next);
        final TextView tv_explaination = findViewById(R.id.explaination);
        final RadioGroup radioGroup = findViewById(R.id.radioGroup);
        //为控件赋值
        Question q = list.get(0);
        tv_question.setText(q.question);
        tv_explaination.setText(q.explaination);
        radioButtons[0].setText(q.answerA);
        radioButtons[1].setText(q.answerB);
        radioButtons[2].setText(q.answerC);
        radioButtons[3].setText(q.answerD);

        btn_next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (current < count - 1) {//若当前题目不为最后一题，点击next按钮跳转到下一题；否则不响应
                    current++;
                    //更新题目
                    Question q = list.get(current);
                    tv_question.setText(q.question);
                    radioButtons[0].setText(q.answerA);
                    radioButtons[1].setText(q.answerB);
                    radioButtons[2].setText(q.answerC);
                    radioButtons[3].setText(q.answerD);
                    tv_explaination.setText(q.explaination);

                    //若之前已经选择过，则应记录选择
                    radioGroup.clearCheck();
                    if (q.selectedAnswer != -1) {
                        radioButtons[q.selectedAnswer].setChecked(true);
                    }

                }
                //错题模式的最后一题
                else if(current==count-1&& wrongMode==true){
                    new AlertDialog.Builder(WordTestActivity.this)
                            .setTitle("提示")
                            .setMessage("已经到达最后一题，是否退出？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    WordTestActivity.this.finish();
                                }
                            })
                            .setNegativeButton("取消",null)
                            .show();

                }
                else{
                    //当前题目为最后一题时，告知用户作答正确的数量和作答错误的数量，并询问用户是否要查看错题
                    final List<Integer>  wrongList=checkAnswer(list);
                    //作对所有题目
                    if(wrongList.size()==0){
                        new AlertDialog.Builder(WordTestActivity.this)
                                .setTitle("提示")
                                .setMessage("恭喜你全部回答正确！")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        WordTestActivity.this.finish();
                                    }
                                }).show();

                    }
                    else
                        new AlertDialog.Builder(WordTestActivity.this)
                                .setTitle("提示")
                                .setMessage("您答对了"+(list.size()-wrongList.size())+
                                        "道题目；答错了"+wrongList.size()+"道题目。是否查看错题？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int which) {

                                        //判断进入错题模式
                                        wrongMode=true;
                                        List<Question> newList=new ArrayList<Question>();
                                        //将错误题目复制到newList中
                                        for(int i=0;i< wrongList.size();i++){
                                            newList.add(list.get(wrongList.get(i)));
                                        }
                                        //将原来的list清空
                                        list.clear();
                                        //将错误题目添加到原来的list中
                                        for(int i=0;i<newList.size();i++){
                                            list.add(newList.get(i));
                                        }
                                        current=0;
                                        count=list.size();
                                        //更新显示时的内容
                                        Question q = list.get(current);
                                        tv_question.setText(q.question);
                                        radioButtons[0].setText(q.answerA);
                                        radioButtons[1].setText(q.answerB);
                                        radioButtons[2].setText(q.answerC);
                                        radioButtons[3].setText(q.answerD);
                                        tv_explaination.setText(q.explaination);
                                        //显示解析
                                        tv_explaination.setVisibility(View.VISIBLE);
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int which) {
//点击取消时，关闭当前activity
                                        WordTestActivity.this.finish();
                                    }
                                }).show();
                }
            }
        });
        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (current > 0)//若当前题目不为第一题，点击previous按钮跳转到上一题；否则不响应
                {
                    current--;
                    Question q = list.get(current);
                    tv_question.setText(q.question);
                    radioButtons[0].setText(q.answerA);
                    radioButtons[1].setText(q.answerB);
                    radioButtons[2].setText(q.answerC);
                    radioButtons[3].setText(q.answerD);
                    tv_explaination.setText(q.explaination);


                    //若之前已经选择过，则应记录选择
                    radioGroup.clearCheck();
                    if (q.selectedAnswer != -1) {
                        radioButtons[q.selectedAnswer].setChecked(true);
                    }

                }

            }
        });
        //选择选项时更新选择
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                for (int i = 0; i < 4; i++) {
                    if (radioButtons[i].isChecked() == true) {
                        list.get(current).selectedAnswer = i;
                        break;
                    }
                }

            }
        });
    }

    /*
判断用户作答是否正确，并将作答错误题目的下标生成list,返回给调用者
 */
    private List<Integer> checkAnswer(List<Question> list) {
        List<Integer> wrongList = new ArrayList<Integer>();
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).answer!=list.get(i).selectedAnswer){
                wrongList.add(i);
            }
        }
        return wrongList;
    }
}
