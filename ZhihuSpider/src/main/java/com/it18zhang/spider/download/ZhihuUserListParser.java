package com.it18zhang.spider.download;

import com.it18zhang.spider.domain.PageInfo;
import com.it18zhang.spider.domain.ZhihuUserInfo;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户用户列表解析器
 */
public class ZhihuUserListParser   {

	public List<ZhihuUserInfo> parseListPage(PageInfo page) {
		List<ZhihuUserInfo> list = new ArrayList<ZhihuUserInfo>();
		String baseJsonPath = "$.data.length()";
		DocumentContext dc = JsonPath.parse(page.getHtml());
		Integer userCount = dc.read(baseJsonPath);
		for (int i = 0; i < userCount; i++) {
			ZhihuUserInfo zhihuUserInfo = new ZhihuUserInfo();
			String userBaseJsonPath = "$.data[" + i + "]";
			setUserInfoByJsonPth(zhihuUserInfo, "userToken", dc, userBaseJsonPath + ".url_token");//user_token
			setUserInfoByJsonPth(zhihuUserInfo, "username", dc, userBaseJsonPath + ".name");//username
			setUserInfoByJsonPth(zhihuUserInfo, "hashId", dc, userBaseJsonPath + ".id");//hashId
			setUserInfoByJsonPth(zhihuUserInfo, "followees", dc, userBaseJsonPath + ".following_count");//关注人数
			setUserInfoByJsonPth(zhihuUserInfo, "location", dc, userBaseJsonPath + ".locations[0].name");//位置
			setUserInfoByJsonPth(zhihuUserInfo, "business", dc, userBaseJsonPath + ".business.name");//行业
			setUserInfoByJsonPth(zhihuUserInfo, "employment", dc, userBaseJsonPath + ".employments[0].company.name");//公司
			setUserInfoByJsonPth(zhihuUserInfo, "position", dc, userBaseJsonPath + ".employments[0].job.name");//职位
			setUserInfoByJsonPth(zhihuUserInfo, "education", dc, userBaseJsonPath + ".educations[0].school.name");//学校
			setUserInfoByJsonPth(zhihuUserInfo, "answers", dc, userBaseJsonPath + ".answer_count");//回答数
			setUserInfoByJsonPth(zhihuUserInfo, "asks", dc, userBaseJsonPath + ".question_count");//提问数
			setUserInfoByJsonPth(zhihuUserInfo, "posts", dc, userBaseJsonPath + ".articles_count");//文章数
			setUserInfoByJsonPth(zhihuUserInfo, "followers", dc, userBaseJsonPath + ".follower_count");//粉丝数
			setUserInfoByJsonPth(zhihuUserInfo, "agrees", dc, userBaseJsonPath + ".voteup_count");//赞同数
			setUserInfoByJsonPth(zhihuUserInfo, "thanks", dc, userBaseJsonPath + ".thanked_count");//感谢数
			try {
				Integer gender = dc.read(userBaseJsonPath + ".gender");
				if (gender != null && gender == 1) {
					zhihuUserInfo.setSex("male");
				} else if (gender != null && gender == 0) {
					zhihuUserInfo.setSex("female");
				}
			} catch (PathNotFoundException e) {
				//没有该属性
			}
			list.add(zhihuUserInfo);
		}
		return list;
	}

	/**
	 * jsonPath获取值，并通过反射直接注入到user中
	 *
	 * @param zhihuUserInfo
	 * @param fieldName
	 * @param dc
	 * @param jsonPath
	 */
	private void setUserInfoByJsonPth(ZhihuUserInfo zhihuUserInfo, String fieldName, DocumentContext dc, String jsonPath) {
		try {
			Object o = dc.read(jsonPath);
			Field field = zhihuUserInfo.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(zhihuUserInfo, o);
		} catch (PathNotFoundException e1) {
			//no results
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
