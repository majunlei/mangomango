package cc.mangomango.service;

import cc.mangomango.constants.Constants;
import cc.mangomango.dao.PicMapper;
import cc.mangomango.domain.Pic;
import cc.mangomango.util.DateUtil;
import cc.mangomango.util.FileUtil;
import cc.mangomango.util.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * 作者:majunlei
 * 日期:2017/10/12 下午9:47
 */
@Service
public class PicService {

    private static Logger logger = LoggerFactory.getLogger(PicService.class);

    @Autowired
    private PicMapper picMapper;

    public boolean save(String fileType, MultipartFile file) {
        String fileName = UUID.randomUUID().toString().replaceAll("-", "");
        String thumbName = fileName + "_thumb" + "." + fileType;
        fileName = fileName + "." + fileType;
        try {
            boolean success = ImageUtil.uploadImg(file, Constants.PIC_DIR + fileName, Constants.PIC_DIR + thumbName);
            if (!success) {
                return false;
            }
            Integer latestSeq = picMapper.getLatestSeq();
            int seq = latestSeq == null ? 1 : latestSeq + 1;
            cc.mangomango.domain.Pic pic = new Pic();
            pic.setSeq(seq);
            pic.setUrl(fileName);
            pic.setThumbUrl(thumbName);
            pic.setTitle(DateUtil.getNowStr());
            picMapper.save(pic);
        } catch (Exception e) {
            logger.error("保存图片发生错误. fileName:{}", fileName, e);
            return false;
        }
        return true;
    }

    public List<Pic> getByPage(int offset, int limits) {
        if (offset < 0 || limits < 0) {
            return null;
        }
        return picMapper.getByPage(offset, limits);
    }

    public int getCount() {
        return picMapper.countTotal();
    }

    public int del(long id) {
        String url = picMapper.getUrlById(id);
        if (!StringUtils.isEmpty(url)) {
            String fullName = url.replace("thumb", "");
            FileUtil.delFile(Constants.PIC_DIR + fullName);
            FileUtil.delFile(Constants.PIC_DIR + url);
        }
        return picMapper.del(id);
    }

    public int changePos(long id, int pos) {
        return picMapper.updatePos(id, pos);
    }

}
