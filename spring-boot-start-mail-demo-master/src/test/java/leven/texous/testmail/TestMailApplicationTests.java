package leven.texous.testmail;

import leven.texous.testmail.service.MailSendService;
import leven.texous.testmail.service.impl.MailSendServiceImpl;
import org.assertj.core.util.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestMailApplicationTests {

  String email = "545431131@qq.com";
  @Autowired
  private MailSendServiceImpl mailSendService;

  public void contextLoads() {
  }

  @Test
  public void sendSimpleEmail() throws Exception {
    String content = "hello";
    mailSendService.sendSimpleMail(new String[] {email}, "测试邮件", content);

  }

  @Test
  public void sendMimeEmail() throws Exception {
    String content = "<a href='https://blog.csdn.net/'>你好，欢迎注册网站，请点击链接激活</a>";
    mailSendService.sendHtmlMail(new String[] {email}, "激活邮件", content);

  }

  @Test
  public void sendAttachment() {
    //mailSendService
    //    .sendAttachmentsMail(new String[] {email}, "发送附件", "这是Java体系图",
    //        "F:/图片/爱旅行.jpg");
  }

  @Test
  public void sendInlineMail() throws Exception {
    mailSendService.sendInlineMail(new String[] {email},"ttt" ,
        "<html><body>带静态资源的邮件内容 图片:<img src='cid:picture' /></body></html>",
        Maps.newHashMap("dd",new File("src/main/resources/static/image/picture.jpg")));
  }

  @Test
  public void sendTemplateMail() throws Exception {
    mailSendService.sendTemplateMail(new String[] {email}, "dsfds", null,
        Maps.newHashMap("username","cpf" ));

  }

}
