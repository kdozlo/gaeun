package com.todayeat.backend.order.dto.response.consumer;

import com.todayeat.backend.order.entity.OrderInfo;
import com.todayeat.backend.order.entity.OrderInfoItem;
import com.todayeat.backend.store.entity.Store;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Getter
@Schema(name = "GetOrderConsumerResponse", description = "소비자 주문 조회 응답")
public class GetOrderConsumerResponse {

    @Schema(description = "주문 고유번호", example = "1")
    private Long orderInfoId;

    @Schema(description = "주문 내용", example = "마라샹궈 1개 외 2건")
    private String orderContents;

    @Schema(description = "주문 가격", example = "10000")
    private Integer orderPrice;

    @Schema(description = "주문 상태", example = "진행 중")
    private String orderStatus;

    @Schema(description = "주문 날짜", example = "2024.05.14(화)")
    private String orderDate;

    @Schema(description = "가게 고유번호", example = "1")
    private Long storeId;

    @Schema(description = "가게 이름", example = "마라조아")
    private String storeName;

    @Schema(description = "가게 전화번호", example = "01012345678")
    private String storeTel;

    @Builder
    private GetOrderConsumerResponse(Long orderInfoId, String orderContents, Integer orderPrice, String orderStatus, String orderDate, Long storeId, String storeName, String storeTel) {
        this.orderInfoId = orderInfoId;
        this.orderContents = orderContents;
        this.orderPrice = orderPrice;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeTel = storeTel;
    }

    public static GetOrderConsumerResponse from(OrderInfo orderInfo) {

        Store store = orderInfo.getStore();

        return builder()
                .orderInfoId(orderInfo.getId())
                .orderContents(getContents(orderInfo.getOrderInfoItemList()))
                .orderPrice(orderInfo.getPaymentPrice())
                .orderStatus(orderInfo.getStatus().getDescription())
                .orderDate(getDate(orderInfo.getCreatedAt()))
                .storeId(store.getId())
                .storeName(store.getName())
                .storeTel(store.getTel())
                .build();
    }

    private static String getContents(List<OrderInfoItem> orderInfoItems) {

        OrderInfoItem firstItem = orderInfoItems.getFirst();

        StringBuilder sb =  new StringBuilder();
        sb.append(firstItem.getName()).append(" ").append(firstItem.getQuantity()).append("개");

        if (orderInfoItems.size() == 1) {
            return sb.toString();
        }

        sb.append(" 외 ").append(orderInfoItems.size() - 1).append("건");
        return sb.toString();
    }

    private static String getDate(LocalDateTime localDateTime) {

        StringBuilder sb =  new StringBuilder();

        String date = localDateTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        String dayOfWeek = localDateTime.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);

        sb.append(date).append("(").append(dayOfWeek).append(")");
        return sb.toString();
    }
}