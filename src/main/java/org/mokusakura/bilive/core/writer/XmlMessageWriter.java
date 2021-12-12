package org.mokusakura.bilive.core.writer;

import lombok.extern.log4j.Log4j2;
import org.mokusakura.bilive.core.model.GenericBilibiliMessage;
import org.mokusakura.bilive.core.model.MessageType;
import org.mokusakura.bilive.core.util.PropertiesUtils;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.zip.GZIPOutputStream;

/**
 * @author MokuSakura
 */
@Log4j2
public class XmlMessageWriter extends FileBasedWriter implements MessageWriter {
    private final Lock lock;
    private XMLStreamWriter xmlWriter;
    private Long roomId;
    private Date beginTime;
    private Boolean enableRaw;
    private String commentTag;
    private String giftTag;
    private String guardTag;
    private String scTag;
    private String iwTag;
    private String timeFormat;
    private String extraMessage;
    private OutputStream outputStream;
    private File file;
    private Boolean lineSeparate;


    public XmlMessageWriter() {
        lock = new ReentrantLock();
    }

    @Override
    public void write(GenericBilibiliMessage danmaku) {
        try {
            lock.lock();
            if (xmlWriter == null) {
                return;
            }
            String type = danmaku.getMessageType();
            String tag;
            if (MessageType.COMMENT.equals(type)) {
                tag = commentTag;
            } else if (MessageType.GIFT_SEND.equals(type)) {
                tag = giftTag;
            } else if (MessageType.GUARD_BUY.equals(type)) {
                tag = guardTag;
            } else if (MessageType.SUPER_CHAT.equals(type)) {
                tag = scTag;
            } else if (MessageType.INTERACT_WORD.equals(type)) {
                tag = iwTag;
            } else {
                return;
            }
            if (tag == null) {
                return;
            }
            xmlWriter.writeStartElement(tag);
            if (enableRaw) {
                xmlWriter.writeAttribute("raw", danmaku.getRawMessage());
            }
            xmlWriter.writeEndElement();
            writeSeparator();
            xmlWriter.flush();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * @param properties properties that can be used in this class.
     *                   Supported properties:
     *                   <p>
     *                   - roomId: long, mandatory
     *                   Bilibili live room id.
     *                   <p>
     *                   - time: long or java.util.Date, mandatory
     *                   Live begin time.
     *                   <p>
     *                   - path: String, mandatory
     *                   File that will store all danmaku will be created in this path.
     *                   If the file doesn't exists,
     *                   new file will be created as well as all directories.
     *                   <p>
     *                   - bufferSize: String, default=8k
     *                   Buffer size. Should be integer or
     *                   integer with k,m or b. default unit is b
     *                   <p>
     *                   - lineSeparate: Boolean, default=false
     *                   Whether to separate each line.
     *                   <p>
     *                   - compress: Boolean, default=false
     *                   If true, file will be compressed as gzip.
     *                   <p>
     *                   - timeFormat: String, default=yyyy/MM/dd HH:mm:ss.SSS
     *                   Time format string to format time.
     *                   <p>
     *                   - extraMsg: String, default=null
     *                   Extra message that need to be stored.
     *                   <p>
     *                   - enableRaw: true or false, default=false.
     *                   If true, raw messaged will be stored in the raw attribute.
     *                   <p>
     *                   - replaceFile: true or false, default=false
     *                   If true and there is an already existing file,
     *                   new file will replace the old one.
     *                   <p>
     *                   - element.comment: string, default=d
     *                   element name that will store comment.
     *                   If null value is specified, comment will not be stored.
     *                   <p>
     *                   - element.gift: string, default=gift
     *                   element name that will store gift.
     *                   If null, gift will not be stored.
     *                   <p>
     *                   - element.guard: string, default=guard
     *                   element name that will store guard.
     *                   If null value is specified, guard will not be stored.
     *                   <p>
     *                   - element.superChat: string, default=sc
     *                   element name that will store super chat.
     *                   If null value is specified, super chat will not be stored.
     *                   <p>
     *                   - element.interactWord: string, default=iw
     *                   element name that will store interact word.
     *                   If null value is specified, interact word will not be stored.
     * @return true if successfully enabled else false
     * @throws FileAlreadyExistsException if file already exists.
     * @throws IOException                if fail to create file or directories.
     * @throws IllegalArgumentException   if missing argument(s) or wrong argument type.
     * @see FileBasedWriter#resolveFileProperties(PropertiesUtils)
     */
    @Override
    public boolean enable(Properties properties) throws IOException {
        try {
            lock.lock();
            if (xmlWriter != null) {
                return false;
            }
            resolveProperties(properties);
            file = new File(path);
            String parentPath = path.substring(0, path.lastIndexOf(File.separator));
            File parentDir = new File(parentPath);

            if (file.exists()) {
                if (this.replaceFile) {
                    if (!file.delete()) {
                        throw new IOException("Error deleting old file");
                    }
                } else {
                    throw new FileAlreadyExistsException(path);
                }
            }

            if (!parentDir.exists() && !parentDir.mkdirs()) {
                throw new IOException("Error creating dir " + parentPath);
            }
            if (!file.createNewFile()) {
                throw new IOException("Error creating file " + path);
            }
            log.info("Create file {}", path);
            outputStream = new BufferedOutputStream(new FileOutputStream(file), bufferSize);
            if (compress) {
                outputStream = new GZIPOutputStream(outputStream, true);
            }
            xmlWriter = XMLOutputFactory
                    .newDefaultFactory()
                    .createXMLStreamWriter(outputStream);

            xmlWriter.writeStartDocument();
            writeSeparator();

            xmlWriter.writeStartElement("room-id");
            xmlWriter.writeCharacters(roomId.toString());
            xmlWriter.writeEndElement();
            writeSeparator();

            xmlWriter.writeStartElement("time");
            SimpleDateFormat dateFormat = new SimpleDateFormat(timeFormat);
            xmlWriter.writeCharacters(dateFormat.format(beginTime));
            xmlWriter.writeEndElement();
            writeSeparator();

            xmlWriter.writeStartElement("extra-msg");
            xmlWriter.writeCharacters(extraMessage);
            xmlWriter.writeEndElement();
            writeSeparator();

            xmlWriter.writeStartElement("i");
            writeSeparator();

        } catch (XMLStreamException e) {
            log.error("{}{}{}", e.getMessage(), System.lineSeparator(), Arrays.toString(e.getStackTrace()));
        } finally {
            lock.unlock();
        }
        return true;
    }

    protected boolean writeSeparator() {
        if (this.lineSeparate) {
            return doWriteSeparator();
        }
        return false;
    }

    protected boolean doWriteSeparator() {
        try {
            xmlWriter.writeCharacters(System.lineSeparator());
            return true;
        } catch (XMLStreamException ignored) {
            return false;
        }
    }

    protected void resolveProperties(Properties p) {
        PropertiesUtils properties = new PropertiesUtils(p);
        resolveFileProperties(properties);
        roomId = properties.getLongProperty("roomId");
        Object time = properties.get("time");
        if (time == null) {
            throw new IllegalArgumentException(MessageFormat.format("{0} is not specified", "time"));
        }
        if (time instanceof Date) {
            beginTime = (Date) time;
        } else if (time instanceof Long) {
            beginTime = new Date((Long) time);
        }
        lineSeparate = properties.getBooleanProperty("lineSeparate", false);
        extraMessage = properties.getStringPropertyResolveNull("extraMsg", null);
        timeFormat = properties.getStringProperty("timeFormat", "yyyy/MM/dd HH:mm:ss.SSS");
        enableRaw = properties.getBooleanProperty("enableRaw", false);
        commentTag = properties.getStringPropertyResolveNull("element.comment", "d");
        giftTag = properties.getStringPropertyResolveNull("element.gift", "gift");
        guardTag = properties.getStringPropertyResolveNull("element.guard", "guard");
        scTag = properties.getStringPropertyResolveNull("element.superChat", "sc");
        iwTag = properties.getStringPropertyResolveNull("element.interactWord", "iw");
    }

    @Override
    public void close() throws IOException {
        try {
            lock.lock();
            if (xmlWriter == null) {
                return;
            }
            xmlWriter.writeEndElement();
            xmlWriter.flush();
            xmlWriter.close();
            xmlWriter = null;
            outputStream.close();
            //noinspection ResultOfMethodCallIgnored
            file.setReadOnly();


        } catch (XMLStreamException e) {
            throw new IOException(e);
        } finally {
            file = null;
            xmlWriter = null;
            lock.unlock();
        }
    }
}
