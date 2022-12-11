package com.example.oop2022finalp2.Data;

import java.io.Serializable;

import java.util.TreeMap;

/**
 * @author Zhilong Cao
 * @description:
 * @date :2022/11/17 14:02
 **/
public class WordBase implements Serializable {

    //存放单词
    public TreeMap<Integer,Word> WordMap = new TreeMap<Integer, Word>();

    public Integer WordBase_id;
    public String WordBase_name;


    public WordBase(int id,String wordBaseName)
    {
        this.WordBase_id=id;
        this.WordBase_name=wordBaseName;
    }
    /**
     * @author Zhilong Cao
     * @description:
     * @date :2022/11/17 14:02
     * @return :return Word if exist ,return null if not exist
     **/
    public Word getWord(Integer integer)
    {
        if (WordMap.get(integer) == null)
        {
            return null;
        }
        return WordMap.get(integer);
    }

    /**
     * @author Zhilong Cao
     * @description:
     * @date :2022/11/17 14:02
     * @return : Return true if add success. Return false if already exist.
     **/
    public Boolean addWord(Integer integer,Word word)
    {
        if (WordMap.get(integer) != null)
        {
            return false;
        }
        WordMap.put(integer,word);
        return true;
    }

    private static IsWordBaseFactory wordBaseFactory = null;
    /**
     * @author Zhilong Cao
     * @description:
     * @date :2022/11/17 14:02
     * @return : Return WordBaseFactory
     **/
    public static IsWordBaseFactory getWordBaseFactory ()
    {
        if (wordBaseFactory == null)
        {
            wordBaseFactory = new IsWordBaseFactory() {
                @Override
                public WordBase creWordBase(int id,String wordBaseName) {
                    return new WordBase(id,wordBaseName);
                }
            };
        }
        return wordBaseFactory;
    }
}
