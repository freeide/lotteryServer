package lottery.domains.content.biz.read;

import javautils.jdbc.PageList;
import lottery.domains.content.entity.UserDailySettle;

import java.util.List;

/**
 * Created by Nick on 2017-06-05.
 */
public interface UserDailySettleReadService {
    // PageList search(List<Integer> userIds, int start, int limit);
    //
    // PageList searchByDirectLowers(int upUserId, int start, int limit);
    //
    // List<UserDailySettle> findByDirectLowers(int upUserId);

    /**
     * 查询所有数据，按创建时间倒序排序
     */
    PageList searchAll(int start, int limit);

    /**
     * 查询指定用户的团队数据，按创建时间倒序排序
     */
    PageList searchByTeam(int[] userIds, int start, int limit);

    /**
     * 查询指定用户的团队数据，按创建时间倒序排序
     */
    PageList searchByTeam(int userId, int start, int limit);

    PageList searchByUserId(int userId, int start, int limit);

    List<UserDailySettle> findByUserIds(List<Integer> userIds);
    
    Long getCountUser(int userId);
}
