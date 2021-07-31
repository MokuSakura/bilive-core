package org.mokusakura.bilive.core;

import lombok.extern.slf4j.Slf4j;
import org.mokusakura.bilive.core.model.AbstractDanmaku;

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
@Slf4j
public class BasicDanmakuWriter implements DanmakuWriter {
    private final ReadWriteLock outputStreamReadWriteLock;
    private final ExecutorService threadPool;
    private File file;
    private XMLStreamWriter xmlWriter;


    public BasicDanmakuWriter() {
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
                switch (danmaku.getDanmakuType()) {
                    case Comment:
                        xmlWriter.writeStartElement("d");
                        break;
                    case GiftSend:
                        xmlWriter.writeStartElement("gift");
                        break;
                    case SuperChat:
                        xmlWriter.writeStartElement("sc");
                        break;
                    case GuardBuy:
                        xmlWriter.writeStartElement("guard");
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
            outputStreamReadWriteLock.writeLock().lock();
            if (xmlWriter != null) {
                return;
            }
            file = new File(path);
            if (file.exists()) {
                throw new FileAlreadyExistsException(path);
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
