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

			String name = ""; //帖子名
			List<String> content = new ArrayList<String>();
			
			while (entries.hasMoreElements()) {

				ZipEntry entry = (ZipEntry) entries.nextElement();

				// 如果是文件夹，就创建个文件夹

				if (entry.isDirectory()) {
					
					//1，处理前一个帖子
					if(name.length()>0) {
						System.out.println("名称：" + entry.getName());
						InputStreamReader is = new InputStreamReader(zipFile.getInputStream(entry)); 
		                BufferedReader br = new BufferedReader(is);
						String lineTxt = null; // 读取文件的方法
		                while ((lineTxt = br.readLine()) != null) {
		                	
		                }
						
					}
					

					name = entry.getName();

	               
					
					//2，开始新的帖子
					String dirPath = destDirPath + "/" + entry.getName();

					File dir = new File(dirPath);

					dir.mkdirs();
					

				} else {

					// 如果是文件，就先创建一个文件，然后用io流把内容copy过去

					File targetFile = new File(destDirPath + "/" + entry.getName());

					// 保证这个文件的父文件夹必须要存在

					if (!targetFile.getParentFile().exists()) {

						targetFile.getParentFile().mkdirs();

					}

					targetFile.createNewFile();

					// 将压缩文件内容写入到这个文件中

					InputStream is = zipFile.getInputStream(entry);

					FileOutputStream fos = new FileOutputStream(targetFile);

					int len;

					byte[] buf = new byte[1024];

					while ((len = is.read(buf)) != -1) {

						fos.write(buf, 0, len);

					}

					// 关流顺序，先打开的后关闭

					fos.close();

					is.close();

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
	
	class Forum {
		String name;
		String content;
	}
}
