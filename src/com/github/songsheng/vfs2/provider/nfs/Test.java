package com.github.songsheng.vfs2.provider.nfs;

import java.io.File;
import java.io.IOException;

import com.sun.nfs.XFileExtensionAccessor;
import com.sun.xfile.XFile;
import com.sun.xfile.XFileInputStream;
import com.sun.xfile.XFileOutputStream;


public class Test {

	public static void main(String[] args) {
		final String ip = "10.0.202.122";
		final String user = "song";
		final String password = "password";
		final String dir = "/opt/glog/a.txt";
		try {

			String url = "nfs://" + ip + "/" + dir;

			XFile xf = new XFile(url);

			if (xf.exists())

			{

				System.out.println("URL is OK!");

			} else

			{

				System.out.println("URL is bad!");

				return;

			}
			/*XFileExtensionAccessor nfsx = (XFileExtensionAccessor) xf.getExtensionAccessor();
			if (!nfsx.loginPCNFSD(ip, user, password))

			{

				System.out.println("login failed!");
				return;

			}*/
			
			String[] fileList = xf.list();

			XFile temp = null;

			long startTime = System.currentTimeMillis();

			int filesz = 0;

			//for (String file : fileList)

			//{

				//temp = new XFile(url + "/" + file);
				String file = xf.getName();
				XFileInputStream in = new XFileInputStream(xf);

				XFileOutputStream out = new XFileOutputStream("H://temp" + File.separator + file);

				int c;

				byte[] buf = new byte[8196];

				while ((c = in.read(buf)) > 0) {

					filesz += c;

					out.write(buf, 0, c);

				}

				System.out.println(file + " is downloaded!");

				in.close();

				out.close();

				

			//}

			long endTime = System.currentTimeMillis();

			long timeDiff = endTime - startTime;

			int rate = (int) ((filesz / 1000) / (timeDiff / 1000.0));

			System.out.println(filesz + " bytes copied @ " + rate + "Kb/sec");

		} catch (IOException e) {

			System.out.println(e);

		}

	}
}
