package com.zhang.mymusic.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.Environment;

public class FileUtils {
	private String SDPATH;

	private int FILESIZE = 4 * 1024;

	public String getSDPATH() {
		return SDPATH;
	}

	public FileUtils() {
		// �õ���ǰ�ⲿ�洢�豸��Ŀ¼( /SDCARD )
		SDPATH = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ File.separator;
	}

	/**
	 * ��SD���ϴ����ļ�
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public File createSDFile(String fileName,String dir) throws IOException {
		File file = new File(SDPATH +dir +File.separator+ fileName);
		file.createNewFile();
		return file;
	}

	/**
	 * ��SD���ϴ���Ŀ¼
	 * 
	 * @param dirName
	 * @return
	 */
	public File createSDDir(String dirName) {
		File dir = new File(SDPATH + dirName+File.separator);
		dir.mkdirs();
		return dir;
	}

	/**
	 * �ж�SD���ϵ��ļ����Ƿ����
	 * 
	 * @param fileName
	 * @return
	 */
	public boolean isFileExist(String fileName,String path) {
		File file = new File(SDPATH + path + File.separator + fileName);
		return file.exists();
	}

	/**
	 * ��һ��InputStream���������д�뵽SD����
	 * 
	 * @param path
	 * @param fileName
	 * @param input
	 * @return
	 */
	public File write2SDFromInput(String path, String fileName,
			InputStream input) {
		File file = null;
		OutputStream output = null;
		try {
			createSDDir(path);
			file = createSDFile(fileName,path);
			output = new FileOutputStream(file);
			byte[] buffer = new byte[FILESIZE];

			/*
			 * ������ԣ���ο��������⣬��������������ṩ�� while((input.read(buffer)) != -1){
			 * output.write(buffer); }
			 */

			/* �����ṩ begin */
			int length;
			while ((length = (input.read(buffer))) > 0) {
				output.write(buffer, 0, length);
			}
			/* �����ṩ end */

			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

}