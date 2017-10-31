package cc.mangomango.service;

import cc.mangomango.constants.Constants;
import cc.mangomango.dao.UserMapper;
import cc.mangomango.dao.XqMapper;
import cc.mangomango.domain.PicUrl;
import cc.mangomango.domain.User;
import cc.mangomango.domain.Xq;
import cc.mangomango.domain.XqComment;
import cc.mangomango.util.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * Created by majunlei on 2017/10/29.
 */
@Service
public class XqService {

    private static Logger logger = LoggerFactory.getLogger(StoryService.class);

    @Autowired
    private XqMapper xqMapper;
    @Autowired
    private UserMapper userMapper;

    public int getCount() {
        return xqMapper.getCount();
    }

    public List<Xq> getByPage(int offset, int limits) {
        if (offset < 0 || limits < 0) {
            return null;
        }
        List<Xq> xqList = xqMapper.getByPage(offset, limits);
        if (!CollectionUtils.isEmpty(xqList)) {
            for (Xq xq : xqList) {
                List<PicUrl> urls = xqMapper.getUrls(xq.getStamp());
                if (!StringUtils.isEmpty(urls)) {
                    xq.setUrls(urls);
                }
                List<XqComment> xqCommentList = xqMapper.getComments(xq.getId());
                if (!CollectionUtils.isEmpty(xqCommentList)) {
                    xq.setComments(xqCommentList);
                }
                User user = userMapper.getByUsername(xq.getAuthor());
                if (user != null && !StringUtils.isEmpty(user.getHeadPhoto())) {
                    xq.setHeadPhoto(user.getHeadPhoto());
                }
            }
        }
        return xqList;
    }

    public void like(long id) {
        xqMapper.likeAdd(id);
    }

    public void viewAdd(long id) {
        xqMapper.viewAdd(id);
    }

    public void commentAdd(long id, String comment) {
        xqMapper.saveComment(id, comment);
    }

    public void save(String xqContent, int stamp, String author) {
        Xq xq = new Xq();
        xq.setXq(xqContent);
        xq.setAuthor(author);
        xq.setStamp(stamp);
        xqMapper.save(xq);
    }

    public boolean upload(int stamp, String fileType, MultipartFile file) {
        String fileName = UUID.randomUUID().toString().replaceAll("-", "");
        String thumbName = fileName + "_thumb" + "." + fileType;
        fileName = fileName + "." + fileType;
        try {
            boolean success = ImageUtil.uploadImg(file, Constants.PIC_DIR + fileName, Constants.PIC_DIR + thumbName);
            if (!success) {
                return false;
            }
            xqMapper.savePic(stamp, fileName, thumbName);
        } catch (Exception e) {
            logger.error("保存图片发生错误. fileName:{}", fileName, e);
            return false;
        }
        return true;
    }

}
