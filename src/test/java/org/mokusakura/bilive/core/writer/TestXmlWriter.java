package org.mokusakura.bilive.core.writer;

import org.junit.jupiter.api.Test;
import org.mokusakura.bilive.core.model.CommentMessage;
import org.mokusakura.bilive.core.util.PropertiesUtils;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * @author MokuSakura
 */

public class TestXmlWriter {

    @Test
    public void testEnable() {
        try {
            XmlMessageWriter danmakuWriter = new XmlMessageWriter();
            Properties properties = new Properties();
            properties.put("path", "C:\\Users\\MokuSakura\\Desktop\\test\\fa\\13414514.xml.gz");
            properties.put("roomId", "114514");
            properties.put("lineSeparate", true);
            properties.put("time", System.currentTimeMillis());
            properties.put("compress", true);
            properties.put("replaceFile", true);
            properties.put("extraMsg", "这是一个测试");
            properties.put("enableRaw", true);
            danmakuWriter.enable(properties);
            String json = "{\"info\":[[0,4,25,14893055,1623586486675,662327081,0,\"88920f90\",0,0,5,\"#1453BAFF,#4C2263A2,#3353BAFF\"],\"（￣▽￣）\",[415565,\"鈴羽カルペ・ディエム\",1,0,0,10000,1,\"#00D1F1\"],[27,\"水溅跃\",\"Liyuu_\",5265,398668,\"\",0,6809855,398668,6850801,3,1,4549624],[24,0,5805790,\">50000\",1],[\"\",\"\"],0,3,null,{\"ct\":\"CAB2F936\",\"ts\":1623586486},0,0,null,null,0,105]}";
            for (int i = 0; i < 1000; i++) {
                danmakuWriter.write(CommentMessage.createFromJson(json));
            }
            danmakuWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProperties() {
        try {
            PropertiesUtils properties = new PropertiesUtils();
            properties.load(
                    new FileInputStream("D:\\Code\\Java\\bilive-core\\src\\main\\resources\\xmlWriter.properties"));
            System.out.println(properties.getProperty("element.interactWord", "iw").getClass().getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
