package com.supermap.gaf.authority.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

/**
 * @Description: 接收前端变更邮箱的参数
 * @Author wxl
 * @Date 2021/5/31 14:34
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("接收前端变更邮箱的参数")
public class EmailChangeVo {
    String oldEmailCheckCode;
    @Email
    String newEmail;
    String newEmailCheckCode;
}
