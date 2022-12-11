package com.example.oop2022finalp2.Data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Zhilong Cao
 * @description: Test should contain both right words and wrong words
 * @date :2022/12/5 12:37
 **/
public class Test implements Serializable {
    public Integer word_id;
    public double grade;
    public ArrayList<Word> rightWords;
    public ArrayList<Word> wrongWords;

    private static IsTestFactory isTestFactory = null;

    /**
     * @author Zhilong Cao
     * @description: getFactory
     * @date :2022/12/5 12:37
     **/
    public static IsTestFactory getTestFactory()
    {

        if ( isTestFactory == null)
        {
            isTestFactory = new IsTestFactory() {
                @Override
                public Test creTest() {
                    return new Test();
                }
            };
        }
        return isTestFactory;
    }



}
