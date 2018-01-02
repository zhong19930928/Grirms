package com.yunhu.yhshxc.http.download.apk;
 /**
  *创建一个下载信息的实体类
  */
 public class DownloadInfo {
     private int threadId;//下载器id
     private int startPos;//开始点
     private int endPos;//结束点
     private int compeleteSize;//完成度
     private String url;//下载器网络标识
     private String md5;//下载文件的MD5
     public int fileSize;//文件大小
     public static String NEEDDOWNTHREADID=null;//当前需要下载的线程的ID 
     public DownloadInfo(int threadId, int startPos, int endPos,
             int compeleteSize,String url,String md5) {
         this.threadId = threadId;
         this.startPos = startPos;
         this.endPos = endPos;
         this.compeleteSize = compeleteSize;
         this.url=url;
         this.md5=md5;
     }
     
     public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public DownloadInfo() {
     }
     public String getUrl() {
         return url;
     }
     public void setUrl(String url) {
         this.url = url;
     }
     public int getThreadId() {
         return threadId;
     }
     public void setThreadId(int threadId) {
         this.threadId = threadId;
     }
     public int getStartPos() {
         return startPos;
     }
     public void setStartPos(int startPos) {
         this.startPos = startPos;
     }
     public int getEndPos() {
         return endPos;
     }
     public void setEndPos(int endPos) {
         this.endPos = endPos;
     }
     public int getCompeleteSize() {
         return compeleteSize;
     }
     public void setCompeleteSize(int compeleteSize) {
         this.compeleteSize = compeleteSize;
     }
 
     @Override
     public String toString() {
         return "DownloadInfo [threadId=" + threadId
                 + ", startPos=" + startPos + ", endPos=" + endPos
                 + ", compeleteSize=" + compeleteSize +"]";
     }
 }