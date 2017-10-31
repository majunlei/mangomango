package cc.mangomango.service;

import cc.mangomango.dao.TimeLineMapper;
import cc.mangomango.domain.TimeLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimeLineService {

    @Autowired
    private TimeLineMapper timeLineMapper;

    public List<TimeLine> getByPage(int offset, int limits) {
        if (offset < 0 || limits < 0) {
            return null;
        }
        return timeLineMapper.getByPage(offset, limits);
    }

    public int getCount() {
        return timeLineMapper.getCount();
    }

    public void save(TimeLine timeLine) {
        timeLineMapper.save(timeLine);
    }

}
