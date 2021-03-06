package leven.texous.testmail.service.impl;

import freemarker.template.Template;
import leven.texous.testmail.service.MailSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Map;
import java.util.Set;

/**
 * created by lxh
 * Date: 2017/12/4
 * Time: 8:54
 * Description:
 */
@Service
public class MailSendServiceImpl implements MailSendService {

  @Autowired
  private JavaMailSender mailSender;
  @Autowired
  private FreeMarkerConfigurer freeMarkerConfigurer;
  //@Value("spring.mail.username")
  private static final String sender = "caopf@maplecloudy.com";

  @Override
  public void sendSimpleMail(String[] to, String subject, String content)
      throws Exception {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(sender);
    message.setTo(to);
    message.setSubject(subject);
    message.setText(content);
    mailSender.send(message);
  }

  @Override
  public void sendHtmlMail(String[] to, String subject, String html)
      throws Exception {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);
    helper.setFrom(sender);
    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(html, true);
    mailSender.send(message);
  }

  @Override
  public void sendAttachmentsMail(String[] to, String subject, String content,
      Map<String,File> files) throws Exception {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);
    helper.setFrom(sender);
    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(content);
    //???????????????????????????????????????????????????
    FileSystemResource file = new FileSystemResource(
        new File("src/main/resources/static/image/picture.jpg"));
    //????????????
    helper.addAttachment("??????.jpg", file);
    Set<Map.Entry<String,File>> fileSet = files.entrySet();
    for (Map.Entry f : fileSet) {
      helper.addAttachment((String) f.getKey(), (File) f.getValue());
    }
    mailSender.send(message);
  }

  @Override
  public void sendInlineMail(String[] to, String subject, String html,
      Map<String,File> files) throws Exception {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);
    helper.setFrom(sender);
    helper.setTo(to);
    helper.setSubject(subject);
    //?????????????????????????????????HTML??????,??????cid:??????????????????
    //"<html><body>?????????????????????????????? ??????:<img src='cid:picture' /></body></html>"
    helper.setText(html, true);
    Set<Map.Entry<String,File>> fileSet = files.entrySet();
    for (Map.Entry f : fileSet) {
      helper.addInline((String) f.getKey(), (File) f.getValue());
    }
    FileSystemResource file = new FileSystemResource(
        new File("src/main/resources/static/image/picture.jpg"));
    helper.addInline("picture", file);
    mailSender.send(message);
  }

  @Override
  public void sendTemplateMail(String[] to, String subject, Template template,
      Map<String,Object> model) throws Exception {
    MimeMessage message = null;
    message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);
    helper.setFrom(sender);
    helper.setTo(to);
    helper.setSubject(subject);
    //?????? application.properties ????????????????????????
    FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
    configurer.setTemplateLoaderPath("classpath:templates");
    //?????? html ??????
    template = freeMarkerConfigurer.getConfiguration()
        .getTemplate("mail.html");
    String html = FreeMarkerTemplateUtils
        .processTemplateIntoString(template, model);
    helper.setText(html, true);
    mailSender.send(message);
  }
}
