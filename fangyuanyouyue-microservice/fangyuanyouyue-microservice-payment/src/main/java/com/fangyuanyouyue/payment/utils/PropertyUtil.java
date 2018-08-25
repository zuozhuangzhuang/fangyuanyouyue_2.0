package com.fangyuanyouyue.payment.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {
    private static Properties loveFlyProperties;
    private PropertyUtil(){

    }
    public static Properties getInstance(){
        if(loveFlyProperties == null){
            loveFlyProperties = new Properties();
            InputStream in = null;
            try{
                in=PropertyUtil.class.getResourceAsStream("/loveFly.properties");
                loveFlyProperties.load(in);
            }catch(IOException e){
                e.printStackTrace();
            }finally{
                try{
                    in.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
        return loveFlyProperties;
    }
}
