package com.example.oop2022finalp2.Data;

import java.io.Serializable;

/**
 * @author Zhilong Cao
 * @description:contain factory and field for word
 * @date :2022/11/15 14:17
 **/
public class Word implements Serializable {

    //熟练度，默认为0,记住后删除变为1，通过测试后变为2
    //展示单词时，应当选择熟练度为0的单词，测试时应当选择熟练度为1的单词
    private int proficiency;

    public int getProficiency() {
        return proficiency;
    }
    public void setProficiency(int proficiency) {
        this.proficiency = proficiency;
    }

    private static IsWordFactory wordFactory = null;
    /**
     * @author Zhilong Cao
     * @description:contain factory and field for word
     * @date :2022/11/15 14:17
     * @return :Factory
     **/
    public static IsWordFactory getWordFactory()
    {

        if ( wordFactory == null)
        {
            wordFactory = new IsWordFactory() {
                @Override
                public Word creWord(String english,String chinese) {
                    return new Word(english,chinese);
                }
            };
        }
        return wordFactory;
    }
    /**
     * @author Zhilong Cao
     * @description: key for WordMap
     * @date :2022/11/15 14:17
     **/

    //单词的加入顺序，考虑是否写入构造方法，目前没有通过id来获取word的应用场景，如果用户能够删除，那么如果仅靠size()+1是会出现重复id的
    public Integer word_id;

    public String english;
    public String chinese;

    private Word(String english,String chinese)
    {
        this.english=english;
        this.chinese=chinese;
        this.proficiency=0;
    }


}
