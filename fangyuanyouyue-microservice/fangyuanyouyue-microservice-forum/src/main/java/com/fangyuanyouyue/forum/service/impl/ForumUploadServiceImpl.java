package com.fangyuanyouyue.forum.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.forum.dao.ForumColumnMapper;
import com.fangyuanyouyue.forum.dao.ForumInfoMapper;
import com.fangyuanyouyue.forum.dto.ForumDetailDto;
import com.fangyuanyouyue.forum.model.ForumColumn;
import com.fangyuanyouyue.forum.model.ForumInfo;
import com.fangyuanyouyue.forum.service.ForumUploadService;
import com.google.gson.Gson;


@Service(value = "forumUploadService")
@Transactional(rollbackFor=Exception.class)
public class ForumUploadServiceImpl implements ForumUploadService {

	@Autowired
	private ForumInfoMapper forumInfoMapper;
	@Autowired
	private ForumColumnMapper forumColumnMapper;
	@Autowired
	private FileUploadServiceImpl fileUploadServiceImpl;
	
	@Override
	public String uploadForum(String filePath) throws ServiceException {

		int success = 0;
		int error = 0;
		
		try {
			File file = new File(filePath);
			File[] files = file.listFiles();

			//TODO 遍历每个分类
			for(File element:files) {
				System.out.println(element.getName());
				String cloumnType = element.getName().split("-")[0];
				String cloumnName = element.getName().split("-")[1];
				System.out.println("专栏分类:【"+cloumnType+"】");
				System.out.println("专栏名称:【"+cloumnName+"】");
				if(element.isDirectory()&&!element.getName().startsWith(".")) {
					//找出分类
					for(File forum:element.listFiles()) {
						if(!forum.isDirectory()) {
							continue;
						}
						//解析里面的帖子
						String title = forum.getName();
						System.out.println("帖子标题：【"+title+"】");
						File content = null;
						List<File> imgs = new ArrayList<File>();
						for(File details:forum.listFiles()) {
							String name = details.getName().toLowerCase();
							if(name.endsWith("txt")) {
								//解析内容
								content = details;
							}else if(name.endsWith("png")||name.endsWith("jpg")||name.endsWith("jpeg")) {
								//上传图片
								imgs.add(details);
							}
						}
						if(content==null) {
							System.out.println("内容不存在："+title);
						}else {
							if(importForum(cloumnName,title, content, imgs)) {
								success ++;
							}else {
								error ++;
							}
						}
					}

				}
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		

		return "导入成功："+success+"，导入失败："+error;
		
	}
	
	
	private boolean importForum(String cloumnName,String title,File content,List<File> imgs) {
		try {
			
			System.out.println("导入帖子，标题："+title);
			
			String[] titles = title.split("-");
			
			if(titles.length==2) {
				title = titles[1];
			}
			
			ForumColumn column = forumColumnMapper.selectByName(cloumnName);
			if(column==null) {
				System.err.println("找不到专栏");
				return false;
			}
			List<String> urls = new ArrayList<String>();
			List<ForumDetailDto> details = new ArrayList<ForumDetailDto>();
			//解析图片
			for(File img:imgs) {
				System.out.println("上传图片："+img.getName());
				String url = fileUploadServiceImpl.uploadPic(img);
				System.out.println("图片地址："+url);
				urls.add(url);
				ForumDetailDto detail = new ForumDetailDto();
				detail.setType(2);
				detail.setImgUrl(url);
				details.add(detail);
			}
			
			BufferedReader buff = new BufferedReader(new InputStreamReader(new FileInputStream(content),codeString(content.getAbsolutePath())));
			int i = 0;
			StringBuffer temp = new StringBuffer();
			String lineTxt = "";
	        while ((lineTxt = buff.readLine()) != null) {
	        	System.out.println(lineTxt);
                temp.append(lineTxt);
                temp.append("\n");
	        }
	        //如果后面还有文字
	        if(!temp.equals("")) {
	        	ForumDetailDto detail = new ForumDetailDto();
	    		detail.setType(1);
	    		detail.setContent(temp.toString());
	    		details.add(detail);
	        }

	     // 使用new方法
	        Gson gson = new Gson();
	     
	        String contents = gson.toJson(details);
	        System.out.println(contents);
			
			ForumInfo info = new ForumInfo();
			info.setContent(contents);
			info.setTitle(title);
			info.setAddTime(new Date());
			info.setColumnId(column.getId());
			info.setIsChosen(2);
			info.setSort(2);
			info.setStatus(1);
			info.setType(1);
			info.setUserId(column.getUserId());
			info.setPvCount((int)(Math.random() * 1000) + 100);
			
			System.err.println(info.toString());
			
			forumInfoMapper.insert(info);
			
			return true;
			
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	   public static String codeString(String fileName) throws Exception {
	        BufferedInputStream bin = new BufferedInputStream(new FileInputStream(fileName));
	        int p = (bin.read() << 8) + bin.read();
	        bin.close();
	        String code = null;
	 
	        switch (p) {
	        case 0xefbb:
	            code = "UTF-8";
	            break;
	        case 0xfffe:
	            code = "Unicode";
	            break;
	        case 0xfeff:
	            code = "UTF-16BE";
	            break;
	        default:
	            code = "GBK";
	        }
	 
	        return code;
	   }

	        
	
	public static void main(String[] args) throws Exception {
		
		System.out.println(codeString("/Users/wuzhimin/Desktop/unzip/haha/123.txt"));

		BufferedReader buff = new BufferedReader(new InputStreamReader(new FileInputStream("/Users/wuzhimin/Desktop/unzip/haha/123.txt"),"Unicode"));
		String lineTxt = null; // 读取文件的方法
		int i = 0;
        
        List<ForumDetailDto> details = new ArrayList<ForumDetailDto>();
    	boolean any = false;
    	String temp = "";
        while ((lineTxt = buff.readLine()) != null) {
        	System.out.println(lineTxt);
        	if(lineTxt.equals("图")) {
        		//前面的清除
            	ForumDetailDto detail = new ForumDetailDto();
        		detail.setType(1);
        		detail.setContent(temp);
        		details.add(detail);
        		temp = "";
        		
        		//加上图片
            	detail = new ForumDetailDto();
        		detail.setType(2);
        		detail.setImgUrl("img");
        		i ++;
            	details.add(detail);
            	
        	}else {
        		temp += lineTxt;
        	}
        }
        //如果后面还有文字
        if(!temp.equals("")) {
        	ForumDetailDto detail = new ForumDetailDto();
    		detail.setType(1);
    		detail.setContent(temp);
    		details.add(detail);
        }
        
        System.out.println(new Gson().toJson(details));
        
	}
}
