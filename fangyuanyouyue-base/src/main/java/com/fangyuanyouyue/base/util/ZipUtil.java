package com.fangyuanyouyue.base.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipUtil {
	
	public static void main(String[] args) {
		unZip(new File("/Users/wuzhimin/Desktop/unzip/test.zip"),"/Users/wuzhimin/Desktop/unzip");
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

	public static void unZip(File srcFile, String destDirPath) throws RuntimeException {

		long start = System.currentTimeMillis();
		// 判断源文件是否存在
		if (!srcFile.exists()) {
			throw new RuntimeException(srcFile.getPath() + "所指文件不存在");
		}
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
					//1，处理前一个帖子
					if(title!=null) {
						importForum(zipFile, content, imgs);
					}
					title = entry.getName();
				} else {
					if(name.contains(title)) {
						continue;
					}
					if(name.toLowerCase().endsWith(".txt")) {
						content = entry;
					}else if(name.endsWith("png")||name.endsWith("jpg")||name.endsWith("jpeg")) {
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
		}
	}
	
	
	private static boolean importForum(ZipFile zipFile,ZipEntry content,List<ZipEntry> imgs) {
		
		//InputStreamReader is = new InputStreamReader(zipFile.getInputStream()); 
		
		return false;
		
	}
	
}
