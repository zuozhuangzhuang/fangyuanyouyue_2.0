package com.fangyuanyouyue.forum.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;


/**
 * 返回数据基础类
 *
 */
public class BaseClientResult {

	private HashMap<String,Object> map;
	
	
	
	public BaseClientResult(String reCode, String reMsg) {
		map = new HashMap<String,Object>();
		map.put("reCode", reCode);
		map.put("reMsg", reMsg);
	}
	
	public BaseClientResult(String reCode) {
		map = new HashMap<String,Object>();
		map.put("reCode", reCode);
		map.put("reMsg", "");
	}
	
	public BaseClientResult(){
		map = new HashMap<String,Object>();
	}

	
	public void put(String key, Object obj){
		map.put(key, obj);
	}
	
	
	public HashMap<String, Object> getMap() {
		return map;
	}

	public void setMap(HashMap<String, Object> map) {
		this.map = map;
	}

	/**
	 * 将对象转为json字符串
	 * @return
	 * @throws IOException
	 */
	public String toJson() throws IOException {
		ObjectMapper mapper = getDefaultObjectMapper();
		try {
			return mapper.writeValueAsString(map);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "";
		}
		
	}
	
	/**
	 * 将json转为加密后的字符串
	 * @param str
	 * @return
	 */
	public String toEncJson(String str){
		return str;
	}
	
	
	
    private ObjectMapper getDefaultObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        //设置将对象转换成JSON字符串时候:包含的属性不能为空或"";    
        //Include.Include.ALWAYS 默认    
        //Include.NON_DEFAULT 属性为默认值不序列化    
        //Include.NON_EMPTY 属性为 空（""）  或者为 NULL 都不序列化    
        //Include.NON_NULL 属性为NULL 不序列化    
       // mapper.setSerializationInclusion(Inclusion.NON_EMPTY);  
        //设置将MAP转换为JSON时候只转换值不等于NULL的  
       // mapper.configure(SerializationConfig.Feature.WRITE_NULL_MAP_VALUES, false);  
       // mapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);  
       // mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));  
        //设置有属性不能映射成PO时不报错  
        //mapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);  
        return mapper;  
    }

}
