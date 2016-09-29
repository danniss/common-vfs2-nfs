package com.github.songsheng.vfs2.provider.nfs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;

public class Main {

	public static void main(String[] args) {
		try {
			FileSystemManager fileSystemManager = VFS.getManager();
			
			FileObject fileObject;
			String path = "";
	        fileObject = fileSystemManager.resolveFile("nfs://10.0.202.122//opt/glog/a.txt");
	        if (fileObject == null) {
	            throw new IOException("File cannot be resolved: " + path);
	        }
	        if (!fileObject.exists()) {
	            throw new IOException("File does not exist: " + path);
	        }
	        System.out.println(fileObject.getName().getPath());
	        BufferedReader stream = new BufferedReader(new InputStreamReader(fileObject.getContent().getInputStream(), "utf-8"));
        	String line = null;
        	while((line = stream.readLine()) != null) {
        		System.out.println(line);
        	}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
