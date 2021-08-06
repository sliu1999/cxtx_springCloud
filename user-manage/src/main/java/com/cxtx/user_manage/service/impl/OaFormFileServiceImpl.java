package com.cxtx.user_manage.service.impl;

import com.cxtx.common.domain.JwtModel;
import com.cxtx.common.unit.HttpServletUtils;
import com.cxtx.common.unit.ServiceUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.OaFormFile;
import com.cxtx.user_manage.mapper.OaFormFileMapper;
import com.cxtx.user_manage.service.OaFormFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
@Transactional(rollbackFor = Exception.class)
public class OaFormFileServiceImpl implements OaFormFileService {

    @Value("${upload-path}")
    String uploadPath;

    @Autowired
    private OaFormFileMapper oaFormFileMapper;


    @Override
    public int deleteByPrimaryKey(Long id) {
        return oaFormFileMapper.deleteByPrimaryKey(id);
    }


    @Override
    public OaFormFile uploadImg(MultipartFile file) throws IOException {
        JwtModel curUser = HttpServletUtils.getUserInfo();
        OaFormFile fileSave = null;

        String currentTime = new SimpleDateFormat("yyyyMMddHHmmssSSSS").format(new Date());
        String trueFileName = file.getOriginalFilename();
        String suffix = trueFileName.substring(trueFileName.lastIndexOf(".") + 1);
        String fileName = currentTime + "." + suffix;
        // 默认文件夹
        String defaultDir = "/pc/";
        File dirCheck = new File(uploadPath + defaultDir);
        if (!dirCheck.exists()) {
            dirCheck.mkdirs();
        }
        file.transferTo(new File(uploadPath + defaultDir + fileName));

        fileSave = new OaFormFile();
        fileSave.setFileName(fileName);
        fileSave.setTrueName(trueFileName);
        fileSave.setFilePath(defaultDir);
        fileSave.setSize(String.valueOf(file.getSize()));
        fileSave.setCreateName(curUser.getUsername());
        fileSave.setCreateBy(Long.valueOf(curUser.getUserId()));
        fileSave.setCreateDate(new Date());
        oaFormFileMapper.insertSelective(fileSave);

        return fileSave;

    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public OaFormFile selectByPrimaryKey(Long id) {
        return oaFormFileMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(OaFormFile record) {
        return oaFormFileMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public List<OaFormFile> selectAll() {
        return oaFormFileMapper.selectAll();
    }

    @Override
    public List<OaFormFile> getListByIds(List<Long> ids) {
        return oaFormFileMapper.getListByIds(ids);
    }

    @Override
    public PageInfo<OaFormFile> queryByPage(Map params) {
        ServiceUtil.checkPageParams(params);
        PageHelper.startPage(params);
        List<OaFormFile> labels = oaFormFileMapper.selectAll();
        return new PageInfo<OaFormFile>(labels);
    }
}