//package com.zejor.devops.nginx.bean;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class NginxErrorPage {
//
//	/**
//	 * 页面状态码
//	 */
//	private List<String> status = new ArrayList<>();
//
//	/**
//	 * 跳转页面
//	 */
//	private String path;
//
//	public List<String> getStatus() {
//		return status;
//	}
//
//	public void setStatus(List<String> status) {
//		this.status = status;
//	}
//
//	public String getPath() {
//		return path;
//	}
//
//	public void setPath(String path) {
//		this.path = path;
//	}
//
//	@Override
//	public String toString() {
//		return "NginxErrorPage [status = " + status.toString() + ", path = " + path + "]";
//	}
//
//	public String getStatusStr(){
//		String str = "";
//		for (String s:
//			 status) {
//			str += s + ",";
//		}
//		return  str.substring(0, str.length() - 1);
//	}
//
//}
