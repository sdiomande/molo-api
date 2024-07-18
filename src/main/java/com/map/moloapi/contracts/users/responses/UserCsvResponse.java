package com.map.moloapi.contracts.users.responses;

import com.map.moloapi.dtos.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author DIOMANDE Souleymane
 * @Date 20/11/2022 11:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCsvResponse {
    private int uploaded;
    private UserCsvResponseDetail created;
    private UserCsvResponseDetail failed;
//    private int uploaded;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UserCsvResponseDetail {
        private int number;
        private List<UserDto> data;
    }
}
