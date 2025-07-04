package com.lcx.utils;

import com.lcx.jaxb.PersonElement;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;

/**
 * @author : lichangxin
 * @create : 2024/5/22 9:52
 * @description
 */
public class XmlUtils {
    public static File genterateXmlFile(String xmlPath,String xmlName, Object object, Class<?> clazz) throws JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance(clazz);

        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); // 格式化输出

        File xmlFile = new File(xmlPath + xmlName);
        marshaller.marshal(object, xmlFile); // 将对象写入文件
        return xmlFile;
    }


}
