package com.fangyuanyouyue.wallet.service;

import com.fangyuanyouyue.base.dto.WechatPayDto;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.wallet.dto.BillMonthDto;
import com.fangyuanyouyue.wallet.dto.UserBalanceDto;
import com.fangyuanyouyue.wallet.dto.WalletDto;

import java.math.BigDecimal;
import java.util.List;

/**
 * 钱包操作接口
 */
public interface WalletService {
    /**
     * 充值
     * @param userId
     * @param price
     * @param type
     * @throws ServiceException
     */
    Object recharge(Integer userId, BigDecimal price,Integer type) throws Exception;

    /**
     * 提现
     * @param userId
     * @param price
     * @param type
     * @throws ServiceException
     */
    void withdrawDeposit(Integer userId, BigDecimal price,Integer type,String account,String realName,String payPwd) throws ServiceException;

    /**
     * 获取用户钱包信息
     * @param userId
     * @return
     * @throws ServiceException
     */
    WalletDto getWallet(Integer userId) throws ServiceException;

    /**
     * 修改积分
     * @param userId
     * @param score
     * @param type 1 增加 2减少
     * @throws ServiceException
     */
    void updateScore(Integer userId,Long score,Integer type) throws ServiceException;

    /**
     * 修改余额
     * @param userId
     * @param amount
     * @param type 1充值 2消费
     * @throws ServiceException
     */
    boolean updateBalance(Integer userId,BigDecimal amount,Integer type) throws ServiceException;

    /**
     * 查询免费鉴定次数
     * @param userId
     * @return
     * @throws ServiceException
     */
    Integer getAppraisalCount(Integer userId) throws ServiceException;

    /**
     * 修改剩余免费鉴定次数
     * @param userId
     * @param count
     * @throws ServiceException
     */
    void updateAppraisalCount(Integer userId,Integer count) throws ServiceException;

    /**
     * 修改信誉度
     * @param userId
     * @param credit
     * @param type 1增加 2减少
     * @throws ServiceException
     */
    void updateCredit(Integer userId,Long credit,Integer type) throws ServiceException;

    /**
     * 修改支付密码
     * @param userId
     * @param payPwd
     * @param newPwd
     * @throws ServiceException
     */
    void updatePayPwd(Integer userId,String payPwd,String newPwd) throws ServiceException;

    /**
     * 微信支付
     * @param orderNo
     * @param price
     * @param notifyUrl
     * @throws ServiceException
     */
    WechatPayDto orderPayByWechat(String orderNo, BigDecimal price,String notifyUrl) throws Exception;

    /**
     * 小程序支付
     * @param userId
     * @param orderNo
     * @param price
     * @param notifyUrl
     * @throws ServiceException
     */
    WechatPayDto orderPayByWechatMini(Integer userId,String orderNo, BigDecimal price,String notifyUrl) throws Exception;

    /**
     * 支付宝支付
     * @param orderNo
     * @param price
     * @param notifyUrl
     * @return
     * @throws ServiceException
     */
    String orderPayByALi(String orderNo, BigDecimal price, String notifyUrl) throws Exception;

    /**
     * 修改充值订单状态
     * @param orderNo
     * @param thirdOrderNo
     * @param payType
     * @return
     * @throws ServiceException
     */
    boolean updateOrder(String orderNo,String thirdOrderNo,Integer payType) throws ServiceException;

    /**
     * 余额账单
     * @param userId
     * @param start
     * @param limit
     * @param type
     * @param date
     * @return
     * @throws ServiceException
     */
    List<UserBalanceDto> billList(Integer userId, Integer start, Integer limit, Integer type, String date) throws ServiceException;

    /**
     * 余额账单详情
     * @param userId
     * @param orderNo
     * @return
     * @throws ServiceException
     */
    UserBalanceDto billDetail(Integer userId, String orderNo) throws ServiceException;

    /**
     * 新增用户收支信息
     * @param userId
     * @param amount
     * @param payType 支付类型 1微信 2支付宝 3余额 4小程序
     * @param type 收支类型 1收入 2支出 3退款
     * @param orderNo
     * @param title
     * @param orderType 订单类型 1商品、抢购 2官方鉴定 3商品议价 4全民鉴定 5专栏(申请专栏：支出、每日返利：收入、申请被拒：退款) 6充值 7提现 8开通会员 9续费会员 10认证店铺
     * @param sellerId
     * @param buyerId
     * @throws ServiceException
     */
    void addUserBalanceDetail(Integer userId,BigDecimal amount,Integer payType,Integer type, String orderNo, String title,Integer orderType,Integer sellerId,Integer buyerId) throws ServiceException;
}
