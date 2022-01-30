package com.autotrade.connector.component;

import com.autotrade.connector.config.ConnectionSettings;
import com.autotrade.connector.model.ConnectionProfile;
import com.autotrade.connector.model.callback.Callback;
import com.autotrade.connector.model.command.Command;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;

@Component
@Slf4j
public class Utils {
    private final XmlMapper xmlMapper;
    private final ObjectMapper objectMapper;
    private final DocumentBuilder documentBuilder;
    private final ConnectionSettings connectionSettings;

    public Utils(ObjectMapper objectMapper, ConnectionSettings connectionSettings) throws ParserConfigurationException {
        this.objectMapper = objectMapper;
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        this.connectionSettings = connectionSettings;

        this.xmlMapper = new XmlMapper();
        this.xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        this.xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        this.xmlMapper.registerModule(new JavaTimeModule());

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        documentBuilder = dbf.newDocumentBuilder();
    }

    public XmlMapper getXmlMapper() {
        return xmlMapper;
    }

    // DocumentBuilder - not thread safe
    // http://www.javaroots.com/2014/08/jaxb-error-fwk005-parse-may-not-be.html
    public String getRootElementName1(String xmlData) {
        try {
//        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//        DocumentBuilder db = dbf.newDocumentBuilder();
            InputStream is = new ByteArrayInputStream(xmlData.getBytes());
            Document doc = documentBuilder.parse(is);
            Element root = doc.getDocumentElement();
            return root.getNodeName();
        }
        catch (IOException | SAXException e) {
            log.error("parse: " + xmlData);
            e.printStackTrace();
            return null;
        }
    }

    public String getRootElementName(String xmlData) {
        try {
            int openBracketIndex = xmlData.indexOf("<");
            int closeBracketIndex = xmlData.indexOf(">");
            return xmlData.substring(openBracketIndex + 1, closeBracketIndex).split(" ")[0];
        }
        catch (Exception e) {
            log.error("root xml element not found: " + xmlData);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public String getRootElementName3(String xmlData) throws JsonProcessingException {
        XmlMapper xmlMapper = new XmlMapper();
        JsonNode jsonNode = xmlMapper.readTree(xmlData);

        return null;
    }

    public <T extends Command> String serializeCommand(T command) throws IOException {
        return xmlMapper.writeValueAsString(command);
    }

    public <T> T deserializeCallback(String xmlData, Class<T> clazz) {
        try {
            return xmlMapper.readValue(xmlData, clazz);
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T extends Callback> T deserializeCallbackFlux(String xmlData, Class<T> clazz) {
        try {
            return xmlMapper.readValue(xmlData, clazz);
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }


    //TODO нужно ли очищать выделенную память вручную
    // https://java-native-access.github.io/jna/4.2.0/com/sun/jna/Memory.html
    public Pointer stringToPointer(String data) {
        Pointer pointer = new Memory((long) (data.length() + 1) * Native.WCHAR_SIZE);
        pointer.setString(0, data, "UTF-8");
        return pointer;
    }

    public String pointerToString(Pointer pointer) {
        return pointer.getString(0, "UTF-8");
    }

    public ConnectionProfile getConnectionProfile() throws IOException {
        URL url = Path.of(connectionSettings.getConfigPath()).toUri().toURL();
        return objectMapper.readValue(url, ConnectionProfile.class);
    }

    public ConnectionProfile getDemoConnectionProfile() throws IOException {
        URL url = Path.of(connectionSettings.getDemoConfigPath()).toUri().toURL();
        return objectMapper.readValue(url, ConnectionProfile.class);
    }
}
