package com.lyloou.lou.test;

import org.junit.Test;

import com.lyloou.lou.util.Udata;

import junit.framework.Assert;

import java.io.File;
import java.io.IOException;

public class LouTest {

	@Test
    public void testUdata() throws Exception{  
        int sum = 3;  
        byte[] datas = new byte[]{0x2f, 0x23, 0x44};
          System.out.println("Udata:"+Udata.getBitString(datas));
          System.out.println("getBit:"+Udata.getBit(datas[0], 0));
          System.out.println("getBit:"+Udata.getBit(datas[0], 1));
          System.out.println("getBit:"+Udata.getBit(datas[0], 2));
          System.out.println("getBit:"+Udata.getBit(datas[0], 3));
          System.out.println("getBit:"+Udata.getBit(datas[0], 4));
          System.out.println("getBit:"+Udata.getBit(datas[0], 5));
          System.out.println("getBit:"+Udata.getBit(datas[0], 6));
          System.out.println("getBit:"+Udata.getBit(datas[0], 7));
          System.out.println();
          
          System.out.println("bit:"+Udata.getBit(datas, 2, 6));
          System.out.println();
          System.out.println("changeBit_before:"+0x36);
          System.out.println("changeBit_after:"+Udata.changeBit((byte)0x36, 4, 1));
        Assert.assertEquals(3, sum);  
    }  

    @Test
    public void path() throws Exception{
        String path = "/lou/slk";
        System.out.println("isValidPath===>"+isValidPath(path));

        path = "//dkj/ sdf aa< \f : > \0 ~/ |";
        System.out.println("isValidPath===>"+isValidPath(path));
    }

    private static boolean isValidPath(String path){
        File f = new File(path);
        try{
            String cc = f.getCanonicalPath();
            System.out.println("path = [" + cc + "]");
            return true;
        } catch (IOException e){
            return false;
        }
    }
}  