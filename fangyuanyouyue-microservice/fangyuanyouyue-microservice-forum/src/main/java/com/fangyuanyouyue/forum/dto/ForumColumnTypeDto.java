package com.fangyuanyouyue.forum.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 论坛专栏类型
 */
@Getter
@Setter
@ToString
public class ForumColumnTypeDto {

    private Integer typeId;//帖子id

    private String name;//专栏名称

    private List<ForumColumnDto> forumColumnList;


    public ForumColumnTypeDto() {
    	
    }
    
    public static List<ForumColumnTypeDto> toDtoList(List<ForumColumnDto> list) {
       HashMap<Integer,ArrayList<ForumColumnDto>> map = new HashMap<Integer, ArrayList<ForumColumnDto>>();
       for(ForumColumnDto dto:list) {
    	   if(map.get(dto.getTypeId())!=null){
    		   ArrayList<ForumColumnDto> dtos = map.get(dto.getTypeId());
    		   dtos.add(dto);
    		   map.put(dto.getTypeId(), dtos);
    		   
    	   }else {
    		   ArrayList<ForumColumnDto> dtos = new ArrayList<ForumColumnDto>();
    		   dtos.add(dto);
    		   map.put(dto.getTypeId(), dtos);
    	   }
       }
       ArrayList<ForumColumnTypeDto> dtos = new ArrayList<ForumColumnTypeDto>();
       for(Integer typeId:map.keySet()) {
    	   ForumColumnTypeDto dto = new ForumColumnTypeDto();
    	   dto.setTypeId(typeId);
    	   dto.setForumColumnList(map.get(typeId));
    	   dto.setName(map.get(typeId).get(0).getTypeName());
    	   dtos.add(dto);
       }
       return dtos;
    }

   
}
