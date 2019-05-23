package com.glory.blink.server.utils.flink;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

public class JarUtils {

	public static void main(String[] args) {
		// compress(CompressType.ZIP);
		// unZip("压缩.zip", "压缩后");
		// unZip("压缩.jar", "压缩后jar");
		jar("D:\\test\\target\\classes", "D:\\test\\target\\ss.jar");

	}

	public static void jar(String inputFile, String outputFile) {
		jar(new File(inputFile), new File(outputFile));
	}

	public static void jar(File inputFile, File outputFile) {
		JarOutputStream jos = null;
		try {

			jos = new JarOutputStream(new FileOutputStream(outputFile));

			// 设置压缩包注释
			jos.setComment("From Log");
			jarFile(jos, inputFile, null);
			System.err.println("压缩完成!");
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("压缩失败!");
		} finally {
			if (jos != null) {
				try {
					jos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 如果是单个文件，那么就直接进行压缩。如果是文件夹，那么递归压缩所有文件夹里的文件
	 * 
	 * @param jos
	 *            压缩输出流
	 * @param inputFile
	 *            需要压缩的文件
	 * @param path
	 *            需要压缩的文件在压缩包里的路径
	 */
	public static void jarFile(JarOutputStream jos, File inputFile, String path) {
		if (inputFile.isDirectory()) {
			// 记录压缩包中文件的全路径
			String p = null;
			File[] fileList = inputFile.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				File file = fileList[i];
				// 如果路径为空，说明是根目录
				if (path == null || path.isEmpty()) {
					p = file.getName();
				} else {
					p = path + "/" + file.getName();
				}

				// 如果是目录递归调用，直到遇到文件为止
				jarFile(jos, file, p);
			}
		} else {
			jarSingleFile(jos, inputFile, path);
		}
	}

	/**
	 * 压缩单个文件到指定压缩流里
	 * 
	 * @param jos
	 *            压缩输出流
	 * @param inputFile
	 *            需要压缩的文件
	 * @param path
	 *            需要压缩的文件在压缩包里的路径
	 * @throws FileNotFoundException
	 */
	public static void jarSingleFile(JarOutputStream jos, File inputFile, String path) {
		try {
			InputStream in = new FileInputStream(inputFile);
			jos.putNextEntry(new JarEntry(path));
			write(in, jos);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从输入流写入到输出流的方便方法 【注意】这个函数只会关闭输入流，且读写完成后会调用输出流的flush()函数，但不会关闭输出流！
	 * 
	 * @param input
	 * @param output
	 */
	public static void write(InputStream input, OutputStream output) {
		int len = -1;
		byte[] buff = new byte[1024];
		try {
			while ((len = input.read(buff)) != -1) {
				output.write(buff, 0, len);
			}
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}