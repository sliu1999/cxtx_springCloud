//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.cxtx.user_manage.service.impl;

import com.cxtx.common.domain.FileInfo;
import com.cxtx.common.service.FileInfoService;
import com.cxtx.common.unit.ServiceUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.Staff;
import com.cxtx.user_manage.mapper.StaffMapper;
import com.cxtx.user_manage.service.StaffService;
import com.cxtx.user_manage.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
@Transactional(
        rollbackFor = {Exception.class}
)
public class StaffServiceImpl implements StaffService {
    private static Logger logger = LoggerFactory.getLogger(StaffServiceImpl.class);
    @Autowired
    private StaffMapper staffMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private FileInfoService fileInfoService;
    private static String FILE_TABLE_ICON_CARD_ID = "2018111401";
    private static String FILE_TABLE_ICON_PHOTO_ID = "2018111402";


    @Override
    public int saveStaff(Staff staff) throws Exception {
        if (!this.duplicateCheckStaffNo(staff.getStaffNo(), staff.getId())) {
            throw new Exception("工号不可重复……");
        } else {
            int result;
            if (staff.getId() == null) {
                result = this.staffMapper.insertStaff(staff);
                if (staff.getDepartments() != null && staff.getDepartments().size() > 0) {
                    this.staffMapper.insertStaff2Departments(staff);
                }
            } else {
                result = this.staffMapper.updateStaff(staff);
                this.staffMapper.deleteStaff2Departments(staff.getId());
                if (staff.getDepartments() != null && staff.getDepartments().size() > 0) {
                    this.staffMapper.insertStaff2Departments(staff);
                }
            }

            if (staff.getIconCard() != null) {
                this.fileInfoService.saveFilesInfo(FILE_TABLE_ICON_CARD_ID, staff.getId(), staff.getIconCard());
            }

            if (staff.getIconPhoto() != null) {
                this.fileInfoService.saveFilesInfo(FILE_TABLE_ICON_PHOTO_ID, staff.getId(), staff.getIconPhoto());
            }

            return result;
        }
    }

    @Override
    public Staff selectStaffById(String id) {
        Staff staff = this.staffMapper.selectStaffById(id);
        if (staff != null) {
            List<FileInfo> iconCard = this.fileInfoService.queryFilesInfoByMainId(FILE_TABLE_ICON_CARD_ID, staff.getId());
            List<FileInfo> iconPhoto = this.fileInfoService.queryFilesInfoByMainId(FILE_TABLE_ICON_PHOTO_ID, staff.getId());
            staff.setIconCard(iconCard);
            staff.setIconPhoto(iconPhoto);
        }

        return staff;
    }

    @Override
    public Staff selectStaffByLoginId(String loginId) {
        Staff staff = this.staffMapper.selectStaffByLoginId(loginId);
        if (staff != null) {
            List<FileInfo> iconCard = this.fileInfoService.queryFilesInfoByMainId(FILE_TABLE_ICON_CARD_ID, staff.getId());
            List<FileInfo> iconPhoto = this.fileInfoService.queryFilesInfoByMainId(FILE_TABLE_ICON_PHOTO_ID, staff.getId());
            staff.setIconCard(iconCard);
            staff.setIconPhoto(iconPhoto);
        }

        return staff;
    }

    @Override
    public int deleteStaffById(String id) throws Exception {
        Staff staff = this.staffMapper.selectStaffById(id);
        if (staff != null) {
            if (staff.getLoginId() != null) {
                this.userService.deleteUserByLoginId(staff.getLoginId());
            }

            this.staffMapper.deleteStaff2Departments(id);
            this.fileInfoService.deleteFileInfo(FILE_TABLE_ICON_CARD_ID, id);
            this.fileInfoService.deleteFileInfo(FILE_TABLE_ICON_PHOTO_ID, id);
            return this.staffMapper.deleteStaffById(id);
        } else {
            throw new Exception("没有对应的记录……");
        }
    }

    @Override
    public PageInfo<Staff> selectStaffListByPage(Map params) throws Exception {
        ServiceUtil.checkPageParams(params);
        PageHelper.startPage(params);
        List<Staff> staffs = this.staffMapper.selectStaffList(params);
        return new PageInfo(staffs);
    }

    @Override
    public Boolean duplicateCheckStaffNo(String staffNo, String id) {
        return this.staffMapper.selectCountByStaffNo(staffNo, id) == 0 ? true : false;
    }

    @Override
    public List<Staff> queryStaffsList(Map param) {
        return staffMapper.selectStaffList(param);
    }
}
