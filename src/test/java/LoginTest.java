import com.youran.controller.Discuz;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class LoginTest {
    @Test
    public void login() throws UnsupportedEncodingException {
        Discuz discuz = new Discuz();
        boolean login = discuz.login("aliyun", "aliyun");
        if (login) {
            System.out.println("login success");
        } else {
            System.out.println("login faild");
        }
    }

    @Test
    public void getInfo() throws IOException {
        Discuz discuz = new Discuz();
        boolean login = discuz.login("aliyun", "aliyun");
        if (login) {
            discuz.getInfo();
        }
    }
    @Test
    public void getFormHash() throws IOException {
        Discuz discuz = new Discuz();
        discuz.getFromHash();
        System.out.println(discuz.getFormHash());
    }
    @Test
    public void register() throws IOException {
        Discuz discuz = new Discuz();
        discuz.register("test6","123456","123445556@qq.com");
    }
    @Test
    public void postMsg() throws IOException {
        Discuz discuz = new Discuz();
        boolean login = discuz.login("aliyun", "aliyun");
        discuz.getFromHash();
        if (login) {
            Scanner sc=new Scanner(System.in);
            System.out.println("请输入标题");
            String title=sc.next();
            System.out.println("请输入内容");
            String msg=sc.nextLine();
            System.out.println("请输入板块ID  默认板块：2、测试板块1:36、测试板块1:37");
            String fid=sc.nextLine();
            System.out.println("是否确定发表？ y/n");
            String flag=sc.nextLine();
            if ("y".equals(flag)||"Y".equals(flag)){
                discuz.postMsg(title,msg,fid);
            }else{
                System.out.println("发帖失败");
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Discuz discuz = new Discuz();
        boolean login = discuz.login("aliyun", "aliyun");
        discuz.getFromHash();
        if (login) {
            Scanner sc=new Scanner(System.in);
            System.out.println("请输入标题");
            String title=sc.next();
            System.out.println("请输入内容");
            String msg=sc.next();
            System.out.println("请输入板块ID  默认板块：2、测试板块1:36、测试板块1:37");
            String fid=sc.next();
            System.out.println("是否确定发表？ y/n");
            String flag=sc.next();
            if ("y".equals(flag)||"Y".equals(flag)){
                discuz.postMsg(title,msg,fid);
                System.out.println("发帖成功！");
            }else{
                System.out.println("发帖失败");
            }
        }
    }

}