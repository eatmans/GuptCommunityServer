package net.gupt.community.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * Description 图片实体类 <br/>
 *
 * @author YG <br/>
 * @date 2019/10/8 17:07<br/>
 */
@Data
@Accessors(chain = true)
public class Img implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String imgUrl;
    private Byte articleType;
    private Integer articleId;
}