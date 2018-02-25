# common-vfs2-nfs
nfs provider for java common vfs2

[![License](http://img.shields.io/badge/License-MIT-blue.svg?style=flat)](http://opensource.org/licenses/mit)


Project "common-vfs2-nfs" is a NFS provider for [Commons VFS](http://commons.apache.org/proper/commons-vfs/).


## Requirements

Project "commons-vfs2-nfs" requires:
* [yanfs library](https://github.com/raisercostin/yanfs)
* Java 6


## Building

    mvn clean package dependency:tree


## Example

```java
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

		String path = "nfs://10.0.202.122//opt/glog/a.txt";
	        FileObject fileObject = fileSystemManager.resolveFile();
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
		e.printStackTrace();
	    }

	}

}
```



## License

```
Copyright 2016 Song Sheng

Licensed under the MIT License,(the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://opensource.org/licenses/MIT

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
