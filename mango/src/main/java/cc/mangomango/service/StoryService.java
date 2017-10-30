package cc.mangomango.service;

import cc.mangomango.constants.Constants;
import cc.mangomango.dao.StoryMapper;
import cc.mangomango.domain.Story;
import cc.mangomango.util.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by majunlei on 2017/10/15.
 */
@Service
public class StoryService {

    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

    private static Logger logger = LoggerFactory.getLogger(StoryService.class);

    @Autowired
    private StoryMapper storyMapper;

    public int getCount() {
        return storyMapper.getCount();
    }

    public List<Story> getByPage(int offset, int limits) {
        if (offset < 0 || limits < 0) {
            return null;
        }
        return storyMapper.getByPage(offset, limits);
    }

    public Story getDetailByStoryId(long id) {
        if (id <= 0) {
            return null;
        }
        return storyMapper.getDetailByStoryId(id);
    }

    public String upload(String fileType, MultipartFile file) {
        String fileName = UUID.randomUUID().toString().replaceAll("-", "");
        fileName = fileName + "." + fileType;
        try {
            boolean success = ImageUtil.uploadImg(file, Constants.PIC_DIR + fileName, "");
            if (success) {
                return fileName;
            }
        } catch (Exception e) {
            logger.error("保存图片发生错误. fileName:{}", fileName, e);
        }
        return null;
    }

    @Transactional
    public void save(String title, String author, String content) {
        Date currentTime = new Date();
        int date = Integer.parseInt(formatter.format(currentTime));
        Story story = new Story();
        story.setTitle(title);
        story.setAuthor(author);
        story.setCtime((int) (System.currentTimeMillis() / 1000));
        story.setDt(date);
        storyMapper.save(story);
        if (story.getId() <= 0) {
            throw new RuntimeException("插入story失败");
        }
        storyMapper.saveContent(story.getId(), content);
    }

}
