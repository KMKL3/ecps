package com.rl.ecps.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.rl.ecps.util.ECPSUtils;
import com.rl.ecps.util.UploadResponse;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

@Controller
@RequestMapping("/upload")
public class EbUploadController {
	
	@RequestMapping("/uploadPic.do")
	public void uploadPic(HttpServletRequest request, PrintWriter out, String lastRealPath) throws IOException{
		//强制转换request
		MultipartHttpServletRequest mr = (MultipartHttpServletRequest) request;
		//从表单获得文件
		//获得文件类型input的name
		Iterator<String> iter = mr.getFileNames();
		String inputName = iter.next();
		//获得文件
		MultipartFile mf = mr.getFile(inputName);
		byte[] mfs = mf.getBytes();
		//定义上传后的文件名字
		String fileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		Random random = new Random();
		for(int i = 0; i < 3; i++){
			fileName = fileName + random.nextInt(10);
		}
		//获得后缀名
		String oriFileName = mf.getOriginalFilename();
		String suffix = oriFileName.substring(oriFileName.lastIndexOf("."));
		//要上传文件的绝对路径
		String realPath = ECPSUtils.readProp("upload_file_path")+"/upload/"+fileName + suffix;
		String relativePath = "/upload/"+fileName + suffix;
		//由于我们需要在不同主机上上传不能直接通过流的方式写文件
				//创建Jersey客户端
		Client client = Client.create();
		//判断是不是第一次上传
		if(StringUtils.isNotBlank(lastRealPath)){
			WebResource wr = client.resource(lastRealPath);
			wr.delete();
		}
		
		
		//指定要上传的具体的地址,参数就是要上传的图片的绝对路径
		WebResource wr = client.resource(realPath);
		//文件的上传到文件主机上
		wr.put(mfs);
		//使用json对象把绝对路径和相对路径写回去
		JSONObject jo = new JSONObject();
		jo.accumulate("realPath", realPath);
		jo.accumulate("relativePath", relativePath);
		//{"realPath":"http://...", "relativePath":"/upload/.."}
		String result = jo.toString();
		out.write(result);
	}
	@RequestMapping("/uploadForFck.do")
	public void uploadForFck(HttpServletRequest request, PrintWriter out) throws IOException{
		//强制转换request
		MultipartHttpServletRequest mr = (MultipartHttpServletRequest) request;
		//从表单获得文件
		//获得文件类型input的name
		Iterator<String> iter = mr.getFileNames();
		String inputName = iter.next();
		//获得文件
		MultipartFile mf = mr.getFile(inputName);
		byte[] mfs = mf.getBytes();
		//定义上传后的文件名字
		String fileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		Random random = new Random();
		for(int i = 0; i < 3; i++){
			fileName = fileName + random.nextInt(10);
		}
		//获得后缀名
		String oriFileName = mf.getOriginalFilename();
		String suffix = oriFileName.substring(oriFileName.lastIndexOf("."));
		//要上传文件的绝对路径
		String realPath = ECPSUtils.readProp("upload_file_path")+"/upload/"+fileName + suffix;
		String relativePath = "/upload/"+fileName + suffix;
		//由于我们需要在不同主机上上传不能直接通过流的方式写文件
		//创建Jersey客户端
		Client client = Client.create();
		
		//指定要上传的具体的地址,参数就是要上传的图片的绝对路径
		WebResource wr = client.resource(realPath);
		//文件的上传到文件主机上
		wr.put(mfs);
		//回调对象的创建
		UploadResponse ur = new UploadResponse(UploadResponse.EN_OK, realPath);
		out.print(ur);
		
	}
}
