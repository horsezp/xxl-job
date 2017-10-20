package com.xxl.job.admin.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xxl.job.admin.core.enums.ExecutorFailStrategyEnum;
import com.xxl.job.admin.core.model.XxlJobGroup;
import com.xxl.job.admin.core.model.XxlJobInfo;
import com.xxl.job.admin.core.route.ExecutorRouteStrategyEnum;
import com.xxl.job.admin.dao.XxlJobGroupDao;
import com.xxl.job.admin.service.XxlJobService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.enums.ExecutorBlockStrategyEnum;
import com.xxl.job.core.glue.GlueTypeEnum;

/**
 * index controller
 * 
 * @author xuxueli 2015-12-19 16:13:16
 */
@Controller
@RequestMapping("/jobinfo")
public class JobInfoController {

	@Resource
	private XxlJobGroupDao xxlJobGroupDao;
	@Resource
	private XxlJobService xxlJobService;

	@RequestMapping
	public String index(Model model, @RequestParam(required = false, defaultValue = "-1") int jobGroup) {

		// 枚举-字典
		model.addAttribute("ExecutorRouteStrategyEnum", ExecutorRouteStrategyEnum.values()); // 路由策略-列表
		model.addAttribute("GlueTypeEnum", GlueTypeEnum.values()); // Glue类型-字典
		model.addAttribute("ExecutorBlockStrategyEnum", ExecutorBlockStrategyEnum.values()); // 阻塞处理策略-字典
		model.addAttribute("ExecutorFailStrategyEnum", ExecutorFailStrategyEnum.values()); // 失败处理策略-字典

		// 任务组
		List<XxlJobGroup> jobGroupList = xxlJobGroupDao.findAll();
		model.addAttribute("JobGroupList", jobGroupList);
		model.addAttribute("jobGroup", jobGroup);

		return "jobinfo/jobinfo.index";
	}

	@RequestMapping("/pageList")
	@ResponseBody
	public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,
			@RequestParam(required = false, defaultValue = "10") int length, int jobGroup, String executorHandler,
			String filterTime) {

		return xxlJobService.pageList(start, length, jobGroup, executorHandler, filterTime);
	}

	@RequestMapping("/add")
	@ResponseBody
	public ReturnT<String> add(XxlJobInfo jobInfo) {
		return xxlJobService.add(jobInfo);
	}

	@RequestMapping("/reschedule")
	@ResponseBody
	public ReturnT<String> reschedule(XxlJobInfo jobInfo) {
		return xxlJobService.reschedule(jobInfo);
	}

	@RequestMapping("/remove")
	@ResponseBody
	public ReturnT<String> remove(int id) {
		return xxlJobService.remove(id);
	}

	@RequestMapping("/pause")
	@ResponseBody
	public ReturnT<String> pause(int id) {
		return xxlJobService.pause(id);
	}

	@RequestMapping("/resume")
	@ResponseBody
	public ReturnT<String> resume(int id) {
		return xxlJobService.resume(id);
	}

	@RequestMapping("/trigger")
	@ResponseBody
	public ReturnT<String> triggerJob(int id) {
		return xxlJobService.triggerJob(id);
	}

	@RequestMapping("/trigger2")
	@ResponseBody
	public String triggerJob2(int id, String callback) throws Exception {
		ReturnT<String> t = xxlJobService.triggerJob(id);
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(t);
		String value = callback + "(" + json + ")";
		return value;
	}

	@RequestMapping("/triggerRemote")
	@ResponseBody
	//http://10.1.126.87:8080/xxl-job-admin/jobinfo/triggerRemote?id=8
	public ReturnT<String> triggerJob3(int id) throws Exception {
		ReturnT<String> t = xxlJobService.triggerJob(id);
		return t;
	}

}
