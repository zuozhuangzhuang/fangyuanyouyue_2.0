package com.fangyuanyouyue.forum.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

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
				String url = fileUploadServiceImpl.uploadPic(title, new FileInputStream(img));
				//String url = fileUploadServiceImpl.uploadPic(img);
				System.out.println("图片地址："+url);
				urls.add(url);
				ForumDetailDto detail = new ForumDetailDto();
				detail.setType(2);
				detail.setImgUrl(url);
				details.add(detail);
			}
			
			BufferedReader buff = new BufferedReader(new InputStreamReader(new FileInputStream(content), codeString(new FileInputStream(content))));
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
	
	public static String codeString(InputStream file) throws Exception {
		BufferedInputStream bin = new BufferedInputStream(file);
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


		/**
		 * 
		 * zip解压
		 * 
		 * @param srcFile
		 *            zip源文件
		 * 
		 * @param destDirPath
		 *            解压后的目标文件夹
		 * 
		 * @throws RuntimeException
		 *             解压失败会抛出运行时异常
		 * 
		 */
		@Override
		public String uploadZipFile(HttpServletRequest request,MultipartFile file) throws Exception {
			int success = 0;
			int error = 0;
			
			 //在根目录下创建一个tempfileDir的临时文件夹
	        String contextpath = request.getContextPath()+"/tempfileDir";
	        File f = new File(contextpath); 
	        if(!f.exists()){
	            f.mkdirs();
	        }

	        //在tempfileDir的临时文件夹下创建一个新的和上传文件名一样的文件
	        String filename = file.getOriginalFilename();
	        String filepath = contextpath+"/"+filename;
	        File srcFile = new File(filepath);

	        //将MultipartFile文件转换，即写入File新文件中，返回File文件
	        CommonsMultipartFile commonsmultipartfile = (CommonsMultipartFile) file;
	        commonsmultipartfile.transferTo(srcFile);
	        //File srcFile = commonsmultipartfile.transferTo(newFile);
	        System.out.println("转换之后的文件："+file);
//			
//			CommonsMultipartFile cf = (CommonsMultipartFile) file;
//			DiskFileItem fi = (DiskFileItem) cf.getFileItem();
	//
//			File srcFile = fi.getStoreLocation();

			long start = System.currentTimeMillis();
			// 开始解压
			ZipFile zipFile = null;
			try {
				zipFile = new ZipFile(srcFile);
				Enumeration<?> entries = zipFile.entries();
				ZipEntry content = null;
				List<ZipEntry> imgs = new ArrayList<ZipEntry>();
				String title = null;
				while (entries.hasMoreElements()) {

					ZipEntry entry = (ZipEntry) entries.nextElement();
					String name = entry.getName();
					// 如果是文件夹，就创建个文件夹
					if (entry.isDirectory()) {
						// 1，处理前一个帖子
						if (title != null) {
							boolean flag = importForum(title,zipFile, content, imgs);
							if(flag) {
								success ++;
							}else {
								error ++;
							}
						}
						title = entry.getName();
						imgs = new ArrayList<ZipEntry>();
					} else {
						if (name.contains(title)) {
							continue;
						}
						if (name.toLowerCase().endsWith(".txt")) {
							content = entry;
						} else if (name.endsWith("png") || name.endsWith("jpg") || name.endsWith("jpeg")) {
							imgs.add(entry);
						}
					}
				}
				long end = System.currentTimeMillis();
				System.out.println("解压完成，耗时：" + (end - start) + " ms");
			} catch (Exception e) {
				throw new RuntimeException("unzip error from ZipUtils", e);
			} finally {
				if (zipFile != null) {
					try {
						zipFile.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				// 判断源文件是否存在
				if (!srcFile.exists()) {
					throw new RuntimeException(srcFile.getPath() + "所指文件不存在");
				}
			}

			return "导入成功：" + success + "，导入失败：" + error;
		}

		private boolean importForum(String title, ZipFile zipFile, ZipEntry content, List<ZipEntry> imgs) {

			try {

				InputStreamReader is = new InputStreamReader(zipFile.getInputStream(content));
				
				String[] titles = title.split("\\-");

				if (titles.length != 2) {
					System.err.println("标题不对");
					return false;
				}

				ForumColumn column = forumColumnMapper.selectByName(titles[0]);
				if (column == null) {
					System.err.println("找不到专栏");
					return false;
				}
				List<String> urls = new ArrayList<String>();
				// 解析图片
				for (ZipEntry entry : imgs) {
					System.out.println("上传图片：" + entry.getName());
					String url = fileUploadServiceImpl.uploadPic(entry.getName(),zipFile.getInputStream(entry));
					System.out.println("图片地址：" + url);
					urls.add(url);
				}
				InputStream inputStream = zipFile.getInputStream(content);
				BufferedReader buff = new BufferedReader(new InputStreamReader(inputStream, codeString(inputStream)));
				// InputStreamReader is = new InputStreamReader(new
				// FileInputStream(content,"utf-8"));
				// BufferedReader br = new BufferedReader(buff);
				String lineTxt = null; // 读取文件的方法
				int i = 0;
				List<ForumDetailDto> details = new ArrayList<ForumDetailDto>();
				while ((lineTxt = buff.readLine()) != null) {
					System.out.println(lineTxt);
					ForumDetailDto detail = new ForumDetailDto();
					if (lineTxt.equals("图")) {
						detail.setType(2);
						if (i > urls.size() - 1) {
							System.err.println("图片数量有误");
							return false;
						}
						detail.setImgUrl(urls.get(i));
						i++;
					} else {
						detail.setType(1);
						detail.setContent(new String(lineTxt));
					}
					details.add(detail);
				}

				// 使用new方法
				Gson gson = new Gson();

				String contents = gson.toJson(details);
				System.out.println(contents);

				ForumInfo info = new ForumInfo();
				info.setContent(contents);
				info.setTitle(titles[1]);
				info.setAddTime(new Date());
				info.setColumnId(column.getId());
				info.setIsChosen(2);
				info.setSort(2);
				info.setStatus(1);
				info.setType(1);
				info.setUserId(column.getUserId());
				info.setPvCount(0);

				System.err.println(info.toString());

				forumInfoMapper.insert(info);

				return true;
				
				
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}

	
	public static void main(String[] args) throws Exception {
		
		//System.out.println(codeString("/Users/wuzhimin/Desktop/unzip/haha/123.txt"));

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
