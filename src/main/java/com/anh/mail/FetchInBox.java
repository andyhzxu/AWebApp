package com.anh.mail;

import com.anh.java8.Article;
import scala.annotation.meta.field;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.sound.midi.Soundbank;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * java类简单作用描述
 *
 * @Description: java类作用描述
 * @Author: anh6
 * @CreateDate: 2018/3/14 22:38
 * @UpdateUser:
 * @UpdateRemark:
 */
public class FetchInBox {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void mail() throws Exception {

        /**
         * 126邮箱配置
         * POP3服务器 pop.126.com 110
         * SMTP服务器 smtp.126.com 25
         * IMAP服务器 imap.126.com 143
         */
//        URLName url = new URLName("pop3", "pop.126.com", 110, "INBOX", "xx@xx.xx", "xx");
        URLName url = new URLName("pop3", "pop3.xx.com.cn", 110, "INBOX", "aa@xx.xx.xx", "xxx");
        Properties props = new Properties();
        Session session = Session.getInstance(props, null);
        Store store = session.getStore(url);
        store.connect();
        Folder folder = store.getFolder(url);
        folder.open(Folder.READ_WRITE);
        Message[] message = folder.getMessages();
//      System.out.println("Message Length: " + message.length + "\nTop 3 Message subject:");
        for (int i = message.length - 1; i > message.length - 10; i--) {
            Message msg = message[i];
            System.out.println("=====================================================================================================================");
            System.out.println("发件人:" + formatAddress(msg.getFrom())
                    + "\n发送时间:" + formatDate(msg.getSentDate())
                    + "\n收件人:" + formatAddress(msg.getRecipients(Message.RecipientType.TO))
                    + "\n收件时间:" + formatDate(msg.getReceivedDate())
                    + "\n抄送:" + formatAddress(msg.getRecipients(Message.RecipientType.CC))
                    + "\n暗送:" + formatAddress(msg.getRecipients(Message.RecipientType.BCC))
                    + "\n主题:" + msg.getSubject());

//            System.out.println("Next Reply to:" + formatAddress(msg.getReplyTo()));
            if (msg.isMimeType("text/plain")) {
                System.out.println("--------------------------------------------------------------------------");
                System.out.println(">>>1:Content Type: " + msg.getContentType());
                System.out.println(msg.getContent());
                System.out.println("--------------------------------------------------------------------------");
            } else if (msg.isMimeType("multipart/*")) {
                System.out.println("--------------------------------------------------------------------------");
                System.out.println(">>>2:Content Type: " + msg.getContentType());
                Multipart mp = (Multipart) msg.getContent();
                int count = mp.getCount();
                System.out.println("A total of " + count + " part");
                for (int j = 0; j < count; j++) {
                    BodyPart bp = mp.getBodyPart(j);
                    System.out.println("BodyPart Type[" + j + "]:" + bp.getContentType() + "");

                    String disposition = bp.getDisposition();
                    if (disposition != null && disposition.equals(Part.ATTACHMENT)) {
                        System.out.println("*************************************************************************************************************************");
                        System.out.println("附件：" + bp.getFileName());
                        System.out.println("*************************************************************************************************************************");
                    }

                    System.out.println("》---------");
                    System.out.println(bp.getContent());
                    System.out.println("---------《");
                }
            } else {
                System.out.println("--------------------------------------------------------------------------");
                System.out.println(">>>3:Content Type: " + msg.getContentType());
                System.out.println(msg.getContent());
                System.out.println("--------------------------------------------------------------------------");
            }


        }
    }

    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        return sdf.format(date);
    }

    public static String formatAddress(Address[] addr) {
        StringBuffer sb = new StringBuffer();
        if (addr == null) {
            return "";
        }
        for (int i = 0; i < addr.length; i++) {
            Address a = addr[i];
            if (a instanceof InternetAddress) {
                InternetAddress ia = (InternetAddress) a;
                sb.append(ia.getPersonal() + "<" + ia.getAddress() + ">,");
            } else {
                sb.append(a.toString());
            }
        }
        if (sb.length() != 0) {
            return sb.substring(0, sb.length() - 1);
        }
        return "";
    }

    public static void main(String[] args) throws Exception {
//        mail();
        FetchInBox fetchInBox = new FetchInBox();
        fetchInBox.test();
    }

    public void test() {
        list.add(0.012);
        list.add(0.034);
        list.add(0.056);
        list.add(0.078);
        list.add(0.091);
        d[0] = 3.214;
        d[1] = 4.238;
        transferDouble(this);

        for (int i = 0; i < d.length; i++) {
            System.out.println(">>----->" + d[i]);
        }
    }

    double[] d = new double[2];
    double d2 = 0.2342;
    Article article = null;
    Double dd = new Double(10.0301);
    List<Double> list = new ArrayList<>();
    Map<Double, List<Double>> m = new HashMap<>();

    public double getRoundingValue(double in, int scale) {
        BigDecimal bd = new BigDecimal(in);
        return bd.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public double getRoundingValue(double in) {
        return getRoundingValue(in, 2);
    }

    public void transferDouble(Object obj) {
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            try {
                // 基本类型、包装类型
                String typeName = field.getType().getSimpleName();
                if ("double".equals(typeName)) {
                    double d = field.getDouble(obj);
                    field.setDouble(obj, getRoundingValue(d));
                } else if ("java.lang.Double".equals(typeName)) {
                    Double d = (Double) field.get(obj);
                    if (d != null) {
                        field.set(obj, getRoundingValue(d.doubleValue()));
                    }
                } else if ("double[]".equals(typeName)) {
                    double[] ds = (double[]) field.get(obj);
                    for (int i = 0; i < ds.length; i++) {
                        ds[i] = getRoundingValue(ds[i]);
                    }
                    field.set(obj, ds);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
