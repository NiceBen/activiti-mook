package com.deo.activitimook.listener;

import com.deo.activitimook.service.HelloService;
import com.deo.activitimook.util.SpringContextHolder;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * TODO
 *
 * @date 2021-12-02
 * @since TODO
 */
public class MangerTaskHandlerCandidateUsers implements TaskListener {
    private static final long serialVersionUID = 1L;

    private ApplicationContext applicationContext = SpringContextHolder.getApplicationContext();

    private Expression roleCode;

    private Expression crId;

        @Override
        public void notify(DelegateTask delegateTask) {

            Object delegateCrId = delegateTask.getVariable("crId");
            Object delegateRoleCode = delegateTask.getVariable("roleCode");
            System.out.println("这是delegateTask从getVariable中获取的：delegateCrId:" + delegateCrId + ", delegateRoleCode:" + delegateRoleCode);

            // 调用@Service注解标识方法
            HelloService service = (HelloService) applicationContext.getBean(HelloService.class);
            List<String> usersList = service.sayHello();

            System.out.println("进入MangerTaskHandlerCandidateUsers=========");

            Object roleCodeValue= roleCode.getValue(delegateTask);
            Object crIdValue= this.crId.getValue(delegateTask);
            System.out.println("roleCodeValue:" + roleCodeValue + ", crIdValue:" + crIdValue);

            System.out.println("-----------------");
            String roleCodeText = roleCode.getExpressionText();
            String crIdText = this.crId.getExpressionText();

            System.out.println("roleCodeText:" + roleCodeText + ", crIdText:" + crIdText);
//            List<String> users = Arrays.asList("bajie", "wukong");
            delegateTask.addCandidateUsers(usersList);//完成多处理人的指定
            System.out.println("完成指定处理人！");


//            System.out.println("进入MangerTaskHandlerCandidateUsers=========");
//            /**从新查询当前用户，再获取当前用户对应的领导*/
//            Employee employee = SessionContext.get();
//            //当前用户
//            String name = employee.getName();
//            System.out.println("当前登录人========"+name);
//            String[] empLoyees = {"冯小刚经纪人","范冰冰经纪人","冯小刚"};
//            delegateTask.addCandidateUsers(Arrays.asList(empLoyees));//完成多处理人的指定
//            System.out.println("节点任务人========冯小刚经纪人,范冰冰经纪人,冯小刚");
        }
}
