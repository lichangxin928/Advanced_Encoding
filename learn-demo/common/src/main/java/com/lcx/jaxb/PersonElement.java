package com.lcx.jaxb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author : lichangxin
 * @create : 2024/5/22 9:47
 * @description
 */

@XmlRootElement(name = "person")
@XmlAccessorType(XmlAccessType.FIELD)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PersonElement {

    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "age")
    private int age;
}
