package com.zejor.devops.utils;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.core.ParseException;
import freemarker.template.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

public class FreeMarkerTemplateUtils {

	private static Configuration cfg;

	private static ClassTemplateLoader ctl;

	public static Configuration getConfiguration() {
		if (cfg == null) {
			// 创建一个FreeMarker实例
			cfg = new Configuration(Configuration.VERSION_2_3_23);
			// 指定FreeMarker模板文件的位置
		}
		return cfg;
	}

	public static Template getTemplate(String classpath, String template, Class<?> clazz) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException {
		ctl = new ClassTemplateLoader(clazz, classpath);
		// 直接运行，不打包情况
		getConfiguration().setTemplateLoader(ctl);
		return getConfiguration().getTemplate(template);
	}

	public static String processTemplateToString(Template template, Map<String, String> data) throws TemplateException, IOException {
		Writer writer = new StringWriter();
		template.process(data, writer);
		return writer.toString();
	}

	public static String processTemplateToString(String src, Map<String, String> data) throws TemplateException, IOException {
		Configuration cfg = getConfiguration();
		StringTemplateLoader stringLoader = new StringTemplateLoader();
		stringLoader.putTemplate("temp", src);
		cfg.setTemplateLoader(stringLoader);
		Template template = cfg.getTemplate("temp", "utf-8");
		Writer writer = new StringWriter();
		template.process(data, writer);
		return writer.toString();
	}

}
