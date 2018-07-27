package com.fangyuanyouyue.forum.utils;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;


/**
 * 图片处理
 * 
 */
public class FileUtil {
/*
 * import:
 * <br>spring-web-4.1.0.RELEASE.jar
 * <br>CCP_REST_SDK_JAVA_v2.6.3r.jar
 */
	protected Logger log=Logger.getLogger(this.getClass());
	//	public static void main(String[] args) throws Exception {
	//		FileUtil f = new FileUtil();
	//		f.saveImg(new File("C:\\aaa.jpg"), "C:\\", "today.jpeg");
	//	}


	public FileUtil() {
	}

	/**
	 * 保存小图片,固定宽高为240像素
	 * 
	 * @param file 文件流
	 * @param filePath 文件存储路径
	 * @param fileName 文件名
	 * @throws Exception
	 */
	public void saveSmallImg(MultipartFile file, String filePath, String fileName) throws Exception {
		Image img = ImageIO.read(file.getInputStream());// 构造Image对象
		int width = img.getWidth(null);// 得到源图宽
		int height = img.getHeight(null);// 得到源图长

		int w = 240,h=240;//固定宽高
		if ((width / height) > (w / h)) {
			h = (int) (height * w / width); 
		} else {
			w = (int) (width * h / height);
		}

		File newFile =new File(filePath);
		
		if(!newFile.exists()){//如果文件夹不存在则创建
			newFile.mkdirs();
		}

		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB );
		image.getGraphics().drawImage(img, 0, 0, w, h, null); // 绘制缩小后的图
		//因为之前改变了w或者h的值，不管输入的是多大的图像，输出图像的画布大小为w*h，即等比例于width*height

		File destFile = new File(filePath+fileName); //创建缩小后的图像文件
		FileOutputStream out = new FileOutputStream(destFile); // 输出到文件流
		out.flush();
		// 可以正常实现bmp、png、gif转jpg
		ImageIO.write(image, "jpeg", out);
		out.close();
	}
	/**
	 * 
	 * 保存高精度小图片,固定宽高为200像素
	 * 参数为MultipartFile类型的二进制文件流
	 * 
	 * @param file  文件流
	 * @param filePath  文件存储路径
	 * @param fileName  文件名
	 * @throws Exception
	 */
	public static void saveSmallImgBetter(MultipartFile file, String filePath, String fileName) throws Exception {
		Image img = ImageIO.read(file.getInputStream());      // 构造Image对象
		int width = img.getWidth(null);    // 得到源图宽
		int height = img.getHeight(null);  // 得到源图长

		int w = 0,h=0;//缩略图宽高
		double comBase = 200;//图像某一边长最大像素
		double scale = 1.0;
		//压缩限制(宽/高)比例  一般用1;当scale>=1,缩略图height=comBase,width按原图宽高比例;若scale<1,缩略图width=comBase,height按原图宽高比例
		double srcScale = (double) height / width;
		/**缩略图宽高算法*/
		if ((double) height > comBase || (double) width > comBase) {
			if (srcScale >= scale || 1 / srcScale > scale) {
				if (srcScale >= scale) {
					h = (int) comBase;
					w = width * h / height;
				} else {
					w = (int) comBase;
					h = height * w / width;
				}
			} else {
				if ((double) height > comBase) {
					h = (int) comBase;
					w = width * h / height;
				} else {
					w = (int) comBase;
					h = height * w / width;
				}
			}
		} else {
			h = height;
			w = width;
		}

		File newFile =new File(filePath);
		//如果文件夹不存在则创建  
		if  (!newFile.exists()){     
			newFile.mkdirs();  
		}

		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR );
		image.getGraphics().drawImage(img, 0, 0, w, h, null); // 绘制缩小后的图
		//因为之前改变了w或者h的值，不管输入的是多大的图像，输出图像的画布大小为w*h，即等比例于width*height
		FileOutputStream out = new FileOutputStream(filePath+ File.separator+fileName); // 创建缩小后的图像文件,输出到文件流
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		encoder.encode(image); //近JPEG编码
		out.close();
	}
	/**
	 * 
	 * 保存小图片,宽高可变
	 * 
	 * @param file 文件流
	 * @param filePath 文件存储路径
	 * @param fileName 文件名
	 * @param w 宽度
	 * @param h 高度
	 * @throws Exception
	 */
	public void saveSmallImg(MultipartFile file, String filePath, String fileName, Integer w, Integer h) throws Exception {
		Image img = ImageIO.read(file.getInputStream());// 构造Image对象
		int width = img.getWidth(null);// 得到源图宽
		int height = img.getHeight(null);// 得到源图长
		
		//宽高可变
		if ((width / height) > (w / h)) {
			h = (int) (height * w / width); 
		} else {
			w = (int) (width * h / height);
		}
		
		File newFile =new File(filePath);
		//如果文件夹不存在则创建
		if(!newFile.exists()){ 
			newFile.mkdirs();
		}
		
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB );
		image.getGraphics().drawImage(img, 0, 0, w, h, null); // 绘制缩小后的图
		
		File destFile = new File(filePath+fileName); //创建缩小后的图像文件
		FileOutputStream out = new FileOutputStream(destFile); // 输出到文件流
		out.flush();
		// 可以正常实现bmp、png、gif转jpg
		//	JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		ImageIO.write(image, "jpeg", out);
		//	encoder.encode(image); // JPEG编码
		out.close();
	}
	
	/**
	 * 
	 * 保存源图片，不改变宽高
	 * 
	 * @param file
	 * @param filePath
	 * @param fileName
	 * @throws Exception
	 */
	public void saveImg(MultipartFile file, String filePath, String fileName) throws Exception {
		Image img = ImageIO.read(file.getInputStream());// 构造Image对象
		int width = img.getWidth(null);// 得到源图宽
		int height = img.getHeight(null);// 得到源图长
		
		File newFile =new File(filePath);
		//如果文件夹不存在则创建
		if(!newFile.exists()){ 
			newFile.mkdirs();
		}
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB );
		image.getGraphics().drawImage(img, 0, 0, width, height, null); // 绘制缩小后的图
		
		File destFile = new File(filePath+fileName); //创建图像文件
		FileOutputStream out = new FileOutputStream(destFile); // 输出到文件流
		out.flush();
		// 可以正常实现bmp、png、gif转jpg
		//	JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		ImageIO.write(image, "jpg", out);
		//	encoder.encode(image); // JPEG编码
		out.close();
	}
	
	/**
	 * 
	 * 保存源图片，不改变宽高
	 * 
	 * @param is
	 * @param filePath
	 * @param fileName
	 * @throws Exception
	 */
	public void saveImgFromStream(InputStream is, String filePath, String fileName) throws Exception {
		Image img = ImageIO.read(is);      // 构造Image对象
		int width = img.getWidth(null);    // 得到源图宽
		int height = img.getHeight(null);  // 得到源图长
		
		File newFile =new File(filePath);
		//如果文件夹不存在则创建  
		if  (!newFile.exists()){     
			newFile.mkdirs();  
		}
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB );
		image.getGraphics().drawImage(img, 0, 0, width, height, null); // 绘制缩小后的图
		
		File destFile = new File(filePath+fileName); //创建图像文件
		FileOutputStream out = new FileOutputStream(destFile); // 输出到文件流
		out.flush();
		// 可以正常实现bmp、png、gif转jpg
		//	JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		ImageIO.write(image, "jpg", out);
		//	encoder.encode(image); // JPEG编码
		out.close();
	}
	

	/**
	 * 
	 * 保存图片 File类型二进制文件流
	 * 
	 * @param file 文件流
	 * @param filePath 文件存储路径
	 * @param fileName 文件名
	 * @throws Exception
	 */
	public void saveImg(File file, String filePath, String fileName) throws Exception {
		Image img = ImageIO.read(file);// 构造Image对象
		int width = img.getWidth(null);// 得到源图宽
		int height = img.getHeight(null);// 得到源图长

		int w = width,h=height;
		if(width>440){
			w=440;
			h = (int) (height * w / width);
		}

		File newFile =new File(filePath);
		//如果文件夹不存在则创建
		if(!newFile.exists()){ 
			newFile.mkdirs();
		}

		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB );
		image.getGraphics().drawImage(img, 0, 0, w, h, null); // 绘制缩小后的图

		File destFile = new File(filePath+fileName);
		FileOutputStream out = new FileOutputStream(destFile); // 输出到文件流
		out.flush();
		// 可以正常实现bmp、png、gif转jpg
		ImageIO.write(image, "jpeg", out);
		//encoder.encode(image); // JPEG编码
		out.close();

	}


	/**
	 * 
	 * 保存文件 File类型二进制文件流
	 * 
	 * @param file 文件流
	 * @param filePath 文件存储路径
	 * @param fileName 文件名
	 * @throws Exception
	 */
	public void saveFile(File file, String filePath, String fileName) throws Exception {
		FileInputStream fis = new FileInputStream(file);
		byte[] buffer = new byte[10240];
		int len = 0;
		log.info("文件大小为:" + fis.available());

		File newFile =new File(filePath);
		//如果文件夹不存在则创建
		if(!newFile.exists()){ 
			log.info("目录不存在,创建目录："+filePath);
			newFile.mkdirs();
		}

		FileOutputStream out = new FileOutputStream(new File(filePath+ File.separator+fileName), true);
		FileChannel fcout = out.getChannel();
		FileLock flout = null;
		while (true) {
			flout = fcout.tryLock();
			if (flout != null) {
				break;
			}
			log.info("有其他线程正在操作该文件，当前线程休眠1000毫秒");
			Thread.sleep(100L);
		}

		while ((len = fis.read(buffer)) > 0) {
			out.write(buffer, 0, len);
			out.flush();
		}
		fis.close();
		out.close();
	}
	
	
	/**
	 * 保存文件 IO流
	 * @param is 文件流
	 * @param filePath 文件存储路径
	 * @param fileName 文件名
	 * @throws Exception
	 */
	public void saveFile(InputStream is, String filePath, String fileName) throws Exception {
		byte[] buffer = new byte[10240];
		int len = 0;
		log.info("文件大小为:" + is.available());
		
		File newFile =new File(filePath);
		//如果文件夹不存在则创建
		if(!newFile.exists()){ 
			log.info("目录不存在,创建目录："+filePath);
			newFile.mkdirs();
		}
		
		FileOutputStream out = new FileOutputStream(new File(filePath+ File.separator+fileName), true);
		FileChannel fcout = out.getChannel();
		FileLock flout = null;
		while (true) {
			flout = fcout.tryLock();
			if (flout != null) {
				break;
			}
			log.info("有其他线程正在操作该文件，当前线程休眠1000毫秒");
			Thread.sleep(100L);
		}
		
		while ((len = is.read(buffer)) > 0) {
			out.write(buffer, 0, len);
			out.flush();
		}
		is.close();
		out.close();
	}


	/**
	 * 
	 * 保存文件，MultipartFile类型二进制文件流
	 * @param file 文件流
	 * @param filePath 文件存储路径
	 * @param fileName 文件名
	 * @throws Exception
	 */
	public void saveFile(MultipartFile file, String filePath, String fileName) throws Exception {
		InputStream fis = file.getInputStream();
		byte[] buffer = new byte[10240];
		int len = 0;
		log.info("文件大小为:" + fis.available());

		File newFile =new File(filePath);
		//如果文件夹不存在则创建
		if(!newFile.exists())
		{ 
			log.info("目录不存在,创建目录："+filePath);
			newFile.mkdirs();
		}

		FileOutputStream out = new FileOutputStream(new File(filePath+ File.separator+fileName), true);
		FileChannel fcout = out.getChannel();
		FileLock flout = null;
		while (true) {
			flout = fcout.tryLock();
			if (flout != null) {
				break;
			}
			log.info("有其他线程正在操作该文件，当前线程休眠1000毫秒");
			Thread.sleep(100L);
		}

		while ((len = fis.read(buffer)) > 0) {
			out.write(buffer, 0, len);
			out.flush();
		}
		fis.close();
		out.close();
	}


	/**
	 * 
	 * 获得文件名
	 * @param file
	 * @param prefix 前缀
	 * @return
	 */
	public String getFileName(MultipartFile file, String prefix) {
		String fileName = file.getOriginalFilename(); //获得文件名+后缀，即上传时的全称
		int dotIndex = fileName.lastIndexOf("."); //截取的关键字
		String suffix = fileName.substring(dotIndex); //获得文件的后缀名
		fileName = prefix+"_"+DateUtil.getCurrentDate("yyMMddHHmmssSSS")+suffix;
		return fileName;
	}

	public Boolean isFileExist(MultipartFile file){
		return !file.isEmpty();
	}

	/**
	 * 
	 * 获得存放2个以上的文件的文件夹的路径
	 * 
	 * @param sourcePath 待查找的文件夹
	 * @return
	 */
	public String findFilePath(String sourcePath){
		Integer fileCount = 0;
		File fileFolder = new File(sourcePath);
		File[] listFiles = fileFolder.listFiles();
		String filePath = null;
		
		for (File file : listFiles) {
			if(file.isDirectory()){
				filePath = findFilePath(file.getAbsolutePath());
			}else{
				fileCount += 1;
			}
		}
		if(fileCount > 2){
			return sourcePath;
		}else{
			return filePath;
		}
	}
	
	/**
	 * 
	 * 转移单个文件到目标文件夹
	 * 
	 * @param sourcePath 源文件夹
	 * @param fileName 文件名
	 * @param targetPath 目标文件夹
	 * @throws IOException
	 */
	public void copyOneFile(String sourcePath, String fileName , String targetPath) throws IOException {
		File oldFile = new File(sourcePath+ File.separator+fileName);
		if(oldFile.exists()){//文件存在
			InputStream ips = new FileInputStream(oldFile);
			File targetFolder = new File(targetPath);
			if(!targetFolder.exists()){
				targetFolder.mkdirs();
			}
			File newFile = new File(targetPath+ File.separator+fileName);
			OutputStream ops = new FileOutputStream(newFile);
			BufferedInputStream bis = new BufferedInputStream(ips);
			BufferedOutputStream bos = new BufferedOutputStream(ops);
			int len;
			byte[] b = new byte[1024];
			while((len = bis.read(b)) != -1){
				bos.write(b, 0, len);
			}
			bos.close();
			bis.close();
//			System.out.println("移动文件:"+fileName+"到:"+targetPath);
		}
	}
	
	/**
	 * 
	 * 移动所有文件到目标文件夹
	 * 
	 * @param sourcePath 源文件夹
	 * @param targetPath 目标文件夹
	 * @throws IOException
	 */
	public void copyAllFile(String sourcePath, String targetPath) throws IOException {
		File fileFolder = new File(sourcePath);
		if(!fileFolder.exists()){
			return;
		}
		File[] listFiles = fileFolder.listFiles();
		
		for (File file : listFiles) {
			if(file.isFile()){
				copyOneFile(sourcePath, file.getName(), targetPath);
			}
		}
	}
	
	/**
	 * 
	 * 移动所有文件和文件夹到目标文件
	 * 
	 * @param sourcePath 源文件夹
	 * @param targetPath 目标文件夹
	 * @throws IOException
	 */
	public void moveAllFileAndFolder(String sourcePath, String targetPath) throws IOException {
		File fileFolder = new File(sourcePath);
		if(!fileFolder.exists()){
			return;
		}
		File targetFolder = new File(targetPath);
		if(!targetFolder.exists()){
			targetFolder.mkdirs();
		}
//		endPackagePath = targetPath;
		File[] listFiles = fileFolder.listFiles();
		
		for (File file : listFiles) {
			if(file.isDirectory()){
				moveAllFileAndFolder(file.getAbsolutePath(), targetPath+ File.separator+file.getName());
			}else if(file.isFile()){
				copyOneFile(sourcePath, file.getName(), targetPath);
			}
		}
	}
	
	/**
	 * 
	 * 创建bat文件，填入待执行命令
	 * 
	 * @param filePath bat文件存放路径
	 * @param detail bat文件执行的内容
	 */
	public void createBatFile(String filePath, String detail){
		File bat = new File(filePath+ File.separator+"execute.bat");
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(bat));
			bw.write(detail);
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bw != null){
				try {
					bw.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				bw = null;
			}
		}
	}
	
	/**
	 * 
	 * 创建bat文件，待执行命令自动填入
	 * 
	 * @param filePath bat文件存放路径
	 * @param executePath 执行命令的文件路径
	 * @param command
	 */
	public void createBatFileWritePath(String filePath, String executePath, String command){
		File bat = new File(filePath+ File.separator+"execute.bat");
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(bat));
			String executeCommand = "cd /d "+executePath+" && "+ command;
			bw.write(executeCommand);
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bw != null){
				try {
					bw.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				bw = null;
			}
		}
	}


}