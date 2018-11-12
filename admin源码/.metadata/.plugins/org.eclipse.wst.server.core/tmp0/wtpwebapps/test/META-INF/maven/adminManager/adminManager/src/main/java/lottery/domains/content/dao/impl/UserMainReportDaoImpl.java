package lottery.domains.content.dao.impl;

import java.util.Map;
import java.math.BigDecimal;
import java.util.HashMap;
import lottery.domains.content.vo.bill.UserMainReportVO;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Criterion;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import lottery.domains.content.entity.UserMainReport;
import javautils.jdbc.hibernate.HibernateSuperDao;
import org.springframework.stereotype.Repository;
import lottery.domains.content.dao.UserMainReportDao;

@Repository
public class UserMainReportDaoImpl implements UserMainReportDao
{
    private final String tab;
    @Autowired
    private HibernateSuperDao<UserMainReport> superDao;
    
    public UserMainReportDaoImpl() {
        this.tab = UserMainReport.class.getSimpleName();
    }
    
    @Override
    public boolean add(final UserMainReport entity) {
        return this.superDao.save(entity);
    }
    
    @Override
    public UserMainReport get(final int userId, final String time) {
        final String hql = "from " + this.tab + " where userId = ?0 and time = ?1";
        final Object[] values = { userId, time };
        return (UserMainReport)this.superDao.unique(hql, values);
    }
    
    @Override
    public List<UserMainReport> list(final int userId, final String sTime, final String eTime) {
        final String hql = "from " + this.tab + " where userId = ?0 and time >= ?1 and time < ?2";
        final Object[] values = { userId, sTime, eTime };
        return this.superDao.list(hql, values);
    }
    
    @Override
    public boolean update(final UserMainReport entity) {
        final String hql = "update " + this.tab + " set recharge = recharge + ?1, withdrawals = withdrawals + ?2, transIn = transIn + ?3, transOut = transOut + ?4, accountIn = accountIn + ?5, accountOut = accountOut + ?6, activity = activity + ?7 where id = ?0";
        final Object[] values = { entity.getId(), entity.getRecharge(), entity.getWithdrawals(), entity.getTransIn(), entity.getTransOut(), entity.getAccountIn(), entity.getAccountOut(), entity.getActivity() };
        return this.superDao.update(hql, values);
    }
    
    @Override
    public List<UserMainReport> find(final List<Criterion> criterions, final List<Order> orders) {
        return this.superDao.findByCriteria(UserMainReport.class, criterions, orders);
    }
    
    @Override
    public UserMainReportVO sumLowersAndSelf(final int userId, final String sTime, final String eTime) {
        final String sql = "select sum(umr.recharge) recharge,sum(umr.withdrawals) withdrawals,sum(umr.trans_in) transIn,sum(umr.trans_out) transOut,sum(umr.account_in) accountIn,sum(umr.account_out) accountOut,sum(umr.activity) activity from user_main_report umr left join user u on umr.user_id = u.id where umr.time >= :sTime and umr.time < :eTime and (u.upids like :upid or umr.user_id = :userId)";
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("sTime", sTime);
        params.put("eTime", eTime);
        params.put("upid", "%[" + userId + "]%");
        params.put("userId", userId);
        final Object result = this.superDao.uniqueSqlWithParams(sql, params);
        if (result == null) {
            return null;
        }
        final Object[] results = (Object[])result;
        final double recharge = (results[0] == null) ? 0.0 : ((BigDecimal)results[0]).doubleValue();
        final double withdrawals = (results[1] == null) ? 0.0 : ((BigDecimal)results[1]).doubleValue();
        final double transIn = (results[2] == null) ? 0.0 : ((BigDecimal)results[2]).doubleValue();
        final double transOut = (results[3] == null) ? 0.0 : ((BigDecimal)results[3]).doubleValue();
        final double accountIn = (results[4] == null) ? 0.0 : ((BigDecimal)results[4]).doubleValue();
        final double accountOut = (results[5] == null) ? 0.0 : ((BigDecimal)results[5]).doubleValue();
        final double activity = (results[6] == null) ? 0.0 : ((BigDecimal)results[6]).doubleValue();
        final UserMainReportVO report = new UserMainReportVO();
        report.setRecharge(recharge);
        report.setWithdrawals(withdrawals);
        report.setTransIn(transIn);
        report.setTransOut(transOut);
        report.setAccountIn(accountIn);
        report.setAccountOut(accountOut);
        report.setActivity(activity);
        return report;
    }
}
