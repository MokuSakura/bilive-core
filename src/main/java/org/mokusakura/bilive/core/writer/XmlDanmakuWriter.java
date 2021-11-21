package org.mokusakura.bilive.core.writer;

import lombok.extern.log4j.Log4j2;
import org.mokusakura.bilive.core.model.AbstractDanmaku;
import org.mokusakura.bilive.core.model.MessageType;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.Arrays;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author MokuSakura
 */
@Log4j2
public class XmlDanmakuWriter implements DanmakuWriter {
    private final ReadWriteLock outputStreamReadWriteLock;
    private final ExecutorService threadPool;
    private File file;
    private XMLStreamWriter xmlWriter;


    public XmlDanmakuWriter() {
        outputStreamReadWriteLock = new ReentrantReadWriteLock();
        threadPool = new ThreadPoolExecutor(100, 100, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));
    }

    @Override
    public synchronized Future<Void> writeAsync(AbstractDanmaku danmaku) {
        return threadPool.submit(() -> {
            try {
                outputStreamReadWriteLock.writeLock().lock();
                if (xmlWriter == null) {
                    return null;
                }
                switch (danmaku.getMessageType()) {
                    case MessageType.Comment:
                        xmlWriter.writeStartElement("d");
                        break;
                    case MessageType.GiftSend:
                        xmlWriter.writeStartElement("gift");
                        break;
                    case MessageType.SuperChat:
                        xmlWriter.writeStartElement("sc");
                        break;
                    case MessageType.GuardBuy:
                        xmlWriter.writeStartElement("guard");
                        break;
                    case MessageType.InteractWord:
                        xmlWriter.writeStartElement("iw");
                        break;
                    default:
                        return null;
                }
                xmlWriter.writeAttribute("raw", danmaku.getRawMessage());
                xmlWriter.writeEndElement();
                xmlWriter.writeCharacters(System.lineSeparator());
                xmlWriter.flush();
            } finally {
                outputStreamReadWriteLock.writeLock().unlock();
            }
            return null;
        });
    }

    @Override
    public void enable(String path) throws IOException {
        try {
            if (path.endsWith(File.separator)) {
                path = path.substring(0, path.length() - 1 - File.separator.length());
            }
            String parentPath = path.substring(0, path.lastIndexOf(File.separator));
            outputStreamReadWriteLock.writeLock().lock();
            if (xmlWriter != null) {
                return;
            }
            file = new File(path);
            File parentDir = new File(parentPath);
            if (file.exists()) {
                throw new FileAlreadyExistsException(path);
            }
            if (!parentDir.exists() && !parentDir.mkdirs()) {
                throw new IOException("Error creating dir " + parentPath);
            }
            if (!file.createNewFile()) {
                throw new IOException("Error creating file " + path);
            }
            log.info("Create file {}", path);
            xmlWriter = XMLOutputFactory.newDefaultFactory().createXMLStreamWriter(new FileOutputStream(file));
            xmlWriter.writeStartDocument();
            xmlWriter.writeCharacters(System.lineSeparator());
            xmlWriter.writeStartElement("i");
            xmlWriter.writeCharacters(System.lineSeparator());

        } catch (XMLStreamException e) {
            log.error("{}{}{}", e.getMessage(), System.lineSeparator(), Arrays.toString(e.getStackTrace()));
        } finally {
            outputStreamReadWriteLock.writeLock().unlock();
        }
    }

    @Override
    public void close() throws IOException {
        try {
            outputStreamReadWriteLock.writeLock().lock();
            if (xmlWriter == null) {
                return;
            }
            xmlWriter.writeEndElement();
            //noinspection ResultOfMethodCallIgnored
            file.setReadOnly();
            xmlWriter.close();

        } catch (XMLStreamException e) {
            throw new IOException(e);
        } finally {
            file = null;
            xmlWriter = null;
            outputStreamReadWriteLock.writeLock().unlock();
        }
    }
}