package com.yql.oidc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Package com.yql.oidc.dto
 * @ClassName ClientIdAndSecretDTO
 * @Description TODO
 * @Author Ryan
 * @Date 3/20/2023
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
public class ClientIdAndSecretDTO {
    private String clientId;
    private String clientSecret;
}
