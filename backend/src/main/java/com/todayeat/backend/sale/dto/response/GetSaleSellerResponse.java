package com.todayeat.backend.sale.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(name = "GetSaleToSellerResponse", description = "판매자용 판매 조회")
public class GetSaleSellerResponse {

    @Schema(description = "판매 ID", example = "1")
    private Long saleId;

    @Schema(description = "메뉴 ID", example = "1")
    private Long menuId;

    @Schema(description = "판매 이미지 url", example = "https://todayeat-bucket.s3.ap-northeast-2.amazonaws.com/seller/1/menu-image/uuid.png")
    private String imageUrl;

    @Schema(description = "판매 이름", example = "마라샹궈")
    private String name;

    @Schema(description = "판매 원가", example = "20000")
    private Integer originalPrice;

    @Schema(description = "판매 판매가", example = "16000")
    private Integer sellPrice;

    @Schema(description = "판매 할인율", example = "20")
    private Integer discountRate;

    @Schema(description = "판매 내용", example = "분모자 옥수수면 소고기")
    private String content;

    @Schema(description = "판매 남은 수량", example = "3")
    private Integer restStock;

    @Schema(description = "판매 종료 여부", example = "false")
    private Boolean isFinished;

    @Builder
    private GetSaleSellerResponse(Long saleId, Long menuId, String imageUrl, String name, Integer originalPrice, Integer sellPrice, Integer discountRate, String content, Integer restStock, Boolean isFinished) {
        this.saleId = saleId;
        this.menuId = menuId;
        this.imageUrl = imageUrl;
        this.name = name;
        this.originalPrice = originalPrice;
        this.sellPrice = sellPrice;
        this.discountRate = discountRate;
        this.content = content;
        this.restStock = restStock;
        this.isFinished = isFinished;
    }
}
