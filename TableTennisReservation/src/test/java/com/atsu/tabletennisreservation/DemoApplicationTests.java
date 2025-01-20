package com.atsu.tabletennisreservation;

import com.atsu.tabletennisreservation.configuration.BaseDataLoader;
import com.atsu.tabletennisreservation.dto.MenuNode;
import com.atsu.tabletennisreservation.dto.PageResult;
import com.atsu.tabletennisreservation.mapper.*;
import com.atsu.tabletennisreservation.pojo.*;
import com.atsu.tabletennisreservation.propertyBean.UploadFilePropertyBean;
import com.atsu.tabletennisreservation.redis.dao.RedisDao;
import com.atsu.tabletennisreservation.service.*;
import com.atsu.tabletennisreservation.utils.DateUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//使用Tomcat环境进行单元测试，因为webscoket是需要依赖tomcat等容器才能启动，不然会报错
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DemoApplicationTests {
    @Resource
    UserMapper userMapper;
    @Resource
    DateUtil dateUtil;
    @Resource
    CommonService commonService=new CommonService();
    @Resource
    UploadFilePropertyBean uploadFilePropertyBean;
    @Resource
    BallTableMapper ballTableMapper;
    @Resource
    BallTableService ballTableService;
    @Resource
    ReserveMapper reserveMapper;
    @Resource
    BaseDataLoader baseDataLoader;
    @Resource
    CandidateReserveMapper candidateReserveMapper;
    @Resource
    private CandidateReserveService candidateReserveService;
    @Resource
    private MessageMapper messageMapper;
    @Resource
    private MessageService messageService;
    @Resource
    private MenuMapper menuMapper;
    @Resource
    private MenuService menuService;
    @Resource
    private  RoleService roleService;
    @Resource
    private  RoleMenuService roleMenuService;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private RedisDao redisDao;
    @Resource
    RedisTemplate redisTemplate;
    @Resource
    MatchInfoMapper matchInfoMapper;
    @Resource
    MatchInfoService matchInfoService;
    @Test
    public void testUserDao(){
//        User userById = userMapper.getUserById("11");
//        System.out.println(userById);
//        User user=new User();
//
//        user.setUserName("hello");
//        user.setPassword("123456");
//        String dateToStr = dateUtil.dateToStr(new Date());
//        user.setCreateDate(dateToStr);
//        user.setUpdateDate(dateToStr);
//        user.setGuid(commonService.getId());
//        user.setStatus(1);
//        userMapper.savaUser(user);
        userMapper.deleteUserByCode("hello");
        User hello = userMapper.getUserByCode("hello");
        System.out.println(hello);

    }
    //测试配置绑定
    @Test
    public void testPropertyBean(){
        String filePath = uploadFilePropertyBean.getFilePath();
        String urlMapping = uploadFilePropertyBean.getUrlMapping();
        System.out.println(filePath);
        System.out.println(urlMapping);
    }
    @Test
    public void testBallTableMapper(){
        //保存
//        BallTable ballTable=new BallTable();
//        ballTable.setAddress("体育馆三楼3号桌");
//        ballTable.setCode("3");
//        ballTable.setGuid(commonService.getId());
//        ballTable.setDescription("测试数据");
//        ballTable.setCreateDate(commonService.getNowDateStr());
//        ballTable.setUpdateDate(commonService.getNowDateStr());
//        ballTable.setStatus(1);
//        int i = ballTableMapper.saveBallTable(ballTable);
//        System.out.println(i);

        //获取
//        List<BallTable> ballTableList = ballTableMapper.getBallTableList(1);
//        System.out.println(ballTableList);
        PageResult<BallTable> ballTableListPage = ballTableService.getBallTableListPage(1, 2);
        System.out.println(ballTableListPage);
    }
    @Test
    public void testReserveBean(){
//        List<String> list=new ArrayList<>();
//        list.add("2024-02-04");
//       reserveMapper.getReserveByDateList(list);
//        List<ReserveDate> reserveDateList = baseDataLoader.getReserveDateList();
//        System.out.println(reserveDateList);
        List<ReserveDate> list = commonService.getReserveDate();
        Map<String, ReserveDate> reserveDateListMap = commonService.getReserveDateListMap();
        System.out.println(reserveDateListMap);
        System.out.println(list);
    }
    @Test
    public void testBallMapperBean(){
        List<BallTable> canReserveBallTableList = ballTableMapper.getCanReserveBallTableList();
        System.out.println(canReserveBallTableList);
    }
    @Test
    public void testCandidateReserveMapper(){
        CandidateReserve candidateReserve=new CandidateReserve();
        candidateReserve.setGuid("cdfd159f-d4d1-4667-9112-08b7c1764f09");
        candidateReserve.setUseTime(9);
//        int i = candidateReserveMapper.saveCandidateReserve(candidateReserve);
//        System.out.println(i);
//        List<CandidateReserve> list = candidateReserveMapper.getCandidateReserveList(candidateReserve);
//        System.out.println(list);
        candidateReserveMapper.updateCandidateReserveById(candidateReserve);

    }
    @Test
    public void testSaveCandidateReserveMapper(){
        CandidateReserve candidateReserve=new CandidateReserve();
        candidateReserve.setGuid(commonService.getId());
        candidateReserve.setUseTime(9);
        candidateReserve.setStatus(0);
        candidateReserve.setUserId("3ccbec15-588d-4821-8df4-4c3c66930d73");
        candidateReserveMapper.saveCandidateReserve(candidateReserve);

    }
    @Test
    public void testCandidateReserveService(){
        PageResult<CandidateReserve> candidateReserveListPage = candidateReserveService.getCandidateReserveListPage(1, 3, new CandidateReserve());
        System.out.println(candidateReserveListPage);

    }
    @Test
    public void testPeriod(){
        ReserveDate reserveDate=new ReserveDate();
        reserveDate.reserveProcess(14,3);
        reserveDate.reserveProcess(20,2);
        List<Period> periodList = reserveDate.getPeriodList();
        System.out.println(periodList);
    }
    @Test
    public void testGetUseCandidateReserveList(){
        Period period=new Period();
        period.setUseTime(5);
        period.setStartTime(10);
        period.setEndTime(15);
        period.setTableCode("1");
        List<CandidateReserve> list = candidateReserveMapper.getUseCandidateReserveList(period);
        System.out.println(list);

    }
    @Test
    public void  testMessageMapper(){
        Message message=new Message();
//        message.setGuid(commonService.getId());
//        message.setValue("你好，我是测试消息");
//        message.setType(2);
//        message.setCategory(1);
//        message.setOriginId("fdsaf");
//        message.setOriginName("苏小白");
//        message.setTargetId("fdsafdsagf");
//        message.setTargetName("李白");
//        message.setCreateTime(commonService.getNowDateStr());
//        messageMapper.saveMessage(message);
//        message.setOriginName("苏小白");
//        List<Message> messageList = messageMapper.getMessageList(message);
//        System.out.println(messageList);
        message.setIsRead(1);
//        message.setGuid("656e08e5-f97c-4ff8-bf55-bac75e8ebb34");
        List<String> ids=new ArrayList<>();
        ids.add("656e08e5-f97c-4ff8-bf55-bac75e8ebb34");
        ids.add("b020bc4a-1ee9-46de-8aaa-5b3467b24289");
        messageMapper.updateMessageByIdBatch(message,ids);
    }
    @Test
    public void  testMessageService(){
        boolean b = messageService.saveSystemMessage("测试保存系统消息", "test");
        System.out.println(b);
    }
    @Test
    public void testMenuMapper(){
        List<Menu> menuList = menuMapper.getMenuList(null);
        List<MenuNode> nodeList=new ArrayList<>();
        Iterator<Menu> iterator = menuList.iterator();
        List<String> removeIdList=new ArrayList<>();//需要移除的内容
        List<MenuNode> re=new ArrayList<>();
        //转化node
        for (int i=0;i<menuList.size();i++){
            nodeList.add(new MenuNode(menuList.get(i)));
        }
        //把主父菜单提取出来
        while (iterator.hasNext()){
            Menu menu = iterator.next();
            String parentId = menu.getParentId();
            if (parentId!=null&&parentId.length()>0){
                //找到父节点
                MenuNode parentMenuNode=null;
                for (int i=0;i<nodeList.size();i++){
                    if (nodeList.get(i).getId().equals(parentId)){
                        parentMenuNode=nodeList.get(i);
                        break;
                    }
                }
                //加入节点
                List<MenuNode> childrenList = parentMenuNode.getChildren();
                if (childrenList==null){
                    parentMenuNode.setChildren(new ArrayList<MenuNode>());
                }
                parentMenuNode.getChildren().add(new MenuNode(menu));
                //标记移除
                removeIdList.add(menu.getGuid());
            }
        }
        //移除内容
        for (int i=0;i<nodeList.size();i++){
            MenuNode menuNode = nodeList.get(i);
            if (!removeIdList.contains(menuNode.getId())){
                re.add(menuNode);
            }
        }
        //将内容转化为MenuNode

//        System.out.println(menuList);
//        System.out.println(nodeList);
        Gson gson=new Gson();
        String s = gson.toJson(re);
        System.out.println(s);
//        System.out.println(re.size());
//        for (int i=0;i<re.size();i++){
//            System.out.println(re.get(i));
//        }

    }
    @Test
    public void  testMenuService(){
        Menu menu=new Menu();
        menu.setGuid("003");
        menu.setIsLeaf(0);//非叶子节点
        menu.setType(1);
        menu.setName("候补管理");
        menuService.saveMenu(menu);
    }
    @Test
    public void  testRoleService(){
        Role role=new Role();
        role.setGuid("normal");
        role.setName("普通会员");
//        role.setRemark("系统管理员角色");
//        roleService.saveRole(role);
        role.setRemark("普通用户角色");
//        roleService.updateRoleById(role);
//        List<Role> roleList = roleService.getRoleList(role);
//        System.out.println(roleList);
        roleService.saveRole(role);
    }
    @Test
    public void testRoleMenuService(){
        RoleMenu roleMenu=new RoleMenu();
        roleMenu.setGuid("432fdsa1");
        roleMenu.setMenuId("001");
        roleMenu.setRoleId("admin");
        roleMenuService.saveRoleMenu(roleMenu);
    }
    @Test
    public void getRoleMenu(){
        List<Menu> list = menuMapper.getRoleMenuList("normal");
        System.out.println(list);
    }
    @Test
    public void testRole(){
        List<Role> roleList = roleMapper.getRoleList(new Role());
        System.out.println(roleList);
    }
    @Test
    public void  testCount(){
        int count = messageMapper.getCount(new Message());
        System.out.println(count);
    }
    @Test
    public void  testRedis(){
        Reserve reserve=new Reserve();
        reserve.setPayAmt(new BigDecimal(19));
        reserve.setGuid(commonService.getId());
        reserve.setUserId("fdasrfdsa");
        reserve.setUserName("test");
        reserve.setStartDate("20240101");
        Reserve reserve2=new Reserve();
        reserve2.setPayAmt(new BigDecimal(19));
        reserve2.setGuid(commonService.getId());
        reserve2.setUserId("fdasrfdsa");
        reserve2.setUserName("hello");
        reserve2.setStartDate("20240101");
        redisDao.addReserve("hello1",reserve, 1000);
        redisDao.addReserve("hello2",reserve2, 1000);
    }
    //获取数据
    @Test
    public void  testRedis2(){
        Reserve reserve=null;
        String hello = (String) redisTemplate.opsForValue().get("hello");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
             reserve = objectMapper.readValue(hello, Reserve.class);
            System.out.println(reserve);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void testGetRedis(){
        List<String> stringList=new ArrayList<>();
        stringList.add("hello1");
        stringList.add("hello2");
        List<Reserve> reserveList = redisDao.getReserveList(stringList);
        System.out.println(reserveList);
    }
    @Test
    public void  testMatchInfoMapper(){
        MatchInfo matchInfo=new MatchInfo();
        matchInfo.setGuid(commonService.getId());
        matchInfo.setBillUserName("hello");
        matchInfo.setStatus(2);
        matchInfo.setBillRemark("测试数据");
        matchInfo.setLevel(1);
        int i = matchInfoMapper.saveMatchInfo(matchInfo);
    }
    @Test
    public void  testMatchInfoMapperSelect(){
        MatchInfo matchInfo=new MatchInfo();
        matchInfo.setLevel(1);
        List<MatchInfo> matchInfoList = matchInfoMapper.getMatchInfoList(matchInfo);
        System.out.println(matchInfoList);
    }
    @Test
    public void  testMatchInfoMapperDelete(){
        MatchInfo matchInfo=new MatchInfo();
        matchInfo.setGuid("158bb3c7-ca4a-44f2-8cdb-feec605d035d");
        int i = matchInfoMapper.deleteMatchInfo(matchInfo);
        System.out.println(i);
    }
    @Test
    public void  testMatchInfoMapperUpdate(){
        MatchInfo matchInfo=new MatchInfo();
        matchInfo.setGuid("158bb3c7-ca4a-44f2-8cdb-feec605d035d");
        matchInfo.setBillRemark("修改后的数据");
        matchInfo.setMatchUserId("fdsafdsgfgfsdgsaddfg");
        matchInfoMapper.updateMatchInfoById(matchInfo);
    }
    @Test
    public void testMatchInfoService(){
        List<MatchInfo> matchInfoList=new ArrayList<>();
        MatchInfo matchInfo_1=new MatchInfo();
        matchInfo_1.setBillRemark("测试批量数据1");
        MatchInfo matchInfo_2=new MatchInfo();
        matchInfo_2.setBillRemark("测试批量数据1");
        matchInfoList.add(matchInfo_1);
        matchInfoList.add(matchInfo_2);
        matchInfoService.saveMatchInfoBatch(matchInfoList);
    }
    @Test
    public void fdsa(){
        List<String> list=new ArrayList<>();
        list.add("f405d584-51c2-4fb2-b797-39f2ac3f8f30");
        List<Reserve> reserveListByMatchInfoId = reserveMapper.getReserveListByMatchInfoId(list);
        System.out.println(reserveListByMatchInfoId);
    }
    //查询进行中的订单
    @Test
    public void  testR(){
        Reserve reserve=new Reserve();
        List<Reserve> list = reserveMapper.getProgressReserve(reserve);
        System.out.println(list);
    }
    @Test
    public void testResetMatch(){
        //将匹配单清空
        matchInfoService.resetMatchInfo("5922fc77-0062-45cb-82aa-852da6a15a8f");
    }
    @Test
    public void  testGetValidReserve(){
        List<Reserve> list = reserveMapper.getValidReserveByDateList(null);
        System.out.println(list);
    }
    @Test
    public void  testRe(){
        CandidateReserve candidateReserve=new CandidateReserve();
        candidateReserve.setTableCode("1");
        candidateReserve.setCreateDate(commonService.getNowDateStr());
//        redisDao.addCandidateReserve("hello",candidateReserve,500);
        CandidateReserve hello = redisDao.getCandidateReserve("hello");
        System.out.println(hello);
    }
}
