package com.example.oop2022finalp2.Data;

import java.io.Serializable;
import java.util.TreeMap;

/**
 * @author Zhilong Cao
 * @description: Use Obj WordBaseManager to use getTestBase to get TestBase and record Test
 * @date :2022/12/5 12:41
 **/
public class TestBase implements Serializable {
    public TreeMap<Integer,Test> TestMap = new TreeMap<Integer,Test>();

    public Integer TestBaseId;
    public String TestBaseName;


    /**
     * @author Zhilong Cao
     * @description: getTest object by id
     * @date :2022/12/5 12:37
     **/
    public Test getTest(Integer integer)
    {
        if (TestMap.get(integer) == null)
        {
            return null;
        }
        else
             return TestMap.get(integer);
    }
    /**
     * @author Zhilong Cao
     * @description: add a Test to TestMap, the key is the id. make sure to create a new test before adding this test.
     * @date :2022/12/5 12:37
     **/
    public void AddTest(Test test) throws Exception {
        if (TestMap.get(TestMap.size() + 1) != null)
            throw new Exception();

        TestMap.put(TestMap.size() + 1,test);
    }
    private static IsTestBaseFactory isTestBaseFactory = null;

    /**
     * @author Zhilong Cao
     * @description: get TestBaseFactory object
     * @date :2022/12/5 12:37
     **/
    public static IsTestBaseFactory getTestBaseFactory()
    {

        if ( isTestBaseFactory == null)
        {
              isTestBaseFactory = new IsTestBaseFactory() {
                  @Override
                  public TestBase creTestBase() {
                      return new TestBase();
                  }
              };
        }
        return isTestBaseFactory;
    }

}
