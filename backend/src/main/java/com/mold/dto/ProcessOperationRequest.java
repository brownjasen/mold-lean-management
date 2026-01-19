package com.mold.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProcessOperationRequest {

    @NotNull(message = "操作类型不能为空")
    private String operation; // start, complete

    @Size(max = 200, message = "备注最多200个字符")
    private String remark;
}
