package com.info.back.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.info.back.dao.IBackUserDao;
import com.info.constant.Constant;
import com.info.web.pojo.cspojo.*;
import com.info.web.pojo.cspojo.BackUser;
import org.apache.commons.lang3.StringUtils;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.info.back.dao.IBackUserCompanyPermissionsDao;
import com.info.back.dao.IBackUserRoleDao;
import com.info.back.dao.IPaginationDao;
import com.info.web.util.PageConfig;

@Service
public class BackUserServiceImpl implements IBackUserService {
	@Resource
	private IBackUserDao backUserDao;
	@Resource
	private IBackUserRoleDao backUserRoleDao;
	@Resource
	private IPaginationDao paginationDao;
	@Resource
	private IBackUserCompanyPermissionsDao backUserCompanyPermissionsDao;
	@Resource
	private IBackRoleService backRoleService;

	@Override
	public List<BackUser> findAll(HashMap<String, Object> params) {
		return backUserDao.findAll(params);
	}

	@Override
	public BackUser findOneUser(HashMap<String, Object> params) {
		List<BackUser> list = this.findAll(params);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public void insert(BackUser backUser) {
		if (backUser.getDataComapnyIDs() != null) {
			backUser.setViewdataStatus(1);
		} else {
			backUser.setViewdataStatus(0);
		}
		backUserDao.insert(backUser);
		if (backUser.getDataComapnyIDs() != null) {
			HashMap<String, Object> params = new HashMap<>();
			params.put("id", backUser.getId());
			params.put("companyIds", backUser.getDataComapnyIDs());
			backUserCompanyPermissionsDao.inserUserCompanyList(params);
		}
	}

	@Override
	public void updateById(BackUser backUser) {
		backUserCompanyPermissionsDao.deleteUserCompanyByUserId(backUser
				.getId());
		if (backUser.getDataComapnyIDs() != null) {
			HashMap<String, Object> params = new HashMap<>();
			params.put("id", backUser.getId());
			params.put("companyIds", backUser.getDataComapnyIDs());
			backUserCompanyPermissionsDao.inserUserCompanyList(params);
			backUser.setViewdataStatus(1);
		} else {
			backUser.setViewdataStatus(0);
		}
		backUserDao.updateById(backUser);
	}

	@Override
	public void deleteById(Integer id) {
		backUserDao.deleteById(id);
	}

	@Override
	public PageConfig<BackUser> findPage(HashMap<String, Object> params) {
		params.put(Constant.NAME_SPACE, "BackUser");
		PageConfig<BackUser> pageConfig = new PageConfig<BackUser>();
		pageConfig = paginationDao.findPage("findAll", "findAllCount", params,
				null);
		return pageConfig;
	}

	@Override
	public void addUserRole(HashMap<String, Object> params) {
		backUserRoleDao.deleteUserRoleByUserId(Integer.valueOf(String
				.valueOf(params.get("id"))));
		String[] roleIds = (String[]) params.get("roleIds");
		if (null != roleIds && roleIds.length > 0) {
			backUserRoleDao.inserUserRoleList(params);
			String roleStrs = "";
			for (int i = 0; i < roleIds.length; i++) {
				if (i == roleIds.length - 1) {
					roleStrs += roleIds[i];
				} else {
					roleStrs += roleIds[i] + ",";
				}
			}
			HashMap<String, Object> roleMap = new HashMap<>();
			roleMap.put("id", Integer.valueOf(String.valueOf(params.get("id"))));
			roleMap.put("roleIds", roleStrs);
			backUserDao.updateRoleId(roleMap);
		}
	}

	@Override
	public void updatePwdById(BackUser backUser) {
		backUserDao.updatePwdById(backUser);
	}

	@Override
	public List<BackUserRole> findUserRoleByUserId(String userId) {
		return backUserDao.findUserRoleByUserId(userId);
	}

	@Override
    public String findUserRoleIdByUserId(String userId) {
		String result = "";

		if (StringUtils.isBlank(userId)) {
			return result;
		}

		List<BackUserRole> list = backUserDao.findUserRoleByUserId(userId);
		if (null == list || list.isEmpty()) {
			return result;
		}
		for (BackUserRole userRole : list) {
			result = result + userRole.getRoleId() + ",";
		}

		return result.substring(0, result.length() - 1);
	}

	@Override
	public List<BackUserCompanyPermissions> findCompanyPermissions(
			Integer userId) {
		return backUserCompanyPermissionsDao.findCompanyPermissions(userId);
	}

	@Override
	public Integer getUserPhoneCount(HashMap<String, Object> params) {
		return backUserDao.getUserPhoneCount(params);
	}

	@Override
	public List<BackUser> findUserByDispatch(BackUser backUser) {
		return backUserDao.findUserByDispatch(backUser);
	}

	@Override
	public BackUser getBackUserByUuid(String uuid) {
		// TODO Auto-generated method stub
		return backUserDao.getBackUserByUuid(uuid);
	}

	@Override
	public List<BackUserCompanyPermissions> findSelfCompanyPermissions(
			Integer userId) {
		return backUserCompanyPermissionsDao.findSelfCompanyPermissions(userId);
	}

	// 每月1号产生的M3+订单派单时派给每个有该对应催收组的公司的一个人手上(由该人转派给公司其他催收员 定时执行)
	@Override
    public void disableCollections() {
		List<BackUser> users = new ArrayList<BackUser>();
		String companyId = null;
		HashMap<String, Object> companyPartmer = new HashMap<>();
		companyPartmer.put("status", "1");
		companyPartmer.put("groupLevel", "7");
		try {
			users = backUserDao.getM3Companys(companyPartmer);
			HashMap<String, Object> map = new HashMap<>();
			if (users != null) {
				for (BackUser user : users) {
					companyId = user.getCompanyId();
					// 查询出每个公司添加时间最早的催收员uuid(启用中的)
					map.put("companyId", companyId);
					map.put("status", "1");
					map.put("groupLevel", "7");
					BackUser earliestUser = backUserDao
							.getEarliestCollection(map);
					if (earliestUser != null) {
						// 查询其他正常的(需要被禁用的催收员)
						HashMap<String, Object> otherUsersMap = new HashMap<>();
						otherUsersMap.put("userId", earliestUser.getUuid());
						otherUsersMap.put("status", "1");
						otherUsersMap.put("groupLevel", "7");
						otherUsersMap.put("companyId", companyId);
						List<BackUser> otherUsers = backUserDao
								.getOtherCollections(otherUsersMap);
						if (otherUsers != null) {
							List<String> uuids = new ArrayList<>();
							for (BackUser backUser : otherUsers) {
								uuids.add(backUser.getUuid());
							}
							map.put("uuids", uuids);
							map.put("status", "0");
							// 启用催收员
							backUserDao.disableOrEnableCollections(map);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 启用催收员
	@Override
    public void enableCollections() {
		// 查询出当前含有M3+组的公司里M3+组及以上禁用的催收员
		List<BackUser> users = new ArrayList<BackUser>();
		HashMap<String, Object> companyPartmer = new HashMap<>();
		companyPartmer.put("status", "0");
		companyPartmer.put("groupLevel", "7");
		try {
			users = backUserDao.getM3Companys(companyPartmer);
			HashMap<String, Object> otherUsersParmater = new HashMap<>();
			List<String> companyIds = new ArrayList<>(); // 存放公司id
			for (BackUser user : users) {
				companyIds.add(user.getCompanyId());
			}
			otherUsersParmater.put("companyIds", companyIds);
			otherUsersParmater.put("status", "0");
			otherUsersParmater.put("groupLevel", "7");
			List<BackUser> otherUsers = backUserDao
					.getUsersByStatusAndCompanyId(otherUsersParmater);

			HashMap<String, Object> map = new HashMap<>();
			List<String> uuids = new ArrayList<>(); // 存放需要被禁用的催收员uuid
			for (BackUser backUser : otherUsers) {
				uuids.add(backUser.getUuid());
			}
			map.put("uuids", uuids);
			map.put("status", "1");
			// 启用催收员
			backUserDao.disableOrEnableCollections(map);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	@Override
	public List<MmanLoanCollectionPerson> findUnCompleteCollectionOrderByCurrentUnCompleteCountListByMap(Map<String, Object> param) {
		BackRole topCsRole = backRoleService.getTopCsRole();
		List<Integer> roleChildIds = backRoleService.showChildRoleListByRoleId(topCsRole.getId());
		param.put("realIds", roleChildIds);
		return backUserDao.findUnCompleteCollectionOrderByCurrentUnCompleteCountListByMap(param);
	}
}
