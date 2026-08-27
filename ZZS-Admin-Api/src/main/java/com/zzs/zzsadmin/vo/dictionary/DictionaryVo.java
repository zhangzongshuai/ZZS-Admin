package com.zzs.zzsadmin.vo.dictionary;

import lombok.Data;

import java.util.Date;

/**
 * <p></p>
 *
 * @author zzs
 * @date 2022-12-22 14:12:47
 */
@Data
public class DictionaryVo {
    private String id;

    private String name;

    private String code;

    private Integer isEnabled;

    private String remark;
}
