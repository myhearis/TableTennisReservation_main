package com.atsu.tabletennisreservation.service;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.atsu.tabletennisreservation.configuration.BaseDataLoader;
import com.atsu.tabletennisreservation.pojo.ReserveDate;
import com.atsu.tabletennisreservation.pojo.User;
import com.atsu.tabletennisreservation.propertyBean.UploadFilePropertyBean;
import com.atsu.tabletennisreservation.utils.DateUtil;
import com.atsu.tabletennisreservation.utils.FileUploadUtil;
import com.atsu.tabletennisreservation.utils.UserContext;
import com.atsu.tabletennisreservation.utils.ZfbCommonUtils;
import com.atsu.tabletennisreservation.webSocket.WebSocket;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

//一些常用的操作
@Component
public class CommonService {
    @Resource
    private DateUtil dateUtil;
    @Resource
    private WebSocket webSocket;
    @Resource
    private UploadFilePropertyBean uploadFilePropertyBean;
    @Resource
    private BaseDataLoader baseDataLoader;
    //获取当前日期字符串
    public String getNowDateStr(){
      return   dateUtil.dateToStr(new Date());
    }
    //获取字符串日期
    public Date getNowDate(String dateStr){
        return dateUtil.parse(dateStr);
    }
    //获取guid
    public String getId(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
    /**
     * #Description 文件上传,返回保存的路径
     * @param uploadFile: 上传的文件
     * @param req: 请求
     * @return Map relativePath：相对路径（用于保存），readPath：访问路径，用于前台访问
     * @author sujinbin
     * #Date 2024/1/2
     */
    public Map<String,String> uploadFile(MultipartFile uploadFile, HttpServletRequest req){
        String filePath = uploadFilePropertyBean.getFilePath();
        Map<String, String> map = FileUploadUtil.uploadPahtFile(uploadFile, req, filePath);
        return map;
    }

    //将外部获取的时间字符串与当前时间对比，如果返回值大于1，
    // 说明现在时间大于传入时间，如果为0说明相等，
    // 如果小于0，说明当期时间小于传入时间
    public Long compareToNowDate(String dateStr){
        Date date = dateUtil.parse(dateStr);
        String nowDateStr = getNowDateStr();
        Date nowDate=dateUtil.parse(nowDateStr);
        return nowDate.getTime()-date.getTime();
    }

    //获取当前用户
    public User getThisUserId(){
       return UserContext.getThisUser();
    }
    //给用户发送消息
    public void  sendMsg(String userId,String msg){
        webSocket.sendOneMessage(userId,msg);
    }
    //生成未来n天的格式化后的日期列表
    public List<String> createFutureDataStrList(Integer dataCount){
        return dateUtil.createFutureDataStrList(dataCount);
    }
    //获取预订信息的列表
    public List<ReserveDate> getReserveDate(){
        return baseDataLoader.getReserveDateList();
    }
    //获取预订信息的Map
    public Map<String,ReserveDate> getReserveDateListMap(){
        Map<String, ReserveDate> reserveDateListMap = baseDataLoader.getReserveDateListMap();
        //校验是否需要清除无用基础数据
        if (reserveDateListMap.size()>7){
            clear();
        }
        return reserveDateListMap;
    }
    //清除最近七天以外的基础数据
    public void  clear(){
        Map<String, ReserveDate> reserveDateListMap = baseDataLoader.getReserveDateListMap();
        Set<String> futureDataStrSet = dateUtil.createFutureDataStrSet(7);
        List<String> removeDateList=new ArrayList<>();
        for (String s : reserveDateListMap.keySet()) {
             //截取出日期
             String[] split =s.split("_");
             String dateStr = split[1];
             //如果不是最近七天的日期，把key存入要删除的列表中
             if (!futureDataStrSet.contains(dateStr)){
                 removeDateList.add(s);
             }
        }
        //删除数据
        for (int i=0;i<removeDateList.size();i++){
            reserveDateListMap.remove(removeDateList.get(i));
        }
    }
    //getMessageText
    public String getMessageText(String event){
        return "";
    }
    //生成支付宝交易订单号
    public String createOutTradeNo(){
        StringBuffer key=new StringBuffer();
        key.append(dateUtil.getNowPayTimeStr());
        //加上用户的哈希码
        User user = getThisUserId();
        key.append(user.getGuid().hashCode());
        return key.toString();
    }
    //生成一个支付宝客户端对象
    public AlipayClient createAlipayClient(){
        //获取支付宝网关
        String URL= ZfbCommonUtils.getZFBinfoValue("open_api_domain");
        //获取支付宝应用id:app_id
        String APP_ID= ZfbCommonUtils.getZFBinfoValue("appid");
        //获取应用私钥
        String APP_PRIVATE_KEY=ZfbCommonUtils.getZFBinfoValue("private_key");
        //获取支付宝公钥
        String ALIPAY_PUBLIC_KEY=ZfbCommonUtils.getZFBinfoValue("alipay_public_key");
        //加签方式
        String SIGN_TYPE = ZfbCommonUtils.getZFBinfoValue("sign_type");
        //编码
        String CHARSET=ZfbCommonUtils.getZFBinfoValue("charset");
        //初始化支付宝客户端对象
        AlipayClient alipayClient = new DefaultAlipayClient(URL,APP_ID,APP_PRIVATE_KEY,"json",CHARSET,ALIPAY_PUBLIC_KEY,SIGN_TYPE);
        return alipayClient;
    }
}
