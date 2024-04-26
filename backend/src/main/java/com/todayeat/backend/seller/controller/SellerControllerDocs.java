package com.todayeat.backend.seller.controller;

import com.todayeat.backend._common.response.success.SuccessResponse;
import com.todayeat.backend.seller.dto.request.CheckEmailRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "판매자 Controller")
@RequestMapping("/api/sellers")
public interface SellerControllerDocs {

    @Operation(summary = "아이디(이메일) 사용 가능 여부 확인")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @PostMapping()
    public SuccessResponse<Boolean> checkEmail(@RequestBody @Valid CheckEmailRequest checkEmailRequest);
}
